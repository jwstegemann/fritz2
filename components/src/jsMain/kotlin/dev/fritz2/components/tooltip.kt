package dev.fritz2.components

import dev.fritz2.components.foundations.randomId
import dev.fritz2.components.popup.Placement
import dev.fritz2.components.tooltip.TooltipComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.StyleParams
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.TooltipPlacements

@Deprecated("since 0.12 - please use TooltipComponent")
fun StyleParams.tooltip(vararg text: String) = Theme().tooltip.write(*text)

@Deprecated("since 0.12 - please use TooltipComponent")
fun StyleParams.tooltip(vararg text: String, tooltipPlacement: TooltipPlacements.() -> Style<BasicParams>) =
    Theme().tooltip.write(*text, tooltipPlacement = tooltipPlacement)

/**
 * This component creates a Tooltip
 * A `tooltip` should be used to display fast information for the user.
 * The individual `text` will be shown on hover the `RenderContext` in which be called.
 *
 * Example usage:
 * ```
 *   tooltip(text = "my Tooltip") { }
 *
 *   tooltip({
 *       color { danger.mainContrast }
 *       background {
 *           color { danger.main }
 *       }
 *   }) {
 *       text(listOf("first line, second line"))
 *       placement { bottomEnd }
 *   }
 * ```
 *
 * @see TooltipComponent
 */
fun RenderContext.tooltip(
    styling: BasicParams.() -> Unit = {},
    text: String? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-tooltip-${randomId()}",
    prefix: String = "tooltip",
    build: TooltipComponent.() -> Unit
) = TooltipComponent(text).apply(build).render(this, styling, baseClass, id, prefix)


/**
 * `tooltip` convenience function
 *
 * Example usage:
 * ```
 *   tooltip("my Tooltip") { }
 *   tooltip("my Tooltip") { placement { bottom } }
 * ```
 *
 * @see TooltipComponent
 */
fun RenderContext.tooltip(
    text: String,
    build: TooltipComponent.() -> Unit
) {
    tooltip({}, text) {
        build()
    }
}

/**
 * `tooltip` convenience function
 *
 * Example usage:
 * ```
 *   tooltip("my Tooltip", "second tooltip line") { }
 * ```
 *
 *  @see TooltipComponent
 */
fun RenderContext.tooltip(
    vararg text: String,
    build: TooltipComponent.() -> Unit
) {
    tooltip({}) {
        build()
        text(*text)
    }
}