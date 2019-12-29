package backup

import Browser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import HTML.div
import HTML.mount
import Var
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun main() {
    val x = Var<Int>(10)

    val myComponent = x.flow().map {
        div {
            +"Wert: $it"
        }
    }

    myComponent.mount("target")

    Browser.run(x)
}