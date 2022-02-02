package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.hooks.hook
import dev.fritz2.identification.Id
import dev.fritz2.utils.classes
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class HeadlessPopOver<C : HTMLElement>(val tag: Tag<C>, id: String?) : Tag<C> by tag,
    OpenClose by OpenCloseDelegate() {

    val componentId: String by lazy { id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null

    fun render() {
    }

    fun <CB : HTMLElement> RenderContext.popOverButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CB>>,
        content: Tag<CB>.() -> Unit
    ) = tag(this, classes, "$componentId-button", scope) {
        if (!openClose.isSet) openClose(storeOf(false))
        content()
        attr(Aria.expanded, opened.asString())
        hook(openClose)
    }.also { button = it }

    fun RenderContext.popOverButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = popOverButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class PopOverPanel<C : HTMLElement>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<Tag<C>>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<C>(renderContext, tagFactory, classes, "$componentId-items", scope, this@HeadlessPopOver, button)

    fun <CP : HTMLElement> RenderContext.popOverPanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CP>>,
        initialize: PopOverPanel<CP>.() -> Unit
    ) {
        PopOverPanel(this, tag, classes, scope).run {
            initialize()
            render()
        }
    }

    fun RenderContext.popOverPanel(
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: PopOverPanel<HTMLDivElement>.() -> Unit
    ) = popOverPanel(classes, internalScope, RenderContext::div, initialize)
}

fun <C : HTMLElement> RenderContext.headlessPopOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessPopOver<C>.() -> Unit
): Tag<C> = tag(this, classes(classes, "relative"), id, scope) {
    HeadlessPopOver(this, id).run {
        initialize(this)
        render()
    }
    trapFocus()
}

fun RenderContext.headlessPopOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessPopOver<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = headlessPopOver(classes, id, scope, RenderContext::div, initialize)
