package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.test.*


class AttributeTests {

    @Test
    fun testAttributes() = runTest {

        val testRange = (0..4)
        val testId = Id.next()

        val (name0, value0) = "test0" to "value0"
        val (name1, value1) = "test1" to "value1"
        val (name2, values2) = "test2" to testRange.map { "value$it" }
        val (name3, values3) = "test3" to testRange.map { "value$it" }

        render {
            div(id = testId) {
                attr(name0, value0)
                attr(name1, flowOf(value1))
                attr("data-$name0", value0)
                attr("data-$name1", flowOf(value1))
                attr(name2, values2.joinToString(" "))
                attr(name3, flowOf(values3.joinToString(" ")))

                attr("test4", flowOf(true))
                attr("test5", flowOf(false))
                attr("test6", flowOf(true), "foo")
            }
        }

        delay(200)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertEquals(testId, element.id)
        assertEquals("div", element.localName)

        assertEquals(value0, element.getAttribute(name0))
        assertEquals(value1, element.getAttribute(name1))

        assertEquals(value0, element.getAttribute("data-$name0"))
        assertEquals(value1, element.getAttribute("data-$name1"))

        assertEquals(values2.joinToString(" "), element.getAttribute(name2))
        assertEquals(values3.joinToString(" "), element.getAttribute(name3))

        assertEquals(value0, element.getAttribute(name0))
        assertEquals(value1, element.getAttribute(name1))

        assertTrue(element.hasAttribute("test4"))
        assertEquals("", element.getAttribute("test4"))

        assertFalse(element.hasAttribute("test5"))

        assertTrue(element.hasAttribute("test6"))
        assertEquals("foo", element.getAttribute("test6"))
    }

    @Test
    fun testNullableAttributes() = runTest {
        val testId = Id.next()

        render {
            div(id = testId) {
                attr("nullableString", null as String?)
                attr("nullableFlowOfString", flowOf(null as String?))
                attr("nullableT", null as Int?)
                attr("nullableFlowOfT", flowOf(null as Int?))
                attr("nullableBoolean", null as Boolean?, "nullableBoolean")
                attr("nullableFlowOfBoolean", flowOf(null as Boolean?), "nullableFlowOfBoolean")
            }
        }

        delay(200)
        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertFalse(element.hasAttribute("nullableString"))
        assertFalse(element.hasAttribute("nullableFlowOfString"))
        assertFalse(element.hasAttribute("nullableT"))
        assertFalse(element.hasAttribute("nullableFlowOfT"))
        assertFalse(element.hasAttribute("nullableBoolean"))
        assertFalse(element.hasAttribute("nullableFlowOfBoolean"))
    }

    @Test
    fun testOverridingAStaticAttributeWithNullWillRemoveAttribute() = runTest {
        val testId = Id.next()

        lateinit var sut: HtmlTag<HTMLDivElement>
        render {
            sut = div(id = testId) {
                attr("data-test", "important data")
            }
        }

        delay(200)
        assertTrue(sut.domNode.hasAttribute("data-test"))

        sut.apply {
            attr<String?>("data-test", null)
        }

        delay(200)
        assertFalse(sut.domNode.hasAttribute("data-test"))
    }

    @Test
    fun testAlternatingNullableStringFlows() = runTest {
        val testId = Id.next()

        val nullableFlow = storeOf<String?>("a")

        render {
            div(id = testId) {
                attr("test", nullableFlow.data)
            }
        }

        delay(50)
        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertTrue(element.hasAttribute("test"))
        assertEquals("a", element.getAttribute("test"))

        nullableFlow.update(null)
        delay(50)

        assertFalse(element.hasAttribute("test"))

        nullableFlow.update("c")
        delay(50)

        assertTrue(element.hasAttribute("test"))
        assertEquals("c", element.getAttribute("test"))
    }


    @Test
    fun testAlternatingNullableTFlows() = runTest {
        val testId = Id.next()

        val nullableFlow = storeOf<Int?>(42)

        render {
            div(id = testId) {
                attr("test", nullableFlow.data)
            }
        }

        delay(50)
        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()

        assertTrue(element.hasAttribute("test"))
        assertEquals("42", element.getAttribute("test"))

        nullableFlow.update(null)
        delay(50)

        assertFalse(element.hasAttribute("test"))

        nullableFlow.update(99)
        delay(50)

        assertTrue(element.hasAttribute("test"))
        assertEquals("99", element.getAttribute("test"))
    }

    /**
     * This test must simulate sequentially the following actions:
     * 1.) "Type" into an input field by keyboard
     * 2.) change the store's value directly
     * 3.) check whether the input's `value` property (not by `getAttribute`! By DOM API `node.value` access!) has
     * changed
     *
     * Step 3 would fail, if the special treatment in `fun Tag<HTMLInputElement>.value(value: Flow<String>)`
     * is not correct.
     *
     * BUT: This does simply not work via DOM API! The dispatched event is not recognized by the browser.
     *
     * Approach https://stackoverflow.com/questions/45098634/how-to-simulate-keypress-with-javascript-in-chrome
     * is based upon a deprecated function `initKeyboardEvent`, which is even not available by Kotlin API.
     */
    @Test
    @Ignore
    fun testSpeciallyHandledAttributes() = runTest {
        val id = Id.next()

        val storedText = storeOf("initial")
        val storedState = storeOf(false)

        lateinit var textInput: Tag<HTMLInputElement>
        //lateinit var checkBox: Tag<HTMLInputElement>
        render {
            textInput = input(id = id) {
                placeholder("Foo")
                type("text")
                value(storedText.data)
                changes.values() handledBy { console.log(it) }
            }
            /*
            checkBox = input {
                type("checkbox")
                checked(storedState.data)
            }
             */
        }

        delay(100)

        val input = document.getElementById(id).unsafeCast<HTMLInputElement>()

        assertEquals("initial", textInput.domNode.getAttribute("value"))
        assertEquals("initial", textInput.domNode.value)
        assertEquals("initial", textInput.domNode.defaultValue)

        input.focus()
        delay(100)

        // Per Keyevent String Wert in Input schreiben
        val keyEvent = KeyboardEvent("keydown", KeyboardEventInit(key = "e", "KeyE"))
        window.dispatchEvent(keyEvent)

        // Prüfen: domnode.value == Eingabe?
        delay(1500)
        //assertEquals("a", textInput.domNode.value)
        assertEquals("e", input.value)

        // dann update auf neuer Wert
        storedText.update("updated")
        storedState.update(true)
        delay(100)

        // dann in domnode.value == neuer Wert sein!
        assertEquals("updated", textInput.domNode.getAttribute("value"))
        assertEquals("updated", textInput.domNode.value)
        assertEquals("updated", textInput.domNode.defaultValue)

        //assertEquals("changed", textInput.domNode.getAttribute("defaultValue"))
        //println(checkBox.domNode.getAttribute("checked").toBoolean())
        //println(checkBox.domNode.checked)
        //assertTrue(checkBox.domNode.getAttribute("checked").toBoolean())
        //assertTrue(checkBox.domNode.getAttribute("defaultChecked").toBoolean())
    }
}