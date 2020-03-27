package io.fritz2.binding

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import io.fritz2.test.randomId
import io.fritz2.test.runTest
import io.fritz2.test.targetId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

@FlowPreview
@ExperimentalCoroutinesApi
class StoreTests {

    @Test
    fun testStoreApplierAndThenHandler() = runTest {
        initDocument()

        val resultId = randomId("result")
        val buttonId = randomId("button")

        val store = object : RootStore<String>("start") {

            val finish = apply<Unit, String> {
                flow {
                    delay(200)
                    emit("finish")
                }
            }

            val execute = finish andThen update
        }

        html {
            section {
                div(id=resultId) {
                    store.data.bind()
                }
                button(id=buttonId) {
                    store.execute <= clicks
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

        val resultId = randomId("result")
        val buttonId = randomId("button")

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

        html {
            section {
                div(id=resultId) {
                    store.data.bind()
                }
                button(id=buttonId) {
                    store.execute <= clicks
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

        val resultId = randomId("result")
        val buttonId = randomId("button")

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

        html {
            section {
                div(id=resultId) {
                    store.data.bind()
                }
                button(id=buttonId) {
                    store.execute <= clicks
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
}