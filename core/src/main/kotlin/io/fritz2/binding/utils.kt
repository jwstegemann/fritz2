package io.fritz2.binding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlin.random.Random
import kotlin.random.nextUBytes

fun <T> const(value: T): Flow<T> = flowOf(value)


fun <T> Flow<T>.watch(scope: CoroutineScope = MainScope()): Job = this.catch {}.launchIn(scope)

fun uniqueId() = Random.nextUBytes(16).contentHashCode().toString()
