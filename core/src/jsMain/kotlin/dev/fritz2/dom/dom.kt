package dev.fritz2.dom

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.get

/**
 * base-interface for everything that represents a node in the DOM.
 */
interface WithDomNode<out T : Node> {
    val domNode: T
}

internal fun Element.removeChildren() {
    val children = this.childNodes
    for (i in 0 until children.length) {
        children[i]?.let { this.removeChild(it) }
    }
}




