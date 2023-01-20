package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import org.w3c.dom.events.Event

/**
 * Prints [Exception] to error-[console] by ignoring [CollectionLensGetException].
 */
internal fun printErrorIgnoreLensException(cause: Throwable) {
    when(cause) {
        is CollectionLensGetException -> {}
        else -> console.error(cause)
    }
}

/**
 * Marks a class that it has a [Job] to start coroutines with.
 */
interface WithJob {

    /**
     * [Job] for launching coroutines in.
     */
    val job: Job

    /**
     * Default error handler printing the error to console.
     *
     * @param cause Throwable to handle
     */
    fun errorHandler(cause: Throwable): Unit = printErrorIgnoreLensException(cause)

    /**
     * Connects a [Flow] to a [Handler].
     *
     * @param handler [Handler] that will be called for each action/event on the [Flow]
     * @receiver [Flow] of action/events to bind to a [Handler]
     */
    infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.process(this, job)

    /**
     * Connects a [Flow] to a suspendable [execute] function.
     *
     * @param execute function that will be called for each action/event on the [Flow]
     * @receiver [Flow] of action/events to bind to
     */
    infix fun <A> Flow<A>.handledBy(execute: suspend (A) -> Unit) =
        this.onEach { execute(it) }.catch { errorHandler(it) }.launchIn(MainScope() + job)


    /**
     * Connects [Event]s to a [Handler].
     *
     * @receiver [Flow] which contains the [Event]
     * @param handler that will handle the fired [Event]
     */
    infix fun <E : Event> Flow<E>.handledBy(handler: Handler<Unit>) =
        handler.process(this.map { }, job)

    /**
     * Connects a [Flow] to a suspendable [execute] function.
     *
     * @receiver [Flow] which contains the [Event]
     * @param execute function that will handle the fired [Event]
     */
    infix fun <E : Event> Flow<E>.handledBy(execute: suspend (E) -> Unit) =
        this.onEach { execute(it) }.catch { errorHandler(it) }.launchIn(MainScope() + job)

    /**
     * Calls this handler exactly once.
     *
     * @param data parameter forwarded to the handler
     */
    operator fun <A> Handler<A>.invoke(data: A) = this.process(flowOnceOf(data), job)

    /**
     * Calls this handler exactly once.
     */
    operator fun Handler<Unit>.invoke() = this.process(flowOnceOf(Unit), job)

}

/**
 * Connects a [Flow] to a [Handler].
 *
 * @param handler [Handler] that will be called for each action/event on the [Flow]
 * @receiver [Flow] of action/events to bind to an [Handler]
 */
infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.process(this, Job())

/**
 * Connects a [Flow] to a suspendable [execute] function.
 *
 * @param execute function that will be called for each action/event on the [Flow]
 * @receiver [Flow] of action/events to bind to an [Handler]
 */
infix fun <A> Flow<A>.handledBy(execute: suspend (A) -> Unit) =
    this.onEach { execute(it) }.catch { printErrorIgnoreLensException(it) }.launchIn(MainScope() + Job())