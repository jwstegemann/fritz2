package dev.fritz2.components

import SelectFieldComponent
import dev.fritz2.binding.Store
import dev.fritz2.components.FormControlComponent.Control
import dev.fritz2.components.validation.ComponentValidationMessage
import dev.fritz2.components.validation.Severity
import dev.fritz2.components.validation.validationMessages
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import selectField


/**
 * This component class manages the configuration of a [formControl] and some render centric functionalities.
 * The former are important for clients of a [formControl], the latter for extending or changing the default behaviors.
 *
 * A [formControl] can be configured in different aspects:
 * - a label for a description of the control as a whole
 * - mark the control as _required_
 * - an optional helper text
 * - provide a validation message as a [Flow<ComponentValidationMessage>]]; the *severity* is reflected by the default
 *   theme!
 * - disable the control
 * - provide a store to the control itself, with optional additional validation ([dev.fritz2.components.validation.WithValidator])
 *   at best, in order to automatically apply model validation and get the results shown
 *
 * Customizing the control:
 *
 * To add a new control, extend this class and add a new control function that wraps the desired control component
 * factory function like [FormControlComponent.inputField], [FormControlComponent.selectField],
 * [FormControlComponent.checkbox], [FormControlComponent.checkboxGroup] and [FormControlComponent.radioGroup] do.
 *
 * In order to simply change the target of some of the default control wrapping function to a different control
 * component, extend this class and override the desired function. Be aware that you cannot provide default arguments
 * for an overridden function, so you must offer a new function with default arguments that just directs to
 * the overridden one.
 *
 * Be aware of the render strategy - pick whether your control should be rendered as a single control or a group.
 * - [SingleControlRenderer] for a control that consists of a single element
 * - [ControlGroupRenderer] for a control that consists of multiple parts (like checkBoxes etc.)
 *
 * If those do not fit, just implement the [ControlRenderer] interface and pair it with the string based key of the
 * related control wrapping function. Have a look at the init block, [renderStrategies] field and [Control.assignee]
 * field to learn how the mapping between control and rendering strategy is done.
 *
 */
open class FormControlComponent : Component<Unit>, FormProperties by FormMixin() {
    companion object {
        object ControlNames {
            const val inputField = "inputField"
            const val textArea = "textArea"
            const val switch = "switch"
            const val selectField = "selectField"
            const val radioGroup = "radioGroup"
            const val checkbox = "checkbox"
            const val checkboxGroup = "checkboxGroup"
        }
    }

    class Control {

        private val overflows: MutableList<String> = mutableListOf()
        var assignee: Pair<String, (RenderContext.() -> Unit)>? = null

        fun set(
            controlName: String,
            component: (RenderContext.() -> Unit),
        ): Boolean {
            if (assignee == null) {
                assignee = Pair(controlName, component)
                return true
            } else {
                overflows.add(controlName)
            }
            return false
        }

        fun assert() {
            if (overflows.isNotEmpty()) {
                console.error(
                    UnsupportedOperationException(
                        message = "Only one control within a formControl is allowed! Accepted control: ${assignee?.first}"
                                + " The following controls are not applied and overflow this form: "
                                + overflows.joinToString(", ")
                                + " Please remove those!"
                    )
                )
            }
        }
    }

    /**
     * Use this method from your own control wrapping methods or if you override an existing one in order to
     * register the control for the form.
     *
     * @param controlName a unique String name / key for the control. Prefer the predefined [ControlNames] if possible
     * @param component pass in some control (could be arbitrary complex!) that should be wrapped by the form.
     * @param onSuccess some optional action that will be executed if the registration was successful (remember that
     *                  only *one* control, so normally the *first*, will be accepted!), for example the temporary
     *                  storage of validation messages from a passed in store.
     */
    protected fun registerControl(
        controlName: String,
        component: (RenderContext.() -> Unit),
        onSuccess: FormControlComponent.() -> Unit = {}
    ) {
        if (control.set(controlName, component)) {
            onSuccess(this)
        }
    }

    private val renderStrategies: MutableMap<String, ControlRenderer> = mutableMapOf()

    /**
     * Use this method to register your custom renderer for existing controls or to register a built-in renderer
     * for a custom control.
     *
     * Remember to prefer to use the predefined [ControlNames] as key for the first case.
     *
     * @param controlName a unique String name / key for the control. Prefer the predefined [ControlNames] if possible
     * @param renderer some instance of a [ControlRenderer] that should be used to render the form for a registered
     *                 control (match via the name obviously)
     */
    protected fun registerRenderStrategy(controlName: String, renderer: ControlRenderer) {
        renderStrategies[controlName] = renderer
    }

    private val control = Control()

    object FormSizeContext {
        enum class FormSizeSpecifier {
            small, normal, large
        }

        val small = FormSizeSpecifier.small
        val normal = FormSizeSpecifier.normal
        val large = FormSizeSpecifier.large

    }

    fun ownSize(): Style<BasicParams> = when (size.value(FormSizeContext)) {
        FormSizeContext.FormSizeSpecifier.small -> Theme().formControl.sizes.small
        FormSizeContext.FormSizeSpecifier.normal -> Theme().formControl.sizes.normal
        FormSizeContext.FormSizeSpecifier.large -> Theme().formControl.sizes.large
    }

