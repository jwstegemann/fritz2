import io.fritz2.binding.Store
import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import io.fritz2.binding.map
import io.fritz2.binding.mapIndexed
import io.fritz2.dom.html.Li
import org.w3c.dom.HTMLButtonElement


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
        val deleteItem = Handler<Int> { list, i ->
            list.drop(i)
        }
    }

    val myComponent = html {
        div {
            input() {
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
                seq.each().mapIndexed{ i:Int, s: String ->
                    html {
                        button {
                            +s
                            seq.deleteItem <= clicks.map { console.log(i); i }
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