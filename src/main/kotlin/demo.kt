import kotlinx.coroutines.flow.map
import io.fritz2.binding.*
import io.fritz2.dom.html.div
import io.fritz2.dom.mount
import io.fritz2.util.Browser
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

    fun myNestedComponent(c: String) = y.map {
        div {
            +"$c - $it"
        }
    }

    val myComponent = x.map {
        div {
            testMe = !"17%"
            //testMe = +"Hugo"
            attribute("width","20%")
            //attribute("height", a)
            a.bind("height")
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