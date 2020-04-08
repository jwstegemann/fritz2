package io.fritz2.dom

import io.fritz2.binding.RootStore
import io.fritz2.dom.html.Key
import io.fritz2.dom.html.Keys
import io.fritz2.dom.html.html
import io.fritz2.test.initDocument
import io.fritz2.test.randomId
import io.fritz2.test.runTest
import io.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals


class ListenerTest {

    @Test
    fun testListenerForChangeEvent() = runTest {
        initDocument()

        val inputId = randomId("input")
        val resultId = randomId("result")

        val store = object : RootStore<String>("start") {}

        html {
            section {
                input(id = inputId) {
                    value = store.data
                    store.update <= changes.values()
                }
                div(id = resultId) {
                    store.data.bind()
                }
            }
        }.mount(targetId)

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
        initDocument()

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
                div(id = resultId) {
                    store.data.bind()
                }
                button(id = buttonId) {
                    store.addADot <= clicks
                }
            }
        }.mount(targetId)

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
        initDocument()

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
                div(id = resultId) {
                    store.data.bind()
                }
                button(id = buttonId) {
                    store.addDot <= clicks
                    store.addPlus <= clicks
                    store.addDollar <= clicks
                }
            }
        }.mount(targetId)

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

    @Test
    fun testListenerForKeyboardEvent() = runTest {
        initDocument()

        val resultId = "result4"
        val inputId = "button4"

        val store = object : RootStore<String>("") {
            var countHandlerCalls = 0

            val keyPressed = handle { _, key: Key ->
                countHandlerCalls++
                var pressed = ""
                when {
                    key.alt -> pressed = "alt+"
                    key.ctrl -> pressed = "ctrl+"
                    key.meta -> pressed = "meta+"
                    key.shift -> pressed = "shift+"
                }
                pressed += when (key.code) {
                    Keys.ArrowUp.code -> "up"
                    Keys.ArrowDown.code -> "down"
                    else -> "unknown"
                }
                pressed
            }

        }

        html {
            section {
                div(id = resultId) {
                    store.data.bind()
                }
                input(id = inputId) {
                    store.keyPressed <= keydowns.key()
                }
            }
        }.mount(targetId)

        delay(100)

        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()
        val input = document.getElementById(inputId).unsafeCast<HTMLButtonElement>()
        var handlerCalls = 0

        assertEquals(handlerCalls, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("", result.textContent, "wrong dom content of result-node")

        val keyboardEvents = listOf(Keys.ArrowUp, Keys.ArrowDown)
            .flatMap {
                listOf(
                    KeyboardEvent("keydown", KeyboardEventInit(it.name, it.name, ctrlKey = true)),
                    KeyboardEvent("keydown", KeyboardEventInit(it.name, it.name, altKey = true)),
                    KeyboardEvent("keydown", KeyboardEventInit(it.name, it.name, shiftKey = true)),
                    KeyboardEvent("keydown", KeyboardEventInit(it.name, it.name, metaKey = true))
                )
            }


        for (e in keyboardEvents) {
            input.dispatchEvent(e)
            delay(100)
            assertEquals(++handlerCalls, store.countHandlerCalls, "wrong number of handler calls")
            var expected = ""
            when {
                e.altKey -> expected = "alt+"
                e.ctrlKey -> expected = "ctrl+"
                e.metaKey -> expected = "meta+"
                e.shiftKey -> expected = "shift+"
            }
            expected += when (e.keyCode) {
                Keys.ArrowUp.code -> "up"
                Keys.ArrowDown.code -> "down"
                else -> "unknown"
            }
            assertEquals(expected, result.textContent, "wrong dom content of result-node")
        }
    }
}