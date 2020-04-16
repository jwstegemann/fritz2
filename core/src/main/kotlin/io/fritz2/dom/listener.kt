package io.fritz2.dom

import io.fritz2.dom.html.Key
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.files.FileList

/**
 * [Listener] handles a Flow of [Event]s.
 */
inline class Listener<E : Event, X : Element>(val events: Flow<E>) {
    /**
     * Maps the given [Event] to a new value.
     */
    inline fun <R> map(crossinline mapper: suspend (E) -> R) = events.map(mapper)
}

/**
 * Calls the js method [preventDefault] on the given [Event]
 */
fun <E : Event, X : Element> Listener<E, X>.preventDefault(): Listener<E, X> = Listener(
    events.map { it.preventDefault(); it }
)

/**
 * Calls the js method [stopImmediatePropagation] on the given [Event]
 */
fun <E : Event, X : Element> Listener<E, X>.stopImmediatePropagation(): Listener<E, X> = Listener(
    events.map { it.stopImmediatePropagation(); it }
)

/**
 * Calls the js method [stopPropagation] on the given [Event]
 */
fun <E : Event, X : Element> Listener<E, X>.stopPropagation(): Listener<E, X> = Listener(
    events.map { it.stopPropagation(); it }
)

/**
 * Gives you the new value as [String] from the targeting [Element]
 */
fun Listener<Event, HTMLInputElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Gives you the new value as [Double] from the targeting [Element]
 */
fun Listener<Event, HTMLInputElement>.valuesAsNumber(): Flow<Double> =
    events.map { it.target.unsafeCast<HTMLInputElement>().valueAsNumber }

/**
 * Gives you the new value as [String] from the targeting [Element]
 */
fun Listener<Event, HTMLSelectElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLSelectElement>().value }

/**
 * Gives you the new value as [String] from the targeting [Element]
 */
fun Listener<Event, HTMLTextAreaElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLTextAreaElement>().value }

/**
 * Gives you the [FileList] from the targeting [Element]
 */
fun Listener<Event, HTMLInputElement>.files(): Flow<FileList?> =
    events.map { it.target.unsafeCast<HTMLInputElement>().files }

/**
 * Gives you the checked value as [Boolean] from the targeting [Element]
 */
fun Listener<Event, HTMLInputElement>.states(): Flow<Boolean> =
    events.map { it.target.unsafeCast<HTMLInputElement>().checked }

/**
 * Gives you the selected index as [Int] from the targeting [Element]
 */
fun Listener<Event, HTMLSelectElement>.selectedIndex(): Flow<Int> =
    events.map { it.target.unsafeCast<HTMLSelectElement>().selectedIndex }

/**
 * Gives you the selected value as [String] from the targeting [Element]
 */
fun Listener<Event, HTMLSelectElement>.selectedValue(): Flow<String> =
    events.map {
        val select = it.target.unsafeCast<HTMLSelectElement>()
        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().value
    }

/**
 * Gives you the selected text as [String] from the targeting [Element]
 */
fun Listener<Event, HTMLSelectElement>.selectedText(): Flow<String> =
    events.map {
        val select = it.target.unsafeCast<HTMLSelectElement>()
        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().text
    }

/**
 * Gives you the pressed key as [Key] from a [KeyboardEvent]
 */
fun <X : Element> Listener<KeyboardEvent, X>.key(): Flow<Key> = events.map { Key.from(it) }