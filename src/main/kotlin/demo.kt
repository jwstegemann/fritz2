import io.fritz2.binding.*
import io.fritz2.dom.Element
import io.fritz2.dom.Node
import io.fritz2.dom.html.Div
import io.fritz2.dom.html.div
import io.fritz2.dom.mount
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


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

    val s = Seq<String>(listOf("a","b","c"))

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
            button {
                +"Test-Button"
                event("click") {
                    //TODO: better convenience (coroutine-scope)
                    GlobalScope.launch {
                        y.set(y.value()+'.')
                    }
                }
            }
            // sequence
            s.map {
                Patch(it.from, it.that.map {
                    div {
                        +it
                    }
                }, it.replaced)
            }.bind()
        }
    }

    myComponent.mount("target")

//    Browser.run(x)

}