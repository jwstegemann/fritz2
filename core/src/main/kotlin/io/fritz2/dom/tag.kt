package io.fritz2.dom

import io.fritz2.binding.*
import io.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import kotlin.browser.window

@DslMarker
annotation class HtmlTagMarker

//TODO: Could inherit w3c.dom.Element by Delegation
@ExperimentalCoroutinesApi
@FlowPreview
@HtmlTagMarker
abstract class Tag<T : Element>(tagName: String, val id: String? = null, override val domNode: T = createDomElement(tagName, id).unsafeCast<T>())
    : WithDomNode<T>, WithAttributes<T>, WithEvents<T>(), HtmlElements {

    override fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    fun <X : Element> Flow<Tag<X>>.bind(): SingleMountPoint<WithDomNode<Element>> = DomMountPoint(this, domNode)

    fun <X : Element> Seq<Tag<X>>.bind(): MultiMountPoint<WithDomNode<Element>> = DomMultiMountPoint(this.data, domNode)

    operator fun <T> T.not() = Const(this)

    operator fun <E: Event, X: Element> Handler<Unit>.compareTo(listener: Listener<E, X>): Int {
        execute(listener.events.map { Unit })
        return 0
    }

    var `class`: Flow<String> by AttributeDelegate
    var classes: Flow<List<String>>
        get() {throw NotImplementedError()}
        set(values) { attribute("class", values)}
}

internal fun createDomElement(tagName: String, id: String? = null): Element =
    window.document.createElement(tagName).also {element ->
        id?.let {
            element.setAttribute("id",id)
        }
    }
