package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals


class StoreTests {

    @Test
    fun testStoreHandleAndOfferHandler() = runTest {
        initDocument()

        val id1 = uniqueId()
        val id2 = uniqueId()
        val buttonId = uniqueId()

        val store1 = object : RootStore<String>("start") {

            val finish = handleAndOffer<Int> { _ ->
                offer(5)
                "finish"
            }
        }

        val store2 = object : RootStore<Int>(0) {}

        store1.finish handledBy store2.update

        render {
            section {
                div(id = id1) {
                    store1.data.bind()
                }
                div(id = id2) {
                    store2.data.map { it.toString() }.bind()
                }
                button(id = buttonId) {
                    clicks handledBy store1.finish
                }
            }
        }.mount(targetId)

        delay(100)

        val button = document.getElementById(buttonId) as HTMLButtonElement
        val div1 = document.getElementById(id1) as HTMLDivElement
        val div2 = document.getElementById(id2) as HTMLDivElement

        assertEquals("start", div1.textContent, "textContent of div1 is not same like store1")
        assertEquals("0", div2.textContent, "textContent of div2 is not same like store2")

        button.click()
        delay(100)

        assertEquals("finish", div1.textContent, "textContent of div1 is not same like store1 after click")
        assertEquals("5", div2.textContent, "textContent of div2 is not same like store2 after click")
    }

    @Test
    fun testStoreHandleAndOfferHandleAndOfferHandler() = runTest {
        initDocument()

        val id1 = uniqueId()
        val id2 = uniqueId()
        val id3 = uniqueId()
        val buttonId = uniqueId()

        val s1 = object : RootStore<String>("s1.start") {

            val finish = handleAndOffer<String> {
                offer("s1.finish")
                "s1.finish"
            }
        }

        val s2 = object : RootStore<String>("s2.start") {
            val finish = handleAndOffer<String, String> { _, action ->
                offer("s2.finish")
                action
            }
        }

        val s3 = object : RootStore<String>("s3.start") {}

        s1.finish handledBy s2.finish
        s2.finish handledBy s3.update

        render {
            section {
                div(id = id1) {
                    s1.data.bind()
                }
                div(id = id2) {
                    s2.data.bind()
                }
                div(id = id3) {
                    s3.data.bind()
                }
                button(id = buttonId) {
                    clicks handledBy s1.finish
                }
            }
        }.mount(targetId)

        delay(100)

        val button = document.getElementById(buttonId) as HTMLButtonElement
        val div1 = document.getElementById(id1) as HTMLDivElement
        val div2 = document.getElementById(id2) as HTMLDivElement
        val div3 = document.getElementById(id3) as HTMLDivElement

        assertEquals("s1.start", div1.textContent, "textContent of div1 is not same like store1")
        assertEquals("s2.start", div2.textContent, "textContent of div2 is not same like store2")
        assertEquals("s3.start", div3.textContent, "textContent of div3 is not same like store3")

        button.click()
        delay(100)

        assertEquals("s1.finish", div1.textContent, "textContent of div1 is not same like store1 after click")
        assertEquals("s1.finish", div2.textContent, "textContent of div2 is not same like store2 after click")
        assertEquals("s2.finish", div3.textContent, "textContent of div3 is not same like store3 after click")
    }
}