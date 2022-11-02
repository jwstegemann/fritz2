package dev.fritz2.examples.repositories

import dev.fritz2.core.*
import dev.fritz2.history.history
import dev.fritz2.repository.localstorage.localStorageEntityOf
import dev.fritz2.repository.localstorage.localStorageQueryOf
import dev.fritz2.tracking.tracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.w3c.dom.HTMLInputElement

val numberFormat = format({ it.toInt() }, { it.toString() })

const val personPrefix = "dev.fritz2.examples.person"

object EntityStore : RootStore<Person>(Person()) {

    val running = tracker()
    val history = history<Person>(10).sync(this)

    private val localStorage = localStorageEntityOf(PersonResource, personPrefix)

    val load = handle<String> { _, id ->
        history.reset()
        localStorage.load(id)
    }

    val addOrUpdate = handleAndEmit<Unit> { person ->
        running.track("myTransaction") {
            delay(1500)
            localStorage.addOrUpdate(person.copy(saved = true))
                .also { emit(Unit) }
        }
    }

    val delete = handleAndEmit<Unit> { person ->
        history.reset()
        localStorage.delete(person).also { emit(Unit) }
        Person()
    }

    val reset = handle {
        history.reset()
        Person()
    }

    val trigger = merge(addOrUpdate, delete)

    val undo = handle {
        history.back()
    }

    val isSaved = data.map { it.saved }
}

object QueryStore : RootStore<List<Person>>(emptyList()) {
    private val localStorage = localStorageQueryOf<Person, String, Unit>(PersonResource, personPrefix)

    private val query = handle { localStorage.query(Unit) }
    val delete = handle<String> { list, id ->
        localStorage.delete(list, id)
    }

    init {
        EntityStore.trigger handledBy query
        query()
    }
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
                        QueryStore.data.renderEach { p ->
                            tr {
                                td { +"...${p._id.takeLast(5)}" }
                                td { +p.name }
                                td { +p.age.toString() }
                                td { +p.salary.toString() }
                                td {
                                    button("btn btn-primary") {
                                        +"Edit"
                                        clicks.map { p._id } handledBy EntityStore.load
                                    }
                                    button("btn btn-danger ml-2") {
                                        className(EntityStore.data.map { if (it._id == p._id) "d-none" else "" })
                                        +"Delete"
                                        clicks.map { p._id } handledBy QueryStore.delete
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
    val visibleWhenSaved = EntityStore.isSaved.map { if (it) "" else "d-none" }

    div("col-12") {
        div("card") {
            h5("card-header") { EntityStore.data.map {
                "Persons Details (...${it._id.takeLast(5)})"
            }.renderText()
            }
            div("card-body") {
                div {
                    formGroup("name", EntityStore.sub(Person.name()))
                    formGroup("age", EntityStore.sub(Person.age() + numberFormat), inputType = "number")
                    formGroup("salary", EntityStore.sub(Person.salary() + numberFormat), inputType = "number")
                }
            }
            div("card-footer") {
                button("btn btn-success") {
                    span {
                        className(EntityStore.running.data.map {
                            if(it) "spinner-border spinner-border-sm mr-2" else ""
                        })
                    }
                    EntityStore.isSaved.map { if (it) "Save" else "Add" }.renderText()

                    clicks handledBy EntityStore.addOrUpdate
                }
                button("btn btn-danger ml-2") {
                    className(visibleWhenSaved)
                    +"Delete"
                    clicks handledBy EntityStore.delete
                }
                button("btn btn-warning ml-2") {
                    className(EntityStore.history.data.combine(EntityStore.data) { history, value ->
                        history.isNotEmpty() && history.first() != value
                    }.map { if (it) "" else "d-none" })
                    +"Undo"
                    clicks handledBy EntityStore.undo
                }
                button("btn btn-info ml-2") {
                    className(visibleWhenSaved)
                    +"Close"
                    clicks handledBy EntityStore.reset
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
                                EntityStore.data.map { JSON.stringify(it, space = 2) }.renderText()
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