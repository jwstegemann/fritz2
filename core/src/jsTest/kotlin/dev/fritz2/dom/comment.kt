package dev.fritz2.dom

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


class CommentTests {

    @Test
    fun testCommentOnString() = runTest {
        initDocument()

        val id1 = Id.next()
        val id2 = Id.next()
        val comment = "testComment"

        render {
            section {
                div(id = id1) {
                    flowOf(comment).asComment()
                }
                div(id = id2) {
                    !comment
                }
            }
        }

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