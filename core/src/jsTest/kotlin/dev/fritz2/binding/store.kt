package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals


class StoreTests {

    @Test
    fun testStoreHandleAndOfferHandler() = runTest {
        initDocument()

        val id1 = Id.next()
        val id2 = Id.next()
        val buttonId = Id.next()

        val store2 = object : RootStore<Int>(0) {}

        val store1 = object : RootStore<String>("start") {

            val finish = handleAndEmit<Int> {
                emit(5)
                "finish"
            }

            init {
                finish handledBy store2.update
            }
        }

        render {
            section {
                div(id = id1) {
                    store1.data.asText()
                }
                div(id = id2) {
                    store2.data.asText()
                }
                button(id = buttonId) {
                    clicks handledBy store1.finish
                }
            }
        }

        delay(100)

        val button = document.getElementById(buttonId) as HTMLButtonElement
        val div1 = document.getElementById(id1) as HTMLDivElement
        val div2 = document.getElementById(id2) as HTMLDivElement

        assertEquals("start", div1.textContent, "textContent of div1 is not same like store1")
        assertEquals("0", div2.textContent, "textContent of div2 is not same like store2")

        button.click()
        delay(100)

        assertEquals("finish", div1.textContent, "textContent of div1 is not same like store1 after click")
        assertEquals("5", div2.textContent, "textContent of div2 is not same like store2 after click")
    }

    @Test
    fun testStoreHandleAndOfferHandleAndOfferHandler() = runTest {
        initDocument()

        val id1 = Id.next()
        val id2 = Id.next()
        val id3 = Id.next()
        val buttonId = Id.next()

        val s3 = object : RootStore<String>("s3.start") {}

        val s2 = object : RootStore<String>("s2.start") {
            val finish = handleAndEmit<String, String> { _, action ->
                emit("s2.finish")
                action
            }

            init {
                finish handledBy s3.update
            }
        }

        val s1 = object : RootStore<String>("s1.start") {

            val finish = handleAndEmit<String> {
                emit("s1.finish")
                "s1.finish"
            }

            init {
                finish handledBy s2.finish
            }
        }

        render {
            section {
                div(id = id1) {
                    s1.data.asText()
                }
                div(id = id2) {
                    s2.data.asText()
                }
                div(id = id3) {
                    s3.data.asText()
                }
                button(id = buttonId) {
                    clicks handledBy s1.finish
                }
            }
        }

        delay(100)

        val button = document.getElementById(buttonId) as HTMLButtonElement
        val div1 = document.getElementById(id1) as HTMLDivElement
        val div2 = document.getElementById(id2) as HTMLDivElement
        val div3 = document.getElementById(id3) as HTMLDivElement

        assertEquals("s1.start", div1.textContent, "textContent of div1 is not same like store1")
        assertEquals("s2.start", div2.textContent, "textContent of div2 is not same like store2")
        assertEquals("s3.start", div3.textContent, "textContent of div3 is not same like store3")

        button.click()
        delay(100)

        assertEquals("s1.finish", div1.textContent, "textContent of div1 is not same like store1 after click")
        assertEquals("s1.finish", div2.textContent, "textContent of div2 is not same like store2 after click")
        assertEquals("s2.finish", div3.textContent, "textContent of div3 is not same like store3 after click")
    }

    @Test
    fun testErrorHandling() = runTest {
        initDocument()

        val errorMsg = Id.next()
        val valueId = Id.next()
        fun getValue() = document.getElementById(valueId)?.textContent
        val buttonId = Id.next()

        var errorHandlerResult = ""
        val startValue = "start"
        val errorHandler: ErrorHandler<String> = { e, o ->
            errorHandlerResult = e.message.orEmpty()
            o
        }

        val store = object : RootStore<String>(startValue) {
            val testHandler = handle(errorHandler) { model ->
                if (errorHandlerResult.isEmpty()) throw Exception(errorMsg)
                model.reversed()
            }
        }

        render {
            div {
                span(id = valueId) { store.data.asText() }
                button(id = buttonId) {
                    clicks handledBy store.testHandler
                }
            }
        }

        delay(100)

        assertEquals(getValue(), startValue, "wrong start value")

        (document.getElementById(buttonId) as HTMLButtonElement).click()

        delay(150)

        assertEquals(errorMsg, errorHandlerResult, "wrong message")
        assertEquals(getValue(), startValue, "wrong value after error")

        (document.getElementById(buttonId) as HTMLButtonElement).click()

        delay(150)

        assertEquals(getValue(), startValue.reversed(), "wrong value second action")

    }


}