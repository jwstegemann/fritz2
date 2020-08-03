package dev.fritz2.validation

import dev.fritz2.lenses.Lenses
import kotlin.test.Test
import kotlin.test.assertEquals

val carNameIsBlank = ValidationTests.Message("car name can not be blank")
val colorValuesAreToLow = ValidationTests.Message("color members are lower then 0")
val colorValuesAreToHigh = ValidationTests.Message("color members are greater then 255")

val carValidator = validator { car: ValidationTests.Car, _: Unit ->
    val msgs = mutableListOf<ValidationTests.Message>()

    if (car.name.isBlank())
        msgs.add(carNameIsBlank)

    if (car.color.r < 0 || car.color.g < 0 || car.color.b < 0)
        msgs.add(colorValuesAreToLow)

    if (car.color.r > 255 || car.color.g > 255 || car.color.b > 255)
        msgs.add(colorValuesAreToHigh)

    msgs
}

class ValidationTests {

    @Lenses
    data class Car(val name: String, val color: Color)

    @Lenses
    data class Color(val r: Int, val g: Int, val b: Int)

    class Message(val text: String) : ValidationMessage {
        override fun failed(): Boolean = true

    }

    @Test
    fun testValidation() {
        val c1 = Car(" ", Color(120, 120, 120))
        val c2 = Car("car1", Color(-1, -1, -1))
        val c3 = Car("car2", Color(256, 256, 256))
        val c4 = Car(" ", Color(256, -1, 120))

        assertEquals(carNameIsBlank, carValidator.validate(c1, Unit).first())
        assertEquals(colorValuesAreToLow, carValidator.validate(c2, Unit).first())
        assertEquals(colorValuesAreToHigh, carValidator.validate(c3, Unit).first())
        assertEquals(3, carValidator.validate(c4, Unit).size, "number of messages not right")
    }
}