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

    fun icon(value: IconDefinition) {
        icon = value
    }

    fun title(value: Flow<String>) {
        title = {
            (::span.styled {
                margins { right { small } }
            }) {
                value.asText()
            }
        }
    }
    fun title(value: String) = title(flowOf(value))

    fun content(value: Flow<String>) {
        content = {
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
        val variantStyle = alertVariant.invoke(alertSeverity)

        renderContext.apply {
            (::div.styled {
                height { "3rem" }
                color { Theme().colors.light }
                variantStyle.background()
            }) {
                (::span.styled {
                    width { "1.5rem" }
                    height { "1.5rem" }
                    margins {
                        left { small }
                        right { small }
                    }
                    variantStyle.accent()
                }) {
                    icon({
                        height { "100%" }
                    }) {
                        fromTheme { icon }
                    }
                }

                (::span.styled {
                    display { inlineBlock }
                    verticalAlign { middle }
                    variantStyle.text()
                }) {
                    title?.invoke(this)
                    content?.invoke(this)
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