package dev.fritz2.validation

import kotlin.test.Test
import kotlin.test.assertEquals

data class Car(val name: String, val color: Color)

data class Color(val r: Int, val g: Int, val b: Int)

class Message(val text: String) : ValidationMessage {
    override fun failed(): Boolean = true
}

val carNameIsBlank = Message("car name can not be blank")
val colorValuesAreToLow = Message("color members are lower then 0")
val colorValuesAreToHigh = Message("color members are greater then 255")

val carValidator = object: Validator<Car, Message, Unit>() {
    override fun validate(data: Car, metadata: Unit): List<Message> {
        val msgs = mutableListOf<Message>()

        if (data.name.isBlank())
            msgs.add(carNameIsBlank)

        if (data.color.r < 0 || data.color.g < 0 || data.color.b < 0)
            msgs.add(colorValuesAreToLow)

        if (data.color.r > 255 || data.color.g > 255 || data.color.b > 255)
            msgs.add(colorValuesAreToHigh)

        return msgs
    }

}

class ValidationTests {

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