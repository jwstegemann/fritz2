package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.GridParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi


//TODO: typealias
fun HtmlElements.Box(build: BasicComponentContext.() -> Unit = {}): Component<Div> {
    val context = BasicComponentContext("box").apply(build)

    return Component { init ->
        div(context.cssClass, content = init)
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