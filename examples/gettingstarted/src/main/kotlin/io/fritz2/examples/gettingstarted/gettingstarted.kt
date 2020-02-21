package io.fritz2.examples.gettingstarted

import io.fritz2.binding.RootStore
import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map


data class ActionData(val x: Int, val y: Int)

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("start") {
        val addADot = handle<Any> { model, _ ->
            "$model."
        }
    }

    val classStore = object : RootStore<List<String>>(listOf("btn", "items")) {
        val add = handle<String> { list, new ->
            list + new
        }
        val remove = handle<String> { list, del ->
            list.minus(del)
        }
    }

    val seq = object : RootStore<List<String>>(listOf("one", "two", "three")) {
        var count = 0

        val addItem = handle<Any> { list, _ ->
            count++
            list + "yet another item no. $count"
        }
        val deleteItem = handle<String> { list, current ->
            list.minus(current)
        }
    }

    val myComponent = html {
        section {
            input {
                value = store.data
                store.update <= changes
            }
            div {
                +"value: "
                store.data.bind()
            }
            div {
                +"value: "
                store.data.bind()
            }
            div {
                +"value: "
                store.data.bind()
            }
            div {
                +"value: "
                store.data.bind()
            }
            div {
                +"value: "
                store.data.bind()
            }
            button {
                +"add one more little dot"
                store.addADot <= clicks
            }
            ul {
                seq.data.each().map { s ->
                    html {
                        li {
                            button {
                                +s
                                id = !"delete-btn"
                                `class` = !"btn"
                                seq.deleteItem <= clicks.map { console.log("deleting $s"); s }
                                classStore.remove <= clicks.map { e ->
                                    "newItem"
                                }
                            }
                        }
                    }
                }.bind()
            }
            button {
                +"add an item"
                seq.addItem <= clicks
                classStore.add <= clicks.map { e ->
                    "newItem"
                }
                attributeData("test", "test-button1")
                id = !"button"
                classes = classStore.data
            }
        }
    }

    myComponent.mount("target")
}