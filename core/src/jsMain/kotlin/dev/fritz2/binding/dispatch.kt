package dev.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf


/**
 * dispatches a given action to a given handler
 *
 * @param action an instance of the action to dispatch
 * @param handler that should handle the action
 * @param scope optional [CoroutineScope] the action is launched in
 * @param wait optional time to wait before the action is dispatched
 */
fun <A> dispatch(action: A, handler: Handler<A>, scope: CoroutineScope = GlobalScope, wait: Long? = null): Unit {
    scope.launch(start = CoroutineStart.ATOMIC) {
        if (wait != null) delay(wait)
        flowOf(action) handledBy handler
    }
}

/**
 * convenience function to dispatch a action without content
 *
 * @param handler that should handle the action
 * @param scope optional [CoroutineScope] the action is launched in
 * @param wait optional time to wait before the action is dispatched
 */
fun dispatch(handler: Handler<Unit>, scope: CoroutineScope = GlobalScope, wait: Long? = null): Unit =
    dispatch<Unit>(Unit, handler, scope, wait)

/**
 * dispatches a given action to a given handler within the scope of the [Store]
 *
 * @param action an instance of the action to dispatch
 * @param handler that should handle the action
 * @param wait optional time to wait before the action is dispatched
 */
fun <T, A> Store<T>.dispatch(action: A, handler: Handler<A>, wait: Long? = null): Unit =
    dispatch(action, handler, this, wait)

/**
 * convenience function to dispatch an action without content within the scope of the [Store]
 *
 * @param handler that should handle the action
 * @param wait optional time to wait before the action is dispatched
 */
fun <T> Store<T>.dispatch(handler: Handler<Unit>, wait: Long? = null): Unit =
    dispatch<Unit>(Unit, handler, this, wait)

