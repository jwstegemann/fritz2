package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.hooks.hook
import dev.fritz2.identification.Id
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class HeadlessDisclosure<C : HTMLElement>(tag: Tag<C>, id: String?) :
    Tag<C> by tag,
    OpenClose by OpenCloseDelegate() {

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
        hook(openClose)
    }.also { button = it }

    fun RenderContext.disclosureButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = disclosureButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class DisclosurePanel<CP : HTMLElement>(val tag: Tag<CP>) : Tag<CP> by tag {
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

fun <C : HTMLElement> RenderContext.headlessDisclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessDisclosure<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    HeadlessDisclosure(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessDisclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessDisclosure<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = headlessDisclosure(classes, id, scope, RenderContext::div, initialize)
