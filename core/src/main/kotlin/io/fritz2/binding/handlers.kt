package io.fritz2.binding

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*

/**
 * A [Handler] defines, how to handle actions in your [Store]. Each Handler accepts actions of a defined type.
 * If your handler just needs the current value of the [Store] and no action, use [Unit].
 *
 * @param execute defines how to handle the values of the connected [Flow]
 */
class Handler<A>(inline val execute: (Flow<A>) -> Unit)

/**
 * bind a [Flow] of actions/events to an [Handler]
 *
 * @param handler [Handler] that will be called for each action/event on the [Flow]
 * @receiver [Flow] of action/events to bind to an [Handler]
 */
infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.execute(this)

/**
 * An [EmittingHandler] is a special [Handler] that constitutes a new [Flow] by itself. You can emit values to this [Flow] from your code
 * and connect it to other [Handler]s on this or on other [Store]s. This way inter-store-communication is done in fritz2.
 *
 * @param bufferSize number of values of the new [Flow] to buffer
 * @param execute defines how to handle the values of the connected [Flow]
 */
class EmittingHandler<A, E>(bufferSize: Int, inline val execute: (Flow<A>, SendChannel<E>) -> Unit) : Flow<E> {

    internal val channel = BroadcastChannel<E>(bufferSize)

    @InternalCoroutinesApi
    /**
     * implementing the [Flow]-interface
     */
    override suspend fun collect(collector: FlowCollector<E>) {
        collector.emitAll(channel.asFlow())
    }
}

/**
 * bind a [Flow] of actions/events to an [EmittingHandler]
 *
 * @param handler [EmittingHandler] that will be called for each action/event on the [Flow]
 * @receiver [Flow] of action/events to bind to an [EmittingHandler]
 */
infix fun <A, E> Flow<A>.handledBy(handler: EmittingHandler<A, E>) = handler.execute(this, handler.channel)


//FIXME: we need an Applicator, that can access the actual model
class Applicator<A, X>(inline val execute: suspend (A) -> Flow<X>) {
    infix fun andThen(nextHandler: Handler<X>) = Handler<A> {
        nextHandler.execute(it.flatMapConcat(this.execute))
    }

    infix fun <E> andThen(nextHandler: EmittingHandler<X, E>) = Handler<A> {
        nextHandler.execute(it.flatMapConcat(this.execute), nextHandler.channel)
    }

    infix fun <Y> andThen(nextApplicator: Applicator<X, Y>): Applicator<A, Y> = Applicator {
        execute(it).flatMapConcat(nextApplicator.execute)
    }
}
