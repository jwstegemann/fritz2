package dev.fritz2.dom.html

import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.WithText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.*


/**
 * Exposes the JavaScript [HTMLAnchorElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAnchorElement) to Kotlin
 */
open class A(id: String? = null, baseClass: String? = null) : Tag<HTMLAnchorElement>("a", id, baseClass),
    WithText<HTMLAnchorElement> {
    fun target(value: String) = attr("target", value)
	fun target(value: Flow<String>) = attr("target", value)

    fun download(value: String) = attr("download", value)
	fun download(value: Flow<String>) = attr("download", value)

    fun ping(value: String) = attr("ping", value)
	fun ping(value: Flow<String>) = attr("ping", value)

    fun rel(value: String) = attr("rel", value)
	fun rel(value: Flow<String>) = attr("rel", value)

    fun hreflang(value: String) = attr("hreflang", value)
	fun hreflang(value: Flow<String>) = attr("hreflang", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun referrerPolicy(value: String) = attr("referrerPolicy", value)
	fun referrerPolicy(value: Flow<String>) = attr("referrerPolicy", value)

    fun href(value: String) = attr("href", value)
	fun href(value: Flow<String>) = attr("href", value)

    fun protocol(value: String) = attr("protocol", value)
	fun protocol(value: Flow<String>) = attr("protocol", value)

    fun username(value: String) = attr("username", value)
	fun username(value: Flow<String>) = attr("username", value)

    fun password(value: String) = attr("password", value)
	fun password(value: Flow<String>) = attr("password", value)

    fun host(value: String) = attr("host", value)
	fun host(value: Flow<String>) = attr("host", value)

    fun hostname(value: String) = attr("hostname", value)
	fun hostname(value: Flow<String>) = attr("hostname", value)

    fun port(value: String) = attr("port", value)
	fun port(value: Flow<String>) = attr("port", value)

    fun pathname(value: String) = attr("pathname", value)
	fun pathname(value: Flow<String>) = attr("pathname", value)

    fun search(value: String) = attr("search", value)
	fun search(value: Flow<String>) = attr("search", value)

    fun hash(value: String) = attr("hash", value)
	fun hash(value: Flow<String>) = attr("hash", value)

}


/**
 * Exposes the JavaScript [HTMLAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAreaElement) to Kotlin
 */
open class Area(id: String? = null, baseClass: String? = null) : Tag<HTMLAreaElement>("area", id, baseClass),
    WithText<HTMLAreaElement> {
    fun alt(value: String) = attr("alt", value)
	fun alt(value: Flow<String>) = attr("alt", value)

    fun coords(value: String) = attr("coords", value)
	fun coords(value: Flow<String>) = attr("coords", value)

    fun shape(value: String) = attr("shape", value)
	fun shape(value: Flow<String>) = attr("shape", value)

    fun target(value: String) = attr("target", value)
	fun target(value: Flow<String>) = attr("target", value)

    fun download(value: String) = attr("download", value)
	fun download(value: Flow<String>) = attr("download", value)

    fun ping(value: String) = attr("ping", value)
	fun ping(value: Flow<String>) = attr("ping", value)

    fun rel(value: String) = attr("rel", value)
	fun rel(value: Flow<String>) = attr("rel", value)

    fun referrerPolicy(value: String) = attr("referrerPolicy", value)
	fun referrerPolicy(value: Flow<String>) = attr("referrerPolicy", value)

    fun href(value: String) = attr("href", value)
	fun href(value: Flow<String>) = attr("href", value)

    fun protocol(value: String) = attr("protocol", value)
	fun protocol(value: Flow<String>) = attr("protocol", value)

    fun username(value: String) = attr("username", value)
	fun username(value: Flow<String>) = attr("username", value)

    fun password(value: String) = attr("password", value)
	fun password(value: Flow<String>) = attr("password", value)

    fun host(value: String) = attr("host", value)
	fun host(value: Flow<String>) = attr("host", value)

    fun hostname(value: String) = attr("hostname", value)
	fun hostname(value: Flow<String>) = attr("hostname", value)

    fun port(value: String) = attr("port", value)
	fun port(value: Flow<String>) = attr("port", value)

    fun pathname(value: String) = attr("pathname", value)
	fun pathname(value: Flow<String>) = attr("pathname", value)

    fun search(value: String) = attr("search", value)
	fun search(value: Flow<String>) = attr("search", value)

    fun hash(value: String) = attr("hash", value)
	fun hash(value: Flow<String>) = attr("hash", value)

}


/**
 * Exposes the JavaScript [HTMLBRElement](https://developer.mozilla.org/en/docs/Web/API/HTMLBRElement) to Kotlin
 */
open class Br(id: String? = null, baseClass: String? = null) : Tag<HTMLBRElement>("br", id, baseClass)


/**
 * Exposes the JavaScript [HTMLButtonElement](https://developer.mozilla.org/en/docs/Web/API/HTMLButtonElement) to Kotlin
 */
open class Button(id: String? = null, baseClass: String? = null) : Tag<HTMLButtonElement>("button", id, baseClass),
    WithText<HTMLButtonElement> {
    var autofocus: Flow<Boolean> by BooleanAttributeDelegate
    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    fun formAction(value: String) = attr("formAction", value)
	fun formAction(value: Flow<String>) = attr("formAction", value)

    fun formEnctype(value: String) = attr("formEnctype", value)
	fun formEnctype(value: Flow<String>) = attr("formEnctype", value)

    fun formMethod(value: String) = attr("formMethod", value)
	fun formMethod(value: Flow<String>) = attr("formMethod", value)

    var formNoValidate: Flow<Boolean> by BooleanAttributeDelegate
    fun formTarget(value: String) = attr("formTarget", value)
	fun formTarget(value: Flow<String>) = attr("formTarget", value)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    var value: Flow<String> by ValueAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLCanvasElement](https://developer.mozilla.org/en/docs/Web/API/HTMLCanvasElement) to Kotlin
 */
open class Canvas(id: String? = null, baseClass: String? = null) : Tag<HTMLCanvasElement>("canvas", id, baseClass),
    WithText<HTMLCanvasElement> {
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLDListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDListElement) to Kotlin
 */
open class Dl(id: String? = null, baseClass: String? = null) : Tag<HTMLDListElement>("dl", id, baseClass),
    WithText<HTMLDListElement>


/**
 * Exposes the JavaScript [HTMLDataElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataElement) to Kotlin
 */
open class Data(id: String? = null, baseClass: String? = null) : Tag<HTMLDataElement>("data", id, baseClass),
    WithText<HTMLDataElement> {
    var value: Flow<String> by ValueAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLDataListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataListElement) to Kotlin
 */
open class DataList(id: String? = null, baseClass: String? = null) : Tag<HTMLDataListElement>("datalist", id, baseClass),
    WithText<HTMLDataListElement>


/**
 * Exposes the JavaScript [HTMLDetailsElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDetailsElement) to Kotlin
 */
open class Details(id: String? = null, baseClass: String? = null) : Tag<HTMLDetailsElement>("details", id, baseClass),
    WithText<HTMLDetailsElement> {
    var open: Flow<Boolean> by BooleanAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLDialogElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDialogElement) to Kotlin
 */
open class Dialog(id: String? = null, baseClass: String? = null) : Tag<HTMLDialogElement>("dialog", id, baseClass),
    WithText<HTMLDialogElement> {
    var open: Flow<Boolean> by BooleanAttributeDelegate
    fun returnValue(value: String) = attr("returnValue", value)
	fun returnValue(value: Flow<String>) = attr("returnValue", value)

}


/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
open class Div(id: String? = null, baseClass: String? = null) : Tag<HTMLDivElement>("div", id, baseClass),
    WithText<HTMLDivElement> {
    fun align(value: String) = attr("align", value)
	fun align(value: Flow<String>) = attr("align", value)

}


/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
open class Embed(id: String? = null, baseClass: String? = null) : Tag<HTMLEmbedElement>("embed", id, baseClass) {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun width(value: String) = attr("width", value)
	fun width(value: Flow<String>) = attr("width", value)

    fun height(value: String) = attr("height", value)
	fun height(value: Flow<String>) = attr("height", value)

}


/**
 * Exposes the JavaScript [HTMLFieldSetElement](https://developer.mozilla.org/en/docs/Web/API/HTMLFieldSetElement) to Kotlin
 */
open class FieldSet(id: String? = null, baseClass: String? = null) : Tag<HTMLFieldSetElement>("fieldSet", id, baseClass) {
    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

}


/**
 * Exposes the JavaScript [HTMLFormElement](https://developer.mozilla.org/en/docs/Web/API/`for`mElement) to Kotlin
 */
open class Form(id: String? = null, baseClass: String? = null) : Tag<HTMLFormElement>("form", id, baseClass),
    WithText<HTMLFormElement> {
    fun acceptCharset(value: String) = attr("acceptCharset", value)
	fun acceptCharset(value: Flow<String>) = attr("acceptCharset", value)

    fun action(value: String) = attr("action", value)
	fun action(value: Flow<String>) = attr("action", value)

    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    fun enctype(value: String) = attr("enctype", value)
	fun enctype(value: Flow<String>) = attr("enctype", value)

    fun encoding(value: String) = attr("encoding", value)
	fun encoding(value: Flow<String>) = attr("encoding", value)

    fun method(value: String) = attr("method", value)
	fun method(value: Flow<String>) = attr("method", value)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    var noValidate: Flow<Boolean> by BooleanAttributeDelegate
    fun target(value: String) = attr("target", value)
	fun target(value: Flow<String>) = attr("target", value)

}


/**
 * Exposes the JavaScript [HTMLHRElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHRElement) to Kotlin
 */
open class Hr(id: String? = null, baseClass: String? = null) : Tag<HTMLHRElement>("hr", id, baseClass)


/**
 * Exposes the JavaScript [HTMLHeadingElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHeadingElement) to Kotlin
 */
open class H(num: Int, id: String? = null, baseClass: String? = null) : Tag<HTMLHeadingElement>("h$num", id, baseClass),
    WithText<HTMLHeadingElement>


/**
 * Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin
 */
open class IFrame(id: String? = null, baseClass: String? = null) : Tag<HTMLIFrameElement>("iframe", id, baseClass),
    WithText<HTMLIFrameElement> {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun srcdoc(value: String) = attr("srcdoc", value)
	fun srcdoc(value: Flow<String>) = attr("srcdoc", value)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    var allowFullscreen: Flow<Boolean> by BooleanAttributeDelegate
    var allowUserMedia: Flow<Boolean> by BooleanAttributeDelegate
    fun width(value: String) = attr("width", value)
	fun width(value: Flow<String>) = attr("width", value)

    fun height(value: String) = attr("height", value)
	fun height(value: Flow<String>) = attr("height", value)

    fun referrerPolicy(value: String) = attr("referrerPolicy", value)
	fun referrerPolicy(value: Flow<String>) = attr("referrerPolicy", value)

}


/**
 * Exposes the JavaScript [HTMLImageElement](https://developer.mozilla.org/en/docs/Web/API/HTMLImageElement) to Kotlin
 */
open class Img(id: String? = null, baseClass: String? = null) : Tag<HTMLImageElement>("img", id, baseClass),
    WithText<HTMLImageElement> {
    fun alt(value: String) = attr("alt", value)
	fun alt(value: Flow<String>) = attr("alt", value)

    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun srcset(value: String) = attr("srcset", value)
	fun srcset(value: Flow<String>) = attr("srcset", value)

    fun sizes(value: String) = attr("sizes", value)
	fun sizes(value: Flow<String>) = attr("sizes", value)

    fun crossOrigin(value: String) = attr("crossOrigin", value)
	fun crossOrigin(value: Flow<String>) = attr("crossOrigin", value)

    fun useMap(value: String) = attr("useMap", value)
	fun useMap(value: Flow<String>) = attr("useMap", value)

    var isMap: Flow<Boolean> by BooleanAttributeDelegate
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
    fun referrerPolicy(value: String) = attr("referrerPolicy", value)
	fun referrerPolicy(value: Flow<String>) = attr("referrerPolicy", value)

}


/**
 * Exposes the JavaScript [HTMLInputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLInputElement) to Kotlin
 */
open class Input(id: String? = null, baseClass: String? = null) : Tag<HTMLInputElement>("input", id, baseClass) {
    fun accept(value: String) = attr("accept", value)
	fun accept(value: Flow<String>) = attr("accept", value)

    fun alt(value: String) = attr("alt", value)
	fun alt(value: Flow<String>) = attr("alt", value)

    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    var autofocus: Flow<Boolean> by BooleanAttributeDelegate
    var defaultChecked: Flow<Boolean> by BooleanAttributeDelegate
    var checked: Flow<Boolean> by CheckedAttributeDelegate
    fun dirName(value: String) = attr("dirName", value)
	fun dirName(value: Flow<String>) = attr("dirName", value)

    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    fun formAction(value: String) = attr("formAction", value)
	fun formAction(value: Flow<String>) = attr("formAction", value)

    fun formEnctype(value: String) = attr("formEnctype", value)
	fun formEnctype(value: Flow<String>) = attr("formEnctype", value)

    fun formMethod(value: String) = attr("formMethod", value)
	fun formMethod(value: Flow<String>) = attr("formMethod", value)

    var formNoValidate: Flow<Boolean> by BooleanAttributeDelegate
    fun formTarget(value: String) = attr("formTarget", value)
	fun formTarget(value: Flow<String>) = attr("formTarget", value)

    var height: Flow<Int> by AttributeDelegate
    var indeterminate: Flow<Boolean> by BooleanAttributeDelegate
    fun inputMode(value: String) = attr("inputMode", value)
	fun inputMode(value: Flow<String>) = attr("inputMode", value)

    fun max(value: String) = attr("max", value)
	fun max(value: Flow<String>) = attr("max", value)

    var maxLength: Flow<Int> by AttributeDelegate
    fun min(value: String) = attr("min", value)
	fun min(value: Flow<String>) = attr("min", value)

    var minLength: Flow<Int> by AttributeDelegate
    var multiple: Flow<Boolean> by BooleanAttributeDelegate
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun pattern(value: String) = attr("pattern", value)
	fun pattern(value: Flow<String>) = attr("pattern", value)

    fun placeholder(value: String) = attr("placeholder", value)
	fun placeholder(value: Flow<String>) = attr("placeholder", value)

    var readOnly: Flow<Boolean> by BooleanAttributeDelegate
    var required: Flow<Boolean> by BooleanAttributeDelegate
    var size: Flow<Int> by AttributeDelegate
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun step(value: String) = attr("step", value)
	fun step(value: Flow<String>) = attr("step", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun defaultValue(value: String) = attr("defaultValue", value)
	fun defaultValue(value: Flow<String>) = attr("defaultValue", value)

    var value: Flow<String> by ValueAttributeDelegate
    var width: Flow<Int> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLLIElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLIElement) to Kotlin
 */
open class Li(id: String? = null, baseClass: String? = null) : Tag<HTMLLIElement>("li", id, baseClass),
    WithText<HTMLLIElement> {
    var value: Flow<Int> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLLabelElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLabelElement) to Kotlin
 */
open class Label(id: String? = null, baseClass: String? = null) : Tag<HTMLLabelElement>("label", id, baseClass),
    WithText<HTMLLabelElement> {
    fun `for`(value: String) = attr("`for`", value)
	fun `for`(value: Flow<String>) = attr("`for`", value)

}


/**
 * Exposes the JavaScript [HTMLLegendElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLegendElement) to Kotlin
 */
open class Legend(id: String? = null, baseClass: String? = null) : Tag<HTMLLegendElement>("legend", id, baseClass),
    WithText<HTMLLegendElement>


/**
 * Exposes the JavaScript [HTMLMapElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMapElement) to Kotlin
 */
open class Map(id: String? = null, baseClass: String? = null) : Tag<HTMLMapElement>("map", id, baseClass),
    WithText<HTMLMapElement> {
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

}


/**
 * Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin
 */
open class Audio(id: String? = null, baseClass: String? = null) : Tag<HTMLAudioElement>("audio", id, baseClass),
    WithText<HTMLAudioElement> {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun preload(value: String) = attr("preload", value)
	fun preload(value: Flow<String>) = attr("preload", value)

    var currentTime: Flow<Double> by AttributeDelegate
    var defaultPlaybackRate: Flow<Double> by AttributeDelegate
    var playbackRate: Flow<Double> by AttributeDelegate
    var autoplay: Flow<Boolean> by BooleanAttributeDelegate
    var loop: Flow<Boolean> by BooleanAttributeDelegate
    var controls: Flow<Boolean> by BooleanAttributeDelegate
    var volume: Flow<Double> by AttributeDelegate
    var muted: Flow<Boolean> by BooleanAttributeDelegate
    var defaultMuted: Flow<Boolean> by BooleanAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLVideoElement](https://developer.mozilla.org/en/docs/Web/API/HTMLVideoElement) to Kotlin
 */
open class Video(id: String? = null, baseClass: String? = null) : Tag<HTMLVideoElement>("video", id, baseClass),
    WithText<HTMLVideoElement> {
    var width: Flow<Int> by AttributeDelegate
    var height: Flow<Int> by AttributeDelegate
    fun poster(value: String) = attr("poster", value)
	fun poster(value: Flow<String>) = attr("poster", value)

    var playsInline: Flow<Boolean> by BooleanAttributeDelegate
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun preload(value: String) = attr("preload", value)
	fun preload(value: Flow<String>) = attr("preload", value)

    var currentTime: Flow<Double> by AttributeDelegate
    var defaultPlaybackRate: Flow<Double> by AttributeDelegate
    var playbackRate: Flow<Double> by AttributeDelegate
    var autoplay: Flow<Boolean> by BooleanAttributeDelegate
    var loop: Flow<Boolean> by BooleanAttributeDelegate
    var controls: Flow<Boolean> by BooleanAttributeDelegate
    var volume: Flow<Double> by AttributeDelegate
    var muted: Flow<Boolean> by BooleanAttributeDelegate
    var defaultMuted: Flow<Boolean> by BooleanAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLMeterElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMeterElement) to Kotlin
 */
open class Meter(id: String? = null, baseClass: String? = null) : Tag<HTMLMeterElement>("meter", id, baseClass),
    WithText<HTMLMeterElement> {
    var value: Flow<Double> by AttributeDelegate
    var min: Flow<Double> by AttributeDelegate
    var max: Flow<Double> by AttributeDelegate
    var low: Flow<Double> by AttributeDelegate
    var high: Flow<Double> by AttributeDelegate
    var optimum: Flow<Double> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
open class Ins(id: String? = null, baseClass: String? = null) : Tag<HTMLModElement>("ins", id, baseClass),
    WithText<HTMLModElement> {
    fun cite(value: String) = attr("cite", value)
	fun cite(value: Flow<String>) = attr("cite", value)

    fun dateTime(value: String) = attr("dateTime", value)
	fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
open class Del(id: String? = null, baseClass: String? = null) : Tag<HTMLModElement>("del", id, baseClass),
    WithText<HTMLModElement> {
    fun cite(value: String) = attr("cite", value)
	fun cite(value: Flow<String>) = attr("cite", value)

    fun dateTime(value: String) = attr("dateTime", value)
	fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLOListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOListElement) to Kotlin
 */
open class Ol(id: String? = null, baseClass: String? = null) : Tag<HTMLOListElement>("ol", id, baseClass),
    WithText<HTMLOListElement> {
    var reversed: Flow<Boolean> by BooleanAttributeDelegate
    var start: Flow<Int> by AttributeDelegate
    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

}


/**
 * Exposes the JavaScript [HTMLObjectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLObjectElement) to Kotlin
 */
open class Object(id: String? = null, baseClass: String? = null) : Tag<HTMLObjectElement>("object", id, baseClass),
    WithText<HTMLObjectElement> {
    fun data(value: String) = attr("data", value)
	fun data(value: Flow<String>) = attr("data", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    var typeMustMatch: Flow<Boolean> by BooleanAttributeDelegate
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun useMap(value: String) = attr("useMap", value)
	fun useMap(value: Flow<String>) = attr("useMap", value)

    fun width(value: String) = attr("width", value)
	fun width(value: Flow<String>) = attr("width", value)

    fun height(value: String) = attr("height", value)
	fun height(value: Flow<String>) = attr("height", value)

}


/**
 * Exposes the JavaScript [HTMLOptGroupElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptGroupElement) to Kotlin
 */
open class Optgroup(id: String? = null, baseClass: String? = null) : Tag<HTMLOptGroupElement>("optgroup", id, baseClass),
    WithText<HTMLOptGroupElement> {
    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    fun label(value: String) = attr("label", value)
	fun label(value: Flow<String>) = attr("label", value)

}


/**
 * Exposes the JavaScript [HTMLOptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptionElement) to Kotlin
 */
open class Option(id: String? = null, baseClass: String? = null) : Tag<HTMLOptionElement>("option", id, baseClass),
    WithText<HTMLOptionElement> {
    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    fun label(value: String) = attr("label", value)
	fun label(value: Flow<String>) = attr("label", value)

    var defaultSelected: Flow<Boolean> by BooleanAttributeDelegate
    var selected: Flow<Boolean> by BooleanAttributeDelegate
    var value: Flow<String> by ValueAttributeDelegate

}


/**
 * Exposes the JavaScript [HTMLOutputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOutputElement) to Kotlin
 */
open class Output(id: String? = null, baseClass: String? = null) : Tag<HTMLOutputElement>("output", id, baseClass),
    WithText<HTMLOutputElement> {
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun defaultValue(value: String) = attr("defaultValue", value)
	fun defaultValue(value: Flow<String>) = attr("defaultValue", value)

    var value: Flow<String> by ValueAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLParagraphElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParagraphElement) to Kotlin
 */
open class P(id: String? = null, baseClass: String? = null) : Tag<HTMLParagraphElement>("p", id, baseClass),
    WithText<HTMLParagraphElement>


/**
 * Exposes the JavaScript [HTMLParamElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParamElement) to Kotlin
 */
open class Param(id: String? = null, baseClass: String? = null) : Tag<HTMLParamElement>("param", id, baseClass),
    WithText<HTMLParamElement> {
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    var value: Flow<String> by ValueAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLPictureElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPictureElement) to Kotlin
 */
open class Picture(id: String? = null, baseClass: String? = null) : Tag<HTMLPictureElement>("picture", id, baseClass),
    WithText<HTMLPictureElement>


/**
 * Exposes the JavaScript [HTMLPreElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPreElement) to Kotlin
 */
open class Pre(id: String? = null, baseClass: String? = null) : Tag<HTMLPreElement>("pre", id, baseClass),
    WithText<HTMLPreElement>


/**
 * Exposes the JavaScript [HTMLProgressElement](https://developer.mozilla.org/en/docs/Web/API/HTMLProgressElement) to Kotlin
 */
open class Progress(id: String? = null, baseClass: String? = null) : Tag<HTMLProgressElement>("progress", id, baseClass),
    WithText<HTMLProgressElement> {
    var value: Flow<Double> by AttributeDelegate
    var max: Flow<Double> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLQuoteElement](https://developer.mozilla.org/en/docs/Web/API/HTMLQuoteElement) to Kotlin
 */
open class Quote(id: String? = null, baseClass: String? = null) : Tag<HTMLQuoteElement>("quote", id, baseClass),
    WithText<HTMLQuoteElement> {
    fun cite(value: String) = attr("cite", value)
	fun cite(value: Flow<String>) = attr("cite", value)

}


/**
 * Exposes the JavaScript [HTMLScriptElement](https://developer.mozilla.org/en/docs/Web/API/HTMLScriptElement) to Kotlin
 */
open class Script(id: String? = null, baseClass: String? = null) : Tag<HTMLScriptElement>("script", id, baseClass),
    WithText<HTMLScriptElement> {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun charset(value: String) = attr("charset", value)
	fun charset(value: Flow<String>) = attr("charset", value)

    var async: Flow<Boolean> by BooleanAttributeDelegate
    var defer: Flow<Boolean> by BooleanAttributeDelegate

    fun nonce(value: String) = attr("nonce", value)
	fun nonce(value: Flow<String>) = attr("nonce", value)

    fun event(value: String) = attr("event", value)
	fun event(value: Flow<String>) = attr("event", value)

    fun `for`(value: String) = attr("`for`", value)
	fun `for`(value: Flow<String>) = attr("`for`", value)

}


/**
 * Exposes the JavaScript [HTMLSelectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLSelectElement) to Kotlin
 */
open class Select(id: String? = null, baseClass: String? = null) : Tag<HTMLSelectElement>("select", id, baseClass) {
    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    var autofocus: Flow<Boolean> by BooleanAttributeDelegate
    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    var multiple: Flow<Boolean> by BooleanAttributeDelegate
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    var required: Flow<Boolean> by BooleanAttributeDelegate
    var size: Flow<Int> by AttributeDelegate
    var length: Flow<Int> by AttributeDelegate
    var selectedIndex: Flow<Int> by AttributeDelegate
    var value: Flow<String> by ValueAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLSpanElement](https://developer.mozilla.org/en/docs/Web/API/HTMLSpanElement) to Kotlin
 */
open class Span(id: String? = null, baseClass: String? = null) : Tag<HTMLSpanElement>("span", id, baseClass),
    WithText<HTMLSpanElement>


/**
 * Exposes the JavaScript [HTMLTableCaptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCaptionElement) to Kotlin
 */
open class Caption(id: String? = null, baseClass: String? = null) : Tag<HTMLTableCaptionElement>("caption", id, baseClass),
    WithText<HTMLTableCaptionElement>


/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
open class Th(id: String? = null, baseClass: String? = null) : Tag<HTMLTableCellElement>("th", id, baseClass),
    WithText<HTMLTableCellElement> {
    var colSpan: Flow<Int> by AttributeDelegate
    var rowSpan: Flow<Int> by AttributeDelegate
    fun headers(value: String) = attr("headers", value)
	fun headers(value: Flow<String>) = attr("headers", value)

    fun scope(value: String) = attr("scope", value)
	fun scope(value: Flow<String>) = attr("scope", value)

    fun abbr(value: String) = attr("abbr", value)
	fun abbr(value: Flow<String>) = attr("abbr", value)

}


/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
open class Td(id: String? = null, baseClass: String? = null) : Tag<HTMLTableCellElement>("td", id, baseClass),
    WithText<HTMLTableCellElement> {
    var colSpan: Flow<Int> by AttributeDelegate
    var rowSpan: Flow<Int> by AttributeDelegate
    fun headers(value: String) = attr("headers", value)
	fun headers(value: Flow<String>) = attr("headers", value)

    fun scope(value: String) = attr("scope", value)
	fun scope(value: Flow<String>) = attr("scope", value)

    fun abbr(value: String) = attr("abbr", value)
	fun abbr(value: Flow<String>) = attr("abbr", value)

}


/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
open class Col(id: String? = null, baseClass: String? = null) : Tag<HTMLTableColElement>("col", id, baseClass),
    WithText<HTMLTableColElement> {
    var span: Flow<Int> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
open class Colgroup(id: String? = null, baseClass: String? = null) : Tag<HTMLTableColElement>("colgroup", id, baseClass),
    WithText<HTMLTableColElement> {
    var span: Flow<Int> by AttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLTableElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableElement) to Kotlin
 */
open class Table(id: String? = null, baseClass: String? = null) : Tag<HTMLTableElement>("table", id, baseClass)


/**
 * Exposes the JavaScript [HTMLTableRowElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableRowElement) to Kotlin
 */
open class Tr(id: String? = null, baseClass: String? = null) : Tag<HTMLTableRowElement>("tr", id, baseClass),
    WithText<HTMLTableRowElement>


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class TFoot(id: String? = null, baseClass: String? = null) : Tag<HTMLTableSectionElement>("tfoot", id, baseClass)


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class THead(id: String? = null, baseClass: String? = null) : Tag<HTMLTableSectionElement>("thead", id, baseClass)


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class TBody(id: String? = null, baseClass: String? = null) : Tag<HTMLTableSectionElement>("tbody", id, baseClass)


/**
 * Exposes the JavaScript [HTMLTextAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTextAreaElement) to Kotlin
 */
open class TextArea(id: String? = null, baseClass: String? = null) : Tag<HTMLTextAreaElement>("textarea", id, baseClass),
    WithText<HTMLTextAreaElement> {
    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    var autofocus: Flow<Boolean> by BooleanAttributeDelegate
    var cols: Flow<Int> by AttributeDelegate
    fun dirName(value: String) = attr("dirName", value)
	fun dirName(value: Flow<String>) = attr("dirName", value)

    var disabled: Flow<Boolean> by BooleanAttributeDelegate
    fun inputMode(value: String) = attr("inputMode", value)
	fun inputMode(value: Flow<String>) = attr("inputMode", value)

    var maxLength: Flow<Int> by AttributeDelegate
    var minLength: Flow<Int> by AttributeDelegate
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun placeholder(value: String) = attr("placeholder", value)
	fun placeholder(value: Flow<String>) = attr("placeholder", value)

    var readOnly: Flow<Boolean> by BooleanAttributeDelegate
    var required: Flow<Boolean> by BooleanAttributeDelegate
    var rows: Flow<Int> by AttributeDelegate
    fun wrap(value: String) = attr("wrap", value)
	fun wrap(value: Flow<String>) = attr("wrap", value)

    fun defaultValue(value: String) = attr("defaultValue", value)
	fun defaultValue(value: Flow<String>) = attr("defaultValue", value)

    var value: Flow<String> by ValueAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLTimeElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTimeElement) to Kotlin
 */
open class Time(id: String? = null, baseClass: String? = null) : Tag<HTMLTimeElement>("time", id, baseClass),
    WithText<HTMLTimeElement> {
    fun dateTime(value: String) = attr("dateTime", value)
	fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLTrackElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTrackElement) to Kotlin
 */
open class Track(id: String? = null, baseClass: String? = null) : Tag<HTMLTrackElement>("track", id, baseClass),
    WithText<HTMLTrackElement> {
    fun kind(value: String) = attr("kind", value)
	fun kind(value: Flow<String>) = attr("kind", value)

    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun srclang(value: String) = attr("srclang", value)
	fun srclang(value: Flow<String>) = attr("srclang", value)

    fun label(value: String) = attr("label", value)
	fun label(value: Flow<String>) = attr("label", value)

    var default: Flow<Boolean> by BooleanAttributeDelegate
}


/**
 * Exposes the JavaScript [HTMLUListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLUListElement) to Kotlin
 */
open class Ul(id: String? = null, baseClass: String? = null) : Tag<HTMLUListElement>("ul", id, baseClass)


/**
 * General class for standard html5 elements
 */
open class TextElement(tagName: String, id: String? = null, baseClass: String? = null) :
    Tag<HTMLElement>(tagName, id, baseClass), WithText<HTMLElement>


/**
 * Context for rendering dynamically DOM-nodes to your page.
 */
interface RenderContext {
    val job: Job

    fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T

    fun <T> Flow<T>.asString() = this.map { it.toString() }
}

interface HtmlElements {
    fun custom(localName: String, content: Tag<HTMLElement>.() -> Unit): Tag<HTMLElement> =
        register(Tag(localName), content)

    fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T
    fun a(baseClass: String? = null, id: String? = null, content: A.() -> Unit): A = register(A(id, baseClass), content)
    fun area(baseClass: String? = null, id: String? = null, content: Area.() -> Unit): Area =
        register(Area(id, baseClass), content)

    fun br(baseClass: String? = null, id: String? = null, content: Br.() -> Unit): Br =
        register(Br(id, baseClass), content)

    fun button(baseClass: String? = null, id: String? = null, content: Button.() -> Unit): Button =
        register(Button(id, baseClass), content)

    fun canvas(baseClass: String? = null, id: String? = null, content: Canvas.() -> Unit): Canvas =
        register(Canvas(id, baseClass), content)

    fun dl(baseClass: String? = null, id: String? = null, content: Dl.() -> Unit): Dl =
        register(Dl(id, baseClass), content)

    fun dt(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dt", id, baseClass), content)

    fun dd(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dd", id, baseClass), content)

    fun data(baseClass: String? = null, id: String? = null, content: Data.() -> Unit): Data =
        register(Data(id, baseClass), content)

    fun datalist(baseClass: String? = null, id: String? = null, content: DataList.() -> Unit): DataList =
        register(DataList(id, baseClass), content)

    fun details(baseClass: String? = null, id: String? = null, content: Details.() -> Unit): Details =
        register(Details(id, baseClass), content)

    fun dialog(baseClass: String? = null, id: String? = null, content: Dialog.() -> Unit): Dialog =
        register(Dialog(id, baseClass), content)

    fun div(baseClass: String? = null, id: String? = null, content: Div.() -> Unit): Div =
        register(Div(id, baseClass), content)

    fun embed(baseClass: String? = null, id: String? = null, content: Embed.() -> Unit): Embed =
        register(Embed(id, baseClass), content)

    fun fieldset(baseClass: String? = null, id: String? = null, content: FieldSet.() -> Unit): FieldSet =
        register(FieldSet(id, baseClass), content)

    fun form(baseClass: String? = null, id: String? = null, content: Form.() -> Unit): Form =
        register(Form(id, baseClass), content)

    fun hr(baseClass: String? = null, id: String? = null, content: Hr.() -> Unit): Hr =
        register(Hr(id, baseClass), content)

    fun h1(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(1, id, baseClass), content)

    fun h2(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(2, id, baseClass), content)

    fun h3(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(3, id, baseClass), content)

    fun h4(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(4, id, baseClass), content)

    fun h5(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(5, id, baseClass), content)

    fun h6(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(6, id, baseClass), content)

    fun iframe(baseClass: String? = null, id: String? = null, content: IFrame.() -> Unit): IFrame =
        register(IFrame(id, baseClass), content)

    fun img(baseClass: String? = null, id: String? = null, content: Img.() -> Unit): Img =
        register(Img(id, baseClass), content)

    fun input(baseClass: String? = null, id: String? = null, content: Input.() -> Unit): Input =
        register(Input(id, baseClass), content)

    fun li(baseClass: String? = null, id: String? = null, content: Li.() -> Unit): Li =
        register(Li(id, baseClass), content)

    fun label(baseClass: String? = null, id: String? = null, `for`: String? = null, content: Label.() -> Unit): Label {
        val label = Label(id, baseClass)
        if (`for` != null) label.domNode.setAttribute("for", `for`)
        return register(label, content)
    }

    fun legend(baseClass: String? = null, id: String? = null, content: Legend.() -> Unit): Legend =
        register(Legend(id, baseClass), content)

    fun map(baseClass: String? = null, id: String? = null, content: Map.() -> Unit): Map =
        register(Map(id, baseClass), content)

    fun audio(baseClass: String? = null, id: String? = null, content: Audio.() -> Unit): Audio =
        register(Audio(id, baseClass), content)

    fun video(baseClass: String? = null, id: String? = null, content: Video.() -> Unit): Video =
        register(Video(id, baseClass), content)

    fun meter(baseClass: String? = null, id: String? = null, content: Meter.() -> Unit): Meter =
        register(Meter(id, baseClass), content)

    fun ins(baseClass: String? = null, id: String? = null, content: Ins.() -> Unit): Ins =
        register(Ins(id, baseClass), content)

    fun del(baseClass: String? = null, id: String? = null, content: Del.() -> Unit): Del =
        register(Del(id, baseClass), content)

    fun ol(baseClass: String? = null, id: String? = null, content: Ol.() -> Unit): Ol =
        register(Ol(id, baseClass), content)

    fun `object`(baseClass: String? = null, id: String? = null, content: Object.() -> Unit): Object =
        register(Object(id, baseClass), content)

    fun optgroup(baseClass: String? = null, id: String? = null, content: Optgroup.() -> Unit): Optgroup =
        register(Optgroup(id, baseClass), content)

    fun option(baseClass: String? = null, id: String? = null, content: Option.() -> Unit): Option =
        register(Option(id, baseClass), content)

    fun output(baseClass: String? = null, id: String? = null, content: Output.() -> Unit): Output =
        register(Output(id, baseClass), content)

    fun p(baseClass: String? = null, id: String? = null, content: P.() -> Unit): P = register(P(id, baseClass), content)
    fun param(baseClass: String? = null, id: String? = null, content: Param.() -> Unit): Param =
        register(Param(id, baseClass), content)

    fun picture(baseClass: String? = null, id: String? = null, content: Picture.() -> Unit): Picture =
        register(Picture(id, baseClass), content)

    fun pre(baseClass: String? = null, id: String? = null, content: Pre.() -> Unit): Pre =
        register(Pre(id, baseClass), content)

    fun progress(baseClass: String? = null, id: String? = null, content: Progress.() -> Unit): Progress =
        register(Progress(id, baseClass), content)

    fun quote(baseClass: String? = null, id: String? = null, content: Quote.() -> Unit): Quote =
        register(Quote(id, baseClass), content)

    fun script(baseClass: String? = null, id: String? = null, content: Script.() -> Unit): Script =
        register(Script(id, baseClass), content)

    fun select(baseClass: String? = null, id: String? = null, content: Select.() -> Unit): Select =
        register(Select(id, baseClass), content)

    fun span(baseClass: String? = null, id: String? = null, content: Span.() -> Unit): Span =
        register(Span(id, baseClass), content)

    fun caption(baseClass: String? = null, id: String? = null, content: Caption.() -> Unit): Caption =
        register(Caption(id, baseClass), content)

    fun th(baseClass: String? = null, id: String? = null, content: Th.() -> Unit): Th =
        register(Th(id, baseClass), content)

    fun td(baseClass: String? = null, id: String? = null, content: Td.() -> Unit): Td =
        register(Td(id, baseClass), content)

    fun col(baseClass: String? = null, id: String? = null, content: Col.() -> Unit): Col =
        register(Col(id, baseClass), content)

    fun colgroup(baseClass: String? = null, id: String? = null, content: Colgroup.() -> Unit): Colgroup =
        register(Colgroup(id, baseClass), content)

    fun table(baseClass: String? = null, id: String? = null, content: Table.() -> Unit): Table =
        register(Table(id, baseClass), content)

    fun tr(baseClass: String? = null, id: String? = null, content: Tr.() -> Unit): Tr =
        register(Tr(id, baseClass), content)

    fun tfoot(baseClass: String? = null, id: String? = null, content: TFoot.() -> Unit): TFoot =
        register(TFoot(id, baseClass), content)

    fun thead(baseClass: String? = null, id: String? = null, content: THead.() -> Unit): THead =
        register(THead(id, baseClass), content)

    fun tbody(baseClass: String? = null, id: String? = null, content: TBody.() -> Unit): TBody =
        register(TBody(id, baseClass), content)

    fun textarea(baseClass: String? = null, id: String? = null, content: TextArea.() -> Unit): TextArea =
        register(TextArea(id, baseClass), content)

    fun time(baseClass: String? = null, id: String? = null, content: Time.() -> Unit): Time =
        register(Time(id, baseClass), content)

    fun track(baseClass: String? = null, id: String? = null, content: Track.() -> Unit): Track =
        register(Track(id, baseClass), content)

    fun ul(baseClass: String? = null, id: String? = null, content: Ul.() -> Unit): Ul =
        register(Ul(id, baseClass), content)

    fun address(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("address", id, baseClass), content)

    fun article(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("article", id, baseClass), content)

    fun aside(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("aside", id, baseClass), content)

    fun bdi(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("bdi", id, baseClass), content)

    fun details(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("details", id, baseClass), content)

    fun dialog(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dialog", id, baseClass), content)

    fun figcaption(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("figcaption", id, baseClass), content)

    fun figure(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("figure", id, baseClass), content)

    fun footer(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("footer", id, baseClass), content)

    fun header(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("header", id, baseClass), content)

    fun main(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("main", id, baseClass), content)

    fun mark(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("mark", id, baseClass), content)

    fun nav(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("nav", id, baseClass), content)

    fun noscript(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("noscript", id, baseClass), content)

    fun progress(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("progress", id, baseClass), content)

    fun rp(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("rp", id, baseClass), content)

    fun rt(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("rt", id, baseClass), content)

    fun ruby(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("ruby", id, baseClass), content)

    fun section(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("section", id, baseClass), content)

    fun summary(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("summary", id, baseClass), content)

    fun time(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("time", id, baseClass), content)

    fun wbr(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("wbr", id, baseClass), content)

    fun blockquote(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("blockquote", id, baseClass), content)

    fun em(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("em", id, baseClass), content)

    fun strong(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("strong", id, baseClass), content)

    fun small(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("small", id, baseClass), content)

    fun s(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("s", id, baseClass), content)

    fun cite(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("cite", id, baseClass), content)

    fun q(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("q", id, baseClass), content)

    fun dfn(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dfn", id, baseClass), content)

    fun abbr(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("abbr", id, baseClass), content)

    fun code(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("code", id, baseClass), content)

    fun `var`(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("var", id, baseClass), content)

    fun samp(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("samp", id, baseClass), content)

    fun kbd(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("kbd", id, baseClass), content)

    fun sub(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("sub", id, baseClass), content)

    fun sup(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("sup", id, baseClass), content)

    fun i(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("i", id, baseClass), content)

    fun b(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("b", id, baseClass), content)

    fun u(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("u", id, baseClass), content)

    fun bdo(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("bdo", id, baseClass), content)

    fun command(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("command", id, baseClass), content)
}
