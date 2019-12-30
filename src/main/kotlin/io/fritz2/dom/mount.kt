package io.fritz2.dom

import io.fritz2.binding.SingleMountPoint
import io.fritz2.util.Browser
import kotlinx.coroutines.flow.Flow

class DomMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Node<T>>, val target: org.w3c.dom.Node?) : SingleMountPoint<Node<T>>(upstream) {
    override fun set(value: Node<T>, last: Node<T>?) {
        last?.let { target?.replaceChild(value.domNode, last.domNode) }
                ?: target?.appendChild(value.domNode)
    }
}

class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: org.w3c.dom.Element?) : SingleMountPoint<String>(upstream) {
    override fun set(value: String, last: String?) {
        target?.setAttribute(name, value)
    }
}

fun Flow<Element>.mount(targetId: String) {
    Browser.document.getElementById(targetId)?.let { DomMountPoint(this, it) }
}
