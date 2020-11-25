package dev.fritz2.components

import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.StyleParams
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.TooltipPlacements

fun StyleParams.tooltip(vararg text: String) = Theme().tooltip.write(*text)

fun StyleParams.tooltip(vararg text: String, tooltipPlacement: TooltipPlacements.() -> Style<BasicParams>) =
    Theme().tooltip.write(*text, tooltipPlacement = tooltipPlacement)
