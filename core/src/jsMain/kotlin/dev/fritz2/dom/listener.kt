package dev.fritz2.dom

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget

///**
// * Handles a Flow of [Event]s
// */
//interface Listener<E : Event> {
//    val events: Flow<E>
//}

fun <E: Event> Flow<E>.preventDefault(): Flow<E> = this.map { it.preventDefault(); it }
fun <E: Event> Flow<E>.stopImmediatePropagation(): Flow<E> = this.map { it.stopImmediatePropagation(); it }
fun <E: Event> Flow<E>.stopPropagation(): Flow<E> = this.map { it.stopPropagation(); it }
fun <E: Event> Flow<E>.composedPath(): Flow<Array<EventTarget>> = this.map { it.composedPath() }

///**
// * Handles a Flow of Window [Event]s
// */
//class WindowListener<E : Event>(override val events: Flow<E>) : Listener<E>, Flow<E> by events
//
///**
// * Handles a Flow of Dom [Event]s.
// */
//class DomListener<E : Event, out X : Element>(override val events: Flow<E>) : Listener<E>, Flow<E> by events
//
///**
// * Gives you the new value as [String] from the targeting [Element].
// */
//fun DomListener<*, HTMLFieldSetElement>.values(): Flow<String> =
//    events.map { it.target.unsafeCast<HTMLInputElement>().value }
//
///**
// * Gives you the new value as [String] from the targeting [Element].
// */
//fun DomListener<*, HTMLInputElement>.values(): Flow<String> =
//    events.map { it.target.unsafeCast<HTMLInputElement>().value }
//
///**
// * Gives you the new value as [Double] from the targeting [Element].
// */
//fun DomListener<*, HTMLInputElement>.valuesAsNumber(): Flow<Double> =
//    events.map { it.target.unsafeCast<HTMLInputElement>().valueAsNumber }
//
///**
// * Gives you the new value as [String] from the targeting [Element].
// */
//fun DomListener<*, HTMLSelectElement>.values(): Flow<String> =
//    events.map { it.target.unsafeCast<HTMLSelectElement>().value }
//
///**
// * Gives you the new value as [String] from the targeting [Element].
// */
//fun DomListener<*, HTMLTextAreaElement>.values(): Flow<String> =
//    events.map { it.target.unsafeCast<HTMLTextAreaElement>().value }
//
///**
// * Gives you the [FileList] from the targeting [Element].
// */
//fun DomListener<*, HTMLInputElement>.files(): Flow<FileList?> =
//    events.map { it.target.unsafeCast<HTMLInputElement>().files }
//
///**
// * Gives you the checked value as [Boolean] from the targeting [Element].
// */
//fun DomListener<*, HTMLInputElement>.states(): Flow<Boolean> =
//    events.map { it.target.unsafeCast<HTMLInputElement>().checked }
//
///**
// * Gives you the selected index as [Int] from the targeting [Element].
// */
//fun DomListener<*, HTMLSelectElement>.selectedIndex(): Flow<Int> =
//    events.map { it.target.unsafeCast<HTMLSelectElement>().selectedIndex }
//
///**
// * Gives you the selected value as [String] from the targeting [Element].
// */
//fun DomListener<*, HTMLSelectElement>.selectedValue(): Flow<String> =
//    events.map {
//        val select = it.target.unsafeCast<HTMLSelectElement>()
//        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().value
//    }
//
///**
// * Gives you the selected text as [String] from the targeting [Element].
// */
//fun DomListener<*, HTMLSelectElement>.selectedText(): Flow<String> =
//    events.map {
//        val select = it.target.unsafeCast<HTMLSelectElement>()
//        select.options[select.selectedIndex].unsafeCast<HTMLOptionElement>().text
//    }

/**
 * Merges multiple [Event]-flows
 *
 * @param events the [Flow]s to merge
 */
fun merge(vararg events: Flow<*>): Flow<Unit> =
    kotlinx.coroutines.flow.merge(*events.map { it }.toTypedArray()).map {}
