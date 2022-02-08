@file:Suppress("unused")

package dev.fritz2.dom

import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import org.w3c.dom.Window
import org.w3c.dom.events.Event

/**
 * Represents all [Event]s of the browser [window] object as [Event]-flows
 */
object Window : WithEvents<Window> {

    private val scope = MainScope()

    override fun <X : Event> subscribe(name: String): Listener<X, Window> = Listener(callbackFlow {
        val listener: (Event) -> Unit = {
            try {
                trySend(it.unsafeCast<X>())
            } catch (e: Exception) {
                console.error("Unexpected type while listening for `$name` events in Window object", e)
            }
        }
        window.addEventListener(name, listener)

        awaitClose { window.removeEventListener(name, listener) }
    }.shareIn(scope, SharingStarted.Lazily))

    override val aborts by lazy { super.aborts }
    override val afterprints by lazy { super.afterprints }
    override val beforeprints by lazy { super.beforeprints }
    override val beforeunloads by lazy { super.beforeunloads }
    override val blurs by lazy { super.blurs }
    override val canplays by lazy { super.canplays }
    override val canplaythroughs by lazy { super.canplaythroughs }
    override val changes by lazy { super.changes }
    override val clicks by lazy { super.clicks }
    override val contextmenus by lazy { super.contextmenus }
    override val copys by lazy { super.copys }
    override val cuts by lazy { super.cuts }
    override val dblclicks by lazy { super.dblclicks }
    override val drags by lazy { super.drags }
    override val dragends by lazy { super.dragends }
    override val dragenters by lazy { super.dragenters }
    override val dragleaves by lazy { super.dragleaves }
    override val dragovers by lazy { super.dragovers }
    override val dragstarts by lazy { super.dragstarts }
    override val drops by lazy { super.drops }
    override val durationchanges by lazy { super.durationchanges }
    override val endeds by lazy { super.endeds }
    override val focuss by lazy { super.focuss }
    override val focusins by lazy { super.focusins }
    override val focusouts by lazy { super.focusouts }
    override val fullscreenchanges by lazy { super.fullscreenchanges }
    override val fullscreenerrors by lazy { super.fullscreenerrors }
    override val hashchanges by lazy { super.hashchanges }
    override val inputs by lazy { super.inputs }
    override val invalids by lazy { super.invalids }
    override val keydowns by lazy { super.keydowns }
    override val keypresss by lazy { super.keypresss }
    override val keyups by lazy { super.keyups }
    override val loads by lazy { super.loads }
    override val loadeddatas by lazy { super.loadeddatas }
    override val loadedmetadatas by lazy { super.loadedmetadatas }
    override val loadstarts by lazy { super.loadstarts }
    override val messages by lazy { super.messages }
    override val mousedowns by lazy { super.mousedowns }
    override val mouseenters by lazy { super.mouseenters }
    override val mouseleaves by lazy { super.mouseleaves }
    override val mousemoves by lazy { super.mousemoves }
    override val mouseovers by lazy { super.mouseovers }
    override val mouseouts by lazy { super.mouseouts }
    override val mouseups by lazy { super.mouseups }
    override val offlines by lazy { super.offlines }
    override val onlines by lazy { super.onlines }
    override val opens by lazy { super.opens }
    override val pagehides by lazy { super.pagehides }
    override val pageshows by lazy { super.pageshows }
    override val pastes by lazy { super.pastes }
    override val pauses by lazy { super.pauses }
    override val plays by lazy { super.plays }
    override val playings by lazy { super.playings }
    override val popstates by lazy { super.popstates }
    override val progresss by lazy { super.progresss }
    override val ratechanges by lazy { super.ratechanges }
    override val resizes by lazy { super.resizes }
    override val resets by lazy { super.resets }
    override val scrolls by lazy { super.scrolls }
    override val searchs by lazy { super.searchs }
    override val seekeds by lazy { super.seekeds }
    override val seekings by lazy { super.seekings }
    override val selects by lazy { super.selects }
    override val shows by lazy { super.shows }
    override val stalleds by lazy { super.stalleds }
    override val storages by lazy { super.storages }
    override val submits by lazy { super.submits }
    override val suspends by lazy { super.suspends }
    override val timeupdates by lazy { super.timeupdates }
    override val toggles by lazy { super.toggles }
    override val touchcancels by lazy { super.touchcancels }
    override val touchends by lazy { super.touchends }
    override val touchmoves by lazy { super.touchmoves }
    override val touchstarts by lazy { super.touchstarts }
    override val unloads by lazy { super.unloads }
    override val volumechanges by lazy { super.volumechanges }
    override val waitings by lazy { super.waitings }
    override val wheels by lazy { super.wheels }
}

