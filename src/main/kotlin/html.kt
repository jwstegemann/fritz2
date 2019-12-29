import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.w3c.dom.*

object HTML {

    class DomMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Node<T>>, val target: org.w3c.dom.Node?) : SingleMountPoint<Node<T>>(upstream) {
        override fun set(value: Node<T>, last: Node<T>?) {
            last?.let { target?.replaceChild(value.domNode, last.domNode) }
                    ?: target?.appendChild(value.domNode)
        }
    }

    class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: org.w3c.dom.Element?) : SingleMountPoint<String>(upstream) {
        override fun set(value: String, last: String?) {
            target?.setAttribute(name, value)
        }
    }

    fun Flow<Element>.mount(targetId: String) {
        Browser.document.getElementById(targetId)?.let { DomMountPoint(this, it) }
    }

    fun div(content: Div.() -> Unit): Div = Div().also { it.content() }

    //TODO: Variance
    interface WithDomNode<T : org.w3c.dom.Node> {
        val domNode: T
    }

    interface WithText<T : org.w3c.dom.Node> : WithDomNode<T> {
        operator fun String.unaryPlus() = domNode.appendChild(TextNode(this).domNode)

        operator fun Flow<String>.unaryPlus(): SingleMountPoint<Node<Text>> = this.bind()

        //TODO: what does conflate mean?
        fun Flow<String>.bind() = DomMountPoint<Text>(this.map {
            TextNode(it)
        }.distinctUntilChanged().conflate(), domNode)
    }

    //TODO: Could inherit w3c.dom.Node by Delegation
    abstract class Node<T : org.w3c.dom.Node>(override val domNode: T) : WithDomNode<T> {

        fun div(content: Div.() -> Unit): Div = Div().also {
            it.content()
            domNode.appendChild(it.domNode)
        }

        fun Flow<Element>.bind(): SingleMountPoint<Node<org.w3c.dom.Element>> = DomMountPoint(this, domNode)
    }

    //TODO: Could inherit w3c.dom.Element by Delegation
    abstract class Element(tagName: String, override val domNode: org.w3c.dom.Element = Browser.document.createElement(tagName)) : Node<org.w3c.dom.Element>(domNode) {
        fun attribute(name: String, value: String) = domNode.setAttribute(name, value)
        fun attribute(name: String, values: Flow<String>) = values.bind(name)

        fun Flow<String>.bind(name: String) = AttributeMountPoint(name, this, domNode)
    }

    class Div(): Element("div"), WithText<org.w3c.dom.Element>

    class TextNode(private val content: String): Node<Text>(Browser.document.createTextNode(content))

}

