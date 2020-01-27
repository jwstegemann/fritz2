package io.fritz2.dom.html

import io.fritz2.dom.AttributeDelegate
import io.fritz2.dom.Tag
import io.fritz2.dom.WithText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.*

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLAnchorElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAnchorElement) to Kotlin
 */
class A : Tag<HTMLAnchorElement>("a"), WithText<HTMLAnchorElement> {
    var target: Flow<String> by AttributeDelegate
    var download: Flow<String> by AttributeDelegate
    var ping: Flow<String> by AttributeDelegate
    var rel: Flow<String> by AttributeDelegate
    var hreflang: Flow<String> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
    var text: Flow<String> by AttributeDelegate
    var referrerPolicy: Flow<String> by AttributeDelegate
    var href: Flow<String> by AttributeDelegate
    var protocol: Flow<String> by AttributeDelegate
    var username: Flow<String> by AttributeDelegate
    var password: Flow<String> by AttributeDelegate
    var host: Flow<String> by AttributeDelegate
    var hostname: Flow<String> by AttributeDelegate
    var port: Flow<String> by AttributeDelegate
    var pathname: Flow<String> by AttributeDelegate
    var search: Flow<String> by AttributeDelegate
    var hash: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAreaElement) to Kotlin
 */
class Area : Tag<HTMLAreaElement>("area"), WithText<HTMLAreaElement> {
    var alt: Flow<String> by AttributeDelegate
    var coords: Flow<String> by AttributeDelegate
    var shape: Flow<String> by AttributeDelegate
    var target: Flow<String> by AttributeDelegate
    var download: Flow<String> by AttributeDelegate
    var ping: Flow<String> by AttributeDelegate
    var rel: Flow<String> by AttributeDelegate
    var referrerPolicy: Flow<String> by AttributeDelegate
    var href: Flow<String> by AttributeDelegate
    var protocol: Flow<String> by AttributeDelegate
    var username: Flow<String> by AttributeDelegate
    var password: Flow<String> by AttributeDelegate
    var host: Flow<String> by AttributeDelegate
    var hostname: Flow<String> by AttributeDelegate
    var port: Flow<String> by AttributeDelegate
    var pathname: Flow<String> by AttributeDelegate
    var search: Flow<String> by AttributeDelegate
    var hash: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLBRElement](https://developer.mozilla.org/en/docs/Web/API/HTMLBRElement) to Kotlin
 */
class Br : Tag<HTMLBRElement>("br")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLButtonElement](https://developer.mozilla.org/en/docs/Web/API/HTMLButtonElement) to Kotlin
 */
class Button : Tag<HTMLButtonElement>("button"), WithText<HTMLButtonElement> {
    var autofocus: Flow<Boolean> by AttributeDelegate
    var disabled: Flow<Boolean> by AttributeDelegate
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
/**
 * Exposes the JavaScript [HTMLCanvasElement](https://developer.mozilla.org/en/docs/Web/API/HTMLCanvasElement) to Kotlin
 */
class Canvas : Tag<HTMLCanvasElement>("canvas"), WithText<HTMLCanvasElement> {
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDListElement) to Kotlin
 */
class Dl : Tag<HTMLDListElement>("dl"), WithText<HTMLDListElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDataElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataElement) to Kotlin
 */
class Data : Tag<HTMLDataElement>("data"), WithText<HTMLDataElement> {
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDataListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataListElement) to Kotlin
 */
class DataList : Tag<HTMLDataListElement>("datalist"), WithText<HTMLDataListElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDetailsElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDetailsElement) to Kotlin
 */
class Details : Tag<HTMLDetailsElement>("details"), WithText<HTMLDetailsElement> {
    var open: Flow<Boolean> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDialogElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDialogElement) to Kotlin
 */
