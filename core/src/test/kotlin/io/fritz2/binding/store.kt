package io.fritz2.binding

import kotlinx.coroutines.*
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreTests {

    @FlowPreview
    @Test
    fun storeUpdateTriggersMountPoint(): Promise<Boolean> {

        val store = RootStore<String>("")

        val mp = checkFlow(store.data, 5) { count, value, last ->
            val expected = (0 until count).fold("",{ s,i ->
                "$s-$i"
            })
            assertEquals(expected, value, "set wrong value in MountPoint")
        }

        return GlobalScope.promise {
            for (i in 0..4) {
                store.enqueue { "$it-$i" }
            }
            mp.await()
        }
    }

}