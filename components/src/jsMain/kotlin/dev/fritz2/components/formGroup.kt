package dev.fritz2.components

import dev.fritz2.components.forms.control.FormControlComponent
import dev.fritz2.components.forms.group.FormGroupComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * This component is meant to act a a grouping component for [formControl]s.
 *
 * It controls the overall layout, that is whether the labels are rendered on top of their controls or on the left
 * hand side. In the latter case also the space of the label area can be controlled, so that the whole form offers
 * a consistent visual appearance.
 *
 * This component supports responsiveness out of the box. The above features can be all configured in a different
 * manner for each breakpoint (sm, md, lg and xl) to offer the best UX for a device.
 *
 * Example call:
 * ```kotlin
 * formGroup {
 *     items {
 *         formControl {
 *             label("Password")
 *             helperText("Tip: Choose a long password")
 *             inputField { type("password") }
 *         }
 *         formControl {
 *             // ...
 *         }
 *         // some more formControls
 *     }
 * }
 * ```
 *
 * In order to place the labels on the left side of the control, use the ``labels`` context:
 * ```kotlin
 * formGroup {
 *     labels {
 *         placement { left } // ``top`` is default (see above example)
 *         width("200px") // ``auto`` is default, could also be ``vw``, ``%`` or ``rem`` and alike
 *     }
 *     items {
 *         formControl {
 *             // ...
 *         }
 *         // some more formControls
 *     }
 * }
 * ```
 *
 * To adapt the form layout to the device size, use the overloaded variant of the ``labels`` context:
 * - on small devices place the labels on top of the control
 * - from medium devices upwards place the labels on the left side of the controls
 * ```kotlin
 * formGroup {
 *     labels(
 *         sm = {
 *             placement { top }
 *         },
 *         md = {
 *             placement { left }
 *             width("200px")
 *         }
 *     )
 *     items {
 *         formControl {
 *             // ...
 *         }
 *         // some more formControls
 *     }
 * }
 * ```
 *
 * @see FormGroupComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [FormGroupComponent]
 */
fun RenderContext.formGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "formGroup",
    build: FormGroupComponent.() -> Unit = {}
) {
    FormGroupComponent().apply(build).render(this, styling, baseClass, id, prefix)
}