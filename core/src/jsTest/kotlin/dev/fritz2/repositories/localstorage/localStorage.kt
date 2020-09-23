package dev.fritz2.repositories.localstorage

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.action
import dev.fritz2.binding.each
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.buildLens
import dev.fritz2.repositories.Resource
import dev.fritz2.serialization.Serializer
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class LocalStorageTests {
    data class LocalPerson(val name: String, val age: Int, val _id: String = uniqueId())

    private val nameLens = buildLens("name", LocalPerson::name) { p, v -> p.copy(name = v) }
    private val ageLens = buildLens("age", LocalPerson::age) { p, v -> p.copy(age = v) }
    private val idLens = buildLens("id", LocalPerson::_id) { p, v -> p.copy(_id = v) }


    object PersonSerializer : Serializer<LocalPerson, String> {
        override fun write(item: LocalPerson): String = JSON.stringify(item)
        override fun read(msg: String): LocalPerson {
            val obj = JSON.parse<dynamic>(msg)
            return LocalPerson(obj.name as String, obj.age as Int, obj._id as String)
        }

        override fun writeList(items: List<LocalPerson>): String = JSON.stringify(items)
        override fun readList(msg: String): List<LocalPerson> {
            val list = JSON.parse<Array<dynamic>>(msg)
            return list.map { obj -> LocalPerson(obj.name as String, obj.age as Int, obj._id as String) }
        }
    }

    @Test
    fun testEntityService() = runTest {
        initDocument()

        val startPerson = LocalPerson("Heinz", 18)
        val changedAge = 99

        val personResource = Resource(
            LocalPerson::_id,
            PersonSerializer,
            LocalPerson("", 0)
        )

        val entityStore = object : RootStore<LocalPerson>(personResource.emptyEntity) {
            override fun errorHandler(exception: Throwable, oldValue: LocalPerson): LocalPerson {
                fail(exception.message)
            }

            private val localStorage = localStorageEntity(personResource, "")

            val load = handle { entity, id: String -> localStorage.load(entity, id) }

            val saveOrUpdate = handle { entity -> localStorage.saveOrUpdate(entity) }
            val delete = handle { entity -> localStorage.delete(entity) }
        }

        val nameId = "name-${uniqueId()}"
        val nameSubStore = entityStore.sub(nameLens)
        val ageId = "age-${uniqueId()}"
        val ageSubStore = entityStore.sub(ageLens)
        val idId = "id-${uniqueId()}"
        val idSubStore = entityStore.sub(idLens)


        render {
            div {
                div(id = idId) { idSubStore.data.bind() }
                div(id = nameId) { nameSubStore.data.bind() }
                div(id = ageId) { ageSubStore.data.map { it.toString() }.bind() }
            }
        }.mount(targetId)

        action(startPerson) handledBy entityStore.update
        delay(100)

        val nameAfterStart = document.getElementById(nameId)?.textContent
        assertEquals(startPerson.name, nameAfterStart, "no name after start")

        action() handledBy entityStore.saveOrUpdate
        delay(200)

        val idAfterSave = document.getElementById(idId)?.textContent
        assertTrue(idAfterSave?.length ?: 0 > 10, "no id after save")

        action(data = changedAge) handledBy ageSubStore.update
        action() handledBy entityStore.saveOrUpdate
        delay(200)

        val ageAfterUpdate = document.getElementById(ageId)?.textContent
        assertEquals(changedAge.toString(), ageAfterUpdate, "wrong age after update")

        action(data = 0) handledBy ageSubStore.update
        action(idAfterSave.orEmpty()) handledBy entityStore.load
        delay(200)

        val ageAfterLoad = document.getElementById(ageId)?.textContent
        assertEquals("99", ageAfterLoad, "wrong age after load")

        action() handledBy entityStore.delete
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

        val personResource = Resource(
            LocalPerson::_id,
            PersonSerializer,
            LocalPerson("", 0)
        )

        val queryStore = object : RootStore<List<LocalPerson>>(emptyList()) {
            override fun errorHandler(exception: Throwable, oldValue: List<LocalPerson>): List<LocalPerson> {
                fail(exception.message)
            }

            private val localStorage =
                localStorageQuery(personResource, "") { entities, _: Unit ->
                    entities.sortedBy(LocalPerson::name)
                }
            val addOrUpdate = handle<LocalPerson> { entities, person -> localStorage.addOrUpdate(entities, person) }
            val query = handle<Unit> { entities, query -> localStorage.query(entities, query) }
            val delete = handle<String> { entites, id -> localStorage.delete(entites, id) }
        }

        val listId = "list-${uniqueId()}"
        val firstPersonId = "first-${uniqueId()}"

        render {
            div {
                ul(id = listId) {
                    queryStore.each(LocalPerson::_id).render { p ->
                        li { p.data.map { it.name }.bind() }
                    }.bind()
                }
                span(id = firstPersonId) {
                    queryStore.data.map {
                        if (it.isEmpty()) ""
                        else it.first()._id
                    }.bind()
                }
            }
        }.mount(targetId)

        delay(100)

        testList.forEach {
            action(it) handledBy queryStore.addOrUpdate
            delay(1)
        }

        delay(250)

        action() handledBy queryStore.query
        delay(250)

        val listAfterQuery = document.getElementById(listId)?.textContent
        assertEquals(testList.joinToString("") { it.name }, listAfterQuery, "wrong list after query")

        val firstId = document.getElementById(firstPersonId)?.textContent
        assertTrue(firstId != null && firstId.length > 10)

        action(firstId) handledBy queryStore.delete
        delay(250)

        val listAfterDelete = document.getElementById(listId)?.textContent
        assertEquals(testList.drop(1).joinToString("") { it.name }, listAfterDelete, "wrong list after query")

        action(emptyList<LocalPerson>()) handledBy queryStore.update
        delay(1)
        action() handledBy queryStore.query
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

        val personResource = Resource(
            LocalPerson::_id,
            PersonSerializer,
            LocalPerson("", 0)
        )

        val queryStore = object : RootStore<List<LocalPerson>>(emptyList()) {
            override fun errorHandler(exception: Throwable, oldValue: List<LocalPerson>): List<LocalPerson> {
                fail(exception.message)
            }

            private val localStorage: LocalStorageQuery<LocalPerson, String, Unit> = localStorageQuery(personResource, "")

            val addOrUpdate = handle<LocalPerson> { entities, entity -> localStorage.addOrUpdate(entities, entity) }
            val updateMany = handle<List<LocalPerson>> { entities, updatedEntities -> localStorage.updateMany(entities, updatedEntities) }
        }

        val listId = "list-${uniqueId()}"

        render {
            div {
                ul(id = listId) {
                    queryStore.each(LocalPerson::_id).render { p ->
                        li { p.data.map { it.name }.bind() }
                    }.bind()
                }
            }
        }.mount(targetId)

        testList.forEach {
            action(it) handledBy queryStore.addOrUpdate
            delay(1)
        }

        delay(250)

        val listAfterAdd = document.getElementById(listId)?.textContent
        assertEquals(testList.joinToString("") { it.name }, listAfterAdd, "wrong list after adding")

        val updatedTestList = testList.map { it.copy(name = "${it.name}2") }
        action(updatedTestList) handledBy queryStore.updateMany
        delay(250)

        val listAfterUpdateMany = document.getElementById(listId)?.textContent
        assertEquals(updatedTestList.joinToString("") { it.name }, listAfterUpdateMany, "wrong list after update many")

        action(updatedTestList[2].copy(name = "C3")) handledBy queryStore.addOrUpdate
        delay(250)
        val listAfterUpdate = document.getElementById(listId)?.textContent
        assertEquals(updatedTestList.map { if(it.name == "C2") it.copy(name = "C3") else it }.joinToString("") { it.name }, listAfterUpdate, "wrong list after update")
    }
}