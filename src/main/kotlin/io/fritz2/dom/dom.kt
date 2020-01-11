package io.fritz2.dom

import org.w3c.dom.Node

interface WithDomNode<out T : Node> {
    val domNode: T
}






