package io.fritz2.dom

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import kotlin.browser.window

class DomMountPoint<T : org.w3c.dom.Node>(upstream: Flow<WithDomNode<T>>, val target: org.w3c.dom.Node?) :
    SingleMountPoint<WithDomNode<T>>(upstream) {
    override fun set(value: WithDomNode<T>, last: WithDomNode<T>?) {
        last?.let { target?.replaceChild(value.domNode, last.domNode) }
            ?: target?.appendChild(value.domNode)
    }
}

class DomMultiMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Patch<WithDomNode<T>>>, val target: org.w3c.dom.Node?) :
    MultiMountPoint<WithDomNode<T>>(upstream) {
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
        //console.log("### MountPoint: ... patching: ${patch.from} with ${patch.that} replacing ${patch.replaced}")
/*        patch.apply {
            val child = removeChildren(target?.childNodes?.get(from), replaced)
            //console.log("### MountPoint: child: $child")
            if (child == null) {
                for (newChild in that) {
                    target?.appendChild(newChild.domNode)
                    //console.log("### MountPoint: ... appending: $newChild")
                }
            } else {
                for (newChild in that) {
                    target?.insertBefore(newChild.domNode, child)
                    //console.log("### MountPoint: ... insert: $newChild")
                }
            }
        }

 */
    }

}

class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: Element?) :
    SingleMountPoint<String>(upstream) {
    override fun set(value: String, last: String?) {
        //FIXME: Should only be true for Boolean-Attributes...
        if (value == "false") target?.removeAttribute(name)
        else target?.setAttribute(name, value)
    }
}

/**
 * [ValueAttributeDelegate] is a special [SingleMountPoint] for the html value
 * attribute without calling `setAttribute` method.
 */
class ValueAttributeMountPoint(upstream: Flow<String>, val target: Element?) : SingleMountPoint<String>(upstream) {
    override fun set(value: String, last: String?) {
        if (value == "false") target?.removeAttribute("value")
        else target?.unsafeCast<HTMLInputElement>()?.value = value
    }
}

//TODO: maybe possible with addClass() and removeClass() methods on elements?
//class AttributeMultiMountPoint(val name: String, upstream: Flow<Patch<String>>, val target: Element?) : MultiMountPoint<String>(upstream) {
//
//    override fun patch(patch: Patch<String>) {
//        patch.apply {
//            console.log(this)
//            var entries = target?.getAttribute(name)?.split(' ')?.toMutableList()
//            if (entries == null) entries = mutableListOf()
//            if(replaced == 0) {
//                entries.addAll(from, that)
//            } else {
//                for (i in from until (from + replaced)) {
//                    entries.removeAt(i)
//                }
//                entries.addAll(from, that)
//            }
//            target?.setAttribute(name, entries.joinToString(separator = " "))
//        }
//    }
//}


fun <X : Element> Flow<Tag<X>>.mount(targetId: String) {
    window.document.getElementById(targetId)?.let {
        it.removeChildren()
        DomMountPoint(this, it)
    }
}


fun <X : Element> append(targetId: String, vararg flows: Flow<Tag<X>>) {
    window.document.getElementById(targetId)?.let { element ->
        flows.forEach { flow -> DomMountPoint(flow, element) }
    }
}


fun <X : Element> Tag<X>.mount(targetId: String) {
    window.document.getElementById(targetId)?.let {
        it.removeChildren()
        it.appendChild(this.domNode)
    }
}


fun <X : Element> append(targetId: String, vararg tags: Tag<X>) {
    window.document.getElementById(targetId)?.let { element ->
        tags.forEach { tag -> element.appendChild(tag.domNode) }
    }
}
