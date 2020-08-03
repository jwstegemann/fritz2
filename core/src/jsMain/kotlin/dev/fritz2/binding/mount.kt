package dev.fritz2.binding

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A [SingleMountPoint] collects the values of a given [Flow] one by one. Use this for data-types that represent a single (simple or complex) value.
 *
 * @param upstream the Flow that should be mounted at this point.
 */
abstract class SingleMountPoint<T>(upstream: Flow<T>) {
    init {
        upstream.onEach {
            set(it, last)
            last = it
        }.launchIn(MainScope())
    }

    private var last: T? = null

    /**
     * this method is called for each new value on the upstream-[Flow]
     *
     * @param value new value on the [Flow]
     * @param last old value of the [Flow] (before the last update)
     */
    abstract fun set(value: T, last: T?): Unit
}

/**
 * A [MultiMountPoint] collects the values of a given [Flow] one by one. Use this for data-types that represent a sequence of values.
 *
 * @param upstream the Flow that should be mounted at this point.
 */
abstract class MultiMountPoint<T>(upstream: Flow<Patch<T>>) {
    init {
        upstream.onEach { patch(it) }.launchIn(MainScope())
    }

    /**
     * this method is called for each new value on the upstream-[Flow]
     *
     * @param patch a [Patch] describing the changes made by the last update.
     */
    abstract fun patch(patch: Patch<T>)
}
