package dev.fritz2.headless.components

/*
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.headless.foundation.*
import dev.fritz2.identification.Id
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLHeadingElement
import org.w3c.dom.HTMLParagraphElement

class HeadlessModal(val renderContext: RenderContext) : RenderContext by renderContext,
    OpenClose by OpenCloseDelegate() {

    var restoreFocus: Boolean = true
    var setInitialFocus: Boolean = true

    private var panel: (RenderContext.() -> Tag<HTMLElement>)? = null

    fun render() {
        opened.render {
            if (it) {
                panel?.invoke(this)!!.apply {
                    trapFocus(restoreFocus, setInitialFocus)
                }
            }
        }
    }

    inner class ModalPanel<C : HTMLElement>(
        tag: Tag<C>,
        private val explicitId: String? = null
    ) : Tag<C> by tag {
        val componentId: String by lazy { explicitId ?: Id.next() }

        private var title: Tag<HTMLElement>? = null
        private var description: Tag<HTMLElement>? = null

        fun render() {
            attr("id", componentId)
            attr("role", Aria.Role.dialog)
            attr(Aria.modal, "true")
            title?.let { attr(Aria.labelledby, it.id) }
            description?.let { attr(Aria.describedby, it.id) }
        }

        fun <CO : HTMLElement> RenderContext.modalOverlay(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CO>>,
            content: Tag<CO>.() -> Unit
        ) = tag(this, classes, "$componentId-overlay", scope) {
            attr(Aria.hidden, "true")
            content()
        }

        fun RenderContext.modalOverlay(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLDivElement>.() -> Unit
        ) = modalOverlay(classes, scope, RenderContext::div, content)

        fun <CT : HTMLElement> RenderContext.modalTitle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            content: Tag<CT>.() -> Unit
        ) = tag(this, classes, "$componentId-title", scope, content).also { title = it }

        fun RenderContext.modalTitle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLHeadingElement>.() -> Unit
        ) = modalTitle(classes, scope, RenderContext::h2, content)

        fun <CD : HTMLElement> RenderContext.modalDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CD>>,
            content: Tag<CD>.() -> Unit
        ) = tag(this, classes, "$componentId-description", scope, content).also { description = it }

        fun RenderContext.modalDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLParagraphElement>.() -> Unit
        ) = modalDescription(classes, scope, RenderContext::p, content)
    }

    fun <C : HTMLElement> RenderContext.modalPanel(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<C>>,
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
        initialize: ModalPanel<HTMLDivElement>.() -> Unit
    ) = modalPanel(classes, id, internalScope, RenderContext::div, initialize)
}


fun RenderContext.headlessModal(
    initialize: HeadlessModal.() -> Unit
) = HeadlessModal(this).run {
    initialize(this)
    render()
}


 */