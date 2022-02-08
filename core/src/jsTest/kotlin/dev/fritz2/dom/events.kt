package dev.fritz2.dom

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.*
import dev.fritz2.identification.Id
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.test.Test
import kotlin.test.assertEquals


class EventsTest {

    @Test
    fun testChangeAndInputEvent() = runTest {
        initDocument()

        val inputId = Id.next()

        val store = object : RootStore<String>("start") {}

        render {
            section {
                input(id = inputId) {
                    value(store.data)
                    changes.preventDefault().values() handledBy store.update
                    inputs.values() handledBy store.update
                }
            }
        }

        // wait for initial rendering to finish
        delay(100)
        val input = document.getElementById(inputId).unsafeCast<HTMLInputElement>()

        assertEquals("start", input.value, "wrong dom content of result-node")

        input.value = "test1"
        input.dispatchEvent(Event("change"))
        delay(200)
        assertEquals("test1", input.value, "wrong dom content of result-node")

        input.value = "test2"
        input.dispatchEvent(Event("input"))
        delay(200)
        assertEquals("test2", input.value, "wrong dom content of result-node")
    }

    @Test
    fun testClickEvent() = runTest {
        initDocument()

        val resultId = Id.next()
        val buttonId = Id.next()

        val store = object : RootStore<String>("start") {
            var countHandlerCalls = 0

            val addADot = handle { model ->
                countHandlerCalls++
                "$model."
            }
        }

        render {
            section {
                div(id = resultId) {
                    store.data.renderText()
                }
                button(id = buttonId) {
                    clicks.preventDefault() handledBy store.addADot
                }
            }
        }

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
    fun testMultipleClickEvent() = runTest {
        initDocument()

        val resultId = Id.next()
        val buttonId = Id.next()

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

        render {
            section {
                div(id = resultId) {
                    store.data.renderText()
                }
                button(id = buttonId) {
                    clicks handledBy store.addDot
                    clicks handledBy store.addPlus
                    clicks handledBy store.addDollar
                }
            }
        }

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
    fun testKeyboardEvent() = runTest {
        initDocument()

        val resultId = Id.next()
        val inputId = Id.next()

        val store = object : RootStore<String>("") {
            var countHandlerCalls = 0

            val keyPressed = handle { _, shortcut: Shortcut ->
                countHandlerCalls++
                var pressed = ""
                when {
                    shortcut.alt -> pressed = "alt+"
                    shortcut.ctrl -> pressed = "ctrl+"
                    shortcut.meta -> pressed = "meta+"
                    shortcut.shift -> pressed = "shift+"
                }
                pressed += when (shortcut) {
                    Keys.ArrowUp -> "up"
                    Keys.ArrowDown -> "down"
                    else -> "unknown"
                }
                pressed
            }

        }

        render {
            section {
                div(id = resultId) {
                    store.data.renderText()
                }
                input(id = inputId) {
                    keydowns.map { shortcutOf(it) } handledBy store.keyPressed
                }
            }
        }

        delay(100)

        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()
        val input = document.getElementById(inputId).unsafeCast<HTMLButtonElement>()
        var handlerCalls = 0

        assertEquals(handlerCalls, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("", result.textContent, "wrong dom content of result-node")

        val keyboardEvents = listOf(Keys.ArrowUp, Keys.ArrowDown)
            .flatMap {
                listOf(
                    KeyboardEvent("keydown", KeyboardEventInit(it.key, it.key, ctrlKey = true)),
                    KeyboardEvent("keydown", KeyboardEventInit(it.key, it.key, altKey = true)),
                    KeyboardEvent("keydown", KeyboardEventInit(it.key, it.key, shiftKey = true)),
                    KeyboardEvent("keydown", KeyboardEventInit(it.key, it.key, metaKey = true))
                )
            }


        for (e: KeyboardEvent in keyboardEvents) {
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
            expected += when (Shortcut(e)) {
                Keys.ArrowUp -> "up"
                Keys.ArrowDown -> "down"
                else -> "unknown"
            }
            assertEquals(expected, result.textContent, "wrong dom content of result-node")
        }
    }

    // TODO: Evaluate parameterized tests in order to test also keydowns and keypresss
    //  This should also be transferred to other tests as well to eliminate boilerplate and control structures!
    @Test
    fun testEnterForInput() = runTest {
        // TODO: Evaluate pre test execution by framework
        initDocument()

        val inputId = Id.next()
        val resultId = Id.next()

        val store = object : RootStore<String>("start") {}

        render {
            section {
                input(id = inputId) {
                    value(store.data)
                    keyups.mapNotNull {
                        if (shortcutOf(it) == Keys.Enter) this.domNode.value
                        else null
                    } handledBy store.update
                }
                p(id = resultId) {
                    store.data.renderText()
                }
            }
        }

        // wait for initial rendering to finish
        delay(100)
        val input = document.getElementById(inputId).unsafeCast<HTMLInputElement>()
        val resultNode = document.getElementById(resultId).unsafeCast<HTMLParagraphElement>()

        assertEquals("start", resultNode.textContent, "wrong dom content of result-node")

        input.value = "some other content"
        val event = KeyboardEvent("keyup", KeyboardEventInit(Keys.Enter.key, code = Keys.Enter.key))
        input.dispatchEvent(event)
        delay(200)

        assertEquals("some other content", resultNode.textContent, "wrong dom content of result-node")
    }
}