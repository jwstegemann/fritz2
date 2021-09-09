package dev.fritz2.identification

import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.buildLens
import kotlin.test.Test
import kotlin.test.assertEquals

class InspectorTests {

    @Lenses
    data class Address(val street: String, val id: String = Id.next())

    val streetLens = buildLens(Address::street.name, Address::street) { p, v -> p.copy(street = v) }

    @Lenses
    data class Person(val name: String, val address: Address, val id: String = Id.next())

    val nameLens = buildLens(Person::name.name, Person::name) { p, v -> p.copy(name = v) }
    val addressLens = buildLens(Person::address.name, Person::address) { p, v -> p.copy(address = v) }

    @Test
    fun testInspectorPaths() {

        val rootData = Person("Foo", Address("Street 5"))
        val rootInspector = RootInspector(rootData)
        assertEquals("", rootInspector.path, "model id root not correct")
        assertEquals(rootData, rootInspector.data, "model data root not correct")

        val nameInspector = rootInspector.sub(nameLens)
        assertEquals(".name", nameInspector.path, "sub model id not correct")
        assertEquals(rootData.name, nameInspector.data, "sub model data not correct")

        val addressInspector = rootInspector.sub(addressLens)
        assertEquals(".address", addressInspector.path, "sub model id not correct")
        assertEquals(rootData.address, addressInspector.data, "sub model data not correct")

        val streetInspector = addressInspector.sub(streetLens)
        assertEquals(".address.street", streetInspector.path, "sub sub model id not correct")
        assertEquals(rootData.address.street, streetInspector.data, "sub sub model data not correct")
    }

    @Test
    fun testPathsByElementLens() {

        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList)

        val p1Inspector = rootInspector.sub(personList[0], Person::id)
        assertEquals(".${personList[0].id}", p1Inspector.path, "sub model id for element not correct")
        assertEquals(personList[0], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.sub(nameLens)
        assertEquals(".${personList[0].id}.name", p1NameInspector.path, "sub sub model id for element not correct")
        assertEquals(personList[0].name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.sub(addressLens).sub(streetLens)
        assertEquals(".${personList[0].id}.address.street", p1StreetInspector.path, "sub sub sub model id for element not correct")
        assertEquals(personList[0].address.street, p1StreetInspector.data, "sub sub sub model data for element not correct")
    }

    @Test
    fun testOnEachByElementLens() {
        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList)

        var i = 0
        rootInspector.inspectEach(Person::id) {
            assertEquals(".${personList[i].id}", it.path, "[$i] sub model id for element not correct")
            assertEquals(personList[i], it.data, "[$i] sub model data for element not correct")

            val p1NameInspector = it.sub(nameLens)
            assertEquals(".${personList[i].id}.name", p1NameInspector.path, "[$i] sub sub model id for element not correct")
            assertEquals(personList[i].name, p1NameInspector.data, "[$i] sub sub model data for element not correct")

            val p1StreetInspector = it.sub(addressLens).sub(streetLens)
            assertEquals(".${personList[i].id}.address.street", p1StreetInspector.path, "[$i] sub sub sub model id for element not correct")
            assertEquals(personList[i].address.street, p1StreetInspector.data, "[$i] sub sub sub model data for element not correct")

            i++
        }
    }

    @Test
    fun testPathsByPositionLens() {
        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList)

        val p1Inspector = rootInspector.sub(0)
        assertEquals(".0", p1Inspector.path, "sub model id for element not correct")
        assertEquals(personList[0], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.sub(nameLens)
        assertEquals(".0.name", p1NameInspector.path, "sub sub model id for element not correct")
        assertEquals(personList[0].name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.sub(addressLens).sub(streetLens)
        assertEquals(".0.address.street", p1StreetInspector.path, "sub sub sub model id for element not correct")
        assertEquals(personList[0].address.street, p1StreetInspector.data, "sub sub sub model data for element not correct")
    }

    @Test
    fun testOnEachByPositionLens() {
        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList)

        var i = 0
        rootInspector.inspectEach {
            assertEquals(".$i", it.path, "[$i] sub model id for element not correct")
            assertEquals(personList[i], it.data, "[$i] sub model data for element not correct")

            val p1NameInspector = it.sub(nameLens)
            assertEquals(".$i.name", p1NameInspector.path, "[$i] sub sub model id for element not correct")
            assertEquals(personList[i].name, p1NameInspector.data, "[$i] sub sub model data for element not correct")

            val p1StreetInspector = it.sub(addressLens).sub(streetLens)
            assertEquals(".$i.address.street", p1StreetInspector.path, "[$i] sub sub sub model id for element not correct")
            assertEquals(personList[i].address.street, p1StreetInspector.data, "[$i] sub sub sub model data for element not correct")

            i++
        }
    }
}