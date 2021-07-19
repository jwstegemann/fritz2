package dev.fritz2.components

import dev.fritz2.components.alert.AlertComponent
import dev.fritz2.components.validation.ComponentValidationMessage
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


/**
 * Creates an alert and renders it right away.
 *
 * Usage example:
 * ```
 * alert {
 *     title("Alert")
 *     content("This is an alert.")
 *     severity { info }
 * }
 * ```
 *
 * @see AlertComponent
 *
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id the ID of the toast element
 * @param prefix the prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.alert(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "alert",
    build: AlertComponent.() -> Unit,
) {
    AlertComponent().apply(build).render(this, styling, baseClass, id, prefix)
}


/**
 * Convenience extension function to display a [ComponentValidationMessage] as an alert.
 *
 * The alert's severity and content are determined from the validation message's properties.
 * Custom styling via the [styling] parameter is optionally supported, as well as any other customization via the
 * [build]-lambda.
 *
 * Usage example:
 * ```
 * val message = infoMessage("SomeId", "This is a simple info message.")
 *
 * // within some `RenderContext` so `this` refers to it
 * message.asAlert(this)
 * ```
 *
 * @see AlertComponent
 *
 * @param renderContext RenderContext to render the alert in
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param build a lambda expression for setting up the component itself
 */
fun ComponentValidationMessage.asAlert(
    styling: BasicParams.() -> Unit = { },
    renderContext: RenderContext,
    build: AlertComponent.() -> Unit = { }
) {
    val receiver = this
    renderContext.alert(styling) {
        severity {
            when (receiver.severity) {
                Severity.Info -> info
                Severity.Success -> success
                Severity.Warning -> warning
                Severity.Error -> error
            }
        }
        content(message)
        build()
    }
}

/**
 * Convenience extension to display a [ComponentValidationMessage] as an alert.
 *
 * The alert's severity and content are determined from the validation message's properties.
 * Customization of the underlying [AlertComponent] is supported via the [build]-lambda.
 *
 * This variant does not offer custom styling. The latter can be applied via the overloaded
 * [ComponentValidationMessage.asAlert] method.
 *
 * Usage example:
 * ```
 * val message = infoMessage("SomeId", "This is a simple info message.")
 *
 * // within some `RenderContext` so `this` refers to it
 * message.asAlert(this)
 * ```
 *
 * @see AlertComponent
 *
 * @param renderContext RenderContext to render the alert in
 * @param build a lambda expression for setting up the component itself
 */
fun ComponentValidationMessage.asAlert(
    renderContext: RenderContext,
    build: AlertComponent.() -> Unit = { }
) = asAlert({ }, renderContext, build)
