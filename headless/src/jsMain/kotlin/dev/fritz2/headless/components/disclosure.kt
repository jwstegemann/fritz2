package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.OpenClose
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.addComponentStructureInfo
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * This class provides the building blocks to implement a disclosure.
 *
 * Use [disclosure] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/disclosure)
 */
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

    /**
     * Factory function to create a [disclosureButton].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosurebutton)
     */
    fun <CB : HTMLElement> RenderContext.disclosureButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CB>>,
        content: Tag<CB>.() -> Unit
    ): Tag<CB> {
        addComponentStructureInfo("disclosureButton", this@disclosureButton.scope, this)
        return tag(this, classes, "$componentId-button", scope) {
            if (!openState.isSet) openState(storeOf(false))
            content()
            attr(Aria.expanded, opened.asString())
            attr("tabindex", "0")
            activations.preventDefault().stopPropagation() handledBy toggle
        }.also { button = it }
    }

    /**
     * Factory function to create a [disclosureButton] with a [HTMLButtonElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosurebutton)
     */
    fun RenderContext.disclosureButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = disclosureButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class DisclosurePanel<CP : HTMLElement>(tag: Tag<CP>) : Tag<CP> by tag {
        fun render() {
            button?.attr(Aria.controls, id.whenever(opened))
        }

        /**
         * Factory function to create a [disclosureCloseButton].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosureclosebutton)
         */
        fun <CC : HTMLElement> RenderContext.disclosureCloseButton(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CC>>,
            content: Tag<CC>.() -> Unit
        ): Tag<CC> {
            addComponentStructureInfo("disclosureCloseButton", this@disclosureCloseButton.scope, this)
            return tag(this, classes, "$componentId-close-button", scope) {
                content()
                clicks handledBy close
            }
        }

        /**
         * Factory function to create a [disclosureCloseButton] with a [HTMLButtonElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosureclosebutton)
         */
        fun RenderContext.disclosureCloseButton(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLButtonElement>.() -> Unit
        ) = disclosureCloseButton(classes, scope, RenderContext::button, content).apply {
            attr("type", "button")
        }
    }

    /**
     * Factory function to create a [disclosurePanel].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosurepanel)
     */
    fun <CP : HTMLElement> RenderContext.disclosurePanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CP>>,
        initialize: DisclosurePanel<CP>.() -> Unit
    ) {
        addComponentStructureInfo("disclosurePanel", this@disclosurePanel.scope, this)
        panel = {
            tag(this, classes, "$componentId-panel", scope) {
                DisclosurePanel(this).run {
                    initialize()
                    render()
                }
            }
        }
    }

    /**
     * Factory function to create a [disclosurePanel] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosurepanel)
     */
    fun RenderContext.disclosurePanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: DisclosurePanel<HTMLDivElement>.() -> Unit
    ) = disclosurePanel(classes, scope, RenderContext::div, initialize)
}

/**
 * Factory function to create a [Disclosure].
 *
 * API-Sketch:
 * ```kotlin
 * disclosure() {
 *     // inherited by `OpenClose`
 *     val openState: DatabindingProperty<Boolean>
 *     val opened: Flow<Boolean>
 *     val close: SimpleHandler<Unit>
 *     val open: SimpleHandler<Unit>
 *     val toggle: SimpleHandler<Unit>
 *
 *     disclosureButton() { }
 *     disclosurePanel() {
 *         disclosureCloseButton() {}
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosure)
 */
fun <C : HTMLElement> RenderContext.disclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Disclosure<C>.() -> Unit
): Tag<C> {
    addComponentStructureInfo("disclosure", this@disclosure.scope, this)
    return tag(this, classes, id, scope) {
        Disclosure(this, id).run {
            initialize()
            render()
        }
    }
}

/**
 * Factory function to create a [Disclosure] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * disclosure() {
 *     // inherited by `OpenClose`
 *     val openState: DatabindingProperty<Boolean>
 *     val opened: Flow<Boolean>
 *     val close: SimpleHandler<Unit>
 *     val open: SimpleHandler<Unit>
 *     val toggle: SimpleHandler<Unit>
 *
 *     disclosureButton() { }
 *     disclosurePanel() {
 *         disclosureCloseButton() {}
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/disclosure/#disclosure)
 */
fun RenderContext.disclosure(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Disclosure<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = disclosure(classes, id, scope, RenderContext::div, initialize)
