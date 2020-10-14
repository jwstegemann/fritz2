package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Img
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use

fun HtmlElements.f2Image(build: Context<BasicComponentContext> = {}): Component<Img> {
    val context = BasicComponentContext("f2Image").apply(build)

    //FIXME: how to deal with attributes we want to add things to from component and init?
    return Component { init -> img(context.cssClass, content = init) }
}
