package io.fritz2.examples.gettingstarted

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map


data class ActionData(val x: Int, val y: Int)

data class ValMsg(override val id: String,  override val severity: Severity, val text: String): WithSeverity
data class ValMetadata(val step: String)

class ListValidator: Validator<List<String>, ValMsg, ValMetadata> {
    override fun validate(data: List<String>, metadata: ValMetadata): List<ValMsg> {
        return when {
            metadata.step == "test" -> listOf(ValMsg("test", Severity.Info, "Test Step!"))
            data.isEmpty() -> listOf(ValMsg("listEmpty", Severity.Warning, "List should not be empty!"))
            data.size > 5 -> listOf(ValMsg("listMaxLimit", Severity.Error, "Maximum list size reached!"))
            else -> emptyList()
        }
    }

}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("start") {
        val addADot = Handler<ActionData> { model, _ ->
            "$model."
        }
    }

    val seq = object : RootStore<List<String>>(listOf("one", "two", "three")), Validation<List<String>, ValMsg, ValMetadata> {

        override val validator: Validator<List<String>, ValMsg, ValMetadata> = ListValidator()
        override var msgs: Flow<ValMsg> = emptyFlow()

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