package dev.fritz2.services.tracking

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.action
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackingTests {


    @Test
    fun testTracking() = runTest {
        initDocument()

        val transactionText = "long running"
        val transactionId = "transaction-${uniqueId()}"

        val startValue = "start"
        val endValue = "end"
        val valueId = "value-${uniqueId()}"

        val store = object : RootStore<String>(startValue) {
            val running = tracker()

            val longRunningHandler = handle {
                running.track(transactionText) {
                    delay(600)
                    endValue
                }
            }
        }

        render {
            div {
                span(id = transactionId) { store.running.map { it.orEmpty() }.bind() }
                span(id = valueId) { store.data.bind() }
            }
        }.mount(targetId)
        delay(200)

        action() handledBy store.longRunningHandler

        val valueBeforeTransaction = document.getElementById(valueId)?.textContent
        assertEquals(startValue, valueBeforeTransaction)

        delay(300)

        val transactionDuringHandler = document.getElementById(transactionId)?.textContent
        assertEquals(transactionText, transactionDuringHandler)

        val valueDuringTransaction = document.getElementById(valueId)?.textContent
        assertEquals(startValue, valueDuringTransaction)

        delay(450)

        val transactionAfterHandler = document.getElementById(transactionId)?.textContent
        assertEquals("", transactionAfterHandler)

        val valueAfterTransaction = document.getElementById(valueId)?.textContent
        assertEquals(endValue, valueAfterTransaction)

    }
}