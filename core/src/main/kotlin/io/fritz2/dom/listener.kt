package io.fritz2.dom

import io.fritz2.dom.html.Key
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
 * [Action] contains the fired [Event] and targeting [Element]
 */
data class Action<E: Event, X: Element>(val event: E, val target: X)

/**
 * [Listener] handles a Flow of [Action]s and gives
 * the [Event] with [events] as Flow or
 * the targeting [Element] with [targets] also as Flow back.
 * If you don't need either the [Event] or [Element] you can call the [Listener]
 * directly (e.g. `clicks()`) to get an Flow of [Unit] instead.
 */
@FlowPreview
@ExperimentalCoroutinesApi
inline class Listener<E: Event, X: Element>(val actions: Flow<Action<E, X>>) {
    operator fun invoke(): Flow<Unit> = actions.map { Unit }
    fun events(): Flow<E> = actions.map { it.event }
    fun targets(): Flow<X> = actions.map { it.target }
}

/**
 * Gives you the new value as [String] from the targeting [Element]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun Listener<Event, HTMLInputElement>.value(): Flow<String> = targets().map { it.value }
/**
 * Gives you the new value as [String] from the targeting [Element]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun Listener<Event, HTMLSelectElement>.value(): Flow<String> = targets().map { it.value }
/**
 * Gives you the new value as [String] from the targeting [Element]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun Listener<Event, HTMLTextAreaElement>.value(): Flow<String> = targets().map { it.value }

/**
 * Gives you the [FileList] from the targeting [Element]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun Listener<Event, HTMLInputElement>.fileList(): Flow<FileList?> = targets().map { it.files }

/**
 * Gives you the checked value as [Boolean] from the targeting [Element]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun Listener<Event, HTMLInputElement>.checked(): Flow<Boolean> = targets().map { it.checked }

/**
 * Gives you the selected index as [Int] from the targeting [Element]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun Listener<Event, HTMLSelectElement>.selectedIndex(): Flow<Int> = targets().map { it.selectedIndex }

/**
 * Gives you the pressed key as [Key] from a [KeyboardEvent]
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun <X: Element> Listener<KeyboardEvent, X>.key(): Flow<Key> = events().map { Key.from(it) }