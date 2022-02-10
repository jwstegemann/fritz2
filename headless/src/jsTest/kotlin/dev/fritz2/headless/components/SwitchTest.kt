package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.render
import dev.fritz2.headless.test.*
import dev.fritz2.identification.Id
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
import kotlin.test.Test
import kotlin.test.assertEquals

class SwitchTest {

    @Test
    fun testSwitchStructure() = runTest {
        initDocument()

        val switchId = "switch-${Id.next()}"
        val switchToggleId = "switch-toggle-${Id.next()}"
        val switchState = storeOf(false)

        render {
            switch("classes", switchId, { set(scopeTestKey, scopeTestValue) }, RenderContext::div) {
                value(switchState)
                scope.asDataAttr(scopeTestKey)
                span(id = switchToggleId) {
                    attr("data-enabled", enabled.map { it.toString() })
                }
            }
        }

        delay(100)
        val switchElement = getElementById<HTMLDivElement>(switchId)
        assertEquals(switchId, switchElement.id)
        assertEquals("DIV", switchElement.tagName)
        assertEquals("classes", switchElement.className)
        assertEquals(scopeTestValue, switchElement.getAttribute("data-${scopeTestKey.name}"))

        val switchToggleElement = getElementById<HTMLSpanElement>(switchToggleId)
        assertEquals("false", switchToggleElement.getAttribute("data-enabled"))

        getElementById<HTMLDivElement>(switchId).click()
        delay(100)
        assertEquals("true", switchToggleElement.getAttribute("data-enabled"))
    }

}