package io.fritz2.binding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class SingleMountPoint<T>(upstream: Flow<T>) : CoroutineScope by MainScope() {
    init {
        launch {
            try {
               upstream.collect {
                    set(it, last)
                    last = it
                }
            } catch (t: Throwable) {
                this.cancel()
                // do nothing here but ignore the exception
            }
        }
    }

    private var last: T? = null

    abstract fun set(value: T, last: T?): Unit
}


abstract class MultiMountPoint<T>(upstream: Flow<Patch<T>>) : CoroutineScope by MainScope() {
    init {
        launch {
            try {
            upstream.collect {
                patch(it)
            }
            } catch (t: Throwable) {
                this.cancel()
            }
        }
    }

    abstract fun patch(patch: Patch<T>)
}
