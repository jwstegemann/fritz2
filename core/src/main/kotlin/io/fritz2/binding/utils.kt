package io.fritz2.binding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn

/**
 * convenience-method to create a never changing [Flow] just to be more readable in templates, etc.
 */
fun <T> const(value: T): Flow<T> = flowOf(value)


/**
 * If a [Store]'s data-[Flow] is never mounted, use this method to start the updating of derived values.
 */
fun <T> Flow<T>.watch(scope: CoroutineScope = MainScope()): Job = this.catch {}.launchIn(scope)
