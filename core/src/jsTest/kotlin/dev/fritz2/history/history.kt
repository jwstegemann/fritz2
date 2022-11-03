package dev.fritz2.history

import dev.fritz2.core.Id
import dev.fritz2.core.RootStore
import dev.fritz2.core.render
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HistoryTests {

    @Test
    fun testSyncedHistory() = runTest {
        
        val valueId = "value-${Id.next()}"
        val historyId = "history-${Id.next()}"
        val availableId = "available-${Id.next()}"

        fun getValue() = document.getElementById(valueId)?.textContent
        fun getHistory() = document.getElementById(historyId)?.textContent
        fun getAvailable() = document.getElementById(availableId)?.textContent?.toBoolean()

        val store = object : RootStore<String>("A") {
            val hist = history()
        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
                span(id = historyId) { store.hist.data.map { it.joinToString() }.renderText() }
                span(id = availableId) { store.hist.available.asString().renderText() }
            }
        }

        delay(100)
        assertEquals("A", getValue())
        assertEquals("A", getHistory())
        assertEquals(false, getAvailable())

        store.update("B")
        delay(100)
        assertEquals("B", getValue())
        assertEquals("A, B", getHistory())
        assertEquals(true, getAvailable())

        store.update("C")
        delay(100)
        assertEquals("C", getValue())
        assertEquals("A, B, C", getHistory())
        assertEquals(true, getAvailable())

        store.update("D")
        delay(100)
        assertEquals("D", getValue())
        assertEquals("A, B, C, D", getHistory())
        assertEquals(true, getAvailable())

        store.update(store.hist.back())
        delay(100)
        assertEquals("C", getValue())
        assertEquals("A, B, C", getHistory())
        assertEquals(true, getAvailable())

        store.update(store.hist.back())
        delay(100)
        assertEquals("B", getValue())
        assertEquals("A, B", getHistory())
        assertEquals(true, getAvailable())

        store.hist.clear()
        delay(100)
        assertEquals("B", getValue())
        assertEquals("B", getHistory())
        assertEquals(false, getAvailable())
    }

    @Test
    fun testHistoryLongerMax() = runTest {
        
        val valueId = "value-${Id.next()}"
        val historyId = "history-${Id.next()}"
        val values = listOf("A", "B", "C", "D", "E", "F", "G")

        fun getValue() = document.getElementById(valueId)?.textContent
        fun getHistory() = document.getElementById(historyId)?.textContent

        val histLength = 4

        val store = object : RootStore<String>("") {
            val hist = history(histLength)

        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
                span(id = historyId) { store.hist.data.map { it.joinToString() }.renderText() }
            }
        }

        delay(100)
        assertEquals("", getValue())
        assertEquals("", getHistory())

        values.forEach { value ->
            store.update(value)
            delay(1)
        }
        delay(200)

        assertEquals(values.last(), getValue())
        assertEquals(values.takeLast(histLength + 1).drop(1).joinToString(), getHistory())
    }


    @Test
    fun testMaxCapacityError() = runTest {
        assertFailsWith<IllegalArgumentException> {
            history(4, listOf("a", "b", "c", "d", "e"))
        }
    }
}