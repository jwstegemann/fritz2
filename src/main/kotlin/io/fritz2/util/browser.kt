package io.fritz2.util

/* import io.fritz2.binding.Var
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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

object Browser {

    fun doc(): Document {
        val dbf = DocumentBuilderFactory.newInstance()
        val builder = dbf.newDocumentBuilder()
        val document: Document = builder.newDocument()

        val html: Element = document.createElement("html")
        document.appendChild(html)

        val body: Element = document.createElement("body")
        body.setAttribute("id", "target")
        body.setIdAttribute("id", true)
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

    @ExperimentalCoroutinesApi
    fun run(x: Var<Int>) {
        runBlocking {
            while (true) {
                print("#")
                readLine()?.toInt()?.let {
                    if (it != 0) x.set(it) else document.prettyPrint()
                }
            }
        }
    }


}

 */