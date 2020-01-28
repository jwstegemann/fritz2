package io.fritz2.examples.gettingstarted

import io.fritz2.binding.Store
import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import io.fritz2.binding.map


data class ActionData(val x: Int, val y: Int)

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : Store<String>("start") {
        val addADot = Handler<ActionData> { model, _ ->
            "$model."
        }
    }

    val seq = object : Store<List<String>>(listOf("one", "two", "three")) {
        val addItem = Handler<Any> { list, _ ->
            list + "yet another item"
        }
    }

    val myComponent = html {
        section {
            input {
                value = store.data
                onchange >= store.update
            }
            div {
                +"value: "
                store.data.bind()
            }
            button {
                +"add one more little dot"
                onclick.map {
                    ActionData(it.clientX, it.clientY)
                } >= store.addADot
            }
            ul {
                seq.each().map { s: String ->
                    html {
                        li {
                            +s
                        }
                    }
                }.bind()
            }
            button {
                +"add an item"
                onclick >= seq.addItem
            }
        }
    }

    myComponent.mount("target")
}