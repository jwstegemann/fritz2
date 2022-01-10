package dev.fritz2.dom

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
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

        val testId = Id.next()
        val testClass = "testClass"

        render {
            div(id = testId) {
                className(flowOf(testClass))
            }
        }

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals(testClass, element.className)
    }

    @Test
    fun testSingleTagWithBaseClass() = runTest {
        initDocument()

        val testId = Id.next()
        val baseClass = "baseClass"
        val testClass = "testClass"

        render {
            div(baseClass = baseClass, id = testId) {
                className(flowOf(testClass))
            }
        }

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals("$baseClass $testClass", element.className)
    }

    @Test
    fun testSingleTagWithBaseClassOnly() = runTest {
        initDocument()

        val testId = Id.next()
        val baseClass = "baseClass"

        render {
            div(baseClass = baseClass, id = testId) {
                className(flowOf(""))
            }
        }

        delay(100)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals(baseClass, element.className)
    }

    @Test
    fun testMultipleTags() = runTest {
        initDocument()

        val testRange = (0..4)
        val testIds = testRange.map { "testId$it" }
        val testClasses = testRange.map { "testClass$it" }

        render {
            ul(id = "list") {
                (flowOf(testIds)).renderEach(into = this) {
                    li(id = it) {
                        classList(flowOf(testClasses))
                    }
                }
            }
        }

        delay(500)

        for (i in testRange) {
            val e = document.getElementById("list")
            console.error(e?.outerHTML + "\n\n\n")


            val element = document.getElementById(testIds[i]).unsafeCast<HTMLDivElement>()
            assertEquals(testIds[i], element.id)
            assertEquals("li", element.localName)
            assertEquals(testClasses.joinToString(separator = " "), element.className, "wrong classes for $i")
        }
    }

    @Test
    fun testRenderWithCondition() = runTest {
        initDocument()

        val outerId = Id.next()
        val innerId = Id.next()

        val switch = storeOf(false)
        val data = storeOf("Test")

        render {
            div(id = outerId) {
                switch.data.render {
                    if (it) {
                        data.data.render { text ->
                            if (text.isNotBlank()) div(id = innerId) { +text }
                        }
                    }
                }
            }
        }

        delay(300)

        val outer = document.getElementById(outerId) as HTMLDivElement
        fun inner() = document.getElementById(innerId) as HTMLDivElement

        assertEquals(0, outer.firstElementChild?.childElementCount, "outer element has a children")

        for(i in 0..2) {
            switch.update(true)
            delay(200)

            assertEquals(1, outer.firstElementChild?.childElementCount, "[$i] outer element has no children")
            assertEquals("Test", inner().textContent, "[$i] inner has no text")

            switch.update(false)
            delay(200)

            assertEquals(0, outer.firstElementChild?.childElementCount, "[$i] outer element has no children")
        }
    }

    @Test
    fun testAnnex() = runTest {
        initDocument()

        val contentId = Id.next()

        render {
            div(id = contentId) {
                div {
                    +"inner div"
                }.annex.apply {
                    span { +"outer div" }
                }
                span { +"after inner div" }
            }
        }

        delay(100)

        assertEquals(
            document.getElementById(contentId)?.innerHTML,
            "<div>inner div</div><span>outer div</span><span>after inner div</span>"
        )
    }

}