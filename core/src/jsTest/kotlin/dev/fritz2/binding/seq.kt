package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.lenses.buildLens
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import kotlin.test.Test
import kotlin.test.assertEquals

class SeqTests {

    private fun listContent(listId: String) = document.getElementById(listId)?.textContent
    private fun clickButton(id: String) {
        (document.getElementById(id) as HTMLButtonElement).click()
    }

    class TestListStore : RootStore<List<String>>(listOf("a", "b", "c", "d")) {
        val append = handle { model -> model + "e" }
        val change = handle { model -> listOf(model.first(), "x") + model.takeLast(3) }
        val insert = handle { model -> listOf("y") + model }
        val delete = handle { model -> model.take(3) + model.takeLast(2) }
    }

    @Test
    fun testEachElement() = runTest {
        val listId = "list" + Id.next()
        val appendBtnId = "btn-append" + Id.next()
        val changeBtnId = "btn-change" + Id.next()
        val insertBtnId = "btn-insert" + Id.next()
        val deleteBtnId = "btn-delete" + Id.next()

        initDocument()

        val store = TestListStore()

        render {
            div {
                ul(id = listId) {
                    store.data.renderEach {
                        li { +it }
                    }
                }
                button(id = appendBtnId) { clicks handledBy store.append }
                button(id = changeBtnId) { clicks handledBy store.change }
                button(id = insertBtnId) { clicks handledBy store.insert }
                button(id = deleteBtnId) { clicks handledBy store.delete }
            }
        }

        delay(200)
        assertEquals("abcd", listContent(listId), "list incorrect after init")

        clickButton(appendBtnId)
        delay(100)
        assertEquals("abcde", listContent(listId), "list incorrect after append")

        clickButton(changeBtnId)
        delay(100)
        assertEquals("axcde", listContent(listId), "list incorrect after change")

        clickButton(insertBtnId)
        delay(100)
        assertEquals("yaxcde", listContent(listId), "list incorrect after insert")

        clickButton(deleteBtnId)
        delay(100)
        assertEquals("yaxde", listContent(listId), "list incorrect after delete")
    }

    data class Entity(val id: String, val value: String)

    private val valueLens = buildLens("value", Entity::value) { p, v -> p.copy(value = v) }

    class TestEntityListStore : RootStore<List<Entity>>(
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
    fun testEachEntity() = runTest {
        val listId = "list" + Id.next()
        val appendBtnId = "btn-append" + Id.next()
        val changeBtnId = "btn-change" + Id.next()
        val insertBtnId = "btn-insert" + Id.next()
        val deleteBtnId = "btn-delete" + Id.next()

        initDocument()

        val store = TestEntityListStore()

        render {
            div {
                ul(id = listId) {
                    store.data.renderEach(Entity::id) {
                        li { +it.value }
                    }
                }
                button(id = appendBtnId) { clicks handledBy store.append }
                button(id = changeBtnId) { clicks handledBy store.change }
                button(id = insertBtnId) { clicks handledBy store.insert }
                button(id = deleteBtnId) { clicks handledBy store.delete }
            }
        }

        delay(200)
        assertEquals("abcd", listContent(listId), "list incorrect after init")

        clickButton(appendBtnId)
        delay(100)
        assertEquals("abcde", listContent(listId), "list incorrect after append")

        clickButton(changeBtnId)
        delay(100)
        assertEquals("abcde", listContent(listId), "list incorrect after change")

        clickButton(insertBtnId)
        delay(100)
        assertEquals("yabcde", listContent(listId), "list incorrect after insert")

        clickButton(deleteBtnId)
        delay(100)
        assertEquals("yabde", listContent(listId), "list incorrect after delete")
    }


    @Test
    fun testEachEntityStore() = runTest {
        val listId = "list" + Id.next()
        val appendBtnId = "btn-append" + Id.next()
        val changeBtnId = "btn-change" + Id.next()
        val insertBtnId = "btn-insert" + Id.next()
        val deleteBtnId = "btn-delete" + Id.next()

        initDocument()

        val store = TestEntityListStore()

        render {
            div {
                ul(id = listId) {
                    store.renderEach(Entity::id) {
                        val valueStore = it.sub(valueLens)
                        li { valueStore.data.renderText() }
                    }
                }
                button(id = appendBtnId) { clicks handledBy store.append }
                button(id = changeBtnId) { clicks handledBy store.change }
                button(id = insertBtnId) { clicks handledBy store.insert }
                button(id = deleteBtnId) { clicks handledBy store.delete }
            }
        }

        delay(200)
        assertEquals("abcd", listContent(listId), "list incorrect after init")

        clickButton(appendBtnId)
        delay(100)
        assertEquals("abcde", listContent(listId), "list incorrect after append")

        clickButton(changeBtnId)
        delay(100)
        assertEquals("axcde", listContent(listId), "list incorrect after change")

        clickButton(insertBtnId)
        delay(100)
        assertEquals("yaxcde", listContent(listId), "list incorrect after insert")

        clickButton(deleteBtnId)
        delay(100)
        assertEquals("yaxde", listContent(listId), "list incorrect after delete")

    }
}