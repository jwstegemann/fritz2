package dev.fritz2.binding

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface WithJob {
    val job: Job

    /**
     * Connects a [Flow] to a [Handler].
     *
     * @param handler [Handler] that will be called for each action/event on the [Flow]
     * @receiver [Flow] of action/events to bind to an [Handler]
     */
    infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.collect(this, job)
}