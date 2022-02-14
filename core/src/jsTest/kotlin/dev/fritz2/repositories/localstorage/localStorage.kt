package dev.fritz2.repositories.localstorage

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.lens
import dev.fritz2.repositories.ResourceNotFoundException
import dev.fritz2.resource.Resource
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.*

class LocalStorageTests {
    @Serializable
    data class LocalPerson(val name: String, val age: Int, val _id: String = Id.next())

    private val nameLens = lens("name", LocalPerson::name) { p, v -> p.copy(name = v) }
    private val ageLens = lens("age", LocalPerson::age) { p, v -> p.copy(age = v) }
    private val idLens = lens("id", LocalPerson::_id) { p, v -> p.copy(_id = v) }


    object PersonResource : Resource<LocalPerson, String> {
        override val idProvider: IdProvider<LocalPerson, String> = LocalPerson::_id
        override fun serialize(item: LocalPerson): String = Json.encodeToString(LocalPerson.serializer(), item)
        override fun deserialize(source: String): LocalPerson = Json.decodeFromString(LocalPerson.serializer(), source)
        override fun serializeList(items: List<LocalPerson>): String =
            Json.encodeToString(ListSerializer(LocalPerson.serializer()), items)
        override fun deserializeList(source: String): List<LocalPerson> =
            Json.decodeFromString(ListSerializer(LocalPerson.serializer()), source)
    }

    @Test
    fun testEntityService() = runTest {
        initDocument()

        val defaultPerson = LocalPerson("", 0)
        val startPerson = LocalPerson("Heinz", 18)
        val changedAge = 99

        val entityStore = object : RootStore<LocalPerson>(defaultPerson) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }

            val localStorage = localStorageEntityOf(PersonResource, "")

            val load = handle { _, id: String -> localStorage.load(id) }

