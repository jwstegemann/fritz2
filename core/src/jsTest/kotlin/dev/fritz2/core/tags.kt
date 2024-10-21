package dev.fritz2.core

import dev.fritz2.renderWithJob
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.w3c.dom.*
import kotlin.test.*


class TagTests {

    @Test
    fun testTextNodes() = runTest {


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

        val testId = Id.next()
        val text = "testText"

        render {
            div(id = testId) {
                flowOf(text).renderText()
            }
        }

        delay(100)

        val element = document.getElementById(testId) as HTMLDivElement
        val mountPointTag = element.firstChild!! as HTMLSpanElement

        assertEquals(testId, element.id)
        assertEquals(text, element.textContent)
        assertTrue(
            mountPointTag.hasAttribute("data-mount-point"),
            "Attribute `data-mount-point` missing, found only the following attributes: "
                    + mountPointTag.attributes.asList().joinToString(",") { it.name })
    }

    @Test
    fun testRenderTextOnlyReactsToChangedValues() = runTest {
        fun getSpanText(id: String) = (document.getElementById(id) as HTMLSpanElement).textContent
        fun getStaticSpan(id: String) =
            document.getElementById(id)!!.firstChild as HTMLSpanElement

        fun getStaticTextNode(id: String) = document.getElementById(id)!!.firstChild!!.firstChild

        data class Model(val static: String, val reactive: String)

        val model = Model("fritz", "RC-3")
        val store = storeOf(model)
        val idValueReactive = Id.next()
        val idMain = Id.next()

        render {
            div(id = idMain) {
                // this should be re-rendered only after last change
                store.data.map { it.static }.renderText()

                store.data.map { it.reactive }.render { reactive ->
                    span(id = idValueReactive) { +reactive }
                }
            }
        }

        delay(100)

        assertEquals("RC-3", getSpanText(idValueReactive))
        val firstRenderedSpan = getStaticTextNode(idMain)
        assertEquals("fritz", getStaticSpan(idMain).textContent)

        // update with repeated value should omit rendering
        store.update(model.copy(reactive = "RC-4"))
        delay(100)

        assertEquals("RC-4", getSpanText(idValueReactive))
        val firstRenderedSpanCheck = getStaticTextNode(idMain)
        assertSame(firstRenderedSpan, firstRenderedSpanCheck)
        assertEquals("fritz", getStaticSpan(idMain).textContent)

        // update with new value should trigger re-render
        store.update(Model("fritz2", "1.0-FINAL"))
        delay(100)

        assertEquals("1.0-FINAL", getSpanText(idValueReactive))
        val secondRenderedSpan = getStaticTextNode(idMain)
        assertNotSame(firstRenderedSpan, secondRenderedSpan)
        assertEquals("fritz2", getStaticSpan(idMain).textContent)
    }

