package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.buildLens
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
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

    class TestListStore() : RootStore<List<String>>(listOf("a", "b", "c", "d")) {
        val append = handle { model -> model + "e" }
        val change = handle { model -> listOf(model.first(), "x") + model.takeLast(3) }
        val insert = handle { model -> listOf("y") + model }
        val delete = handle { model -> model.take(3) + model.takeLast(2) }
    }

    @Test
    fun testEachElement() = runTest {
        val listId = "list" + uniqueId()
        val appendBtnId = "btn-append" + uniqueId()
        val changeBtnId = "btn-change" + uniqueId()
        val insertBtnId = "btn-insert" + uniqueId()
        val deleteBtnId = "btn-delete" + uniqueId()

        initDocument()

        val store = TestListStore()

        render {
            div {
                ul(id = listId) {
                    store.data.each().map {
                        render {
                            li { text(it) }
                        }
                    }.bind()
                }
                button(id = appendBtnId) { clicks handledBy store.append }
                button(id = changeBtnId) { clicks handledBy store.change }
                button(id = insertBtnId) { clicks handledBy store.insert }
                button(id = deleteBtnId) { clicks handledBy store.delete }
            }
        }.mount(targetId)

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


    @Test
    @Ignore
    fun testEachIndexStore() = runTest {
        val listId = "list" + uniqueId()
        val appendBtnId = "btn-append" + uniqueId()
        val changeBtnId = "btn-change" + uniqueId()
        val insertBtnId = "btn-insert" + uniqueId()
        val deleteBtnId = "btn-delete" + uniqueId()

        initDocument()

        val store = TestListStore()

        render {
            div {
                ul(id = listId) {
                    store.each().map {
                        render {
                            li { it.data.bind() }
                        }
                    }.bind()
                }
                button(id = "append") { clicks handledBy store.append }
                button(id = "change") { clicks handledBy store.change }
                button(id = "insert") { clicks handledBy store.insert }
                button(id = "delete") { clicks handledBy store.delete }

                div(id = "hugo") { store.data.map { it.toString() }.bind() }
            }
        }.mount(targetId)

        delay(200)
        assertEquals("abcd", listContent(listId), "list incorrect after init")

        clickButton(appendBtnId)
        delay(100)
        assertEquals("abcde", listContent(listId), "list incorrect after append")

        clickButton("change")
        delay(100)
        assertEquals("axcde", listContent(listId), "list incorrect after change")

        clickButton("insert")
        delay(100)
        assertEquals("yaxcde", listContent(listId), "list incorrect after insert")

        clickButton("delete")
        delay(100)
        assertEquals("yaxde", listContent(listId), "list incorrect after delete")
    }


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
    fun testEachEntity() = runTest {
        val listId = "list" + uniqueId()
        val appendBtnId = "btn-append" + uniqueId()
        val changeBtnId = "btn-change" + uniqueId()
        val insertBtnId = "btn-insert" + uniqueId()
        val deleteBtnId = "btn-delete" + uniqueId()

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
                button(id = appendBtnId) { clicks handledBy store.append }
                button(id = changeBtnId) { clicks handledBy store.change }
                button(id = insertBtnId) { clicks handledBy store.insert }
                button(id = deleteBtnId) { clicks handledBy store.delete }
            }
        }.mount(targetId)

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
        val listId = "list" + uniqueId()
        val appendBtnId = "btn-append" + uniqueId()
        val changeBtnId = "btn-change" + uniqueId()
        val insertBtnId = "btn-insert" + uniqueId()
        val deleteBtnId = "btn-delete" + uniqueId()

        initDocument()

        val store = TestEntityListStore()

        render {
            div {
                ul(id = listId) {
                    store.each(Entity::id).map {
                        val valueStore = it.sub(valueLens)
                        render {
                            li { valueStore.data.bind() }
                        }
                    }.bind()
                }
                button(id = appendBtnId) { clicks handledBy store.append }
                button(id = changeBtnId) { clicks handledBy store.change }
                button(id = insertBtnId) { clicks handledBy store.insert }
                button(id = deleteBtnId) { clicks handledBy store.delete }
            }
        }.mount(targetId)

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