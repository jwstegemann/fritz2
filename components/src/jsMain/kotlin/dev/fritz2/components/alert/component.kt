package dev.fritz2.components.alert

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.icon
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
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
 * - 'leftAccent': A variation of the 'subtle' variant with a decoration element at the top.
 * - 'topAccent': A variation of the 'subtle' variant with a decoration element on the left.
 * - 'ghost': This variant does not have any decoration besides the icon (no background, similar to 'ghost' in push-buttons)
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

    enum class Variant {
        SOLID, SUBTLE, LEFT_ACCENT, TOP_ACCENT, GHOST
    }

    object VariantContext {
        val solid = Variant.SOLID
        val subtle = Variant.SUBTLE
        val leftAccent = Variant.LEFT_ACCENT
        val topAccent = Variant.TOP_ACCENT
        val ghost = Variant.GHOST
    }

    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)
    val severity = ComponentProperty<(AlertSeverities.() -> AlertSeverity)> { info }
    val variant = ComponentProperty<VariantContext.() -> Variant> { solid }
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
                    Variant.SOLID -> Theme().alert.variants.solid
                    Variant.SUBTLE -> Theme().alert.variants.subtle
                    Variant.LEFT_ACCENT -> Theme().alert.variants.leftAccent
                    Variant.TOP_ACCENT -> Theme().alert.variants.topAccent
                    Variant.GHOST -> Theme().alert.variants.ghost
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