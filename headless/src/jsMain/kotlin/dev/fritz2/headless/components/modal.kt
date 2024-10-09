package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.take
import org.w3c.dom.*

/**
 * This class provides the building blocks to implement a modal.
 *
 * Use [modal] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/modal/)
 */
class Modal(id: String?) : OpenClose() {

    val componentId: String by lazy { id ?: Id.next() }

    override val job: Job = Job()

    var restoreFocus: Boolean = true
    var setInitialFocus: InitialFocus = InitialFocus.InsistToSet

    private var panel: (RenderContext.() -> Tag<HTMLElement>)? = null

    fun init() {
        opened.filter { it }.handledBy {
            PortalRenderContext.run {
                portal(id = componentId, tag = RenderContext::dialog, scope = scopeContext) { remove ->
                    inlineStyle("display: contents")
                    panel?.invoke(this)!!.apply {
                        trapFocusInMountpoint(restoreFocus, setInitialFocus)
                    }
                    opened.onCompletion {
                        /*
                         * This needs to be explained:
                         * As a `modal` is not dependent on any `Tag<*>`, we cannot provide some
                         * `PortalContainer.reference`-object. The latter is needed to couple the portal-portion of
                         * a component to its counterpart inside the normal `RenderContext` (or fritz2 controlled
                         * subtree if that is more understandable). From that reference we can get its nearest
                         * `MountPoint` and rely on the latter to register some `DomLifecycleHandler` by the
                         * `beforeUnmount`-lifecycle hook. In the case of a portal, we can call its `remove`-handler,
                         * which itself will change the global `PortalStack`, which will then reactively execute
                         * `renderEach` on all portals. So the portal-portion will get removed if its reference is
                         * reactively removed. This is always the case if the reference lives inside some reactive
                         * scope, which are created by any `render*`-call. (This is true for `Router`-based content
                         * too of course!)
                         *
                         * As we do not have such a reference here, we can only refer to the data-binding-flow, which
                         * will normally reside inside some reactive scope. So if the `Job` of this `Flow` is canceled
                         * due to some normal fritz2 reactive action, we know that the modal must also be removed from
                         * the DOM.
                         */
                        remove(Unit)
                    }.filter { !it }
                        .map { }
                        .take(1) handledBy remove
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
     * It is recommended to define some explicit z-index within the classes-parameter.
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/modal/#modalpanel)
     */
    fun <C : HTMLElement> Modal.modalPanel(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<C>>,
        initialize: ModalPanel<C>.() -> Unit
    ) {
        panel = {
            tag(this, classes, null, internalScope) {
                addComponentStructureInfo("parent is modalPanel", scope, this)
                ModalPanel(this, id).run {
                    initialize()
                    render()
                }
            }
        }
    }

    /**
     * Factory function to create a [modalPanel] with a [HTMLDivElement] as default [Tag].
     *
     * It is recommended to define some explicit z-index within the classes-parameter.
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/modal/#modalpanel)
     */
    fun Modal.modalPanel(
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
fun modal(
    id: String? = null,
    initialize: Modal.() -> Unit
) {
    Modal(id).run {
        initialize(this)
        init()
    }
}