package dev.fritz2.components

import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle

class LinkComponentContext(prefix: String) : BasicComponentContext(prefix) {
    companion object Foundation {
        val cssClass = staticStyle(
            "f2Link",
            """
                    transition: all 0.15s ease-out;
                    cursor: pointer;
                    text-decoration: none;
                    outline: none;
                    color: inherit;
                    
                    &:hover {
                        text-decoration: underline;    
                    }
                
                    &:focus {
                        box-shadow: outline;
                    }
                    
                    &:disabled {
                        opacity: 0.4;
                        cursor: not-allowed;
                        text-decoration: none;
                    }
                """
        )
    }
}

fun HtmlElements.Link(build: Context<LinkComponentContext> = {}): Component<A> {
    val context = LinkComponentContext("f2Link").apply(build)

    return Component { init ->
        a("${LinkComponentContext.cssClass} ${context.cssClass}", content = init)
    }
}
