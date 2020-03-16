package io.fritz2.binding

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.dom.value
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@FlowPreview
@ExperimentalCoroutinesApi
class EventTests {

    @Test
    fun eventHandlerDomChange() = runTest {
        initDocument()

        val store = object : RootStore<String>("start") {
            var countHandlerCalls = 0

            val addADot = handle <Any> { model, _ ->
                countHandlerCalls++
                "$model."
             }
        }


        val myComponent = html {
            section {
                input {
                    value = store.data
                    store.update <= changes.value()
                }
                div("myResult") {
                    +"value: "
                    store.data.bind()
                }
                button("myButton") {
                    +"add one more little dot"
                    store.addADot <= clicks
                }
            }
        }

        myComponent.mount("target")

        delay(100)

        val result = document.getElementById("myResult").unsafeCast<HTMLDivElement>()
        val button = document.getElementById("myButton").unsafeCast<HTMLButtonElement>()

        assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start",result.textContent,"wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(1, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start.",result.textContent,"wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(2, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start..",result.textContent,"wrong dom content of result-node")
    }

}