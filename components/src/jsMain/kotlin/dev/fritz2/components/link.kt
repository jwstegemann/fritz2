package dev.fritz2.components

import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle

val link = staticStyle(
    "link",
    """
    transition: all 0.15s ease-out;
    cursor: pointer;
    textDecoration: none;
    outline: none;
    color: inherit;
    
    &:hover {
        textDecoration: underline;    
    }

    &:focus {
        boxShadow: outline;
    }
    
    &:disabled {
        opacity: 0.4;
        cursor: not-allowed;
        textDecoration: none;
    }
"""
)

inline fun HtmlElements.link(
    styles: Style<BasicStyleParams> = {},
    crossinline init: A.() -> Any
): A {

    return a("$link ${use(styles, "link")}") {
        init()
    }

}