    @Test
    fun testCommentOnString() = runTest {

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
    fun testStaticClassNameIsAppliedImmediately() = runTest {
        renderWithJob {
            div("ctor", id = Id.next()) {
                className("immediately").also {
                    assertEquals("ctor immediately", domNode.className)
                }
            }
        }
    }

    @Test
    fun testClassNameWithInitialValue() = runTest {

        val testId = Id.next()
        val gate = storeOf(false)

        val document = renderWithJob {
            div("ctor", id = testId) {
                className("fixed")
                className(gate.data.transform { if (it) emit("visible") }, "invisible").also {
                    // ensure initial class names are really applied *immediately*
                    assertEquals("ctor fixed invisible", domNode.className)
                }
            }
        }

        delay(50)

        val element = document.domNode.firstChild!! as HTMLDivElement

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals("ctor fixed invisible", element.className)

        gate.update(true)
        delay(50)
        assertEquals("ctor fixed visible", element.className)
    }

    @Test
    fun testClassNameWithGenericFlowAndInitialValue() = runTest {

        val testId = Id.next()
        val gate = storeOf(false)
        val statesToNames = mapOf(0 to "initial", 1 to "changed", 2 to "another")
        val state = storeOf(-1)

        val document = renderWithJob {
            div("ctor", id = testId) {
                className("fixed")
                className(gate.data.transform { if (it) emitAll(state.data) }, 0) { statesToNames[it]!! }.also {
                    // ensure initial class names are really applied *immediately*
                    assertEquals("ctor fixed initial", domNode.className)
                }
            }
        }

        delay(50)

        val element = document.domNode.firstChild!! as HTMLDivElement

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals("ctor fixed initial", element.className)

        // after initialization step, we allow state changes to appear within `className.value`
        gate.update(true)

        state.update(1)
        delay(50)
        assertEquals("ctor fixed changed", element.className)

        state.update(2)
        delay(50)
        assertEquals("ctor fixed another", element.className)
    }

    @Test
    fun testMultipleClassNameWithGenericFlowAndInitialValue() = runTest {

        val testId = Id.next()
        val gate = storeOf(false)
        val fonts = mapOf(-1 to "none", 0 to "small", 1 to "medium", 2 to "large")
        val stateOfFont = storeOf(-1)
        val stateOfColor = storeOf("green")

        val document = renderWithJob {
            div("ctor", id = testId) {
                className("fixed")
                className(gate.data.transform { if (it) emitAll(stateOfFont.data) }, 0) { fonts[it]!! }.also {
                    // ensure initial class names are really applied *immediately*
                    assertEquals("ctor fixed small", domNode.className)
                }
                className(gate.data.transform { if (it) emitAll(stateOfColor.data) }, "red").also {
                    // ensure initial class names are really applied *immediately*
                    assertEquals("ctor fixed small red", domNode.className)
                }
            }
        }

        delay(50)

        val element = document.domNode.firstChild!! as HTMLDivElement

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)
        assertEquals("ctor fixed small red", element.className)

        // after initialization step, we allow state changes to appear within `className.value`
        gate.update(true)

        delay(50)
        assertEquals("ctor fixed none green", element.className)

        stateOfFont.update(1)
        delay(50)
        assertEquals("ctor fixed medium green", element.className)

        stateOfFont.update(2)
        stateOfColor.update("cyan")
        delay(50)
        assertEquals("ctor fixed large cyan", element.className)
    }

    @Test
    fun testRenderWithCondition() = runTest {

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

        val steering = storeOf(false)
        val value = object : RootStore<String>("first", job = Job()) {
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

    @Test
    fun testClassNameWithFlowInsideReactiveScopeBasedUponListMapping_WontThrowUnhandledCollectionLensGetException() =
        runTest {
            val items = listOf("red", "green", "blue")

            fun extractClasses() = items.mapNotNull { item -> document.getElementById(item) }
                .joinToString(",") { it.className }

            val storedItems = storeOf(items, Job())
            val removeItem = storedItems.handle<String> { allItems, toRemove ->
                allItems.filterNot { it == toRemove }
            }

            render {
                storedItems.data.renderEach { item ->
                    val storedItem = storedItems.mapByElement(item) { it }
                    div(id = item) {
                        // access to mapped item will throw `CollectionLensGetException` after deletion
                        // -> must be catched internally like handlers do!
                        className(storedItem.data)

                        // we give some time to the `className`-function to try to access a mapped lens for an object
                        // that might not exist anymore.
                        beforeUnmount { _, _ -> delay(100) }
                    }
                }
            }

            delay(100)
            assertEquals("red,green,blue", extractClasses())

            // this is the source of possible problem: By removing mapped lenses won't find their targeting object
            // anymore. This will lead to `CollectionLensGetException`, that must be handled internally!
            removeItem("green")

            delay(200)
            assertEquals("red,blue", extractClasses())

            removeItem("blue")

            // if not exception handling on the `classes`-Flow is done, rendering will freeze and the node for green
            // will remain.
            delay(200)
            assertEquals("red", extractClasses())
        }
}