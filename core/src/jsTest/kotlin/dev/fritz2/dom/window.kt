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
        val button2Id = "button2Id"

        val buttonEventText = "buttonClicked"
        val windowEventText = "clicked"

        val windowStore = object : RootStore<String>("") {  }
        val buttonStore = object : RootStore<String>("") {  }

        render {
            Window.clicks.stopImmediatePropagation().map {
                windowEventText
            } handledBy windowStore.update


            section {
                div(id = divId) {
                    windowStore.data.asText()
                    button(id = buttonId) {
                        clicks.events.map { buttonEventText } handledBy buttonStore.update
                        buttonStore.data.asText()
                    }
                }
                button(id = button2Id) {
                    clicks.stopImmediatePropagation().map { buttonEventText } handledBy buttonStore.update
                    buttonStore.data.asText()
                }
            }
        }

        delay(100)

        val div = document.getElementById(divId).unsafeCast<HTMLDivElement>()
        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()
        val button2 = document.getElementById(button2Id).unsafeCast<HTMLButtonElement>()

        button.click()
        delay(100)
        assertEquals(windowEventText, div.textContent, "Button clicked: wrong content into div")
        assertEquals("", button.textContent, "Button clicked: wrong content into Button")

        buttonStore.update("")
        windowStore.update("")

        button2.click()
        delay(100)
        assertEquals("", div.textContent, "Button 2 clicked: wrong content into div")
        assertEquals(buttonEventText, button2.textContent, "Button 2 clicked: wrong content into Button")

    }
}