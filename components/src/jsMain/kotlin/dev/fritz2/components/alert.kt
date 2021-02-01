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

fun RenderContext.alert(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "alert",
    build: AlertComponent.() -> Unit,
) = AlertComponent().apply(build).show(this, styling, baseClass, id, prefix)

fun RenderContext.alertToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "alert",
    toastBuild: ToastComponent.() -> Unit = { },
    build: AlertComponent.() -> Unit,
): SimpleHandler<Unit> {

    val pendingToastStore = object : RootStore<AddToast>({}) {
        val show = handle {
            showAlertToast(styling, baseClass, id, prefix, toastBuild, build)
            it
        }
    }
    return pendingToastStore.show
}

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

fun ComponentValidationMessage.showAsToast(renderContext: RenderContext) {
    renderContext.showAlertToast {
        severity { when(severity) {
            Severity.Info -> info
            Severity.Warning -> warning
            Severity.Error -> error
        } }
        content(this@showAsToast.message)
    }
}