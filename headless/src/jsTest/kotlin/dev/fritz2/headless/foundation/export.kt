package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.runTest
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExporterTest {

    @Test
    fun testExporter() = runTest {

        val id = Id.next()
        val inputId = Id.next()

        fun RenderContext.component(): HtmlTag<HTMLInputElement> = export {
            div(id = id) {
                export(
                    input(id = inputId) {
                        type("text")
                    }
                )
            }
        }

        var element: HTMLInputElement? = null
        render {
            element = component().domNode
        }

        delay(100)
        val div = getElementById<HTMLDivElement>(id)
        assertTrue(element is HTMLInputElement)
        assertEquals(inputId, element?.id)
        assertEquals(1, div.childElementCount)
        assertEquals(div.firstElementChild?.id, element?.id)
    }
}