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

fun RenderContext.tooltip(
    styling: BasicParams.() -> Unit = {},
    text: String? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-tooltip-${randomId()}",
    prefix: String = "tooltip",
    build: TooltipComponent.() -> Unit
) = TooltipComponent(text).apply(build).render(this, styling, baseClass, id, prefix)

fun RenderContext.tooltip(
    text: String,
    build: TooltipComponent.() -> Unit
) {
    tooltip({}, text) {
        build()
    }
}

fun RenderContext.tooltip(
    vararg text: String,
    build: TooltipComponent.() -> Unit
) {
    tooltip({}) {
        build()
        text(*text)
    }
}