package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.render
import dev.fritz2.headless.test.*
import dev.fritz2.identification.Id
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLSpanElement
import kotlin.test.Test
import kotlin.test.assertEquals

class SwitchTest {

    @Test
    fun testSwitchStructure() = runTest {
        initDocument()
        val switchId = "switch-${Id.next()}"
        val switchState = storeOf(false)

        render {
            switch("classes", switchId, { set(scopeTestKey, scopeTestValue) }, RenderContext::button) {
                value(switchState)
                scope.asDataAttr(scopeTestKey)
                span(id = value.id) {
                    attr("data-state", enabled.map { it.toString() })
                    attr("data-hasError", value.hasError.map { it.toString() })
                    attr("data-message", value.validationMessages.map { it.firstOrNull().toString() })
                }
            }
        }

        delay(100)
        val switchElement = getElementById<HTMLButtonElement>(switchId)
        assertEquals(switchId, switchElement.id)
        assertEquals("BUTTON", switchElement.tagName)
        assertEquals("classes", switchElement.className)
        assertEquals(scopeTestValue, switchElement.getAttribute("data-${scopeTestKey.name}"))

        val switchToggleElement = getElementById<HTMLSpanElement>(switchState.id)
        assertEquals("false", switchToggleElement.getAttribute("data-state"))
        assertEquals("false", switchToggleElement.getAttribute("data-hasError"))
        assertEquals("null", switchToggleElement.getAttribute("data-message"))

        switchElement.click()
        delay(100)
        assertEquals("true", switchToggleElement.getAttribute("data-state"))
        assertEquals("false", switchToggleElement.getAttribute("data-hasError"))
        assertEquals("null", switchToggleElement.getAttribute("data-message"))
    }

}