            val saveOrUpdate = handle { entity -> localStorage.addOrUpdate(entity) }
            val delete = handle { entity -> localStorage.delete(entity); defaultPerson }
        }

        assertFailsWith(ResourceNotFoundException::class) {
            entityStore.localStorage.load("unknown")
        }

        val nameId = "name-${Id.next()}"
        val nameSubStore = entityStore.sub(nameLens)
        val ageId = "age-${Id.next()}"
        val ageSubStore = entityStore.sub(ageLens)
        val idId = "id-${Id.next()}"
        val idSubStore = entityStore.sub(idLens)


        render {
            div {
                div(id = idId) { idSubStore.data.renderText() }
                div(id = nameId) { nameSubStore.data.renderText() }
                div(id = ageId) { ageSubStore.data.renderText() }
            }
        }

        entityStore.update(startPerson)
        delay(100)

        val nameAfterStart = document.getElementById(nameId)?.textContent
        assertEquals(startPerson.name, nameAfterStart, "wrong name on start")
        val idAfterStart = document.getElementById(idId)?.textContent
        assertEquals(startPerson._id, idAfterStart, "wrong id on start")
        val ageAfterStart = document.getElementById(ageId)?.textContent
        assertEquals(startPerson.age.toString(), ageAfterStart, "wrong age on start")

        ageSubStore.update(data = changedAge)
        entityStore.saveOrUpdate()
        delay(200)

        val ageAfterUpdate = document.getElementById(ageId)?.textContent
        assertEquals(changedAge.toString(), ageAfterUpdate, "wrong age after update")

        ageSubStore.update(data = 0)
        entityStore.load(startPerson._id)
        delay(200)

        val ageAfterLoad = document.getElementById(ageId)?.textContent
        assertEquals("99", ageAfterLoad, "wrong age after load")

        entityStore.delete()
        delay(200)

        val nameAfterDelete = document.getElementById(nameId)?.textContent
        assertEquals("", nameAfterDelete, "wrong name after delete")
    }


    @Test
    fun testQueryService() = runTest {
        initDocument()

        val testList = listOf(
            LocalPerson("A", 0),
            LocalPerson("B", 1),
            LocalPerson("C", 0),
            LocalPerson("D", 1),
            LocalPerson("E", 0)
        )

        val queryStore = object : RootStore<List<LocalPerson>>(emptyList()) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }

            private val localStorage =
                localStorageQueryOf(PersonResource, "") { entities, _: Unit ->
                    entities.sortedBy(LocalPerson::name)
                }
            val addOrUpdate = handle<LocalPerson> { entities, person -> localStorage.addOrUpdate(entities, person) }
            val query = handle<Unit> { _, query -> localStorage.query(query) }
            val delete = handle<String> { entities, id -> localStorage.delete(entities, id) }
        }

        val listId = "list-${Id.next()}"
        val firstPersonId = "first-${Id.next()}"

        render {
            div {
                ul(id = listId) {
                    queryStore.renderEach(LocalPerson::_id) { p ->
                        li { p.data.map { it.name }.renderText() }
                    }
                }
                span(id = firstPersonId) {
                    queryStore.data.map {
                        if (it.isEmpty()) ""
                        else it.first()._id
                    }.renderText()
                }
            }
        }

        delay(100)

        testList.forEach {
            queryStore.addOrUpdate(it)
            delay(1)
        }

        delay(250)

        queryStore.query()
        delay(250)

        val listAfterQuery = document.getElementById(listId)?.textContent
        assertEquals(testList.joinToString("") { it.name }, listAfterQuery, "wrong list after query")

        val firstId = document.getElementById(firstPersonId)?.textContent
        assertTrue(firstId != null && firstId.isNotEmpty())

        queryStore.delete(firstId)
        delay(250)

        val listAfterDelete = document.getElementById(listId)?.textContent
        assertEquals(testList.drop(1).joinToString("") { it.name }, listAfterDelete, "wrong list after query")

        queryStore.update(emptyList())
        delay(1)
        queryStore.query()
        delay(250)

        val listAfterDeleteAndQuery = document.getElementById(listId)?.textContent
        assertEquals(testList.drop(1).joinToString("") { it.name }, listAfterDeleteAndQuery, "wrong list after query")
    }

    @Test
    fun testQueryServiceUpdates() = runTest {
        initDocument()

        val testList = listOf(
            LocalPerson("A", 0),
            LocalPerson("B", 1),
            LocalPerson("C", 0),
            LocalPerson("D", 1),
            LocalPerson("E", 0)
        )

        val queryStore = object : RootStore<List<LocalPerson>>(emptyList()) {
            override fun errorHandler(cause: Throwable) {
                fail(cause.message)
            }

            private val localStorage: LocalStorageQuery<LocalPerson, String, Unit> = localStorageQueryOf(PersonResource, "")

            val addOrUpdate = handle<LocalPerson> { entities, entity -> localStorage.addOrUpdate(entities, entity) }
            val updateMany = handle<List<LocalPerson>> { entities, updatedEntities -> localStorage.updateMany(entities, updatedEntities) }
        }

        val listId = "list-${Id.next()}"

        render {
            div {
                ul(id = listId) {
                    queryStore.renderEach(LocalPerson::_id) { p ->
                        li { p.data.map { it.name }.renderText() }
                    }
                }
            }
        }

        testList.forEach {
            queryStore.addOrUpdate(it)
            delay(1)
        }

        delay(250)

        val listAfterAdd = document.getElementById(listId)?.textContent
        assertEquals(testList.joinToString("") { it.name }, listAfterAdd, "wrong list after adding")

        val updatedTestList = testList.map { it.copy(name = "${it.name}2") }
        queryStore.updateMany(updatedTestList)
        delay(250)

        val listAfterUpdateMany = document.getElementById(listId)?.textContent
        assertEquals(updatedTestList.joinToString("") { it.name }, listAfterUpdateMany, "wrong list after update many")

        queryStore.addOrUpdate(updatedTestList[2].copy(name = "C3"))
        delay(250)
        val listAfterUpdate = document.getElementById(listId)?.textContent
        assertEquals(updatedTestList.map { if(it.name == "C2") it.copy(name = "C3") else it }.joinToString("") { it.name }, listAfterUpdate, "wrong list after update")
    }
}