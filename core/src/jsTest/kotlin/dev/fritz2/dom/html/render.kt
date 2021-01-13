package dev.fritz2.dom.html

import dev.fritz2.binding.storeOf
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class RenderTests {

    @Test
    fun testShortRenderFunction() = runTest {
        initDocument()

        val store = storeOf(true)

        val divId = uniqueId()

        render(targetId) {
            section {
                store.data.renderElement { value ->
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
        initDocument()

        val store = storeOf(true)

        val divId = uniqueId()

        render(targetId) {
            div(id = divId) {
                store.data.render { value ->
                    if (value) div { +"on" } else span { +"off" }
                }
            }
        }

        delay(100)

        val div = document.getElementById(divId) as HTMLDivElement

        assertEquals(1, div.childElementCount)
        assertEquals("DIV", div.firstChild?.nodeName)
        assertEquals("on", div.textContent)

        store.update(false)
        delay(200)

        val span = document.getElementById(divId) as HTMLDivElement
        assertEquals(1, span.childElementCount)
        assertEquals("SPAN", span.firstChild?.nodeName)
        assertEquals("off", span.textContent)
    }
}