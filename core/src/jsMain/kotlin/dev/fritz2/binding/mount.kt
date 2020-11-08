package dev.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

/**
 * collects the values of a given [Flow] one by one.
 * Use this for data-types that represent a single (simple or complex) value.
 *
 * @param parentJob parent Job for starting a new coroutine
 * @param upstream returns the Flow that should be mounted at this point
 * @param set function which getting called when values are changing (rerender)
 */
inline fun <T> mountSingle(parentJob: Job, crossinline upstream: (Job) -> Flow<T>, crossinline set: suspend (T, T?) -> Unit) {
    (MainScope() + parentJob).launch {
        upstream(currentCoroutineContext()[Job]!!).scan(null) { last: T?, value: T ->
            set(value, last)
            value
        }.catch {
            cancel("error mounting", it)
        }.collect()
    }
}