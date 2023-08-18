package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubStoreTests {

    data class Person(val name: String, val address: Address)
    data class Address(val street: String = "", val postalCode: PostalCode)
    data class PostalCode(val code: Int)

    private val nameLens = lensOf("name", Person::name) { p, v -> p.copy(name = v) }
    private val addressLens = lensOf("address", Person::address) { p, v -> p.copy(address = v) }
    private val streetLens = lensOf("street", Address::street) { p, v -> p.copy(street = v) }
    private val postalCodeLens = lensOf("postalCode", Address::postalCode) { p, v -> p.copy(postalCode = v) }
    private val codeLens = lensOf("code", PostalCode::code) { p, v -> p.copy(code = v) }

    @Test
    fun testSubStore() = runTest {
        val person = Person("Foo", Address("Bar Street 3", PostalCode(9999)))
        val store = object : RootStore<Person>(person) {}

        val nameSub = store.map(nameLens)
        val addressSub = store.map(addressLens)
        val streetSub = addressSub.map(streetLens)
        val postalCodeSub = addressSub.map(postalCodeLens)
        val codeSub = postalCodeSub.map(codeLens)

        val nameId = "name-${Id.next()}"
        val streetId = "street-${Id.next()}"
        val postalCodeId = "postalCode-${Id.next()}"

        render {
            div {
                label {
                    +"Name: "
                    div(id = nameId) { nameSub.data.renderText() }
                }
                label {
                    +"Street: "
                    div(id = streetId) { streetSub.data.renderText() }
                }
                label {
                    +"Postal code: "
                    div(id = postalCodeId) { codeSub.data.renderText() }
                }
            }
        }

        delay(200)

        val nameDiv = document.getElementById(nameId) as HTMLDivElement
        val streetDiv = document.getElementById(streetId) as HTMLDivElement
        val postalCodeDiv = document.getElementById(postalCodeId) as HTMLDivElement

        assertEquals(person.name, nameDiv.innerText, "name is not correct")
        assertEquals(person.address.street, streetDiv.innerText, "street is not correct")
        assertEquals(person.address.postalCode.code.toString(), postalCodeDiv.innerText, "postalCode is not correct")

        val newPerson = Person("Bar", Address("Foo St. 9", PostalCode(1111)))
        nameSub.update(newPerson.name)
        streetSub.update(newPerson.address.street)
        codeSub.update(newPerson.address.postalCode.code)

        delay(200)

        assertEquals(newPerson.name, nameDiv.innerText, "name is not correct")
        assertEquals(newPerson.address.street, streetDiv.innerText, "street is not correct")
        assertEquals(newPerson.address.postalCode.code.toString(), postalCodeDiv.innerText, "postalCode is not correct")
    }

    @Test
    fun testSubStoreWithLensOf() = runTest {
        val person = Person("Foo", Address("Bar Street 3", PostalCode(9999)))
        val store = object : RootStore<Person>(person, id = "person") {}

        val personFormatLens = lensOf(
            { value: Person ->
                "${value.name},${value.address.street},${value.address.postalCode.code}"
            },
            { value: String ->
                val fields = value.split(",")
                val name = fields[0]
                val street = fields[1]
                val code = fields[2].toInt()
                Person(name, Address(street, PostalCode(code)))
            },
        )

        val completeSub = store.map(personFormatLens)

        render {
            div {
                label {
                    +"Person: "
                    div(id = completeSub.id) { completeSub.data.renderText() }
                }
            }
        }

        delay(200)

        val completeDiv = document.getElementById(completeSub.id) as HTMLDivElement

        assertEquals(store.id, completeDiv.id)
        assertEquals(personFormatLens.get(person), completeDiv.innerText, "formatting is not working")

        val newPerson = Person("Bar", Address("Foo St. 9", PostalCode(1111)))
        completeSub.update(personFormatLens.get(newPerson))

        delay(200)

        assertEquals(personFormatLens.get(newPerson), completeDiv.innerText, "parsing is not working")
    }

    @Test
    fun testSubStoreWithRenderEach() = runTest {
        val id = Id.next()
        val store = storeOf(listOf("a", "b", "c"))

        lateinit var bStore: Store<String>

        render {
            div(id = id) {
                store.renderEach({ it }, this) {
                    if (it.current == "b") bStore = it
                    p {
                        it.data.renderText()
                    }
                }
            }
        }

        delay(200)

        val container = document.getElementById(id) as HTMLDivElement

        assertEquals(3, container.childElementCount)
        assertEquals("abc", container.textContent)

        bStore.update("d")
        delay(200)
        assertEquals(3, container.childElementCount)
        assertEquals("adc", container.textContent)
    }

    @Test
    fun testSubStoreWithRenderEachSameId() = runTest {
        val id = Id.next()
        val store = object : RootStore<List<String>>(listOf("a", "b", "c", "b")) {
            var throwable: Throwable? = null
            override fun errorHandler(cause: Throwable) {
                throwable = cause
            }
        }

        var bStore: Store<String>? = null

        render {
            div(id = id) {
                store.renderEach({ it }, this) {
                    if (bStore == null && it.current == "b") bStore = it
                    p {
                        it.data.renderText()
                    }
                }
            }
        }

        delay(200)

        val container = document.getElementById(id) as HTMLDivElement

        assertEquals(4, container.childElementCount)
        assertEquals("abcb", container.textContent)
        assertEquals(null, store.throwable)

        bStore?.update("d")

        delay(200)
        assertTrue(store.throwable is CollectionLensSetException)
    }

    @Test
    fun testSubStoreWithIndex() = runTest {
        val id = Id.next()
        val store = storeOf(listOf("a", "b", "c"))

        render {
            div(id = id) {
                store.data.renderEach(into = this) {
                    p {
                        +it
                    }
                }
            }
        }

        delay(200)

        val container = document.getElementById(id) as HTMLDivElement

        assertEquals(3, container.childElementCount)
        assertEquals("abc", container.textContent)

        store.mapByIndex(1).update("d")

        delay(200)
        assertEquals(3, container.childElementCount)
        assertEquals("adc", container.textContent)
    }

    @Test
    fun testSubStoreWithMap() = runTest {
        val id = Id.next()
        val store = storeOf(mapOf(1 to "a", 2 to "b", 3 to "c"))

        render {
            div(id = id) {
                store.data.map { it.values.toList() }.renderEach(into = this) {
                    p {
                        +it
                    }
                }
            }
        }

        delay(200)

        val container = document.getElementById(id) as HTMLDivElement

        assertEquals(3, container.childElementCount)
        assertEquals("abc", container.textContent)

        store.mapByKey(2).update("d")

        delay(200)
        assertEquals(3, container.childElementCount)
        assertEquals("adc", container.textContent)
    }
}
