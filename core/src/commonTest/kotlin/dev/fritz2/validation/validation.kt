package dev.fritz2.validation

import dev.fritz2.identification.Inspector
import kotlin.test.Test
import kotlin.test.assertEquals

data class Car(val name: String, val color: Color)

data class Color(val r: Int, val g: Int, val b: Int)

class Message(val text: String) : ValidationMessage {
    override fun isError(): Boolean = true
}

val carNameIsBlank = Message("car name can not be blank")
val colorValuesAreToLow = Message("color members are lower then 0")
val colorValuesAreToHigh = Message("color members are greater then 255")

val carValidator get() = object : Validator<Car, Message, Unit>() {
    override fun MutableList<Message>.validate(inspector: Inspector<Car>, metadata: Unit) {
        if (inspector.data.name.isBlank()) add(carNameIsBlank)

        if (inspector.data.color.r < 0 || inspector.data.color.g < 0 || inspector.data.color.b < 0)
            add(colorValuesAreToLow)

        if (inspector.data.color.r > 255 || inspector.data.color.g > 255 || inspector.data.color.b > 255)
            add(colorValuesAreToHigh)
    }
}

class ValidationTests {

    @Test
    fun testValidation() {
        val c1 = Car(" ", Color(120, 120, 120))
        val c2 = Car("car1", Color(-1, -1, -1))
        val c3 = Car("car2", Color(256, 256, 256))
        val c4 = Car(" ", Color(256, -1, 120))

        assertEquals(carNameIsBlank, carValidator.getMessages(c1, Unit).first())
        assertEquals(colorValuesAreToLow, carValidator.getMessages(c2, Unit).first())
        assertEquals(colorValuesAreToHigh, carValidator.getMessages(c3, Unit).first())
        assertEquals(3, carValidator.getMessages(c4, Unit).size, "number of messages not right")
    }
}