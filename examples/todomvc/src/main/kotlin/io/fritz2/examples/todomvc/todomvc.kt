package io.fritz2.examples.todomvc

import io.fritz2.binding.Const
import io.fritz2.binding.RootStore
import io.fritz2.binding.each
import io.fritz2.binding.eachStore
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.dom.states
import io.fritz2.dom.values
import io.fritz2.optics.WithId
import io.fritz2.optics.buildLens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

data class ToDo(
    val text: String,
    val completed: Boolean,
    override val id: String = text.hashCode().toString()
) : WithId

val textLens = buildLens<ToDo, String>("text", {it.text}, {p,v -> p.copy(text = v)})
val completedLens = buildLens<ToDo, Boolean>("completed", {it.completed}, {p,v -> p.copy(completed = v)})


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val toDos = object : RootStore<List<ToDo>>(emptyList()) {
        val add = handle<String> { toDos, text ->
            toDos + ToDo(text, false)
        }

        val remove = handle<ToDo> {toDos, item ->
            toDos.minus(item)
        }

        val toggleAll = handle<Boolean> {toDos, toggle ->
            toDos.map { it.copy(completed = toggle)}
        }

        val count = data.map { it.count { !it.completed } }
    }

    fun HtmlElements.inputHeader() {
        header {
            h1 { +"todos" }
            input {
                className = !"new-todo"
                placeholder = !"What needs to be done?"
                autofocus = Const(true)

                toDos.add <= changes.values().onEach {
                    console.log("adding $it")
                    domNode.value = ""
                }
            }
        }
    }

    fun HtmlElements.mainSection() {
        section {
            className = !"main"
            input("toggle-all") {
                className = !"toggle-all"
                type = !"checkbox"
                toDos.toggleAll <= changes.states()
                //TODO: set if all items are completed
            }
            label {
                `for` = !"toggle-all"
                +"Mark all as complete"
            }
            ul {
                className = !"todo-list"
                toDos.eachStore().map { toDoStore ->
                    val textStore = toDoStore.sub(textLens)
                    val completedStore = toDoStore.sub(completedLens)
                    html {
                        li {
                            className = completedStore.data.map { if(it) "completed" else "" }
                            div {
                                className = !"view"
                                input {
                                    className = !"toggle"
                                    type = !"checkbox"
                                    defaultChecked = completedStore.data
                                    completedStore.update <= changes.states()
                                }
                                label { textStore.data.bind() }
                                button {
                                    className = !"destroy"
                                    toDos.remove <= clicks.events.flatMapLatest { toDoStore.data }
                                }
                            }
                            // <input class="edit" value="Create a TodoMVC template">
                        }
                    }
                }.bind()
            }
        }
    }

    fun HtmlElements.appFooter() {
        footer {
            className = !"footer"
            span {
                className = !"todo-count"
                strong {
                    toDos.count.map {
                        val plural = if (it != 1) "s" else ""
                        "$it item$plural left"
                    }.bind()
                }
            }
        }
        /*
        <footer class="footer">
        <!-- This should be `0 items left` by default -->
        <span class="todo-count"><strong>0</strong> item left</span>
        <!-- Remove this if you don't implement routing -->
        <ul class="filters">
        <li>
        <a class="selected" href="#/">All</a>
        </li>
        <li>
        <a href="#/active">Active</a>
        </li>
        <li>
        <a href="#/completed">Completed</a>
        </li>
        </ul>
        <!-- Hidden if no completed items are left ↓ -->
        <button class="clear-completed">Clear completed</button>
        </footer>
         */
    }

    val app = html {
        section {
            inputHeader()
            mainSection()
            appFooter()
        }
    }

    app.mount("todoapp")
}