package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class DispatchTests {

    @Test
    fun dispatchFromStore() = runTest {
        initDocument()

        val store = object : RootStore<String>("abcdef") {
            val reverse = handle { it.reversed() }

            init {
                dispatch(reverse)
            }
        }

        val valueId = "value" + uniqueId()

        render {
            div {
                span(id = valueId) { store.data.bind() }
            }
        }.mount(targetId)

        delay(500)
        val content = document.getElementById(valueId)?.textContent
        assertEquals(content, "fedcba")
    }

    @Test
    fun dispatchAnywhere() = runTest {
        initDocument()

        val store = object : RootStore<String>("abcdef") {
            val reverse = handle { it.reversed() }
        }

        val valueId = "value" + uniqueId()

        render {
            div {
                span(id = valueId) { store.data.bind() }
            }
        }.mount(targetId)

        delay(200)
        dispatch(store.reverse)

        delay(500)
        val content = document.getElementById(valueId)?.textContent
        assertEquals(content, "fedcba")
    }

}