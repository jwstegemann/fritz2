package dev.fritz2.tracking

import dev.fritz2.core.Id
import dev.fritz2.core.RootStore
import dev.fritz2.core.render
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackingTests {

    @Test
    fun testTracking() = runTest {
        val transactionText = "long running"
        val transactionId = "transaction-${Id.next()}"

        val startValue = "start"
        val endValue = "end"
        val valueId = "value-${Id.next()}"

        val store = object : RootStore<String>(startValue) {
            val running = tracker()

            val longRunningHandler = handle {
                running.track {
                    delay(600)
                    endValue
                }
            }
        }

        render {
            div {
                span(id = transactionId) { store.running.data.map { if (it) transactionText else "" }.renderText() }
                span(id = valueId) { store.data.renderText() }
            }
        }
        delay(200)

        store.longRunningHandler()

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

    @Test
    fun testStopTrackingIfExceptionOccursDuringOperation() = runTest {
        val resultElementId = "tracker-${Id.next()}"

        val store = object : RootStore<Int>(0) {
            val running = tracker()

            val handler = handle {
                try {
                    running.track {
                        delay(500)
                        throw Exception("Something unexpected happened")
                    }
                } catch (ex: Exception) {
                    // we just don't want to let this escape to the log...
                }
                it
            }
        }

        render {
            div {
                span(id = resultElementId) { store.running.data.map { if (it) "running" else "stopped" }.renderText() }
            }
        }

        delay(100)

        store.handler()

        val valueBeforeTransaction = document.getElementById(resultElementId)?.textContent
        assertEquals("", valueBeforeTransaction)

        delay(200)

        val valueDuringTransaction = document.getElementById(resultElementId)?.textContent
        assertEquals("running", valueDuringTransaction)

        delay(500)

        val valueAfterTransaction = document.getElementById(resultElementId)?.textContent
        assertEquals("stopped", valueAfterTransaction)
    }

    @Test
    fun testExceptionHandlingInTrackerDuringOperation() = runTest {
        val storeElementId = "store-${Id.next()}"
        val resultElementId = "tracker-${Id.next()}"

        var lastException: String? = null

        val store = object : RootStore<String>("initial") {
            val running = tracker()
            override fun errorHandler(cause: Throwable) {
                lastException = cause.message
            }

            val handler = handle<String> { _, _ ->
                running.track {
                    delay(500)
                    throw Exception("Something unexpected happened")
                }
            }
        }

        render {
            div {
                span(id = storeElementId) { store.data.renderText() }
                span(id = resultElementId) { store.running.data.map { if (it) "running" else "stopped" }.renderText() }
            }
        }

        delay(100)

        store.handler("foo")

        fun textContent(elementId: String) = document.getElementById(elementId)?.textContent

        assertEquals("initial", textContent(storeElementId))
        assertEquals("", textContent(resultElementId))

        delay(200)

        assertEquals("initial", textContent(storeElementId))
        assertEquals("running", textContent(resultElementId))

        delay(500)

        assertEquals("initial", textContent(storeElementId))
        assertEquals("stopped", textContent(resultElementId))
        assertEquals("Something unexpected happened", lastException)

        store.update("bar")
        delay(200)
        assertEquals("bar", textContent(storeElementId))
    }
}
