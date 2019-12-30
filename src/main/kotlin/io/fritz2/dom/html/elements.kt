package io.fritz2.dom.html

import io.fritz2.dom.AttributeDelegate
import kotlinx.coroutines.flow.Flow
import io.fritz2.dom.Element
import io.fritz2.dom.WithText

fun div(content: Div.() -> Unit): Div = Div().also { it.content() }

class Div(): Element("div"), WithText<org.w3c.dom.Element> {
    var testMe: Flow<String> by AttributeDelegate
}
