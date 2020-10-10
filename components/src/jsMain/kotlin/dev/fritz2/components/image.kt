package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Img
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use


inline fun HtmlElements.Image(
    styles: Style<BasicStyleParams> = {},
    crossinline init: Img.() -> Unit
): Img {

    //FIXME: how to deal with attributes we want to add things to from component and init?
    return img(use(styles, "img")) {
        init()
    }

}
