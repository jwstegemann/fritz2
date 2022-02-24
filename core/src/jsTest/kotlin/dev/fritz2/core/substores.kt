package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class SubStoreTests {

    data class Person(val name: String, val address: Address)
    data class Address(val street: String = "", val postalCode: PostalCode)
    data class PostalCode(val code: Int)

    private val nameLens = lens("name", Person::name) { p, v -> p.copy(name = v) }
    private val addressLens = lens("address", Person::address) { p, v -> p.copy(address = v) }
    private val streetLens = lens("street", Address::street) { p, v -> p.copy(street = v) }
    private val postalCodeLens = lens("postalCode", Address::postalCode) { p, v -> p.copy(postalCode = v) }
    private val codeLens = lens("code", PostalCode::code) { p, v -> p.copy(code = v) }

    @Test
    fun testSubStore() = runTest {
        
        val person = Person("Foo", Address("Bar Street 3", PostalCode(9999)))
        val store = object : RootStore<Person>(person) {}

        val nameSub = store.sub(nameLens)
        val addressSub = store.sub(addressLens)
        val streetSub = addressSub.sub(streetLens)
        val postalCodeSub = addressSub.sub(postalCodeLens)
        val codeSub = postalCodeSub.sub(codeLens)

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
    fun testSubStoreWithFormat() = runTest {
        
        val person = Person("Foo", Address("Bar Street 3", PostalCode(9999)))
        val store = object : RootStore<Person>(person, id = "person") {}

        val personFormatLens = format(
            { value: String ->
                val fields = value.split(",")
                val name = fields[0]
                val street = fields[1]
                val code = fields[2].toInt()
                Person(name, Address(street, PostalCode(code)))
            }, { value: Person ->
                "${value.name},${value.address.street},${value.address.postalCode.code}"
            })

        val completeSub = store.sub(personFormatLens)

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
}