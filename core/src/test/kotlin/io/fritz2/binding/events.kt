package io.fritz2.binding

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.js.Promise
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class EventTests {

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun eventHandlerDomChange(): Promise<Boolean> {

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
                    store.update <= changes
                }
                div {
                    id = !"myResult"
                    +"value: "
                    store.data.bind()
                }
                button {
                    id = !"myButton"
                    +"add one more little dot"
                    store.addADot <= clicks
                }
            }
        }

        document.write("""
            <body id="target">
                Loading...
            </body>
        """.trimIndent())

        myComponent.mount("target")

        return GlobalScope.promise {
            delay(250)

            val result = document.getElementById("myResult").unsafeCast<HTMLDivElement>()
            val button = document.getElementById("myButton").unsafeCast<HTMLButtonElement>()

            assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")
            assertEquals("value: start",result.textContent,"wrong dom content of result-node")

            button.click()
            delay(250)
            assertEquals(1, store.countHandlerCalls, "wrong number of handler calls")
            assertEquals("value: start.",result.textContent,"wrong dom content of result-node")

            button.click()
            delay(250)
            assertEquals(2, store.countHandlerCalls, "wrong number of handler calls")
            assertEquals("value: start..",result.textContent,"wrong dom content of result-node")

            true
        }
    }

}