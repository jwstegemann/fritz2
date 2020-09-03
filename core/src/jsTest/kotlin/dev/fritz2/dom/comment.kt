package dev.fritz2.dom

import dev.fritz2.dom.html.render
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals


class CommentTests {

    @Test
    fun testCommentOnString() = runTest {
        initDocument()

        val id1 = uniqueId()
        val id2 = uniqueId()
        val comment = "testComment"

        render {
            section {
                div(id = id1) {
                    comment(comment)
                }
                div(id = id2) {
                    !comment
                }
            }
        }.mount(targetId)

        delay(100)

        val div1 = document.getElementById(id1) as HTMLDivElement

        assertEquals(id1, div1.id)
        assertEquals(8, div1.firstChild?.nodeType)
        assertEquals(comment, div1.firstChild?.nodeValue)

        val div2 = document.getElementById(id2) as HTMLDivElement

        assertEquals(id2, div2.id)
        assertEquals(8, div2.firstChild?.nodeType)
        assertEquals(comment, div2.firstChild?.nodeValue)
    }
}