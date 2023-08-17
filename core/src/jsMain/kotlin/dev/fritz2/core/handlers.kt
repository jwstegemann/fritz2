package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * This [Flow] implementation represents a flow that emits exactly one value during its lifetime.
 *
 * @param value the value to emit on the flow
 */
class OnlyOnceFlow<T>(private val value: T) : Flow<T> {
    private var collected = false

    override suspend fun collect(collector: FlowCollector<T>) {
        if (!collected) {
            collected = true
            collector.emit(value)
        }
    }
}

/**
 * This factory function creates an [OnlyOnceFlow].
 *
 * @param value the value to emit on the flow
 */
fun <T> flowOnceOf(value: T) = OnlyOnceFlow(value)

/**
 * Base-interface of the different types of handlers
 *
 * @property process function describing how this handler collects a [Flow] when called
 */
interface Handler<A> {
    val process: (Flow<A>, Job) -> Unit
}

/**
 * Defines, how to handle actions in your [Store]. Each Handler accepts actions of a defined type.
 * If your handler just needs the current value of the [Store] and no action, use [Unit].
 *
 * @param process defines how to handle the values of the connected [Flow]
 */
value class SimpleHandler<A>(override inline val process: (Flow<A>, Job) -> Unit) : Handler<A>

/**
 * An [EmittingHandler] is a special [Handler] that constitutes a new [Flow] by itself. You can emit values to this [Flow] from your code
 * and connect it to other [Handler]s on this or on other [Store]s. This way inter-store-communication is done in fritz2.
 *
 * @param collectWithChannel defines how to handle the values of the connected [Flow]
 * @property process function defining how this [Handler] collects a [Flow] when connected using [handledBy]
 */
class EmittingHandler<A, E>(
    inline val collectWithChannel: (Flow<A>, FlowCollector<E>, Job) -> Unit,
    private val flow: MutableSharedFlow<E> = MutableSharedFlow(),
) : Handler<A>, Flow<E> by flow {

    override val process: (Flow<A>, Job) -> Unit = { upstream, job ->
        collectWithChannel(upstream, flow, job)
    }
}
