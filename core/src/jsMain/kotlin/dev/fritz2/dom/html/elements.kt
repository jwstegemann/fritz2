package dev.fritz2.dom.html

import dev.fritz2.binding.WithJob
import dev.fritz2.binding.mountSingle
import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.WithText
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.*


/**
 * Exposes the JavaScript [HTMLAnchorElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAnchorElement) to Kotlin
 */
open class A(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLAnchorElement>("a", id, baseClass, job),
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
open class Area(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLAreaElement>("area", id, baseClass, job),
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
open class Br(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLBRElement>("br", id, baseClass, job)


/**
 * Exposes the JavaScript [HTMLButtonElement](https://developer.mozilla.org/en/docs/Web/API/HTMLButtonElement) to Kotlin
 */
open class Button(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLButtonElement>("button", id, baseClass, job),
    WithText<HTMLButtonElement> {
    fun autofocus(value: Boolean, trueValue: String = "") = attr("autofocus", value, trueValue)
	fun autofocus(value: Flow<Boolean>, trueValue: String = "") = attr("autofocus", value, trueValue)

    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun formAction(value: String) = attr("formAction", value)
	fun formAction(value: Flow<String>) = attr("formAction", value)

    fun formEnctype(value: String) = attr("formEnctype", value)
	fun formEnctype(value: Flow<String>) = attr("formEnctype", value)

    fun formMethod(value: String) = attr("formMethod", value)
	fun formMethod(value: Flow<String>) = attr("formMethod", value)

    fun formNoValidate(value: Boolean, trueValue: String = "") = attr("formNoValidate", value, trueValue)
	fun formNoValidate(value: Flow<Boolean>, trueValue: String = "") = attr("formNoValidate", value, trueValue)

    fun formTarget(value: String) = attr("formTarget", value)
	fun formTarget(value: Flow<String>) = attr("formTarget", value)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLCanvasElement](https://developer.mozilla.org/en/docs/Web/API/HTMLCanvasElement) to Kotlin
 */
open class Canvas(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLCanvasElement>("canvas", id, baseClass, job),
    WithText<HTMLCanvasElement> {
    fun width(value: Int) = attr("width", value)
	fun width(value: Flow<Int>) = attr("width", value)

    fun height(value: Int) = attr("height", value)
	fun height(value: Flow<Int>) = attr("height", value)

}


/**
 * Exposes the JavaScript [HTMLDListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDListElement) to Kotlin
 */
open class Dl(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLDListElement>("dl", id, baseClass, job),
    WithText<HTMLDListElement>


/**
 * Exposes the JavaScript [HTMLDataElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataElement) to Kotlin
 */
open class Data(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLDataElement>("data", id, baseClass, job),
    WithText<HTMLDataElement> {
    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLDataListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataListElement) to Kotlin
 */
open class DataList(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLDataListElement>("datalist", id, baseClass, job),
    WithText<HTMLDataListElement>


/**
 * Exposes the JavaScript [HTMLDetailsElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDetailsElement) to Kotlin
 */
open class Details(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLDetailsElement>("details", id, baseClass, job),
    WithText<HTMLDetailsElement> {
    fun open(value: Boolean, trueValue: String = "") = attr("open", value, trueValue)
	fun open(value: Flow<Boolean>, trueValue: String = "") = attr("open", value, trueValue)

}


/**
 * Exposes the JavaScript [HTMLDialogElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDialogElement) to Kotlin
 */
open class Dialog(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLDialogElement>("dialog", id, baseClass, job),
    WithText<HTMLDialogElement> {
    fun open(value: Boolean, trueValue: String = "") = attr("open", value, trueValue)
	fun open(value: Flow<Boolean>, trueValue: String = "") = attr("open", value, trueValue)

    fun returnValue(value: String) = attr("returnValue", value)
	fun returnValue(value: Flow<String>) = attr("returnValue", value)

}


/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
open class Div(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLDivElement>("div", id, baseClass, job),
    WithText<HTMLDivElement> {
    fun align(value: String) = attr("align", value)
	fun align(value: Flow<String>) = attr("align", value)

}


/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
open class Embed(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLEmbedElement>("embed", id, baseClass, job) {
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
open class FieldSet(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLFieldSetElement>("fieldSet", id, baseClass, job) {
    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

}


/**
 * Exposes the JavaScript [HTMLFormElement](https://developer.mozilla.org/en/docs/Web/API/`for`mElement) to Kotlin
 */
open class Form(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLFormElement>("form", id, baseClass, job),
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

    fun noValidate(value: Boolean, trueValue: String = "") = attr("noValidate", value, trueValue)
	fun noValidate(value: Flow<Boolean>, trueValue: String = "") = attr("noValidate", value, trueValue)

    fun target(value: String) = attr("target", value)
	fun target(value: Flow<String>) = attr("target", value)

}


/**
 * Exposes the JavaScript [HTMLHRElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHRElement) to Kotlin
 */
open class Hr(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLHRElement>("hr", id, baseClass, job)


/**
 * Exposes the JavaScript [HTMLHeadingElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHeadingElement) to Kotlin
 */
open class H(num: Int, id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLHeadingElement>("h$num", id, baseClass, job),
    WithText<HTMLHeadingElement>


/**
 * Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin
 */
open class IFrame(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLIFrameElement>("iframe", id, baseClass, job),
    WithText<HTMLIFrameElement> {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun srcdoc(value: String) = attr("srcdoc", value)
	fun srcdoc(value: Flow<String>) = attr("srcdoc", value)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun allowFullscreen(value: Boolean, trueValue: String = "") = attr("allowFullscreen", value, trueValue)
	fun allowFullscreen(value: Flow<Boolean>, trueValue: String = "") = attr("allowFullscreen", value, trueValue)

    fun allowUserMedia(value: Boolean, trueValue: String = "") = attr("allowUserMedia", value, trueValue)
	fun allowUserMedia(value: Flow<Boolean>, trueValue: String = "") = attr("allowUserMedia", value, trueValue)

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
open class Img(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLImageElement>("img", id, baseClass, job),
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

    fun isMap(value: Boolean, trueValue: String = "") = attr("isMap", value, trueValue)
	fun isMap(value: Flow<Boolean>, trueValue: String = "") = attr("isMap", value, trueValue)

    fun width(value: Int) = attr("width", value)
	fun width(value: Flow<Int>) = attr("width", value)

    fun height(value: Int) = attr("height", value)
	fun height(value: Flow<Int>) = attr("height", value)

    fun referrerPolicy(value: String) = attr("referrerPolicy", value)
	fun referrerPolicy(value: Flow<String>) = attr("referrerPolicy", value)

}


/**
 * Exposes the JavaScript [HTMLInputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLInputElement) to Kotlin
 */
open class Input(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLInputElement>("input", id, baseClass, job) {
    fun accept(value: String) = attr("accept", value)
	fun accept(value: Flow<String>) = attr("accept", value)

    fun alt(value: String) = attr("alt", value)
	fun alt(value: Flow<String>) = attr("alt", value)

    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    fun autofocus(value: Boolean, trueValue: String = "") = attr("autofocus", value, trueValue)
	fun autofocus(value: Flow<Boolean>, trueValue: String = "") = attr("autofocus", value, trueValue)

    fun defaultChecked(value: Boolean, trueValue: String = "") = attr("defaultChecked", value, trueValue)
	fun defaultChecked(value: Flow<Boolean>, trueValue: String = "") = attr("defaultChecked", value, trueValue)

    fun checked(value: Boolean, trueValue: String = "") {
        domNode.checked = value
        domNode.defaultChecked = value
        if(value) domNode.setAttribute("checked", trueValue)
        else domNode.removeAttribute("checked")
    }
    fun checked(value: Flow<Boolean>, trueValue: String = "") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> checked(v, trueValue) }
    }

    fun dirName(value: String) = attr("dirName", value)
	fun dirName(value: Flow<String>) = attr("dirName", value)

    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun formAction(value: String) = attr("formAction", value)
	fun formAction(value: Flow<String>) = attr("formAction", value)

    fun formEnctype(value: String) = attr("formEnctype", value)
	fun formEnctype(value: Flow<String>) = attr("formEnctype", value)

    fun formMethod(value: String) = attr("formMethod", value)
	fun formMethod(value: Flow<String>) = attr("formMethod", value)

    fun formNoValidate(value: Boolean, trueValue: String = "") = attr("formNoValidate", value, trueValue)
	fun formNoValidate(value: Flow<Boolean>, trueValue: String = "") = attr("formNoValidate", value, trueValue)

    fun formTarget(value: String) = attr("formTarget", value)
	fun formTarget(value: Flow<String>) = attr("formTarget", value)

    fun height(value: Int) = attr("height", value)
	fun height(value: Flow<Int>) = attr("height", value)

    fun indeterminate(value: Boolean, trueValue: String = "") = attr("indeterminate", value, trueValue)
	fun indeterminate(value: Flow<Boolean>, trueValue: String = "") = attr("indeterminate", value, trueValue)

    fun inputMode(value: String) = attr("inputMode", value)
	fun inputMode(value: Flow<String>) = attr("inputMode", value)

    fun max(value: String) = attr("max", value)
	fun max(value: Flow<String>) = attr("max", value)

    fun maxLength(value: Int) = attr("maxLength", value)
	fun maxLength(value: Flow<Int>) = attr("maxLength", value)

    fun min(value: String) = attr("min", value)
	fun min(value: Flow<String>) = attr("min", value)

    fun minLength(value: Int) = attr("minLength", value)
	fun minLength(value: Flow<Int>) = attr("minLength", value)

    fun multiple(value: Boolean, trueValue: String = "") = attr("multiple", value, trueValue)
	fun multiple(value: Flow<Boolean>, trueValue: String = "") = attr("multiple", value, trueValue)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun pattern(value: String) = attr("pattern", value)
	fun pattern(value: Flow<String>) = attr("pattern", value)

    fun placeholder(value: String) = attr("placeholder", value)
	fun placeholder(value: Flow<String>) = attr("placeholder", value)

    fun readOnly(value: Boolean, trueValue: String = "") = attr("readOnly", value, trueValue)
	fun readOnly(value: Flow<Boolean>, trueValue: String = "") = attr("readOnly", value, trueValue)

    fun required(value: Boolean, trueValue: String = "") = attr("required", value, trueValue)
	fun required(value: Flow<Boolean>, trueValue: String = "") = attr("required", value, trueValue)

    fun size(value: Int) = attr("size", value)
	fun size(value: Flow<Int>) = attr("size", value)

    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun step(value: String) = attr("step", value)
	fun step(value: Flow<String>) = attr("step", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun defaultValue(value: String) = attr("defaultValue", value)
	fun defaultValue(value: Flow<String>) = attr("defaultValue", value)

    fun value(value: String) {
        domNode.value = value
        domNode.defaultValue = value
        domNode.setAttribute("value", value)
    }
    fun value(value: Flow<String>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> value(v) }
    }

    fun width(value: Int) = attr("width", value)
	fun width(value: Flow<Int>) = attr("width", value)

}


/**
 * Exposes the JavaScript [HTMLLIElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLIElement) to Kotlin
 */
open class Li(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLLIElement>("li", id, baseClass, job),
    WithText<HTMLLIElement> {
    fun value(value: Int) = attr("value", value)
	fun value(value: Flow<Int>) = attr("value", value)

}


/**
 * Exposes the JavaScript [HTMLLabelElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLabelElement) to Kotlin
 */
open class Label(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLLabelElement>("label", id, baseClass, job),
    WithText<HTMLLabelElement> {
    fun `for`(value: String) = attr("`for`", value)
	fun `for`(value: Flow<String>) = attr("`for`", value)

}


/**
 * Exposes the JavaScript [HTMLLegendElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLegendElement) to Kotlin
 */
open class Legend(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLLegendElement>("legend", id, baseClass, job),
    WithText<HTMLLegendElement>


/**
 * Exposes the JavaScript [HTMLMapElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMapElement) to Kotlin
 */
open class Map(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLMapElement>("map", id, baseClass, job),
    WithText<HTMLMapElement> {
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

}


/**
 * Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin
 */
open class Audio(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLAudioElement>("audio", id, baseClass, job),
    WithText<HTMLAudioElement> {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun preload(value: String) = attr("preload", value)
	fun preload(value: Flow<String>) = attr("preload", value)

    fun currentTime(value: Double) = attr("currentTime", value)
	fun currentTime(value: Flow<Double>) = attr("currentTime", value)

    fun defaultPlaybackRate(value: Double) = attr("defaultPlaybackRate", value)
	fun defaultPlaybackRate(value: Flow<Double>) = attr("defaultPlaybackRate", value)

    fun playbackRate(value: Double) {
        domNode.playbackRate = value
        domNode.defaultPlaybackRate = value
        domNode.setAttribute("playbackRate", value.toString())
    }
    fun playbackRate(value: Flow<Double>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> playbackRate(v) }
    }

    fun autoplay(value: Boolean, trueValue: String = "") = attr("autoplay", value, trueValue)
	fun autoplay(value: Flow<Boolean>, trueValue: String = "") = attr("autoplay", value, trueValue)

    fun loop(value: Boolean, trueValue: String = "") = attr("loop", value, trueValue)
	fun loop(value: Flow<Boolean>, trueValue: String = "") = attr("loop", value, trueValue)

    fun controls(value: Boolean, trueValue: String = "") = attr("controls", value, trueValue)
	fun controls(value: Flow<Boolean>, trueValue: String = "") = attr("controls", value, trueValue)

    fun volume(value: Double) = attr("volume", value)
	fun volume(value: Flow<Double>) = attr("volume", value)

    fun defaultMuted(value: Boolean, trueValue: String = "") = attr("defaultMuted", value, trueValue)
	fun defaultMuted(value: Flow<Boolean>, trueValue: String = "") = attr("defaultMuted", value, trueValue)

    fun muted(value: Boolean, trueValue: String = "") {
        domNode.muted = value
        domNode.defaultMuted = value
        if(value) domNode.setAttribute("muted", trueValue)
        else domNode.removeAttribute("muted")
    }
    fun muted(value: Flow<Boolean>, trueValue: String = "") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> muted(v, trueValue) }
    }
}


/**
 * Exposes the JavaScript [HTMLVideoElement](https://developer.mozilla.org/en/docs/Web/API/HTMLVideoElement) to Kotlin
 */
open class Video(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLVideoElement>("video", id, baseClass, job),
    WithText<HTMLVideoElement> {
    fun width(value: Int) = attr("width", value)
	fun width(value: Flow<Int>) = attr("width", value)

    fun height(value: Int) = attr("height", value)
	fun height(value: Flow<Int>) = attr("height", value)

    fun poster(value: String) = attr("poster", value)
	fun poster(value: Flow<String>) = attr("poster", value)

    fun playsInline(value: Boolean, trueValue: String = "") = attr("playsInline", value, trueValue)
	fun playsInline(value: Flow<Boolean>, trueValue: String = "") = attr("playsInline", value, trueValue)

    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun preload(value: String) = attr("preload", value)
	fun preload(value: Flow<String>) = attr("preload", value)

    fun currentTime(value: Double) = attr("currentTime", value)
	fun currentTime(value: Flow<Double>) = attr("currentTime", value)

    fun defaultPlaybackRate(value: Double) = attr("defaultPlaybackRate", value)
	fun defaultPlaybackRate(value: Flow<Double>) = attr("defaultPlaybackRate", value)

    fun playbackRate(value: Double) {
        domNode.playbackRate = value
        domNode.defaultPlaybackRate = value
        domNode.setAttribute("playbackRate", value.toString())
    }
    fun playbackRate(value: Flow<Double>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> playbackRate(v) }
    }

    fun autoplay(value: Boolean, trueValue: String = "") = attr("autoplay", value, trueValue)
	fun autoplay(value: Flow<Boolean>, trueValue: String = "") = attr("autoplay", value, trueValue)

    fun loop(value: Boolean, trueValue: String = "") = attr("loop", value, trueValue)
	fun loop(value: Flow<Boolean>, trueValue: String = "") = attr("loop", value, trueValue)

    fun controls(value: Boolean, trueValue: String = "") = attr("controls", value, trueValue)
	fun controls(value: Flow<Boolean>, trueValue: String = "") = attr("controls", value, trueValue)

    fun volume(value: Double) = attr("volume", value)
	fun volume(value: Flow<Double>) = attr("volume", value)

    fun defaultMuted(value: Boolean, trueValue: String = "") = attr("defaultMuted", value, trueValue)
	fun defaultMuted(value: Flow<Boolean>, trueValue: String = "") = attr("defaultMuted", value, trueValue)

    fun muted(value: Boolean, trueValue: String = "") {
        domNode.muted = value
        domNode.defaultMuted = value
        if(value) domNode.setAttribute("muted", trueValue)
        else domNode.removeAttribute("muted")
    }
    fun muted(value: Flow<Boolean>, trueValue: String = "") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> muted(v, trueValue) }
    }
}


/**
 * Exposes the JavaScript [HTMLMeterElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMeterElement) to Kotlin
 */
open class Meter(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLMeterElement>("meter", id, baseClass, job),
    WithText<HTMLMeterElement> {
    fun value(value: Double) = attr("value", value)
	fun value(value: Flow<Double>) = attr("value", value)

    fun min(value: Double) = attr("min", value)
	fun min(value: Flow<Double>) = attr("min", value)

    fun max(value: Double) = attr("max", value)
	fun max(value: Flow<Double>) = attr("max", value)

    fun low(value: Double) = attr("low", value)
	fun low(value: Flow<Double>) = attr("low", value)

    fun high(value: Double) = attr("high", value)
	fun high(value: Flow<Double>) = attr("high", value)

    fun optimum(value: Double) = attr("optimum", value)
	fun optimum(value: Flow<Double>) = attr("optimum", value)

}


/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
open class Ins(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLModElement>("ins", id, baseClass, job),
    WithText<HTMLModElement> {
    fun cite(value: String) = attr("cite", value)
	fun cite(value: Flow<String>) = attr("cite", value)

    fun dateTime(value: String) = attr("dateTime", value)
	fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
open class Del(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLModElement>("del", id, baseClass, job),
    WithText<HTMLModElement> {
    fun cite(value: String) = attr("cite", value)
	fun cite(value: Flow<String>) = attr("cite", value)

    fun dateTime(value: String) = attr("dateTime", value)
	fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLOListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOListElement) to Kotlin
 */
open class Ol(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLOListElement>("ol", id, baseClass, job),
    WithText<HTMLOListElement> {
    fun reversed(value: Boolean, trueValue: String = "") = attr("reversed", value, trueValue)
	fun reversed(value: Flow<Boolean>, trueValue: String = "") = attr("reversed", value, trueValue)

    fun start(value: Int) = attr("start", value)
	fun start(value: Flow<Int>) = attr("start", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

}


/**
 * Exposes the JavaScript [HTMLObjectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLObjectElement) to Kotlin
 */
open class Object(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLObjectElement>("object", id, baseClass, job),
    WithText<HTMLObjectElement> {
    fun data(value: String) = attr("data", value)
	fun data(value: Flow<String>) = attr("data", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun typeMustMatch(value: Boolean, trueValue: String = "") = attr("typeMustMatch", value, trueValue)
	fun typeMustMatch(value: Flow<Boolean>, trueValue: String = "") = attr("typeMustMatch", value, trueValue)

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
open class Optgroup(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLOptGroupElement>("optgroup", id, baseClass, job),
    WithText<HTMLOptGroupElement> {
    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun label(value: String) = attr("label", value)
	fun label(value: Flow<String>) = attr("label", value)

}


/**
 * Exposes the JavaScript [HTMLOptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptionElement) to Kotlin
 */
open class Option(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLOptionElement>("option", id, baseClass, job),
    WithText<HTMLOptionElement> {
    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun label(value: String) = attr("label", value)
	fun label(value: Flow<String>) = attr("label", value)

    fun defaultSelected(value: Boolean, trueValue: String = "") = attr("defaultSelected", value, trueValue)
	fun defaultSelected(value: Flow<Boolean>, trueValue: String = "") = attr("defaultSelected", value, trueValue)

    fun selected(value: Boolean, trueValue: String = "") {
        domNode.selected = value
        domNode.defaultSelected = value
        if(value) domNode.setAttribute("selected", trueValue)
        else domNode.removeAttribute("selected")
    }
    fun selected(value: Flow<Boolean>, trueValue: String = "") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> selected(v, trueValue) }
    }

    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLOutputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOutputElement) to Kotlin
 */
open class Output(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLOutputElement>("output", id, baseClass, job),
    WithText<HTMLOutputElement> {
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun defaultValue(value: String) = attr("defaultValue", value)
	fun defaultValue(value: Flow<String>) = attr("defaultValue", value)

    fun value(value: String) {
        domNode.value = value
        domNode.defaultValue = value
        domNode.setAttribute("value", value)
    }
    fun value(value: Flow<String>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> value(v) }
    }
}


/**
 * Exposes the JavaScript [HTMLParagraphElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParagraphElement) to Kotlin
 */
open class P(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLParagraphElement>("p", id, baseClass, job),
    WithText<HTMLParagraphElement>


/**
 * Exposes the JavaScript [HTMLParamElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParamElement) to Kotlin
 */
open class Param(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLParamElement>("param", id, baseClass, job),
    WithText<HTMLParamElement> {
    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLPictureElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPictureElement) to Kotlin
 */
open class Picture(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLPictureElement>("picture", id, baseClass, job),
    WithText<HTMLPictureElement>


/**
 * Exposes the JavaScript [HTMLPreElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPreElement) to Kotlin
 */
open class Pre(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLPreElement>("pre", id, baseClass, job),
    WithText<HTMLPreElement>


/**
 * Exposes the JavaScript [HTMLProgressElement](https://developer.mozilla.org/en/docs/Web/API/HTMLProgressElement) to Kotlin
 */
open class Progress(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLProgressElement>("progress", id, baseClass, job),
    WithText<HTMLProgressElement> {
    fun value(value: Double) = attr("value", value)
	fun value(value: Flow<Double>) = attr("value", value)

    fun max(value: Double) = attr("max", value)
	fun max(value: Flow<Double>) = attr("max", value)

}


/**
 * Exposes the JavaScript [HTMLQuoteElement](https://developer.mozilla.org/en/docs/Web/API/HTMLQuoteElement) to Kotlin
 */
open class Quote(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLQuoteElement>("quote", id, baseClass, job),
    WithText<HTMLQuoteElement> {
    fun cite(value: String) = attr("cite", value)
	fun cite(value: Flow<String>) = attr("cite", value)

}


/**
 * Exposes the JavaScript [HTMLScriptElement](https://developer.mozilla.org/en/docs/Web/API/HTMLScriptElement) to Kotlin
 */
open class Script(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLScriptElement>("script", id, baseClass, job),
    WithText<HTMLScriptElement> {
    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun type(value: String) = attr("type", value)
	fun type(value: Flow<String>) = attr("type", value)

    fun charset(value: String) = attr("charset", value)
	fun charset(value: Flow<String>) = attr("charset", value)

    fun async(value: Boolean, trueValue: String = "") = attr("async", value, trueValue)
	fun async(value: Flow<Boolean>, trueValue: String = "") = attr("async", value, trueValue)

    fun defer(value: Boolean, trueValue: String = "") = attr("defer", value, trueValue)
	fun defer(value: Flow<Boolean>, trueValue: String = "") = attr("defer", value, trueValue)


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
open class Select(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLSelectElement>("select", id, baseClass, job) {
    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    fun autofocus(value: Boolean, trueValue: String = "") = attr("autofocus", value, trueValue)
	fun autofocus(value: Flow<Boolean>, trueValue: String = "") = attr("autofocus", value, trueValue)

    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun multiple(value: Boolean, trueValue: String = "") = attr("multiple", value, trueValue)
	fun multiple(value: Flow<Boolean>, trueValue: String = "") = attr("multiple", value, trueValue)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun required(value: Boolean, trueValue: String = "") = attr("required", value, trueValue)
	fun required(value: Flow<Boolean>, trueValue: String = "") = attr("required", value, trueValue)

    fun size(value: Int) = attr("size", value)
	fun size(value: Flow<Int>) = attr("size", value)

    fun length(value: Int) = attr("length", value)
	fun length(value: Flow<Int>) = attr("length", value)

    fun selectedIndex(value: Int) = attr("selectedIndex", value)
	fun selectedIndex(value: Flow<Int>) = attr("selectedIndex", value)

    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLSpanElement](https://developer.mozilla.org/en/docs/Web/API/HTMLSpanElement) to Kotlin
 */
open class Span(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLSpanElement>("span", id, baseClass, job),
    WithText<HTMLSpanElement>


/**
 * Exposes the JavaScript [HTMLTableCaptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCaptionElement) to Kotlin
 */
open class Caption(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableCaptionElement>("caption", id, baseClass, job),
    WithText<HTMLTableCaptionElement>


/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
open class Th(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableCellElement>("th", id, baseClass, job),
    WithText<HTMLTableCellElement> {
    fun colSpan(value: Int) = attr("colSpan", value)
	fun colSpan(value: Flow<Int>) = attr("colSpan", value)

    fun rowSpan(value: Int) = attr("rowSpan", value)
	fun rowSpan(value: Flow<Int>) = attr("rowSpan", value)

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
open class Td(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableCellElement>("td", id, baseClass, job),
    WithText<HTMLTableCellElement> {
    fun colSpan(value: Int) = attr("colSpan", value)
	fun colSpan(value: Flow<Int>) = attr("colSpan", value)

    fun rowSpan(value: Int) = attr("rowSpan", value)
	fun rowSpan(value: Flow<Int>) = attr("rowSpan", value)

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
open class Col(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableColElement>("col", id, baseClass, job),
    WithText<HTMLTableColElement> {
    fun span(value: Int) = attr("span", value)
	fun span(value: Flow<Int>) = attr("span", value)

}


/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
open class Colgroup(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableColElement>("colgroup", id, baseClass, job),
    WithText<HTMLTableColElement> {
    fun span(value: Int) = attr("span", value)
	fun span(value: Flow<Int>) = attr("span", value)

}


/**
 * Exposes the JavaScript [HTMLTableElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableElement) to Kotlin
 */
open class Table(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableElement>("table", id, baseClass, job)


/**
 * Exposes the JavaScript [HTMLTableRowElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableRowElement) to Kotlin
 */
open class Tr(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableRowElement>("tr", id, baseClass, job),
    WithText<HTMLTableRowElement>


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class TFoot(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableSectionElement>("tfoot", id, baseClass, job)


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class THead(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableSectionElement>("thead", id, baseClass, job)


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class TBody(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTableSectionElement>("tbody", id, baseClass, job)


/**
 * Exposes the JavaScript [HTMLTextAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTextAreaElement) to Kotlin
 */
open class TextArea(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTextAreaElement>("textarea", id, baseClass, job),
    WithText<HTMLTextAreaElement> {
    fun autocomplete(value: String) = attr("autocomplete", value)
	fun autocomplete(value: Flow<String>) = attr("autocomplete", value)

    fun autofocus(value: Boolean, trueValue: String = "") = attr("autofocus", value, trueValue)
	fun autofocus(value: Flow<Boolean>, trueValue: String = "") = attr("autofocus", value, trueValue)

    fun cols(value: Int) = attr("cols", value)
	fun cols(value: Flow<Int>) = attr("cols", value)

    fun dirName(value: String) = attr("dirName", value)
	fun dirName(value: Flow<String>) = attr("dirName", value)

    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
	fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun inputMode(value: String) = attr("inputMode", value)
	fun inputMode(value: Flow<String>) = attr("inputMode", value)

    fun maxLength(value: Int) = attr("maxLength", value)
	fun maxLength(value: Flow<Int>) = attr("maxLength", value)

    fun minLength(value: Int) = attr("minLength", value)
	fun minLength(value: Flow<Int>) = attr("minLength", value)

    fun name(value: String) = attr("name", value)
	fun name(value: Flow<String>) = attr("name", value)

    fun placeholder(value: String) = attr("placeholder", value)
	fun placeholder(value: Flow<String>) = attr("placeholder", value)

    fun readOnly(value: Boolean, trueValue: String = "") = attr("readOnly", value, trueValue)
	fun readOnly(value: Flow<Boolean>, trueValue: String = "") = attr("readOnly", value, trueValue)

    fun required(value: Boolean, trueValue: String = "") = attr("required", value, trueValue)
	fun required(value: Flow<Boolean>, trueValue: String = "") = attr("required", value, trueValue)

    fun rows(value: Int) = attr("rows", value)
	fun rows(value: Flow<Int>) = attr("rows", value)

    fun wrap(value: String) = attr("wrap", value)
	fun wrap(value: Flow<String>) = attr("wrap", value)

    fun defaultValue(value: String) = attr("defaultValue", value)
	fun defaultValue(value: Flow<String>) = attr("defaultValue", value)

    fun value(value: String) {
        domNode.value = value
        domNode.defaultValue = value
        domNode.setAttribute("value", value)
    }
    fun value(value: Flow<String>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> value(v) }
    }
}


/**
 * Exposes the JavaScript [HTMLTimeElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTimeElement) to Kotlin
 */
open class Time(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTimeElement>("time", id, baseClass, job),
    WithText<HTMLTimeElement> {
    fun dateTime(value: String) = attr("dateTime", value)
	fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLTrackElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTrackElement) to Kotlin
 */
open class Track(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLTrackElement>("track", id, baseClass, job),
    WithText<HTMLTrackElement> {
    fun kind(value: String) = attr("kind", value)
	fun kind(value: Flow<String>) = attr("kind", value)

    fun src(value: String) = attr("src", value)
	fun src(value: Flow<String>) = attr("src", value)

    fun srclang(value: String) = attr("srclang", value)
	fun srclang(value: Flow<String>) = attr("srclang", value)

    fun label(value: String) = attr("label", value)
	fun label(value: Flow<String>) = attr("label", value)

    fun default(value: Boolean, trueValue: String = "") = attr("default", value, trueValue)
	fun default(value: Flow<Boolean>, trueValue: String = "") = attr("default", value, trueValue)

}


/**
 * Exposes the JavaScript [HTMLUListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLUListElement) to Kotlin
 */
open class Ul(id: String? = null, baseClass: String? = null, job: Job) : Tag<HTMLUListElement>("ul", id, baseClass, job)


/**
 * Special [Tag] for html5 with no attributes
 */
open class TextElement(tagName: String, id: String? = null, baseClass: String? = null, job: Job) :
    Tag<HTMLElement>(tagName, id, baseClass, job), WithText<HTMLElement>


/**
 * Context for rendering dynamically DOM-nodes to your page.
 */
interface RenderContext : WithJob {

    fun custom(tagName: String, content: Tag<HTMLElement>.() -> Unit): Tag<HTMLElement> =
        register(Tag(tagName, job = job), content)

    fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T

    fun a(baseClass: String? = null, id: String? = null, content: A.() -> Unit): A =
        register(A(id, baseClass, job), content)

    fun area(baseClass: String? = null, id: String? = null, content: Area.() -> Unit): Area =
        register(Area(id, baseClass, job), content)

    fun br(baseClass: String? = null, id: String? = null, content: Br.() -> Unit): Br =
        register(Br(id, baseClass, job), content)

    fun button(baseClass: String? = null, id: String? = null, content: Button.() -> Unit): Button =
        register(Button(id, baseClass, job), content)

    fun canvas(baseClass: String? = null, id: String? = null, content: Canvas.() -> Unit): Canvas =
        register(Canvas(id, baseClass, job), content)

    fun dl(baseClass: String? = null, id: String? = null, content: Dl.() -> Unit): Dl =
        register(Dl(id, baseClass, job), content)

    fun dt(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dt", id, baseClass, job), content)

    fun dd(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dd", id, baseClass, job), content)

    fun data(baseClass: String? = null, id: String? = null, content: Data.() -> Unit): Data =
        register(Data(id, baseClass, job), content)

    fun datalist(baseClass: String? = null, id: String? = null, content: DataList.() -> Unit): DataList =
        register(DataList(id, baseClass, job), content)

    fun details(baseClass: String? = null, id: String? = null, content: Details.() -> Unit): Details =
        register(Details(id, baseClass, job), content)

    fun dialog(baseClass: String? = null, id: String? = null, content: Dialog.() -> Unit): Dialog =
        register(Dialog(id, baseClass, job), content)

    fun div(baseClass: String? = null, id: String? = null, content: Div.() -> Unit): Div =
        register(Div(id, baseClass, job), content)

    fun embed(baseClass: String? = null, id: String? = null, content: Embed.() -> Unit): Embed =
        register(Embed(id, baseClass, job), content)

    fun fieldset(baseClass: String? = null, id: String? = null, content: FieldSet.() -> Unit): FieldSet =
        register(FieldSet(id, baseClass, job), content)

    fun form(baseClass: String? = null, id: String? = null, content: Form.() -> Unit): Form =
        register(Form(id, baseClass, job), content)

    fun hr(baseClass: String? = null, id: String? = null, content: Hr.() -> Unit): Hr =
        register(Hr(id, baseClass, job), content)

    fun h1(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(1, id, baseClass, job), content)

    fun h2(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(2, id, baseClass, job), content)

    fun h3(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(3, id, baseClass, job), content)

    fun h4(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(4, id, baseClass, job), content)

    fun h5(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(5, id, baseClass, job), content)

    fun h6(baseClass: String? = null, id: String? = null, content: H.() -> Unit): H =
        register(H(6, id, baseClass, job), content)

    fun iframe(baseClass: String? = null, id: String? = null, content: IFrame.() -> Unit): IFrame =
        register(IFrame(id, baseClass, job), content)

    fun img(baseClass: String? = null, id: String? = null, content: Img.() -> Unit): Img =
        register(Img(id, baseClass, job), content)

    fun input(baseClass: String? = null, id: String? = null, content: Input.() -> Unit): Input =
        register(Input(id, baseClass, job), content)

    fun li(baseClass: String? = null, id: String? = null, content: Li.() -> Unit): Li =
        register(Li(id, baseClass, job), content)

    fun label(baseClass: String? = null, id: String? = null, content: Label.() -> Unit): Label =
        register(Label(id, baseClass, job), content)

    fun legend(baseClass: String? = null, id: String? = null, content: Legend.() -> Unit): Legend =
        register(Legend(id, baseClass, job), content)

    fun map(baseClass: String? = null, id: String? = null, content: Map.() -> Unit): Map =
        register(Map(id, baseClass, job), content)

    fun audio(baseClass: String? = null, id: String? = null, content: Audio.() -> Unit): Audio =
        register(Audio(id, baseClass, job), content)

    fun video(baseClass: String? = null, id: String? = null, content: Video.() -> Unit): Video =
        register(Video(id, baseClass, job), content)

    fun meter(baseClass: String? = null, id: String? = null, content: Meter.() -> Unit): Meter =
        register(Meter(id, baseClass, job), content)

    fun ins(baseClass: String? = null, id: String? = null, content: Ins.() -> Unit): Ins =
        register(Ins(id, baseClass, job), content)

    fun del(baseClass: String? = null, id: String? = null, content: Del.() -> Unit): Del =
        register(Del(id, baseClass, job), content)

    fun ol(baseClass: String? = null, id: String? = null, content: Ol.() -> Unit): Ol =
        register(Ol(id, baseClass, job), content)

    fun `object`(baseClass: String? = null, id: String? = null, content: Object.() -> Unit): Object =
        register(Object(id, baseClass, job), content)

    fun optgroup(baseClass: String? = null, id: String? = null, content: Optgroup.() -> Unit): Optgroup =
        register(Optgroup(id, baseClass, job), content)

    fun option(baseClass: String? = null, id: String? = null, content: Option.() -> Unit): Option =
        register(Option(id, baseClass, job), content)

    fun output(baseClass: String? = null, id: String? = null, content: Output.() -> Unit): Output =
        register(Output(id, baseClass, job), content)

    fun p(baseClass: String? = null, id: String? = null, content: P.() -> Unit): P =
        register(P(id, baseClass, job), content)

    fun param(baseClass: String? = null, id: String? = null, content: Param.() -> Unit): Param =
        register(Param(id, baseClass, job), content)

    fun picture(baseClass: String? = null, id: String? = null, content: Picture.() -> Unit): Picture =
        register(Picture(id, baseClass, job), content)

    fun pre(baseClass: String? = null, id: String? = null, content: Pre.() -> Unit): Pre =
        register(Pre(id, baseClass, job), content)

    fun progress(baseClass: String? = null, id: String? = null, content: Progress.() -> Unit): Progress =
        register(Progress(id, baseClass, job), content)

    fun quote(baseClass: String? = null, id: String? = null, content: Quote.() -> Unit): Quote =
        register(Quote(id, baseClass, job), content)

    fun script(baseClass: String? = null, id: String? = null, content: Script.() -> Unit): Script =
        register(Script(id, baseClass, job), content)

    fun select(baseClass: String? = null, id: String? = null, content: Select.() -> Unit): Select =
        register(Select(id, baseClass, job), content)

    fun span(baseClass: String? = null, id: String? = null, content: Span.() -> Unit): Span =
        register(Span(id, baseClass, job), content)

    fun caption(baseClass: String? = null, id: String? = null, content: Caption.() -> Unit): Caption =
        register(Caption(id, baseClass, job), content)

    fun th(baseClass: String? = null, id: String? = null, content: Th.() -> Unit): Th =
        register(Th(id, baseClass, job), content)

    fun td(baseClass: String? = null, id: String? = null, content: Td.() -> Unit): Td =
        register(Td(id, baseClass, job), content)

    fun col(baseClass: String? = null, id: String? = null, content: Col.() -> Unit): Col =
        register(Col(id, baseClass, job), content)

    fun colgroup(baseClass: String? = null, id: String? = null, content: Colgroup.() -> Unit): Colgroup =
        register(Colgroup(id, baseClass, job), content)

    fun table(baseClass: String? = null, id: String? = null, content: Table.() -> Unit): Table =
        register(Table(id, baseClass, job), content)

    fun tr(baseClass: String? = null, id: String? = null, content: Tr.() -> Unit): Tr =
        register(Tr(id, baseClass, job), content)

    fun tfoot(baseClass: String? = null, id: String? = null, content: TFoot.() -> Unit): TFoot =
        register(TFoot(id, baseClass, job), content)

    fun thead(baseClass: String? = null, id: String? = null, content: THead.() -> Unit): THead =
        register(THead(id, baseClass, job), content)

    fun tbody(baseClass: String? = null, id: String? = null, content: TBody.() -> Unit): TBody =
        register(TBody(id, baseClass, job), content)

    fun textarea(baseClass: String? = null, id: String? = null, content: TextArea.() -> Unit): TextArea =
        register(TextArea(id, baseClass, job), content)

    fun time(baseClass: String? = null, id: String? = null, content: Time.() -> Unit): Time =
        register(Time(id, baseClass, job), content)

    fun track(baseClass: String? = null, id: String? = null, content: Track.() -> Unit): Track =
        register(Track(id, baseClass, job), content)

    fun ul(baseClass: String? = null, id: String? = null, content: Ul.() -> Unit): Ul =
        register(Ul(id, baseClass, job), content)

    fun address(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("address", id, baseClass, job), content)

    fun article(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("article", id, baseClass, job), content)

    fun aside(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("aside", id, baseClass, job), content)

    fun bdi(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("bdi", id, baseClass, job), content)

    fun details(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("details", id, baseClass, job), content)

    fun dialog(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dialog", id, baseClass, job), content)

    fun figcaption(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("figcaption", id, baseClass, job), content)

    fun figure(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("figure", id, baseClass, job), content)

    fun footer(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("footer", id, baseClass, job), content)

    fun header(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("header", id, baseClass, job), content)

    fun main(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("main", id, baseClass, job), content)

    fun mark(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("mark", id, baseClass, job), content)

    fun nav(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("nav", id, baseClass, job), content)

    fun noscript(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("noscript", id, baseClass, job), content)

    fun progress(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("progress", id, baseClass, job), content)

    fun rp(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("rp", id, baseClass, job), content)

    fun rt(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("rt", id, baseClass, job), content)

    fun ruby(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("ruby", id, baseClass, job), content)

    fun section(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("section", id, baseClass, job), content)

    fun summary(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("summary", id, baseClass, job), content)

    fun time(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("time", id, baseClass, job), content)

    fun wbr(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("wbr", id, baseClass, job), content)

    fun blockquote(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("blockquote", id, baseClass, job), content)

    fun em(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("em", id, baseClass, job), content)

    fun strong(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("strong", id, baseClass, job), content)

    fun small(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("small", id, baseClass, job), content)

    fun s(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("s", id, baseClass, job), content)

    fun cite(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("cite", id, baseClass, job), content)

    fun q(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("q", id, baseClass, job), content)

    fun dfn(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("dfn", id, baseClass, job), content)

    fun abbr(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("abbr", id, baseClass, job), content)

    fun code(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("code", id, baseClass, job), content)

    fun `var`(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("var", id, baseClass, job), content)

    fun samp(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("samp", id, baseClass, job), content)

    fun kbd(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("kbd", id, baseClass, job), content)

    fun sub(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("sub", id, baseClass, job), content)

    fun sup(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("sup", id, baseClass, job), content)

    fun i(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("i", id, baseClass, job), content)

    fun b(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("b", id, baseClass, job), content)

    fun u(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("u", id, baseClass, job), content)

    fun bdo(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("bdo", id, baseClass, job), content)

    fun command(baseClass: String? = null, id: String? = null, content: TextElement.() -> Unit): TextElement =
        register(TextElement("command", id, baseClass, job), content)
}
