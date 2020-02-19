package io.fritz2.binding

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test

fun <T> checkFlow(upstream: Flow<T>, numberOfUpdates: Int = 0, check: TestSingleMountPoint<T>.(Int, T, T?) -> Unit) = TestSingleMountPoint(upstream, check, numberOfUpdates)

class TestSingleMountPoint<T>(upstream: Flow<T>,
                              val check: TestSingleMountPoint<T>.(Int, T, T?) -> Unit,
                              val numberOfUpdates: Int,
                              val done: CompletableDeferred<Boolean> = CompletableDeferred<Boolean>()) : SingleMountPoint<T>(upstream), CompletableDeferred<Boolean> by done {
    var count = 0;

    override fun set(value: T, last: T?) {
        check(count, value, last)
        count++;
        if (numberOfUpdates == count) done.complete(true)
    }
}