    val size = ComponentProperty<FormSizeContext.() -> FormSizeContext.FormSizeSpecifier> { normal }

    protected var sizeBuilder: (FormSizes) -> Style<BasicParams> = { sizes ->
        when (this@FormControlComponent.size.value(FormSizeContext)) {
            FormSizeContext.FormSizeSpecifier.small -> sizes.small
            FormSizeContext.FormSizeSpecifier.normal -> sizes.normal
            FormSizeContext.FormSizeSpecifier.large -> sizes.large
        }
    }


    val label = ComponentProperty("")
    val labelStyle = ComponentProperty(Theme().formControl.label)

    val helperText = ComponentProperty<String?>(null)
    val helperTextStyle = ComponentProperty(Theme().formControl.helperText)

    class ValidationResult(val messages: Flow<List<ComponentValidationMessage>>?) {
        companion object {
            /**
             * Simple factory method to encapsulate the logic that defining some ``validationMessage(s)`` property
             * beats the validation from the store!
             * Also defer the *execution* after the component object is fully initialized, so that the
             * *declaration order* of the control and a possible manually declared validation message property does
             * not matter!
             */
            fun <T> builderOf(formControl: FormControlComponent, store: Store<T>? = null): () -> ValidationResult = {
                ValidationResult(
                    (formControl.validationMessage.value()?.map { if (it == null) emptyList() else listOf(it) }
                        ?: formControl.validationMessages.value()) ?: store?.validationMessages())
            }
        }

        val hasSeverity: Flow<Severity?>
            get() = messages?.map { messages -> messages.map { it.severity }.maxOrNull() } ?: flowOf(null)
    }

    protected var validationMessagesBuilder: (() -> ValidationResult)? = null

    val validationMessage = ComponentProperty<() -> Flow<ComponentValidationMessage?>?> { null }
    val validationMessages = ComponentProperty<() -> Flow<List<ComponentValidationMessage>>?> { null }

    val validationMessageRendering =
        ComponentProperty<RenderContext.(ComponentValidationMessage) -> Unit> { message ->
            message.asAlert(this) {
                size(this@FormControlComponent.sizeBuilder)
                stacking { compact }
                variant { discreet }
            }
        }

    init {
        val singleRenderer = SingleControlRenderer(this)
        val groupRenderer = ControlGroupRenderer(this)
        sequenceOf(
            ControlNames.inputField,
            ControlNames.switch,
            ControlNames.textArea,
            ControlNames.selectField,
            ControlNames.checkbox
        ).forEach { registerRenderStrategy(it, singleRenderer) }
        registerRenderStrategy(ControlNames.checkboxGroup, groupRenderer)
        registerRenderStrategy(ControlNames.radioGroup, groupRenderer)
    }

