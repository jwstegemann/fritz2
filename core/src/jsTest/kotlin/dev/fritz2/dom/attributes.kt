package dev.fritz2.dom

import dev.fritz2.core.storeOf
import dev.fritz2.identification.Id
import dev.fritz2.initDocument
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class AttributeTests {

    @Test
    fun testAttributes() = runTest {
        initDocument()

        val testRange = (0..4)
        val testId = Id.next()

        val (name0, value0) = "test0" to "value0"
        val (name1, value1) = "test1" to "value1"
        val (name2, values2) = "test2" to testRange.map { "value$it" }
        val (name3, values3) = "test3" to testRange.map { "value$it" }

        render {
            div(id = testId) {
                attr(name0, value0)
                attr(name1, flowOf(value1))
                attr("data-$name0", value0)
                attr("data-$name1", flowOf(value1))
                attr(name2, values2.joinToString(" "))
                attr(name3, flowOf(values3.joinToString(" ")))

                attr("test4", flowOf(true))
                attr("test5", flowOf(false))
                attr("test6", flowOf(true), "foo")
            }
        }

        delay(200)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)

        assertEquals(value0, element.getAttribute(name0))
        assertEquals(value1, element.getAttribute(name1))

        assertEquals(value0, element.getAttribute("data-$name0"))
        assertEquals(value1, element.getAttribute("data-$name1"))

        assertEquals(values2.joinToString(" "), element.getAttribute(name2))
        assertEquals(values3.joinToString(" "), element.getAttribute(name3))

        assertEquals(value0, element.getAttribute(name0))
        assertEquals(value1, element.getAttribute(name1))

        assertTrue(element.hasAttribute("test4"))
        assertEquals("", element.getAttribute("test4"))

        assertFalse(element.hasAttribute("test5"))

        assertTrue(element.hasAttribute("test6"))
        assertEquals("foo", element.getAttribute("test6"))
    }

    @Test
    fun testNullableAttributes() = runTest {
        initDocument()
        val testId = Id.next()

        render {
            div(id = testId) {
                attr("nullableString", null as String?)
                attr("nullableFlowOfString", flowOf(null as String?))
                attr("nullableT", null as Int?)
                attr("nullableFlowOfT", flowOf(null as Int?))
                attr("nullableBoolean", null as Boolean?, "nullableBoolean")
                attr("nullableFlowOfBoolean", flowOf(null as Boolean?), "nullableFlowOfBoolean")
            }
        }

        delay(200)
        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertFalse(element.hasAttribute("nullableString"))
        assertFalse(element.hasAttribute("nullableFlowOfString"))
        assertFalse(element.hasAttribute("nullableT"))
        assertFalse(element.hasAttribute("nullableFlowOfT"))
        assertFalse(element.hasAttribute("nullableBoolean"))
        assertFalse(element.hasAttribute("nullableFlowOfBoolean"))
    }

    @Test
    fun testAlternatingNullableStringFlows() = runTest {
        initDocument()
        val testId = Id.next()

        val nullableFlow = storeOf<String?>("a")

        render {
            div(id = testId) {
                attr("test", nullableFlow.data)
            }
        }

        delay(50)
        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertTrue(element.hasAttribute("test"))
        assertEquals("a", element.getAttribute("test"))

        nullableFlow.update(null)
        delay(50)

        assertFalse(element.hasAttribute("test"))

        nullableFlow.update("c")
        delay(50)

        assertTrue(element.hasAttribute("test"))
        assertEquals("c", element.getAttribute("test"))
    }


    @Test
    fun testAlternatingNullableTFlows() = runTest {
        initDocument()
        val testId = Id.next()

        val nullableFlow = storeOf<Int?>(42)

        render {
            div(id = testId) {
                attr("test", nullableFlow.data)
            }
        }

        delay(50)
        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertTrue(element.hasAttribute("test"))
        assertEquals("42", element.getAttribute("test"))

        nullableFlow.update(null)
        delay(50)

        assertFalse(element.hasAttribute("test"))

        nullableFlow.update(99)
        delay(50)

        assertTrue(element.hasAttribute("test"))
        assertEquals("99", element.getAttribute("test"))
    }
}