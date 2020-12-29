package dev.fritz2.repositories.localstorage

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.invoke
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.buildLens
import dev.fritz2.repositories.ResourceNotFoundException
import dev.fritz2.resource.Resource

import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.test.*

class LocalStorageTests {
    data class LocalPerson(val name: String, val age: Int, val _id: String = uniqueId())

    private val nameLens = buildLens("name", LocalPerson::name) { p, v -> p.copy(name = v) }
    private val ageLens = buildLens("age", LocalPerson::age) { p, v -> p.copy(age = v) }
    private val idLens = buildLens("id", LocalPerson::_id) { p, v -> p.copy(_id = v) }


    object PersonResource : Resource<LocalPerson, String> {
        override val idProvider: IdProvider<LocalPerson, String> = LocalPerson::_id
        override fun serialize(item: LocalPerson): String = JSON.stringify(item)
        override fun deserialize(source: String): LocalPerson {
            val obj = JSON.parse<dynamic>(source)
            return LocalPerson(obj.name as String, obj.age as Int, obj._id as String)
        }

        override fun serializeList(items: List<LocalPerson>): String = JSON.stringify(items)
        override fun deserializeList(source: String): List<LocalPerson> {
            val list = JSON.parse<Array<dynamic>>(source)
            return list.map { obj -> LocalPerson(obj.name as String, obj.age as Int, obj._id as String) }
        }
    }

    @Test
    fun testEntityService() = runTest {
        initDocument()

        val defaultPerson = LocalPerson("", 0)
        val startPerson = LocalPerson("Heinz", 18)
        val changedAge = 99

        val entityStore = object : RootStore<LocalPerson>(defaultPerson) {
            override fun errorHandler(exception: Throwable, oldValue: LocalPerson): LocalPerson {
                fail(exception.message)
            }

            val localStorage = localStorageEntity(PersonResource, "")

            val load = handle { _, id: String -> localStorage.load(id) }

            val saveOrUpdate = handle { entity -> localStorage.addOrUpdate(entity) }
            val delete = handle { entity -> localStorage.delete(entity); defaultPerson }
        }

        assertFailsWith(ResourceNotFoundException::class) {
            entityStore.localStorage.load("unknown")
        }

        val nameId = "name-${uniqueId()}"
        val nameSubStore = entityStore.sub(nameLens)
        val ageId = "age-${uniqueId()}"
        val ageSubStore = entityStore.sub(ageLens)
        val idId = "id-${uniqueId()}"
        val idSubStore = entityStore.sub(idLens)


        render {
            div {
                div(id = idId) { idSubStore.data.asText() }
                div(id = nameId) { nameSubStore.data.asText() }
                div(id = ageId) { ageSubStore.data.asText() }
            }
        }.mount(targetId)

        entityStore.update(startPerson)
        delay(100)

        val nameAfterStart = document.getElementById(nameId)?.textContent
        assertEquals(startPerson.name, nameAfterStart, "no name after start")

        entityStore.saveOrUpdate()
        delay(200)

        val idAfterSave = document.getElementById(idId)?.textContent
        assertTrue(idAfterSave?.length ?: 0 > 10, "no id after save")

        ageSubStore.update(data = changedAge)
        entityStore.saveOrUpdate()
        delay(200)

        val ageAfterUpdate = document.getElementById(ageId)?.textContent
        assertEquals(changedAge.toString(), ageAfterUpdate, "wrong age after update")

        ageSubStore.update(data = 0)
        entityStore.load(idAfterSave.orEmpty())
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
            override fun errorHandler(exception: Throwable, oldValue: List<LocalPerson>): List<LocalPerson> {
                fail(exception.message)
            }

            private val localStorage =
                localStorageQuery(PersonResource, "") { entities, _: Unit ->
                    entities.sortedBy(LocalPerson::name)
                }
            val addOrUpdate = handle<LocalPerson> { entities, person -> localStorage.addOrUpdate(entities, person) }
            val query = handle<Unit> { _, query -> localStorage.query(query) }
            val delete = handle<String> { entities, id -> localStorage.delete(entities, id) }
        }

        val listId = "list-${uniqueId()}"
        val firstPersonId = "first-${uniqueId()}"

        render {
            div {
                ul(id = listId) {
                    queryStore.renderEach(LocalPerson::_id) { p ->
                        li { p.data.map { it.name }.asText() }
                    }
                }
                span(id = firstPersonId) {
                    queryStore.data.map {
                        if (it.isEmpty()) ""
                        else it.first()._id
                    }.asText()
                }
            }
        }.mount(targetId)

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
        assertTrue(firstId != null && firstId.length > 10)

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
            override fun errorHandler(exception: Throwable, oldValue: List<LocalPerson>): List<LocalPerson> {
                fail(exception.message)
            }

            private val localStorage: LocalStorageQuery<LocalPerson, String, Unit> = localStorageQuery(PersonResource, "")

            val addOrUpdate = handle<LocalPerson> { entities, entity -> localStorage.addOrUpdate(entities, entity) }
            val updateMany = handle<List<LocalPerson>> { entities, updatedEntities -> localStorage.updateMany(entities, updatedEntities) }
        }

        val listId = "list-${uniqueId()}"

        render {
            div {
                ul(id = listId) {
                    queryStore.renderEach(LocalPerson::_id) { p ->
                        li { p.data.map { it.name }.asText() }
                    }
                }
            }
        }.mount(targetId)

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