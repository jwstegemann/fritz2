package io.fritz2.dom.html

import io.fritz2.dom.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.Element

@ExperimentalCoroutinesApi
@FlowPreview
fun <E : Element> html(content: HtmlElements.() -> Tag<E>) =
    content(object : HtmlElements {

        var alreadyRegistered: Boolean = false

        override fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T {
            if (!alreadyRegistered) {
                content(element)
                alreadyRegistered = true
                return element
            } else {
                throw MultipleRootElementsException(
                    "Don't use multiple root elements/tags in the html{...} context of your component! That's why rendering is not working."
                )
            }
        }
    })

internal class MultipleRootElementsException(message: String) : RuntimeException(message)