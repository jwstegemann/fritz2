package io.fritz2.util

import org.w3c.dom.Element
import org.w3c.dom.get

fun Element.removeChildren() {
    val children = this.childNodes
    for (i in 0 until children.length) {
        children[i]?.let { this.removeChild(it) }
    }
}