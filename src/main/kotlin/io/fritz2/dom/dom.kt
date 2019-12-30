package io.fritz2.dom

import io.fritz2.binding.SingleMountPoint
import io.fritz2.dom.html.Div
import kotlinx.coroutines.flow.*
import org.w3c.dom.Document
import org.w3c.dom.Text
import kotlin.browser.window
import kotlin.reflect.KProperty


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

object AttributeDelegate {
    operator fun getValue(thisRef: Element, property: KProperty<*>): Flow<String> = throw NotImplementedError()
    operator fun setValue(thisRef: Element, property: KProperty<*>, values: Flow<String>) {
        thisRef.attribute(property.name, values)
    }
}

//TODO: Could inherit w3c.dom.Element by Delegation
abstract class Element(tagName: String, override val domNode: org.w3c.dom.Element = window.document.createElement(tagName)) : Node<org.w3c.dom.Element>(domNode) {
    fun attribute(name: String, value: String) = domNode.setAttribute(name, value)
    fun attribute(name: String, values: Flow<String>) = values.bind(name)

    //TODO: convenience-methods for data-attributes

    fun Flow<String>.bind(name: String) = AttributeMountPoint(name, this, domNode)

    fun String.component1(): Flow<String> = flowOf(this)
}

class TextNode(private val content: String): Node<Text>(window.document.createTextNode(content))
