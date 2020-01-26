package io.fritz2.binding

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class SingleMountPoint<T>(upstream: Flow<T>) {
    init {
        //FIXME: GLobalScope, Context? onEach?
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


abstract class MultiMountPoint<T>(upstream: Flow<Patch<T>>) {
    init {
        //FIXME: GLobalScope, Context?
        GlobalScope.launch {
            upstream.collect {
                patch(it)
            }
        }
    }

    abstract fun patch(patch: Patch<T>)
}
