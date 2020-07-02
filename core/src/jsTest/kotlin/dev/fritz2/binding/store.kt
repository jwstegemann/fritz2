package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals


class StoreTests {

    @Test
    fun testStoreApplierAndThenHandler() = runTest {
        initDocument()

        val resultId = uniqueId()
        val buttonId = uniqueId()

        val store = object : RootStore<String>("start") {

            val finish = apply<Unit, String> {
                flow {
                    delay(200)
                    emit("finish")
                }
            }

            val execute = finish andThen update
        }

        render {
            section {
                div(id = resultId) {
                    store.data.bind()
                }
                button(id = buttonId) {
                    clicks handledBy store.execute
                }
            }
        }.mount(targetId)

        delay(100)

        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()
        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()

        button.click()
        delay(100)
        assertEquals("start", result.textContent, "wrong dom content of result-node")
        delay(300)
        assertEquals("finish", result.textContent, "wrong dom content of result-node")
    }

    @Test
    fun testStoreApplierAndThenApplierAndThenHandler() = runTest {
        initDocument()

        val resultId = uniqueId()
        val buttonId = uniqueId()

        val store = object : RootStore<String>("start") {

            val generate = apply<Unit, String> {
                flow {
                    delay(100)
                    emit("generate")
                }
            }

            val finish = apply<String, String> {
                flow {
                    delay(100)
                    emit("finish")
                }
            }

            val execute = generate andThen finish andThen update
        }

        render {
            section {
                div(id = resultId) {
                    store.data.bind()
                }
                button(id = buttonId) {
                    clicks handledBy store.execute
                }
            }
        }.mount(targetId)

        delay(100)

        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()
        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()

        button.click()
        assertEquals("start", result.textContent, "wrong dom content of result-node")
        delay(300)
        assertEquals("finish", result.textContent, "wrong dom content of result-node")
    }

    @Test
    fun testStoreApplierAndThenApplierAndThenHandlerWithTypeConversion() = runTest {
        initDocument()

        val resultId = uniqueId()
        val buttonId = uniqueId()

        val store = object : RootStore<String>("start") {

            val generate = apply<Unit, Int> {
                flow {
                    delay(100)
                    emit(100)
                }
            }

            val finish = apply<Int, String> {
                flow {
                    delay(100)
                    emit(it.toString())
                }
            }

            val execute = generate andThen finish andThen update
        }

        render {
            section {
                div(id = resultId) {
                    store.data.bind()
                }
                button(id = buttonId) {
                    clicks handledBy store.execute
                }
            }
        }.mount(targetId)

        delay(100)

        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()
        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()

        button.click()
        assertEquals("start", result.textContent, "wrong dom content of result-node")
        delay(300)
        assertEquals("100", result.textContent, "wrong dom content of result-node")
    }

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
}