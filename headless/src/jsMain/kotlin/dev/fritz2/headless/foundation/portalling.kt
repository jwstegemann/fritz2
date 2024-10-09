package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.w3c.dom.*


private val portalRootId by lazy { "portal-root".also { addGlobalStyle("#$it { display: contents; }") } }

private object PortalStack : RootStore<List<PortalContainer<out HTMLElement>>>(emptyList(), job = Job()) {
    val add = handle<PortalContainer<out HTMLElement>> { stack, it -> stack + it }
}

private data class PortalContainer<C : HTMLElement>(
    val classes: String?,
    val id: String?,
    val scope: (ScopeContext.() -> Unit),
    val tag: TagFactory<Tag<C>>,
    val reference: MountPoint?,
    val content: Tag<C>.(remove: suspend (Unit) -> Unit) -> Unit
) {
    /**
     * used as ID-provider for the rendering of `PortalStack.data`
     */
    val portalId = Id.next()

    val remove = PortalStack.handle { list -> list.filterNot { it.portalId == portalId } }

    fun render(ctx: RenderContext) =
        tag(ctx, classes, id, scope + { ctx.scope[MOUNT_POINT_KEY]?.let { set(MOUNT_POINT_KEY, it) } }) {
            scope[SHOW_COMPONENT_STRUCTURE]?.let {
                if (it) attr("data-portal-id", portalId)
            }
            content.invoke(this) { remove.invoke() }
            reference?.beforeUnmount(this, null) { _, _ -> remove.invoke() }
        }
}

/**
 * A [portalRoot] is needed to use floating components like [dev.fritz2.headless.components.modal],
 * [dev.fritz2.headless.components.toast] and [dev.fritz2.headless.components.popOver].
 * Basically all components based upon [PopUpPanel].
 *
 * Should be the last element in `document.body` to ensure it will not be clipped by other elements.
 *
 * @see portal
 */
fun RenderContext.portalRoot(scopeContext: (ScopeContext.() -> Unit) = {}): RenderContext {
    addComponentStructureInfo(portalRootId, this.scope, this)
    register(PortalRenderContext.withScope(scopeContext + scope)) {}
    return PortalRenderContext
}

internal object PortalRenderContext : HtmlTag<HTMLDivElement>("div", portalRootId, null, Job(), Scope()) {

    var scopeContext: ScopeContext.() -> Unit = {}

    fun withScope(scopeContext: ScopeContext.() -> Unit): PortalRenderContext = apply {
        this.scopeContext = scopeContext + scope
    }

    init {
        attr(Aria.live, "polite")

        PortalStack.data.distinctUntilChangedBy { it.map { it.portalId } }
            .renderEach(PortalContainer<*>::portalId, into = this) {
                it.render(this)
            }

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
fun <C : HTMLElement> RenderContext.portal(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    content: Tag<C>.(close: suspend (Unit) -> Unit) -> Unit = {}
) {
    val portalId = id ?: Id.next()

    /**
     *  toasts and modals are rendered directly into the PortalRenderContext, they do not need a reference.
     *  To be more precise: They do not have any valid reference, as for example the modal is rendered completely
     *  agnostic from the fritz2 controlled `RenderContext`.
     */
    val reference = if (this != PortalRenderContext) this else null

    PortalStack.add(
        PortalContainer(
            classes = classes,
            id = portalId,
            scope = this.scope + scope,
            tag = tag,
            reference = reference?.mountPoint(),
            content = content
        )
    )
}

/**
 * @see portal
 */
fun RenderContext.portal(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    content: Tag<HTMLDivElement>.(close: suspend (Unit) -> Unit) -> Unit,
) = portal(classes, id, scope, RenderContext::div, content)
