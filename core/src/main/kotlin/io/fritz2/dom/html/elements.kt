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
class A(id: String? = null): Tag<HTMLAnchorElement>("a", id), WithText<HTMLAnchorElement> {
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
class Area(id: String? = null): Tag<HTMLAreaElement>("area", id), WithText<HTMLAreaElement> {
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
class Br(id: String? = null): Tag<HTMLBRElement>("br", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLButtonElement](https://developer.mozilla.org/en/docs/Web/API/HTMLButtonElement) to Kotlin
 */
class Button(id: String? = null): Tag<HTMLButtonElement>("button", id), WithText<HTMLButtonElement> {
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
class Canvas(id: String? = null): Tag<HTMLCanvasElement>("canvas", id), WithText<HTMLCanvasElement> {
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDListElement) to Kotlin
 */
class Dl(id: String? = null): Tag<HTMLDListElement>("dl", id), WithText<HTMLDListElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDataElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataElement) to Kotlin
 */
class Data(id: String? = null): Tag<HTMLDataElement>("data", id), WithText<HTMLDataElement> {
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDataListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataListElement) to Kotlin
 */
class DataList(id: String? = null): Tag<HTMLDataListElement>("datalist", id), WithText<HTMLDataListElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDetailsElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDetailsElement) to Kotlin
 */
class Details(id: String? = null): Tag<HTMLDetailsElement>("details", id), WithText<HTMLDetailsElement> {
    var open: Flow<Boolean> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDialogElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDialogElement) to Kotlin
 */
class Dialog(id: String? = null): Tag<HTMLDialogElement>("dialog", id), WithText<HTMLDialogElement> {
    var open: Flow<Boolean> by AttributeDelegate
    var returnValue: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
class Div(id: String? = null): Tag<HTMLDivElement>("div", id), WithText<HTMLDivElement> {
    var align: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
class Embed(id: String? = null): Tag<HTMLEmbedElement>("embed", id) {
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
class FieldSet(id: String? = null): Tag<HTMLFieldSetElement>("fieldSet", id) {
    var disabled: Flow<Boolean> by AttributeDelegate
    var name: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLFormElement](https://developer.mozilla.org/en/docs/Web/API/`for`mElement) to Kotlin
 */
class Form(id: String? = null): Tag<HTMLFormElement>("form", id), WithText<HTMLFormElement> {
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
class Hre(id: String? = null): Tag<HTMLHRElement>("hr", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLHeadingElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHeadingElement) to Kotlin
 */
class H(num: Int, id: String? = null): Tag<HTMLHeadingElement>("h$num", id), WithText<HTMLHeadingElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin
 */
class IFrame(id: String? = null): Tag<HTMLIFrameElement>("iframe", id), WithText<HTMLIFrameElement> {
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
class Img(id: String? = null): Tag<HTMLImageElement>("img", id), WithText<HTMLImageElement> {
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
class Input(id: String? = null): Tag<HTMLInputElement>("input", id) {
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
class Li(id: String? = null): Tag<HTMLLIElement>("li", id), WithText<HTMLLIElement> {
    var value: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLLabelElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLabelElement) to Kotlin
 */
class Label(id: String? = null): Tag<HTMLLabelElement>("label", id), WithText<HTMLLabelElement> {
    var `for`: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLLegendElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLegendElement) to Kotlin
 */
class Legend(id: String? = null): Tag<HTMLLegendElement>("legend", id), WithText<HTMLLegendElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLMapElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMapElement) to Kotlin
 */
class Map(id: String? = null): Tag<HTMLMapElement>("map", id), WithText<HTMLMapElement> {
    var name: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin
 */
class Audio(id: String? = null): Tag<HTMLAudioElement>("audio", id), WithText<HTMLAudioElement> {
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
class Video(id: String? = null): Tag<HTMLVideoElement>("video", id), WithText<HTMLVideoElement> {
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
class Meter(id: String? = null): Tag<HTMLMeterElement>("meter", id), WithText<HTMLMeterElement> {
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
class Ins(id: String? = null): Tag<HTMLModElement>("ins", id), WithText<HTMLModElement> {
    var cite: Flow<String> by AttributeDelegate
    var dateTime: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
class Del(id: String? = null): Tag<HTMLModElement>("del", id), WithText<HTMLModElement> {
    var cite: Flow<String> by AttributeDelegate
    var dateTime: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLOListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOListElement) to Kotlin
 */
class Ol(id: String? = null): Tag<HTMLOListElement>("ol", id), WithText<HTMLOListElement> {
    var reversed: Flow<Boolean> by AttributeDelegate
    var start: Flow<Int> by AttributeDelegate
    var type: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLObjectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLObjectElement) to Kotlin
 */
class Object(id: String? = null): Tag<HTMLObjectElement>("object", id), WithText<HTMLObjectElement> {
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
class Optgroup(id: String? = null): Tag<HTMLOptGroupElement>("optgroup", id), WithText<HTMLOptGroupElement> {
    var disabled: Flow<Boolean> by AttributeDelegate
    var label: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLOptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptionElement) to Kotlin
 */
class Option(id: String? = null): Tag<HTMLOptionElement>("option", id), WithText<HTMLOptionElement> {
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
class Output(id: String? = null): Tag<HTMLOutputElement>("output", id), WithText<HTMLOutputElement> {
    var name: Flow<String> by AttributeDelegate
    var defaultValue: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLParagraphElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParagraphElement) to Kotlin
 */
class P(id: String? = null): Tag<HTMLParagraphElement>("p", id), WithText<HTMLParagraphElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLParamElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParamElement) to Kotlin
 */
class Param(id: String? = null): Tag<HTMLParamElement>("param", id), WithText<HTMLParamElement> {
    var name: Flow<String> by AttributeDelegate
    var value: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLPictureElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPictureElement) to Kotlin
 */
class Picture(id: String? = null): Tag<HTMLPictureElement>("picture", id), WithText<HTMLPictureElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLPreElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPreElement) to Kotlin
 */
class Pre(id: String? = null): Tag<HTMLPreElement>("pre", id), WithText<HTMLPreElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLProgressElement](https://developer.mozilla.org/en/docs/Web/API/HTMLProgressElement) to Kotlin
 */
class Progress(id: String? = null): Tag<HTMLProgressElement>("progress", id), WithText<HTMLProgressElement> {
    var value: Flow<Double> by AttributeDelegate
    var max: Flow<Double> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLQuoteElement](https://developer.mozilla.org/en/docs/Web/API/HTMLQuoteElement) to Kotlin
 */
class Quote(id: String? = null): Tag<HTMLQuoteElement>("quote", id), WithText<HTMLQuoteElement> {
    var cite: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLScriptElement](https://developer.mozilla.org/en/docs/Web/API/HTMLScriptElement) to Kotlin
 */
class Script(id: String? = null): Tag<HTMLScriptElement>("script", id) {
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
class Select(id: String? = null): Tag<HTMLSelectElement>("select", id) {
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
class Span(id: String? = null): Tag<HTMLSpanElement>("span", id), WithText<HTMLSpanElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableCaptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCaptionElement) to Kotlin
 */
class Caption(id: String? = null): Tag<HTMLTableCaptionElement>("caption", id), WithText<HTMLTableCaptionElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
class Th(id: String? = null): Tag<HTMLTableCellElement>("th", id), WithText<HTMLTableCellElement> {
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
class Td(id: String? = null): Tag<HTMLTableCellElement>("td", id), WithText<HTMLTableCellElement> {
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
class Col(id: String? = null): Tag<HTMLTableColElement>("col", id), WithText<HTMLTableColElement> {
    var span: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
class Colgroup(id: String? = null): Tag<HTMLTableColElement>("colgroup", id), WithText<HTMLTableColElement> {
    var span: Flow<Int> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableElement) to Kotlin
 */
class Table(id: String? = null): Tag<HTMLTableElement>("table", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableRowElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableRowElement) to Kotlin
 */
class Tr(id: String? = null): Tag<HTMLTableRowElement>("tr", id), WithText<HTMLTableRowElement>

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
class TFoot(id: String? = null): Tag<HTMLTableSectionElement>("tfoot", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
class THead(id: String? = null): Tag<HTMLTableSectionElement>("thead", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
class TBody(id: String? = null): Tag<HTMLTableSectionElement>("tbody", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTextAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTextAreaElement) to Kotlin
 */
class TextArea(id: String? = null): Tag<HTMLTextAreaElement>("textarea", id), WithText<HTMLTextAreaElement> {
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
class Time(id: String? = null): Tag<HTMLTimeElement>("time", id), WithText<HTMLTimeElement> {
    var dateTime: Flow<String> by AttributeDelegate
}

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * Exposes the JavaScript [HTMLTrackElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTrackElement) to Kotlin
 */
class Track(id: String? = null): Tag<HTMLTrackElement>("track", id), WithText<HTMLTrackElement> {
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
class Ul(id: String? = null): Tag<HTMLUListElement>("ul", id)

@ExperimentalCoroutinesApi
@FlowPreview
/**
 * General class for standard html5 elements
 */
class TextElement(tagName: String, id: String? = null): Tag<HTMLElement>(tagName, id), WithText<HTMLElement>

@ExperimentalCoroutinesApi
@FlowPreview
interface HtmlElements {
    fun <X : Element, T: Tag<X>> register(element: T, content: (T) -> Unit): T
    fun a(id: String? = null, content: A.() -> Unit): A = register(A(id), content)
    fun area(id: String? = null, content: Area.() -> Unit): Area = register(Area(id), content)
    fun br(id: String? = null, content: Br.() -> Unit): Br = register(Br(id), content)
    fun button(id: String? = null, content: Button.() -> Unit): Button = register(Button(id), content)
    fun canvas(id: String? = null, content: Canvas.() -> Unit): Canvas = register(Canvas(id), content)
    fun dl(id: String? = null, content: Dl.() -> Unit): Dl = register(Dl(id), content)
    fun data(id: String? = null, content: Data.() -> Unit): Data = register(Data(id), content)
    fun datalist(id: String? = null, content: DataList.() -> Unit): DataList = register(DataList(id), content)
    fun details(id: String? = null, content: Details.() -> Unit): Details = register(Details(id), content)
    fun dialog(id: String? = null, content: Dialog.() -> Unit): Dialog = register(Dialog(id), content)
    fun div(id: String? = null, content: Div.() -> Unit): Div = register(Div(id), content)
    fun embed(id: String? = null, content: Embed.() -> Unit): Embed = register(Embed(id), content)
    fun fieldset(id: String? = null, content: FieldSet.() -> Unit): FieldSet = register(FieldSet(id), content)
    fun form(id: String? = null, content: Form.() -> Unit): Form = register(Form(id), content)
    fun hre(id: String? = null, content: Hre.() -> Unit): Hre = register(Hre(id), content)
    fun h1(id: String? = null, content: H.() -> Unit): H = register(H(1, id), content)
    fun h2(id: String? = null, content: H.() -> Unit): H = register(H(2, id), content)
    fun h3(id: String? = null, content: H.() -> Unit): H = register(H(3, id), content)
    fun h4(id: String? = null, content: H.() -> Unit): H = register(H(4, id), content)
    fun h5(id: String? = null, content: H.() -> Unit): H = register(H(5, id), content)
    fun h6(id: String? = null, content: H.() -> Unit): H = register(H(6, id), content)
    fun iframe(id: String? = null, content: IFrame.() -> Unit): IFrame = register(IFrame(id), content)
    fun img(id: String? = null, content: Img.() -> Unit): Img = register(Img(id), content)
    fun input(id: String? = null, content: Input.() -> Unit): Input = register(Input(id), content)
    fun li(id: String? = null, content: Li.() -> Unit): Li = register(Li(id), content)
    fun label(id: String? = null, content: Label.() -> Unit): Label = register(Label(id), content)
    fun legend(id: String? = null, content: Legend.() -> Unit): Legend = register(Legend(id), content)
    fun map(id: String? = null, content: Map.() -> Unit): Map = register(Map(id), content)
    fun audio(id: String? = null, content: Audio.() -> Unit): Audio = register(Audio(id), content)
    fun video(id: String? = null, content: Video.() -> Unit): Video = register(Video(id), content)
    fun meter(id: String? = null, content: Meter.() -> Unit): Meter = register(Meter(id), content)
    fun ins(id: String? = null, content: Ins.() -> Unit): Ins = register(Ins(id), content)
    fun del(id: String? = null, content: Del.() -> Unit): Del = register(Del(id), content)
    fun ol(id: String? = null, content: Ol.() -> Unit): Ol = register(Ol(id), content)
    fun `object`(id: String? = null, content: Object.() -> Unit): Object = register(Object(id), content)
    fun optgroup(id: String? = null, content: Optgroup.() -> Unit): Optgroup = register(Optgroup(id), content)
    fun option(id: String? = null, content: Option.() -> Unit): Option = register(Option(id), content)
    fun output(id: String? = null, content: Output.() -> Unit): Output = register(Output(id), content)
    fun p(id: String? = null, content: P.() -> Unit): P = register(P(id), content)
    fun param(id: String? = null, content: Param.() -> Unit): Param = register(Param(id), content)
    fun picture(id: String? = null, content: Picture.() -> Unit): Picture = register(Picture(id), content)
    fun pre(id: String? = null, content: Pre.() -> Unit): Pre = register(Pre(id), content)
    fun progress(id: String? = null, content: Progress.() -> Unit): Progress = register(Progress(id), content)
    fun quote(id: String? = null, content: Quote.() -> Unit): Quote = register(Quote(id), content)
    fun script(id: String? = null, content: Script.() -> Unit): Script = register(Script(id), content)
    fun select(id: String? = null, content: Select.() -> Unit): Select = register(Select(id), content)
    fun span(id: String? = null, content: Span.() -> Unit): Span = register(Span(id), content)
    fun caption(id: String? = null, content: Caption.() -> Unit): Caption = register(Caption(id), content)
    fun th(id: String? = null, content: Th.() -> Unit): Th = register(Th(id), content)
    fun td(id: String? = null, content: Td.() -> Unit): Td = register(Td(id), content)
    fun col(id: String? = null, content: Col.() -> Unit): Col = register(Col(id), content)
    fun colgroup(id: String? = null, content: Colgroup.() -> Unit): Colgroup = register(Colgroup(id), content)
    fun table(id: String? = null, content: Table.() -> Unit): Table = register(Table(id), content)
    fun tr(id: String? = null, content: Tr.() -> Unit): Tr = register(Tr(id), content)
    fun tfoot(id: String? = null, content: TFoot.() -> Unit): TFoot = register(TFoot(id), content)
    fun thead(id: String? = null, content: THead.() -> Unit): THead = register(THead(id), content)
    fun tbody(id: String? = null, content: TBody.() -> Unit): TBody = register(TBody(id), content)
    fun textarea(id: String? = null, content: TextArea.() -> Unit): TextArea = register(TextArea(id), content)
    fun time(id: String? = null, content: Time.() -> Unit): Time = register(Time(id), content)
    fun track(id: String? = null, content: Track.() -> Unit): Track = register(Track(id), content)
    fun ul(id: String? = null, content: Ul.() -> Unit): Ul = register(Ul(id), content)

    fun address(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("address", id), content)
    fun article(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("article", id), content)
    fun aside(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("aside", id), content)
    fun bdi(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("bdi", id), content)
    fun details(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("details", id), content)
    fun dialog(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("dialog", id), content)
    fun figcaption(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("figcaption", id), content)
    fun figure(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("figure", id), content)
    fun footer(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("footer", id), content)
    fun header(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("header", id), content)
    fun main(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("main", id), content)
    fun mark(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("mark", id), content)
    fun nav(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("nav", id), content)
    fun noscript(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("noscript", id), content)
    fun progress(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("progress", id), content)
    fun rp(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("rp", id), content)
    fun rt(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("rt", id), content)
    fun ruby(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("ruby", id), content)
    fun section(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("section", id), content)
    fun summary(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("summary", id), content)
    fun time(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("time", id), content)
    fun wbr(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("wbr", id), content)
    fun blockquote(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("blockquote", id), content)
    fun em(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("em", id), content)
    fun strong(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("strong", id), content)
    fun small(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("small", id), content)
    fun s(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("s", id), content)
    fun cite(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("cite", id), content)
    fun q(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("q", id), content)
    fun dfn(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("dfn", id), content)
    fun abbr(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("abbr", id), content)
    fun code(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("code", id), content)
    fun `var`(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("var", id), content)
    fun samp(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("samp", id), content)
    fun kbd(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("kbd", id), content)
    fun sub(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("sub", id), content)
    fun sup(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("sup", id), content)
    fun i(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("i", id), content)
    fun b(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("b", id), content)
    fun u(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("u", id), content)
    fun bdo(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("bdo", id), content)
    fun command(id: String? = null, content: TextElement.() -> Unit): TextElement = register(TextElement("command", id), content)
}
