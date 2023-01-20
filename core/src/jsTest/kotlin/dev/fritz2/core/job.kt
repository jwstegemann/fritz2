package dev.fritz2.core

import kotlin.test.*
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement


class AdHocHandlerTests {

    @Test
    fun givenSomeAdHocHandlerWhenNewValuesAppearOnTheFlowItWillExecuteItsExpression() {

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

        busyFlag = true
        delay(50)
        assertEquals(
            "true",
            document.getElementById(idWorkState)?.textContent,
            "Handler has finished and set state to `true`"
        )
    }
}