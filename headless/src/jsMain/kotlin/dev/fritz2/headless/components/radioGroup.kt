package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import kotlinx.browser.document
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.*

/**
 * This class provides the building blocks to implement a radio-group.
 *
 * Use [radioGroup] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/radiogroup/)
 */
class RadioGroup<C : HTMLElement, T>(tag: Tag<C>, private val explicitId: String?) :
    Tag<C> by tag {

    private var label: Tag<HTMLElement>? = null
    private var validationMessages: Tag<HTMLElement>? = null
    private val isActive: Store<T?> = storeOf(null)
    private var withKeyboardNavigation = true
    private var options: MutableList<T> = mutableListOf()

    val componentId: String by lazy { explicitId ?: value.id ?: Id.next() }
    val value = DatabindingProperty<T>()

    fun render() {
        attr("id", componentId)
        attr("role", Aria.Role.radiogroup)
        attr(Aria.invalid, "true".whenever(value.hasError))
        label?.let { attr(Aria.labelledby, it.id) }
        if (withKeyboardNavigation == true) {
            value.handler?.invoke(
                value.data.flatMapLatest { option ->
                    keydowns.mapNotNull { event ->
                        when (shortcutOf(event)) {
                            Keys.ArrowDown -> options.rotateNext(option)
                            Keys.ArrowUp -> options.rotatePrevious(option)
                            else -> null
                        }.also {
                            if (it != null) {
                                event.stopImmediatePropagation()
                                event.preventDefault()
                                isActive.update(it)
                            }
                        }
                    }
                })
        }
    }

    /**
     * Factory function to create a [radioGroupLabel].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogrouplabel)
     */
    fun <CL : HTMLElement> RenderContext.radioGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ): Tag<CL> {
        addComponentStructureInfo("radioGroupLabel", this@radioGroupLabel.scope, this)
        return tag(this, classes, "$componentId-label", scope, content).also { label = it }
    }

    /**
     * Factory function to create a [radioGroupLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogrouplabel)
     */
    fun RenderContext.radioGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = radioGroupLabel(classes, scope, RenderContext::label, content)

    /**
     * Factory function to create a [radioGroupValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupvalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.radioGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) {
        div(MOUNT_POINT_STYLE_CLASS) {
            value.validationMessages.map { it.isNotEmpty() }.distinctUntilChanged().render(into = this) { isNotEmpty ->
                if (isNotEmpty) {
                    addComponentStructureInfo(
                        "radioGroupValidationMessages",
                        this@radioGroupValidationMessages.scope,
                        this@div
                    )
                    tag(this, classes, "$componentId-${ValidationMessages.ID_SUFFIX}", scope) {
                        validationMessages = this
                        initialize(ValidationMessages(value.validationMessages, this))
                    }
                }
            }
        }
    }

    /**
     * Factory function to create a [radioGroupValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupvalidationmessages)
     */
    fun RenderContext.radioGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = radioGroupValidationMessages(classes, scope, RenderContext::div, initialize)

    inner class RadioGroupOption<CO : HTMLElement>(
        tag: Tag<CO>,
        private val option: T,
        val optionId: String
    ) : Tag<CO> by tag {

        val selected = value.data.map { it == option }
        val active = isActive.data.map { it == option }.distinctUntilChanged()

        private var toggle: Tag<HTMLElement>? = null
        private var label: Tag<HTMLElement>? = null
        private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

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
         * Factory function to create a [radioGroupOptionToggle].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoptiontoggle)
         */
        fun <CT : HTMLElement> RenderContext.radioGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            content: Tag<CT>.() -> Unit
        ): Tag<CT> {
            addComponentStructureInfo("radioGroupOptionToggle", this@radioGroupOptionToggle.scope, this)
            return tag(this, classes, "$optionId-toggle", scope) {
                content()
                attr("role", Aria.Role.radio)
                attr(Aria.checked, selected.asString())
                attr("tabindex", selected.map { if (it) "0" else "-1" })
                var toggleEvent: Listener<*, *> = clicks
                if (domNode is HTMLInputElement) {
                    if (domNode.getAttribute("name") == null) {
                        attr("name", componentId)
                    }
                    withKeyboardNavigation = false
                    toggleEvent = changes
                }
                value.handler?.invoke(toggleEvent.map { option })
                active handledBy {
                    if (it && domNode != document.activeElement) {
                        domNode.focus()
                    }
                }
                focuss.map { option } handledBy isActive.update
                blurs.map { null } handledBy isActive.update
            }.also { toggle = it }
        }

        /**
         * Factory function to create a [radioGroupOptionToggle] with a [HTMLDivElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoptiontoggle)
         */
        fun RenderContext.radioGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLDivElement>.() -> Unit
        ) = radioGroupOptionToggle(classes, scope, RenderContext::div, content)

        /**
         * Factory function to create a [radioGroupOptionLabel].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoptionlabel)
         */
        fun <CL : HTMLElement> RenderContext.radioGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CL>>,
            content: Tag<CL>.() -> Unit
        ): Tag<CL> {
            addComponentStructureInfo("radioGroupOptionLabel", this@radioGroupOptionLabel.scope, this)
            return tag(this, classes, "$optionId-label", scope, content).also { label = it }
        }

        /**
         * Factory function to create a [radioGroupOptionLabel] with a [HTMLLabelElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoptionlabel)
         */
        fun RenderContext.radioGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLLabelElement>.() -> Unit
        ) = radioGroupOptionLabel(classes, scope, RenderContext::label) {
            content()
            `for`(optionId)
        }

        /**
         * Factory function to create a [radioGroupOptionDescription].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoptiondescription)
         */
        fun <CL : HTMLElement> RenderContext.radioGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CL>>,
            content: Tag<CL>.() -> Unit
        ): Tag<CL> {
            addComponentStructureInfo("radioGroupOptionDescription", this@radioGroupOptionDescription.scope, this)
            return tag(
                this,
                classes,
                "$optionId-description-${descriptions.size}",
                scope,
                content
            ).also { descriptions.add(it) }
        }

        /**
         * Factory function to create a [radioGroupOptionDescription] with a [HTMLSpanElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoptiondescription)
         */
        fun RenderContext.radioGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLSpanElement>.() -> Unit
        ) = radioGroupOptionDescription(classes, scope, RenderContext::span, content)

        init {
            options.add(option)
        }
    }

    /**
     * Factory function to create a [radioGroupOption].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoption)
     */
    fun <CO : HTMLElement> RenderContext.radioGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CO>>,
        initialize: RadioGroupOption<CO>.() -> Unit
    ): Tag<CO> {
        addComponentStructureInfo("radioGroupOption", this@radioGroupOption.scope, this)
        val optionId = "$componentId-${id ?: Id.next()}"
        return tag(this, classes, optionId, scope) {
            RadioGroupOption(this, option, optionId).run {
                initialize()
                render()
            }
        }
    }

    /**
     * Factory function to create a [radioGroupOption] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroupoption)
     */
    fun RenderContext.radioGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: RadioGroupOption<HTMLDivElement>.() -> Unit
    ): Tag<HTMLDivElement> = radioGroupOption(option, classes, id, scope, RenderContext::div, initialize)
}

