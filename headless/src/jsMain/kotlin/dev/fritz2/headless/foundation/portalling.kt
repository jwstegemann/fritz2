package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.w3c.dom.*

/**
 * Z-Index used for Portalled-Modals
 *
 * @see portal
 */
const val PORTALLING_MODAL_ZINDEX = 10
/**
 * Z-Index used for Portalled-Popups
 *
 * @see portal
 */
const val PORTALLING_POPUP_ZINDEX = 30
/**
 * Z-Index used for Portalled-Toasts
 *
 * @see portal
 */
const val PORTALLING_TOAST_ZINDEX = 50

private val portalRootId by lazy { "portal-root".also { addGlobalStyle("#$it { display: contents; }") } }

private val portalContainerClass by lazy {
    "portal-container".also {
        addGlobalStyles(
            listOf(
                ".$it { display: contents; }",
                ".$it > * { z-index: inherit; }"
            )
        )
    }
}

private object PortalStack : RootStore<List<PortalContainer<out HTMLElement>>>(emptyList()) {
    val add = handle<PortalContainer<out HTMLElement>> { stack, it -> stack + it }
    val remove = handle<String> { stack, id -> stack.filterNot { it.portalId == id } }
}

private data class PortalContainer<C : HTMLElement>(
    val classes: String?,
    val id: String?,
    val scope: (ScopeContext.() -> Unit),
    val tag: TagFactory<Tag<C>>,
    val zIndex: Int,
    val reference: HTMLElement?,
    val content: Tag<C>.(close: suspend (Unit) -> Unit) -> Unit
) {
    val portalId = Id.next() // used for renderEach only

    val remove = PortalStack.handle { list -> list.filterNot { it.portalId == portalId } }

    fun render(ctx: RenderContext) =
        tag(ctx, classes(portalContainerClass, classes), id, scope) {
            domNode.style.zIndex = zIndex.toString()
            content.invoke(this) { remove.invoke() }
        }
}

/**
 * A [portalRoot] is needed to use floating components like [modal], [toast] and [popupPanel].
 *
 * Should be the last element in `document.body` to ensure it will not be clipped by other elements.
 *
 * @see portal
 */
fun RenderContext.portalRoot(): RenderContext {
    addComponentStructureInfo(portalRootId, this.scope, this)
    register(PortalRenderContext) {}
    return PortalRenderContext
}

internal object PortalRenderContext : HtmlTag<HTMLDivElement>("div", portalRootId, null, Job(), Scope()) {

    private val bodyMutation = callbackFlow {
        val observer = MutationObserver { _, _ -> trySend(Unit) }
        observer.observe(document.body!!, MutationObserverInit(childList = true, subtree = true))
        awaitClose { observer.disconnect() }
    }

    init {
        attr(Aria.live, "polite")

        PortalStack.data.distinctUntilChangedBy { it.map { it.portalId } }
            .renderEach(PortalContainer<*>::portalId, into = this) { it.render(this) }

        bodyMutation.combine(PortalStack.data) { _, stack ->
            stack.firstOrNull { it.reference != null && document.body?.contains(it.reference) == false }
        }.mapNotNull { it?.portalId } handledBy PortalStack.remove

        MainScope().launch {
            delay(500)
            if (domNode.parentNode == null) {
                console.error("you have to create a portalRoot to use portalled components (e.g. popup, modal and toast)")
            }
        }
    }
}

/**
 * With Portalling a rendered overlay will be rendered outside of the clipping ancestors to avoid clipping.
 * Therefore, a [portalRoot] is needed as last element in the document.body.
 *
 * See https://floating-ui.com/docs/misc#clipping for more information.
 *
 * A Portal might have a reference element. When the reference element is removed from the DOM, the portal will either.
 * The reference element is always the receiver Type [Tag<HTMLElement>] of the [portal] extension function.
 */
fun <C : HTMLElement> Tag<HTMLElement>.portal(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    zIndex: Int,
    content: Tag<C>.(close: suspend (Unit) -> Unit) -> Unit = {}
) {
    val portalId = id ?: Id.next()

    // toasts and modals are rendered directly into the PortalRenderContext, they do not need a reference
    val reference = if (this != PortalRenderContext) this else null

    PortalStack.add(
        PortalContainer(
            classes = classes,
            id = portalId,
            scope = scope,
            tag = tag,
            reference = reference?.domNode,
            zIndex = zIndex,
            content = content
        )
    )
}

/**
 * @see portal
 */
fun Tag<HTMLElement>.portal(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    zIndex: Int,
    content: Tag<HTMLDivElement>.(close: suspend (Unit) -> Unit) -> Unit,
) = portal(classes, id, scope, RenderContext::div, zIndex, content)
