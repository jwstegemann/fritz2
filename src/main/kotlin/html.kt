import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.html.dom.append
import org.w3c.dom.*

object HTML {

    class DomMountPoint<T : Node>(upstream: Flow<HtmlNode<T>>, val target: Node?) : SingleMountPoint<HtmlNode<T>>(upstream) {
        override fun set(value: HtmlNode<T>, last: HtmlNode<T>?) {
            last?.let { target?.replaceChild(value.domNode, last.domNode) }
                    ?: target?.appendChild(value.domNode)
        }
    }

    class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: Element?) : SingleMountPoint<String>(upstream) {
        override fun set(value: String, last: String?) {
            target?.setAttribute(name, value)
        }
    }

    fun Flow<HtmlNode<Element>>.mount(targetId: String) {
        Browser.document.getElementById(targetId)?.let { DomMountPoint(this, it) }
    }

    fun div(content: Div.() -> Unit): Div = Div().also { it.content() }

    //TODO: Variance
    interface WithDomNode<T : Node> {
        val domNode: T
    }

    interface WithText<T : Node> : WithDomNode<T> {
        operator fun String.unaryPlus() = domNode.appendChild(TextNode(this).domNode)

        operator fun Flow<String>.unaryPlus(): SingleMountPoint<HtmlNode<Text>> = DomMountPoint<Text>(this.map {
            TextNode(it)
        }, domNode)
    }

    abstract class HtmlNode<T : Node>(override val domNode: T) : WithDomNode<T> {

        //TODO: only on Elements? ElementNode als eigenes Interface
        fun attribute(name: String, value: String) = (domNode as Element).setAttribute(name, value)
        fun attribute(name: String, values: Flow<String>) = AttributeMountPoint(name, values, this.domNode as Element)

        fun div(content: Div.() -> Unit): Div = Div().also {
            it.content()
            domNode.appendChild(it.domNode)
        }

        fun Flow<HtmlNode<Element>>.bind(): SingleMountPoint<HtmlNode<Element>> = DomMountPoint(this, domNode)
    }

    class Div(): HtmlNode<Element>(Browser.document.createElement("div")), WithText<Element>

    class TextNode(private val content: String): HtmlNode<Text>(Browser.document.createTextNode(content))

    class AttributeNode(private val name: String): HtmlNode<Attr>(Browser.document.createAttribute(name)) {
        fun set(value: String) {
            domNode.nodeValue = value
        }
    }
}

