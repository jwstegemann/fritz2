package io.fritz2.dom

import io.fritz2.binding.Const
import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import io.fritz2.binding.WithSeverity
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
abstract class Tag<out T : Element>(tagName: String, override val domNode: T = window.document.createElement(tagName).unsafeCast<T>())
    : WithDomNode<T>, WithAttributes<T>, WithEvents<T>(), HtmlElements {

    override fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    fun <X : Element> Flow<Tag<X>>.bind(): SingleMountPoint<WithDomNode<Element>> = DomMountPoint(this, domNode)

    fun <X : Element> Flow<Patch<Tag<X>>>.bind(): MultiMountPoint<WithDomNode<Element>> = DomMultiMountPoint(this, domNode)

    operator fun <T> T.not() = Const(this)

    var id: Flow<String> by AttributeDelegate
    var `class`: Flow<String> by AttributeDelegate
    var classes: Flow<List<String>>
        get() {throw NotImplementedError()}
        set(values) { attribute("class", values)}

    fun <X : WithSeverity> Flow<List<X>>.bind(): SingleMountPoint<List<X>> = object : SingleMountPoint<List<X>>(this){
        override fun set(value: List<X>, last: List<X>?) {}
    }
}
