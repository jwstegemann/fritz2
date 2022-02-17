package dev.fritz2.dom

import dev.fritz2.core.RootStore
import dev.fritz2.core.storeOf
import dev.fritz2.identification.Id
import dev.fritz2.initDocument
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLParagraphElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class TagTests {

    @Test
    fun testTextNodes() = runTest {

        initDocument()

        val id1 = Id.next()
        val id2 = Id.next()

        render {
            div {
                p(id = id1) {
                    +"Hello World1!"
                }
                p(id = id2) {
                    +"Hello World2!"
                }
            }
        }

        delay(250)

        val p1 = document.getElementById(id1).unsafeCast<HTMLParagraphElement>()
        val p2 = document.getElementById(id2).unsafeCast<HTMLParagraphElement>()

        assertEquals("Hello World1!", p1.innerText)
        assertEquals("Hello World2!", p2.innerText)
    }

    @Test
    fun testTextOnString() = runTest {
        initDocument()

        val id1 = Id.next()
        val id2 = Id.next()
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
        }

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

        val testId = Id.next()
        val text = "testText"

        render {
            div(id = testId) {
                flowOf(text).renderText()
            }
        }

        delay(100)

        val element = document.getElementById(testId) as HTMLDivElement

        assertEquals(testId, element.id)
        assertEquals(text, element.textContent)
    }

    @Test
    fun testCommentOnString() = runTest {
        initDocument()

        val id1 = Id.next()
        val comment = "testComment"

        render {
            section {
                div(id = id1) {
                    !comment
                }
            }
        }

        delay(100)

        val div1 = document.getElementById(id1) as HTMLDivElement

        assertEquals(id1, div1.id)
        assertEquals(8, div1.firstChild?.nodeType)
        assertEquals(comment, div1.firstChild?.nodeValue)
    }

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

        for (i in 0..2) {
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

    @Test
    fun testWheneverWithStaticData() = runTest {
        initDocument()

        val steering = storeOf(false)
        val getAttribute = { document.getElementById("root")!!.getAttribute("data-foo") }

        render {
            div(id = "root") {
                attr("data-foo", "I am there!".whenever(steering.data))
            }
        }

        delay(100)
        assertNull(getAttribute())

        steering.update(true)

        delay(100)
        assertEquals("I am there!", getAttribute())
    }

    @Test
    fun testWheneverWithFlowData() = runTest {
        initDocument()

        val steering = storeOf(false)
        val value = object : RootStore<String>("first") {
            private val items = listOf("first", "second", "skipped", "fourth")

            val next = handle {
                items[items.indexOf(it) + 1]
            }
        }

        val getAttribute = { document.getElementById("root")!!.getAttribute("data-foo") }

        render {
            div(id = "root") {
                attr("data-foo", value.data.whenever(steering.data))
            }
        }

        delay(100)
        // steering | off   | on    | on     | off     | on
        // value    | first | first | second | skipped | fourth
        // -----------------------------------------------------
        // sequence |   ^
        // attr     |  null
        assertNull(getAttribute())

        steering.update(true)
        delay(100)
        // steering | off   | on    | on     | off     | on
        // value    | first | first | second | skipped | fourth
        // -----------------------------------------------------
        // sequence |           ^
        // attr     |         first
        assertEquals("first", getAttribute())

        value.next()
        delay(100)
        // steering | off   | on    | on     | off     | on
        // value    | first | first | second | skipped | fourth
        // -----------------------------------------------------
        // sequence |                   ^
        // attr     |                 second
        assertEquals("second", getAttribute())

        steering.update(false)
        value.next()
        delay(100)
        // steering | off   | on    | on     | off     | on
        // value    | first | first | second | skipped | fourth
        // -----------------------------------------------------
        // sequence |                             ^
        // attr     |                           null
        assertNull(getAttribute())

        steering.update(true)
        value.next()
        delay(100)
        // steering | off   | on    | on     | off     | on
        // value    | first | first | second | skipped | fourth
        // -----------------------------------------------------
        // sequence |                                      ^
        // attr     |                                    fourth
        assertEquals("fourth", getAttribute())
    }

}