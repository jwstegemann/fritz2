import kotlinx.coroutines.flow.map
import HTML.div
import HTML.mount


fun main() {

    val x = Var<Int>(10)

    val myComponent = x.flow().map {
        div {
            div {
                +"Wert: $it"
            }
        }
    }

    myComponent.mount("target")

    Browser.run(x)

}