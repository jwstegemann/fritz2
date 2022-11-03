package dev.fritz2.examples.todomvc

import dev.fritz2.core.*
import dev.fritz2.routing.routerOf
import kotlinx.browser.localStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.w3c.dom.get

data class Filter(val text: String, val function: (List<ToDo>) -> List<ToDo>)

val filters = mapOf(
    "all" to Filter("All") { it },
    "active" to Filter("Active") { toDos -> toDos.filter { !it.completed } },
    "completed" to Filter("Completed") { toDos -> toDos.filter { it.completed } }
)

const val persistencePrefix = "todos"

val router = routerOf("all")

@ExperimentalStdlibApi
object ToDoListStore : RootStore<List<ToDo>>(emptyList(), id = persistencePrefix) {

    private val query = handle {
        buildList {
            for (index in 0 until localStorage.length) {
                val key = localStorage.key(index)
                if (key != null && key.startsWith(persistencePrefix)) {
                    add(ToDo.deserialize(localStorage[key]!!))
                }
            }
        }
    }

    val save = handle<ToDo> { toDos, new ->
        if (new.text.isNotBlank()) {
            localStorage.setItem("${persistencePrefix}.${new.id}", ToDo.serialize(new))
            var inList = false
            val updatedList = toDos.map {
                if (it.id == new.id) {
                    inList = true
                    new
                } else it
            }
            if (inList) updatedList else toDos + new
        } else delete(toDos, new.id)
    }

    val remove = handle<String> { toDos, id ->
        delete(toDos, id)
    }

    private fun delete(entities: List<ToDo>, id: String): List<ToDo> {
        localStorage.removeItem("${persistencePrefix}.$id")
        return entities.filterNot { it.id == id }
    }

    val toggleAll = handle { toDos, toggle: Boolean ->
        val toUpdate = toDos.mapNotNull {
            if (it.completed != toggle) it.copy(completed = toggle) else null
        }
        console.log(toUpdate.joinToString { it.toString() })

        val updated = (toDos + toUpdate).groupBy{ it.id }
            .filterValues { it.size > 1 }.mapValues { (id, entities) ->
                val entity = entities.last()
                localStorage.setItem(
                    "${persistencePrefix}.$id",
                    ToDo.serialize(entity)
                )
                entity
            }
        val result = toDos.map { updated[it.id] ?: it }
        console.log(result.joinToString { it.toString() })

        result
    }

    val clearCompleted = handle { toDos ->
        toDos.partition(ToDo::completed).let { (completed, active) ->
            console.info("delete: ${completed.joinToString()}")
            completed.map(ToDo::id).forEach {
                localStorage.removeItem("${persistencePrefix}.$it")
            }
            active
        }
    }

    val count = data.map { todos -> todos.count { !it.completed } }.distinctUntilChanged()
    val empty = data.map { it.isEmpty() }.distinctUntilChanged()
    val allChecked = data.map { todos -> todos.isNotEmpty() && todos.all { it.completed } }.distinctUntilChanged()

    init {
        query()
    }
}

fun RenderContext.filter(text: String, route: String) {
    li {
        a {
            className(router.data.map { if (it == route) "selected" else "" })
            href("#$route")
            +text
        }
    }
}

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
fun main() {

    fun RenderContext.inputHeader() {
        header {
            h1 { +"todos" }
            input("new-todo") {
                placeholder("What needs to be done?")
                autofocus(true)

                changes.values().map { domNode.value = ""; ToDo(text = it.trim()) } handledBy ToDoListStore.save
            }
        }
    }

    fun RenderContext.mainSection() {
        section("main") {
            input("toggle-all", id = "toggle-all") {
                type("checkbox")
                checked(ToDoListStore.allChecked)

                changes.states() handledBy ToDoListStore.toggleAll
            }
            label {
                `for`("toggle-all")
                +"Mark all as complete"
            }
            ul("todo-list") {
                ToDoListStore.data.combine(router.data) { all, route ->
                    filters[route]?.function?.invoke(all) ?: all
                }.renderEach(ToDo::id) { toDo ->
                    val toDoStore = ToDoListStore.sub(toDo, ToDo::id)
                    toDoStore.data.drop(1) handledBy ToDoListStore.save
                    val textStore = toDoStore.sub(ToDo.text())
                    val completedStore = toDoStore.sub(ToDo.completed())

                    val editingStore = storeOf(false)

                    li {
                        attr("data-id", toDoStore.id)
                        classMap(toDoStore.data.combine(editingStore.data) { toDo, isEditing ->
                            mapOf(
                                "completed" to toDo.completed,
                                "editing" to isEditing
                            )
                        })
                        div("view") {
                            input("toggle") {
                                type("checkbox")
                                checked(completedStore.data)

                                changes.states() handledBy completedStore.update
                            }
                            label {
                                textStore.data.renderText()

                                dblclicks.map { true } handledBy editingStore.update
                            }
                            button("destroy") {
                                clicks.map { toDo.id } handledBy ToDoListStore.remove
                            }
                        }
                        input("edit") {
                            value(textStore.data)
                            changes.values() handledBy textStore.update

                            editingStore.data handledBy { isEditing ->
                                if (isEditing) {
                                    domNode.focus()
                                    domNode.select()
                                }
                            }
                            merge(
                                blurs.map { false },
                                keyups.filter { shortcutOf(it) == Keys.Enter }.map { false }
                            ) handledBy editingStore.update
                        }
                    }
                }
            }
        }
    }

    fun RenderContext.appFooter() {
        footer("footer") {
            className(ToDoListStore.empty.map { if (it) "hidden" else "" })

            span("todo-count") {
                strong {
                    ToDoListStore.count.map {
                        "$it item${if (it != 1) "s" else ""} left"
                    }
                }
            }

            ul("filters") {
                filters.forEach { filter(it.value.text, it.key) }
            }
            button("clear-completed") {
                +"Clear completed"

                clicks handledBy ToDoListStore.clearCompleted
            }
        }
    }

    render("#todoapp") {
        inputHeader()
        mainSection()
        appFooter()
    }
}