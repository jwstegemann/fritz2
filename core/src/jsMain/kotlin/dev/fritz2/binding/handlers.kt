package dev.fritz2.binding

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll

interface Handler<A> {
    val collect: (Flow<A>) -> Unit
}

/**
 * A [SimpleHandler] defines, how to handle actions in your [Store]. Each Handler accepts actions of a defined type.
 * If your handler just needs the current value of the [Store] and no action, use [Unit].
 *
 * @param collect defines how to handle the values of the connected [Flow]
 */
class SimpleHandler<A>(override inline val collect: (Flow<A>) -> Unit) : Handler<A>

/**
 * bind a [Flow] of actions/events to an [Handler]
 *
 * @param handler [Handler] that will be called for each action/event on the [Flow]
 * @receiver [Flow] of action/events to bind to an [Handler]
 */
infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.collect(this)

/**
 * An [OfferingHandler] is a special [SimpleHandler] that constitutes a new [Flow] by itself. You can emit values to this [Flow] from your code
 * and connect it to other [SimpleHandler]s on this or on other [Store]s. This way inter-store-communication is done in fritz2.
 *
 * @param bufferSize number of values of the new [Flow] to buffer
 * @param collectWithChannel defines how to handle the values of the connected [Flow]
 */
class OfferingHandler<A, E>(bufferSize: Int, inline val collectWithChannel: (Flow<A>, SendChannel<E>) -> Unit) :
    Handler<A>, Flow<E> {

    private val channel = BroadcastChannel<E>(bufferSize)

    override val collect: (Flow<A>) -> Unit = {
        collectWithChannel(it, channel)
    }

    @InternalCoroutinesApi
    /**
     * implementing the [Flow]-interface
     */
    override suspend fun collect(collector: FlowCollector<E>) {
        collector.emitAll(channel.asFlow())
    }
}
