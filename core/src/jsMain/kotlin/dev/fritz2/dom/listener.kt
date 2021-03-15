package dev.fritz2.dom

import dev.fritz2.dom.html.Key
import dev.fritz2.dom.html.Keys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.files.FileList

/**
 * Handles a Flow of [Event]s
 */
open class Listener<E : Event>(val events: Flow<E>) {
    fun preventDefault() = Listener(events.map { it.preventDefault(); it })
    
    fun stopImmediatePropagation() = Listener(events.map { it.stopImmediatePropagation(); it })

    fun stopPropagation() =  Listener(events.map { it.stopPropagation(); it })

    fun composedPath() =  events.map { it.composedPath() }

    inline fun <R> map(crossinline mapper: suspend (E) -> R) = events.map(mapper)
}

/**
 * Handles a Flow of Window [Event]s
 */
class WindowListener<E: Event>(events: Flow<E>): Listener<E>(events)

/**
 * Handles a Flow of Dom [Event]s.
 */
class DomListener<E : Event, out X : Element>(events: Flow<E>): Listener<E>(events)

/**
 * Gives you the new value as [String] from the targeting [Element].
 */
fun DomListener<Event, HTMLInputElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Gives you the new value as [String] from the targeting [Element].
 */
fun DomListener<InputEvent, HTMLInputElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLInputElement>().value }

/**
 * Gives you the new value as [Double] from the targeting [Element].
 */
fun DomListener<Event, HTMLInputElement>.valuesAsNumber(): Flow<Double> =
    events.map { it.target.unsafeCast<HTMLInputElement>().valueAsNumber }

/**
 * Gives you the new value as [Double] from the targeting [Element].
 */
fun DomListener<InputEvent, HTMLInputElement>.valuesAsNumber(): Flow<Double> =
    events.map { it.target.unsafeCast<HTMLInputElement>().valueAsNumber }

/**
 * Gives you the new value as [String] from the targeting [Element] when enter is pressed.
 */
fun DomListener<KeyboardEvent, HTMLInputElement>.enter(): Flow<String> =
    events.mapNotNull {
        if(it.keyCode == Keys.Enter.code) it.target.unsafeCast<HTMLInputElement>().value
        else null
    }

/**
 * Gives you the new value as [Double] from the targeting [Element] when enter is pressed.
 */
fun DomListener<KeyboardEvent, HTMLInputElement>.enterAsNumber(): Flow<Double> =
    events.mapNotNull {
        if(it.keyCode == Keys.Enter.code) it.target.unsafeCast<HTMLInputElement>().valueAsNumber
        else null
    }

/**
 * Gives you the new value as [String] from the targeting [Element].
 */
fun DomListener<Event, HTMLSelectElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLSelectElement>().value }

/**
 * Gives you the new value as [String] from the targeting [Element].
 */
fun DomListener<Event, HTMLTextAreaElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLTextAreaElement>().value }

/**
 * Gives you the new value as [String] from the targeting [Element].
 */
fun DomListener<KeyboardEvent, HTMLTextAreaElement>.enter(): Flow<String> =
    events.mapNotNull {
        if(it.keyCode == Keys.Enter.code) it.target.unsafeCast<HTMLTextAreaElement>().value
        else null
    }

/**
 * Gives you the [FileList] from the targeting [Element].
 */
fun DomListener<Event, HTMLInputElement>.files(): Flow<FileList?> =
    events.map { it.target.unsafeCast<HTMLInputElement>().files }

/**
 * Gives you the checked value as [Boolean] from the targeting [Element].
 */
fun DomListener<Event, HTMLInputElement>.states(): Flow<Boolean> =
    events.map { it.target.unsafeCast<HTMLInputElement>().checked }

/**
 * Gives you the selected index as [Int] from the targeting [Element].
 */
fun DomListener<Event, HTMLSelectElement>.selectedIndex(): Flow<Int> =
    events.map { it.target.unsafeCast<HTMLSelectElement>().selectedIndex }

/**
 * Gives you the selected value as [String] from the targeting [Element].
 */
fun DomListener<Event, HTMLSelectElement>.selectedValue(): Flow<String> =
    events.map {
        val select = it.target.unsafeCast<HTMLSelectElement>()
        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().value
    }

/**
 * Gives you the selected text as [String] from the targeting [Element].
 */
fun DomListener<Event, HTMLSelectElement>.selectedText(): Flow<String> =
    events.map {
        val select = it.target.unsafeCast<HTMLSelectElement>()
        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().text
    }

/**
 * Gives you the pressed key as [Key] from a [KeyboardEvent].
 */
fun <X : Element> DomListener<KeyboardEvent, X>.key(): Flow<Key> = events.map { Key.from(it) }