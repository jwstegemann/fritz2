package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class DomNodeListTests {

    @Test
    fun testAsElementListDropsNoneHtmlTags() = runTest {
        
        render {
            div(id = "root") {
                +"Some Textnode" // should be dropped
                p(id = "child") {
                    +"Some Textnode" // should be dropped
                }
                +"Some further Textnode" // should be dropped
                span(id = "child2") {
                }
            }
        }

        delay(100)

        val elements = document.getElementById("root")!!.querySelectorAll("*").asElementList().map { it.id }

        assertContentEquals(listOf("child", "child2"), elements)
    }

    @Test
    fun testAccessingInvalidIndexThrowsIndexOutOfBoundsException() = runTest {
        
        render {
            div(id = "root") {
                p(id = "child") { }
            }
        }

        delay(100)

        val elements = document.getElementById("root")!!.childNodes.asElementList()

        val result = try {
            elements[2]
            false
        } catch (ex: IndexOutOfBoundsException) {
            true
        }

        assertTrue(result)
    }
}