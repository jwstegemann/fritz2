package dev.fritz2.core

import kotlin.test.Test
import kotlin.test.assertEquals

class InspectorTests {

    data class Address(val street: String, val id: String = Id.next())

    val streetLens = lensOf(Address::street.name, Address::street) { p, v -> p.copy(street = v) }

    data class Person(val name: String, val address: Address, val telephone: String? = null, val id: String = Id.next())

    val nameLens = lensOf(Person::name.name, Person::name) { p, v -> p.copy(name = v) }
    val addressLens = lensOf(Person::address.name, Person::address) { p, v -> p.copy(address = v) }
    val telephoneLens = lensOf(Person::telephone.name, Person::telephone) { p, v -> p.copy(telephone = v) }

    @Test
    fun testInspectorPaths() {

        val rootData = Person("Foo", Address("Street 5"))
        val rootInspector = inspectorOf(rootData)

        assertEquals("", rootInspector.path, "model id root not correct")
        assertEquals(rootData, rootInspector.data, "model data root not correct")

        val nameInspector = rootInspector.map(nameLens)
        assertEquals(".name", nameInspector.path, "sub model id not correct")
        assertEquals(rootData.name, nameInspector.data, "sub model data not correct")

        val addressInspector = rootInspector.map(addressLens)
        assertEquals(".address", addressInspector.path, "sub model id not correct")
        assertEquals(rootData.address, addressInspector.data, "sub model data not correct")

        val streetInspector = addressInspector.map(streetLens)
        assertEquals(".address.street", streetInspector.path, "sub sub model id not correct")
        assertEquals(rootData.address.street, streetInspector.data, "sub sub model data not correct")
    }

    @Test
    fun testMapNull() {
        val personA = Person("Hans", Address("Musterstreet 3"), "0138584/943")
        val inspectorA = inspectorOf(personA).map(telephoneLens).mapNull("")

        assertEquals(".telephone", inspectorA.path)
        assertEquals(personA.telephone, inspectorA.data)

        val personB = Person("Peter", Address("Musterstreet 5"))
        val inspectorB = inspectorOf(personB).map(telephoneLens).mapNull("no-num")

        assertEquals(".telephone", inspectorB.path)
        assertEquals("no-num", inspectorB.data)
    }

