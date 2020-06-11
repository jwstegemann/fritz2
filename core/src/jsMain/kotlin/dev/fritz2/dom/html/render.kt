package dev.fritz2.dom.html

import dev.fritz2.dom.Tag
import org.w3c.dom.Element


fun <E : Element> render(content: HtmlElements.() -> Tag<E>) =
    content(object : HtmlElements {

        var alreadyRegistered: Boolean = false

        override fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T {
            if (alreadyRegistered) {
                throw MultipleRootElementsException(
                    "You can have only one root-tag per html-context!"
                )
            } else {
                content(element)
                alreadyRegistered = true
                return element
            }
        }
    })

internal class MultipleRootElementsException(message: String) : RuntimeException(message)