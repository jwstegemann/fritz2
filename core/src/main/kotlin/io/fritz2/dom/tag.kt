package io.fritz2.dom

import io.fritz2.binding.Const
import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Seq
import io.fritz2.binding.SingleMountPoint
import io.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.Node
import kotlin.browser.window
import kotlin.js.Math
import kotlin.random.Random

@DslMarker
annotation class HtmlTagMarker

//TODO: Could inherit w3c.dom.Element by Delegation
@ExperimentalCoroutinesApi
@FlowPreview
@HtmlTagMarker
abstract class Tag<out T : Element>(tagName: String, val id: String? = null, override val domNode: T = createDomElement(tagName, id).unsafeCast<T>())
    : WithDomNode<T>, WithAttributes<T>, WithEvents<T>(), HtmlElements {

    override fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    fun <X : Element> Flow<Tag<X>>.bind(): SingleMountPoint<WithDomNode<Element>> = DomMountPoint(this, domNode)

    fun <X : Element> Seq<Tag<X>>.bind(): MultiMountPoint<WithDomNode<Element>> = DomMultiMountPoint(this.data, domNode)

    operator fun <T> T.not() = Const(this)

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
