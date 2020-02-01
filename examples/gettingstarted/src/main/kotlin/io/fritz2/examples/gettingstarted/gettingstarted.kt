package io.fritz2.examples.gettingstarted

import io.fritz2.binding.RootStore
import io.fritz2.binding.each
import io.fritz2.binding.mapItems
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
        val addADot = Handler<ActionData> { model, _ ->
            "$model."
        }
    }

    val seq = object : RootStore<List<String>>(listOf("one", "two", "three")) {
        var count = 0

        val addItem = Handler<Any> { list, _ ->
            count++
            list + "yet another item no. $count"
        }
        val deleteItem = Handler<String> { list, current ->
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
            button {
                +"add one more little dot"
                store.addADot <= clicks.map {
                    ActionData(it.clientX, it.clientY)
                }
            }
            ul {
                seq.each().mapItems { s ->
                    html {
                        li {
                            button {
                                +s
                                seq.deleteItem <= clicks.map { console.log("deleting $s"); s }
                            }
                        }
                    }
                }.bind()
            }
            button {
                +"add an item"
                seq.addItem <= clicks
            }
        }
    }

    myComponent.mount("target")
}