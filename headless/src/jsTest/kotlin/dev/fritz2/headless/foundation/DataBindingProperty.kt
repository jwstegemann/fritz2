package dev.fritz2.headless.foundation

import dev.fritz2.core.Id
import dev.fritz2.core.render
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.runTest
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.headless.validation.errorMessage
import dev.fritz2.validation.storeOf
import dev.fritz2.validation.validation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLUListElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataBindingPropertyTest {

    private val validation = validation<Boolean, ComponentValidationMessage> {
        if(it.data) add(errorMessage(it.path, "error"))
    }

    @Test
    fun testDataBindingProperty() = runTest {
        val id = Id.next()
        val msgs = Id.next()
        val state = storeOf(false, validation, id)
        val prop = DatabindingProperty<Boolean>()
        prop(state)


        render {
            div(id = prop.id) {
                attr("state", state.data.asString())
                attr("prop", prop.data.asString())
            }
            ul(id = msgs) {
                attr("hasError", prop.hasError.asString())
                prop.validationMessages.renderEach(into = this) {
                    li { +it.message }
                }
            }
        }

        delay(100)
        val div = getElementById<HTMLDivElement>(id)
        val ul = getElementById<HTMLUListElement>(msgs)
        assertTrue(prop.isSet)
        assertEquals("false", div.getAttribute("state"))
        assertEquals("false", div.getAttribute("prop"))
        assertEquals("false", ul.getAttribute("hasError"))
        assertEquals(0, ul.childElementCount)

        prop.handler?.invoke(flowOf(true))
        delay(100)
        assertEquals("true", div.getAttribute("state"))
        assertEquals("true", div.getAttribute("prop"))
        assertEquals("true", ul.getAttribute("hasError"))
        assertEquals(1, ul.childElementCount)
        assertEquals("error", ul.firstElementChild?.textContent)
    }
}