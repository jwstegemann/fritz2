package io.fritz2.examples.todomvc

import io.fritz2.binding.*
import io.fritz2.dom.append
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.Keys
import io.fritz2.dom.html.html
import io.fritz2.dom.key
import io.fritz2.dom.states
import io.fritz2.dom.values
import io.fritz2.optics.WithId
import io.fritz2.optics.buildLens
import io.fritz2.routing.router
import io.fritz2.utils.createUUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

data class ToDo(
    val text: String,
    val completed: Boolean = false,
    val editing: Boolean = false,
    override val id: String = createUUID()
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
            if (text.isNotEmpty()) toDos + ToDo(text)
            else toDos
        }

        val remove = handle<String> { toDos, id ->
            toDos.filterNot { it.id == id }
        }

        val toggleAll = handle<Boolean> { toDos, toggle ->
            toDos.map { it.copy(completed = toggle) }
        }

        val clearCompleted = handle { toDos ->
            toDos.filterNot { it.completed }
        }

        val count = data.map { todos -> todos.count { !it.completed } }.distinctUntilChanged()
        val allChecked = data.map { todos -> todos.isNotEmpty() && todos.all { it.completed }}.distinctUntilChanged()
    }

    val inputHeader = html {
        header {
            h1 { text("todos") }
            input("new-todo") {
                placeholder = const("What needs to be done?")
                autofocus = const(true)

                toDos.add <= changes.values().onEach { domNode.value = "" }
            }
        }
    }

    val mainSection = html {
        section("main") {
            input("toggle-all", id = "toggle-all") {
                type = const("checkbox")
                checked = toDos.allChecked

                toDos.toggleAll <= changes.states()
            }
            label(`for` = "toggle-all") {
                text("Mark all as complete")
            }
            ul("todo-list") {
                toDos.data.flatMapLatest { all ->
                    router.routes.map { route ->
                        filters[route]?.function?.invoke(all) ?: all
                    }
                }.each().map { toDo ->
                    val toDoStore = toDos.sub(toDo)
                    val textStore = toDoStore.sub(textLens)
                    val completedStore = toDoStore.sub(completedLens)
                    val editingStore = toDoStore.sub(editingLens)

                    html {
                        li {
                            attr("data-id", toDoStore.id)
                            //TODO: better flatmap over editing and completed
                            classMap = toDoStore.data.map { mapOf(
                                    "completed" to it.completed,
                                    "editing" to it.editing
                            )}
                            div("view") {
                                input("toggle") {
                                    type = const("checkbox")
                                    checked = completedStore.data

                                    completedStore.update <= changes.states()
                                }
                                label {
                                    textStore.data.bind()

                                    editingStore.update <= dblclicks.map { true }
                                }
                                button("destroy") {
                                    toDos.remove <= clicks.events.map { toDo.id } //flatMapLatest { toDoStore.data }
                                }
                            }
                            input("edit") {
                                value = textStore.data
                                textStore.update <= changes.values()

                                editingStore.data.map { isEditing ->
                                    if (isEditing) domNode.apply {
                                        focus()
                                        select()
                                    }
                                    isEditing.toString()
                                }.watch()
                                editingStore.update <= merge(
                                    blurs.map { false },
                                    keyups.key().filter { it.isKey(Keys.Enter) }.map { false }
                                )
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
                href = const("#$route")
                text(text)
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
                text("Clear completed")

                toDos.clearCompleted <= clicks
            }
        }
    }

    append("todoapp", inputHeader, mainSection, appFooter)
}