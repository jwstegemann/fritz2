package dev.fritz2.headless.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.*
import org.w3c.dom.*

/**
 * This base class provides the building blocks to implement a switch.
 *
 * There exist two different implementations:
 * - [Switch] for a simple switch with optional validation handling
 * - [SwitchWithLabel] for a switch wit additional label and description facilities and optional validation handling
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/)
 */
abstract class AbstractSwitch<C : HTMLElement>(tag: Tag<C>, private val explicitId: String?) :
    Tag<C> by tag {

    val value = DatabindingProperty<Boolean>()
    val enabled: Flow<Boolean> = flowOf(false).flatMapLatest { value.data }

    val componentId: String by lazy { explicitId ?: value.id ?: Id.next() }

    protected var validationMessages: Tag<HTMLElement>? = null

    abstract fun render()

    protected fun renderSwitchCore(renderContext: Tag<HTMLElement>) = renderContext.apply {
        attr("role", Aria.Role.switch)
        attr(Aria.checked, enabled.asString())
        attr(Aria.invalid, "true".whenever(value.hasError))
        attr("tabindex", "0")
        value.handler?.invoke(value.data.flatMapLatest { state -> clicks.map { !state } })
        value.handler?.invoke(
            value.data.flatMapLatest { state ->
                keydowns.filter { shortcutOf(it) == Keys.Space }.map {
                    it.stopImmediatePropagation()
                    it.preventDefault()
                    !state
                }
            })
    }

    /**
     * Factory function to create a [switchValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchvalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.switchValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        content: Tag<CV>.(List<ComponentValidationMessage>) -> Unit
    ) = value.validationMessages.render { messages ->
        if (messages.isNotEmpty()) {
            tag(this, classes, "$componentId-validation-messages", scope, { })
                .apply {
                    content(messages)
                }.also { validationMessages = it }
        }
    }

    /**
     * Factory function to create a [switchValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchvalidationmessages)
     */
    fun RenderContext.switchValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLDivElement>.(List<ComponentValidationMessage>) -> Unit
    ) = switchValidationMessages(classes, scope, RenderContext::div, content)
}

/**
 * This class provides the building blocks to implement a switch with label and description facilities.
 *
 * Use [switchWithLabel] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/)
 */
class SwitchWithLabel<C : HTMLElement>(tag: Tag<C>, id: String?) :
    AbstractSwitch<C>(tag, id) {

    private var toggle: Tag<HTMLElement>? = null
    private var label: Tag<HTMLElement>? = null
    private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

    override fun render() {
        attr("id", componentId)
        toggle?.apply {
            label?.let { attr(Aria.labelledby, it.id) }
            attr(
                Aria.describedby,
                value.validationMessages.map { messages ->
                    if (messages.isNotEmpty()) validationMessages?.id
                    else descriptions.map { it.id }.joinToString(" ")
                }
            )
        }
    }

    /**
     * Factory function to create a [switchToggle].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchtoggle)
     */
    fun <CT : HTMLElement> RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CT>>,
        content: Tag<CT>.() -> Unit
    ) = tag(this, classes, "$componentId-toggle", scope) {
        content()
        renderSwitchCore(this)
    }.also { toggle = it }

    /**
     * Factory function to create a [switchToggle] with a [HTMLButtonElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchtoggle)
     */
    fun RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = switchToggle(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    /**
     * Factory function to create a [switchLabel].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchlabel)
     */
    fun <CL : HTMLElement> RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).apply {
        value.handler?.invoke(value.data.flatMapLatest { state -> clicks.map { !state } })
    }.also { label = it }

    /**
     * Factory function to create a [switchLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchlabel)
     */
    fun RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = switchLabel(classes, scope, RenderContext::label) {
        content()
        `for`(componentId)
    }

    /**
     * Factory function to create a [switchDescription].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchdescription)
     */
    fun <CL : HTMLElement> RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(
        this,
        classes,
        "$componentId-description-${descriptions.size}",
        scope,
        content
    ).also { descriptions.add(it) }

    /**
     * Factory function to create a [switchDescription] with a [HTMLSpanElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/switch/#switchdescription)
     */
    fun RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLSpanElement>.() -> Unit
    ) = switchDescription(classes, scope, RenderContext::span, content)

}

/**
 * Factory function to create a [SwitchWithLabel].
 *
 * API-Sketch:
 * ```kotlin
 * switchWithLabel() {
 *     val value: DatabindingProperty<Boolean>
 *     val enabled: Flow<Boolean>
 *
 *     switchToggle() { }
 *     switchLabel() { }
 *     switchDescription() { } // use multiple times
 *     switchValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/#switch)
 */
fun <C : HTMLElement> RenderContext.switchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: SwitchWithLabel<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    SwitchWithLabel(this, id).run {
        initialize()
        render()
    }
}

/**
 * Factory function to create a [SwitchWithLabel] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * switchWithLabel() {
 *     val value: DatabindingProperty<Boolean>
 *     val enabled: Flow<Boolean>
 *
 *     switchToggle() { }
 *     switchLabel() { }
 *     switchDescription() { } // use multiple times
 *     switchValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/#switch)
 */
fun RenderContext.switchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: SwitchWithLabel<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = switchWithLabel(classes, id, scope, RenderContext::div, initialize)

/**
 * This class provides the building blocks to implement a simple switch.
 *
 * Use [switch] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/)
 */
class Switch<C : HTMLElement>(tag: Tag<C>, explicitId: String?) :
    AbstractSwitch<C>(tag, explicitId) {

    override fun render() {
        attr("id", componentId)
        renderSwitchCore(this)
        attr(
            Aria.describedby,
            value.validationMessages.map { messages ->
                if (messages.isNotEmpty()) validationMessages?.id else null
            }
        )
    }
}

/**
 * Factory function to create a [Switch].
 *
 * API-Sketch:
 * ```kotlin
 * switch() {
 *     val value: DatabindingProperty<Boolean>
 *     val enabled: Flow<Boolean>
 *
 *     switchValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/#switch)
 */
fun <C : HTMLElement> RenderContext.switch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Switch<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    Switch(this, id).run {
        initialize()
        render()
    }
}

/**
 * Factory function to create a [Switch] with a [HTMLButtonElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * switch() {
 *     val value: DatabindingProperty<Boolean>
 *     val enabled: Flow<Boolean>
 *
 *     switchValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/switch/#switch)
 */
fun RenderContext.switch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Switch<HTMLButtonElement>.() -> Unit
): Tag<HTMLButtonElement> = switch(classes, id, scope, RenderContext::button, initialize)
    .apply { attr("type", "button") }
