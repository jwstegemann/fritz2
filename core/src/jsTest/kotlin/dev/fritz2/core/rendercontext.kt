package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RenderContextTests {

    @Test
    fun testMountTargetNotFoundException() = runTest {
        assertFailsWith(MountTargetNotFoundException::class) {
            render("missing") {
                div { +"div" }
            }
        }
    }

    @Test
    fun testShortRenderFunction() = runTest {

        val store = storeOf(true)

        val divId = Id.next()

        render {
            section {
                store.data.render { value ->
                    div(id = divId) {
                        +if (value) "on" else "off"
                    }
                }
            }
        }

        delay(100)

        val div = document.getElementById(divId) as HTMLDivElement

        assertEquals("on", div.textContent)

        store.update(false)
        delay(200)

        val div2 = document.getElementById(divId) as HTMLDivElement

        assertEquals("off", div2.textContent)
    }

    @Test
    fun testRenderFunction() = runTest {
        document.body?.id = "target"

        val store = storeOf(true)
        val divId = Id.next()

        render("#target") {
            div(id = divId) {
                store.data.render { value ->
                    if (value) div { +"on" } else span { +"off" }
                }
            }
        }

        delay(100)

        val target = document.getElementById("target") as HTMLBodyElement
        assertEquals("target", target.id)
        assertEquals(1, target.childElementCount)
        assertEquals(divId, target.firstElementChild?.id)

        val div = document.getElementById(divId) as HTMLDivElement

        assertEquals(1, div.firstElementChild?.childElementCount)
        assertEquals("DIV", div.firstElementChild?.firstChild?.nodeName)
        assertEquals("on", div.firstElementChild?.textContent)

        store.update(false)
        delay(200)

        val span = document.getElementById(divId) as HTMLDivElement
        assertEquals(1, span.firstElementChild?.childElementCount)
        assertEquals("SPAN", span.firstElementChild?.firstChild?.nodeName)
        assertEquals("off", span.firstElementChild?.textContent)
    }

    @Test
    fun testRenderReactsOnlyToNewValues() = runTest {
        document.body?.id = "target"

        fun getSpanText(id: String) = (document.getElementById(id) as HTMLSpanElement).textContent

        data class Model(val static: String, val reactive: String)

        val uniqueHashes = generateSequence(1) { it + 1 }.map(Int::toString).iterator()
        val model = Model("fritz", "RC-3")
        val store = storeOf(model)
        val idValueReactive = Id.next()
        val idHash = Id.next()

        render("#target") {
            div {
                store.data.map { it.static }.render { _ ->
                    span(id = idHash) { +uniqueHashes.next() }
                }
                store.data.map { it.reactive }.render { reactive ->
                    span(id = idValueReactive) { +reactive }
                }
            }
        }

        delay(100)

        assertEquals("RC-3", getSpanText(idValueReactive))
        assertEquals("1", getSpanText(idHash))

        // update with repeated value should omit rendering
        store.update(model.copy(reactive = "RC-4"))
        delay(100)

        assertEquals("RC-4", getSpanText(idValueReactive))
        assertEquals("1", getSpanText(idHash))

        // update with new value should trigger re-render
        store.update(Model("fritz2", "1.0-FINAL"))
        delay(100)

        assertEquals("1.0-FINAL", getSpanText(idValueReactive))
        assertEquals("2", getSpanText(idHash))
    }
}