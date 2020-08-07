package dev.fritz2.history

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

class HistoryTests {

    @Test
    fun testSyncedHistory() = runTest {
        initDocument()

        val valueId = "value-${uniqueId()}"
        val historyId = "history-${uniqueId()}"
        val values = listOf("A", "B", "C", "D")

        fun getValue() = document.getElementById(valueId)?.textContent
        fun getHistory() = document.getElementById(historyId)?.textContent

        val store = object : RootStore<String>(values[0]) {
            val hist = history<String>().sync(this)

        }

        render {
            div {
                span(id = valueId) { store.data.bind() }
                span(id = historyId) { store.hist.map { hist -> hist.joinToString() }.bind() }
            }
        }.mount(targetId)

        delay(100)
        assertEquals(values[0], getValue())
        assertEquals("", getHistory())

        action(values[1]) handledBy store.update
        delay(100)
        assertEquals(values[1], getValue())
        assertEquals(values[0], getHistory())

        action(values[2]) handledBy store.update
        delay(100)
        assertEquals(values[2], getValue())
        assertEquals(values.slice(0..1).reversed().joinToString(), getHistory())

        action(values[3]) handledBy store.update
        delay(100)
        assertEquals(values[3], getValue())
        assertEquals(values.slice(0..2).reversed().joinToString(), getHistory())

        action(store.hist.back()) handledBy store.update
        delay(100)
        assertEquals(values[2], getValue())
        assertEquals(values.slice(0..1).reversed().joinToString(), getHistory())

        assertEquals(store.hist.last(), values[1])

        action(store.hist.back()) handledBy store.update
        delay(100)
        assertEquals(values[1], getValue())
        assertEquals(values[0], getHistory())

        store.hist.reset()
        delay(100)
        assertEquals("", getHistory())
    }


}