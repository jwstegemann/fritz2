package io.fritz2.dom.html

import io.fritz2.dom.AttributeDelegate
import io.fritz2.dom.Tag
import io.fritz2.dom.WithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.MouseEvent

@ExperimentalCoroutinesApi
@FlowPreview
class Div(): Tag<HTMLDivElement>("div"), WithText<HTMLDivElement>

//FIXME: use correct type for domNode - HtmlButtonElement here
@ExperimentalCoroutinesApi
@FlowPreview
class Button(): Tag<HTMLButtonElement>("button"), WithText<HTMLButtonElement> {
    //TODO: structure attributes and events in interfaces
    val clicks by lazy { subscribe(Click) }
}

@ExperimentalCoroutinesApi
@FlowPreview
class Input(): Tag<HTMLInputElement>("input") {
    var value: Flow<String> by AttributeDelegate

    val changes by lazy { subscribe(Change) }
}

@ExperimentalCoroutinesApi
@FlowPreview
interface HtmlElements {
    fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T

    fun div(content: Div.() -> Unit): Div = register(Div(), content)

    fun button(content: Button.() -> Unit): Button = register(Button(), content)

    fun input(content: Input.() -> Unit): Input = register(Input(), content)
}
