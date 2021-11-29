package dev.fritz2.dom

import dev.fritz2.dom.html.Key
import dev.fritz2.dom.html.Keys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.files.FileList

/**
 * Handles a Flow of [Event]s
 */
interface Listener<E : Event> {
    val events: Flow<E>
    fun preventDefault(): Listener<E>
    fun stopImmediatePropagation(): Listener<E>
    fun stopPropagation(): Listener<E>
    fun composedPath(): Flow<Array<EventTarget>> = events.map { it.composedPath() }
    fun <R> map(transform: suspend (E) -> R) = events.map(transform)
}

/**
 * Handles a Flow of Window [Event]s
 */
class WindowListener<E : Event>(override val events: Flow<E>) : Listener<E> {
    override fun preventDefault(): WindowListener<E> =
        WindowListener(events.map { it.preventDefault(); it })

    override fun stopImmediatePropagation(): WindowListener<E> =
        WindowListener(events.map { it.stopImmediatePropagation(); it })

    override fun stopPropagation(): WindowListener<E> =
        WindowListener(events.map { it.stopPropagation(); it })
}

/**
 * Handles a Flow of Dom [Event]s.
 */
class DomListener<E : Event, out X : Element>(override val events: Flow<E>) : Listener<E> {
    override fun preventDefault(): DomListener<E, X> =
        DomListener(events.map { it.preventDefault(); it })

    override fun stopImmediatePropagation(): DomListener<E, X> =
        DomListener(events.map { it.stopImmediatePropagation(); it })

    override fun stopPropagation(): DomListener<E, X> =
        DomListener(events.map { it.stopPropagation(); it })
}

/**
 * Gives you the new value as [String] from the targeting [Element].
 */
fun DomListener<Event, HTMLFieldSetElement>.values(): Flow<String> =
    events.map { it.target.unsafeCast<HTMLInputElement>().value }

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
        if (Key(it) == Keys.Enter) it.target.unsafeCast<HTMLInputElement>().value
        else null
    }

/**
 * Gives you the new value as [Double] from the targeting [Element] when enter is pressed.
 */
fun DomListener<KeyboardEvent, HTMLInputElement>.enterAsNumber(): Flow<Double> =
    events.mapNotNull {
        if (Key(it) == Keys.Enter) it.target.unsafeCast<HTMLInputElement>().valueAsNumber
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
        if (Key(it) == Keys.Enter) it.target.unsafeCast<HTMLTextAreaElement>().value
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
fun <X : Element> DomListener<KeyboardEvent, X>.key(): Flow<Key> = events.map { Key(it) }

/**
 * Gives you the pressed key as [Key] combined with the [KeyboardEvent] filtered by a given set of keys.
 * All other events from other keys will be dropped!
 *
 * This is very helpful if the event bubbling should be stopped for example, as the filtering has to be done before
 * and the bubbling should in most cases only be stopped for the handled keys and not the other ones!
 *
 * @param keys a set with all keys that should be handled
 */
fun <X : Element> DomListener<KeyboardEvent, X>.keys(keys: Set<Key>) =
    events.map { Key(it) to it }.filter { keys.contains(it.first) }

/**
 * Gives you the pressed key as [Key] combined with the [KeyboardEvent] filtered by arbitrary given keys.
 * All other events from other keys will be dropped!
 *
 * This is very helpful if the event bubbling should be stopped for example, as the filtering has to be done before
 * and the bubbling should in most cases only be stopped for the handled keys and not the other ones!
 *
 * @param keys an arbitrary amount of keys which should be handled
 */
fun <X : Element> DomListener<KeyboardEvent, X>.keys(vararg keys: Key) = this.keys(keys.toSet())

/**
 * Gives you the pressed key as [Key] combined with the [KeyboardEvent] filtered by exactly one key.
 * All other events from other keys will be dropped!
 *
 * This is very helpful if the event bubbling should be stopped for example, as the filtering has to be done before
 * and the bubbling should in most cases only be stopped for the handled key and for other ones!
 *
 * @param key the key to be handled
 */
fun <X : Element> DomListener<KeyboardEvent, X>.keys(key: Key) = events.map { Key(it) to it }.filter { it.first == key }

/**
 * Gives you the pressed key as [Key] from a [KeyboardEvent].
 */
fun WindowListener<KeyboardEvent>.key(): Flow<Key> = events.map { Key(it) }

/**
 * Merges multiple [DomListener] like the analog method on [Flow]s
 *
 * @param listener the [DomListener] to merge
 */
fun merge(vararg listener: DomListener<*, *>): Flow<Unit> =
    kotlinx.coroutines.flow.merge(*listener.map { it.events }.toTypedArray()).map {}
