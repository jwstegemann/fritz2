package dev.fritz2.binding

import dev.fritz2.dom.DomListener
import dev.fritz2.dom.WindowListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.events.Event

interface WithJob {
    val job: Job

    /**
     * Connects a [Flow] to a [Handler].
     *
     * @param handler [Handler] that will be called for each action/event on the [Flow]
     * @receiver [Flow] of action/events to bind to an [Handler]
     */
    infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.collect(this, job)

    /**
     * Connects [Event]s to a [Handler].
     *
     * @receiver [DomListener] which contains the [Event]
     * @param handler that will handle the fired [Event]
     */
    infix fun <E : Event, X : Element> DomListener<E, X>.handledBy(handler: Handler<Unit>) =
        handler.collect(this.events.map { }, job)

    /**
     * Connects [Event]s to a [Handler].
     *
     * @receiver [WindowListener] which contains the [Event]
     * @param handler that will handle the fired [Event]
     */
    infix fun <E : Event> WindowListener<E>.handledBy(handler: Handler<Unit>) =
        handler.collect(this.events.map { }, job)
}