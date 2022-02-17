package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class PopOver<C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag, OpenClose() {

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
        handleOpenCloseEvents()
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
    ) : PopUpPanel<C>(renderContext, tagFactory, classes, "$componentId-items", scope, this@PopOver, button)

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

fun <C : HTMLElement> RenderContext.popOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: PopOver<C>.() -> Unit
): Tag<C> = tag(this, classes(classes, "relative"), id, scope) {
    PopOver(this, id).run {
        initialize(this)
        render()
    }
    trapFocus()
}

fun RenderContext.popOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: PopOver<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = popOver(classes, id, scope, RenderContext::div, initialize)
