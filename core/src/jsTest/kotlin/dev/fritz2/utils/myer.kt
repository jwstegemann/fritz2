package dev.fritz2.utils

import dev.fritz2.binding.Patch
import dev.fritz2.test.runTest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlin.js.Date
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
class MyerTests {

    private fun <T> MutableList<T>.applyPatch(patch: Patch<T>) {
        when (patch) {
            is Patch.Insert<T> -> {
                add(patch.index, patch.element)
            }
            is Patch.InsertMany<T> -> {
                for (element in patch.elements) {
                    add(patch.index, element)
                }
            }
            is Patch.Delete<T> -> {
                for (i in (patch.start + patch.count - 1) downTo patch.start) {
                    removeAt(i)
                }
            }
            is Patch.Move<T> -> {
                val element = get(patch.from)
                removeAt(patch.from)
                if (patch.from > patch.to) add(patch.to, element)
                else add(patch.to - 1, element)
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


    suspend fun runTestCase(old: MutableList<String>, new: MutableList<String>) {

//        console.log("old: $old \n")
//        console.log("new: $new \n")

        with(measureTime {
            val patches = Myer.diff(old, new)

            try {
                patches.map { patch ->
//                    console.log("applying patch: $patch \n")
                    old.applyPatch(patch)
//                    console.log("... result: $old \n")
                }.reduce { _, value -> value }
            } catch (e: NoSuchElementException) {
            } //if there is nothing to do this is ok

            assertEquals(new, old)

        }.inMilliseconds) {
//            console.log("took $this ms \n")
        }
    }

    @Test
    fun randomTests() = runTest {
        repeat(50) {
            val (old, new) = createTestCase()
            runTestCase(old, new)
        }
    }

    @Test
    fun testMoveRight() = runTest {
        val old = mutableListOf<String>("a", "b", "c", "d")
        val new = mutableListOf<String>("a", "c", "b", "d")

        runTestCase(old, new)
    }

    @Test
    fun testMoveLeft() = runTest {
        val old = mutableListOf<String>("a", "b", "c", "d")
        val new = mutableListOf<String>("d", "b", "c", "a")

        runTestCase(old, new)
    }

    @Test
    fun testDeleteManyEnd() = runTest {
        val old = mutableListOf<String>("a", "b", "c", "d", "e", "f", "g")
        val new = mutableListOf<String>("a", "b", "c", "d")

        runTestCase(old, new)
    }

    @Test
    fun testDeleteManyStart() = runTest {
        val old = mutableListOf<String>("a", "b", "c", "d", "e", "f", "g")
        val new = mutableListOf<String>("c", "d", "e", "f", "g")

        runTestCase(old, new)
    }

    @Test
    fun testDeleteManyMiddle() = runTest {
        val old = mutableListOf<String>("a", "b", "c", "d", "e", "f", "g")
        val new = mutableListOf<String>("a", "b", "f", "g")

        runTestCase(old, new)
    }

    @Test
    fun testNothingToDo() = runTest {
        val old = mutableListOf<String>("a", "b")
        val new = mutableListOf<String>("a", "b")

        runTestCase(old, new)
    }

    @Test
    fun testInsertManyEnd() = runTest {
        val old = mutableListOf<String>("a", "b")
        val new = mutableListOf<String>("a", "b", "c", "d", "e")

        runTestCase(old, new)
    }

    @Test
    fun testInsertManyStart() = runTest {
        val old = mutableListOf<String>("d", "e", "f")
        val new = mutableListOf<String>("a", "b", "c", "d", "e", "f")

        runTestCase(old, new)
    }

    @Test
    fun testInsertManyMiddle() = runTest {
        val old = mutableListOf<String>("a", "d")
        val new = mutableListOf<String>("a", "b", "c", "d")

        runTestCase(old, new)
    }
}