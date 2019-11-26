import HTML.prettyPrint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.html.dom.serialize
import HTML.div
import HTML.mount
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun main() {
    val doc = HTML.document

    val x = Var<Int>(10)

    val myComponent = x.flow().map {
        div {
            +"Wert: $it"
        }
    }

    myComponent.mount("target")

    runBlocking {
        while (true) {
            print("#")
            val inputString: String? = readLine()
            val inputInt: Int? = inputString?.toInt()
            if (inputInt != null && inputInt != 0) x.set(inputInt)
            else println(doc.prettyPrint())
        }
    }
}