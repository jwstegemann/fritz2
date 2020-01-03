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


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val model = Store(Var<String>("start"))

    val myComponent = div {
        input() {
            value = model.data
            onChange = model.update
        }
        div {
            +"value: "
            model.data.bind()
        }

    }

    myComponent.mount("target")
}