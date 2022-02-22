package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.w3c.dom.*

/**
 * This class provides the building blocks to implement a checkbox-group.
 *
 * Use [checkboxGroup] functions to create an instance, setup the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/)
 */
class CheckboxGroup<C : HTMLElement, T>(tag: Tag<C>, private val explicitId: String?) :
    Tag<C> by tag {

    private var label: Tag<HTMLElement>? = null
    private var validationMessages: Tag<HTMLElement>? = null

    val value = DatabindingProperty<List<T>>()

    val componentId: String by lazy { explicitId ?: value.id ?: Id.next() }

    fun render() {
        attr("id", componentId)
        attr("role", Aria.Role.group)
        attr(Aria.invalid, "true".whenever(value.hasError))
        label?.let { attr(Aria.labelledby, it.id) }
    }

    /**
     * Factory function to create a [checkboxGroupLabel].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgrouplabel)
     */
    fun <CL : HTMLElement> RenderContext.checkboxGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    /**
     * Factory function to create a [checkboxGroupLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgrouplabel)
     */
    fun RenderContext.checkboxGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = checkboxGroupLabel(classes, scope, RenderContext::label, content)

    /**
     * Factory function to create a [checkboxGroupValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupvalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.checkboxGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) {
        value.validationMessages.map { it.isNotEmpty() }.distinctUntilChanged().render { isNotEmpty ->
            if(isNotEmpty) {
                tag(this, classes, "$componentId-${ValidationMessages.ID_SUFFIX}", scope) {
                    validationMessages = this
                    initialize(ValidationMessages(value.validationMessages, this))
                }
            }
        }
    }

    /**
     * Factory function to create a [checkboxGroupValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupvalidationmessages)
     */
    fun RenderContext.checkboxGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = checkboxGroupValidationMessages(classes, scope, RenderContext::div, initialize)

    inner class CheckboxGroupOption<CO : HTMLElement>(
        tag: Tag<CO>,
        private val option: T,
        id: String?
    ) : Tag<CO> by tag {

        val selected = value.data.map { it.contains(option) }

        private var toggle: Tag<HTMLElement>? = null
        private var label: Tag<HTMLElement>? = null
        private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

        val optionId = "$componentId-${id ?: Id.next()}"

        fun render() {
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
         * Factory function to create a [checkboxGroupOptionToggle].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoptiontoggle)
         */
        fun <CT : HTMLElement> RenderContext.checkboxGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            content: Tag<CT>.() -> Unit
        ) = tag(this, classes, optionId, scope) {
            content()
            attr("role", Aria.Role.checkbox)
            attr(Aria.checked, selected.asString())
            attr("tabindex", "0")
            var withKeyboardNavigation = true
            var toggleEvent: Listener<*, *> = clicks
            if (domNode is HTMLInputElement) {
                if (domNode.getAttribute("name") == null) {
                    attr("name", componentId)
                }
                withKeyboardNavigation = false
                toggleEvent = changes
            }
            value.handler?.invoke(value.data.flatMapLatest { value ->
                toggleEvent.map { if (value.contains(option)) value - option else value + option }
            })
            if (withKeyboardNavigation) {
                value.handler?.invoke(
                    value.data.flatMapLatest { value ->
                        keydowns.filter { shortcutOf(it) == Keys.Space }.map {
                            it.stopImmediatePropagation()
                            it.preventDefault()
                            if (value.contains(option)) value - option else value + option
                        }
                    })
            }
        }.also { toggle = it }

        /**
         * Factory function to create a [checkboxGroupOptionToggle] with a [HTMLDivElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoptiontoggle)
         */
        fun RenderContext.checkboxGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLDivElement>.() -> Unit
        ) = checkboxGroupOptionToggle(classes, scope, RenderContext::div, content)

        /**
         * Factory function to create a [checkboxGroupOptionLabel].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoptionlabel)
         */
        fun <CL : HTMLElement> RenderContext.checkboxGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CL>>,
            content: Tag<CL>.() -> Unit
        ) = tag(this, classes, "$optionId-label", scope, content).also { label = it }

        /**
         * Factory function to create a [checkboxGroupOptionLabel] with a [HTMLLabelElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoptionlabel)
         */
        fun RenderContext.checkboxGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLLabelElement>.() -> Unit
        ) = checkboxGroupOptionLabel(classes, scope, RenderContext::label) {
            content()
            `for`(optionId)
        }

        /**
         * Factory function to create a [checkboxGroupOptionDescription].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoptiondescription)
         */
        fun <CL : HTMLElement> RenderContext.checkboxGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CL>>,
            content: Tag<CL>.() -> Unit
        ) = tag(
            this,
            classes,
            "$optionId-description-${descriptions.size}",
            scope,
            content
        ).also { descriptions.add(it) }

        /**
         * Factory function to create a [checkboxGroupOptionDescription] with a [HTMLSpanElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoptiondescription)
         */
        fun RenderContext.checkboxGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLSpanElement>.() -> Unit
        ) = checkboxGroupOptionDescription(classes, scope, RenderContext::span, content)
    }

    /**
     * Factory function to create a [CheckboxGroupOption].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoption)
     */
    fun <CO : HTMLElement> RenderContext.checkboxGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CO>>,
        initialize: CheckboxGroupOption<CO>.() -> Unit
    ): Tag<CO> = tag(this, classes, id, scope) {
        CheckboxGroupOption(this, option, id).run {
            initialize()
            render()
        }
    }

    /**
     * Factory function to create a [CheckboxGroupOption] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroupoption)
     */
    fun RenderContext.checkboxGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: CheckboxGroupOption<HTMLDivElement>.() -> Unit
    ): Tag<HTMLDivElement> = checkboxGroupOption(option, classes, id, scope, RenderContext::div, initialize)
}

/**
 * Factory function to create a [CheckboxGroup].
 *
 * API-Sketch:
 * ```kotlin
 * checkboxGroup<T>() {
 *     val value: DatabindingPropert<List<T>>
 *
 *     checkboxGroupLabel() { }
 *     checkboxGroupValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 *     // for each T {
 *         checkboxGroupOption(option: T) {
 *             val selected: Flow<Boolean>
 *
 *             checkboxGroupOptionToggle() { }
 *             checkboxGroupOptionLabel() { }
 *             checkboxGroupOptionDescription() { } // use multiple times
 *         }
 *     // }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroup)
 */
fun <C : HTMLElement, T> RenderContext.checkboxGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: CheckboxGroup<C, T>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    CheckboxGroup<C, T>(this, id).run {
        initialize()
        render()
    }
}

/**
 * Factory function to create a [CheckboxGroup] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * checkboxGroup<T>() {
 *     val value: DatabindingPropert<List<T>>
 *
 *     checkboxGroupLabel() { }
 *     checkboxGroupValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 *     // for each T {
 *         checkboxGroupOption(option: T) {
 *             val selected: Flow<Boolean>
 *
 *             checkboxGroupOptionToggle() { }
 *             checkboxGroupOptionLabel() { }
 *             checkboxGroupOptionDescription() { } // use multiple times
 *         }
 *     // }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/checkboxgroup/#checkboxgroup)
 */
fun <T> RenderContext.checkboxGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: CheckboxGroup<HTMLDivElement, T>.() -> Unit
): Tag<HTMLDivElement> = checkboxGroup(classes, id, scope, RenderContext::div, initialize)
