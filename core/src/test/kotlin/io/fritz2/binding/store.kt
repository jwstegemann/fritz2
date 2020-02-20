package io.fritz2.binding

import kotlinx.coroutines.*
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreTests {

    @FlowPreview
    @Test
    fun storeUpdateTriggersSingleMountPoint(): Promise<Boolean> {

        val store = RootStore<String>("")

        val mp = checkFlow(store.data, 5) { count, value, last ->
            val expected = (0 until count).fold("",{ s,i ->
                "$s-$i"
            })
            assertEquals(expected, value, "set wrong value in SingleMountPoint")
        }

        return GlobalScope.promise {
            for (i in 0..4) {
                store.enqueue { "$it-$i" }
            }
            mp.await()
        }
    }

    @FlowPreview
    @Test
    fun storeUpdateTriggersMultiMountPoint(): Promise<Boolean> {

        val store = RootStore<List<Int>>(emptyList())

        val mp = checkFlow(store.data.each().data, 5) { count, patch ->
            console.log("Patch $patch\n")
            val expected = (0 until count).fold(Patch<Int>(0, emptyList(), 0), {last, value ->
                Patch(value, last.that + value, 0)
            })
            assertEquals(expected, patch, "set wrong value in MultiMountPoint")
        }

        return GlobalScope.promise {
            for (i in 0..4) {
                store.enqueue { it + i }
            }
            mp.await()
        }
    }

}