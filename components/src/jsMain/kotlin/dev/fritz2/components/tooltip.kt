package dev.fritz2.components

import dev.fritz2.components.foundations.randomId
import dev.fritz2.components.popper.Placement
import dev.fritz2.components.popper.PopperComponent
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

fun RenderContext.tooltip(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-tooltip-${randomId()}",
    prefix: String = "tooltip",
    build: TooltipComponent.() -> Unit
) = TooltipComponent().apply(build).render(this, styling, baseClass, id, prefix)

fun RenderContext.tooltip(
    text: String,
    placement: TooltipComponent.PlacementContext.() -> Placement = { Placement.Top }
) {
    tooltip({}) {
        text(text)
        placement { placement.invoke(TooltipComponent.PlacementContext) }
    }
}

fun RenderContext.tooltip(
    vararg text: String,
    placement: TooltipComponent.PlacementContext.() -> Placement = { Placement.Top }
) {
    tooltip({}) {
        text(*text)
        placement { placement.invoke(TooltipComponent.PlacementContext) }
    }

}