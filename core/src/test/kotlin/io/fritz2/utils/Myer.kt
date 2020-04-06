package io.fritz2.utils

import io.fritz2.binding.Patch
import io.fritz2.test.runTest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlin.js.Date
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


class MyerTests {

    private fun <T> MutableList<T>.applyPatch(patch: Patch<T>): Unit {
        when (patch) {
            is Patch.Insert<T> -> {
                add(patch.index, patch.element)
            }
            is Patch.Delete<T> -> {
                for (i in patch.start until (patch.start + patch.count)) {
                    removeAt(i)
                }
            }
        }
    }


    fun createTestCase(): Pair<MutableList<String>, MutableList<String>> {
        val letters = listOf("a", "b", "c", "d", "e", "f", "g")
        val maxStartLength = 20
        val maxOperations = 10

        val rand = Random(Date.now().toLong())

        val old = mutableListOf<String>()
        for (i in 0 until rand.nextInt(maxStartLength) + 1) {
            old.add(letters[rand.nextInt(letters.size)])
        }

        val new = mutableListOf<String>()
        for (e in old) new.add(e)

        for (n in 0 until rand.nextInt(maxOperations) + 1) {
            when (rand.nextInt(2)) {
                //insert
                0 -> {
                    val index = rand.nextInt(new.size + 1)
                    val element = letters[rand.nextInt(letters.size)]
                    new.add(index, element)
                }
                //delete
                1 -> {
                    if (new.size > 0) {
                        val index = rand.nextInt(new.size)
                        new.removeAt(index)
                    }
                }
            }
        }

        return Pair(old, new)
    }

    @ExperimentalTime
    suspend fun runTestCase(): Unit {

        val (old, new) = createTestCase()
        console.log("old: $old \n")
        console.log("new: $new \n")

        with(measureTime {
            val patches = Myer.diff(old, new) { a, b -> a == b }

            patches.map { patch ->
                console.log("applying patch: $patch \n")
                old.applyPatch(patch)
                console.log("... result: $old \n")
            }.reduce { _, value -> value }

            assertEquals(new, old)

        }.inMilliseconds) {
            console.log("took $this ms \n")
        }
    }

    @ExperimentalTime
    @Test
    fun testMyer() = runTest {
        repeat(20) {
            runTestCase()
        }
    }

}