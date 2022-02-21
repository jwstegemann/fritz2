package dev.fritz2.core

import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.get

/**
 * Base-interface for everything that represents a node in the DOM.
 *
 * @property domNode the wrapped [Node]
 */
external interface WithDomNode<out N : Node> {
    val domNode: N
}

/**
 * This adapter class just lifts the DOM API [NodeList] type into
 * [Kotlin's collection API](https://kotlinlang.org/docs/collections-overview.html#collection-types).
 *
 * In order to improve the further processing of typical DOM native calls, it makes sense to offer an adapter type.
 *
 * To ease the usage further, there is a factory extension defined on [NodeList] named [asElementList].
 *
 * Example usage:
 * ```kotlin
 * domnode // some DOM node
 *     .querySelectorAll("") // DOM API's NodeList as result -> cumbersome iterating and processing!
 *     .asElementList() // create this type via extension factory
 *     .filter { it.hasAttribute("data-myData") } // process the result in some way...
 *     .map { ... }
 * ```
 *
 * @param base the initial [NodeList]
 */
class DomNodeList(private val base: NodeList) : AbstractList<Node>() {
    override val size: Int get() = base.length

    override fun get(index: Int): Node = base[index]!!
}

/**
 * Creates a [DomNodeList] containing all [HTMLElement]s from the DOM API based [NodeList].
 *
 * @see DomNodeList
 */
fun NodeList.asElementList() = DomNodeList(this).mapNotNull { if(it is HTMLElement) it else null }
