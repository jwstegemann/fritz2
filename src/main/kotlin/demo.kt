import io.fritz2.binding.*
import io.fritz2.dom.Element
import io.fritz2.dom.Node
import io.fritz2.dom.html.Change
import io.fritz2.dom.html.Div
import io.fritz2.dom.html.div
import io.fritz2.dom.mount
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = object : Store<String>(Var<String>("start")) {
        val addADot = Slot<MouseEvent> {
            data.set(data.value() + ".")
        }
    }

    val myComponent = div {
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

    myComponent.mount("target")
}