    open fun inputField(
        styling: BasicParams.() -> Unit = {},
        value: Store<String>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = ControlNames.inputField,
        build: InputFieldComponent.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, value)
        registerControl(ControlNames.inputField,
            {
                inputField(styling, value, baseClass, id, prefix) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    open fun switch(
        styling: BasicParams.() -> Unit = {},
        value: Store<Boolean>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = ControlNames.switch,
        build: SwitchComponent.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, value)
        registerControl(ControlNames.inputField,
            {
                switch(styling, value, baseClass, id, prefix) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    open fun textArea(
        styling: BasicParams.() -> Unit = {},
        store: Store<String>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = ControlNames.textArea,
        build: TextAreaComponent.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, store)
        registerControl(ControlNames.textArea,
            {
                textArea(styling, store, baseClass, id, prefix) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    open fun checkbox(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass = StyleClass.None,
        value: Store<Boolean>? = null,
        id: String? = null,
        prefix: String = ControlNames.checkbox,
        build: CheckboxComponent.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, value)
        registerControl(ControlNames.checkbox,
            {
                checkbox({
                    styling()
                }, value, baseClass, id, prefix) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    open fun <T> checkboxGroup(
        styling: BasicParams.() -> Unit = {},
        items: List<T>,
        values: Store<List<T>>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = ControlNames.checkboxGroup,
        build: CheckboxGroupComponent<T>.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, values)
        registerControl(ControlNames.checkboxGroup,
            {
                checkboxGroup(styling, items, values, baseClass, id, prefix) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    open fun <T> radioGroup(
        styling: BasicParams.() -> Unit = {},
        items: List<T>,
        value: Store<T>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = ControlNames.radioGroup,
        build: RadioGroupComponent<T>.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, value)
        registerControl(ControlNames.radioGroup,
            {
                radioGroup(styling, items, value, baseClass, id, prefix) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    open fun <T> selectField(
        styling: BasicParams.() -> Unit = {},
        items: List<T>,
        value: Store<T>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = ControlNames.selectField,
        build: SelectFieldComponent<T>.() -> Unit = {}
    ) {
        val validationMessagesBuilder = ValidationResult.builderOf(this, value)
        registerControl(ControlNames.selectField,
            {
                selectField(
                    styling,
                    items,
                    value,
                    baseClass,
                    id,
                    prefix
                ) {
                    size { this@FormControlComponent.sizeBuilder(this) }
                    severity(validationMessagesBuilder().hasSeverity)
                    build()
                }
            },
            { this.validationMessagesBuilder = validationMessagesBuilder }
        )
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        control.assignee?.second?.let {
            renderStrategies[control.assignee?.first]?.render(
                {
                    styling()
                }, baseClass, id, prefix, context, it
            )
        }
        control.assert()
    }

    open fun renderHelperText(renderContext: RenderContext) {
        renderContext.apply {
            this@FormControlComponent.helperText.value?.let {
                p({
                    this@FormControlComponent.helperTextStyle.value()
                }) { +it }
            }
        }
    }

    open fun renderValidationMessages(renderContext: RenderContext) {
        renderContext.apply {
            stackUp({
                width { "100%" }
            }) {
                spacing { none }
                items {
                    this@FormControlComponent.validationMessagesBuilder?.invoke()?.messages?.renderEach { message ->
                        box({
                            width { "100%" }
                        }) {
                            this@FormControlComponent.validationMessageRendering.value.invoke(this, message)
                        }
                    }
                }
            }
        }
    }
}

interface ControlRenderer {
    fun render(
        styling: BoxParams.() -> Unit = {},
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = "formControl",
        renderContext: RenderContext,
        control: RenderContext.() -> Unit
    )
}

class SingleControlRenderer(private val component: FormControlComponent) : ControlRenderer {
    override fun render(
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String,
        renderContext: RenderContext,
        control: RenderContext.() -> Unit
    ) {
        renderContext.stackUp(
            {
                alignItems { start }
                width { full }
                component.ownSize()()
                styling(this as BoxParams)
            },
            baseClass = baseClass,
            id = id,
            prefix = prefix
        ) {
            spacing { tiny }
            items {
                label({
                    component.labelStyle.value()
                }) {
                    +component.label.value
                }
                control(this)
                component.renderHelperText(this)
                component.renderValidationMessages(this)
            }
        }
    }

}

class ControlGroupRenderer(private val component: FormControlComponent) : ControlRenderer {
    override fun render(
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String,
        renderContext: RenderContext,
        control: RenderContext.() -> Unit
    ) {
        renderContext.box({
            width { full }
        }) {
            fieldset({
                component.ownSize()()
                styling()
            }, baseClass, id, prefix) {
                legend({
                    component.labelStyle.value()
                }) { +component.label.value }
                control(this)
                component.renderHelperText(this)
                component.renderValidationMessages(this)
            }
        }
    }
}


/**
 * This component wraps input elements like [inputField], [selectField], [checkbox], [checkboxGroup], [radioGroup].
 * It enriches those controls with a describing text or label, an optional helper message and also an optional
 * error message. On top it marks a control as _required_ if that should be exposed.
 *
 * The controls themselves offer the same API as if used stand alone. They must be just declared within the build
 * parameter expression of this factory function.
 *
 * Be aware that only one control within a formControl is allowed! If more than one are configured, only the first will
 * get rendered; the remaining ones will be reported as errors in the log.
 *
 * This component can be customized in different ways and thus is quite flexible to...
 * - ... adopt to new input elements
 * - ... get rendered in a new way.
 * In order to achieve this, one can provide new implementations of the rendering strategies or override the control
 * wrapping functions as well. For details have a look at the [ControlRenderer] interface and the control functions
 * [FormControlComponent.inputField], [FormControlComponent.checkbox], [FormControlComponent.checkboxGroup],
 * [FormControlComponent.radioGroup], and [FormControlComponent.selectField].
 *
 * Have a look at some example calls
 * ```
 * // wrap an input field
 * formControl {
 *     label { "Some describing label" }
 *     required { true } // mark the above label with a small red star
 *     helperText { "You can provide a hint here" }
 *     // provide a Flow<ComponentValidationMessage> in order to show some message
 *     errorMessage {
 *         flowOf(errorMessage("id", "Sorry, always wrong in this case"))
 *     }
 *     // just use the appropriate control with its specific API!
 *     inputField(value = someStore) {
 *         placeholder("Some text to type")
 *     }
 * }
 *
 * // providing more than one control results in errors:
 * // - the first will get rendered
 * // - starting with the second all others will be logged as errors
 * formControl {
 *     // leave out label and so on
 *     // ...
 *     // first control function called -> ok, will get rendered
 *     inputField(value = someStore) {
 *         placeholder("Some text to type")
 *     }
 *     // second call -> more than one control -> will not get rendered, but instead be logged as error!
 *     checkBox {
 *          checked { someStore.data }
 *          events {
 *              changes.states() handledBy someStore.someHandler
 *          }
 *     }
 *     // probably more calls to controls -> also reported as errors!
 * }
 * ```
 *
 * For details about the configuration options, have a look at [FormControlComponent].
 *
 * @see FormControlComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [FormControlComponent]
 */
fun RenderContext.formControl(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "formControl",
    build: FormControlComponent.() -> Unit = {}
) {
    FormControlComponent().apply(build).render(this, styling, baseClass, id, prefix)
}

