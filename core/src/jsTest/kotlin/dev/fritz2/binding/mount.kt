package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.checkFlow
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asPromise
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MountTests {

    @Test
    fun testSingleMountPoint(): Promise<Boolean> {

        val store = RootStore("")

        val values = listOf(
            "",
            "1",
            "1-2",
            "1-2-3",
            "1-2-3-4"
        )

        val mp = checkFlow(store.data) { _, value, _ ->
            assertTrue(values.contains(value))
            value == values.last()
        }

        store.data.watch()

        return GlobalScope.promise {
            values.forEach { value ->
                store.enqueue(QueuedUpdate({ value }, store::errorHandler))
            }
            mp.await()
        }
    }

    @Test
    fun testMultiMountPoint(): Promise<Boolean> {
        val listToTest = listOf(1, 2, 3, 4, 5)

        val store = RootStore<List<Int>>(listToTest)

        val mp = checkFlow(store.data.each().data) { _, patch ->
            val expected = Patch.InsertMany(listToTest.reversed(), 0)
            assertEquals(expected, patch, "set wrong value in MultiMountPoint")
            true
        }

        store.data.watch()
        return mp.asPromise()
    }

    @Test
    fun testOrderOfSingleMountPointCreation() = runTest {
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
    fun testOrderOfMultiMountPointCreation() = runTest {
        initDocument()

        val outer = uniqueId()
        val inner1 = uniqueId()
        val inner2 = uniqueId()
        val inner3 = uniqueId()

        val text = flowOf(listOf(inner1, inner2))

        render {
            div(id = outer) {
                text.each().map {
                    render {
                        div(id = it) {}
                    }
                }.bind()
                div(id = inner3) {}
            }
        }.mount("target")

        delay(250)

        val outerElement = document.getElementById(outer) as HTMLDivElement
        assertEquals(inner1, outerElement.firstElementChild?.id, "first element id does not match")
        assertEquals(inner2, outerElement.firstElementChild?.nextElementSibling?.id, "second element id does not match")
        assertEquals(inner3, outerElement.lastElementChild?.id, "last element id does not match")
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