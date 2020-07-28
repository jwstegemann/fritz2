package dev.fritz2.services.rest

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.action
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.buildLens
import dev.fritz2.services.serialization.Serializer
import dev.fritz2.test.getFreshCrudcrudEndpoint
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RestTests {
    data class Person(val name: String, val age: Int, val _id: String = "")

    val nameLens = buildLens("name", Person::name, { p, v -> p.copy(name = v) })
    val ageLens = buildLens("age", Person::age, { p, v -> p.copy(age = v) })
    val idLens = buildLens("id", Person::_id, { p, v -> p.copy(_id = v) })

    //TODO: Default Serializer
    object PersonSerializer : Serializer<Person, String> {
        data class PersonWithoutId(val name: String, val age: Int)

        private fun removeId(person: Person) = PersonWithoutId(person.name, person.age)

        override fun write(item: Person): String = JSON.stringify(removeId(item))
        override fun read(msg: String): Person = JSON.parse(msg)
        override fun writeList(items: List<Person>): String = JSON.stringify(items.map { removeId(it) })
        override fun readList(msg: String): List<Person> = JSON.parse(msg)
    }

    /**
     * See [crudcrud.com](https://crudcrud.com).
     */
    @Test
    fun testEntityService() = runTest {
        initDocument()

        val startPerson = Person("Heinz", 18, "")
        val changedAge = 99

        val personResource = RestResource(
            "",
            Person::_id,
            PersonSerializer,
            Person("", 0, ""),
            remote = getFreshCrudcrudEndpoint().append("/personx")
        )

        val entityStore = object : RootStore<Person>(personResource.emptyEntity) {
            private val rest = RestEntityService(personResource)

            val load = handle { entity, id: String -> rest.load(entity, id) }
            val saveOrUpdate = handleAndOffer<Unit> { entity -> rest.saveOrUpdate(this, entity) }

            //            val delete = handleAndOffer<Unit> { entity: Person , id: String -> rest.delete(this, entity, id)}
            val reset = handle { personResource.emptyEntity }

            val trigger = merge(saveOrUpdate) //, delete)
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

        delay(250)

        val idAfterSave = document.getElementById(idId)?.textContent
        assertTrue(idAfterSave?.length ?: 0 > 10, "no id after save")

        println("####### $idAfterSave \n")

        action(data = changedAge) handledBy ageSubStore.update

        delay(400)

//        action() handledBy entityStore.saveOrUpdate

//        delay(400)

        val ageAfterUpdate = document.getElementById(ageId)?.textContent
        assertEquals(changedAge.toString(), ageAfterUpdate, "wrong age after update")

        action(data = 0) handledBy ageSubStore.update
        action(idAfterSave.orEmpty()) handledBy entityStore.load

        delay(150)

        val ageAfterLoad = document.getElementById(ageId)?.textContent
        assertEquals("99", ageAfterLoad, "wrong age after load")

        action(idAfterSave.orEmpty()) handledBy entityStore.load

        delay(150)

        val idAfterDelete = document.getElementById(idId)?.textContent
        assertEquals(startPerson._id, idAfterDelete, "wrong id after delete")
    }

}