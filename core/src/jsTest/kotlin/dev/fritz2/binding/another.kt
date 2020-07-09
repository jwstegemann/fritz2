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
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SeqTests {

    private fun listContent(listId: String) = document.getElementById(listId)?.textContent
    private fun clickButton(id: String) {
        (document.getElementById(id) as HTMLButtonElement).click()
    }

    private val

    /*
     * with id...
     */

    data class Entity(val id: String, val value: String)

    val valueLens = buildLens("value", Entity::value, { p, v -> p.copy(value = v) })

    class TestEntityListStore() : RootStore<List<Entity>>(
        listOf(
            Entity("1", "a"),
            Entity("2", "b"),
            Entity("3", "c"),
            Entity("4", "d")
        )
    ) {
        val append = handle { model -> model + Entity("5", "e") }
        val change = handle { model -> listOf(model.first(), Entity("2", "x")) + model.takeLast(3) }
        val insert = handle { model -> listOf(Entity("0", "y")) + model }
        val delete = handle { model -> model.take(3) + model.takeLast(2) }
    }

    @Test
    @Ignore
    fun testEachEntity() = runTest {
        val listId = "list" + uniqueId()
        initDocument()

        val store = TestEntityListStore()

        render {
            div {
                ul(id = listId) {
                    store.data.each(Entity::id).map {
                        render {
                            li { text(it.value) }
                        }
                    }.bind()
                }
                button(id = "append") { clicks handledBy store.append }
                button(id = "change") { clicks handledBy store.change }
                button(id = "insert") { clicks handledBy store.insert }
                button(id = "delete") { clicks handledBy store.delete }
            }
        }.mount(targetId)

        delay(400)
        assertEquals("abcd", listContent(listId), "list incorrect after init")

        clickButton("append")
        delay(200)
        assertEquals("abcde", listContent(listId), "list incorrect after append")

        clickButton("change")
        delay(200)
        assertEquals("abcde", listContent(listId), "list incorrect after change")

        clickButton("insert")
        delay(200)
        assertEquals("yabcde", listContent(listId), "list incorrect after insert")

        clickButton("delete")
        delay(200)
        assertEquals("yabde", listContent(listId), "list incorrect after delete")
    }
}