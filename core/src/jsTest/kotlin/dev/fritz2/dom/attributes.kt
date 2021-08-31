package dev.fritz2.dom

import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
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
                attr(name2, values2)
                attr(name3, flowOf(values3))

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

        assertEquals(values2.joinToString(separator = " "), element.getAttribute(name2))
        assertEquals(values3.joinToString(separator = " "), element.getAttribute(name3))

        assertEquals(value0, element.getAttribute(name0))
        assertEquals(value1, element.getAttribute(name1))

        assertTrue(element.hasAttribute("test4"))
        assertEquals("", element.getAttribute("test4"))

        assertFalse(element.hasAttribute("test5"))

        assertTrue(element.hasAttribute("test6"))
        assertEquals("foo", element.getAttribute("test6"))
    }
}