package io.fritz2.dom

import io.fritz2.binding.MultiMountPoint
import io.fritz2.binding.Patch
import io.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
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

    private fun Node.insertOrAppend(child: Node, index: Int): Unit {
        if (index == childNodes.length) appendChild(child)
        else childNodes.item(index)?.let {
//            console.log("inserting ${child.textContent} before ${it.textContent}")
            insertBefore(child, it)
        }
    }

    private fun Node.insert(element: WithDomNode<T>, index: Int): Unit = insertOrAppend(element.domNode, index)

    private fun Node.insertMany(elements: List<WithDomNode<T>>, index: Int) {
//        console.log("insertMany @ $index")
        if (index == childNodes.length) {
            for (child in elements.reversed()) appendChild(child.domNode)
        } else {
            childNodes.item(index)?.let {
//                console.log("inserting before ${it.textContent}")
                for (child in elements.reversed()) {
//                    console.log("... inserting ${child.domNode.textContent}")
                    insertBefore(child.domNode, it)
                }
            }
        }
    }

    private fun Node.delete(start: Int, count: Int): Unit {
        var itemToDelete = childNodes.item(start)
        repeat(count) {
            itemToDelete?.let {
                itemToDelete = it.nextSibling
                removeChild(it)
            }
        }
    }

    private fun Node.move(from: Int, to: Int): Unit {
//        console.log("moving $from -Y $to")
        val itemToMove = childNodes.item(from)
//        console.log("... item $itemToMove")
        if (itemToMove != null) insertOrAppend(itemToMove, to)
    }

    override fun patch(patch: Patch<WithDomNode<T>>) {
        console.log("*** $patch")
        when (patch) {
            is Patch.Insert -> target?.insert(patch.element, patch.index)
            is Patch.InsertMany -> target?.insertMany(patch.elements, patch.index)
            is Patch.Delete -> target?.delete(patch.start, patch.count)
            is Patch.Move -> target?.move(patch.from, patch.to)
        }
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
