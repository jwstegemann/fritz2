package dev.fritz2.components

import dev.fritz2.components.validation.ComponentValidationMessage
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.*
import dev.fritz2.styling.span
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
 * The alert's icon can manually be overridden by setting the respective dsl property.
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
 * }
 * ```
 */
open class AlertComponent : Component<Unit> {

    companion object {
        private val alertCss = style("alert") {
            display { flex }
            alignItems { center }
        }

        private val alertContentCss = style("alert-content") {
            display { inlineBlock }
            width { "100%" }
            lineHeight { "1.2em" }
        }
    }

    enum class AlertVariant {
        SOLID, SUBTLE, LEFT_ACCENT, TOP_ACCENT, DISCREET
    }

    object VariantContext {
        val solid = AlertVariant.SOLID
        val subtle = AlertVariant.SUBTLE
        val leftAccent = AlertVariant.LEFT_ACCENT
        val topAccent = AlertVariant.TOP_ACCENT
        val discreet = AlertVariant.DISCREET
    }

    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(value = null)
    val severity = ComponentProperty<(AlertSeverities.() -> AlertSeverity)> { info }
    val variant = ComponentProperty<VariantContext.() -> AlertVariant> { solid }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { normal }
    val stacking = ComponentProperty<AlertStacking.() -> Style<BasicParams>> { separated }

    private val actualIcon: IconDefinition
        get() = icon.value?.invoke(Theme().icons)
            ?: severity.value(Theme().alert.severities).icon


    private var title: (RenderContext.() -> Unit)? = null

    fun title(value: RenderContext.() -> Unit) {
        title = value
    }

    fun title(value: Flow<String>) {
        title {
            span({
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
            div({
                this@AlertComponent.size.value(Theme().alert.sizes)()
                this@AlertComponent.stacking.value(Theme().alert.stacking)()
                when (this@AlertComponent.variant.value(VariantContext)) {
                    AlertVariant.SOLID -> Theme().alert.variants.solid
                    AlertVariant.SUBTLE -> Theme().alert.variants.subtle
                    AlertVariant.LEFT_ACCENT -> Theme().alert.variants.leftAccent
                    AlertVariant.TOP_ACCENT -> Theme().alert.variants.topAccent
                    AlertVariant.DISCREET -> Theme().alert.variants.discreet
                }.invoke(this, this@AlertComponent.severity.value(Theme().alert.severities).colorScheme)
                styling()
            }, alertCss) {
                div({
                    display { inlineFlex }
                    css("margin-right: var(--al-icon-margin)")
                }) {
                    icon({
                        css("width: var(--al-icon-size)")
                        css("height: var(--al-icon-size)")
                    }) {
                        fromTheme { this@AlertComponent.actualIcon }
                    }
                }

                div(alertContentCss.name) {
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
 * Custom styling via the [styling] parameter is optionally supported, as well as any other customization via the
 * [build]-lambda.
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
 * The alert's severity and content are determined from the validation message's properties.
 * Customization of the underlying [AlertComponent] is supported via the [build]-lambda. Custom styling can be applyied
 * via the overloaded [ComponentValidationMessage.asAlert] method.
 *
 * @param renderContext RenderContext to render the alert in
 * @param build a lambda expression for setting up the component itself
 */
fun ComponentValidationMessage.asAlert(
    renderContext: RenderContext,
    build: AlertComponent.() -> Unit = { }
) = asAlert({ }, renderContext, build)
