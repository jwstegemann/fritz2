package dev.fritz2.dom.html

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class MountTests {

    @Test
    fun testValueAttributeMountPoint() = runTest {
        initDocument()

        val id = uniqueId()

        val store = object : RootStore<String>("test") {
            val modify = handle { _ ->
                "modified"
            }
        }

        render {
            div {
                input(id = id) {
                    type = const("text")
                    value = store.data
                    clicks handledBy store.modify
                }
            }
        }.mount(targetId)

        delay(250)

        val input = document.getElementById(id) as HTMLInputElement
        assertEquals("test", input.value, "initial elem.value is not equal")
        assertEquals("test", input.getAttribute("value"), "initial elem.getAttribute(\"value\") is not equal")

        input.click()
        delay(250)

        assertEquals("modified", input.value, "modified elem.value is not equal")
        assertEquals("modified", input.getAttribute("value"), "modified elem.getAttribute(\"value\") is not equal")
    }

    @Test
    fun testCheckedAttributeMountPoint() = runTest {
        initDocument()

        val id = uniqueId()

        val store = object : RootStore<Boolean>(true) {
            val modify = handle { model ->
                !model
            }
        }

        render {
            div {
                input(id = id) {
                    type = const("checkbox")
                    checked = store.data
                    clicks handledBy store.modify
                }
            }
        }.mount(targetId)

        delay(250)

        val input = document.getElementById(id) as HTMLInputElement
        assertEquals(true, input.checked, "initial elem.checked is not equal")
        assertEquals("", input.getAttribute("checked"), "initial elem.getAttribute(\"checked\") is not equal")

        input.click()
        delay(250)

        assertEquals(false, input.checked, "modified elem.checked is not equal")
        assertEquals(null, input.getAttribute("checked"), "modified elem.getAttribute(\"checked\") is not equal")
    }
}