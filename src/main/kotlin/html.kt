import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringWriter
import java.io.Writer
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult



class ElementMountPoint(val parent: Element, upstream: Flow<HtmlTree>) : SingleMountPoint<HtmlTree>(upstream) {
    override fun set(value: HtmlTree, last: HtmlTree?) {
        parent.append(value)
    }
}

typealias HtmlTree = TagConsumer<Element>.()->Unit




fun mount(target: Element, upstream: Flow<HtmlTree>): ElementMountPoint {
    return ElementMountPoint(target, upstream)
}



fun main() {

    val document = createHTMLDocument().body {
        id = "target"
    }

    runBlocking {
        val target = document.getElementById("target")

        val x = Var<Int>(10)

        val c = x.flow().map {
           val x : HtmlTree = {
               div {
                   +"$it"
               }
           }
           x
       }

        mount(target, c)

        while(true) {
            val inputString: String? = readLine()
            val inputInt : Int? = inputString?.toInt()
            if (inputInt != null && inputInt != 0) x.set(inputInt)
            else println(document.serialize())
        }
    }

}