import io.fritz2.binding.Patch
import io.fritz2.binding.Seq
import io.fritz2.binding.Var
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
@FlowPreview
fun oldMain() {

    val x = Var<Int>(10)
    val y = Var<String>("test")
    val z = flow {
        for (i in 1..10) {
            console.log("Emitting $i")
            emit("test $i")
            delay(10000)
        }
    }
    val a = Var<String>("100%")

    fun myNestedComponent(c: String) = y.map {
        html {
            div {
                +"$c - $it"
            }
        }
    }

    val s = Seq<String>(listOf("a","b","c"))

    val myComponent = x.map {
        html {
            div {
                attribute("width", "20%")
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
                            y.set(y.value() + '.')
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
    }

    myComponent.mount("target")

    GlobalScope.launch {
        delay(1000)
        s.set(listOf("a","b","c","d"))
        delay(1000)
        s.set(listOf("a","b","c"))
        delay(1000)
        s.set(listOf("a","e","c"))
    }
}