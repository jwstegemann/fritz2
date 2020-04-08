package io.fritz2.dom

import io.fritz2.dom.html.Key
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
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
 * Gives you the new value as [String] from the targeting [Element]
 */
fun Listener<Event, HTMLInputElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLInputElement>().value }

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
 * Gives you the pressed key as [Key] from a [KeyboardEvent]
 */
fun <X : Element> Listener<KeyboardEvent, X>.key(): Flow<Key> = events.map { Key.from(it) }