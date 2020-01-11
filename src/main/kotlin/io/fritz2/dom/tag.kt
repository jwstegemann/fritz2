package io.fritz2.dom

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import io.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import kotlin.browser.window

@DslMarker
annotation class HtmlTagMarker

//TODO: Could inherit w3c.dom.Element by Delegation
@HtmlTagMarker
abstract class Tag(tagName: String, override val domNode: Element = window.document.createElement(tagName))
    : WithDomNode<Element>, WithAttributes<Element>, WithEvents<Element>, HtmlElements {

    override fun <E : Tag> register(element: E, content: (E) -> Unit): E {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    fun Flow<Tag>.bind(): SingleMountPoint<WithDomNode<Element>> = DomMountPoint(this, domNode)

    fun Flow<Patch<Tag>>.bind(): MultiMountPoint<WithDomNode<Element>> = DomMultiMountPoint(this, domNode)
}
