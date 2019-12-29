import kotlinx.coroutines.flow.map
import HTML.div
import HTML.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val x = Var<Int>(10)
    val y = flow {
        for (i in 1..10) {
            println("Emitting $i")
            emit("test $i")
            delay(10000)
        }
    }

    fun myNestedComponent(c: String) = y.map {
        div {
            +"$c - $it"
        }
    }

    val myComponent = x.flow().map {
        div {
            div {
                +"Wert: $it"
            }
            myNestedComponent("$it".reversed()).bind()
        }
    }

    myComponent.mount("target")

    Browser.run(x)

}