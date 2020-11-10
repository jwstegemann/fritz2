package dev.fritz2.dom

import dev.fritz2.binding.const
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


class TagTests {

    @Test
    fun testSingleTag() = runTest {
        initDocument()

        val testId = uniqueId()
        val testClass = "testClass"

        render {
            div(id = testId) {
                className(flowOf(testClass))
            }
        }.mount(targetId)

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals(testClass, element.className)
    }

    @Test
    fun testSingleTagWithBaseClass() = runTest {
        initDocument()

        val testId = uniqueId()
        val baseClass = "baseClass"
        val testClass = "testClass"

        render {
            div(baseClass = baseClass, id = testId) {
                className(flowOf(testClass))
            }
        }.mount(targetId)

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals("$baseClass $testClass", element.className)
    }

    @Test
    fun testMultipleTags() = runTest {
        initDocument()

        val testRange = (0..4)
        val testIds = testRange.map { "testId$it" }
        val testClasses = testRange.map { "testClass$it" }

        render {
            ul(id = "list") {
                (const(testIds)).renderEach() {
                    li(id = it) {
                        classList(flowOf(testClasses))
                    }
                }
            }
        }.mount(targetId)

        delay(500)

        for (i in testRange) {
            val element = document.getElementById(testIds[i]).unsafeCast<HTMLDivElement>()
            assertEquals(testIds[i], element.id)
            assertEquals("li", element.localName)
            assertEquals(testClasses.joinToString(separator = " "), element.className, "wrong classes for $i")
        }
    }

}