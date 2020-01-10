import io.fritz2.binding.*
import io.fritz2.dom.*
import io.fritz2.dom.html.Change
import io.fritz2.dom.html.Div
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

    val myComponent = Html.div {
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