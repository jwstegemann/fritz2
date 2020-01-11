package io.fritz2.dom.html

import io.fritz2.dom.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
fun html(content: HtmlElements.() -> Tag) =
    content(object : HtmlElements {
        override fun <T : Tag> register(element: T, content: (T) -> Unit): T {
            content(element)
            return element
        }
    })
