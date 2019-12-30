package io.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class Var<T>(initValue: T,
             private val channel: ConflatedBroadcastChannel<T> = ConflatedBroadcastChannel<T>(initValue),
             private val flow: Flow<T> = channel.asFlow().distinctUntilChanged()) : Flow<T> by flow {

    suspend fun set(value: T): Unit = channel.send(value)

    fun value(): T = channel.value
}

@ExperimentalCoroutinesApi
//TODO: conflate necessary?
class Const<T> constructor(value: T, private val flow: Flow<T> = flowOf(value).conflate()): Flow<T> by flow

//TODO: Generic extensions?
@ExperimentalCoroutinesApi
operator fun String.not() = Const(this)

abstract class SingleMountPoint<T>(upstream: Flow<T>) {
    init {
        GlobalScope.launch {
            upstream.collect {
                set(it, last)
                last = it
            }
        }
    }

    private var last: T? = null

    abstract fun set(value: T, last: T?): Unit
}
