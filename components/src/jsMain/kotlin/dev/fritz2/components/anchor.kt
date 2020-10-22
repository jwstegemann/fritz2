package dev.fritz2.components

import dev.fritz2.dom.html.A
import dev.fritz2.styling.params.StyleParamsImpl
import dev.fritz2.styling.staticStyle
import org.w3c.dom.HTMLAnchorElement


class LinkComponent : StyleParamsImpl(), Application<A> by ApplicationDelegate() {
    companion object {
        internal const val prefix = "anchor"

        val staticCss = staticStyle(
            prefix,
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

    val href = StringAttributeDelegate<HTMLAnchorElement, A>("href")
}

/*
fun HtmlElements.anchor(build: LinkComponent.() -> Unit = {}) {
    val component = LinkComponent().apply {
        classes(LinkComponent.staticCss)
        build()
    }

    a(component.cssClasses) {
        component.use(
            this,
            component.href.value,
            component.application
        )
    }
}

 */