package dev.fritz2.components

import dev.fritz2.components.tooltip.TooltipComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.Id
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
 * This factory function creates a Tooltip.
 *
 * A `tooltip` should be used to display fast information for the user.
 * The individual `text` will be shown on hovering the `RenderContext`'s element in which it is called.
 *
 * Example usage:
 * ```
 *   span {
 *   +"hover me"
 *   tooltip {
 *       text("some tooltip text")
 *       placement { bottomEnd }
 *   }
 *  }
 * ```
 *
 * There exist also one convenience functions, that allow a terser creation! This one offers the whole freedom
 * to provide the common component's parameters though.
 *
 * Especially dynamic text content (`Flow<String>`) is only possible to set up within the context itself.
 *
 * @see TooltipComponent
 */
fun RenderContext.tooltip(
    styling: BasicParams.() -> Unit = {},
    text: String? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-tooltip-${Id.next()}",
    prefix: String = "tooltip",
    build: TooltipComponent.() -> Unit
) = TooltipComponent(text).apply(build).render(this, styling, baseClass, id, prefix)

/**
 * `tooltip` factory function that allow a terser creation for just static text as tooltip's content.
 *
 * Example usage:
 * ```
 *   span {
 *      +"hover me"
 *      tooltip("my Tooltip") { }
 *   }
 * ```
 *
 * One can configure the placement too:
 * ```
 *   span {
 *      +"hover me"
 *      tooltip("my Tooltip") { placement { bottom } }
 *   }
 * ```
 *
 * One can pass multiple text values as parameters, which will result in multi line tooltip:
 * ```
 *   span {
 *      +"hover me"
 *      tooltip("first line", "second line") { }
 *   }
 * ```
 *
 * @see TooltipComponent
 *
 * @param text the content of the tooltip; pass multiple text parameters to create multi line content
 * @param build a lambda expression for setting up the component itself
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