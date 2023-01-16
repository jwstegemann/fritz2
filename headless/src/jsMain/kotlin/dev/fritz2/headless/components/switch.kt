package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.w3c.dom.*

/**
 * This base class provides the building blocks to implement a switch.
 *
 * There exist two different implementations:
 * - [Switch] for a simple switch with optional validation handling
 * - [SwitchWithLabel] for a switch wit additional label and description facilities and optional validation handling
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/)
 */
abstract class AbstractSwitch<C : HTMLElement>(
    tag: Tag<C>,
    private val explicitId: String?,
    private val componentName: String
) :
    Tag<C> by tag {

    val value = DatabindingProperty<Boolean>()
    val enabled: Flow<Boolean> by lazy { value.data }

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
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchvalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.switchValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) {
        value.validationMessages.map { it.isNotEmpty() }.render { isNotEmpty ->
            if (isNotEmpty) {
                addComponentStructureInfo(
                    "switchValidationMessages",
                    this@switchValidationMessages.scope,
                    this
                )
                tag(this, classes, "$componentId-${ValidationMessages.ID_SUFFIX}", scope) {
                    validationMessages = this
                    initialize(ValidationMessages(value.validationMessages, this))
                }
            }
        }
    }

    /**
     * Factory function to create a [switchValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchvalidationmessages)
     */
    fun RenderContext.switchValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = switchValidationMessages(classes, scope, RenderContext::div, initialize)
}

/**
 * This class provides the building blocks to implement a switch with label and description facilities.
 *
 * Use [switchWithLabel] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/)
 */
class SwitchWithLabel<C : HTMLElement>(tag: Tag<C>, id: String?) :
    AbstractSwitch<C>(tag, id, COMPONENT_NAME) {

    companion object {
        const val COMPONENT_NAME = "switchWithLabel"
    }

    private var toggle: Tag<HTMLElement>? = null
    private var label: Tag<HTMLElement>? = null
    private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

    private val toggleId by lazy { "$componentId-toggle" }

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
        if (!value.isSet) {
            warnAboutMissingDatabinding("value", COMPONENT_NAME, componentId, domNode)
        }
    }

    /**
     * Factory function to create a [switchToggle].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchtoggle)
     */
    fun <CT : HTMLElement> RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CT>>,
        content: Tag<CT>.() -> Unit
    ): Tag<CT> {
        addComponentStructureInfo("switchToggle", this@switchToggle.scope, this)
        return tag(this, classes, toggleId, scope) {
            content()
            renderSwitchCore(this)
        }.also { toggle = it }
    }

    /**
     * Factory function to create a [switchToggle] with a [HTMLButtonElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchtoggle)
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
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchlabel)
     */
    fun <CL : HTMLElement> RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ): Tag<CL> {
        addComponentStructureInfo("switchLabel", this@switchLabel.scope, this)
        return tag(this, classes, "$componentId-label", scope, content).apply {
            value.handler?.invoke(value.data.flatMapLatest { state -> clicks.map { !state } })
        }.also { label = it }
    }

    /**
     * Factory function to create a [switchLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchlabel)
     */
    fun RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = switchLabel(classes, scope, RenderContext::label) {
        content()
        `for`(toggleId)
    }

    /**
     * Factory function to create a [switchDescription].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchdescription)
     */
    fun <CL : HTMLElement> RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ): Tag<CL> {
        addComponentStructureInfo("switchDescription", this@switchDescription.scope, this)
        return tag(
            this,
            classes,
            "$componentId-description-${descriptions.size}",
            scope,
            content
        ).also { descriptions.add(it) }
    }

    /**
     * Factory function to create a [switchDescription] with a [HTMLSpanElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/switch/#switchdescription)
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
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/#switch)
 */
fun <C : HTMLElement> RenderContext.switchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: SwitchWithLabel<C>.() -> Unit
): Tag<C> {
    addComponentStructureInfo(SwitchWithLabel.COMPONENT_NAME, this@switchWithLabel.scope, this)
    return tag(this, classes, id, scope) {
        SwitchWithLabel(this, id).run {
            initialize()
            render()
        }
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
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/#switch)
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
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/)
 */
class Switch<C : HTMLElement>(tag: Tag<C>, explicitId: String?) :
    AbstractSwitch<C>(tag, explicitId, COMPONENT_NAME) {

    companion object {
        const val COMPONENT_NAME = "switch"
    }

    override fun render() {
        attr("id", componentId)
        renderSwitchCore(this)
        attr(
            Aria.describedby,
            value.validationMessages.map { messages ->
                if (messages.isNotEmpty()) validationMessages?.id else null
            }
        )
        if (!value.isSet) {
            warnAboutMissingDatabinding("value", COMPONENT_NAME, componentId, domNode)
        }
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
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/#switch)
 */
fun <C : HTMLElement> RenderContext.switch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Switch<C>.() -> Unit
): Tag<C> {
    addComponentStructureInfo(Switch.COMPONENT_NAME, this@switch.scope, this)
    return tag(this, classes, id, scope) {
        Switch(this, id).run {
            initialize()
            render()
        }
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
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/switch/#switch)
 */
fun RenderContext.switch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Switch<HTMLButtonElement>.() -> Unit
): Tag<HTMLButtonElement> = switch(classes, id, scope, RenderContext::button, initialize)
    .apply { attr("type", "button") }
