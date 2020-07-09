package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.buildLens
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreHugoTests {

    private val listId = "list" + uniqueId()

    private fun listContent() = document.getElementById(listId)?.textContent
    private fun clickButton(id: String) {
        (document.getElementById(id) as HTMLButtonElement).click()
    }

    data class Karl(val id: String, val value: String)

    val valueLens = buildLens("value", Karl::value, { p, v -> p.copy(value = v) })

    class Hugo() : RootStore<List<Karl>>(
        listOf(
            Karl("1", "a"),
            Karl("2", "b"),
            Karl("3", "c"),
            Karl("4", "d")
        )
    ) {
        val append = handle { model -> model + Karl("5", "e") }
        val change = handle { model -> listOf(model.first(), Karl("2", "x")) + model.takeLast(3) }
        val insert = handle { model -> listOf(Karl("0", "y")) + model }
        val delete = handle { model -> model.take(3) + model.takeLast(2) }
    }

    @Test
    fun testEachEntityStore() = runTest {
        initDocument()

        val store = Hugo()

        render {
            div {
                ul(id = listId) {
                    store.each(Karl::id).map {
                        val valueStore = it.sub(valueLens)
                        render {
                            li { valueStore.data.bind() }
                        }
                    }.bind()
                }
                button(id = "append") { clicks handledBy store.append }
                button(id = "change") { clicks handledBy store.change }
                button(id = "insert") { clicks handledBy store.insert }
                button(id = "delete") { clicks handledBy store.delete }
            }
        }.mount(targetId)

        delay(200)
        assertEquals("abcd", listContent(), "list incorrect after init")

        clickButton("append")
        delay(200)
        assertEquals("abcde", listContent(), "list incorrect after append")

        clickButton("change")
        delay(200)
        assertEquals("axcde", listContent(), "list incorrect after change")

        clickButton("insert")
        delay(200)
        assertEquals("yaxcde", listContent(), "list incorrect after insert")

        clickButton("delete")
        delay(200)
        assertEquals("yaxde", listContent(), "list incorrect after delete")

    }
}