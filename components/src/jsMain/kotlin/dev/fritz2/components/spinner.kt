package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.currentTheme


val spinnerFoundations = staticStyle(
    "spinner",
    """ 
    display: inline-block;
    border-color: currentColor;
    border-style: solid;
    border-radius: 99999px;
    border-bottom-color: transparent;
    border-left-color: transparent;
    color: currentColor;
"""
)

inline fun HtmlElements.Spinner(
    crossinline styles: Style<BasicParams> = {},
    size: Property = currentTheme.borderWidths.normal
): Div {
    val spinnerStyles: Style<BasicParams> = {
        css("animation: loading 0.6s linear infinite;")
        border { width { size } }
        width { "1rem" }
        height { "1rem" }
    }

    return div(use(styles, "spinner")) {
        div("$spinnerFoundations ${use(spinnerStyles, "spinner")}") {}
    }
}