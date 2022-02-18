package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Base-interface of the different types of handlers
 *
 * @property collect function describing how this handler collects a flow when called
 */
interface Handler<A> {
    val collect: (Flow<A>, Job) -> Unit

    /**
     * Calls this handler exactly once.
     *
     * @param data parameter forwarded to the handler
     */
    operator fun invoke(data: A) = this.collect(flowOf(data), Job())

    /**
     * Calls this handler exactly once.
     */
    operator fun invoke() = this.collect(flowOf(Unit.unsafeCast<A>()), Job())
}

/**
 * Defines, how to handle actions in your [Store]. Each Handler accepts actions of a defined type.
 * If your handler just needs the current value of the [Store] and no action, use [Unit].
 *
 * @param collect defines how to handle the values of the connected [Flow]
 */
value class SimpleHandler<A>(override inline val collect: (Flow<A>, Job) -> Unit) : Handler<A>

/**
 * An [EmittingHandler] is a special [Handler] that constitutes a new [Flow] by itself. You can emit values to this [Flow] from your code
 * and connect it to other [Handler]s on this or on other [Store]s. This way inter-store-communication is done in fritz2.
 *
 * @param collectWithChannel defines how to handle the values of the connected [Flow]
 * @property collect function defining how this [Handler] collects a [Flow] when connected using [handledBy]
 */
class EmittingHandler<A, E>(
    inline val collectWithChannel: (Flow<A>, FlowCollector<E>, Job) -> Unit,
    private val flow: MutableSharedFlow<E> = MutableSharedFlow()
) : Handler<A>, Flow<E> by flow {

    override val collect: (Flow<A>, Job) -> Unit = { upstream, job ->
        collectWithChannel(upstream, flow, job)
    }
}
