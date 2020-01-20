import io.fritz2.binding.Store
import io.fritz2.binding.rangeTo
import io.fritz2.dom.Tag
import io.fritz2.dom.html.Change
import io.fritz2.dom.html.Click
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


data class ActionData(val x: Int, val y: Int)

operator fun <M,A> Store<M>.Slot<A>.compareTo(flow: Flow<A>): Int {
    flow.rangeTo(this)
    return 0
}

operator fun <T> Flow<T>.dec() = this


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : Store<String>("start") {
        val addADot = Slot<ActionData> { model, _ ->
            "$model."
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

        }
    }

    myComponent.mount("target")
}