package io.fritz2.examples.todomvc

import io.fritz2.binding.*
import io.fritz2.dom.append
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.html
import io.fritz2.dom.states
import io.fritz2.dom.values
import io.fritz2.optics.WithId
import io.fritz2.optics.buildLens
import io.fritz2.routing.router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

data class ToDo(
    val text: String,
    val completed: Boolean = false,
    val editing: Boolean = false,
    override val id: String = text.hashCode().toString()
) : WithId


val textLens = buildLens<ToDo, String>("text", {it.text}, {p,v -> p.copy(text = v)})
val completedLens = buildLens<ToDo, Boolean>("completed", {it.completed}, {p,v -> p.copy(completed = v)})
val editingLens = buildLens<ToDo, Boolean>("editing", {it.editing}, {p,v -> p.copy(editing = v)})


data class Filter(val text: String, val function: (List<ToDo>) -> List<ToDo>)

val filters = mapOf (
    "/" to Filter("All") { it },
    "/active" to Filter("Active") { toDos -> toDos.filter { !it.completed } },
    "/completed" to Filter("Completed") { toDos -> toDos.filter { it.completed } }
)


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {
    val router = router("/")

    val toDos = object : RootStore<List<ToDo>>(emptyList()) {
        val add = handle<String> { toDos, text ->
            toDos + ToDo(text)
        }

        val remove = handle<ToDo> {
                toDos, item -> toDos - item
        }

        val toggleAll = handle<Boolean> {toDos, toggle ->
            toDos.map { it.copy(completed = toggle)}
        }

        val clearCompleted = handle {toDos ->
            toDos.filter { !it.completed }
        }

        val count = data.map { todos -> todos.count { !it.completed } }.distinctUntilChanged()
        val allChecked = data.map { todos -> todos.all { it.completed }}
    }

    val inputHeader = html {
        header {
            h1 { +"todos" }
            input("new-todo") {
                placeholder = !"What needs to be done?"
                autofocus = Const(true)

                toDos.add <= changes.values().onEach { domNode.value = "" }
            }
        }
    }

    val mainSection = html {
        section("main") {
            input("toggle-all") {
                type = !"checkbox"
                checked = toDos.allChecked

                toDos.toggleAll <= changes.states()
            }
            label {
                `for` = !"toggle-all"
                +"Mark all as complete"
            }
            ul("todo-list") {
                toDos.data.flatMapLatest { all ->
                    router.routes.map { route -> filters[route]?.function?.invoke(all) ?: all }
                }.each().map { toDo ->
                    val toDoStore = toDos.sub(toDo)
                    val textStore = toDoStore.sub(textLens)
                    val completedStore = toDoStore.sub(completedLens)
                    val editingStore = toDoStore.sub(editingLens)

                    html {
                        li {
                            //TODO: better flatmap over editing and completed
                            classMap = toDoStore.data.map { mapOf(
                                    "completed" to it.completed,
                                    "editing" to it.editing
                            )}
                            div("view") {
                                input("toggle") {
                                    type = !"checkbox"
                                    checked = completedStore.data

                                    completedStore.update <= changes.states()
                                }
                                label {
                                    textStore.data.bind()

                                    editingStore.update <= dblclicks.map { true }
                                }
                                button("destroy") {
                                    toDos.remove <= clicks.events.map{ toDo } //flatMapLatest { toDoStore.data }
                                }
                            }
                            input("edit") {
                                value = textStore.data
                                textStore.update <= changes.values()

                                editingStore.update <= blurs.map { false }
                            }
                        }
                    }
                }.bind()
            }
        }
    }

    fun HtmlElements.filter(text: String, route: String) {
        li {
            a {
                className = router.routes.map {if (it == route) "selected" else ""}
                href = !"#$route"
                +text
            }
        }
    }

    val appFooter = html {
        footer("footer") {
            span("todo-count") {
                strong {
                    toDos.count.map {
                        "$it item${if (it != 1) "s" else ""} left"
                    }.bind()
                }
            }

            ul("filters") {
                filters.forEach { filter(it.value.text, it.key) }
            }
            button("clear-completed") {
                +"Clear completed"

                toDos.clearCompleted <= clicks
            }
        }
    }

    append("todoapp", inputHeader, mainSection, appFooter)
}