package io.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Gives a constant [Flow] from a given value.
 */
@ExperimentalCoroutinesApi
class Const<T>(value: T, private val flow: Flow<T> = flowOf(value).conflate()): Flow<T> by flow