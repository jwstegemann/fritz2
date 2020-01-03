package io.fritz2.dom.html

import io.fritz2.binding.Slot
import io.fritz2.dom.AttributeDelegate
import kotlinx.coroutines.flow.Flow
import io.fritz2.dom.Element
import io.fritz2.dom.WithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent

// global elements
fun div(content: Div.() -> Unit): Div = Div().also { it.content() }


class Div(): Element("div"), WithText<org.w3c.dom.Element> {
    var testMe: Flow<String> by AttributeDelegate
    //TODO: structure attributes and events in interfaces
}

//FIXME: use correct type for domNode - HtmlButtonElement here
class Button(): Element("button"), WithText<org.w3c.dom.Element> {
    var onClick: Slot<MouseEvent> by ClickEventDelegate
}

class Input(): Element("input") {
    var value: Flow<String> by AttributeDelegate

    var onChange: Slot<String> by ChangeEventDelegate

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun changes() = event(Change)
}