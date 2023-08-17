package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.asList
import kotlin.test.*

class HookTests {

    class CombineValueAndPayloadAsAttribute : Hook<Tag<HTMLDivElement>, Tag<HTMLSpanElement>, String>() {
        operator fun invoke(value: String) = apply {
            this.value = { payload, alsoExpr ->
                span {
                    attr("data-value-payload", "$value-$payload")
                }.apply { alsoExpr?.let { it() } }
            }
        }
    }

    @Test
    fun callHookFunctionWithPayloadWillExecuteEffectAndAlsoExpression() = runTest {
        val divId = Id.next()

        val sut = CombineValueAndPayloadAsAttribute()
        sut("hook").also {
            attr("data-also", "ok")
        }

        render {
            div(id = divId) {
                hook(sut, "payload")
            }
        }
        delay(100)

        val result: HTMLSpanElement? = document.getElementById(divId)?.let { (it as HTMLDivElement).firstChild as HTMLSpanElement }
        assertNotNull(result)
        assertEquals("hook-payload", result.getAttribute("data-value-payload"))
        assertEquals("ok", result.getAttribute("data-also"))
    }

    class ValueAsAttribute : Hook<Tag<HTMLDivElement>, Tag<HTMLSpanElement>, Unit>() {
        operator fun invoke(value: String) = apply {
            this.value = { _, alsoExpr ->
                span {
                    attr("data-value", value)
                }.apply { alsoExpr?.let { it() } }
            }
        }
    }

    @Test
    fun callHookFunctionWithoutPayloadWillExecuteEffectAndAlsoExpression() = runTest {
        val divId = Id.next()

        val sut = ValueAsAttribute()
        sut("hook").also {
            attr("data-also", "ok")
        }

        render {
            div(id = divId) {
                hook(sut)
            }
        }
        delay(100)

        val result: HTMLSpanElement? = document.getElementById(divId)?.let { (it as HTMLDivElement).firstChild as HTMLSpanElement }
        assertNotNull(result)
        assertEquals("hook", result.getAttribute("data-value"))
        assertEquals("ok", result.getAttribute("data-also"))
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
        assertEquals(3, results.count())
        assertContentEquals(
            expected,
            results.map {
                val span = it as HTMLSpanElement
                span.getAttribute("data-value")
            },
        )
        assertContentEquals(
            expected,
            results.map {
                val span = it as HTMLSpanElement
                span.getAttribute("data-also")
            },
        )
    }

    class TagPaloadSpyingHook : Hook<RenderContext, Unit, TagPayload<String>>() {
        var data: String? = null
        var id: String? = null
        var classes: String? = null
        var payload: String? = null

        operator fun invoke(data: String) = apply {
            value = { (classes, id, payload), alsoExpr ->
                this@TagPaloadSpyingHook.data = data
                this@TagPaloadSpyingHook.id = id
                this@TagPaloadSpyingHook.classes = classes
                this@TagPaloadSpyingHook.payload = payload
                alsoExpr?.let { it(Unit) }
            }
        }
    }

    @Test
    fun callHookWithSpecialTagPayloadWillExecuteEffectAndAlsoExpression() = runTest {
        val id = Id.next()
        val classes = "fritz2-style"
        var alsoWasCalled = false
        val sut = TagPaloadSpyingHook()
        sut("TagHook").also {
            alsoWasCalled = true
        }

        render {
            hook(sut, classes, id, "payload")
        }
        delay(100)

        assertEquals("TagHook", sut.data)
        assertEquals(id, sut.id)
        assertEquals(classes, sut.classes)
        assertEquals("payload", sut.payload)
        assertTrue(alsoWasCalled)
    }

    class DivHook : TagHook<Tag<HTMLDivElement>, String, String>() {
        override fun RenderContext.renderTag(
            classes: String?,
            id: String?,
            data: String,
            payload: String,
        ): Tag<HTMLDivElement> = div(classes, id) {
            attr("data-value-payload", "$data-$payload")
        }
    }

    @Test
    fun callInvokeOfTagHookWithStaticValuesWillLeadToRenderTagInvocationWithProvidedValues() = runTest {
        val id = Id.next()
        val classes = "fritz2-style"
        val sut = DivHook()
        sut("TagHook").also {
            attr("data-also", "ok")
        }

        render {
            hook(sut, classes, id, "payload")
        }
        delay(100)

        val result = getElementById<HTMLDivElement>(id)
        assertNotNull(result)
        assertEquals(classes, result.className)
        assertEquals(id, result.id)
        assertEquals("TagHook-payload", result.getAttribute("data-value-payload"))
        assertEquals("ok", result.getAttribute("data-also"))
    }

    @Test
    fun callInvokeOfTagHookWithDynamicValuesWillLeadToRenderTagInvocationWithProvidedValues() = runTest {
        val values = storeOf("first")
        val id = Id.next()
        val classes = "fritz2-style"
        val sut = DivHook()
        sut(values.data).also {
            attr("data-also", "ok")
        }

        render {
            hook(sut, classes, id, "payload")
        }
        delay(100)

        var result = getElementById<HTMLDivElement>(id)
        assertNotNull(result)
        assertEquals(classes, result.className)
        assertEquals(id, result.id)
        assertEquals("first-payload", result.getAttribute("data-value-payload"))
        assertEquals("ok", result.getAttribute("data-also"))

        values.update("second")
        delay(100)
        result = getElementById(id)
        assertEquals("second-payload", result.getAttribute("data-value-payload"))
    }
}
