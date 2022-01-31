// package name conflict when test-package is equal to main-package
// do not change until it is fixed by Jetbrains
package dev.fritz2.validation.test

import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.validation
import kotlin.test.Test
import kotlin.test.assertEquals

data class Car(val name: String, val color: Color) {
    companion object {
        val validator = validation<Car, Message> { inspector ->
            if (inspector.data.name.isBlank())
                add(Message(".name", carNameIsBlank))

            if (inspector.data.color.r < 0 || inspector.data.color.g < 0 || inspector.data.color.b < 0)
                add(Message(".color", colorValuesAreTooLow))

            if (inspector.data.color.r > 255 || inspector.data.color.g > 255 || inspector.data.color.b > 255)
                add(Message(".color", colorValuesAreTooHigh))
        }
    }
}

data class Color(val r: Int, val g: Int, val b: Int)

class Message(override val path: String, val text: String) : ValidationMessage {
    override val isError: Boolean = true
}

val carNameIsBlank = "car name can not be blank"
val colorValuesAreTooLow = "color members are lower then 0"
val colorValuesAreTooHigh = "color members are greater then 255"

class ValidationTests {

    @Test
    fun testCommonValidation() {
        val c1 = Car(" ", Color(120, 120, 120))
        val c2 = Car("car1", Color(-1, -1, -1))
        val c3 = Car("car2", Color(256, 256, 256))
        val c4 = Car(" ", Color(256, -1, 120))

        assertEquals(carNameIsBlank, Car.validator(c1).first().text)
        assertEquals(colorValuesAreTooLow, Car.validator.invoke(c2).first().text)
        assertEquals(colorValuesAreTooHigh, Car.validator(c3).first().text)
        assertEquals(3, Car.validator(c4).size, "number of messages not right")
    }
}