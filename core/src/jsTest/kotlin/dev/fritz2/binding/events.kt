package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.html.value
import dev.fritz2.dom.values
import dev.fritz2.identification.Id
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals


class EventTests {

    @Test
    fun eventHandlerDomChange() = runTest {
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