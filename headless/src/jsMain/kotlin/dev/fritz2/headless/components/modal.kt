package dev.fritz2.headless.components

import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.identification.Id
import org.w3c.dom.HTMLElement

class HeadlessModal(val renderContext: RenderContext) : RenderContext by renderContext,
    OpenClose by OpenCloseDelegate() {

    var restoreFocus: Boolean = true
    var setInitialFocus: Boolean = true

    private var panel: (RenderContext.() -> HtmlTag<HTMLElement>)? = null

    fun render() {
        opened.render {
            if (it) {
                panel?.invoke(this)!!.apply {
                    trapFocus(restoreFocus, setInitialFocus)
                }
            }
        }
    }

    inner class ModalPanel<C : HtmlTag<HTMLElement>>(
        val tag: C,
        private val id: String? = null
    ) : RenderContext by tag {
        val componentId: String by lazy { id ?: Id.next() }

        private var title: HtmlTag<HTMLElement>? = null
        private var description: HtmlTag<HTMLElement>? = null

        fun C.render() {
            attr("id", componentId)
            attr("role", Aria.Role.dialog)
            attr(Aria.modal, "true")
            title?.let { attr(Aria.labelledby, it.id) }
            description?.let { attr(Aria.describedby, it.id) }
        }

        fun <CO : HtmlTag<HTMLElement>> RenderContext.modalOverlay(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CO>,
            content: CO.() -> Unit
        ) = tag(this, classes, "$componentId-overlay", scope) {
            attr(Aria.hidden, "true")
            content()
        }

        fun RenderContext.modalOverlay(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Div.() -> Unit
        ) = modalOverlay(classes, scope, RenderContext::div, content)

        fun <CT : HtmlTag<HTMLElement>> RenderContext.modalTitle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CT>,
            content: CT.() -> Unit
        ) = tag(this, classes, "$componentId-title", scope, content).also { title = it }

        fun RenderContext.modalTitle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: H.() -> Unit
        ) = modalTitle(classes, scope, RenderContext::h2, content)

        fun <CD : HtmlTag<HTMLElement>> RenderContext.modalDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CD>,
            content: CD.() -> Unit
        ) = tag(this, classes, "$componentId-description", scope, content).also { description = it }

        fun RenderContext.modalDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: P.() -> Unit
        ) = modalDescription(classes, scope, RenderContext::p, content)
    }

    fun <C : HtmlTag<HTMLElement>> RenderContext.modalPanel(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<C>,
        initialize: ModalPanel<C>.() -> Unit
    ) {
        panel = {
            tag(this, classes, id, internalScope) {
                ModalPanel(this).run {
                    initialize()
                    render()
                }
            }
        }
    }

    fun RenderContext.modalPanel(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: ModalPanel<Div>.() -> Unit
    ) = modalPanel(classes, id, internalScope, RenderContext::div, initialize)
}


fun RenderContext.headlessModal(
    initialize: HeadlessModal.() -> Unit
) = HeadlessModal(this).run {
    initialize(this)
    render()
}
