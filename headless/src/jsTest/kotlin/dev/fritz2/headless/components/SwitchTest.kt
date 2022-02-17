package dev.fritz2.headless.components

import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.render
import dev.fritz2.headless.model.TestModel
import dev.fritz2.headless.test.getElementById
import dev.fritz2.headless.test.runTest
import dev.fritz2.headless.test.scopeTestKey
import dev.fritz2.headless.test.scopeTestValue
import dev.fritz2.identification.Id
import dev.fritz2.validation.storeOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.test.Test
import kotlin.test.assertEquals

class SwitchTest {

    @Test
    fun testSwitch() = runTest {
        val name = "switch"
        val componentId = "$name-${Id.next()}"
        val componentState = storeOf(TestModel(), TestModel.validation).sub(TestModel.switch)

        render {
            switch("classes", componentId, { set(scopeTestKey, scopeTestValue) }, RenderContext::button) {
                value(componentState)
                scope.asDataAttr(scopeTestKey)
                span(id = value.id) {
                    attr("data-state", enabled.map { it.toString() })
                    attr("data-hasError", value.hasError.map { it.toString() })
                    attr("data-message", value.validationMessages.map { it.firstOrNull()?.message ?: "" })
                }
            }
        }

        delay(100)
        val switchElement = getElementById<HTMLButtonElement>(componentId)
        assertEquals(componentId, switchElement.id)
        assertEquals("BUTTON", switchElement.tagName)
        assertEquals("classes", switchElement.className)
        assertEquals(scopeTestValue, switchElement.getAttribute("data-${scopeTestKey.name}"))

        val switchToggleElement = getElementById<HTMLSpanElement>(componentState.id)
        assertEquals("false", switchToggleElement.getAttribute("data-state"), "wrong state")
        assertEquals("false", switchToggleElement.getAttribute("data-hasError"), "wrong hasError")
        assertEquals("", switchToggleElement.getAttribute("data-message"), "wrong message")

        switchElement.click()
        delay(100)
        assertEquals("true", switchToggleElement.getAttribute("data-state"), "wrong state after action click")
        assertEquals("true", switchToggleElement.getAttribute("data-hasError"), "wrong state after action click")
        assertEquals("error", switchToggleElement.getAttribute("data-message"), "wrong state after action click")

        val spacePress = KeyboardEvent("keydown", KeyboardEventInit(Keys.Space.key, Keys.Space.key))
        switchElement.dispatchEvent(spacePress)
        delay(100)
        assertEquals("false", switchToggleElement.getAttribute("data-state"), "wrong state")
        assertEquals("false", switchToggleElement.getAttribute("data-hasError"), "wrong hasError")
        assertEquals("", switchToggleElement.getAttribute("data-message"), "wrong message")
    }

}