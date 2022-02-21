package dev.fritz2.history

import dev.fritz2.core.Id
import dev.fritz2.core.RootStore
import dev.fritz2.core.render
import dev.fritz2.initDocument
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.test.Test
import kotlin.test.assertEquals

class HistoryTests {

    @Test
    fun testSyncedHistory() = runTest {
        initDocument()

        val valueId = "value-${Id.next()}"
        val historyId = "history-${Id.next()}"
        val availableId = "available-${Id.next()}"
        val values = listOf("A", "B", "C", "D")

        fun getValue() = document.getElementById(valueId)?.textContent
        fun getHistory() = document.getElementById(historyId)?.textContent
        fun getAvailable() = document.getElementById(availableId)?.textContent?.toBoolean()

        val store = object : RootStore<String>(values[0]) {
            val hist = history<String>().sync(this)

        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
                span(id = historyId) { store.hist.data.map { hist -> hist.joinToString() }.renderText() }
                span(id = availableId) { store.hist.available.map { it.toString() }.renderText() }
            }
        }

        delay(100)
        assertEquals(values[0], getValue())
        assertEquals("", getHistory())
        assertEquals(false, getAvailable())

        store.update(values[1])
        delay(100)
        assertEquals(values[1], getValue())
        assertEquals(values[0], getHistory())
        assertEquals(true, getAvailable())

        store.update(values[2])
        delay(100)
        assertEquals(values[2], getValue())
        assertEquals(values.slice(0..1).reversed().joinToString(), getHistory())

        store.update(values[3])
        delay(100)
        assertEquals(values[3], getValue())
        assertEquals(values.slice(0..2).reversed().joinToString(), getHistory())

        store.update(store.hist.back())
        delay(100)
        assertEquals(values[2], getValue())
        assertEquals(values.slice(0..1).reversed().joinToString(), getHistory())

        assertEquals(store.hist.last(), values[1])

        store.update(store.hist.back())
        delay(100)
        assertEquals(values[1], getValue())
        assertEquals(values[0], getHistory())

        store.hist.reset()
        delay(100)
        assertEquals("", getHistory())
        assertEquals(false, getAvailable())
    }

    @Test
    fun testHistoryLongerMax() = runTest {
        initDocument()

        val valueId = "value-${Id.next()}"
        val historyId = "history-${Id.next()}"
        val values = listOf("A", "B", "C", "D", "E", "F", "G")

        fun getValue() = document.getElementById(valueId)?.textContent
        fun getHistory() = document.getElementById(historyId)?.textContent

        val histLength = 4

        val store = object : RootStore<String>("") {
            val hist = history<String>(histLength).sync(this)

        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
                span(id = historyId) { store.hist.data.map { hist -> hist.joinToString() }.renderText() }
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
        assertEquals(values.takeLast(histLength + 1).reversed().drop(1).joinToString(), getHistory())
    }


    @Test
    fun testManualHistory() = runTest {
        initDocument()

        val valueId = "value-${Id.next()}"
        val historyId = "history-${Id.next()}"
        val values = listOf("A", "B", "C")

        fun getValue() = document.getElementById(valueId)?.textContent
        fun getHistory() = document.getElementById(historyId)?.textContent

        val histLength = 4

        val store = object : RootStore<String>("") {
            val hist = history<String>(histLength).sync(this)

        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
                span(id = historyId) { store.hist.data.map { hist -> hist.joinToString() }.renderText() }
            }
        }

        delay(100)
        assertEquals("", getValue())
        assertEquals("", getHistory())

        values.forEach { value ->
            store.hist.add(value)
            delay(1)
        }
        delay(200)

        assertEquals(values.reversed().joinToString(), getHistory())

        store.update(store.hist.back())
        delay(100)
        assertEquals(values[2], getValue())
        assertEquals(values.slice(0..1).reversed().joinToString(), getHistory())

        store.update(store.hist.back())
        delay(100)
        assertEquals(values[1], getValue())
        assertEquals(values[0], getHistory())

        store.update(store.hist.back())
        delay(100)
        assertEquals(values[0], getValue())
        assertEquals("", getHistory())
    }


}