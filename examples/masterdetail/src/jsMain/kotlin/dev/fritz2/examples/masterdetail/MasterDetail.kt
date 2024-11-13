package dev.fritz2.examples.masterdetail

import dev.fritz2.core.*
import dev.fritz2.history.history
import dev.fritz2.tracking.tracker
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.get

val numberFormat = lensOf(Int::toString, String::toInt)

const val personPrefix = "dev.fritz2.examples.masterdetail.person"

object MasterStore : RootStore<List<Person>>(emptyList(), job = Job()) {

    val query = handle {
        buildList {
            for (index in 0 until window.localStorage.length) {
                val key = window.localStorage.key(index)
                if (key != null && key.startsWith(personPrefix)) {
                    add(Person.deserialize(window.localStorage[key]!!))
                }
            }
        }
    }

    val delete = handle<String> { persons, id ->
        window.localStorage.removeItem("${personPrefix}.$id")
        persons.filterNot { it.id == id }
    }

    init {
        query()
    }
}

object DetailStore : RootStore<Person>(Person(), job = Job()) {

    val running = tracker()
    val history = history<Person>(10, synced = true)

    val load = handle<String> { _, id ->
        history.clear()
        Person.deserialize(
            window.localStorage["${personPrefix}.$id"]
                ?: throw NoSuchElementException("person with id ($id) does not exist")
        )
    }

    val addOrUpdate = handle { person ->
        running.track() {
            delay(1500)
            person.copy(saved = true).also { dirtyPerson ->
                window.localStorage.setItem("${personPrefix}.${dirtyPerson.id}", Person.serialize(dirtyPerson))
            }.also { MasterStore.query() }
        }
    }

    val delete = handle { person ->
        history.clear()
        window.localStorage.removeItem("${personPrefix}.${person.id}")
            .also { MasterStore.query() }
        Person()
    }

    val reset = handle {
        history.clear()
        Person()
    }

    val undo = handle {
        history.back()
    }

    val isSaved = data.map { it.saved }
}

/*
 * List-View
 */
fun RenderContext.table() {
    div("col-12") {
        div("card") {
            h5("card-header") { +"List of Persons" }
            div("card-body") {
                table("table table-striped") {
                    thead("thead-dark") {
                        tr {
                            th { +"#" }
                            th { +"name" }
                            th { +"age" }
                            th { +"salary" }
                            th { +"" }
                        }
                    }
                    tbody {
                        MasterStore.data.renderEach { p ->
                            tr {
                                td { +"...${p.id.takeLast(5)}" }
                                td { +p.name }
                                td { +p.age.toString() }
                                td { +p.salary.toString() }
                                td {
                                    button("btn btn-primary") {
                                        +"Edit"
                                        clicks.map { p.id } handledBy DetailStore.load
                                    }
                                    button("btn btn-danger ml-2") {
                                        className(DetailStore.data.map { if (it.id == p.id) "d-none" else "" })
                                        +"Delete"
                                        clicks.map { p.id } handledBy MasterStore.delete
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/*
 * Details-View
 */
fun RenderContext.details() {
    val visibleWhenSaved = DetailStore.isSaved.map { if (it) "" else "d-none" }

    div("col-12") {
        div("card") {
            h5("card-header") {
                DetailStore.data.map {
                    "Persons Details (...${it.id.takeLast(5)})"
                }.renderText()
            }
            div("card-body") {
                div {
                    formGroup("name", DetailStore.map(Person.name()))
                    formGroup("age", DetailStore.map(Person.age() + numberFormat), inputType = "number")
                    formGroup("salary", DetailStore.map(Person.salary() + numberFormat), inputType = "number")
                }
            }
            div("card-footer") {
                button("btn btn-success") {
                    span {
                        className(DetailStore.running.data.map {
                            if (it) "spinner-border spinner-border-sm mr-2" else ""
                        })
                    }
                    DetailStore.isSaved.map { if (it) "Save" else "Add" }.renderText()

                    clicks handledBy DetailStore.addOrUpdate
                }
                button("btn btn-danger ml-2") {
                    className(visibleWhenSaved)
                    +"Delete"
                    clicks handledBy DetailStore.delete
                }
                button("btn btn-warning ml-2") {
                    className(DetailStore.history.data.combine(DetailStore.data) { history, value ->
                        history.isNotEmpty() && history.first() != value
                    }.map { if (it) "" else "d-none" })
                    +"Undo"
                    clicks handledBy DetailStore.undo
                }
                button("btn btn-info ml-2") {
                    className(visibleWhenSaved)
                    +"Close"
                    clicks handledBy DetailStore.reset
                }
                button("btn btn-secondary mx-2") {
                    +"Show data"
                    attr("data-toggle", "collapse")
                    attr("data-target", "#showData")
                }
                div("collapse mt-2", id = "showData") {
                    div("card card-body") {
                        pre {
                            code {
                                DetailStore.data.map { JSON.stringify(it, space = 2) }.renderText()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RenderContext.formGroup(
    label: String,
    store: Store<String>,
    inputType: String = "text",
    cssClassName: Flow<String>? = null,
    handleChanges: HtmlTag<HTMLInputElement>.(Store<String>) -> Unit = {
        changes.values() handledBy store.update
    }
) {
    div("form-group") {
        cssClassName?.apply { className(this) }

        label {
            `for`(store.id)
            +label
        }
        input("form-control", id = store.id) {
            type(inputType)
            value(store.data)
            handleChanges(store)
        }
    }
}

fun main() {
    render("#target") {
        section {
            div("row") {
                details()
            }
            div("row mt-2") {
                table()
            }
        }
    }
}