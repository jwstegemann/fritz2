package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.validation.ComponentValidationMessage
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * A component to display an alert consisting of an icon, title and description.
 * Different styles based on severities are supported, as well as a number of different layout options.
 *
 * Currently the following severities are available:
 * - Info
 * - Success
 * - Warning
 * - Error
 * Specifying a severity will change the alert's color scheme based on the colors defined in the application theme.
 * If no severity is specified, 'info' will be used by default.
 *
 * Additionally, a number of different layout options are available. These are:
 * - 'Subtle': A subtle style using different shades of the severity's base color defined in the application theme.
 * - 'Solid': A solid style using the severity's color from the application theme and a solid white color for the icon,
 * text and decorations.
 * - 'Top-Accent': A variation of the 'Subtle' variant with a decoration element at the top.
 * - 'Left-Accent': A variation of the 'Subtle' variant with a decoration element on the left.
 * If no variant is specified, 'Subtle' is used by default.
 *
 * Usage examples:
 * ```
 * alert {
 *     title("Alert")
 *     content("This is an alert.")
 *     severity { info }
 * }
 *
 * alert {
 *     title("Alert")
 *     content("This is an alert.")
 *     severity { error }
 *     variant { leftAccent }
 * }
 * ```
 */
@ComponentMarker
class AlertComponent {

    companion object {
        private const val accentDecorationThickness = "4px"
    }

    private var severity: AlertSeverities.() -> AlertSeverity = { info }
    private var variant: AlertVariants.() -> AlertVariantStyleFactory = { subtle }
    val variantStyles: AlertVariantStyles
        get() {
            val alertSeverity = severity.invoke(Theme().alert.severities)
            val alertVariantFactory = variant.invoke(Theme().alert.variants)
            return alertVariantFactory.invoke(alertSeverity)
        }

    private var icon: IconDefinition = Theme().icons.circleInformation
    private var title: (RenderContext.() -> Unit)? = null
    private var content: (RenderContext.() -> Unit)? = null


    fun severity(value: AlertSeverities.() -> AlertSeverity) {
        severity = value
    }

    @Suppress("unused")
    fun variant(value: AlertVariants.() -> AlertVariantStyleFactory) {
        variant = value
    }

    fun icon(value: Icons.() -> IconDefinition) {
        icon = Theme().icons.value()
    }

    fun title(value: RenderContext.() -> Unit) {
        title = value
    }
    fun title(value: Flow<String>) {
        title {
            (::span.styled {
                margins { right { smaller } }
                fontWeight { bold }
            }) {
                value.asText()
            }
        }
    }
    @Suppress("unused")
    fun title(value: String) = title(flowOf(value))

    fun content(value: RenderContext.() -> Unit) {
        content = value
    }
    fun content(value: Flow<String>) {
        content {
            (::span.styled {
                // To be added
            }) {
                value.asText()
            }
        }
    }
    fun content(value: String) = content(flowOf(value))


    fun show(
        renderContext: RenderContext,
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "alert",
    ) {
        val styles = variantStyles

        renderContext.apply {
            (::div.styled(baseClass = baseClass, id = id, prefix = prefix) {
                styling()
                display { flex }
                position { relative { } }
                styles.background()
            }) {
                (::div.styled {
                    width { "100%" }
                    height { accentDecorationThickness }
                    position { absolute { } }
                    styles.decorationTop()
                }) { }

                (::div.styled {
                    width { accentDecorationThickness }
                    height { "100%" }
                    position { absolute { } }
                    styles.decorationLeft()
                }) { }

                (::div.styled {
                    margin { normal }
                    display { flex }
                    css("flex-direction: row")
                    alignItems { center }
                }) {
                    (::div.styled {
                        margins { right { small } }
                        styles.accent()
                    }) {
                        icon {
                            fromTheme { icon }
                        }
                    }

                    (::div.styled {
                        display { inlineBlock }
                        verticalAlign { middle }
                        width { "100%" }
                        lineHeight { "1.2em" }
                        styles.text()
                    }) {
                        title?.invoke(this)
                        content?.invoke(this)
                    }
                }
            }
        }
    }
}

/**
 * Creates an alert and renders it right away.
 *
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id the ID of the toast element
 * @param prefix the prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.alert(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "alert",
    build: AlertComponent.() -> Unit,
) = AlertComponent().apply(build).show(this, styling, baseClass, id, prefix)

/**
 * Creates and alert and returns a handler that displays it in a toast message when invoked.
 * Use [showAlertToast] to display the toast message right away.
 * The toast's theme properties are automatically set but can manually be overridden by passing [toastBuild].
 *
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id the ID of the toast element
 * @param prefix the prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param toastBuild a lambda expression for setting up the toast containing the alert
 * @param build a lambda expression for setting up the alert component
 */
fun RenderContext.alertToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "alert",
    toastBuild: ToastComponent.() -> Unit = { },
    build: AlertComponent.() -> Unit,
): SimpleHandler<Unit> {

    val pendingToastStore = object : RootStore<AddToast>({ }) {
        val show = handle {
            showAlertToast(styling, baseClass, id, prefix, toastBuild, build)
            it
        }
    }
    return pendingToastStore.show
}

/**
 * Creates and alert and displays it as a toast message.
 * The toast's theme properties are automatically set but can manually be overridden by passing [toastBuild].
 *
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id the ID of the toast element
 * @param prefix the prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param toastBuild a lambda expression for setting up the toast containing the alert
 * @param build a lambda expression for setting up the alert component
 */
fun RenderContext.showAlertToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "alert",
    toastBuild: ToastComponent.() -> Unit = { },
    build: AlertComponent.() -> Unit,
) {
    val alert = AlertComponent().apply(build)

    showToast {
        toastBuild()
        content {
            alert.show(
                this,
                styling = {
                    styling()
                    paddings { right { giant } }
                },
                baseClass,
                id,
                prefix
            )
        }
        closeButtonStyle {
            alert.variantStyles.text()
        }
    }
}

/**
 * Convenience extension to display a [ComponentValidationMessage] as an alert.
 * The alert's severity and content are determined from the validation message's properties.
 *
 * @param renderContext RenderContext to render the alert in.
 */
fun ComponentValidationMessage.asAlert(renderContext: RenderContext) {
    renderContext.alert {
        severity { when(severity) {
            Severity.Info -> info
            Severity.Warning -> warning
            Severity.Error -> error
        } }
        content(message)
    }
}