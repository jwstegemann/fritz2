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
import kotlin.browser.window

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

    fun <X : Element> bind(flow: Flow<Tag<X>>): SingleMountPoint<WithDomNode<Element>> = DomMountPoint(flow, domNode)

    fun <X : Element> bind(seq: Seq<Tag<X>>): MultiMountPoint<WithDomNode<Element>> = DomMultiMountPoint(seq.data, domNode)

    operator fun <T> T.not() = Const(this)

    var className: Flow<String> by AttributeDelegate
    var classList: Flow<List<String>>
        get() {throw NotImplementedError()}
        set(values) { attribute("class", values)}
}

internal fun createDomElement(tagName: String, id: String? = null): Element =
    window.document.createElement(tagName).also {element ->
        id?.let {
            element.setAttribute("id",id)
        }
    }
