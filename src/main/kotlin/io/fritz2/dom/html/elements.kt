package io.fritz2.dom.html

import io.fritz2.dom.AttributeDelegate
import io.fritz2.dom.Tag
import io.fritz2.dom.WithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element

@ExperimentalCoroutinesApi
@FlowPreview
class Div(): Tag("div"), WithText<Element>

//FIXME: use correct type for domNode - HtmlButtonElement here
@ExperimentalCoroutinesApi
@FlowPreview
class Button(): Tag("button"), WithText<Element> {
    //TODO: structure attributes and events in interfaces
    val clicks by lazy { subscribe(Click) }
}

@ExperimentalCoroutinesApi
@FlowPreview
class Input(): Tag("input") {
    var value: Flow<String> by AttributeDelegate

    val changes by lazy { subscribe(Change) }
}

@ExperimentalCoroutinesApi
@FlowPreview
interface HtmlElements {
    fun <T: Tag> register(element: T, content: (T) -> Unit): T

    fun div(content: Div.() -> Unit): Div = register(Div(), content)

    fun button(content: Button.() -> Unit): Button = register(Button(), content)

    fun input(content: Input.() -> Unit): Input = register(Input(), content)
}
