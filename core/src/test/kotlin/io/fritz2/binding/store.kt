package io.fritz2.binding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class StoreTests {

    @FlowPreview
    @Test
    fun storeUpdateTriggersSingleMountPoint(): Promise<Boolean> {

        val store = RootStore<String>("")

        val mp = checkFlow(store.data, 5) { count, value, last ->
            //console.log("CHECK $count: $value from $last\n")
            val expected = (0 until count).fold("",{ s,i ->
                "$s-$i"
            })
            assertEquals(expected, value, "set wrong value in SingleMountPoint")
        }

        return GlobalScope.promise {
            //delay(100)
            for (i in 0..4) {
                //console.log("enqueue: -$i\n")
                store.enqueue { "$it-$i" }
            }
            mp.await()
        }
    }

    @InternalCoroutinesApi
    @FlowPreview
    @Test
    fun storeUpdateTriggersMultiMountPoint(): Promise<Boolean> {

        val store = RootStore<List<Int>>(emptyList())
        store.data.launchIn(GlobalScope)

        val mp = checkFlow(store.data.each().data, 5) { count, patch ->
            val expected = Patch(count, listOf(count), 0)

//            console.log("$count: EXPECTED: $expected\n")
//            console.log("$count: PATCH   : $patch\n")
            assertEquals(expected, patch, "set wrong value in MultiMountPoint")

        }

        return GlobalScope.promise {
            delay(1) //needs a point to suspend
            for (i in 0..4) {
                store.enqueue { it + i }
            }
            delay(1)

            mp.await()

            true
        }
    }

}