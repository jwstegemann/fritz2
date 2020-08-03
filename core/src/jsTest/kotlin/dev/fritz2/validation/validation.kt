package dev.fritz2.validation

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.action
import dev.fritz2.binding.each
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationJSTests {

    @Test
    fun testValidation() = runTest {
        initDocument()

        val carName = "ok"
        val c1 = Car(" ", Color(120, 120, 120))
        val c2 = Car("car1", Color(-1, -1, -1))
        val c3 = Car("car2", Color(256, 256, 256))
        val c4 = Car(" ", Color(256, -1, 120))

        val store =
            object : RootStore<Car>(
                Car(carName, Color(120, 120, 120))
            ) {
                override val update = handle<Car> { old, new ->
                    if (carValidator.isValid(new, Unit)) new else old
                }
            }

        val idData = "data-${uniqueId()}"
        val idMessages = "messages-${uniqueId()}"

        render {
            div {
                div(id = idData) {
                    store.data.map { it.name }.bind()
                }
                div(id = idMessages) {
                    carValidator.msgs.each(Message::text).render {
                        p {
                            +it.text
                        }
                    }.bind()
                }
            }
        }.mount(targetId)

        delay(100)
        val divData = document.getElementById(idData) as HTMLDivElement
        val divMessages = document.getElementById(idMessages) as HTMLDivElement

        assertEquals(carName, divData.textContent, "initial car name is wrong")
        assertEquals(0, divMessages.childElementCount, "there are messages")

        action(c1) handledBy store.update
        delay(100)

        assertEquals(carName, divData.innerText, "c1: car name has changed")
        assertEquals(1, divMessages.childElementCount, "c1: there is not 1 message")
        assertEquals(
            carNameIsBlank.text,
            divMessages.firstElementChild?.textContent,
            "c1: there is not expected message"
        )

        action(c2) handledBy store.update
        delay(100)

        assertEquals(carName, divData.innerText, "c2: car name has changed")
        assertEquals(1, divMessages.childElementCount, "c2: there is not 1 message")
        assertEquals(
            colorValuesAreToLow.text,
            divMessages.firstElementChild?.textContent,
            "c2: there is not expected message"
        )

        action(c3) handledBy store.update
        delay(100)

        assertEquals(carName, divData.innerText, "c3: car name has changed")
        assertEquals(1, divMessages.childElementCount, "c3: there is not 1 message")
        assertEquals(
            colorValuesAreToHigh.text,
            divMessages.firstElementChild?.textContent,
            "c3: there is not expected message"
        )

        action(c4) handledBy store.update
        delay(100)

        assertEquals(carName, divData.innerText, "c4: car name has changed")
        assertEquals(3, divMessages.childElementCount, "c4: there is not 3 message")
    }

}