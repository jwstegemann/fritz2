package dev.fritz2.dom

import org.w3c.dom.Node

/**
 * Base-interface for everything that represents a node in the DOM.
 *
 * @property domNode the wrapped [Node]
 */
interface WithDomNode<out T : Node> {
    val domNode: T
}