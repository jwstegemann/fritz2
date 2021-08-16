package dev.fritz2.dom

import dev.fritz2.dom.html.EventType
import dev.fritz2.dom.html.Events
import dev.fritz2.dom.html.WithJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.w3c.dom.Element
import org.w3c.dom.events.Event

/**
 * Context for handling events
 */
interface EventContext<out T : Element> : WithJob, WithEvents<T>

/**
 * Offers [DomListener]s for all DOM-events available.
 */
interface WithEvents<out T : Element> : WithDomNode<T> {
    /**
     * Creates a [DomListener] on a DOM-element.
     *
     * @param type [EventType] to listen for
     */
    private fun <E : Event> subscribe(type: EventType<E>): DomListener<E, T> = DomListener(
        callbackFlow {
            val listener: (Event) -> Unit = {
                trySend(it.unsafeCast<E>())
            }
            domNode.addEventListener(type.name, listener)

            awaitClose { domNode.removeEventListener(type.name, listener) }
        })

    val aborts
        get() = subscribe(Events.abort)
    val afterprints
        get() = subscribe(Events.afterprint)
    val beforeprints
        get() = subscribe(Events.beforeprint)
    val beforeunloads
        get() = subscribe(Events.beforeunload)
    val blurs
        get() = subscribe(Events.blur)
    val canplays
        get() = subscribe(Events.canplay)
    val canplaythroughs
        get() = subscribe(Events.canplaythrough)
    val changes
        get() = subscribe(Events.change)
    val clicks
        get() = subscribe(Events.click)
    val contextmenus
        get() = subscribe(Events.contextmenu)
    val copys
        get() = subscribe(Events.copy)
    val cuts
        get() = subscribe(Events.cut)
    val dblclicks
        get() = subscribe(Events.dblclick)
    val drags
        get() = subscribe(Events.drag)
    val dragends
        get() = subscribe(Events.dragend)
    val dragenters
        get() = subscribe(Events.dragenter)
    val dragleaves
        get() = subscribe(Events.dragleave)
    val dragovers
        get() = subscribe(Events.dragover)
    val dragstarts
        get() = subscribe(Events.dragstart)
    val drops
        get() = subscribe(Events.drop)
    val durationchanges
        get() = subscribe(Events.durationchange)
    val endeds
        get() = subscribe(Events.ended)
    val focuss
        get() = subscribe(Events.focus)
    val focusins
        get() = subscribe(Events.focusin)
    val focusouts
        get() = subscribe(Events.focusout)
    val fullscreenchanges
        get() = subscribe(Events.fullscreenchange)
    val fullscreenerrors
        get() = subscribe(Events.fullscreenerror)
    val hashchanges
        get() = subscribe(Events.hashchange)
    val inputs
        get() = subscribe(Events.input)
    val invalids
        get() = subscribe(Events.invalid)
    val keydowns
        get() = subscribe(Events.keydown)
    val keypresss
        get() = subscribe(Events.keypress)
    val keyups
        get() = subscribe(Events.keyup)
    val loads
        get() = subscribe(Events.load)
    val loadeddatas
        get() = subscribe(Events.loadeddata)
    val loadedmetadatas
        get() = subscribe(Events.loadedmetadata)
    val loadstarts
        get() = subscribe(Events.loadstart)
    val messages
        get() = subscribe(Events.message)
    val mousedowns
        get() = subscribe(Events.mousedown)
    val mouseenters
        get() = subscribe(Events.mouseenter)
    val mouseleaves
        get() = subscribe(Events.mouseleave)
    val mousemoves
        get() = subscribe(Events.mousemove)
    val mouseovers
        get() = subscribe(Events.mouseover)
    val mouseouts
        get() = subscribe(Events.mouseout)
    val mouseups
        get() = subscribe(Events.mouseup)
    val offlines
        get() = subscribe(Events.offline)
    val onlines
        get() = subscribe(Events.online)
    val opens
        get() = subscribe(Events.open)
    val pagehides
        get() = subscribe(Events.pagehide)
    val pageshows
        get() = subscribe(Events.pageshow)
    val pastes
        get() = subscribe(Events.paste)
    val pauses
        get() = subscribe(Events.pause)
    val plays
        get() = subscribe(Events.play)
    val playings
        get() = subscribe(Events.playing)
    val popstates
        get() = subscribe(Events.popstate)
    val progresss
        get() = subscribe(Events.progress)
    val ratechanges
        get() = subscribe(Events.ratechange)
    val resizes
        get() = subscribe(Events.resize)
    val resets
        get() = subscribe(Events.reset)
    val scrolls
        get() = subscribe(Events.scroll)
    val searchs
        get() = subscribe(Events.search)
    val seekeds
        get() = subscribe(Events.seeked)
    val seekings
        get() = subscribe(Events.seeking)
    val selects
        get() = subscribe(Events.select)
    val shows
        get() = subscribe(Events.show)
    val stalleds
        get() = subscribe(Events.stalled)
    val storages
        get() = subscribe(Events.storage)
    val submits
        get() = subscribe(Events.submit)
    val suspends
        get() = subscribe(Events.suspend)
    val timeupdates
        get() = subscribe(Events.timeupdate)
    val toggles
        get() = subscribe(Events.toggle)
    val touchcancels
        get() = subscribe(Events.touchcancel)
    val touchends
        get() = subscribe(Events.touchend)
    val touchmoves
        get() = subscribe(Events.touchmove)
    val touchstarts
        get() = subscribe(Events.touchstart)
    val unloads
        get() = subscribe(Events.unload)
    val volumechanges
        get() = subscribe(Events.volumechange)
    val waitings
        get() = subscribe(Events.waiting)
    val wheels
        get() = subscribe(Events.wheel)
}