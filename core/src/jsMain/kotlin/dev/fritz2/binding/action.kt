package dev.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf


/**
 * factory-function to create an [Action]
 *
 * @param wait optional time to wait before the action is dispatched
 */
fun <A> action(data: A, wait: Long? = null) = Action(data, GlobalScope, wait)

/**
 * factory-function to create an [Action]
 *
 * @receiver the [Store] that's [CoroutineScope] is used to dispatch the [Action]
 * @param wait optional time to wait before the action is dispatched
 */
fun <T, A> Store<T>.action(data: A, wait: Long? = null) = Action(data, MainScope(), wait)

/**
 * factory-function to create an [Action] without data
 *
 * @param wait optional time to wait before the action is dispatched
 */
fun action(wait: Long? = null) = Action(Unit, GlobalScope, wait)

/**
 * factory-function to create an [Action] without data
 *
 * @receiver the [Store] that's [CoroutineScope] is used to dispatch the [Action]
 * @param wait optional time to wait before the action is dispatched
 */
fun <T> Store<T>.action(wait: Long? = null) = Action(Unit, MainScope(), wait)

/**
 * represents some disputable that can be processed by a [Store]'s [Handler]
 *
 * @property data payload of this action
 * @property scope [CoroutineScope] the action will run in
 * @property wait ms to wait before the action is fired
 */
class Action<T>(val data: T, val scope: CoroutineScope, val wait: Long?)

/**
 * Connects a single [Action] to a [Handler].
 *
 * @receiver the [Action] that will be bound
 * @param handler the [Handler] that will process the [Action]'s data
 */
infix fun <A> Action<A>.handledBy(handler: Handler<A>) {
    this.also { action ->
        action.scope.launch(start = CoroutineStart.ATOMIC) {
            if (action.wait != null) delay(action.wait)
            flowOf(action.data) handledBy handler
        }
    }
}

