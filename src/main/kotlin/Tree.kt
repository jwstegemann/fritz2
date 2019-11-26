import kotlinx.coroutines.flow.Flow
import org.w3c.dom.*
import java.io.StringWriter
import java.io.Writer
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object HTML {

    fun doc(): Document {
        val dbf = DocumentBuilderFactory.newInstance()
        val builder = dbf.newDocumentBuilder()
        val document: Document = builder.newDocument()

        val html: Element = document.createElement("html")
        document.appendChild(html)

        val body: Element = document.createElement("body")
        body.setAttribute("id","target")
        html.appendChild(body)

        return document
    }

    @Throws(Exception::class)
    fun Document.prettyPrint() {
        println("------------------------------------------")
        val tf: Transformer = TransformerFactory.newInstance().newTransformer()
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        tf.setOutputProperty(OutputKeys.INDENT, "yes")
        val out: Writer = StringWriter() as Writer
        tf.transform(DOMSource(this), StreamResult(out))
        println(out.toString())
        println("------------------------------------------")
    }

    val document = doc()

    // ---

    class DomMountPoint(upstream: Flow<HtmlNode>, val target: Node?) : SingleMountPoint<HtmlNode>(upstream) {

        fun Node.replaceContent(by: Node) {
                for (i in 0 until childNodes.length) removeChild(childNodes.item(i))
                appendChild(by)
        }

        override fun set(value: HtmlNode, last: HtmlNode?) {
            if (last != null) target?.replaceChild(value.domNode, last.domNode)
            else target?.replaceContent(value.domNode)
        }

    }

    fun Flow<HtmlNode>.mount(targetId: String) {
        document.getElementsByTagName("body").item(0)?.let {  DomMountPoint(this, it ) }
        //document.getElementsByTagName("body").item(0)?.let { DomMountPoint(this, it) }0
    }

    fun div(init: Div.() -> Unit): Div {
        val div = Div()
        div.init()
        return div
    }


    abstract class HtmlNode(val domNode: Node) {
        fun div(init: Div.() -> Unit): Div {
            val div = Div()
            div.init()
            domNode.appendChild(div.domNode)
            return div
        }

        fun mount(upstream: Flow<HtmlNode>): SingleMountPoint<HtmlNode> {
            return DomMountPoint(upstream, domNode)
        }
    }

    class Div(): HtmlNode(document.createElement("div")) {
        operator fun String.unaryPlus() {
            domNode?.appendChild(document.createTextNode("$this"))
        }
    }


}

