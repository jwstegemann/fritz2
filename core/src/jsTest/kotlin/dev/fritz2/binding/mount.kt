package dev.fritz2.binding

import dev.fritz2.dom.html.handledBy
import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.test.checkSingleFlow
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLDivElement
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MountTests {

    @Test
    fun testStore(): Promise<Boolean> {

        val store = RootStore("")

        val values = listOf(
            "",
            "1",
            "1-2",
            "1-2-3",
            "1-2-3-4"
        )


        val done = CompletableDeferred<Boolean>()
        checkSingleFlow(done, store.data) { _, value ->
            assertTrue(values.contains(value))
            value == values.last()
        }

        store.data handledBy {}

        return GlobalScope.promise {
            values.forEach { value ->
                store.enqueue { value }
            }
            done.await()
        }
    }


    @Test
    fun testOrderOfSingleMountPointCreation() = runTest {
        initDocument()

        val outer = Id.next()
        val inner1 = Id.next()
        val inner2 = Id.next()

        val text = flowOf("test")

        render {
            div(id = outer) {
                text.render {
                    div(id = inner1) {
                        +it
                    }
                }
                div(id = inner2) {
                    +"hallo"
                }
            }
        }

        delay(250)

        val outerElement = document.getElementById(outer) as HTMLDivElement
        assertEquals(inner1, outerElement.firstElementChild?.firstElementChild?.id, "first element id does not match")
        assertEquals(inner2, outerElement.lastElementChild?.id, "last element id does not match")
    }

    @Test
    fun testOrderOfMultiMountPointCreation() = runTest {
        initDocument()

        val outer = Id.next()
        val inner1 = Id.next()
        val inner2 = Id.next()
        val inner3 = Id.next()

        val text = flowOf(listOf(inner1, inner2))

        render {
            div(id = outer) {
                text.renderEach {
                    div(id = it) {}
                }
                div(id = inner3) {}
            }
        }

        delay(250)

        val outerElement = document.getElementById(outer) as HTMLDivElement
        assertEquals(inner1, outerElement.firstElementChild?.firstElementChild?.id, "first element id does not match")
        assertEquals(
            inner2,
            outerElement.firstElementChild?.firstElementChild?.nextElementSibling?.id,
            "second element id does not match"
        )
        assertEquals(inner3, outerElement.lastElementChild?.id, "last element id does not match")
    }

    @Test
    fun testOrderOfTextNodeCreation() = runTest {
        initDocument()

        val id = Id.next()

        val text = flowOf("test")

        render {
            div(id = id) {
                +"start-"
                text.renderText()
                +"-end"
            }
        }

        delay(250)

        val div = document.getElementById(id) as HTMLDivElement
        assertEquals("start-test-end", div.innerText, "order of text does not match")
    }
}