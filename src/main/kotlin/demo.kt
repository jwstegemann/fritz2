import io.fritz2.binding.Store
import io.fritz2.binding.not
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import org.w3c.dom.events.Event
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.MouseEvent


data class ActionData(val x: Int, val y: Int)

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : Store<String>("start") {
        val addADot = Handler<ActionData> { model, _ ->
            "$model."
        }
        val inputHasFocus = Handler<FocusEvent> { model, _ ->
            console.log("input has now focus")
            model
        }
        val divClicked = Handler<MouseEvent> { model, _ ->
            console.log("div clicked")
            model
        }
    }

    val myComponent = html {
        div {
            input() {
                value = store.data
                store.update <= changes
                type = !"text"
                maxLength = !10
                store.inputHasFocus <= focuss
            }
            div {
                +"value: "
                store.data.bind()
                store.divClicked <= clicks
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