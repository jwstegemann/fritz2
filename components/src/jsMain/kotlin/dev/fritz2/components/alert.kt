package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

typealias Severity = (Colors.() -> ColorProperty)
typealias Variant = (AlertVariants.() -> ((ColorProperty) -> AlertVariant))

// TODO: Add support for the 'leftAccent' and 'topAccent' variants
@ComponentMarker
class AlertComponent {

    companion object {
        private const val accentDecorationThickness = "4px"
    }

    private var severity: Severity = { info }
    private var variant: Variant = { subtle }

    private var icon: IconDefinition = Theme().icons.circleInformation
    private var title: (RenderContext.() -> Unit)? = null
    private var content: (RenderContext.() -> Unit)? = null


    fun severity(value: Severity) {
        severity = value
    }

    fun variant(value: Variant) {
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


    fun show(renderContext: RenderContext) {
        val alertSeverity = severity.invoke(Theme().colors)
        val alertVariant = variant.invoke(Theme().alert.variants)
        val alertStyle = alertVariant.invoke(alertSeverity)

        renderContext.apply {
            (::div.styled {
                display { flex }
                position { relative { } }
                alertStyle.background()
            }) {
                (::div.styled {
                    width { "100%" }
                    height { accentDecorationThickness }
                    position { absolute { } }
                    alertStyle.decorationTop()
                }) { }

                (::div.styled {
                    width { accentDecorationThickness }
                    height { "100%" }
                    position { absolute { } }
                    alertStyle.decorationLeft()
                }) { }

                (::div.styled {
                    margin { normal }
                    display { flex }
                    css("flex-direction: row")
                    css("align-items: center")
                }) {
                    (::div.styled {
                        margins { right { small } }
                        alertStyle.accent()
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
                        alertStyle.text()
                    }) {
                        title?.invoke(this)
                        content?.invoke(this)
                    }
                }
            }
        }
    }
}

// TODO: Use additional params
fun RenderContext.alert(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "alert",
    build: AlertComponent.() -> Unit,
) = AlertComponent().apply(build).show(this)