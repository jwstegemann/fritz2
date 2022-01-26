package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.identification.Id
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.hooks.hook
import org.w3c.dom.HTMLElement

class HeadlessDisclosure<C : Tag<HTMLElement>>(val renderContext: C, id: String?) :
    RenderContext by renderContext,
    OpenClose by OpenCloseDelegate() {

    val componentId: String by lazy { id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null
    private var panel: (RenderContext.() -> Unit)? = null

    fun C.render() {
        attr("id", componentId)
        opened.render {
            if (it) panel?.invoke(this)
        }
    }

    fun <CB : Tag<HTMLElement>> RenderContext.disclosureButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CB>,
        content: CB.() -> Unit
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
        content: Button.() -> Unit
    ) = disclosureButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class DisclosurePanel<CP : Tag<HTMLElement>>(val tag: CP) : RenderContext by tag {
        fun CP.render() {
            button?.let { button -> button.attr(Aria.controls, id.whenever(opened)) }
        }

        fun <CC : Tag<HTMLElement>> RenderContext.disclosureCloseButton(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CC>,
            content: CC.() -> Unit
        ) = tag(this, classes, "$componentId-close-button", scope) {
            content()
            clicks handledBy close
        }

        fun RenderContext.disclosureCloseButton(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Button.() -> Unit
        ) = disclosureCloseButton(classes, scope, RenderContext::button, content).apply {
            attr("type", "button")
        }
    }

    fun <CP : Tag<HTMLElement>> RenderContext.disclosurePanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CP>,
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
        initialize: DisclosurePanel<Div>.() -> Unit
    ) = disclosurePanel(classes, scope, RenderContext::div, initialize)
}

fun <C : Tag<HTMLElement>> RenderContext.headlessDisclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessDisclosure<C>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessDisclosure(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessDisclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessDisclosure<Div>.() -> Unit
): Div = headlessDisclosure(classes, id, scope, RenderContext::div, initialize)
