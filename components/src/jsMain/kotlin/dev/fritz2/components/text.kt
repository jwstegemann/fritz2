package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.P
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use


inline fun HtmlElements.text(
    styles: Style<BasicStyleParams> = {},
    crossinline init: P.() -> Any = {}
): P {
    //console.log("#### $styles")
    return p(use(styles, "text")) {
        init()
    }

}
