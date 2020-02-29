package io.fritz2.binding

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow

fun <T> checkFlow(upstream: Flow<T>, numberOfUpdates: Int = 0, check: TestSingleMountPoint<T>.(Int, T, T?) -> Unit) = TestSingleMountPoint(upstream, check, numberOfUpdates)

class TestSingleMountPoint<T>(upstream: Flow<T>,
                              val check: TestSingleMountPoint<T>.(Int, T, T?) -> Unit,
                              val numberOfUpdates: Int,
                              val done: CompletableDeferred<Boolean> = CompletableDeferred()) : SingleMountPoint<T>(upstream), CompletableDeferred<Boolean> by done {
    var count = 0;

    override fun set(value: T, last: T?) {
//        console.log("CALLED: $value from $last\n")
        check(count, value, last)
        count++;
        if (numberOfUpdates == count) done.complete(true)
    }
}

fun <T> checkFlow(upstream: Flow<Patch<T>>, numberOfUpdates: Int = 0, check: TestMultiMountPoint<T>.(Int, Patch<T>) -> Unit) = TestMultiMountPoint(upstream, check, numberOfUpdates)

class TestMultiMountPoint<T>(upstream: Flow<Patch<T>>,
                             val check: TestMultiMountPoint<T>.(Int, Patch<T>) -> Unit,
                             val numberOfUpdates: Int,
                             val done: CompletableDeferred<Boolean> = CompletableDeferred()) : MultiMountPoint<T>(upstream), CompletableDeferred<Boolean> by done {
    private var count = 0;

    override fun patch(patch: Patch<T>) {
        check(count, patch)
        count++;
        if (numberOfUpdates == count) done.complete(true)
    }
}