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
    val y = Var<String>("test")
    val z = flow {
        for (i in 1..10) {
            println("Emitting $i")
            emit("test $i")
            delay(10000)
        }
    }
    val a = Var<String>("100%")

    fun myNestedComponent(c: String) = y.flow().map {
        div {
            +"$c - $it"
        }
    }

    val myComponent = x.flow().map {
        div {
            attribute("width","20%")
            //attribute("height", a.flow())
            a.flow().bind("height")
            div {
                +"Wert: $it"
            }
            +z //Flow of String
            //z.bind()
            //FIXME: unregister Mount-Points/Flows when replaced!
            myNestedComponent("$it".reversed()).bind()
        }
    }

    myComponent.mount("target")

    Browser.run(x)

}