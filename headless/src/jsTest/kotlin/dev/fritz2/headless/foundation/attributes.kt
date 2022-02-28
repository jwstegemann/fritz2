package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLInputElement
import kotlin.test.Test
import kotlin.test.assertEquals

class AttributeHookTest {

    @Test
    fun testAttributeHook() = runTest {
        val inputId = "input-${Id.next()}"
        val type =
            AttributeHook(HtmlTag<HTMLInputElement>::type, HtmlTag<HTMLInputElement>::type)
        val placeholder =
            AttributeHook(HtmlTag<HTMLInputElement>::placeholder, HtmlTag<HTMLInputElement>::placeholder)
        val max = AttributeHook(HtmlTag<HTMLInputElement>::max, HtmlTag<HTMLInputElement>::max)

        type("text")
        placeholder("foo")
        placeholder(flowOf("bar"))
        max("30")

        render {
            input(id = inputId) {
                hook(type, placeholder, max)
            }
        }

        delay(100)
        val input = getElementById<HTMLInputElement>(inputId)
        assertEquals("text", input.getAttribute("type"))
        assertEquals("bar", input.getAttribute("placeholder"))
        assertEquals("30", input.getAttribute("max"))
    }

    @Test
    fun testBooleanAttributeHook() = runTest {
        val inputId = "input-${Id.next()}"
        val readOnly =
            BooleanAttributeHook(HtmlTag<HTMLInputElement>::readOnly, HtmlTag<HTMLInputElement>::readOnly, "true")
        val required =
            BooleanAttributeHook(HtmlTag<HTMLInputElement>::required, HtmlTag<HTMLInputElement>::required, "true")

        readOnly(true)
        required(false)
        required(flowOf(true))

        render {
            input(id = inputId) {
                hook(readOnly, required)
            }
        }

        delay(100)
        val input = getElementById<HTMLInputElement>(inputId)
        assertEquals("true", input.getAttribute("readonly"))
        assertEquals("true", input.getAttribute("required"))
    }

}