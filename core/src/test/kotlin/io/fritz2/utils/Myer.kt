package io.fritz2.utils

import io.fritz2.binding.Patch
import io.fritz2.test.runTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.fold
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


class MyerTests {

    private fun <T> MutableList<T>.applyPatch(patch: Patch<T>): MutableList<T> {
        when (patch) {
            is Patch.Insert<T> -> {
                add(patch.index, patch.element)
            }
            is Patch.Delete<T> -> {
                for (i in patch.start until (patch.start + patch.count)) {
                    console.log("bin da")
                }
            }
        }
        return this
    }

    @ExperimentalTime
    @Test
    fun tryIt() = runTest {

        val old = mutableListOf<String>("a", "b", "c", "d")
        val new = mutableListOf<String>("f", "a", "b", "g", "c", "d", "e")

        /*
         * Mein eigener Myer
         */

        with(measureTime {
            val patches = Myer.diff(old, new) { a, b -> a == b }

            patches.collect { patch ->
                console.log("applying patch: $patch \n")
                old.applyPatch(patch)
                console.log("... result: $old \n")
            }

            val builtList = patches.fold(old, { acc: MutableList<String>, value: Patch<String> ->
                acc.applyPatch(value)
            })

            assertEquals(new, builtList)

        }.inMilliseconds) {
            console.log("took $this ms")
        }

    }

}