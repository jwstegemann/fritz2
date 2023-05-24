package dev.fritz2.validation

import dev.fritz2.core.Id
import dev.fritz2.core.lensOf
import dev.fritz2.core.render
import dev.fritz2.runTest
import dev.fritz2.validation.test.*
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
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
        assertEquals(3, divMessages.childElementCount, "c1: there is not 3 message")
        assertEquals(
            colorValuesAreTooLow,
            divMessages.firstElementChild?.textContent,
            "c1: there is not a expected message"
        )

        store.update(c2)
        delay(100)

        assertEquals(c2.name, divData.innerText, "c2: car name has changed")
        assertEquals(3, divMessages.childElementCount, "c2: there is not 3 message")
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
        val colorStore = store.map(Car.colorLens)
        val rColorStore = colorStore.map(Color.rLens)
        val gColorStore = colorStore.map(Color.gLens)
        val bColorStore = colorStore.map(Color.bLens)

        val idData = "data-${Id.next()}"
        val idMessagesIntermediateLevel = "messages-${Id.next()}"
        val idPathIntermediateLevel = "messages-path-${Id.next()}"
        val idMessagesR = "messages-r-${Id.next()}"
        val idMessagesG = "messages-g-${Id.next()}"
        val idMessagesB = "messages-b-${Id.next()}"

        render {
            div {
                div(id = idData) {
                    colorStore.data.map { "${it.r}, ${it.g}, ${it.b}" }.renderText()
                }
                div(id = idMessagesIntermediateLevel) {
                    colorStore.messages<Message>()?.renderEach(Message::path, into = this) {
                        p {
                            +it.text
                        }
                    }
                }
                div(id = idPathIntermediateLevel) {
                    colorStore.messages<Message>()?.map { messages -> messages.all { it.path.startsWith(".color") } }
                        ?.renderText(into = this)
                }
                div(id = idMessagesR) {
                    rColorStore.messages<Message>()?.renderEach(Message::path, into = this) {
                        p {
                            +it.path
                        }
                    }
                }
                div(id = idMessagesG) {
                    gColorStore.messages<Message>()?.renderEach(Message::path, into = this) {
                        p {
                            +it.path
                        }
                    }
                }
                div(id = idMessagesB) {
                    bColorStore.messages<Message>()?.renderEach(Message::path, into = this) {
                        p {
                            +it.path
                        }
                    }
                }
            }
        }

        // Test Intermediate Level (color-Field of Car)
        delay(100)
        val divData = document.getElementById(idData) as HTMLDivElement
        val divMessagesIntermediateLevel = document.getElementById(idMessagesIntermediateLevel) as HTMLDivElement
        val divPathIntermediateLevel = document.getElementById(idPathIntermediateLevel) as HTMLDivElement
        val divMessagesR = document.getElementById(idMessagesR) as HTMLDivElement
        val divMessagesG = document.getElementById(idMessagesG) as HTMLDivElement
        val divMessagesB = document.getElementById(idMessagesB) as HTMLDivElement

        assertEquals("120, 120, 120", divData.textContent, "initial car color is wrong")
        assertEquals(0, divMessagesIntermediateLevel.childElementCount, "there are messages")

        store.update(Car("car1", Color(-1, -1, -1)))
        delay(100)

        assertEquals("-1, -1, -1", divData.textContent, "c1: car color has not changed")
        assertEquals(3, divMessagesIntermediateLevel.childElementCount, "c1: there is not 3 message")
        assertEquals(
            colorValuesAreTooLow,
            divMessagesIntermediateLevel.firstElementChild?.textContent,
            "c1: there is not a expected message"
        )
        assertEquals("true", divPathIntermediateLevel.textContent, "paths start not all with .color")

        // Test Leave Nodes (fields of Color)
        assertEquals(1, divMessagesR.childElementCount, "r-Field error message not present")
        assertEquals(".color.r", divMessagesR.textContent)
        assertEquals(1, divMessagesG.childElementCount, "g-Field error message not present")
        assertEquals(".color.g", divMessagesG.textContent)
        assertEquals(1, divMessagesB.childElementCount, "b-Field error message not present")
        assertEquals(".color.b", divMessagesB.textContent)
    }

}

data class Bar(val foo: String, val foobar: String) {
    companion object {
        val fooLens = lensOf("foo", Bar::foo) { p, v -> p.copy(foo = v) }
        val foobarLens = lensOf("foobar", Bar::foobar) { p, v -> p.copy(foobar = v) }

        val validate: Validation<Bar, Unit, Message> = validation { inspector ->
            add(Message(inspector.map(fooLens).path, "foo ist falsch"))
            add(Message(inspector.map(foobarLens).path, "foobar ist falsch"))
        }
    }
}

data class Foo(val foo: String, val foobar: String, val bar: Bar) {
    companion object {
        val fooLens = lensOf("foo", Foo::foo) { p, v -> p.copy(foo = v) }
        val foobarLens = lensOf("foobar", Foo::foobar) { p, v -> p.copy(foobar = v) }
        val barLens = lensOf("bar", Foo::bar) { p, v -> p.copy(bar = v) }

        val validate: Validation<Foo, Unit, Message> = validation { inspector ->
            add(Message(inspector.map(fooLens).path, "foo ist falsch"))
            add(Message(inspector.map(foobarLens).path, "foobar ist falsch"))
            addAll(Bar.validate(inspector.map(barLens), Unit))
        }
    }
}

class MessageFilterTests {

    @Test
    fun testOverlappingFieldnamesDoNotMatchEachOthersPathes() = runTest {
        val initial = Foo("", "", Bar("", ""))
        val store = storeOf(initial, Foo.validate)
        store.update(initial.copy(foo = "a"))
        /*
        Validation should result in messages with these pathes:
        ```
        .foo
        .foobar
        .bar.foo
        .bar.foobar
        ```
        Messages of `foo` should not appear in mapped store of `foobar` -> so no overlapping!
        */
        val id = Id.next()

        render {
            span(id = id) {
                // 1
                store.map(Foo.fooLens).messages<Message>()?.map { it.size.toString() }?.renderText()
                // 1
                store.map(Foo.foobarLens).messages<Message>()?.map { it.size.toString() }?.renderText()
                // 2
                store.map(Foo.barLens).messages<Message>()?.map { it.size.toString() }?.renderText()
                // 1
                store.map(Foo.fooLens).messages<Message>()?.map { it.size.toString() }?.renderText()
                // 1
                store.map(Foo.fooLens).messages<Message>()?.map { it.size.toString() }?.renderText()
            }
        }

        delay(100)
        val span = document.getElementById(id) as HTMLSpanElement
        assertEquals("11211", span.textContent)
    }
}