/**
 * Factory function to create a [RadioGroup].
 *
 * API-Sketch:
 * ```kotlin
 * radioGroup<T>() {
 *     val value: DatabindingPropert<T>
 *
 *     radioGroupLabel() { }
 *     radioGroupValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 *     // for each T {
 *         radioGroupOption(option: T) {
 *             val selected: Flow<Boolean>
 *             val active: Flow<Boolean>
 *
 *             radioGroupOptionToggle() { }
 *             radioGroupOptionLabel() { }
 *             radioGroupOptionDescription() { } // use multiple times
 *         }
 *     // }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroup)
 */
fun <C : HTMLElement, T> RenderContext.radioGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: RadioGroup<C, T>.() -> Unit
): Tag<C> {
    addComponentStructureInfo("radioGroup", this@radioGroup.scope, this)
    return tag(this, classes, id, scope) {
        RadioGroup<C, T>(this, id).run {
            initialize()
            render()
        }
    }
}

/**
 * Factory function to create a [RadioGroup] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * radioGroup<T>() {
 *     val value: DatabindingPropert<T>
 *
 *     radioGroupLabel() { }
 *     radioGroupValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 *     // for each T {
 *         radioGroupOption(option: T) {
 *             val selected: Flow<Boolean>
 *             val active: Flow<Boolean>
 *
 *             radioGroupOptionToggle() { }
 *             radioGroupOptionLabel() { }
 *             radioGroupOptionDescription() { } // use multiple times
 *         }
 *     // }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/radiogroup/#radiogroup)
 */
fun <T> RenderContext.radioGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: RadioGroup<HTMLDivElement, T>.() -> Unit
): Tag<HTMLDivElement> = radioGroup(classes, id, scope, RenderContext::div, initialize)
