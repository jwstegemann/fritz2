@file:Suppress("unused")
package dev.fritz2.core

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import org.w3c.files.FileList

/**
 * Creates a [Listener] for the given [Event] type and [name].
 */
fun <X : Event, T : EventTarget> T.subscribe(
    name: String,
    capture: Boolean = false,
    init: Event.() -> Unit = {}
): Listener<X, T> =
    Listener(callbackFlow {
        val listener: (Event) -> Unit = {
            try {
                it.init()
                trySend(it.unsafeCast<X>())
            } catch (e: Exception) {
                console.error("Unexpected event type while listening for `$name` event", e)
            }
        }
        this@subscribe.addEventListener(name, listener, capture)

        awaitClose { this@subscribe.removeEventListener(name, listener, capture) }
    }.filter { it.asDynamic().fritz2StopPropagation == undefined })

/**
 * Encapsulates the [Flow] of the [Event].
 */
class Listener<X: Event, out T: EventTarget>(private val events: Flow<X>): Flow<X> by events {

    constructor(listener: Listener<X, T>) : this(listener.events)

    /**
     * Calls [Event.preventDefault] on the [Event]-flow.
     */
    fun preventDefault(): Listener<X, T> = Listener(this.events.map { it.preventDefault(); it })

    /**
     * Calls [Event.stopImmediatePropagation] on the [Event]-flow.
     */
    fun stopImmediatePropagation(): Listener<X, T> = Listener(this.events.map {
        it.stopImmediatePropagation()
        it.asDynamic().fritz2StopPropagation = true
        it
    })

    /**
     * Calls [Event.stopPropagation] on the [Event]-flow.
     */
    fun stopPropagation(): Listener<X, T> = Listener(this.events.map {
        it.stopPropagation()
        it.asDynamic().fritz2StopPropagation = true
        it
    })

    /**
     * Calls [Event.composedPath] on the [Event]-flow.
     */
    fun composedPath(): Flow<Array<EventTarget>> = this.events.map { it.composedPath() }

}

/**
 * Calls [Event.preventDefault] on the [Event]-flow.
 */
fun <E: Event> Flow<E>.preventDefault(): Flow<E> = this.map { it.preventDefault(); it }
/**
 * Calls [Event.stopImmediatePropagation] on the [Event]-flow.
 */
fun <E: Event> Flow<E>.stopImmediatePropagation(): Flow<E> = this.map {
    it.stopImmediatePropagation()
    it.asDynamic().fritz2StopPropagation = true
    it
}
/**
 * Calls [Event.stopPropagation] on the [Event]-flow.
 */
fun <E: Event> Flow<E>.stopPropagation(): Flow<E> = this.map {
    it.stopPropagation()
    it.asDynamic().fritz2StopPropagation = true
    it
}
/**
 * Calls [Event.composedPath] on the [Event]-flow.
 */
fun <E: Event> Flow<E>.composedPath(): Flow<Array<EventTarget>> = this.map { it.composedPath() }


/**
 * Extracts the [HTMLInputElement.value] from the [Event.target].
 */
fun Listener<*, HTMLInputElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Extracts the [HTMLInputElement.value] from the [Event.target].
 */
fun Listener<*, HTMLSelectElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLSelectElement>().value }

/**
 * Extracts the [HTMLInputElement.value] from the [Event.target].
 */
fun Listener<*, HTMLFieldSetElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Extracts the [HTMLInputElement.value] from the [Event.target].
 */
fun Listener<*, HTMLTextAreaElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLTextAreaElement>().value }

/**
 * Extracts a [FileList] from the [Event.target].
 */
fun Listener<*, HTMLInputElement>.files(): Flow<FileList?> =
    this.map { it.target.unsafeCast<HTMLInputElement>().files }

/**
 * Extracts the [HTMLInputElement.checked] state from the [Event.target].
 */
fun Listener<*, HTMLInputElement>.states(): Flow<Boolean> =
    this.map { it.target.unsafeCast<HTMLInputElement>().checked }

/**
 * Extracts the [HTMLSelectElement.selectedIndex] from the [Event.target].
 */
fun Listener<*, HTMLSelectElement>.selectedIndex(): Flow<Int> =
    this.map { it.target.unsafeCast<HTMLSelectElement>().selectedIndex }

/**
 * Extracts the [HTMLOptionElement.value] from the selected [HTMLOptionElement].
 */
fun Listener<*, HTMLSelectElement>.selectedValue(): Flow<String> =
    this.map {
        val select = it.target.unsafeCast<HTMLSelectElement>()
        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().value
    }

/**
 * Extracts the [HTMLOptionElement.text] from the selected [HTMLOptionElement].
 */
fun Listener<*, HTMLSelectElement>.selectedText(): Flow<String> =
    this.map {
        val select = it.target.unsafeCast<HTMLSelectElement>()
        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().text
    }