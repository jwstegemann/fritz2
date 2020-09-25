package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Select
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle

val select = staticStyle(
    "select",
    """
        width: 100%;
        outline: 0;
        position: relative;
        appearance: none;
        border: 1px solid;
        border-color: inherit;
        background-color: white;
        transition: all 0.2s,

"""
)

inline fun HtmlElements.sel(
    styles: Style<BasicStyleParams> = {},
    crossinline init: Select.() -> Any
): Select {

    return select("$select ${use(styles, "select")}") {
        init()
    }

}
