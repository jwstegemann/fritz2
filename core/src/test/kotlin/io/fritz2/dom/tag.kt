package io.fritz2.dom

import io.fritz2.binding.each
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
class TagTests {

    @BeforeTest
    fun setUp() {
        initDocument()
    }

    @Test
    fun testSingleTag(): Promise<Boolean> {

        val testId = "testId"
        val testClass = "testClass"

        html {
            div(testId) {
                `class` = !testClass
            }
        }.mount("target")

        return GlobalScope.promise {
            delay(100)

            val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

            assertEquals(testId, element.id)
            assertEquals("div", element.localName)
            assertEquals(testClass, element.className)

            true
        }
    }

    @Test
    fun testMultipleTags(): Promise<Boolean> {

        val testRange = (0..4)
        val testIds = testRange.map { "testId$it" }
        val testClasses = testRange.map { "testClass$it" }

        html {
            ul("list") {
                flowOf(testIds).each().map {
                    html {
                        li(it) {
                            classes = flowOf(testClasses)
                        }
                    }
                }.bind()
            }
        }.mount("target")

        return GlobalScope.promise {
            delay(100)

            for(i in testRange) {
                val element = document.getElementById(testIds[i]).unsafeCast<HTMLDivElement>()
                assertEquals(testIds[i], element.id)
                assertEquals("li", element.localName)
                assertEquals(testClasses.joinToString(separator = " "), element.className)
            }
            true
        }
    }

}