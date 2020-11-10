package dev.fritz2.binding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn

/**
 * If a data-[Flow] is never mounted, use this method to start the updating of derived values.
 */
fun <T> Flow<T>.watch(scope: CoroutineScope = MainScope()) { this.catch {}.launchIn(scope) }

/**
 * If a [Store]'s data-[Flow] is never mounted, use this method to start the updating of derived values.
 */
fun <T> Store<T>.watch(scope: CoroutineScope = MainScope()): Store<T> = this.also { data.catch {}.launchIn(scope) }