    @Test
    fun testPathsByElementLens() {

        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = inspectorOf(personList)

        val p1Inspector = rootInspector.mapByElement(personList[0], Person::id)
        assertEquals(".${personList[0].id}", p1Inspector.path, "sub model id for element not correct")
        assertEquals(personList[0], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.map(nameLens)
        assertEquals(".${personList[0].id}.name", p1NameInspector.path, "sub sub model id for element not correct")
        assertEquals(personList[0].name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.map(addressLens).map(streetLens)
        assertEquals(
            ".${personList[0].id}.address.street",
            p1StreetInspector.path,
            "sub sub sub model id for element not correct"
        )
        assertEquals(
            personList[0].address.street,
            p1StreetInspector.data,
            "sub sub sub model data for element not correct"
        )
    }

    @Test
    fun testOnEachByElementLens() {
        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = inspectorOf(personList)

        var i = 0
        rootInspector.inspectEach(Person::id) {
            assertEquals(".${personList[i].id}", it.path, "[$i] sub model id for element not correct")
            assertEquals(personList[i], it.data, "[$i] sub model data for element not correct")

            val p1NameInspector = it.map(nameLens)
            assertEquals(
                ".${personList[i].id}.name",
                p1NameInspector.path,
                "[$i] sub sub model id for element not correct"
            )
            assertEquals(personList[i].name, p1NameInspector.data, "[$i] sub sub model data for element not correct")

            val p1StreetInspector = it.map(addressLens).map(streetLens)
            assertEquals(
                ".${personList[i].id}.address.street",
                p1StreetInspector.path,
                "[$i] sub sub sub model id for element not correct"
            )
            assertEquals(
                personList[i].address.street,
                p1StreetInspector.data,
                "[$i] sub sub sub model data for element not correct"
            )

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

        val rootInspector = inspectorOf(personList)

        val p1Inspector = rootInspector.mapByIndex(0)
        assertEquals(".0", p1Inspector.path, "sub model id for element not correct")
        assertEquals(personList[0], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.map(nameLens)
        assertEquals(".0.name", p1NameInspector.path, "sub sub model id for element not correct")
        assertEquals(personList[0].name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.map(addressLens).map(streetLens)
        assertEquals(".0.address.street", p1StreetInspector.path, "sub sub sub model id for element not correct")
        assertEquals(
            personList[0].address.street,
            p1StreetInspector.data,
            "sub sub sub model data for element not correct"
        )
    }

    @Test
    fun testOnEachByPositionLens() {
        val personList = listOf(
            Person("p1", Address("p1s1")),
            Person("p2", Address("p2s1")),
            Person("p3", Address("p3s1"))
        )

        val rootInspector = inspectorOf(personList)

        var i = 0
        rootInspector.inspectEach {
            assertEquals(".$i", it.path, "[$i] sub model id for element not correct")
            assertEquals(personList[i], it.data, "[$i] sub model data for element not correct")

            val p1NameInspector = it.map(nameLens)
            assertEquals(".$i.name", p1NameInspector.path, "[$i] sub sub model id for element not correct")
            assertEquals(personList[i].name, p1NameInspector.data, "[$i] sub sub model data for element not correct")

            val p1StreetInspector = it.map(addressLens).map(streetLens)
            assertEquals(
                ".$i.address.street",
                p1StreetInspector.path,
                "[$i] sub sub sub model id for element not correct"
            )
            assertEquals(
                personList[i].address.street,
                p1StreetInspector.data,
                "[$i] sub sub sub model data for element not correct"
            )

            i++
        }
    }

    @Test
    fun testPathsByKeyLens() {
        val personList = mapOf(
            1 to Person("p1", Address("p1s1")),
            2 to Person("p2", Address("p2s1")),
            3 to Person("p3", Address("p3s1"))
        )

        val rootInspector = inspectorOf(personList)

        val p1Inspector = rootInspector.mapByKey(2)
        assertEquals(".2", p1Inspector.path, "sub model id for element not correct")
        assertEquals(personList[2], p1Inspector.data, "sub model data for element not correct")

        val p1NameInspector = p1Inspector.map(nameLens)
        assertEquals(".2.name", p1NameInspector.path, "sub sub model id for element not correct")
        assertEquals(personList[2]?.name, p1NameInspector.data, "sub sub model data for element not correct")

        val p1StreetInspector = p1Inspector.map(addressLens).map(streetLens)
        assertEquals(".2.address.street", p1StreetInspector.path, "sub sub sub model id for element not correct")
        assertEquals(
            personList[2]?.address?.street,
            p1StreetInspector.data,
            "sub sub sub model data for element not correct"
        )
    }

    @Test
    fun testOnEachByKeyLens() {
        val personList = mapOf(
            1 to Person("p1", Address("p1s1")),
            2 to Person("p2", Address("p2s1")),
            3 to Person("p3", Address("p3s1"))
        )

        val rootInspector = RootInspector(personList)

        var i = 1
        rootInspector.inspectEach { key, inspector ->
            assertEquals(i, key, "[$i] key for element not correct")
            assertEquals(".$i", inspector.path, "[$i] sub model id for element not correct")
            assertEquals(personList[i], inspector.data, "[$i] sub model data for element not correct")

            val p1NameInspector = inspector.map(nameLens)
            assertEquals(".$i.name", p1NameInspector.path, "[$i] sub sub model id for element not correct")
            assertEquals(personList[i]?.name, p1NameInspector.data, "[$i] sub sub model data for element not correct")

            val p1StreetInspector = inspector.map(addressLens).map(streetLens)
            assertEquals(
                ".$i.address.street",
                p1StreetInspector.path,
                "[$i] sub sub sub model id for element not correct"
            )
            assertEquals(
                personList[i]?.address?.street,
                p1StreetInspector.data,
                "[$i] sub sub sub model data for element not correct"
            )

            i++
        }
    }
}