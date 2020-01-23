package io.fritz2.dom.html

import io.fritz2.dom.AttributeDelegate
import io.fritz2.dom.Tag
import io.fritz2.dom.WithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.*
import kotlin.js.Date

@ExperimentalCoroutinesApi
@FlowPreview
class Div(): Tag<HTMLDivElement>("div"), WithText<HTMLDivElement> {
    var align: Flow<String> by AttributeDelegate
}

//FIXME: use correct type for domNode - HtmlButtonElement here
@ExperimentalCoroutinesApi
@FlowPreview
class Button(): Tag<HTMLButtonElement>("button"), WithText<HTMLButtonElement> {
    var autofocusFlow: Flow<Boolean> by AttributeDelegate
    var disabledFlow: Flow<Boolean> by AttributeDelegate
    var formAction: Flow<String> by AttributeDelegate
    var formEnctype: Flow<String> by AttributeDelegate
    var formMethod: Flow<String> by AttributeDelegate
    var formNoValidate: Flow<Boolean> by AttributeDelegate
    var formTarget: Flow<String> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
    
}

@ExperimentalCoroutinesApi
@FlowPreview
class Input(): Tag<HTMLInputElement>("input") {
    var accept: Flow<String> by AttributeDelegate
    var alt: Flow<String> by AttributeDelegate
    var autocomplete: Flow<String> by AttributeDelegate
    var autofocus: Flow<Boolean> by AttributeDelegate
    var defaultChecked: Flow<Boolean> by AttributeDelegate
    var checked: Flow<Boolean> by AttributeDelegate
    var dirName: Flow<String> by AttributeDelegate
    var disabled: Flow<Boolean> by AttributeDelegate
    var formAction: Flow<String> by AttributeDelegate
    var formEnctype: Flow<String> by AttributeDelegate
    var formMethod: Flow<String> by AttributeDelegate
    var formNoValidate: Flow<Boolean> by AttributeDelegate
    var formTarget: Flow<String> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
    var indeterminate: Flow<Boolean> by AttributeDelegate
    var inputMode: Flow<String> by AttributeDelegate
    var max: Flow<String> by AttributeDelegate
    var maxLength: Flow<Int> by AttributeDelegate
    var min: Flow<String> by AttributeDelegate
    var minLength: Flow<Int> by AttributeDelegate
    var multiple: Flow<Boolean> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var pattern: Flow<String> by AttributeDelegate
    var placeholder: Flow<String> by AttributeDelegate
    var readOnly: Flow<Boolean> by AttributeDelegate
    var required: Flow<Boolean> by AttributeDelegate
    var size: Flow<Int> by AttributeDelegate
    var src: Flow<String> by AttributeDelegate
    var step: Flow<String> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
    var defaultValue: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
    var valueAsDate: Flow<Date> by AttributeDelegate
    var valueAsNumber: Flow<Double> by AttributeDelegate
    var width: Flow<Int> by AttributeDelegate
    var align: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
interface HtmlElements {
    fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T

    fun div(content: Div.() -> Unit): Div = register(Div(), content)

    fun button(content: Button.() -> Unit): Button = register(Button(), content)

    fun input(content: Input.() -> Unit): Input = register(Input(), content)
}
