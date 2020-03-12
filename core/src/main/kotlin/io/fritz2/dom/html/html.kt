package io.fritz2.dom.html

import io.fritz2.dom.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.Element

/**
 * [html] creates a html-context for using fritz2 html elements.
 * It's important to use only one root element in [html] method otherwise
 * a [MultipleRootElementsException] is thrown and rendering is not working.
 */
@ExperimentalCoroutinesApi
@FlowPreview
fun <E : Element> html(content: HtmlElements.() -> Tag<E>) =
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