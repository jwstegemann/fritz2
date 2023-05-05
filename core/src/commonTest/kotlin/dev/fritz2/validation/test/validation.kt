// package name conflict when test-package is equal to main-package
// do not change until it is fixed by Jetbrains
package dev.fritz2.validation.test

import dev.fritz2.core.Lens
import dev.fritz2.core.lensOf
import dev.fritz2.validation.Validation
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.validation
import kotlin.test.Test
import kotlin.test.assertEquals

class Message(override val path: String, val text: String) : ValidationMessage {
    override val isError: Boolean = true
}

data class Car(val name: String, val color: Color) {
    companion object {
        // We cannot use @Lenses annotation in fritz2.core itself, so we must craft this by hand!
        val nameLens: Lens<Car, String> = lensOf("name", Car::name) { parent, value -> parent.copy(name = value) }
        val colorLens: Lens<Car, Color> = lensOf("color", Car::color) { parent, value -> parent.copy(color = value) }

        val validator: Validation<Car, Unit, Message> = validation { inspector ->
            inspector.map(nameLens).let { nameInspector ->
                if (nameInspector.data.isBlank())
                    add(Message(nameInspector.path, carNameIsBlank))
            }
            addAll(Color.validator(inspector.map(colorLens)))
        }
    }
}

data class Color(val r: Int, val g: Int, val b: Int) {
    companion object {
        // We cannot use @Lenses annotation in fritz2.core itself, so we must craft this by hand!
        val rLens: Lens<Color, Int> = lensOf("r", Color::r) { parent, value -> parent.copy(r = value) }
        val gLens: Lens<Color, Int> = lensOf("g", Color::g) { parent, value -> parent.copy(g = value) }
        val bLens: Lens<Color, Int> = lensOf("b", Color::b) { parent, value -> parent.copy(b = value) }

        val validator: Validation<Color, Unit, Message> = validation { inspector ->
            listOf(rLens, gLens, bLens).forEach { fieldLens ->
                inspector.map(fieldLens).let { fieldInspector ->
                    if (fieldInspector.data < 0) add(Message(fieldInspector.path, colorValuesAreTooLow))
                    if (fieldInspector.data > 255) add(Message(fieldInspector.path, colorValuesAreTooHigh))
                }
            }
        }
    }
}

val carNameIsBlank = "car name can not be blank"
val colorValuesAreTooLow = "color members are lower then 0"
val colorValuesAreTooHigh = "color members are greater then 255"

// Would be some type from a dedicated date-library of course!
data class LocalDate(val year: Int, val month: Int, val day: Int) {
    operator fun compareTo(other: LocalDate): Int = when {
        year < other.year -> -1
        year > other.year -> 1
        else -> when {
            month < other.month -> -1
            month > other.month -> 1
            else -> when {
                day < other.day -> -1
                day > other.day -> 1
                else -> 0
            }
        }
    }
}

data class Person(
    val name: String,
    val birthday: LocalDate,
    val address: Address
) {
    data class ValidationMetaData(val today: LocalDate, val knownCities: Set<String>)

    companion object {
        // We cannot use @Lenses annotation in fritz2.core itself, so we must craft this by hand!
        val nameLens: Lens<Person, String> = lensOf("name", Person::name) { parent, value -> parent.copy(name = value) }
        val birthdayLens: Lens<Person, LocalDate> =
            lensOf("birthday", Person::birthday) { parent, value -> parent.copy(birthday = value) }
        val addressLens: Lens<Person, Address> =
            lensOf("address", Person::address) { parent, value -> parent.copy(address = value) }

        val validate: Validation<Person, ValidationMetaData, Message> = validation { inspector, meta ->
            inspector.map(nameLens).let { nameInspector ->
                if (nameInspector.data.isBlank()) add(Message(nameInspector.path, "Name must not be blank!"))
            }
            inspector.map(birthdayLens).let { birthdayInspector ->
                if (birthdayInspector.data > meta!!.today)
                    add(Message(birthdayInspector.path, "Birthday must not be in the future!"))
            }
            addAll(Address.validate(inspector.map(addressLens), meta!!.knownCities))
        }
    }
}

