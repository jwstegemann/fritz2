package dev.fritz2.components

import dev.fritz2.components.spinner.SpinnerComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


/**
 * This component generates an animated spinner. The spinner is either pure CSS (just a rotating curved border segment)
 * or [icon] based.
 *
 * You can customize the animation speed in CSS notation (``s`` or ``ms`` suffix is needed!).
 *
 * If the spinner is made of pure CSS, you can configure the size of the border on top.
 *
 * ```
 * // pure CSS spinner
 * spinner {
 *      size { fat } // really, really fat spinner!
 * }
 *
 * // icon based
 * spinner {
 *      icon { star }
 * }
 * ```
 *
 * @see SpinnerComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SpinnerComponent]
 */
fun RenderContext.spinner(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "spinner",
    build: SpinnerComponent.() -> Unit
) {
    SpinnerComponent().apply(build).render(this, styling, baseClass, id, prefix)
}