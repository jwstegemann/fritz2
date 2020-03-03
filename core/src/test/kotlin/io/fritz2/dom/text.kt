package io.fritz2.dom

import io.fritz2.dom.html.html
import io.fritz2.test.initDocument
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.js.Promise
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class TextTests {

    @BeforeTest
    fun setUp() {
        initDocument()
    }

    @Test
    fun testTextOnString(): Promise<Boolean> {

        val testId = "testId"
        val testText = "testText"

        html {
            div(testId) {
                +testText
            }
        }.mount("target")

        return GlobalScope.promise {
            delay(100)

            val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

            assertEquals(testId, element.id)
            assertEquals(testText, element.textContent)

            true
        }
    }

    @Test
    fun testTextOnFlowOfString(): Promise<Boolean> {

        val testId = "testId"
        val testText = "testText"

        html {
            div(testId) {
                +flowOf(testText)
            }
        }.mount("target")

        return GlobalScope.promise {
            delay(100)

            val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

            assertEquals(testId, element.id)
            assertEquals(testText, element.textContent)

            true
        }
    }

    @Test
    fun testTextBind(): Promise<Boolean> {

        val testId = "testId"
        val testText = "testText"

        html {
            div(testId) {
                flowOf(testText).bind()
            }
        }.mount("target")

        return GlobalScope.promise {
            delay(100)

            val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

            assertEquals(testId, element.id)
            assertEquals(testText, element.textContent)

            true
        }
    }

}