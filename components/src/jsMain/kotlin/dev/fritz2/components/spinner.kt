package dev.fritz2.components

/*
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

 */