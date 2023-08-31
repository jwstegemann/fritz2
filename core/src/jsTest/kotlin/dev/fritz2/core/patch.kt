package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import kotlin.test.Test
import kotlin.test.assertEquals

class PatchTests {

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

    private val valueLens = lensOf("value", Entity::value) { p, v -> p.copy(value = v) }

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

        
        val store = TestEntityListStore()

        render {
            div {
                ul(id = listId) {
                    store.renderEach(Entity::id) {
                        val valueStore = it.map(valueLens)
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


    @Test
    fun testFastDeletePredecessor() = runTest {
        render {
            div(id = "div") {
                flowOf(
                    listOf("ABC"),
                    listOf("ABC", "CDE"),
                    listOf("ABC", "ABC", "CDE"),
                    listOf(
                        "ABC",
                        "CDE"
                    ), // The first element gets removed, the delete patch should still select the correct element
                    listOf()
                ).renderEach(into = this) {
                    p { +it }
                }
            }
        }

        delay(100)
        val div = document.getElementById("div")
        assertEquals(0, div!!.childElementCount)

    }

    @Test
    fun testRenderEach() = runTest {
        val store = storeOf("abcdef")
        render {
            div(id = "div") {
                store.data.map { it.toCharArray().map { "$it" } }.renderEach(into = this) {
                    p { +it }
                }
            }
        }

        delay(100)
        val div = document.getElementById("div") as HTMLElement

        suspend fun check(str:String){
            store.update(str)
            delay(100)
            assertEquals(str, div.innerText.filter { it.isLetter() })
            assertEquals(str.length, div.childElementCount)
        }

        check("abcdef")
        check("abcadefa")
        check("abcdefa")
        check("abdefa")
        check("aaabdea")
        check("aaaea")
        check("aaea")
        check("aae")
        check("")
    }
}