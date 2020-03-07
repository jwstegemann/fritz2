package io.fritz2.test

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.promise
import kotlin.browser.document


//abstract class TestRunner : CoroutineScope by GlobalScope {
    fun <T> runTest(block: suspend () -> T): dynamic = GlobalScope.promise {
    delay(100)
    block()
    delay(100)
}
//}

fun initDocument() {
    document.write("""
            <body id="target">
                Loading...
            </body>
        """.trimIndent())
}

fun <T> checkFlow(upstream: Flow<T>, numberOfUpdates: Int = 0, check: TestSingleMountPoint<T>.(Int, T, T?) -> Unit) = TestSingleMountPoint(upstream, check, numberOfUpdates)

class TestSingleMountPoint<T>(upstream: Flow<T>,
                              val check: TestSingleMountPoint<T>.(Int, T, T?) -> Unit,
                              val numberOfUpdates: Int,
                              val done: CompletableDeferred<Boolean> = CompletableDeferred()
) : SingleMountPoint<T>(upstream), CompletableDeferred<Boolean> by done {
    var count = 0;

    override fun set(value: T, last: T?) {
        check(count, value, last)
        count++;
        if (numberOfUpdates == count) done.complete(true)
    }
}

fun <T> checkFlow(upstream: Flow<Patch<T>>, numberOfUpdates: Int = 0, check: TestMultiMountPoint<T>.(Int, Patch<T>) -> Unit) = TestMultiMountPoint(upstream, check, numberOfUpdates)

class TestMultiMountPoint<T>(upstream: Flow<Patch<T>>,
                             val check: TestMultiMountPoint<T>.(Int, Patch<T>) -> Unit,
                             val numberOfUpdates: Int,
                             val done: CompletableDeferred<Boolean> = CompletableDeferred()
) : MultiMountPoint<T>(upstream), CompletableDeferred<Boolean> by done {
    private var count = 0;

    override fun patch(patch: Patch<T>) {
        check(count, patch)
        count++;
        if (numberOfUpdates == count) done.complete(true)
    }
}