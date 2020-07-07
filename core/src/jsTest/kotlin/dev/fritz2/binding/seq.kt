package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class SeqTests {

    private val listId = "list"

    private fun listContent() = document.getElementById(listId)?.textContent
    private fun clickButton(id: String) {
        (document.getElementById(id) as HTMLButtonElement).click()
    }

    class TestListStore() : RootStore<List<String>>(listOf("a","b","c","d")) {
        val append = handle { model -> model + "e" }
        val change = handle { model -> listOf(model.first(),"x") + model.takeLast(3) }
        val insert = handle { model -> listOf("y") + model }
        val delete = handle { model -> model.take(3) + model.takeLast(2) }
    }

    @Test
    fun testEachElement() = runTest {
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
                button(id = "append") { clicks handledBy store.append }
                button(id = "change") { clicks handledBy store.change }
                button(id = "insert") { clicks handledBy store.insert }
                button(id = "delete") { clicks handledBy store.delete }
            }
        }.mount(targetId)

        delay(200)
        assertEquals("abcd", listContent(),"list incorrect after init")

        clickButton("append")
        delay(200)
        assertEquals("abcde", listContent(),"list incorrect after append")

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


    @Test
    fun testEachIndexStore() = runTest {
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

        delay(500)
//        println("####### " + document.getElementById("hugo")?.textContent)
//        assertEquals("abcd", listContent(),"list incorrect after init")

        clickButton("append")
        delay(500)
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