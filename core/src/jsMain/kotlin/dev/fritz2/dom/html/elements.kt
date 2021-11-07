@file:Suppress("unused")

package dev.fritz2.dom.html

import dev.fritz2.binding.*
import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.WithText
import dev.fritz2.dom.mount
import dev.fritz2.lenses.IdProvider
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.*
import org.w3c.dom.svg.SVGElement
import org.w3c.dom.svg.SVGPathElement

/**
 * Exposes the JavaScript [HTMLAnchorElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAnchorElement) to Kotlin
 */
open class A(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLAnchorElement>("a", id, baseClass, job, scope),
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
open class Area(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLAreaElement>("area", id, baseClass, job, scope),
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
open class Br(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLBRElement>("br", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [HTMLButtonElement](https://developer.mozilla.org/en/docs/Web/API/HTMLButtonElement) to Kotlin
 */
open class Button(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLButtonElement>("button", id, baseClass, job, scope),
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
open class Canvas(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLCanvasElement>("canvas", id, baseClass, job, scope),
    WithText<HTMLCanvasElement> {
    fun width(value: Int) = attr("width", value)
    fun width(value: Flow<Int>) = attr("width", value)

    fun height(value: Int) = attr("height", value)
    fun height(value: Flow<Int>) = attr("height", value)

}


/**
 * Exposes the JavaScript [HTMLDListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDListElement) to Kotlin
 */
open class Dl(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLDListElement>("dl", id, baseClass, job, scope),
    WithText<HTMLDListElement>


/**
 * Exposes the JavaScript [HTMLDataElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataElement) to Kotlin
 */
open class Data(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLDataElement>("data", id, baseClass, job, scope),
    WithText<HTMLDataElement> {
    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLDataListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDataListElement) to Kotlin
 */
open class DataList(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLDataListElement>("datalist", id, baseClass, job, scope),
    WithText<HTMLDataListElement>


/**
 * Exposes the JavaScript [HTMLDetailsElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDetailsElement) to Kotlin
 */
open class Details(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLDetailsElement>("details", id, baseClass, job, scope),
    WithText<HTMLDetailsElement> {
    fun open(value: Boolean, trueValue: String = "") = attr("open", value, trueValue)
    fun open(value: Flow<Boolean>, trueValue: String = "") = attr("open", value, trueValue)

}


/**
 * Exposes the JavaScript [HTMLDialogElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDialogElement) to Kotlin
 */
open class Dialog(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLDialogElement>("dialog", id, baseClass, job, scope),
    WithText<HTMLDialogElement> {
    fun open(value: Boolean, trueValue: String = "") = attr("open", value, trueValue)
    fun open(value: Flow<Boolean>, trueValue: String = "") = attr("open", value, trueValue)

    fun returnValue(value: String) = attr("returnValue", value)
    fun returnValue(value: Flow<String>) = attr("returnValue", value)

}


/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
open class Div(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLDivElement>("div", id, baseClass, job, scope),
    WithText<HTMLDivElement> {
    fun align(value: String) = attr("align", value)
    fun align(value: Flow<String>) = attr("align", value)

}


/**
 * Exposes the JavaScript [HTMLDivElement](https://developer.mozilla.org/en/docs/Web/API/HTMLDivElement) to Kotlin
 */
open class Embed(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLEmbedElement>("embed", id, baseClass, job, scope) {
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
open class FieldSet(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLFieldSetElement>("fieldSet", id, baseClass, job, scope) {
    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
    fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun name(value: String) = attr("name", value)
    fun name(value: Flow<String>) = attr("name", value)

}


/**
 * Exposes the JavaScript [HTMLFormElement](https://developer.mozilla.org/en/docs/Web/API/`for`mElement) to Kotlin
 */
open class Form(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLFormElement>("form", id, baseClass, job, scope),
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
open class Hr(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLHRElement>("hr", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [HTMLHeadingElement](https://developer.mozilla.org/en/docs/Web/API/HTMLHeadingElement) to Kotlin
 */
open class H(num: Int, id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLHeadingElement>("h$num", id, baseClass, job, scope),
    WithText<HTMLHeadingElement>


/**
 * Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin
 */
open class IFrame(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLIFrameElement>("iframe", id, baseClass, job, scope),
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
open class Img(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLImageElement>("img", id, baseClass, job, scope),
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
open class Input(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLInputElement>("input", id, baseClass, job, scope) {
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
        if (value) domNode.setAttribute("checked", trueValue)
        else domNode.removeAttribute("checked")
    }

    fun checked(value: Flow<Boolean>, trueValue: String = "") {
        mountSimple(job, value) { v -> checked(v, trueValue) }
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
        mountSimple(job, value) { v -> value(v) }
    }

    fun width(value: Int) = attr("width", value)
    fun width(value: Flow<Int>) = attr("width", value)

}


/**
 * Exposes the JavaScript [HTMLLIElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLIElement) to Kotlin
 */
open class Li(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLLIElement>("li", id, baseClass, job, scope),
    WithText<HTMLLIElement> {
    fun value(value: Int) = attr("value", value)
    fun value(value: Flow<Int>) = attr("value", value)

}


/**
 * Exposes the JavaScript [HTMLLabelElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLabelElement) to Kotlin
 */
open class Label(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLLabelElement>("label", id, baseClass, job, scope),
    WithText<HTMLLabelElement> {
    fun `for`(value: String) = attr("for", value)
    fun `for`(value: Flow<String>) = attr("for", value)

}


/**
 * Exposes the JavaScript [HTMLLegendElement](https://developer.mozilla.org/en/docs/Web/API/HTMLLegendElement) to Kotlin
 */
open class Legend(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLLegendElement>("legend", id, baseClass, job, scope),
    WithText<HTMLLegendElement>


/**
 * Exposes the JavaScript [HTMLMapElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMapElement) to Kotlin
 */
open class Map(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLMapElement>("map", id, baseClass, job, scope),
    WithText<HTMLMapElement> {
    fun name(value: String) = attr("name", value)
    fun name(value: Flow<String>) = attr("name", value)

}


/**
 * Exposes the JavaScript [HTMLAudioElement](https://developer.mozilla.org/en/docs/Web/API/HTMLAudioElement) to Kotlin
 */
open class Audio(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLAudioElement>("audio", id, baseClass, job, scope),
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
        mountSimple(job, value) { v -> playbackRate(v) }
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
        if (value) domNode.setAttribute("muted", trueValue)
        else domNode.removeAttribute("muted")
    }

    fun muted(value: Flow<Boolean>, trueValue: String = "") {
        mountSimple(job, value) { v -> muted(v, trueValue) }
    }
}


/**
 * Exposes the JavaScript [HTMLVideoElement](https://developer.mozilla.org/en/docs/Web/API/HTMLVideoElement) to Kotlin
 */
open class Video(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLVideoElement>("video", id, baseClass, job, scope),
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
        mountSimple(job, value) { v -> playbackRate(v) }
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
        if (value) domNode.setAttribute("muted", trueValue)
        else domNode.removeAttribute("muted")
    }

    fun muted(value: Flow<Boolean>, trueValue: String = "") {
        mountSimple(job, value) { v -> muted(v, trueValue) }
    }
}


/**
 * Exposes the JavaScript [HTMLMeterElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMeterElement) to Kotlin
 */
open class Meter(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLMeterElement>("meter", id, baseClass, job, scope),
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
open class Ins(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLModElement>("ins", id, baseClass, job, scope),
    WithText<HTMLModElement> {
    fun cite(value: String) = attr("cite", value)
    fun cite(value: Flow<String>) = attr("cite", value)

    fun dateTime(value: String) = attr("dateTime", value)
    fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLModElement](https://developer.mozilla.org/en/docs/Web/API/HTMLModElement) to Kotlin
 */
open class Del(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLModElement>("del", id, baseClass, job, scope),
    WithText<HTMLModElement> {
    fun cite(value: String) = attr("cite", value)
    fun cite(value: Flow<String>) = attr("cite", value)

    fun dateTime(value: String) = attr("dateTime", value)
    fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLOListElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOListElement) to Kotlin
 */
open class Ol(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLOListElement>("ol", id, baseClass, job, scope),
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
open class Object(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLObjectElement>("object", id, baseClass, job, scope),
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
open class Optgroup(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLOptGroupElement>("optgroup", id, baseClass, job, scope),
    WithText<HTMLOptGroupElement> {
    fun disabled(value: Boolean, trueValue: String = "") = attr("disabled", value, trueValue)
    fun disabled(value: Flow<Boolean>, trueValue: String = "") = attr("disabled", value, trueValue)

    fun label(value: String) = attr("label", value)
    fun label(value: Flow<String>) = attr("label", value)

}


/**
 * Exposes the JavaScript [HTMLOptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOptionElement) to Kotlin
 */
open class Option(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLOptionElement>("option", id, baseClass, job, scope),
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
        if (value) domNode.setAttribute("selected", trueValue)
        else domNode.removeAttribute("selected")
    }

    fun selected(value: Flow<Boolean>, trueValue: String = "") {
        mountSimple(job, value) { v -> selected(v, trueValue) }
    }

    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLOutputElement](https://developer.mozilla.org/en/docs/Web/API/HTMLOutputElement) to Kotlin
 */
open class Output(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLOutputElement>("output", id, baseClass, job, scope),
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
        mountSimple(job, value) { v -> value(v) }
    }
}


/**
 * Exposes the JavaScript [HTMLParagraphElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParagraphElement) to Kotlin
 */
open class P(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLParagraphElement>("p", id, baseClass, job, scope),
    WithText<HTMLParagraphElement>


/**
 * Exposes the JavaScript [HTMLParamElement](https://developer.mozilla.org/en/docs/Web/API/HTMLParamElement) to Kotlin
 */
open class Param(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLParamElement>("param", id, baseClass, job, scope),
    WithText<HTMLParamElement> {
    fun name(value: String) = attr("name", value)
    fun name(value: Flow<String>) = attr("name", value)

    fun value(value: String) = attr("value", value)
    fun value(value: Flow<String>) = attr("value", value)
}


/**
 * Exposes the JavaScript [HTMLPictureElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPictureElement) to Kotlin
 */
open class Picture(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLPictureElement>("picture", id, baseClass, job, scope),
    WithText<HTMLPictureElement>


/**
 * Exposes the JavaScript [HTMLPreElement](https://developer.mozilla.org/en/docs/Web/API/HTMLPreElement) to Kotlin
 */
open class Pre(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLPreElement>("pre", id, baseClass, job, scope),
    WithText<HTMLPreElement>


/**
 * Exposes the JavaScript [HTMLProgressElement](https://developer.mozilla.org/en/docs/Web/API/HTMLProgressElement) to Kotlin
 */
open class Progress(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLProgressElement>("progress", id, baseClass, job, scope),
    WithText<HTMLProgressElement> {
    fun value(value: Double) = attr("value", value)
    fun value(value: Flow<Double>) = attr("value", value)

    fun max(value: Double) = attr("max", value)
    fun max(value: Flow<Double>) = attr("max", value)

}


/**
 * Exposes the JavaScript [HTMLQuoteElement](https://developer.mozilla.org/en/docs/Web/API/HTMLQuoteElement) to Kotlin
 */
open class Quote(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLQuoteElement>("quote", id, baseClass, job, scope),
    WithText<HTMLQuoteElement> {
    fun cite(value: String) = attr("cite", value)
    fun cite(value: Flow<String>) = attr("cite", value)

}


/**
 * Exposes the JavaScript [HTMLScriptElement](https://developer.mozilla.org/en/docs/Web/API/HTMLScriptElement) to Kotlin
 */
open class Script(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLScriptElement>("script", id, baseClass, job, scope),
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

    fun `for`(value: String) = attr("for", value)
    fun `for`(value: Flow<String>) = attr("for", value)

}


/**
 * Exposes the JavaScript [HTMLSelectElement](https://developer.mozilla.org/en/docs/Web/API/HTMLSelectElement) to Kotlin
 */
open class Select(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLSelectElement>("select", id, baseClass, job, scope) {
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
open class Span(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLSpanElement>("span", id, baseClass, job, scope),
    WithText<HTMLSpanElement>


/**
 * Exposes the JavaScript [HTMLTableCaptionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCaptionElement) to Kotlin
 */
open class Caption(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableCaptionElement>("caption", id, baseClass, job, scope),
    WithText<HTMLTableCaptionElement>


/**
 * Exposes the JavaScript [HTMLTableCellElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableCellElement) to Kotlin
 */
open class Th(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableCellElement>("th", id, baseClass, job, scope),
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
open class Td(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableCellElement>("td", id, baseClass, job, scope),
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
open class Col(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableColElement>("col", id, baseClass, job, scope),
    WithText<HTMLTableColElement> {
    fun span(value: Int) = attr("span", value)
    fun span(value: Flow<Int>) = attr("span", value)

}


/**
 * Exposes the JavaScript [HTMLTableColElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableColElement) to Kotlin
 */
open class Colgroup(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableColElement>("colgroup", id, baseClass, job, scope),
    WithText<HTMLTableColElement> {
    fun span(value: Int) = attr("span", value)
    fun span(value: Flow<Int>) = attr("span", value)

}


/**
 * Exposes the JavaScript [HTMLTableElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableElement) to Kotlin
 */
open class Table(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableElement>("table", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [HTMLTableRowElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableRowElement) to Kotlin
 */
open class Tr(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableRowElement>("tr", id, baseClass, job, scope),
    WithText<HTMLTableRowElement>


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class TFoot(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableSectionElement>("tfoot", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class THead(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableSectionElement>("thead", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [HTMLTableSectionElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTableSectionElement) to Kotlin
 */
open class TBody(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTableSectionElement>("tbody", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [HTMLTextAreaElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTextAreaElement) to Kotlin
 */
open class TextArea(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTextAreaElement>("textarea", id, baseClass, job, scope),
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
        mountSimple(job, value) { v -> value(v) }
    }
}


/**
 * Exposes the JavaScript [HTMLTimeElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTimeElement) to Kotlin
 */
open class Time(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTimeElement>("time", id, baseClass, job, scope),
    WithText<HTMLTimeElement> {
    fun dateTime(value: String) = attr("dateTime", value)
    fun dateTime(value: Flow<String>) = attr("dateTime", value)

}


/**
 * Exposes the JavaScript [HTMLTrackElement](https://developer.mozilla.org/en/docs/Web/API/HTMLTrackElement) to Kotlin
 */
open class Track(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLTrackElement>("track", id, baseClass, job, scope),
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
open class Ul(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLUListElement>("ul", id, baseClass, job, scope)


/**
 * Exposes the JavaScript [SVGElement](https://developer.mozilla.org/en-US/docs/Web/API/SVGElement) to Kotlin
 */
class Svg(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<SVGElement>("", id, baseClass, job, scope) {

    override val domNode: SVGElement = document.createElementNS(SVG_XMLNS, "svg").unsafeCast<SVGElement>().apply {
        if (baseClass != null) setAttributeNS(null, "class", baseClass)
    }

    /**
     * Sets the given [xml] string to the *innerHTML* of the [SVGElement].
     *
     * @param xml svg xml content
     */
    fun content(xml: String) {
        domNode.innerHTML = xml
    }

    fun xmlns(value: String) = attr("xmlns", value)

    fun viewBox(value: String) = attr("viewBox", value)
    fun viewBox(value: Flow<String>) = attr("viewBox", value)

    fun fill(value: String) = attr("fill", value)
    fun fill(value: Flow<String>) = attr("fill", value)
}

const val SVG_XMLNS = "http://www.w3.org/2000/svg"

/**
 * Exposes the JavaScript Path(https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths) to Kotlin
 */
class Path(id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<SVGPathElement>("", id, baseClass, job, scope) {

    override val domNode: SVGPathElement =
        document.createElementNS(SVG_XMLNS, "path").unsafeCast<SVGPathElement>().apply {
            if (baseClass != null) setAttributeNS(null, "class", baseClass)
        }

    /**
     * Sets the namespace for this [SVGElement].
     *
     * @param value xml namespace
     */
    fun xmlns(value: String) = attr("xmlns", value)

    fun d(value: String) = attr("d", value)
    fun d(value: Flow<String>) = attr("d", value)
}

/**
 * Special [Tag] for HTML5 with no attributes
 */
open class TextElement(tagName: String, id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    Tag<HTMLElement>(tagName, id, baseClass, job, scope), WithText<HTMLElement>


/**
 * Context for rendering standard HTML5 [Tag]s
 */
interface RenderContext : WithJob, WithScope {

    /**
     * Renders the data of a [Flow] as [Tag]s to the DOM.
     *
     * @receiver [Flow] containing the data
     * @param into target to mount content to. If not set a child [DIV] is added to the [Tag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Flow<V>.render(into: Tag<HTMLElement>? = null, content: RenderContext.(V) -> Unit) =
        mount(into, this, content)


    /**
     * Renders each element of a [Flow]s content.
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes its internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param into target to mount content to. If not set a child [DIV] is added to the [Tag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Flow<List<V>>.renderEach(
        idProvider: IdProvider<V, *>? = null,
        into: Tag<HTMLElement>? = null,
        content: RenderContext.(V) -> Tag<HTMLElement>
    ) =
        mount(into, this, idProvider, content)

    /**
     * Renders each element of a [Store]s [List] content.
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes its internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param into target to mount content to. If not set a child [DIV] is added to the [Tag] this method is called on
     * @param content [RenderContext] for rendering the data to the DOM
     */
    fun <V> Store<List<V>>.renderEach(
        idProvider: IdProvider<V, *>,
        into: Tag<HTMLElement>? = null,
        content: RenderContext.(Store<V>) -> Tag<HTMLElement>
    ) =
        mount(into, this, idProvider, content)

    /**
     * Renders each element of a [Store]s list content.
     * Internally the [Patch]es are determined using the position of an item in the list.
     * Moves cannot be detected that way and replacing an item at a certain position will be treated as a change of the item.
     *
     * @param content [RenderContext] for rendering the data to the DOM given a [Store] of the list's item-type
     * @param into target to mount content to. If not set a child [DIV] is added to the [Tag] this method is called on
     */
    fun <V> Store<List<V>>.renderEach(
        into: Tag<HTMLElement>? = null,
        content: RenderContext.(Store<V>) -> Tag<HTMLElement>
    ) =
        mount(into, this, content)


    /**
     * Converts the content of a [Flow] to [String] by using [toString] method.
     *
     * @receiver [Flow] with content
     * @return [Flow] with content as [String]
     */
    fun <T> Flow<T>.asString(): Flow<String> = this.map { it.toString() }

    fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T

    /**
     * Evaluates the scope context and initializes a [ScopeContext]
     * for setting new entries to the scope.
     *
     * @param context to evaluate
     */
    private inline fun evalScope(context: (ScopeContext.() -> Unit)): Scope {
        return ScopeContext(this@RenderContext.scope).apply(context).scope
    }

    /**
     * Creates a custom [Tag] with the provided [content].
     *
     * @param tagName Name of the [Tag] in DOM
     * @param content content scope for inner [Tag]s
     * @return custom [Tag]
     */
    fun custom(
        tagName: String,
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: RenderContext.() -> Unit
    ): RenderContext =
        register(Tag(tagName, id, baseClass, job, evalScope(scope)), content)

    fun a(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: A.() -> Unit
    ): A =
        register(A(id, baseClass, job, evalScope(scope)), content)

    fun area(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Area.() -> Unit
    ): Area =
        register(Area(id, baseClass, job, evalScope(scope)), content)

    fun br(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Br.() -> Unit
    ): Br =
        register(Br(id, baseClass, job, evalScope(scope)), content)

    fun button(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Button.() -> Unit
    ): Button =
        register(Button(id, baseClass, job, evalScope(scope)), content)

    fun canvas(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Canvas.() -> Unit
    ): Canvas =
        register(Canvas(id, baseClass, job, evalScope(scope)), content)

    fun dl(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Dl.() -> Unit
    ): Dl =
        register(Dl(id, baseClass, job, evalScope(scope)), content)

    fun dt(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("dt", id, baseClass, job, evalScope(scope)), content)

    fun dd(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("dd", id, baseClass, job, evalScope(scope)), content)

    fun data(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Data.() -> Unit
    ): Data =
        register(Data(id, baseClass, job, evalScope(scope)), content)

    fun datalist(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: DataList.() -> Unit
    ): DataList =
        register(DataList(id, baseClass, job, evalScope(scope)), content)

    fun details(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Details.() -> Unit
    ): Details =
        register(Details(id, baseClass, job, evalScope(scope)), content)

    fun dialog(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Dialog.() -> Unit
    ): Dialog =
        register(Dialog(id, baseClass, job, evalScope(scope)), content)

    fun div(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.() -> Unit
    ): Div =
        register(Div(id, baseClass, job, evalScope(scope)), content)

    fun embed(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Embed.() -> Unit
    ): Embed =
        register(Embed(id, baseClass, job, evalScope(scope)), content)

    fun fieldset(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: FieldSet.() -> Unit
    ): FieldSet =
        register(FieldSet(id, baseClass, job, evalScope(scope)), content)

    fun form(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Form.() -> Unit
    ): Form =
        register(Form(id, baseClass, job, evalScope(scope)), content)

    fun hr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Hr.() -> Unit
    ): Hr =
        register(Hr(id, baseClass, job, evalScope(scope)), content)

    fun h1(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(1, id, baseClass, job, evalScope(scope)), content)

    fun h2(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(2, id, baseClass, job, evalScope(scope)), content)

    fun h3(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(3, id, baseClass, job, evalScope(scope)), content)

    fun h4(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(4, id, baseClass, job, evalScope(scope)), content)

    fun h5(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(5, id, baseClass, job, evalScope(scope)), content)

    fun h6(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: H.() -> Unit
    ): H =
        register(H(6, id, baseClass, job, evalScope(scope)), content)

    fun iframe(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: IFrame.() -> Unit
    ): IFrame =
        register(IFrame(id, baseClass, job, evalScope(scope)), content)

    fun img(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Img.() -> Unit
    ): Img =
        register(Img(id, baseClass, job, evalScope(scope)), content)

    fun input(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Input.() -> Unit
    ): Input =
        register(Input(id, baseClass, job, evalScope(scope)), content)

    fun li(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Li.() -> Unit
    ): Li =
        register(Li(id, baseClass, job, evalScope(scope)), content)

    fun label(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ): Label =
        register(Label(id, baseClass, job, evalScope(scope)), content)

    fun legend(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Legend.() -> Unit
    ): Legend =
        register(Legend(id, baseClass, job, evalScope(scope)), content)

    fun map(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Map.() -> Unit
    ): Map =
        register(Map(id, baseClass, job, evalScope(scope)), content)

    fun audio(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Audio.() -> Unit
    ): Audio =
        register(Audio(id, baseClass, job, evalScope(scope)), content)

    fun video(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Video.() -> Unit
    ): Video =
        register(Video(id, baseClass, job, evalScope(scope)), content)

    fun meter(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Meter.() -> Unit
    ): Meter =
        register(Meter(id, baseClass, job, evalScope(scope)), content)

    fun ins(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Ins.() -> Unit
    ): Ins =
        register(Ins(id, baseClass, job, evalScope(scope)), content)

    fun del(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Del.() -> Unit
    ): Del =
        register(Del(id, baseClass, job, evalScope(scope)), content)

    fun ol(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Ol.() -> Unit
    ): Ol =
        register(Ol(id, baseClass, job, evalScope(scope)), content)

    fun `object`(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Object.() -> Unit
    ): Object =
        register(Object(id, baseClass, job, evalScope(scope)), content)

    fun optgroup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Optgroup.() -> Unit
    ): Optgroup =
        register(Optgroup(id, baseClass, job, evalScope(scope)), content)

    fun option(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Option.() -> Unit
    ): Option =
        register(Option(id, baseClass, job, evalScope(scope)), content)

    fun output(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Output.() -> Unit
    ): Output =
        register(Output(id, baseClass, job, evalScope(scope)), content)

    fun p(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: P.() -> Unit
    ): P =
        register(P(id, baseClass, job, evalScope(scope)), content)

    fun param(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Param.() -> Unit
    ): Param =
        register(Param(id, baseClass, job, evalScope(scope)), content)

    fun picture(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Picture.() -> Unit
    ): Picture =
        register(Picture(id, baseClass, job, evalScope(scope)), content)

    fun pre(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Pre.() -> Unit
    ): Pre =
        register(Pre(id, baseClass, job, evalScope(scope)), content)

    fun progress(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Progress.() -> Unit
    ): Progress =
        register(Progress(id, baseClass, job, evalScope(scope)), content)

    fun quote(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Quote.() -> Unit
    ): Quote =
        register(Quote(id, baseClass, job, evalScope(scope)), content)

    fun script(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Script.() -> Unit
    ): Script =
        register(Script(id, baseClass, job, evalScope(scope)), content)

    fun select(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Select.() -> Unit
    ): Select =
        register(Select(id, baseClass, job, evalScope(scope)), content)

    fun span(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Span.() -> Unit
    ): Span =
        register(Span(id, baseClass, job, evalScope(scope)), content)

    fun caption(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Caption.() -> Unit
    ): Caption =
        register(Caption(id, baseClass, job, evalScope(scope)), content)

    fun th(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Th.() -> Unit
    ): Th =
        register(Th(id, baseClass, job, evalScope(scope)), content)

    fun td(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Td.() -> Unit
    ): Td =
        register(Td(id, baseClass, job, evalScope(scope)), content)

    fun col(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Col.() -> Unit
    ): Col =
        register(Col(id, baseClass, job, evalScope(scope)), content)

    fun colgroup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Colgroup.() -> Unit
    ): Colgroup =
        register(Colgroup(id, baseClass, job, evalScope(scope)), content)

    fun table(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Table.() -> Unit
    ): Table =
        register(Table(id, baseClass, job, evalScope(scope)), content)

    fun tr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tr.() -> Unit
    ): Tr =
        register(Tr(id, baseClass, job, evalScope(scope)), content)

    fun tfoot(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TFoot.() -> Unit
    ): TFoot =
        register(TFoot(id, baseClass, job, evalScope(scope)), content)

    fun thead(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: THead.() -> Unit
    ): THead =
        register(THead(id, baseClass, job, evalScope(scope)), content)

    fun tbody(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TBody.() -> Unit
    ): TBody =
        register(TBody(id, baseClass, job, evalScope(scope)), content)

    fun textarea(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextArea.() -> Unit
    ): TextArea =
        register(TextArea(id, baseClass, job, evalScope(scope)), content)

    fun time(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Time.() -> Unit
    ): Time =
        register(Time(id, baseClass, job, evalScope(scope)), content)

    fun track(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Track.() -> Unit
    ): Track =
        register(Track(id, baseClass, job, evalScope(scope)), content)

    fun ul(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Ul.() -> Unit
    ): Ul =
        register(Ul(id, baseClass, job, evalScope(scope)), content)

    fun address(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("address", id, baseClass, job, evalScope(scope)), content)

    fun article(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("article", id, baseClass, job, evalScope(scope)), content)

    fun aside(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("aside", id, baseClass, job, evalScope(scope)), content)

    fun bdi(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("bdi", id, baseClass, job, evalScope(scope)), content)

    fun figcaption(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("figcaption", id, baseClass, job, evalScope(scope)), content)

    fun figure(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("figure", id, baseClass, job, evalScope(scope)), content)

    fun footer(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("footer", id, baseClass, job, evalScope(scope)), content)

    fun header(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("header", id, baseClass, job, evalScope(scope)), content)

    fun main(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("main", id, baseClass, job, evalScope(scope)), content)

    fun mark(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("mark", id, baseClass, job, evalScope(scope)), content)

    fun nav(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("nav", id, baseClass, job, evalScope(scope)), content)

    fun noscript(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("noscript", id, baseClass, job, evalScope(scope)), content)

    fun rp(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("rp", id, baseClass, job, evalScope(scope)), content)

    fun rt(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("rt", id, baseClass, job, evalScope(scope)), content)

    fun ruby(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("ruby", id, baseClass, job, evalScope(scope)), content)

    fun section(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("section", id, baseClass, job, evalScope(scope)), content)

    fun summary(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("summary", id, baseClass, job, evalScope(scope)), content)

    fun wbr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("wbr", id, baseClass, job, evalScope(scope)), content)

    fun blockquote(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("blockquote", id, baseClass, job, evalScope(scope)), content)

    fun em(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("em", id, baseClass, job, evalScope(scope)), content)

    fun strong(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("strong", id, baseClass, job, evalScope(scope)), content)

    fun small(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("small", id, baseClass, job, evalScope(scope)), content)

    fun s(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("s", id, baseClass, job, evalScope(scope)), content)

    fun cite(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("cite", id, baseClass, job, evalScope(scope)), content)

    fun q(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("q", id, baseClass, job, evalScope(scope)), content)

    fun dfn(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("dfn", id, baseClass, job, evalScope(scope)), content)

    fun abbr(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("abbr", id, baseClass, job, evalScope(scope)), content)

    fun code(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("code", id, baseClass, job, evalScope(scope)), content)

    fun `var`(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("var", id, baseClass, job, evalScope(scope)), content)

    fun samp(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("samp", id, baseClass, job, evalScope(scope)), content)

    fun kbd(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("kbd", id, baseClass, job, evalScope(scope)), content)

    fun sub(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("sub", id, baseClass, job, evalScope(scope)), content)

    fun sup(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("sup", id, baseClass, job, evalScope(scope)), content)

    fun i(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("i", id, baseClass, job, evalScope(scope)), content)

    fun b(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("b", id, baseClass, job, evalScope(scope)), content)

    fun u(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("u", id, baseClass, job, evalScope(scope)), content)

    fun bdo(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("bdo", id, baseClass, job, evalScope(scope)), content)

    fun command(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextElement.() -> Unit
    ): TextElement =
        register(TextElement("command", id, baseClass, job, evalScope(scope)), content)

    fun svg(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Svg.() -> Unit
    ): Svg =
        register(Svg(id, baseClass, job = job, evalScope(scope)), content)

    fun path(
        baseClass: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Path.() -> Unit
    ): Path =
        register(Path(id, baseClass, job = job, evalScope(scope)), content)
}
