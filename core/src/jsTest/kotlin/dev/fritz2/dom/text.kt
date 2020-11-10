package dev.fritz2.dom

import dev.fritz2.dom.html.render
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals


class TextTests {

    @Test
    fun testTextOnString() = runTest {
        initDocument()

        val id1 = uniqueId()
        val id2 = uniqueId()
        val text = "testText"

        render {
            section {
                div(id = id1) {
                    +text
                }
                div(id = id2) {
                    +text
                }
            }
        }.mount(targetId)

        delay(100)

        val div1 = document.getElementById(id1) as HTMLDivElement

        assertEquals(id1, div1.id)
        assertEquals(text, div1.textContent)

        val div2 = document.getElementById(id2) as HTMLDivElement

        assertEquals(id2, div2.id)
        assertEquals(text, div2.textContent)
    }

    @Test
    fun testTextOnFlowOfString() = runTest {
        initDocument()

        val testId = uniqueId()
        val text = "testText"

        render {
            div(id = testId) {
                flowOf(text).asText()
            }
        }.mount(targetId)

        delay(100)

        val element = document.getElementById(testId) as HTMLDivElement

        assertEquals(testId, element.id)
        assertEquals(text, element.textContent)
    }

}