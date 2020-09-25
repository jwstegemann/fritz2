package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
inline fun HtmlElements.box(
    styles: Style<BoxStyleParams> = {},
    crossinline init: HtmlElements.() -> Any
): Div {

    return div(use(styles, "box")) {
        init()
    }

}

val flex = staticStyle(
    "flex",
    "display: flex;"
)

@ExperimentalCoroutinesApi
inline fun HtmlElements.flex(
    styles: Style<FlexStyleParams> = {},
    crossinline init: HtmlElements.() -> Any
): Div {

    return div("$flex ${use(styles, "flex")}") {
        init()
    }

}

val grid = staticStyle(
    "grid",
    "display: grid;"
)

@ExperimentalCoroutinesApi
inline fun HtmlElements.grid(
    styles: Style<GridStyleParams> = {},
    crossinline init: HtmlElements.() -> Any
): Div {

    return div("$grid ${use(styles, "grid")}") {
        init()
    }

}