package dev.fritz2.headless.components

import dev.fritz2.core.Id
import dev.fritz2.core.RenderContext
import dev.fritz2.core.ScopeContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.foundation.*
import org.w3c.dom.*

/**
 * This class provides the building blocks to implement a modal.
 *
 * Use [modal] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/modal/)
 */
class Modal(val renderContext: RenderContext) : RenderContext by renderContext, OpenClose() {

    var restoreFocus: Boolean = true
    var setInitialFocus: InitialFocus = InitialFocus.InsistToSet

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
        private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

        fun render() {
            attr("id", componentId)
            attr("role", Aria.Role.dialog)
            attr(Aria.modal, "true")
            title?.let { attr(Aria.labelledby, it.id) }
            attr(Aria.describedby, descriptions.map { d -> d.id }.joinToString(" "))
        }

        /**
         * Factory function to create a [modalOverlay].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/modal/#modaloverlay)
         */
        fun <CO : HTMLElement> RenderContext.modalOverlay(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CO>>,
            content: Tag<CO>.() -> Unit
        ): Tag<CO> {
            addComponentStructureInfo("modalOverlay", this@modalOverlay.scope, this)
            return tag(this, classes, "$componentId-overlay", scope) {
                attr(Aria.hidden, "true")
                content()
            }
        }

        /**
         * Factory function to create a [modalOverlay] with a [HTMLDivElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/modal/#modaloverlay)
         */
        fun RenderContext.modalOverlay(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLDivElement>.() -> Unit
        ) = modalOverlay(classes, scope, RenderContext::div, content)

        /**
         * Factory function to create a [modalTitle].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/modal/#modaltitle)
         */
        fun <CT : HTMLElement> RenderContext.modalTitle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            content: Tag<CT>.() -> Unit
        ): Tag<CT> {
            addComponentStructureInfo("modalTitle", this@modalTitle.scope, this)
            return tag(this, classes, "$componentId-title", scope, content).also { title = it }
        }

        /**
         * Factory function to create a [modalTitle] with a [HTMLHeadingElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/modal/#modaltitle)
         */
        fun RenderContext.modalTitle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLHeadingElement>.() -> Unit
        ) = modalTitle(classes, scope, RenderContext::h2, content)

        /**
         * Factory function to create a [modalDescription].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/modal/#modaldescription)
         */
        fun <CD : HTMLElement> RenderContext.modalDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CD>>,
            content: Tag<CD>.() -> Unit
        ): Tag<CD> {
            addComponentStructureInfo("modalDescription", this@modalDescription.scope, this)
            return tag(
                this,
                classes,
                "$componentId-description-${descriptions.size}",
                scope,
                content
            ).also { descriptions.add(it) }
        }

        /**
         * Factory function to create a [modalDescription] with a [HTMLParagraphElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/modal/#modaldescription)
         */
        fun RenderContext.modalDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLParagraphElement>.() -> Unit
        ) = modalDescription(classes, scope, RenderContext::p, content)
    }

    /**
     * Factory function to create a [modalPanel].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/modal/#modalpanel)
     */
    fun <C : HTMLElement> RenderContext.modalPanel(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<C>>,
        initialize: ModalPanel<C>.() -> Unit
    ) {
        panel = {
            tag(this, classes, id, internalScope) {
                addComponentStructureInfo("parent is modalPanel", this@modalPanel.scope, this)
                ModalPanel(this).run {
                    initialize()
                    render()
                }
            }
        }
    }

    /**
     * Factory function to create a [modalPanel] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/modal/#modalpanel)
     */
    fun RenderContext.modalPanel(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: ModalPanel<HTMLDivElement>.() -> Unit
    ) = modalPanel(classes, id, internalScope, RenderContext::div, initialize)
}

/**
 * Factory function to create a [Modal].
 *
 * API-Sketch:
 * ```kotlin
 * modal() {
 *     var restoreFocus: Boolean
 *     var setInitialFocus: Boolean
 *     // inherited by `OpenClose`
 *     val openState: DatabindingProperty<Boolean>
 *     val opened: Flow<Boolean>
 *     val close: SimpleHandler<Unit>
 *     val open: SimpleHandler<Unit>
 *     val toggle: SimpleHandler<Unit>
 *
 *     modalPanel() {
 *         modalOverlay() { }
 *         modalTitle() { }
 *         modalDescription() { } // use multiple times
 *
 *         // setInitialFocus() within one tag is possible
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/modal/#modal)
 */
fun RenderContext.modal(
    initialize: Modal.() -> Unit
) = Modal(this).run {
    initialize(this)
    render()
}
