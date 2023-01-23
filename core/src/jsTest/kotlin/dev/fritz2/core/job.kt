package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import kotlin.test.*


class AdHocHandlerTests {

    @Test
    fun givenSomeAdHocHandlerWhenNewValuesAppearOnTheFlowItWillExecuteItsExpression() = runTest {
        val idButton = Id.next()
        var result = 0

        render {
            button(id = idButton) {
                clicks.map { 41 } handledBy { value ->
                    result = value + 1
                }
            }
        }

        delay(50)
        (document.getElementById(idButton) as HTMLButtonElement).click()

        delay(50)
        assertEquals(42, result)
    }

    @Test
    fun givenSomeAdHocHandlerWhenCanceledItWillStopExecution() = runTest {
        val idButton = Id.next()
        var result = 0
        val busyFlag = storeOf(true)

        render {
            busyFlag.data.render {
                if (it) {
                    button(id = idButton) {
                        clicks.map { 41 } handledBy { value ->
                            result = value + 1
                            do {
                                delay(10)
                            } while (busyFlag.current)
                            result = -1
                        }
                    }
                }
            }
        }

        delay(50)
        (document.getElementById(idButton) as HTMLButtonElement).click()

        delay(50)
        assertEquals(42, result)

        busyFlag.update(false)
        delay(50)

        assertNull(document.getElementById(idButton))
        assertEquals(42, result)
    }

    @Test
    fun cancelAdHocHandlersJobAfterCallingOtherHandlersWontCancelThose() = runTest {
        var busyFlag = true

        val heavyWork = object : RootStore<Boolean>(false) {
            val doWork = handle {
                do {
                    delay(10)
                } while (busyFlag)
                true
            }
        }

        val storeForCanceling = storeOf(false)

        val idButton = Id.next()
        val idWorkState = Id.next()

        render {
            heavyWork.data.render {
                p(id = idWorkState) { +it.toString() }
            }
            storeForCanceling.data.render {
                if (!it) { // job of button will be canceled if `storeForCanceling` is set to `true`
                    button(id = idButton) {
                        clicks handledBy {
                            heavyWork.doWork() // should continue to work after mount-point cancels this ad-hoc handler!
                            storeForCanceling.update(true)
                        }
                    }
                }
            }
        }

        delay(100)
        assertFalse(heavyWork.current)

        (document.getElementById(idButton) as HTMLButtonElement).click()
        delay(100)

        assertTrue(storeForCanceling.current)
        assertNull(document.getElementById(idButton))

        busyFlag = false
        delay(50)
        assertEquals(
            "true",
            document.getElementById(idWorkState)?.textContent,
            "Handler has finished and set state to `true`"
        )
    }

    @Test
    fun testAdHocErrorHandling() = runTest {

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