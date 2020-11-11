package dev.fritz2.binding

import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.buildLens
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import dev.fritz2.test.targetId
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail


class DetachedStoreTests {

    data class Person(val name: String, val id: String = uniqueId())

    private val nameLens = buildLens("name", Person::name) { p, v -> p.copy(name = v) }

    @Test
    fun testDetachStore() = runTest {
        initDocument()

        val person = Person("Foo")
        val store = object : RootStore<Person>(person) {
            override fun errorHandler(exception: Throwable, oldValue: Person): Person {
                fail(exception.message)
            }
        }

        val rootNameId = "name-${uniqueId()}"
        val nameId = "name-${uniqueId()}"
        val detachedId = "name-${uniqueId()}"
        val btnId = "btn-${uniqueId()}"

        render {
            section {

                div(id = rootNameId) {
                    store.data.map { it.name }.asText()
                }

                val nameSub = store.detach(nameLens, "")

                div {
                    div(id = nameId) { nameSub.data.asText() }
                    div(id = detachedId) { nameSub.detached.asText() }
                    button(id = btnId) {
                        clicks.map { "Foo Bar" } handledBy nameSub.update
                    }
                }
            }
        }.mount(targetId)

        delay(200)

        assertEquals(person.name, (document.getElementById(nameId) as HTMLDivElement).innerText, "name is not correct")

        val newPerson = Person("Bar")
        store.update(newPerson)

        delay(200)

        assertEquals(
            newPerson.name,
            (document.getElementById(rootNameId) as HTMLDivElement).innerText,
            "root name is not correct"
        )
        assertEquals(
            newPerson.name,
            (document.getElementById(nameId) as HTMLDivElement).innerText,
            "name is not correct"
        )
        assertEquals(
            "",
            (document.getElementById(detachedId) as HTMLDivElement).innerText,
            "detached name is not correct"
        )

        (document.getElementById(btnId) as HTMLButtonElement).click()
        delay(500)

        assertEquals(
            newPerson.name,
            (document.getElementById(rootNameId) as HTMLDivElement).innerText,
            "root name is not correct"
        )
        assertEquals(
            newPerson.name,
            (document.getElementById(nameId) as HTMLDivElement).innerText,
            "name is not correct"
        )
        assertEquals(
            "Foo Bar",
            (document.getElementById(detachedId) as HTMLDivElement).innerText,
            "detached name is not correct"
        )
    }

    @Test
    fun testDetachStoreOfList() = runTest {
        initDocument()

        val person = Person("Foo")
        val store = object : RootStore<List<Person>>(listOf(person)) {
            override fun errorHandler(exception: Throwable, oldValue: List<Person>): List<Person> {
                fail(exception.message)
            }

            val replace = handle<Person> { _, person ->
                listOf(person)
            }
        }

        val rootNameId = "name-${uniqueId()}"
        val nameId = "name-${uniqueId()}"
        val detachedId = "name-${uniqueId()}"
        val btnId = "btn-${uniqueId()}"

        render {
            section {
                div(id = rootNameId) {
                    store.data.map { it[0].name }.asText()
                }

                store.data.renderEach(Person::id) { person ->

                    val personStore = store.detach(person, Person::id)
                    val nameSub = personStore.sub(nameLens)

                    div {
                        div(id = nameId) { nameSub.data.asText() }
                        div(id = detachedId) { personStore.detached.map { it.name }.asText() }
                        button(id = btnId) {
                            clicks.map {
                                "Foo Bar"
                            } handledBy nameSub.update
                        }
                    }
                }
            }
        }.mount(targetId)

        delay(200)

        assertEquals(person.name, (document.getElementById(nameId) as HTMLDivElement).innerText, "name is not correct")

        val newPerson = Person("Bar")
        store.replace(newPerson)

        delay(200)

        assertEquals(
            newPerson.name,
            (document.getElementById(rootNameId) as HTMLDivElement).innerText,
            "root name is not correct"
        )
        assertEquals(
            newPerson.name,
            (document.getElementById(nameId) as HTMLDivElement).innerText,
            "name is not correct"
        )
        assertEquals(
            newPerson.name,
            (document.getElementById(detachedId) as HTMLDivElement).innerText,
            "detached name is not correct"
        )

        (document.getElementById(btnId) as HTMLButtonElement).click()
        delay(500)

        assertEquals(
            newPerson.name,
            (document.getElementById(rootNameId) as HTMLDivElement).innerText,
            "root name is not correct"
        )
        assertEquals(
            newPerson.name,
            (document.getElementById(nameId) as HTMLDivElement).innerText,
            "name is not correct"
        )
        assertEquals(
            "Foo Bar",
            (document.getElementById(detachedId) as HTMLDivElement).innerText,
            "detached name is not correct"
        )
    }

}