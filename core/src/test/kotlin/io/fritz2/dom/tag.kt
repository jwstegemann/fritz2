package io.fritz2.dom

import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class TagTests {

    @Test
    fun testSingleTag() = runTest {
        initDocument()

        val testId = "testId"
        val testClass = "testClass"

        html {
            div(testId) {
                `class` = !testClass
            }
        }.mount("target")

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals(testClass, element.className)
    }

    @Test
    fun testMultipleTags() = runTest {
        initDocument()

        val testRange = (0..4)
        val testIds = testRange.map { "testId$it" }
        val testClasses = testRange.map { "testClass$it" }

        html {
            ul("list") {
                (!testIds).each().map {
                    html {
                        li(it) {
                            classes = !testClasses
                            //attribute("class", testClasses.joinToString(separator = " "))
                            //attribute("class", "hugo")
                        }
                    }
                }.bind()
            }
        }.mount("target")

        delay(500)

        for(i in testRange) {
            val element = document.getElementById(testIds[i]).unsafeCast<HTMLDivElement>()
            assertEquals(testIds[i], element.id)
            assertEquals("li", element.localName)
            assertEquals(testClasses.joinToString(separator = " "), element.className, "wrong classes for $i")
        }
    }

}