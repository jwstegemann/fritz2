package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
inline fun HtmlElements.Box(
    styles: Style<BoxParams> = {},
    cssClasses: String = "",
    crossinline init: Div.() -> Unit
): Div {

    return div(cssClasses + use(styles, "box")) {
        init()
    }

}

val flex = staticStyle(
    "flex",
    "display: flex;"
)

@ExperimentalCoroutinesApi
inline fun HtmlElements.Flex(
    styles: Style<FlexParams> = {},
    crossinline init: Div.() -> Unit
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
inline fun HtmlElements.Grid(
    styles: Style<GridParams> = {},
    crossinline init: Div.() -> Unit
): Div {

    return div("$grid ${use(styles, "grid")}") {
        init()
    }

}