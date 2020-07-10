package dev.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf

fun <A> dispatch(action: A, handler: Handler<A>, scope: CoroutineScope = GlobalScope, wait: Long? = null): Unit {
    scope.launch(start = CoroutineStart.ATOMIC) {
        if (wait != null) delay(wait)
        flowOf(action) handledBy handler
    }
}

fun dispatch(handler: Handler<Unit>, scope: CoroutineScope = GlobalScope, wait: Long? = null): Unit =
    dispatch<Unit>(Unit, handler, scope, wait)

fun <T, A> Store<T>.dispatch(action: A, handler: Handler<A>, wait: Long? = null): Unit =
    dispatch(action, handler, this, wait)

fun <T> Store<T>.dispatch(handler: Handler<Unit>, scope: CoroutineScope = GlobalScope, wait: Long? = null): Unit =
    dispatch<Unit>(Unit, handler, this, wait)


