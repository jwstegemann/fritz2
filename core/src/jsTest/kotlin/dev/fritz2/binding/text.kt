package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLParagraphElement
import kotlin.test.Test
import kotlin.test.assertEquals

class TextTests {

    @Test
    fun testTextNodes() = runTest {

        initDocument()

        val id1 = uniqueId()
        val id2 = uniqueId()

        render {
            div {
                p(id = id1) {
                    +"Hello World1!"
                }
                p(id = id2) {
                    +"Hello World2!"
                }
            }
        }.mount(targetId)

        delay(250)

        val p1 = document.getElementById(id1).unsafeCast<HTMLParagraphElement>()
        val p2 = document.getElementById(id2).unsafeCast<HTMLParagraphElement>()

        assertEquals("Hello World1!", p1.innerText)
        assertEquals("Hello World2!", p2.innerText)
    }
}