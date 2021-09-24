package dev.fritz2.binding

import dev.fritz2.lenses.LensException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * collects the values of a given [Flow] one by one.
 * Use this for data-types that represent a single (simple or complex) value.
 *
 * @param parentJob parent Job for starting a new coroutine
 * @param upstream returns the Flow that should be mounted at this point
 * @param set function which getting called when values are changing (rerender)
 */
//FIXME: remove when asText is migrated
fun <T> mountSingle(parentJob: Job, upstream: Flow<T>, set: suspend (T, T?) -> Unit) {
    (MainScope() + parentJob).launch(start = CoroutineStart.UNDISPATCHED) {
        upstream.scan(null) { last: T?, value: T ->
            set(value, last)
            value
        }.catch {
            when (it) {
                is LensException -> {
                }
                else -> console.error(it)
            }
            // do not do anything here but canceling the coroutine, because this is an expected
            // behaviour when dealing with filtering, renderEach and idProvider
            cancel("error mounting", it)
        }.collect()
    }
}

inline fun <T> mountSimple(parentJob: Job, upstream: Flow<T>, crossinline collect: suspend (T) -> Unit) {
    (MainScope() + parentJob).launch(start = CoroutineStart.UNDISPATCHED) {
        upstream.onEach { collect(it) }.catch {
            when (it) {
                is LensException -> {
                }
                else -> console.error(it)
            }
            // do not do anything here but canceling the coroutine, because this is an expected
            // behaviour when dealing with filtering, renderEach and idProvider
            cancel("error mounting", it)
        }.collect()
    }
}
