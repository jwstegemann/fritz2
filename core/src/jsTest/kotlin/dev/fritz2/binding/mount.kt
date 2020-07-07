package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.checkFlow
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
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

        val mp = checkFlow(store.data.eachElement().data, 5) { count, patch ->
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

        val mp = checkFlow(store.data.eachElement().data, 3) { count, patch ->
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

        val mp = checkFlow(store.data.eachElement().data, 3) { count, patch ->
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
    fun testOrderOfDomNodeCreation() = runTest {
        initDocument()

        val outer = uniqueId()
        val inner1 = uniqueId()
        val inner2 = uniqueId()

        val text = flowOf("test")

        render {
            div(id = outer) {
                text.map {
                    render {
                        div(id = inner1) {
                            text(it)
                        }
                    }
                }.bind(preserveOrder = true)
                div(id = inner2) {
                    text("hallo")
                }
            }
        }.mount("target")

        delay(250)

        val outerElement = document.getElementById(outer) as HTMLDivElement
        assertEquals(inner1, outerElement.firstElementChild?.id, "first element id does not match")
        assertEquals(inner2, outerElement.lastElementChild?.id, "last element id does not match")
    }

    @Test
    fun testOrderOfTextNodeCreation() = runTest {
        initDocument()

        val id = uniqueId()

        val text = flowOf("test")

        render {
            div(id = id) {
                text("start-")
                text.bind(preserveOrder = true)
                text("-end")
            }
        }.mount("target")

        delay(250)

        val div = document.getElementById(id) as HTMLDivElement
        assertEquals("start-test-end", div.innerText, "order of text does not match")
//        assertEquals("start--endtest", div.innerText, "order of text does not match")
    }
}