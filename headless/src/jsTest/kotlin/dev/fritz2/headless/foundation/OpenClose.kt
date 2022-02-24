package dev.fritz2.headless.foundation

import dev.fritz2.core.Id
import dev.fritz2.core.render
import dev.fritz2.core.storeOf
import dev.fritz2.headless.getElementById
import dev.fritz2.headless.runTest
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenCloseTest {

    @Test
    fun testOpenClose() = runTest {

        val id = Id.next()
        val openClose = object : OpenClose() {}
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

        openClose.toggle()
        delay(100)
        assertEquals("false", div.getAttribute("open"), "after toggle")
    }

}