data class Address(
    val street: String,
    val city: String
) {
    companion object {
        // We cannot use @Lenses annotation in fritz2.core itself, so we must craft this by hand!
        val streetLens: Lens<Address, String> =
            lensOf("street", Address::street) { parent, value -> parent.copy(street = value) }
        val cityLens: Lens<Address, String> =
            lensOf("city", Address::city) { parent, value -> parent.copy(city = value) }

        val validate: Validation<Address, Set<String>, Message> = validation { inspector, cities ->
            inspector.map(streetLens).let { streetInspector ->
                if (streetInspector.data.isBlank()) add(Message(streetInspector.path, "Street must not be blank!"))
            }
            inspector.map(cityLens).let { cityInspector ->
                if (!cities!!.contains(cityInspector.data)) add(Message(cityInspector.path, "City does not exist!"))
            }
        }
    }
}

class ValidationTests {

    @Test
    fun testCommonValidation() {
        val c1 = Car(" ", Color(120, 120, 120))
        val c2 = Car("car1", Color(-1, -1, -1))
        val c3 = Car("car2", Color(256, 256, 256))
        val c4 = Car(" ", Color(256, -1, 120))

        assertEquals(carNameIsBlank, Car.validator(c1).first().text)
        assertEquals(".name", Car.validator(c1).first().path)
        assertEquals(colorValuesAreTooLow, Car.validator.invoke(c2).first().text)
        assertEquals(".color.r", Car.validator.invoke(c2).first().path)
        assertEquals(".color.g", Car.validator.invoke(c2)[1].path)
        assertEquals(".color.b", Car.validator.invoke(c2)[2].path)
        assertEquals(colorValuesAreTooHigh, Car.validator(c3).first().text)
        assertEquals(3, Car.validator(c4).size, "number of messages not right")
    }

    @Test
    fun canUseComposedValidatorAlone() {
        val colorWithTooLowR = Color(-1, 42, 42)
        val colorWithTooLowG = Color(42, -1, 42)
        val colorWithTooLowB = Color(42, 42, -1)

        val colorWithTooHighR = Color(256, 42, 42)
        val colorWithTooHighG = Color(42, 256, 42)
        val colorWithTooHighB = Color(42, 42, 256)

        assertEquals(colorValuesAreTooLow, Color.validator(colorWithTooLowR).first().text)
        assertEquals(".r", Color.validator(colorWithTooLowR).first().path)
        assertEquals(colorValuesAreTooLow, Color.validator(colorWithTooLowG).first().text)
        assertEquals(".g", Color.validator(colorWithTooLowG).first().path)
        assertEquals(colorValuesAreTooLow, Color.validator(colorWithTooLowB).first().text)
        assertEquals(".b", Color.validator(colorWithTooLowB).first().path)

        assertEquals(colorValuesAreTooHigh, Color.validator(colorWithTooHighR).first().text)
        assertEquals(".r", Color.validator(colorWithTooHighR).first().path)
        assertEquals(colorValuesAreTooHigh, Color.validator(colorWithTooHighG).first().text)
        assertEquals(".g", Color.validator(colorWithTooHighG).first().path)
        assertEquals(colorValuesAreTooHigh, Color.validator(colorWithTooHighB).first().text)
        assertEquals(".b", Color.validator(colorWithTooHighB).first().path)
    }

    @Test
    fun canUseMetaDataWithComposedValidation() {
        val fritz = Person(
            "", // must not be empty!
            LocalDate(1712, 1, 24),
            Address("Am Schloss", "Potsdam") // city not in known cities list, see below
        )

        val errors = Person.validate(
            fritz,
            Person.ValidationMetaData(
                LocalDate(1700, 1, 1), // set "today" into the past
                setOf("Berlin", "Hamburg", "Braunschweig") // remember: no Potsdam inside
            )
        )

        assertEquals(3, errors.size, "Amount of error messages is false!")
        assertEquals("Name must not be blank!", errors.first().text)
        assertEquals(".name", errors.first().path)
        assertEquals("Birthday must not be in the future!", errors[1].text)
        assertEquals(".birthday", errors[1].path)
        assertEquals("City does not exist!", errors[2].text)
        assertEquals(".address.city", errors[2].path)
    }
}
