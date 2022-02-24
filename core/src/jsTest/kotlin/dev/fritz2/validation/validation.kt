package dev.fritz2.validation

import dev.fritz2.core.Id
import dev.fritz2.core.lens
import dev.fritz2.core.render
import dev.fritz2.runTest
import dev.fritz2.validation.test.*
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationJSTests {

    @Test
    fun testValidation() = runTest {
        
        val carName = "ok"
        val c1 = Car("car1", Color(-1, -1, -1))
        val c2 = Car("car2", Color(256, 256, 256))
        val c3 = Car("car3", Color(256, -1, 120))

        val store: ValidatingStore<Car, Unit, Message> =
            storeOf(Car(carName, Color(120, 120, 120)), Car.validator)

        val idData = "data-${Id.next()}"
        val idMessages = "messages-${Id.next()}"

        render {
            div {
                div(id = idData) {
                    store.data.map { it.name }.renderText()
                }
                div(id = idMessages) {
                    store.messages.renderEach(Message::text, into = this) {
                        p {
                            +it.text
                        }
                    }
                }
            }
        }

        delay(100)
        val divData = document.getElementById(idData) as HTMLDivElement
        val divMessages = document.getElementById(idMessages) as HTMLDivElement

        assertEquals(carName, divData.textContent, "initial car name is wrong")
        assertEquals(0, divMessages.childElementCount, "there are messages")

        store.update(c1)
        delay(100)

        assertEquals(c1.name, divData.innerText, "c1: car name has not changed")
        assertEquals(1, divMessages.childElementCount, "c1: there is not 1 message")
        assertEquals(
            colorValuesAreTooLow,
            divMessages.firstElementChild?.textContent,
            "c1: there is not a expected message"
        )

        store.update(c2)
        delay(100)

        assertEquals(c2.name, divData.innerText, "c2: car name has changed")
        assertEquals(1, divMessages.childElementCount, "c2: there is not 1 message")
        assertEquals(
            colorValuesAreTooHigh,
            divMessages.firstElementChild?.textContent,
            "c2: there is not expected message"
        )

        store.update(c3)
        delay(100)

        assertEquals(c3.name, divData.innerText, "c3: car name has changed")
        assertEquals(2, divMessages.childElementCount, "c3: there is not 3 message")
    }

    @Test
    fun testSubStoreValidation() = runTest {
        
        val store: ValidatingStore<Car, Unit, Message> =
            storeOf(Car("car", Color(120, 120, 120)), Car.validator)
        val colorLens = lens("color", Car::color) { car, color -> car.copy(color = color) }
        val colorStore = store.sub(colorLens)

        val idData = "data-${Id.next()}"
        val idMessages = "messages-${Id.next()}"

        render {
            div {
                div(id = idData) {
                    colorStore.data.map { "${it.r}, ${it.g}, ${it.b}" }.renderText()
                }
                div(id = idMessages) {
                    colorStore.messages<Message>()?.renderEach(Message::text, into = this) {
                        p {
                            +it.text
                        }
                    }
                }
            }
        }

        delay(100)
        val divData = document.getElementById(idData) as HTMLDivElement
        val divMessages = document.getElementById(idMessages) as HTMLDivElement

        assertEquals("120, 120, 120", divData.textContent, "initial car color is wrong")
        assertEquals(0, divMessages.childElementCount, "there are messages")

        store.update(Car("car1", Color(-1, -1, -1)))
        delay(100)

        assertEquals("-1, -1, -1", divData.innerText, "c1: car color has not changed")
        assertEquals(1, divMessages.childElementCount, "c1: there is not 1 message")
        assertEquals(
            colorValuesAreTooLow,
            divMessages.firstElementChild?.textContent,
            "c1: there is not a expected message"
        )
    }
}