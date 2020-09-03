package dev.fritz2.dom.html

import dev.fritz2.binding.action
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class RenderTests {

    @Test
    fun testShortRenderFunction() = runTest {
        initDocument()

        val store = storeOf(true)

        val divId = uniqueId()

        render {
            section {
                store.data.render { value ->
                    div(id = divId) {
                        +if (value) "on" else "off"
                    }
                }.bind()
            }
        }.mount(targetId)

        delay(100)

        val div = document.getElementById(divId) as HTMLDivElement

        assertEquals("on", div.textContent)

        action(false) handledBy store.update
        delay(200)

        val div2 = document.getElementById(divId) as HTMLDivElement

        assertEquals("off", div2.textContent)
    }
}