package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ScopeTests {

    @Test
    fun testScopeInDifferentContexts() = runTest {
        initDocument()

        val id1 = uniqueId()
        val key1 = keyOf<String>("key1")
        val value1 = "value1"
        val id2 = uniqueId()
        val key2 = keyOf<String>("key2")
        val value2 = "value2"
        val id3 = uniqueId()

        render {
            div(scope = {
                set(key1, value1)
            }) {
                div(id = id1) {
                    +scope.toString()
                }
            }
            div(scope = {
                set(key2, value2)
            }) {
                div(id = id2) {
                    +scope.toString()
                }
            }
            div(scope = {
                set(key1, value1)
                set(key2, value2)
            }) {
                div(id = id3) {
                    scope.asDataAttr()
                }
            }
        }

        delay(200)

        val div1 = document.getElementById(id1) as HTMLDivElement
        assertEquals("""{ "${key1.name}" : "$value1" }""", div1.innerText)

        val div2 = document.getElementById(id2) as HTMLDivElement
        assertEquals("""{ "${key2.name}" : "$value2" }""", div2.innerText)

        val div3 = document.getElementById(id3) as HTMLDivElement
        assertEquals(value1, div3.getAttribute("data-${key1.name}"))
        assertEquals(value2, div3.getAttribute("data-${key2.name}"))
    }

}