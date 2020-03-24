package io.fritz2.examples.todomvc

import io.fritz2.binding.*
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
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


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {
    val router = router("/")

    val toDos = object : RootStore<List<ToDo>>(emptyList()) {
        val add = handle<String> { toDos, text ->
            toDos.plus(ToDo(text))
        }

        val remove = handle<ToDo> {
                toDos, item -> toDos.minus(item)
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

    fun HtmlElements.inputHeader() {
        header {
            h1 { +"todos" }
            input("new-todo") {
                placeholder = !"What needs to be done?"
                autofocus = Const(true)

                toDos.add <= changes.values().onEach { domNode.value = "" }
            }
        }
    }

    fun HtmlElements.mainSection() {
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
                    router.routes.map { route ->
                        console.log("setting filter to $route")
                        when(route) {
                            "/completed" -> all.filter { it.completed }
                            "/active" -> all.filter { !it.completed }
                            else -> all
                        }
                    }
                }.each().map { toDo ->
                    val toDoStore = toDos.sub(toDo)
                    val textStore = toDoStore.sub(textLens)
                    val completedStore = toDoStore.sub(completedLens)
                    val editingStore = toDoStore.sub(editingLens)

                    html {
                        li {
                            className = toDoStore.data.map {
                                //TODO: allow map of className -> boolean
                                if (it.completed) "completed" else " "
                                    .plus(if (it.editing) "editing" else "")
                            }
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

    fun HtmlElements.appFooter() {
        footer("footer") {
            span("todo-count") {
                strong {
                    toDos.count.map {
                        val plural = if (it != 1) "s" else ""
                        "$it item$plural left"
                    }.bind()
                }
            }

            ul("filters") {
                //TODO: Make map and use in routing
                filter("All","/")
                filter("Active","/active")
                filter("Completed", "/completed")
            }
            button("clear-completed") {
                +"Clear completed"

                toDos.clearCompleted <= clicks
            }
        }
    }

    val app = html {
        //TODO: allow to mount three in a row...
        section {
            inputHeader()
            mainSection()
            appFooter()
        }
    }

    app.mount("todoapp")
}