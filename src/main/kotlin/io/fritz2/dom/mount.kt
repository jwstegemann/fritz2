package io.fritz2.dom

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import io.fritz2.util.removeChildren
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.get
import kotlin.browser.window

class DomMountPoint<T : org.w3c.dom.Node>(upstream: Flow<WithDomNode<T>>, val target: org.w3c.dom.Node?) : SingleMountPoint<WithDomNode<T>>(upstream) {
    override fun set(value: WithDomNode<T>, last: WithDomNode<T>?) {
        last?.let { target?.replaceChild(value.domNode, last.domNode) }
                ?: target?.appendChild(value.domNode)
    }
}

class DomMultiMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Patch<WithDomNode<T>>>, val target: org.w3c.dom.Node?): MultiMountPoint<WithDomNode<T>>(upstream) {
    //FIXME: optimize
    private tailrec fun removeChildren(child: org.w3c.dom.Node?, n: Int): org.w3c.dom.Node? {
        return if (n == 0) {
            child
        } else {
            val nextSibling = child?.nextSibling
            if (child != null) target?.removeChild(child)
            removeChildren(nextSibling, n - 1)
        }
    }

    override fun patch(patch: Patch<WithDomNode<T>>) {
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

class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: Element?) : SingleMountPoint<String>(upstream) {
    override fun set(value: String, last: String?) {
        target?.setAttribute(name, value)
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun Flow<Tag>.mount(targetId: String) {
    window.document.getElementById(targetId)?.let {
        it.removeChildren()
        DomMountPoint(this, it)
    }
}

//TODO: is this ok? Better use Constant-Flow?
@ExperimentalCoroutinesApi
@FlowPreview
fun Tag.mount(targetId: String) {
    window.document.getElementById(targetId)?.let {
        it.removeChildren()
        it.appendChild(this.domNode)
    }
}
