package io.fritz2.dom

import io.fritz2.dom.html.html
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class TextTests {

    @Test
    fun testTextOnString() = runTest {
        initDocument()

        val testId = "testId"
        val testText = "testText"

        html {
            div(testId) {
                +testText
            }
        }.mount("target")

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals(testText, element.textContent)
    }

    @Test
    fun testTextOnFlowOfString() = runTest {
        initDocument()

        val testId = "testId"
        val testText = "testText"

        html {
            div(testId) {
                +!testText
            }
        }.mount("target")

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals(testText, element.textContent)
    }

    @Test
    fun testTextBind() = runTest {
        initDocument()

        val testId = "testId"
        val testText = "testText"

        html {
            div(testId) {
                (!testText).bind()
            }
        }.mount("target")

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals(testText, element.textContent)
    }

}