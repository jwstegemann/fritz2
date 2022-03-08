package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ScopeTests {

    @Test
    fun testScopeInDifferentContexts() = runTest {
        
        val id1 = Id.next()
        val key1 = Scope.keyOf<String>("key1")
        val value1 = "value1"
        val id2 = Id.next()
        val key2 = Scope.keyOf<String>("key2")
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
        Json.parseToJsonElement(div1.innerText).jsonObject.let { e ->
            assertEquals(2, e.size, "too many entries in scope")
            assertEquals(e[key1.name]?.jsonPrimitive?.content, value1, "wrong entry")
        }

        val div2 = document.getElementById(id2) as HTMLDivElement
        Json.parseToJsonElement(div2.innerText).jsonObject.let { e ->
            assertEquals(2, e.size, "too many entries in scope")
            assertEquals(e[key2.name]?.jsonPrimitive?.content, value2, "wrong entry")
        }

        val div3 = document.getElementById(id3) as HTMLDivElement
        assertEquals(value1, div3.getAttribute("data-${key1.name}"))
        assertEquals(value2, div3.getAttribute("data-${key2.name}"))
    }

}