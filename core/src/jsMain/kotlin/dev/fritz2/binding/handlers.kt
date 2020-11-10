package dev.fritz2.binding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Base-interface of the different types of handlers
 *
 * @property collect function describing how this handler collects a flow when connected by [handledBy]
 */
interface Handler<A> {
    val collect: (Flow<A>) -> Unit
    //TODO: comment
    operator fun invoke(data: A) = this.collect(flowOf(data))
}

//TODO: comment
operator fun Handler<Unit>.invoke() = this.collect(flowOf(Unit))

/**
 * Defines, how to handle actions in your [Store]. Each Handler accepts actions of a defined type.
 * If your handler just needs the current value of the [Store] and no action, use [Unit].
 *
 * @param collect defines how to handle the values of the connected [Flow]
 */
class SimpleHandler<A>(override inline val collect: (Flow<A>) -> Unit) : Handler<A>

/**
 * Connects a [Flow] to a [Handler].
 *
 * @param handler [Handler] that will be called for each action/event on the [Flow]
 * @receiver [Flow] of action/events to bind to an [Handler]
 */
//TODO: move to RenderContext, forward Job
infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = handler.collect(this)

/**
 * An [OfferingHandler] is a special [Handler] that constitutes a new [Flow] by itself. You can emit values to this [Flow] from your code
 * and connect it to other [Handler]s on this or on other [Store]s. This way inter-store-communication is done in fritz2.
 *
 * @param collectWithChannel defines how to handle the values of the connected [Flow]
 * @property collect function defining how this [Handler] collects a [Flow] when connected using [handledBy]
 */
class OfferingHandler<A, E>(
    inline val collectWithChannel: (Flow<A>, FlowCollector<E>) -> Unit,
    private val flow: MutableSharedFlow<E> = MutableSharedFlow()
) : Handler<A>, Flow<E> by flow {

    override val collect: (Flow<A>) -> Unit = {
        collectWithChannel(it, flow)
    }
}
