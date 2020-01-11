package io.fritz2.dom.html

import io.fritz2.binding.Slot
import io.fritz2.dom.AttributeDelegate
import io.fritz2.dom.Tag
import io.fritz2.dom.WithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.events.MouseEvent


class Div(): Tag("div"), WithText<Element> {
    var testMe: Flow<String> by AttributeDelegate
    //TODO: structure attributes and events in interfaces
}

//FIXME: use correct type for domNode - HtmlButtonElement here
class Button(): Tag("button"), WithText<Element> {
    var onClick: Slot<MouseEvent> by ClickEventDelegate
}

class Input(): Tag("input") {
    var value: Flow<String> by AttributeDelegate

    var onChange: Slot<String> by ChangeEventDelegate

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun changes() = event(Change)
}

interface HtmlElements {
    fun <T: Tag> register(element: T, content: (T) -> Unit): T

    fun div(content: Div.() -> Unit): Div = register(Div(), content)

    fun button(content: Button.() -> Unit): Button = register(Button(), content)

    fun input(content: Input.() -> Unit): Input = register(Input(), content)
}
