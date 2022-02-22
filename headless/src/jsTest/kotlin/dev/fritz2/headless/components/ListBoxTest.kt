package dev.fritz2.headless.components

import dev.fritz2.core.Id
import dev.fritz2.core.RenderContext
import dev.fritz2.core.render
import dev.fritz2.headless.model.TestModel
import dev.fritz2.headless.model.listBoxEntries
import dev.fritz2.headless.test.getElementById
import dev.fritz2.headless.test.runTest
import dev.fritz2.headless.test.scopeTestKey
import dev.fritz2.headless.test.scopeTestValue
import dev.fritz2.validation.storeOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLLabelElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ListBoxTest {

    @Test
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
                    +"Button"
                }
                listboxItems {
                    listBoxEntries.forEach { entry ->
                        listboxItem(entry) {
                            attr("data-index", index)
                            attr("data-active", active.map { it.toString() })
                            attr("data-disabled", disabled.map { it.toString() })
                            attr("data-selected", selected.map { it.toString() })
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
        assertEquals("classes", listBoxElement.className)
        assertEquals(scopeTestValue, listBoxElement.getAttribute("data-${scopeTestKey.name}"))

        val listBoxLabelElement = getElementById<HTMLLabelElement>("$componentId-label")
        assertEquals("Label", listBoxLabelElement.textContent, "wrong text-content")

        val listBoxButtonElement = getElementById<HTMLButtonElement>("$componentId-button")
        assertEquals("Button", listBoxButtonElement.textContent, "wrong text-content")

        val listBoxItemsElement = getElementById<HTMLDivElement>("$componentId-items")
        assertEquals(listBoxEntries.size, listBoxItemsElement.childElementCount, "wrong text-content")
    }

}