class Dialog : Tag<HTMLDialogElement>("dialog"), WithText<HTMLDialogElement> {
    var open: Flow<Boolean> by AttributeDelegate
    var returnValue: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
class Div : Tag<HTMLDivElement>("div"), WithText<HTMLDivElement> {
    var align: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
class Embed : Tag<HTMLEmbedElement>("embed") {
    var src: Flow<String> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
    var width: Flow<String> by AttributeDelegate
    var height: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLFieldSetElement](https://developer.mozilla.org/en/docs/Web/API/HTMLFieldSetElement) to Kotlin
 */
class FieldSet : Tag<HTMLFieldSetElement>("fieldset") {
    var disabled: Flow<Boolean> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [`for`mElement](https://developer.mozilla.org/en/docs/Web/API/`for`mElement) to Kotlin
 */
class Form : Tag<HTMLFormElement>("form"), WithText<HTMLFormElement> {
    var acceptCharset: Flow<String> by AttributeDelegate
    var action: Flow<String> by AttributeDelegate
    var autocomplete: Flow<String> by AttributeDelegate
    var enctype: Flow<String> by AttributeDelegate
    var encoding: Flow<String> by AttributeDelegate
    var method: Flow<String> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var noValidate: Flow<Boolean> by AttributeDelegate
    var target: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLHRElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHRElement) to Kotlin
 */
class Hre : Tag<HTMLHRElement>("hr")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLHeadingElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHeadingElement) to Kotlin
 */
class H(num: Int) : Tag<HTMLHeadingElement>("h$num"), WithText<HTMLHeadingElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin
 */
class IFrame : Tag<HTMLIFrameElement>("iframe"), WithText<HTMLIFrameElement> {
    var src: Flow<String> by AttributeDelegate
    var srcdoc: Flow<String> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var allowFullscreen: Flow<Boolean> by AttributeDelegate
    var allowUserMedia: Flow<Boolean> by AttributeDelegate
    var width: Flow<String> by AttributeDelegate
    var height: Flow<String> by AttributeDelegate
    var referrerPolicy: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLImageElement](https://developer.mozilla.org/en/docs/Web/API/HTMLImageElement) to Kotlin
 */
class Img : Tag<HTMLImageElement>("img"), WithText<HTMLImageElement> {
    var alt: Flow<String> by AttributeDelegate
    var src: Flow<String> by AttributeDelegate
    var srcset: Flow<String> by AttributeDelegate
    var sizes: Flow<String> by AttributeDelegate
    var crossOrigin: Flow<String> by AttributeDelegate
    var useMap: Flow<String> by AttributeDelegate
    var isMap: Flow<Boolean> by AttributeDelegate
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
    var referrerPolicy: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLInputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLInputElement) to Kotlin
 */
class Input : Tag<HTMLInputElement>("input") {
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
    var width: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLLIElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLIElement) to Kotlin
 */
class Li : Tag<HTMLLIElement>("li"), WithText<HTMLLIElement> {
    var value: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLLabelElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLabelElement) to Kotlin
 */
class Label : Tag<HTMLLabelElement>("label"), WithText<HTMLLabelElement> {
    var `for`: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLLegendElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLegendElement) to Kotlin
 */
class Legend : Tag<HTMLLegendElement>("legend"), WithText<HTMLLegendElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLMapElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMapElement) to Kotlin
 */
class Map : Tag<HTMLMapElement>("map"), WithText<HTMLMapElement> {
    var name: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin
 */
class Audio : Tag<HTMLAudioElement>("audio"), WithText<HTMLAudioElement> {
    var src: Flow<String> by AttributeDelegate
    var preload: Flow<String> by AttributeDelegate
    var currentTime: Flow<Double> by AttributeDelegate
    var defaultPlaybackRate: Flow<Double> by AttributeDelegate
    var playbackRate: Flow<Double> by AttributeDelegate
    var autoplay: Flow<Boolean> by AttributeDelegate
    var loop: Flow<Boolean> by AttributeDelegate
    var controls: Flow<Boolean> by AttributeDelegate
    var volume: Flow<Double> by AttributeDelegate
    var muted: Flow<Boolean> by AttributeDelegate
    var defaultMuted: Flow<Boolean> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLVideoElement](https://developer.mozilla.org/en/docs/Web/API/HTMLVideoElement) to Kotlin
 */
class Video : Tag<HTMLVideoElement>("video"), WithText<HTMLVideoElement> {
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
    var poster: Flow<String> by AttributeDelegate
    var playsInline: Flow<Boolean> by AttributeDelegate
    var src: Flow<String> by AttributeDelegate
    var preload: Flow<String> by AttributeDelegate
    var currentTime: Flow<Double> by AttributeDelegate
    var defaultPlaybackRate: Flow<Double> by AttributeDelegate
    var playbackRate: Flow<Double> by AttributeDelegate
    var autoplay: Flow<Boolean> by AttributeDelegate
    var loop: Flow<Boolean> by AttributeDelegate
    var controls: Flow<Boolean> by AttributeDelegate
    var volume: Flow<Double> by AttributeDelegate
    var muted: Flow<Boolean> by AttributeDelegate
    var defaultMuted: Flow<Boolean> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLMeterElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMeterElement) to Kotlin
 */
class Meter : Tag<HTMLMeterElement>("meter"), WithText<HTMLMeterElement> {
    var value: Flow<Double> by AttributeDelegate
    var min: Flow<Double> by AttributeDelegate
    var max: Flow<Double> by AttributeDelegate
    var low: Flow<Double> by AttributeDelegate
    var high: Flow<Double> by AttributeDelegate
    var optimum: Flow<Double> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
class Ins : Tag<HTMLModElement>("ins"), WithText<HTMLModElement> {
    var cite: Flow<String> by AttributeDelegate
    var dateTime: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
class Del : Tag<HTMLModElement>("del"), WithText<HTMLModElement> {
    var cite: Flow<String> by AttributeDelegate
    var dateTime: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLOListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOListElement) to Kotlin
 */
class Ol : Tag<HTMLOListElement>("ol"), WithText<HTMLOListElement> {
    var reversed: Flow<Boolean> by AttributeDelegate
    var start: Flow<Int> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLObjectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLObjectElement) to Kotlin
 */
class Object : Tag<HTMLObjectElement>("object"), WithText<HTMLObjectElement> {
    var data: Flow<String> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
    var typeMustMatch: Flow<Boolean> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var useMap: Flow<String> by AttributeDelegate
    var width: Flow<String> by AttributeDelegate
    var height: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLOptGroupElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptGroupElement) to Kotlin
 */
class Optgroup : Tag<HTMLOptGroupElement>("optgroup"), WithText<HTMLOptGroupElement> {
    var disabled: Flow<Boolean> by AttributeDelegate
    var label: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLOptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptionElement) to Kotlin
 */
class Option : Tag<HTMLOptionElement>("option"), WithText<HTMLOptionElement> {
    var disabled: Flow<Boolean> by AttributeDelegate
    var label: Flow<String> by AttributeDelegate
    var defaultSelected: Flow<Boolean> by AttributeDelegate
    var selected: Flow<Boolean> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
    var text: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLOutputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOutputElement) to Kotlin
 */
class Output : Tag<HTMLOutputElement>("output"), WithText<HTMLOutputElement> {
    var name: Flow<String> by AttributeDelegate
    var defaultValue: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLParagraphElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParagraphElement) to Kotlin
 */
class P : Tag<HTMLParagraphElement>("p"), WithText<HTMLParagraphElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLParamElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParamElement) to Kotlin
 */
class Param : Tag<HTMLParamElement>("param"), WithText<HTMLParamElement> {
    var name: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLPictureElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPictureElement) to Kotlin
 */
class Picture : Tag<HTMLPictureElement>("picture"), WithText<HTMLPictureElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLPreElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPreElement) to Kotlin
 */
class Pre : Tag<HTMLPreElement>("pre"), WithText<HTMLPreElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLProgressElement](https://developer.mozilla.org/en/docs/Web/API/HTMLProgressElement) to Kotlin
 */
class Progress : Tag<HTMLProgressElement>("progress"), WithText<HTMLProgressElement> {
    var value: Flow<Double> by AttributeDelegate
    var max: Flow<Double> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLQuoteElement](https://developer.mozilla.org/en/docs/Web/API/HTMLQuoteElement) to Kotlin
 */
class Quote : Tag<HTMLQuoteElement>("quote"), WithText<HTMLQuoteElement> {
    var cite: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLScriptElement](https://developer.mozilla.org/en/docs/Web/API/HTMLScriptElement) to Kotlin
 */
class Script : Tag<HTMLScriptElement>("script") {
    var src: Flow<String> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
    var charset: Flow<String> by AttributeDelegate
    var async: Flow<Boolean> by AttributeDelegate
    var defer: Flow<Boolean> by AttributeDelegate
    var text: Flow<String> by AttributeDelegate
    var nonce: Flow<String> by AttributeDelegate
    var event: Flow<String> by AttributeDelegate
    var `for`: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLSelectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLSelectElement) to Kotlin
 */
class Select : Tag<HTMLSelectElement>("select") {
    var autocomplete: Flow<String> by AttributeDelegate
    var autofocus: Flow<Boolean> by AttributeDelegate
    var disabled: Flow<Boolean> by AttributeDelegate
    var multiple: Flow<Boolean> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var required: Flow<Boolean> by AttributeDelegate
    var size: Flow<Int> by AttributeDelegate
    var length: Flow<Int> by AttributeDelegate
    var selectedIndex: Flow<Int> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLSpanElement](https://developer.mozilla.org/en/docs/Web/API/HTMLSpanElement) to Kotlin
 */
class Span : Tag<HTMLSpanElement>("span"), WithText<HTMLSpanElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableCaptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCaptionElement) to Kotlin
 */
class Caption : Tag<HTMLTableCaptionElement>("caption"), WithText<HTMLTableCaptionElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
class Th : Tag<HTMLTableCellElement>("th"), WithText<HTMLTableCellElement> {
    var colSpan: Flow<Int> by AttributeDelegate
    var rowSpan: Flow<Int> by AttributeDelegate
    var headers: Flow<String> by AttributeDelegate
    var scope: Flow<String> by AttributeDelegate
    var abbr: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
class Td : Tag<HTMLTableCellElement>("td"), WithText<HTMLTableCellElement> {
    var colSpan: Flow<Int> by AttributeDelegate
    var rowSpan: Flow<Int> by AttributeDelegate
    var headers: Flow<String> by AttributeDelegate
    var scope: Flow<String> by AttributeDelegate
    var abbr: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
class Col : Tag<HTMLTableColElement>("col"), WithText<HTMLTableColElement> {
    var span: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
class Colgroup : Tag<HTMLTableColElement>("colgroup"), WithText<HTMLTableColElement> {
    var span: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableElement) to Kotlin
 */
class Table : Tag<HTMLTableElement>("table")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableRowElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableRowElement) to Kotlin
 */
class Tr : Tag<HTMLTableRowElement>("tr"), WithText<HTMLTableRowElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
class TFoot : Tag<HTMLTableSectionElement>("tfoot")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
class THead : Tag<HTMLTableSectionElement>("thead")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
class TBody : Tag<HTMLTableSectionElement>("tbody")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTextAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTextAreaElement) to Kotlin
 */
class TextArea : Tag<HTMLTextAreaElement>("textarea"), WithText<HTMLTextAreaElement> {
    var autocomplete: Flow<String> by AttributeDelegate
    var autofocus: Flow<Boolean> by AttributeDelegate
    var cols: Flow<Int> by AttributeDelegate
    var dirName: Flow<String> by AttributeDelegate
    var disabled: Flow<Boolean> by AttributeDelegate
    var inputMode: Flow<String> by AttributeDelegate
    var maxLength: Flow<Int> by AttributeDelegate
    var minLength: Flow<Int> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
    var placeholder: Flow<String> by AttributeDelegate
    var readOnly: Flow<Boolean> by AttributeDelegate
    var required: Flow<Boolean> by AttributeDelegate
    var rows: Flow<Int> by AttributeDelegate
    var wrap: Flow<String> by AttributeDelegate
    var defaultValue: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTimeElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTimeElement) to Kotlin
 */
class Time : Tag<HTMLTimeElement>("time"), WithText<HTMLTimeElement> {
    var dateTime: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTrackElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTrackElement) to Kotlin
 */
class Track : Tag<HTMLTrackElement>("track"), WithText<HTMLTrackElement> {
    var kind: Flow<String> by AttributeDelegate
    var src: Flow<String> by AttributeDelegate
    var srclang: Flow<String> by AttributeDelegate
    var label: Flow<String> by AttributeDelegate
    var default: Flow<Boolean> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLUListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLUListElement) to Kotlin
 */
class Ul : Tag<HTMLUListElement>("ul")

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * General class for standard html5 elements
 */
class TextElement(tagName: String) : Tag<HTMLElement>(tagName), WithText<HTMLElement>

@ExperimentalCoroutinesApi
@FlowPreview
interface HtmlElements {
    fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T
    fun a(content: A.() -> Unit): A = register(A(), content)
    fun area(content: Area.() -> Unit): Area = register(Area(), content)
    fun br(content: Br.() -> Unit): Br = register(Br(), content)
    fun button(content: Button.() -> Unit): Button = register(Button(), content)
    fun canvas(content: Canvas.() -> Unit): Canvas = register(Canvas(), content)
    fun dl(content: Dl.() -> Unit): Dl = register(Dl(), content)
    fun data(content: Data.() -> Unit): Data = register(Data(), content)
    fun datalist(content: DataList.() -> Unit): DataList = register(DataList(), content)
    fun details(content: Details.() -> Unit): Details = register(Details(), content)
    fun dialog(content: Dialog.() -> Unit): Dialog = register(Dialog(), content)
    fun div(content: Div.() -> Unit): Div = register(Div(), content)
    fun embed(content: Embed.() -> Unit): Embed = register(Embed(), content)
    fun fieldset(content: FieldSet.() -> Unit): FieldSet = register(FieldSet(), content)
    fun form(content: Form.() -> Unit): Form = register(Form(), content)
    fun hre(content: Hre.() -> Unit): Hre = register(Hre(), content)
    fun h1(content: H.() -> Unit): H = register(H(1), content)
    fun h2(content: H.() -> Unit): H = register(H(2), content)
    fun h3(content: H.() -> Unit): H = register(H(3), content)
    fun h4(content: H.() -> Unit): H = register(H(4), content)
    fun h5(content: H.() -> Unit): H = register(H(5), content)
    fun h6(content: H.() -> Unit): H = register(H(6), content)
    fun iframe(content: IFrame.() -> Unit): IFrame = register(IFrame(), content)
    fun img(content: Img.() -> Unit): Img = register(Img(), content)
    fun input(content: Input.() -> Unit): Input = register(Input(), content)
    fun li(content: Li.() -> Unit): Li = register(Li(), content)
    fun label(content: Label.() -> Unit): Label = register(Label(), content)
    fun legend(content: Legend.() -> Unit): Legend = register(Legend(), content)
    fun map(content: Map.() -> Unit): Map = register(Map(), content)
    fun audio(content: Audio.() -> Unit): Audio = register(Audio(), content)
    fun video(content: Video.() -> Unit): Video = register(Video(), content)
    fun meter(content: Meter.() -> Unit): Meter = register(Meter(), content)
    fun ins(content: Ins.() -> Unit): Ins = register(Ins(), content)
    fun del(content: Del.() -> Unit): Del = register(Del(), content)
    fun ol(content: Ol.() -> Unit): Ol = register(Ol(), content)
    fun `object`(content: Object.() -> Unit): Object = register(Object(), content)
    fun optgroup(content: Optgroup.() -> Unit): Optgroup = register(Optgroup(), content)
    fun option(content: Option.() -> Unit): Option = register(Option(), content)
    fun output(content: Output.() -> Unit): Output = register(Output(), content)
    fun p(content: P.() -> Unit): P = register(P(), content)
    fun param(content: Param.() -> Unit): Param = register(Param(), content)
    fun picture(content: Picture.() -> Unit): Picture = register(Picture(), content)
    fun pre(content: Pre.() -> Unit): Pre = register(Pre(), content)
    fun progress(content: Progress.() -> Unit): Progress = register(Progress(), content)
    fun quote(content: Quote.() -> Unit): Quote = register(Quote(), content)
    fun script(content: Script.() -> Unit): Script = register(Script(), content)
    fun select(content: Select.() -> Unit): Select = register(Select(), content)
    fun span(content: Span.() -> Unit): Span = register(Span(), content)
    fun caption(content: Caption.() -> Unit): Caption = register(Caption(), content)
    fun th(content: Th.() -> Unit): Th = register(Th(), content)
    fun td(content: Td.() -> Unit): Td = register(Td(), content)
    fun col(content: Col.() -> Unit): Col = register(Col(), content)
    fun colgroup(content: Colgroup.() -> Unit): Colgroup = register(Colgroup(), content)
    fun table(content: Table.() -> Unit): Table = register(Table(), content)
    fun tr(content: Tr.() -> Unit): Tr = register(Tr(), content)
    fun tfoot(content: TFoot.() -> Unit): TFoot = register(TFoot(), content)
    fun thead(content: THead.() -> Unit): THead = register(THead(), content)
    fun tbody(content: TBody.() -> Unit): TBody = register(TBody(), content)
    fun textarea(content: TextArea.() -> Unit): TextArea = register(TextArea(), content)
    fun time(content: Time.() -> Unit): Time = register(Time(), content)
    fun track(content: Track.() -> Unit): Track = register(Track(), content)
    fun ul(content: Ul.() -> Unit): Ul = register(Ul(), content)

    fun address(content: TextElement.() -> Unit): TextElement = register(TextElement("address"), content)
    fun article(content: TextElement.() -> Unit): TextElement = register(TextElement("article"), content)
    fun aside(content: TextElement.() -> Unit): TextElement = register(TextElement("aside"), content)
    fun bdi(content: TextElement.() -> Unit): TextElement = register(TextElement("bdi"), content)
    fun details(content: TextElement.() -> Unit): TextElement = register(TextElement("details"), content)
    fun dialog(content: TextElement.() -> Unit): TextElement = register(TextElement("dialog"), content)
    fun figcaption(content: TextElement.() -> Unit): TextElement = register(TextElement("figcaption"), content)
    fun figure(content: TextElement.() -> Unit): TextElement = register(TextElement("figure"), content)
    fun footer(content: TextElement.() -> Unit): TextElement = register(TextElement("footer"), content)
    fun header(content: TextElement.() -> Unit): TextElement = register(TextElement("header"), content)
    fun main(content: TextElement.() -> Unit): TextElement = register(TextElement("main"), content)
    fun mark(content: TextElement.() -> Unit): TextElement = register(TextElement("mark"), content)
    fun nav(content: TextElement.() -> Unit): TextElement = register(TextElement("nav"), content)
    fun noscript(content: TextElement.() -> Unit): TextElement = register(TextElement("noscript"), content)
    fun progress(content: TextElement.() -> Unit): TextElement = register(TextElement("progress"), content)
    fun rp(content: TextElement.() -> Unit): TextElement = register(TextElement("rp"), content)
    fun rt(content: TextElement.() -> Unit): TextElement = register(TextElement("rt"), content)
    fun ruby(content: TextElement.() -> Unit): TextElement = register(TextElement("ruby"), content)
    fun section(content: TextElement.() -> Unit): TextElement = register(TextElement("section"), content)
    fun summary(content: TextElement.() -> Unit): TextElement = register(TextElement("summary"), content)
    fun time(content: TextElement.() -> Unit): TextElement = register(TextElement("time"), content)
    fun wbr(content: TextElement.() -> Unit): TextElement = register(TextElement("wbr"), content)
    fun blockquote(content: TextElement.() -> Unit): TextElement = register(TextElement("blockquote"), content)
    fun em(content: TextElement.() -> Unit): TextElement = register(TextElement("em"), content)
    fun strong(content: TextElement.() -> Unit): TextElement = register(TextElement("strong"), content)
    fun small(content: TextElement.() -> Unit): TextElement = register(TextElement("small"), content)
    fun s(content: TextElement.() -> Unit): TextElement = register(TextElement("s"), content)
    fun cite(content: TextElement.() -> Unit): TextElement = register(TextElement("cite"), content)
    fun q(content: TextElement.() -> Unit): TextElement = register(TextElement("q"), content)
    fun dfn(content: TextElement.() -> Unit): TextElement = register(TextElement("dfn"), content)
    fun abbr(content: TextElement.() -> Unit): TextElement = register(TextElement("abbr"), content)
    fun code(content: TextElement.() -> Unit): TextElement = register(TextElement("code"), content)
    fun `var`(content: TextElement.() -> Unit): TextElement = register(TextElement("var"), content)
    fun samp(content: TextElement.() -> Unit): TextElement = register(TextElement("samp"), content)
    fun kbd(content: TextElement.() -> Unit): TextElement = register(TextElement("kbd"), content)
    fun sub(content: TextElement.() -> Unit): TextElement = register(TextElement("sub"), content)
    fun sup(content: TextElement.() -> Unit): TextElement = register(TextElement("sup"), content)
    fun i(content: TextElement.() -> Unit): TextElement = register(TextElement("i"), content)
    fun b(content: TextElement.() -> Unit): TextElement = register(TextElement("b"), content)
    fun u(content: TextElement.() -> Unit): TextElement = register(TextElement("u"), content)
    fun bdo(content: TextElement.() -> Unit): TextElement = register(TextElement("bdo"), content)
    fun command(content: TextElement.() -> Unit): TextElement = register(TextElement("command"), content)
}
