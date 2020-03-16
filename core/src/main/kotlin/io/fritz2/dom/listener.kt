package io.fritz2.dom

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event

@FlowPreview
@ExperimentalCoroutinesApi
class Listener<E: Event, X: Element>(val event: E, val element: X)

@FlowPreview
@ExperimentalCoroutinesApi
fun <E: Event> Flow<Listener<E, Element>>.pure(): Flow<E> = map { it.event }

@FlowPreview
@ExperimentalCoroutinesApi
fun <X: Element> Flow<Listener<Event, X>>.target(): Flow<X> = map { it.element }

@FlowPreview
@ExperimentalCoroutinesApi
fun Flow<Listener<Event, HTMLInputElement>>.value(): Flow<String> = map { it.element.value }
@FlowPreview
@ExperimentalCoroutinesApi
fun Flow<Listener<Event, HTMLSelectElement>>.value(): Flow<String> = map { it.element.value }
@FlowPreview
@ExperimentalCoroutinesApi
fun Flow<Listener<Event, HTMLTextAreaElement>>.value(): Flow<String> = map { it.element.value }

//TODO add more methods here