import io.fritz2.binding.ConcreteSlot
import io.fritz2.binding.Slot
import io.fritz2.binding.Store
import io.fritz2.binding.Var
import io.fritz2.dom.html.Change
import io.fritz2.dom.html.Click
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.events.MouseEvent


data class ActionData(val x: Int, val y: Int)

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = object : Store<String>("start") {

    }

    val myComponent = html {
        div {
            input() {
                value = model.data
                model.subscribe(event(Change)) { m, a ->
                    a
                }
            }
            div {
                +"value: "
                model.data.bind()
            }
            button {
                +"add one more little dot"
                model.subscribe(event(Click)) { m, a ->
                    "$m."
                }
            }

        }
    }

    myComponent.mount("target")
}