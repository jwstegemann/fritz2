package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.validation.ComponentValidationMessage
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
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
 * Specifying a severity will change the alert's color scheme based on the colors defined in the application theme as
 * well as the icon displayed. If no severity is specified, 'info' will be used by default.
 * The alert's icon can manually be set via the 'icon'-property in which case the severity's icon will be ignored.
 *
 * Additionally, a number of different layout options are available. These are:
 * - 'subtle': A subtle style using different shades of the severity's base color defined in the application theme.
 * - 'solid': A solid style using the severity's color from the application theme and a solid white color for the icon,
 * text and decorations.
 * - 'Top-Accent': A variation of the 'subtle' variant with a decoration element at the top.
 * - 'Left-Accent': A variation of the 'subtle' variant with a decoration element on the left.
 * If no variant is specified, 'solid' is used by default.
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

    val sizes = ComponentProperty<FormSizes.() -> Style<BasicParams>> { normal }
    val stacking = ComponentProperty<AlertStacking.() -> Style<BasicParams>> { separated }
    val severity = ComponentProperty<AlertSeverities.() -> AlertSeverity> { info }
    val variant = ComponentProperty<AlertVariants.() -> AlertVariantStyleFactory> { solid }

    val variantStyles: AlertVariantStyles
        get() {
            val alertSeverity = severity.value.invoke(Theme().alert.severities)
            val alertVariantFactory = variant.value.invoke(Theme().alert.variants)
            return alertVariantFactory.invoke(alertSeverity.color)
        }

    // the icon specified in AlertSeverity is used if no icon is specified manually
    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(value = null)

    private var title: (RenderContext.() -> Unit)? = null
    private var content: (RenderContext.() -> Unit)? = null

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
                    display { flex }
                    css("flex-direction: row")
                    alignItems { center }
                    sizes.value(Theme().alert.sizes)()
                    stacking.value(Theme().alert.stacking)()
                }) {
                    (::div.styled {
                        css("margin-right: var(--al-icon-margin)")
                        styles.accent()
                    }) {
                        icon({
                            css("width: var(--al-icon-size)")
                            css("height: var(--al-icon-size)")
                        }) {
                            fromTheme {
                                icon.value
                                    ?.invoke(Theme().icons)
                                    ?: severity.value(Theme().alert.severities).icon
                            }
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

    val pendingToastStore = object : RootStore<Unit>(Unit) {
        val show = handle {
            showAlertToast(styling, baseClass, id, prefix, toastBuild, build)
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
 * @param size Optional property for the text and icon size.
 * @param stacking Optional property for the margins around one alert.
 */
fun ComponentValidationMessage.asAlert(
    renderContext: RenderContext,
    size: FormSizes.() -> Style<BasicParams> = { normal },
    stacking: AlertStacking.() -> Style<BasicParams> = { separated }
) {
    val receiver = this
    renderContext.alert {
        severity {
            when (receiver.severity) {
                Severity.Info -> info
                Severity.Success -> success
                Severity.Warning -> warning
                Severity.Error -> error
            }
        }
        icon {
            when (receiver.severity) {
                Severity.Info -> circleInformation
                Severity.Success -> circleCheck
                Severity.Warning -> circleWarning
                Severity.Error -> circleError
            }
        }
        variant { discreet }
        sizes { size() }
        stacking { stacking() }
        content(message)
    }
}