package io.fritz2.dom

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

    val aborts by lazy { subscribe(Events.abort)}
    val afterprints by lazy { subscribe(Events.afterprint)}
    val beforeprints by lazy { subscribe(Events.beforeprint)}
    val beforeunloads by lazy { subscribe(Events.beforeunload)}
    val blurs by lazy { subscribe(Events.blur)}
    val canplays by lazy { subscribe(Events.canplay)}
    val canplaythroughs by lazy { subscribe(Events.canplaythrough)}
    val changes by lazy { subscribe(Events.change)}
    val clicks by lazy { subscribe(Events.click)}
    val contextmenus by lazy { subscribe(Events.contextmenu)}
    val copys by lazy { subscribe(Events.copy)}
    val cuts by lazy { subscribe(Events.cut)}
    val dblclicks by lazy { subscribe(Events.dblclick)}
    val drags by lazy { subscribe(Events.drag)}
    val dragends by lazy { subscribe(Events.dragend)}
    val dragenters by lazy { subscribe(Events.dragenter)}
    val dragleaves by lazy { subscribe(Events.dragleave)}
    val dragovers by lazy { subscribe(Events.dragover)}
    val dragstarts by lazy { subscribe(Events.dragstart)}
    val drops by lazy { subscribe(Events.drop)}
    val durationchanges by lazy { subscribe(Events.durationchange)}
    val endeds by lazy { subscribe(Events.ended)}
    val focuss by lazy { subscribe(Events.focus)}
    val focusins by lazy { subscribe(Events.focusin)}
    val focusouts by lazy { subscribe(Events.focusout)}
    val fullscreenchanges by lazy { subscribe(Events.fullscreenchange)}
    val fullscreenerrors by lazy { subscribe(Events.fullscreenerror)}
    val hashchanges by lazy { subscribe(Events.hashchange)}
    val inputs by lazy { subscribe(Events.input)}
    val invalids by lazy { subscribe(Events.invalid)}
    val keydowns by lazy { subscribe(Events.keydown)}
    val keypresss by lazy { subscribe(Events.keypress)}
    val keyups by lazy { subscribe(Events.keyup)}
    val loads by lazy { subscribe(Events.load)}
    val loadeddatas by lazy { subscribe(Events.loadeddata)}
    val loadedmetadatas by lazy { subscribe(Events.loadedmetadata)}
    val loadstarts by lazy { subscribe(Events.loadstart)}
    val messages by lazy { subscribe(Events.message)}
    val mousedowns by lazy { subscribe(Events.mousedown)}
    val mouseenters by lazy { subscribe(Events.mouseenter)}
    val mouseleaves by lazy { subscribe(Events.mouseleave)}
    val mousemoves by lazy { subscribe(Events.mousemove)}
    val mouseovers by lazy { subscribe(Events.mouseover)}
    val mouseouts by lazy { subscribe(Events.mouseout)}
    val mouseups by lazy { subscribe(Events.mouseup)}
    val offlines by lazy { subscribe(Events.offline)}
    val onlines by lazy { subscribe(Events.online)}
    val opens by lazy { subscribe(Events.open)}
    val pagehides by lazy { subscribe(Events.pagehide)}
    val pageshows by lazy { subscribe(Events.pageshow)}
    val pastes by lazy { subscribe(Events.paste)}
    val pauses by lazy { subscribe(Events.pause)}
    val plays by lazy { subscribe(Events.play)}
    val playings by lazy { subscribe(Events.playing)}
    val popstates by lazy { subscribe(Events.popstate)}
    val progresss by lazy { subscribe(Events.progress)}
    val ratechanges by lazy { subscribe(Events.ratechange)}
    val resizes by lazy { subscribe(Events.resize)}
    val resets by lazy { subscribe(Events.reset)}
    val scrolls by lazy { subscribe(Events.scroll)}
    val searchs by lazy { subscribe(Events.search)}
    val seekeds by lazy { subscribe(Events.seeked)}
    val seekings by lazy { subscribe(Events.seeking)}
    val selects by lazy { subscribe(Events.select)}
    val shows by lazy { subscribe(Events.show)}
    val stalleds by lazy { subscribe(Events.stalled)}
    val storages by lazy { subscribe(Events.storage)}
    val submits by lazy { subscribe(Events.submit)}
    val suspends by lazy { subscribe(Events.suspend)}
    val timeupdates by lazy { subscribe(Events.timeupdate)}
    val toggles by lazy { subscribe(Events.toggle)}
    val touchcancels by lazy { subscribe(Events.touchcancel)}
    val touchends by lazy { subscribe(Events.touchend)}
    val touchmoves by lazy { subscribe(Events.touchmove)}
    val touchstarts by lazy { subscribe(Events.touchstart)}
    val unloads by lazy { subscribe(Events.unload)}
    val volumechanges by lazy { subscribe(Events.volumechange)}
    val waitings by lazy { subscribe(Events.waiting)}
    val wheels by lazy { subscribe(Events.wheel)}
}