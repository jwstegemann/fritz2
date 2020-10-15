package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.GridParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

// TODO: Think again whether Box is needed. If so, provide BoxParams-Style based Context!
fun HtmlElements.f2Box(build: Context<BasicComponentContext> = {}): Component<Div> {
    val context = BasicComponentContext("f2Box").apply(build)

    return Component { init -> div(context.cssClass, content = init) }
}


open class FlexComponentContext(prefix: String) : BasicComponentContext(prefix), FlexParams {
    companion object Foundation {
        val cssClass = staticStyle(
            "f2Flex",
            "display: flex;"
        )
    }
}

fun HtmlElements.f2Flex(build: Context<FlexComponentContext> = {}): Component<Div> {
    val context = FlexComponentContext("f2Flex").apply(build)

    return Component { init ->
        div("${FlexComponentContext.cssClass} ${context.baseClasses} ${context.cssClass}", content = init)
    }
}


open class GridComponentContext(prefix: String) : BasicComponentContext(prefix), GridParams {
    companion object Foundation {
        val cssClass = staticStyle(
            "f2Grid",
            "display: grid;"
        )
    }
}

fun HtmlElements.f2Grid(build: Context<GridComponentContext> = {}): Component<Div> {
    val context = GridComponentContext("f2Grid").apply(build)

    return Component { init ->
        div("${GridComponentContext.cssClass} ${context.cssClass}", content = init)
    }
}
