package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.test.*

class HandlersTests {

    @Test
    fun testSimpleHandler() = runTest {
        val store = object : RootStore<Int>(0) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }
        }

        store.handle { assertTrue(true); it }()
        store.handle { assertTrue(true); it }(Unit)
        store.handle<String> { n, s -> assertFalse(s::class == String::class); n }()
        store.handle<String> { n, s -> assertTrue(s::class == Unit::class); n }()
        store.handle<String> { n, s -> assertTrue(s.length == undefined); n }()
        store.handle<String> { n, s -> assertTrue(s.substring(1) == undefined); n }()
        store.handle<String> { n, _ -> assertTrue(true); n }("Hello")
    }

    @Test
    fun eventHandlerDomChange() = runTest {
        
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
                input {
                    value(store.data)
                    changes.values() handledBy store.update
                }
                div(id = resultId) {
                    +"value: "
                    store.data.renderText()
                }
                button(id = buttonId) {
                    +"add one more little dot"
                    clicks handledBy store.addADot
                }
            }
        }

        delay(100)

        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()
        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()

        assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(1, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start.", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(2, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start..", result.textContent, "wrong dom content of result-node")
    }

}
