package dev.fritz2.core

import dev.fritz2.initDocument
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
                    store1.data.renderText()
                }
                div(id = id2) {
                    store2.data.renderText()
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
                    s1.data.renderText()
                }
                div(id = id2) {
                    s2.data.renderText()
                }
                div(id = id3) {
                    s3.data.renderText()
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

        val valueId = Id.next()
        fun getValue() = document.getElementById(valueId)?.textContent

        var errorHandlerResult = ""

        val startValue = "start"

        val store = object : RootStore<String>(startValue) {
            override fun errorHandler(cause: Throwable) {
                errorHandlerResult = cause.message.orEmpty()
            }

            val exceptionSimpleHandler = "exception from simple handler"
            val simpleTestHandlerThrowingException = handle {
                throw Exception(exceptionSimpleHandler)
            }

            val exceptionSimpleHandlerValue = "exception from simple handler"
            val simpleTestHandlerWithActionThrowingException = handle<Int> { _, _ ->
                throw Exception(exceptionSimpleHandlerValue)
            }

            val exceptionEmittingHandler = "exception from simple handler"
            val emittingTestHandlerThrowingException = handleAndEmit<Unit> {
                throw Exception(exceptionEmittingHandler)
            }

            val exceptionEmittingHandlerValue = "exception from simple handler"
            val emittingTestHandlerWithActionThrowingException = handleAndEmit<Int, Unit> { _, _ ->
                throw Exception(exceptionEmittingHandlerValue)
            }

        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
            }
        }

        val updates = MutableStateFlow(startValue)
        updates handledBy store.update

        suspend fun checkUpdate(msg: String) {
            val valueAfterSuccessfullUpdate = Id.next()
            updates.value = valueAfterSuccessfullUpdate
            delay(150)
            assertEquals(valueAfterSuccessfullUpdate, getValue(), msg)
        }

        checkUpdate("store not updating after start")

        flowOf(Unit) handledBy store.simpleTestHandlerThrowingException
        delay(150)
        assertEquals(store.exceptionSimpleHandler, errorHandlerResult, "exception not caught on simple handler")
        assertEquals(updates.value, getValue(), "wrong value rendered after simple handler")
        checkUpdate("store not updating after simple handler")

        flowOf(1) handledBy store.simpleTestHandlerWithActionThrowingException
        delay(150)
        assertEquals(store.exceptionSimpleHandlerValue, errorHandlerResult, "exception not caught on simple handler with action")
        assertEquals(updates.value, getValue(), "wrong value rendered after simple handler with action")
        checkUpdate("store not updating after simple handler with action")

        flowOf(Unit) handledBy store.emittingTestHandlerThrowingException
        delay(150)
        assertEquals(store.exceptionEmittingHandler, errorHandlerResult, "exception not caught on emitting handler")
        assertEquals(updates.value, getValue(), "wrong value rendered after emitting handler")
        checkUpdate("store not updating after emitting handler")

        flowOf(2) handledBy store.emittingTestHandlerWithActionThrowingException
        delay(150)
        assertEquals(store.exceptionEmittingHandlerValue, errorHandlerResult, "exception not caught on emitting handler with action")
        assertEquals(updates.value, getValue(), "wrong value rendered after emitting handler with action")
        checkUpdate("store not updating after emitting handler with action")

        val intermediateException = "intermediate exception"
        flowOf(Unit).map { throw Exception(intermediateException) } handledBy store.update
        delay(150)
        assertEquals(intermediateException, errorHandlerResult, "exception in map not caught")
        checkUpdate("store not updating after intermediate exception")
    }

    @Test
    fun testAdHocErrorHandling() = runTest {
        initDocument()

        val valueId = Id.next()
        fun getValue() = document.getElementById(valueId)?.textContent

        val startValue = "start"

        val store = object : RootStore<String>(startValue) {
        }

        render {
            div {
                span(id = valueId) { store.data.renderText() }
            }
        }

        val updates = MutableStateFlow(startValue)
        updates handledBy store.update

        suspend fun checkUpdate(msg: String) {
            val valueAfterSuccessfullUpdate = Id.next()
            updates.value = valueAfterSuccessfullUpdate
            delay(150)
            assertEquals(valueAfterSuccessfullUpdate, getValue(), msg)
        }

        checkUpdate("store not updating after start")

        val adHocException = "adHoc exception"
        flowOf(Unit) handledBy {
            throw Exception(adHocException)
        }
        delay(150)
        checkUpdate("store not updating after adhoc handler ")

        val adHocIntermediateException = "adHoc intermediate exception"
        flowOf(2).map {throw Exception(adHocIntermediateException)} handledBy {
        }
        delay(150)
        checkUpdate("store not updating after intermediate exception before adhoc handler ")
    }
}