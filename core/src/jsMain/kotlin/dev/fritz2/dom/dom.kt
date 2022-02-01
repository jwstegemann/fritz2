package dev.fritz2.dom

import org.w3c.dom.Node

/**
 * Base-interface for everything that represents a node in the DOM.
 *
 * @property domNode the wrapped [Node]
 */
external interface WithDomNode<out N : Node> {
    val domNode: N
}
