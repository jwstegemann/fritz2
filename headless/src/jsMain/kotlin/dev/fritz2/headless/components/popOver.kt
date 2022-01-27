package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.hook
import dev.fritz2.identification.Id
import dev.fritz2.utils.classes
import org.w3c.dom.HTMLElement


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class HeadlessPopOver<C : Tag<HTMLElement>>(val tag: C, id: String?) : RenderContext by tag,
    OpenClose by OpenCloseDelegate() {

    val componentId: String by lazy { id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null

    fun C.render() {
    }

    fun <CB : Tag<HTMLElement>> RenderContext.popOverButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CB>,
        content: CB.() -> Unit
    ) = tag(this, classes, "$componentId-button", scope) {
        if (!openClose.isSet) openClose(storeOf(false))
        content()
        attr(Aria.expanded, opened.asString())
        hook(openClose)
    }.also { button = it }

    fun RenderContext.popOverButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Button.() -> Unit
    ) = popOverButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class PopOverPanel<C : Tag<HTMLElement>>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<C>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<C>(renderContext, tagFactory, classes, "$componentId-items", scope, this@HeadlessPopOver, button)

    fun <CP : Tag<HTMLElement>> RenderContext.popOverPanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CP>,
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
        initialize: PopOverPanel<Div>.() -> Unit
    ) = popOverPanel(classes, internalScope, RenderContext::div, initialize)
}

fun <C : Tag<HTMLElement>> RenderContext.headlessPopOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessPopOver<C>.() -> Unit
): C = tag(this, classes(classes, "relative"), id, scope) {
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
    initialize: HeadlessPopOver<Div>.() -> Unit
): Div = headlessPopOver(classes, id, scope, RenderContext::div, initialize)
