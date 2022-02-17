package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.OpenClose
import dev.fritz2.headless.foundation.TagFactory
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class Disclosure<C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag, OpenClose() {

    val componentId: String by lazy { id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null
    private var panel: (RenderContext.() -> Unit)? = null

    fun render() {
        attr("id", componentId)
        opened.render {
            if (it) panel?.invoke(this)
        }
    }

    fun <CB : HTMLElement> RenderContext.disclosureButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CB>>,
        content: Tag<CB>.() -> Unit
    ) = tag(this, classes, "$componentId-button", scope) {
        if (!openClose.isSet) openClose(storeOf(false))
        content()
        attr(Aria.expanded, opened.asString())
        attr("tabindex", "0")
        handleOpenCloseEvents()
    }.also { button = it }

    fun RenderContext.disclosureButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = disclosureButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class DisclosurePanel<CP : HTMLElement>(tag: Tag<CP>) : Tag<CP> by tag {
        fun render() {
            button?.let { button -> button.attr(Aria.controls, id.whenever(opened)) }
        }

        fun <CC : HTMLElement> RenderContext.disclosureCloseButton(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CC>>,
            content: Tag<CC>.() -> Unit
        ) = tag(this, classes, "$componentId-close-button", scope) {
            content()
            clicks handledBy close
        }

        fun RenderContext.disclosureCloseButton(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLButtonElement>.() -> Unit
        ) = disclosureCloseButton(classes, scope, RenderContext::button, content).apply {
            attr("type", "button")
        }
    }

    fun <CP : HTMLElement> RenderContext.disclosurePanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CP>>,
        initialize: DisclosurePanel<CP>.() -> Unit
    ) {
        panel = {
            tag(this, classes, "$componentId-panel", scope) {
                DisclosurePanel(this).run {
                    initialize()
                    render()
                }
            }
        }
    }

    fun RenderContext.disclosurePanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: DisclosurePanel<HTMLDivElement>.() -> Unit
    ) = disclosurePanel(classes, scope, RenderContext::div, initialize)
}

fun <C : HTMLElement> RenderContext.disclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Disclosure<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    Disclosure(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.disclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Disclosure<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = disclosure(classes, id, scope, RenderContext::div, initialize)
