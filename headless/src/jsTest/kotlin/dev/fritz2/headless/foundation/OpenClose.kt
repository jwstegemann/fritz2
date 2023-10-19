package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.click
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.keyDown
import dev.fritz2.headless.runTest
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenCloseTest {

    @Test
    fun testOpenClose() = runTest {

        val id = Id.next()
        val openClose = object : OpenClose(), WithJob by this {}
        val state = storeOf(false)
        openClose.openState(state)

        render {
            div(id = id) {
                attr("open", openClose.opened.asString())
            }
        }

        delay(100)
        val div = getElementById<HTMLDivElement>(id)
        assertEquals("false", div.getAttribute("open"), "initial")

        openClose.open()
        delay(100)
        assertEquals("true", div.getAttribute("open"), "after open")

        openClose.close()
        delay(100)
        assertEquals("false", div.getAttribute("open"), "after close")

        openClose.toggle()
        delay(100)
        assertEquals("true", div.getAttribute("open"), "after toggle")

        openClose.toggle()
        delay(100)
        assertEquals("false", div.getAttribute("open"), "after second toggle")
    }

    class OpenCloseTest(withJob: WithJob) : OpenClose(), WithJob by withJob {

        val id = Id.next()

        init {
            openState(storeOf(false))
        }

        fun create(context: RenderContext) = with(context) {
            div(id = id) {
                attr("open", opened.asString())
                toggleOnClicksEnterAndSpace()
                closeOnEscape()
                closeOnBlur()
            }
        }
    }

    @Test
    fun testOpenCloseFunctions() = runTest {
        val openClose = OpenCloseTest(this)

        render {
            openClose.create(this)
        }

        delay(100)
        val div = getElementById<HTMLDivElement>(openClose.id)
        assertEquals("false", div.getAttribute("open"), "initial")

        div.click()
        delay(100)
        assertEquals("true", div.getAttribute("open"), "after click")

        div.keyDown(Keys.Space)
        delay(100)
        assertEquals("false", div.getAttribute("open"), "after space")

        div.keyDown(Keys.Enter)
        delay(100)
        assertEquals("true", div.getAttribute("open"), "after enter")

        window.keyDown(Keys.Escape)
        delay(100)
        assertEquals("false", div.getAttribute("open"), "after escape")

        div.click()
        delay(100)
        assertEquals("true", div.getAttribute("open"), "after click")

        window.click(100, 100)
        delay(100)
        assertEquals("false", div.getAttribute("open"), "after blur")
    }

}