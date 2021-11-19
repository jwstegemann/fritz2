package dev.fritz2.dom.html

import dev.fritz2.dom.MOUNT_POINT_KEY
import dev.fritz2.identification.Id
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

        val id1 = Id.next()
        val key1 = keyOf<String>("key1")
        val value1 = "value1"
        val id2 = Id.next()
        val key2 = keyOf<String>("key2")
        val value2 = "value2"
        val id3 = Id.next()

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
        assertEquals(
            """{ "${MOUNT_POINT_KEY.name}" : "[object Object]", "${key1.name}" : "$value1" }""",
            div1.innerText
        )

        val div2 = document.getElementById(id2) as HTMLDivElement
        assertEquals(
            """{ "${MOUNT_POINT_KEY.name}" : "[object Object]", "${key2.name}" : "$value2" }""",
            div2.innerText
        )

        val div3 = document.getElementById(id3) as HTMLDivElement
        assertEquals(value1, div3.getAttribute("data-${key1.name}"))
        assertEquals(value2, div3.getAttribute("data-${key2.name}"))
    }

}