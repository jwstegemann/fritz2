package io.fritz2.dom

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.get
import kotlin.browser.window

class DomMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Node<T>>, val target: org.w3c.dom.Node?) : SingleMountPoint<Node<T>>(upstream) {
    override fun set(value: Node<T>, last: Node<T>?) {
        last?.let { target?.replaceChild(value.domNode, last.domNode) }
                ?: target?.appendChild(value.domNode)
    }
}

class DomMultiMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Patch<Node<T>>>, val target: org.w3c.dom.Node?): MultiMountPoint<Node<T>>(upstream) {
    //FIXME: optimize and make tailrec
    private tailrec fun removeChildren(child: org.w3c.dom.Node?, n: Int): org.w3c.dom.Node? {
        if (n == 0) {
            return child
        } else {
            val nextSibling = child?.nextSibling
            if (child != null) target?.removeChild(child)
            return removeChildren(nextSibling, n - 1)
        }
    }

    override fun patch(patch: Patch<Node<T>>) {
//        console.log("### MountPoint: ... patching: ${patch.from} with ${patch.that} replacing ${patch.replaced}")
        patch.apply {
            val child = removeChildren(target?.childNodes?.get(from), replaced)
//            console.log("### MountPoint: child: $child")
            if (child == null) {
                for (newChild in that) {
                    target?.appendChild(newChild.domNode)
//                    console.log("### MountPoint: ... appending: $newChild")
                }
            } else {
                for (newChild in that) {
                    target?.insertBefore(newChild.domNode, child)
//                    console.log("### MountPoint: ... insert: $newChild")
                }
            }
        }
    }

}

class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: org.w3c.dom.Element?) : SingleMountPoint<String>(upstream) {
    override fun set(value: String, last: String?) {
        target?.setAttribute(name, value)
    }
}

fun Flow<Element>.mount(targetId: String) {
    window.document.getElementById(targetId)?.let { DomMountPoint(this, it) }
}
