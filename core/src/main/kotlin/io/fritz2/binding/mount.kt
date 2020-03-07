package io.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class SingleMountPoint<T>(upstream: Flow<T>) : CoroutineScope by MainScope() {
    init {
        launch {
            upstream.collect {
                set(it, last)
                last = it
            }
        }
    }

    private var last: T? = null

    abstract fun set(value: T, last: T?): Unit
}


abstract class MultiMountPoint<T>(upstream: Flow<Patch<T>>) : CoroutineScope by MainScope() {
    init {
        launch {
            upstream.collect {
                patch(it)
            }
        }
    }

    abstract fun patch(patch: Patch<T>)
}
