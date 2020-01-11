import io.fritz2.binding.Slot
import io.fritz2.binding.Store
import io.fritz2.binding.Var
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.w3c.dom.events.MouseEvent


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = object : Store<String>(Var<String>("start")) {
        val addADot = Slot<MouseEvent> {
            data.set(data.value() + ".")
        }
    }

    val myComponent = html {
        div {
            input() {
                value = model.data
                onChange = model.update
            }
            div {
                +"value: "
                model.data.bind()
            }
            button {
                +"add one more little dot"
                onClick = model.addADot
            }

        }
    }

    myComponent.mount("target")
}