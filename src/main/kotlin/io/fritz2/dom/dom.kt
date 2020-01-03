package io.fritz2.dom

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import io.fritz2.dom.html.Button
import io.fritz2.dom.html.Div
import io.fritz2.dom.html.Input
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Text
import org.w3c.dom.events.*
import kotlin.browser.window
import kotlin.reflect.KProperty


//TODO: Variance
interface WithDomNode<out T : org.w3c.dom.Node> {
    val domNode: T
}

@ExperimentalCoroutinesApi
interface WithText<T : org.w3c.dom.Node> : WithDomNode<T> {
    operator fun String.unaryPlus() = domNode.appendChild(TextNode(this).domNode)

    operator fun Flow<String>.unaryPlus(): SingleMountPoint<Node<Text>> = this.bind()

    //TODO: what does conflate mean?
    fun Flow<String>.bind() = DomMountPoint<Text>(this.map {
        TextNode(it)
    }.distinctUntilChanged().conflate(), domNode)
}

//TODO: Could inherit w3c.dom.Node by Delegation
//FIXME: Add DSL-Marker-Annotation
abstract class Node<out T : org.w3c.dom.Node>(override val domNode: T) : WithDomNode<T> {

    //TODO: generic fun to register new nodes

    //FIXME: move subs to somewhere elese (element?, interfaces?)
    fun div(content: Div.() -> Unit): Div = Div().also {
        it.content()
        domNode.appendChild(it.domNode)
    }

    fun button(content: Button.() -> Unit): Button = Button().also {
        it.content()
        domNode.appendChild(it.domNode)
    }

    fun input(content: Input.() -> Unit): Input = Input().also {
        it.content()
        domNode.appendChild(it.domNode)
    }

    fun Flow<Element>.bind(): SingleMountPoint<Node<org.w3c.dom.Element>> = DomMountPoint(this, domNode)

    fun Flow<Patch<Element>>.bind(): MultiMountPoint<Node<org.w3c.dom.Element>> = DomMultiMountPoint(this, domNode)
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

    //TODO: better syntax with infix like "handle EVENT by HANDLER"
    fun event(type: String, handler: (Event) -> Unit) = domNode.addEventListener(type, handler)

    @FlowPreview
    @ExperimentalCoroutinesApi

    //TODO: make generic
    fun event(type: String): Flow<String> {
        console.log("bin da")
        val channel = ConflatedBroadcastChannel<String>()

        domNode.addEventListener(type, {
            GlobalScope.run {
                val data = (it.target as HTMLInputElement).value
                println("CHANGED TO $data")
                channel.offer(data)
            }
        })

        return channel.asFlow().distinctUntilChanged()
    }

    fun Flow<String>.bind(name: String) = AttributeMountPoint(name, this, domNode)

    fun String.component1(): Flow<String> = flowOf(this)
}

class TextNode(private val content: String): Node<Text>(window.document.createTextNode(content))
