package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLParagraphElement
import kotlin.test.*

class HandlersTests {

    @Test
    fun testSimpleHandler() = runTest {
        val store = object : RootStore<Int>(0) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }

            val dec = handle { it - 1 }
        }

        lateinit var currentParagraph: HTMLParagraphElement
        lateinit var button: HTMLButtonElement
        lateinit var buttonDynamic: HTMLButtonElement

        render {
            currentParagraph = p {
                store.data.renderText()
            }.domNode

            button = button {
                clicks handledBy store.dec
            }.domNode

            store.data.render {
                if (it > 40) {
                    buttonDynamic = button {
                        clicks handledBy { store.dec() }
                    }.domNode
                }
            }
        }

        delay(100)
        assertEquals("0", currentParagraph.textContent)

        store.handle { it + 1 }()
        delay(100)
        assertEquals("1", currentParagraph.textContent)

        store.handle<Int> { _, new -> new }(42)
        delay(100)
        assertEquals("42", currentParagraph.textContent)

        button.click()
        delay(100)
        assertEquals("41", currentParagraph.textContent)

        buttonDynamic.click()
        delay(100)
        assertEquals("40", currentParagraph.textContent)

        button.click()
        delay(100)
        assertEquals("39", currentParagraph.textContent)
    }

    @Test
    fun eventHandlerDomChange() = runTest {
        val resultId = Id.next()
        val buttonId = Id.next()

        val store = object : RootStore<String>("start") {
            var countHandlerCalls = 0

            val addADot = handle { model ->
                countHandlerCalls++
                "$model."
            }
        }

        render {
            section {
                input {
                    value(store.data)
                    changes.values() handledBy store.update
                }
                div(id = resultId) {
                    +"value: "
                    store.data.renderText()
                }
                button(id = buttonId) {
                    +"add one more little dot"
                    clicks handledBy store.addADot
                }
            }
        }

        delay(100)

        val result = document.getElementById(resultId).unsafeCast<HTMLDivElement>()
        val button = document.getElementById(buttonId).unsafeCast<HTMLButtonElement>()

        assertEquals(0, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(1, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start.", result.textContent, "wrong dom content of result-node")

        button.click()
        delay(100)
        assertEquals(2, store.countHandlerCalls, "wrong number of handler calls")
        assertEquals("value: start..", result.textContent, "wrong dom content of result-node")
    }

    @Test
    fun cancelJobAtHandledByDuringConnectedHandlerWorksWontCancelHandler() = runTest {
        var busyFlag = true
        val storeForCanceling = storeOf(false)

        val heavyWork = object : RootStore<Boolean>(false) {
            val doWork = handle {
                storeForCanceling.update(true)
                do {
                    delay(10)
                } while (busyFlag)
                true
            }
        }

        val idButton = Id.next()
        val idWorkState = Id.next()

        render {
            heavyWork.data.render {
                p(id = idWorkState) { +it.toString() }
            }
            storeForCanceling.data.render {
                if (!it) { // job of button will be canceled if `storeForCanceling` is set to `true`
                    button(id = idButton) {
                        clicks handledBy heavyWork.doWork
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
            "Handler has finished and set state to `true`",
        )
    }

    @Test
    fun flowOnceOfWillEmitOnlyOneValue() = runTest {
        val films = listOf("Highlander", "Star Wars", "Jurassic Park").asFlow()
        val sut = flowOnceOf("There can only be one")

        val result = films.flatMapLatest { film ->
            sut.map {
                "$it $film"
            }
        }.single()

        assertEquals("There can only be one Highlander", result)
    }

    @Test
    fun flowOnceOfWillThrowIfCollectedTwice() = runTest {
        val sut = flowOnceOf("Atom")

        sut.single()
        assertEquals(
            "Flow is empty",
            assertFails {
                sut.single()
            }.message,
        )
    }
}
