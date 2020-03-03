package io.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class Const<T>(value: T, private val flow: Flow<T> = flowOf(value).conflate()): Flow<T> by flow

//TODO: adding Consts<List<T>>


