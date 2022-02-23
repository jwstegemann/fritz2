package dev.fritz2.headless.components

import dev.fritz2.core.Id
import dev.fritz2.core.RenderContext
import dev.fritz2.core.asElementList
import dev.fritz2.core.render
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.model.TestModel
import dev.fritz2.headless.model.listBoxEntries
import dev.fritz2.headless.runTest
import dev.fritz2.headless.scopeTestKey
import dev.fritz2.headless.scopeTestValue
import dev.fritz2.validation.storeOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLUListElement
import kotlin.test.*

class ListBoxTest {

    @Test
    @Ignore
    fun testListBox() = runTest {
        val name = "listBox"
        val componentId = "$name-${Id.next()}"
        val componentState = storeOf(TestModel(), TestModel.validation).sub(TestModel.listBox)

        render {
            listbox<String, HTMLDivElement>("classes", componentId, { set(scopeTestKey, scopeTestValue) }, RenderContext::div) {
                value(componentState)
                scope.asDataAttr(scopeTestKey)
                listboxLabel {
                    +"Label"
                }
                listboxButton {
                    value.data.renderText()
                }
                listboxItems(tag = RenderContext::ul) {
                    listBoxEntries.forEach { entry ->
                        listboxItem(entry, tag = RenderContext::li) {
                            attr("data-index", index)
                            attr("data-active", active.map { it.toString() })
                            attr("data-selected", selected.map { it.toString() })
                            attr("data-disabled", disabled.map { it.toString() })
                            if(index % 2 == 0) disable(true)
                        }
                    }
                }
                listboxValidationMessages {
                    attr("data-hasError", value.hasError.map { it.toString() })
                    attr("data-message", value.validationMessages.map { it.firstOrNull()?.message ?: "" })
                }
            }
        }

        delay(100)
        val listBoxElement = getElementById<HTMLDivElement>(componentId)
        assertEquals(componentId, listBoxElement.id)
        assertEquals("DIV", listBoxElement.tagName)
        assertTrue(listBoxElement.className.contains("classes"))
        assertEquals(scopeTestValue, listBoxElement.getAttribute("data-${scopeTestKey.name}"))

        val listBoxLabelElement = getElementById<HTMLLabelElement>("$componentId-label")
        assertEquals("Label", listBoxLabelElement.textContent, "wrong text-content")

        val listBoxButtonElement = getElementById<HTMLButtonElement>("$componentId-button")
        assertEquals(listBoxEntries.first(), listBoxButtonElement.textContent, "wrong text-content")

        val listBoxItemsElement = getElementById<HTMLUListElement>("$componentId-items")
        assertEquals("UL", listBoxItemsElement.tagName)
        assertEquals(listBoxEntries.size, listBoxItemsElement.childElementCount)
        val items = listBoxItemsElement.childNodes.asElementList()
        for((index, item) in items.withIndex()) {
            assertEquals("$index", item.getAttribute("data-index"), "wrong index $index")
            assertEquals("false", item.getAttribute("data-active"), "wrong active $index")
            assertEquals(if(index == 0) "true" else "false", item.getAttribute("data-selected"), "wrong selected $index")
            assertEquals(if(index % 2 == 0) "true" else "false", item.getAttribute("data-disabled"), "wrong disabled $index")
        }

        assertFails { getElementById<HTMLDivElement>("$componentId-validation-messages") }
    }

}