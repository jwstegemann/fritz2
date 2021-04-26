package dev.fritz2.components

import dev.fritz2.components.validation.ComponentValidationMessage
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.style
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
 * Both the alert's icon and color can manually be overridden by setting the respective dsl property.
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
 *
 * alert {
 *     title("Alert")
 *     content("This is an alert.")
 *     icon { fritz2 }
 *     color { primary }
 * }
 * ```
 */
open class AlertComponent : Component<Unit> {

    companion object {
        private val alertCss = style("alert") {
            alignItems { center }
            padding { normal }
        }

        private val alertContentCss = style("alert-content") {
            display { inlineBlock }
            verticalAlign { middle }
            width { "100%" }
            lineHeight { "1.2em" }
        }
    }

    enum class AlertVariant {
        SOLID, SUBTLE, LEFT_ACCENT, TOP_ACCENT, DISCREET
    }

    object VariantContext {
        val solid : AlertVariant = AlertVariant.SOLID
        val subtle = AlertVariant.SUBTLE
        val leftAccent = AlertVariant.LEFT_ACCENT
        val topAccent = AlertVariant.TOP_ACCENT
        val discreet = AlertVariant.DISCREET
    }

    // icon and color override the values set in the AlertSeverity style (theme)
    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(value = null)
    val color = ComponentProperty<(Colors.() -> ColorScheme)?>(value = null)
    val severity = ComponentProperty<(AlertSeverities.() -> AlertSeverity)> { info }
    val variant = ComponentProperty<VariantContext.() -> AlertVariant> { solid }
    val sizes = ComponentProperty<FormSizes.() -> Style<BasicParams>> { normal }
    val stacking = ComponentProperty<AlertStacking.() -> Style<BasicParams>> { separated }

    private val actualIcon: IconDefinition
        get() = icon.value?.invoke(Theme().icons)
            ?: severity.value(Theme().alert.severities).icon

    private val actualColorScheme: ColorScheme
        get() = color.value?.invoke(Theme().colors)
            ?: severity.value(Theme().alert.severities).colorScheme


    private var title: (RenderContext.() -> Unit)? = null

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


    private var content: (RenderContext.() -> Unit)? = null

    fun content(value: RenderContext.() -> Unit) {
        content = value
    }

    fun content(value: Flow<String>) {
        content {
            span { value.asText() }
        }
    }

    fun content(value: String) = content(flowOf(value))


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String,
    ) {
        context.apply {
            flexBox(baseClass = alertCss, styling = {
                this@AlertComponent.sizes.value(Theme().alert.sizes)()
                this@AlertComponent.stacking.value(Theme().alert.stacking)()

                when(this@AlertComponent.variant.value(VariantContext)) {
                    VariantContext.solid -> Theme().alert.variants.solid
                    VariantContext.subtle -> Theme().alert.variants.subtle
                    VariantContext.leftAccent -> Theme().alert.variants.leftAccent
                    VariantContext.topAccent -> Theme().alert.variants.topAccent
                    VariantContext.discreet -> Theme().alert.variants.discreet
                    else -> Theme().alert.variants.solid
                }.invoke(this, this@AlertComponent.actualColorScheme)
            }) {
                box(styling = {
                    css("margin-right: var(--al-icon-margin)")
                }) {
                    icon({
                        css("width: var(--al-icon-size)")
                        css("height: var(--al-icon-size)")
                    }) {
                        fromTheme { this@AlertComponent.actualIcon }
                    }
                }

                box(baseClass = alertContentCss) {
                    this@AlertComponent.title?.invoke(this)
                    this@AlertComponent.content?.invoke(this)
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
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "alert",
    build: AlertComponent.() -> Unit,
) {
    AlertComponent().apply(build).render(this, styling, baseClass, id, prefix)
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
        variant { discreet }
        sizes { size() }
        stacking { stacking() }
        content(message)
    }
}
