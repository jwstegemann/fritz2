package dev.fritz2.headless.foundation

import dev.fritz2.core.Id
import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.core.render
import dev.fritz2.headless.runTest
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.asList
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class HookTests {

    class CombineValueAndPayloadAsAttribute : Hook<Tag<HTMLDivElement>, Tag<HTMLSpanElement>, String>() {
        operator fun invoke(value: String) = apply {
            this.value = { payload ->
                span {
                    attr("data-value-payload", "$value-$payload")
                }
            }
        }
    }

    @Test
    fun callHookFunctionWithPayloadWillExecuteEffectAndAlsoExpression() = runTest {
        val sut = CombineValueAndPayloadAsAttribute()
        sut("hook").also {
            attr("data-also", "ok")
        }

        var result: Tag<HTMLSpanElement>? = null
        render {
            div {
                result = hook(sut, "payload")
            }
        }
        delay(100)

        assertNotNull(result)
        assertEquals(result!!.domNode.getAttribute("data-value-payload"), "hook-payload")
        assertEquals(result!!.domNode.getAttribute("data-also"), "ok")
    }

    class ValueAsAttribute : Hook<Tag<HTMLDivElement>, Tag<HTMLSpanElement>, Unit>() {
        operator fun invoke(value: String) = apply {
            this.value = {
                span {
                    attr("data-value", "$value")
                }
            }
        }
    }

    @Test
    fun callHookFunctionWithoutPayloadWillExecuteEffectAndAlsoExpression() = runTest {
        val sut = ValueAsAttribute()
        sut("hook").also {
            attr("data-also", "ok")
        }

        var result: Tag<HTMLSpanElement>? = null
        render {
            div {
                result = hook(sut)
            }
        }
        delay(100)

        assertNotNull(result)
        assertEquals(result!!.domNode.getAttribute("data-value"), "hook")
        assertEquals(result!!.domNode.getAttribute("data-also"), "ok")
    }

    @Test
    fun callMultipleHookFunctionWithoutPayloadWillExecuteEachEffectAndAlsoExpression() = runTest {
        val expected = listOf("A", "B", "C")
        val suts = expected.map { value ->
            ValueAsAttribute().apply {
                this(value).also {
                    attr("data-also", value)
                }
            }
        }

        var parent: Tag<HTMLDivElement>? = null
        render {
            parent = div {
                hook(*suts.toTypedArray())
            }
        }
        delay(100)

        assertNotNull(parent)
        val results = parent!!.domNode.childNodes.asList()
        assertEquals(results.count(), 3)
        assertContentEquals(
            results.map {
                val span = it as HTMLSpanElement
                span.getAttribute("data-value")
            },
            expected
        )
        assertContentEquals(
            results.map {
                val span = it as HTMLSpanElement
                span.getAttribute("data-also")
            },
            expected
        )
    }

    class DivHook : TagHook<Tag<HTMLDivElement>, String, String>() {
        override fun RenderContext.renderTag(
            classes: String?,
            id: String?,
            data: String,
            payload: String
        ): Tag<HTMLDivElement> = div(classes, id) {
            attr("data-value-payload", "$data-$payload")
        }
    }

    @Test
    fun callHookWithSpecialTagPayloadWillExecuteEffectAndAlsoExpression() = runTest {
        val sut = DivHook()
        sut("TagHook").also {
            attr("data-also", "ok")
        }

        val id = Id.next()
        val classes = "fritz2-style"

        var result: Tag<HTMLDivElement>? = null
        render {
            result = hook(sut, classes, id, "payload")
        }
        delay(100)

        assertNotNull(result)
        assertEquals(result!!.baseClass, classes)
        assertEquals(result!!.id, id)
        assertEquals(result!!.domNode.getAttribute("data-value-payload"), "TagHook-payload")
        assertEquals(result!!.domNode.getAttribute("data-also"), "ok")
    }

}