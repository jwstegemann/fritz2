package dev.fritz2.identification

import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.buildLens
import kotlin.test.Test
import kotlin.test.assertEquals

@Lenses
data class Address(val street: String, val id: String = uniqueId())

val streetLens = buildLens(Address::street.name, Address::street, { p, v -> p.copy(street = v) })

@Lenses
data class Person(val name: String, val address: Address, val id: String = uniqueId())

val nameLens = buildLens(Person::name.name, Person::name, { p, v -> p.copy(name = v) })
val addressLens = buildLens(Person::address.name, Person::address, { p, v -> p.copy(address = v) })


class ModelIdTests {

    @Test
    fun testModelIds() {
        val rootId = RootModelId<Person>("start")
        assertEquals("start", rootId.id, "model id root not correct")

        val nameId = rootId.sub(nameLens)
        assertEquals("start.name", nameId.id, "sub model id not correct")

        val addressId = rootId.sub(addressLens)
        assertEquals("start.address", addressId.id, "sub model id not correct")

        val streetId = addressId.sub(streetLens)
        assertEquals("start.address.street", streetId.id, "sub sub model id not correct")
    }

    @Test
    fun testIdsByElementLens() {
        val rootId = RootModelId<List<Person>>("start")

        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val p1Id = rootId.sub(personList[0], Person::id)
        assertEquals("start.${personList[0].id}", p1Id.id, "sub model id for element not correct")

        val p1NameId = p1Id.sub(nameLens)
        assertEquals("start.${personList[0].id}.name", p1NameId.id, "sub sub model id for element not correct")
    }

    @Test
    fun testIdsByPositionLens() {
        val rootId = RootModelId<List<Person>>("start")

        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val p1Id = rootId.sub<Person>(0)
        assertEquals("start.0", p1Id.id, "sub model id for element not correct")

        val p1NameId = p1Id.sub(nameLens)
        assertEquals("start.0.name", p1NameId.id, "sub sub model id for element not correct")
    }
}