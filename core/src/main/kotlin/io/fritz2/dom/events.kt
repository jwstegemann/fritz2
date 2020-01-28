package io.fritz2.dom

import io.fritz2.binding.Store
import io.fritz2.dom.html.EventType
import io.fritz2.dom.html.Events
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.w3c.dom.Element
import org.w3c.dom.events.Event

@FlowPreview
@ExperimentalCoroutinesApi
abstract class WithEvents<out T : Element> : WithDomNode<T> {

    private fun <T> subscribe(type: EventType<T>): Flow<T> = callbackFlow {
        val listener: (Event) -> Unit = {
            channel.offer(type.extract(it))
        }
        domNode.addEventListener(type.name, listener)

        awaitClose { domNode.removeEventListener(type.name, listener) }
    }

    operator fun <T, S> Flow<T>.compareTo(handler: Store<S>.Handler<T>): Int {
        handler.handle(this)
        return 0
    }

    val onabort by lazy { subscribe(Events.abort)}
    val onafterprint by lazy { subscribe(Events.afterprint)}
    val onbeforeprint by lazy { subscribe(Events.beforeprint)}
    val onbeforeunload by lazy { subscribe(Events.beforeunload)}
    val onblur by lazy { subscribe(Events.blur)}
    val oncanplay by lazy { subscribe(Events.canplay)}
    val oncanplaythrough by lazy { subscribe(Events.canplaythrough)}
    val onchange by lazy { subscribe(Events.change)}
    val onclick by lazy { subscribe(Events.click)}
    val oncontextmenu by lazy { subscribe(Events.contextmenu)}
    val oncopy by lazy { subscribe(Events.copy)}
    val oncut by lazy { subscribe(Events.cut)}
    val ondblclick by lazy { subscribe(Events.dblclick)}
    val ondrag by lazy { subscribe(Events.drag)}
    val ondragend by lazy { subscribe(Events.dragend)}
    val ondragenter by lazy { subscribe(Events.dragenter)}
    val ondragleave by lazy { subscribe(Events.dragleave)}
    val ondragover by lazy { subscribe(Events.dragover)}
    val ondragstart by lazy { subscribe(Events.dragstart)}
    val ondrop by lazy { subscribe(Events.drop)}
    val ondurationchange by lazy { subscribe(Events.durationchange)}
    val onended by lazy { subscribe(Events.ended)}
    val onfocus by lazy { subscribe(Events.focus)}
    val onfocusin by lazy { subscribe(Events.focusin)}
    val onfocusout by lazy { subscribe(Events.focusout)}
    val onfullscreenchange by lazy { subscribe(Events.fullscreenchange)}
    val onfullscreenerror by lazy { subscribe(Events.fullscreenerror)}
    val onhashchange by lazy { subscribe(Events.hashchange)}
    val oninput by lazy { subscribe(Events.input)}
    val oninvalid by lazy { subscribe(Events.invalid)}
    val onkeydown by lazy { subscribe(Events.keydown)}
    val onkeypress by lazy { subscribe(Events.keypress)}
    val onkeyup by lazy { subscribe(Events.keyup)}
    val onload by lazy { subscribe(Events.load)}
    val onloadeddata by lazy { subscribe(Events.loadeddata)}
    val onloadedmetadata by lazy { subscribe(Events.loadedmetadata)}
    val onloadstart by lazy { subscribe(Events.loadstart)}
    val onmessage by lazy { subscribe(Events.message)}
    val onmousedown by lazy { subscribe(Events.mousedown)}
    val onmouseenter by lazy { subscribe(Events.mouseenter)}
    val onmouseleave by lazy { subscribe(Events.mouseleave)}
    val onmousemove by lazy { subscribe(Events.mousemove)}
    val onmouseover by lazy { subscribe(Events.mouseover)}
    val onmouseout by lazy { subscribe(Events.mouseout)}
    val onmouseup by lazy { subscribe(Events.mouseup)}
    val onoffline by lazy { subscribe(Events.offline)}
    val ononline by lazy { subscribe(Events.online)}
    val onopen by lazy { subscribe(Events.open)}
    val onpagehide by lazy { subscribe(Events.pagehide)}
    val onpageshow by lazy { subscribe(Events.pageshow)}
    val onpaste by lazy { subscribe(Events.paste)}
    val onpause by lazy { subscribe(Events.pause)}
    val onplay by lazy { subscribe(Events.play)}
    val onplaying by lazy { subscribe(Events.playing)}
    val onpopstate by lazy { subscribe(Events.popstate)}
    val onprogress by lazy { subscribe(Events.progress)}
    val onratechange by lazy { subscribe(Events.ratechange)}
    val onresize by lazy { subscribe(Events.resize)}
    val onreset by lazy { subscribe(Events.reset)}
    val onscroll by lazy { subscribe(Events.scroll)}
    val onsearch by lazy { subscribe(Events.search)}
    val onseeked by lazy { subscribe(Events.seeked)}
    val onseeking by lazy { subscribe(Events.seeking)}
    val onselect by lazy { subscribe(Events.select)}
    val onshow by lazy { subscribe(Events.show)}
    val onstalled by lazy { subscribe(Events.stalled)}
    val onstorage by lazy { subscribe(Events.storage)}
    val onsubmit by lazy { subscribe(Events.submit)}
    val onsuspend by lazy { subscribe(Events.suspend)}
    val ontimeupdate by lazy { subscribe(Events.timeupdate)}
    val ontoggle by lazy { subscribe(Events.toggle)}
    val ontouchcancel by lazy { subscribe(Events.touchcancel)}
    val ontouchend by lazy { subscribe(Events.touchend)}
    val ontouchmove by lazy { subscribe(Events.touchmove)}
    val ontouchstart by lazy { subscribe(Events.touchstart)}
    val onunload by lazy { subscribe(Events.unload)}
    val onvolumechange by lazy { subscribe(Events.volumechange)}
    val onwaiting by lazy { subscribe(Events.waiting)}
    val onwheel by lazy { subscribe(Events.wheel)}
}