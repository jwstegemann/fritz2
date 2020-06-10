package dev.fritz2.dom

import dev.fritz2.binding.const
import dev.fritz2.binding.each
import dev.fritz2.dom.html.render
import dev.fritz2.test.initDocument
import dev.fritz2.test.randomId
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals


class TagTests {

    @Test
    fun testSingleTag() = runTest {
        initDocument()

        val testId = randomId()
        val testClass = "testClass"

        render {
            div(id = testId) {
                className = const(testClass)
            }
        }.mount(targetId)

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

        render {
            ul(id = "list") {
                (const(testIds)).each().map {
                    render {
                        li(id = it) {
                            classList = const(testClasses)
                        }
                    }
                }.bind()
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