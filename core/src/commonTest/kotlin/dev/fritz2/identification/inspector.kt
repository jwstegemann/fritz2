package dev.fritz2.identification

import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.buildLens
import kotlin.test.Test
import kotlin.test.assertEquals

@Lenses
data class Address(val street: String, val id: String = uniqueId())

val streetLens = buildLens(Address::street.name, Address::street) { p, v -> p.copy(street = v) }

@Lenses
data class Person(val name: String, val address: Address, val id: String = uniqueId())

val nameLens = buildLens(Person::name.name, Person::name) { p, v -> p.copy(name = v) }
val addressLens = buildLens(Person::address.name, Person::address) { p, v -> p.copy(address = v) }

class InspectorTests {

    @Test
    fun testInspectorIds() {

        val rootData = Person("Foo", Address("Street 5"))
        val rootInspector = RootInspector(rootData,"start")
        assertEquals("start", rootInspector.id, "model id root not correct")
        assertEquals(rootData, rootInspector.data, "model data root not correct")

        val nameInspector = rootInspector.sub(nameLens)
        assertEquals("start.name", nameInspector.id, "sub model id not correct")
        assertEquals(rootData.name, nameInspector.data, "sub model data not correct")

        val addressInspector = rootInspector.sub(addressLens)
        assertEquals("start.address", addressInspector.id, "sub model id not correct")
        assertEquals(rootData.address, addressInspector.data, "sub model data not correct")

        val streetInspector = addressInspector.sub(streetLens)
        assertEquals("start.address.street", streetInspector.id, "sub sub model id not correct")
        assertEquals(rootData.address.street, streetInspector.data, "sub sub model data not correct")
    }

    @Test
    fun testIdsByElementLens() {

        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList, "start")

        val p1Inspector = rootInspector.sub(personList[0], Person::id)
        assertEquals("start.${personList[0].id}", p1Inspector.id, "sub model id for element not correct")
        assertEquals(personList[0], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.sub(nameLens)
        assertEquals("start.${personList[0].id}.name", p1NameInspector.id, "sub sub model id for element not correct")
        assertEquals(personList[0].name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.sub(addressLens).sub(streetLens)
        assertEquals("start.${personList[0].id}.address.street", p1StreetInspector.id, "sub sub sub model id for element not correct")
        assertEquals(personList[0].address.street, p1StreetInspector.data, "sub sub sub model data for element not correct")
    }

    @Test
    fun testIdsByPositionLens() {
        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList, "start")

        val p1Inspector = rootInspector.sub(0)
        assertEquals("start.0", p1Inspector.id, "sub model id for element not correct")
        assertEquals(personList[0], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.sub(nameLens)
        assertEquals("start.0.name", p1NameInspector.id, "sub sub model id for element not correct")
        assertEquals(personList[0].name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.sub(addressLens).sub(streetLens)
        assertEquals("start.0.address.street", p1StreetInspector.id, "sub sub sub model id for element not correct")
        assertEquals(personList[0].address.street, p1StreetInspector.data, "sub sub sub model data for element not correct")
    }
}