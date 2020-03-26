package io.fritz2.binding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn

fun <T> const(value: T): Flow<T> = flowOf(value)

fun <T> Flow<T>.watch(scope: CoroutineScope = MainScope()): Job = launchIn(scope)
