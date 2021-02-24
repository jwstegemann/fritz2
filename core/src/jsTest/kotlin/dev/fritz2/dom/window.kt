package dev.fritz2.dom

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.render
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLLabelElement
import kotlin.test.Test
import kotlin.test.assertEquals

class WindowTest {

    @Test
    fun testWindowListenerForClickEvent() = runTest {
        initDocument()

        val labelId = "labelId"
        val divId = "divId"

        val store = object : RootStore<String>("") {
        }

        render {
            Window.clicks.map {
                labelId
            } handledBy store.update


            section {
                div(id = divId) {
                    store.data.asText()
                    label(id = labelId) { }
                }

            }
        }
        val div = document.getElementById(divId).unsafeCast<HTMLDivElement>()
        val label = document.getElementById(labelId).unsafeCast<HTMLLabelElement>()

        delay(100)

        assertEquals("", div.textContent, "wrong content into div")


        delay(100)


        label.click()
        delay(100)
        assertEquals(labelId, div.textContent, "wrong content into div")
    }

  /*  @Test
    fun testWindowListenerForClickEventAndComposedPath() = runTest {
        initDocument()

        val labelId = "labelId"
        val divId = "divId"

        val store = object : RootStore<String>("") {
            var countHandlerCalls = 0
                val updateData = handle {
                    countHandlerCalls++
                    it
                }
        }

        render {
            Window.clicks.composedPath().map {
                it[0].asDynamic().id
            } handledBy store.updateData


            section {
                div(id = divId) {
                    store.data.asText()
                    label(id = labelId) { }
                }

            }
        }

        delay(100)

        val div = document.getElementById(divId).unsafeCast<HTMLDivElement>()
        val label = document.getElementById(labelId).unsafeCast<HTMLLabelElement>()

        assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")

        label.click()
        delay(100)
        assertEquals(1, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals(labelId, div.textContent, "wrong id into store")

        div.click()
        delay(100)
        assertEquals(2, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals(divId, div.textContent, "wrong id into store")
    }

    @Test
    fun testWindowListenerForStopImmediatePropagation() = runTest {
        initDocument()

        val divId = "divId"
        val buttonId = "buttonId"

        val windowEventText = "windowEventText"
        val windowSecondEventText = "windowSecondEventText"

        val windowStore = object : RootStore<String>("") {  }

        render {
            Window.clicks.stopImmediatePropagation().map {
                windowEventText
            } handledBy windowStore.update

            Window.clicks.map {
                windowSecondEventText
            } handledBy windowStore.update

            section {
                div(id = divId) {
                    windowStore.data.asText()
                }

                button(id = buttonId) {}
            }
        }

        delay(100)

        val div = document.getElementById(divId).unsafeCast<HTMLDivElement>()
        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()

        button.click()
        delay(100)
        assertEquals(windowEventText, div.textContent, "Button clicked: wrong content into div")

    }*/
}