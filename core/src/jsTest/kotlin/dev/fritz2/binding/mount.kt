package dev.fritz2.binding

import dev.fritz2.test.checkFlow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.promise
import kotlin.js.Promise
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


class MountTests {

    @Test
    fun testSingleMountPoint(): Promise<Boolean> {

        val store = RootStore("")

        val mp = checkFlow(store.data, 5) { count, value, _ ->
            //console.log("CHECK $count: $value from $last\n")
            val expected = (0 until count).fold("", { s, i ->
                "$s-$i"
            })
            assertEquals(expected, value, "set wrong value in SingleMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100)
            for (i in 0..4) {
                //console.log("enqueue: -$i\n")
                store.enqueue { "$it-$i" }
            }
            mp.await()
        }
    }

    //FIXME: new tests for MultiMountPoint (not testing diff-Algorithm but just the MountPoint)
    @Test
    @Ignore
    fun testMultiMountPointAppendingAtEnd(): Promise<Boolean> {
        val store = RootStore<List<Int>>(emptyList())
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 5) { count, patch ->
            val expected = Patch.Insert(count, count)

            assertEquals(expected, patch, "set wrong value in MultiMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100) //needs a point to suspend
            for (i in 0..4) {
                store.enqueue { it + i }
            }
            mp.await()
            true
        }
    }

    @Test
    @Ignore
    fun testMultiMountPointAppendingAtBeginning(): Promise<Boolean> {

        val store = RootStore(listOf(0))
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 3) { count, patch ->
            val expected: Patch<Int> = when (count) {
                0 -> Patch.Insert(0, 0) //Patch(0, listOf(0), 0)
                1 -> Patch.Insert(1, 0) // Patch(1, listOf(0), 0)
                2 -> Patch.Delete(0, 1) // Patch(0, listOf(1), 1)
                else -> throw AssertionError("set wrong value in MultiMountPoint\n")
            }
            assertEquals(expected, patch, "set wrong value in MultiMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100) //needs a point to suspend
            store.enqueue { listOf(1) + it }
            mp.await()
            true
        }
    }

    @Test
    @Ignore
    fun testMultiMountPointAppendingAtMiddle(): Promise<Boolean> {

        val store = RootStore(listOf(0, 2))
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 3) { count, patch ->
            val expected: Patch<Int> = when (count) {
                0 -> Patch.Insert(0, 0) //Patch(0, listOf(0, 2), 0)
                //1 -> Patch(2, listOf(2), 0)
                ///2 -> Patch(1, listOf(1), 1)
                else -> throw AssertionError("set wrong value in MultiMountPoint\n")
            }
            assertEquals(expected, patch, "set wrong value in MultiMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100) //needs a point to suspend
            store.enqueue { listOf(0, 1, 2) }
            mp.await()
            true
        }
    }

    @Test
    @Ignore
    fun testMultiMountPointRemovingAtEnd(): Promise<Boolean> {

        val store = RootStore(listOf(0, 1, 2))
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 2) { count, patch ->
            val expected: Patch<Int> = when (count) {
                0 -> Patch.Insert(0, 0) // Patch(0, listOf(0, 1, 2), 0)
                // 1 -> Patch(2, emptyList(), 1)
                else -> throw AssertionError("set wrong value in MultiMountPoint\n")
            }
            assertEquals(expected, patch, "set wrong value in MultiMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100) //needs a point to suspend
            store.enqueue { listOf(0, 1) }
            mp.await()
            true
        }
    }

    @Test
    @Ignore
    fun testMultiMountPointRemovingAtBeginning(): Promise<Boolean> {

        val store = RootStore(listOf(0, 1, 2))
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 4) { count, patch ->
            val expected: Patch<Int> = when (count) {
                0 -> Patch.Insert(0, 0) //Patch(0, listOf(0, 1, 2), 0)
                //1 -> Patch(2, emptyList(), 1)
                //2 -> Patch(0, listOf(1), 1)
                //3 -> Patch(1, listOf(2), 1)
                else -> throw AssertionError("set wrong value in MultiMountPoint\n")
            }
            assertEquals(expected, patch, "set wrong value in MultiMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100) //needs a point to suspend
            store.enqueue { listOf(1, 2) }
            mp.await()
            true
        }
    }

    @Test
    @Ignore
    fun testMultiMountPointRemovingAtMiddle(): Promise<Boolean> {

        val store = RootStore(listOf(0, 1, 2))
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 3) { count, patch ->
            val expected: Patch<Int> = when (count) {
                0 -> Patch.Insert(0, 0) //Patch(0, listOf(0, 1, 2), 0)
                //1 -> Patch(2, emptyList(), 1)
                //2 -> Patch(1, listOf(2), 1)
                else -> throw AssertionError("set wrong value in MultiMountPoint\n")
            }
            assertEquals(expected, patch, "set wrong value in MultiMountPoint\n")
        }

        return GlobalScope.promise {
            delay(100) //needs a point to suspend
            store.enqueue { listOf(0, 2) }
            mp.await()
            true
        }
    }
}