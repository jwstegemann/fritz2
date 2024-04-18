@file:Suppress("unused")

package dev.fritz2.core

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import org.w3c.files.FileList

/**
 * Creates a [Listener] for the given [Event] type and [eventName].
 *
 * @param eventName the [DOM-API name](https://developer.mozilla.org/en-US/docs/Web/API/Element#events) of an event.
 * Can be a custom name.
 * @param capture if `true`, activates capturing mode, else remains in `bubble` mode (default)
 * @param selector optional lambda expression to select specific events with option to manipulate it
 * (e.g. `preventDefault` or `stopPropagation`).
 *
 * @return a [Listener]-object, which is more or less a [Flow] of the specific `Event`-type.
 */
fun <E : Event, T : EventTarget> T.subscribe(
    eventName: String,
    capture: Boolean = false,
    selector: E.() -> Boolean = { true }
): Listener<E, T> =
    Listener(
        callbackFlow {
            val listener: (E) -> Unit = {
                try {
                    if (it.selector()) trySend(it.unsafeCast<E>())
                } catch (e: Exception) {
                    console.error("Unexpected event type while listening for `$eventName` event", e)
                }
            }
            this@subscribe.addEventListener(eventName, listener.unsafeCast<Event.() -> Unit>(), capture)

            awaitClose { this@subscribe.removeEventListener(eventName, listener.unsafeCast<Event.() -> Unit>(), capture) }
        }
    )

/**
 * Encapsulates the [Flow] of the [Event].
 *
 * Acts as a marker class in order to keep the type of the element, so we can offer dedicated methods to extract
 * values from some specific events.
 *
 * @see [values]
 */
value class Listener<X : Event, out T : EventTarget>(private val events: Flow<X>) : Flow<X> by events

/**
 * Extracts the [HTMLInputElement.value] from the [Event.target].
 */
fun Listener<*, HTMLInputElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Extracts the [HTMLSelectElement.value] from the [Event.target].
 */
fun Listener<*, HTMLSelectElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLSelectElement>().value }

/**
 * Extracts the [HTMLInputElement.value] from the [Event.target].
 */
fun Listener<*, HTMLFieldSetElement>.values(): Flow<String> =
    this.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Extracts the [HTMLTextAreaElement.value] from the [Event.target].
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