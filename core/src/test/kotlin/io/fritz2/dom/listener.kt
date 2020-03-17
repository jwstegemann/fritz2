package io.fritz2.dom

import io.fritz2.binding.RootStore
import io.fritz2.dom.html.html
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class ListenerTest {

    @BeforeTest
    fun setUp() {
        initDocument()
    }

    @Test
    fun testListenerForChangeEvent() = runTest {
        val inputId = "input1"
        val resultId = "result1"

        val store = object : RootStore<String>("start") {}

        html {
            section {
                input(inputId) {
                    value = store.data
                    store.update <= changes.value()
                }
                div(resultId) {
                    +store.data
                }
            }
        }.mount("target")

        delay(100)

        val input = document.getElementById(inputId).unsafeCast<HTMLInputElement>()
        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()

        assertEquals("start", result.textContent, "wrong dom content of result-node")

        input.value = "test1"
        input.dispatchEvent(Event("change"));
        delay(100)
        assertEquals("test1", result.textContent, "wrong dom content of result-node")

        input.value = "test2"
        input.dispatchEvent(Event("change"));
        delay(100)
        assertEquals("test2", result.textContent, "wrong dom content of result-node")
    }

    @Test
    fun testListenerForClickEvent() = runTest {
        val resultId = "result2"
        val buttonId = "button2"

        val store = object : RootStore<String>("start") {
            var countHandlerCalls = 0

            val addADot = handle { model ->
                countHandlerCalls++
                "$model."
            }
        }

        html {
            section {
                div(resultId) {
                    store.data.bind()
                }
                button(buttonId) {
                    store.addADot <= clicks()
                }
            }
        }.mount("target")

        delay(100)

        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()
        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()

        assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("start", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(1, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("start.", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(2, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("start..", result.textContent, "wrong dom content of result-node")
    }

    @Test
    fun testListenerForMultipleClickEvent() = runTest {
        val resultId = "result3"
        val buttonId = "button3"

        val store = object : RootStore<String>("") {
            var countHandlerCalls = 0

            val addDot = handle { model ->
                countHandlerCalls++
                "$model."
            }

            val addPlus = handle { model ->
                countHandlerCalls++
                "$model+"
            }

            val addDollar = handle { model ->
                countHandlerCalls++
                "$model$"
            }

        }

        html {
            section {
                div(resultId) {
                    store.data.bind()
                }
                button(buttonId) {
                    store.addDot <= clicks()
                    store.addPlus <= clicks()
                    store.addDollar <= clicks()
                }
            }
        }.mount("target")

        delay(100)

        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()
        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()

        assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(3, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals(".+\$", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(6, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals(".+\$.+\$", result.textContent, "wrong dom content of result-node")
    }


}