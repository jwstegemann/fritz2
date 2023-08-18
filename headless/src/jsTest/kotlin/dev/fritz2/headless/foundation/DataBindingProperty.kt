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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLUListElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataBindingPropertyTest {

    private val validation = validation<Boolean, ComponentValidationMessage> {
        if (it.data) add(errorMessage(it.path, "error"))
    }

    @Test
    fun testDataBindingPropertyNative() = runTest {
        val id = Id.next()
        val id1 = Id.next()
        val id2 = Id.next()
        val prop = DatabindingProperty<String>()
        val state = MutableStateFlow("a")
        prop(id, state)

        render {
            div(id = prop.id) {
                attr("prop", prop.data)
            }
            div(id = id1) {
                attr("prop", prop.data.map { "$it." })
            }
            div(id = id2) {
                attr("prop", prop.data.map { "$it+" })
            }
        }

        delay(100)
        val div = getElementById<HTMLDivElement>(id)
        val div1 = getElementById<HTMLDivElement>(id1)
        val div2 = getElementById<HTMLDivElement>(id2)
        assertTrue(prop.isSet)
        assertEquals("a", div.getAttribute("prop"))
        assertEquals("a.", div1.getAttribute("prop"))
        assertEquals("a+", div2.getAttribute("prop"))

        state.value = "b"
        delay(100)
        assertEquals("b", div.getAttribute("prop"))
        assertEquals("b.", div1.getAttribute("prop"))
        assertEquals("b+", div2.getAttribute("prop"))
    }

    @Test
    fun testDataBindingPropertyWithStore() = runTest {
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
