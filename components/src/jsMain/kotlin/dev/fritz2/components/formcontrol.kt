package dev.fritz2.components

import dev.fritz2.components.forms.control.ControlRenderer
import dev.fritz2.components.forms.control.FormControlComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


/**
 * This component wraps input elements like [inputField], selectField, [checkbox], [checkboxGroup], [radioGroup].
 * It enriches those controls with a describing text or label, an optional helper message and also an optional
 * error message. On top it marks a control as _required_ if that should be exposed.
 *
 * It is recommended to embed a formControl into a [formGroup], which enables a richer control to the overall
 * layout of a whole form.
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
