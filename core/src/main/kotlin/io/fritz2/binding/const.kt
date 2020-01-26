package io.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
//TODO: conflate necessary?
class Const<T>(value: T, private val flow: Flow<T> = flowOf(value).conflate()): Flow<T> by flow

@ExperimentalCoroutinesApi
operator fun <T> T.not() = Const(this)


