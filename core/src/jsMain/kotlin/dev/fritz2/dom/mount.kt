package dev.fritz2.dom

import dev.fritz2.binding.MultiMountPoint
import dev.fritz2.binding.Patch
import dev.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.window

/**
 * A [SingleMountPoint] to mount the values of a [Flow] of [WithDomNode]s (mostly [Tag]s) at this point in the DOM.
 * If you mix constant [Tag]s with one or more of these MountPoints, the order ist not guaranteed.
 * Wrap your mounted elements in a constant [Tag] or use [DomMountPointFixOrder] instead (for example by setting preseveOrder when binding).
 *
 * @param upstream the Flow of [WithDomNode]s to mount here.
 */
class DomMountPoint<T : org.w3c.dom.Node>(upstream: Flow<WithDomNode<T>>, val target: org.w3c.dom.Node?) :
    SingleMountPoint<WithDomNode<T>>(upstream) {

    /**
     * updates the elements in the DOM
     *
     * @param value new [Tag]
     * @param last last [Tag] (to be replaced)
     */
    override fun set(value: WithDomNode<T>, last: WithDomNode<T>?) {
        if (last?.domNode != null) {
            target?.replaceChild(value.domNode, last.domNode)
        } else {
            target?.appendChild(value.domNode)
        }
    }
}


/**
 * A [SingleMountPoint] to mount the values of a [Flow] of [WithDomNode]s (mostly [Tag]s) at this point in the DOM.
 * This MountPoint guarantees to preserve the order of children at it's target by using a placeholder-comment to reserve
 * it's place in the child-list until the first value on the upstream flow is available.
 * For performance-reasons and because it is not necessary in most use-cases this is not the default-behaviour when binding a flow.
 * You can enable it though be setting the preserveOrder-parameter when binding.
 *
 * @param upstream the Flow of [WithDomNode]s to mount here.
 */
class DomMountPointPreserveOrder<T : org.w3c.dom.Node>(upstream: Flow<WithDomNode<T>>, val target: org.w3c.dom.Node?) :
    SingleMountPoint<WithDomNode<T>>(upstream) {

    var placeholder: Comment? = document.createComment("...")

    /**
     * updates the elements in the DOM
     *
     * @param value new [Tag]
     * @param last last [Tag] (to be replaced)
     */
    override fun set(value: WithDomNode<T>, last: WithDomNode<T>?) {
        if (last?.domNode != null) {
            target?.replaceChild(value.domNode, last.domNode)
        } else {
            target?.replaceChild(value.domNode, placeholder!!)
            placeholder = null // so it can be garbage collected
        }
    }

    init {
        target?.appendChild(placeholder!!)
    }
}


/**
 * A [MultiMountPoint] to mount the values of a [Flow] of [Patch]es (mostly [Tag]s) at this point in the DOM.
 *
 * @param upstream the Flow of [WithDomNode]s to mount here.
 */
class DomMultiMountPoint<T : org.w3c.dom.Node>(upstream: Flow<Patch<WithDomNode<T>>>, val target: org.w3c.dom.Node?) :
    MultiMountPoint<WithDomNode<T>>(upstream) {

    private fun Node.insertOrAppend(child: Node, index: Int): Unit {
        if (index == childNodes.length) appendChild(child)
        else childNodes.item(index)?.let {
            insertBefore(child, it)
        }
    }

    private fun Node.insert(element: WithDomNode<T>, index: Int): Unit = insertOrAppend(element.domNode, index)

    private fun Node.insertMany(elements: List<WithDomNode<T>>, index: Int) {
        if (index == childNodes.length) {
            for (child in elements.reversed()) appendChild(child.domNode)
        } else {
            childNodes.item(index)?.let {
                for (child in elements.reversed()) {
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
        val itemToMove = childNodes.item(from)
        if (itemToMove != null) insertOrAppend(itemToMove, to)
    }

    /**
     * executes the patches on the DOM
     *
     * @param patch [Patch] to handle
     */
    override fun patch(patch: Patch<WithDomNode<T>>) {
        when (patch) {
            is Patch.Insert -> target?.insert(patch.element, patch.index)
            is Patch.InsertMany -> target?.insertMany(patch.elements, patch.index)
            is Patch.Delete -> target?.delete(patch.start, patch.count)
            is Patch.Move -> target?.move(patch.from, patch.to)
        }
    }

}

/**
 * a [SingleMountPoint] to mount the values of a [Flow] to a DOM-attribute.
 *
 * @param name of the attribute
 * @param upstream [Flow] to mount to the attribute
 * @param target the element where to set the attribute
 */
class AttributeMountPoint(val name: String, upstream: Flow<String>, val target: Element?) :
    SingleMountPoint<String>(upstream) {
    /**
     * updates the attribute-value in the DOM
     *
     * @param value new value
     * @param value last value (to be replaced)
     */
    override fun set(value: String, last: String?) {
        target?.setAttribute(name, value)
    }
}

/**
 * [BooleanAttributeMountPoint] is a special [SingleMountPoint] for the boolean attributes.
 */
class BooleanAttributeMountPoint(
    private val name: String,
    upstream: Flow<Boolean>,
    private val target: Element?,
    private val trueValue: String
) : SingleMountPoint<Boolean>(upstream) {
    override fun set(value: Boolean, last: Boolean?) {
        if (value) {
            target?.setAttribute(name, trueValue)
        } else {
            target?.removeAttribute(name)
        }
    }
}

/**
 * [ValueAttributeDelegate] is a special [SingleMountPoint] for the html value
 * attribute with the setter directly and the `setAttribute` method.
 */
class ValueAttributeMountPoint(upstream: Flow<String>, val target: Element?) : SingleMountPoint<String>(upstream) {
    /**
     * updates the attribute value in the DOM
     *
     * @param value new value
     * @param value last value (to be replaced)
     */
    override fun set(value: String, last: String?) {
        target?.unsafeCast<HTMLInputElement>()?.value = value
        target?.unsafeCast<HTMLInputElement>()?.defaultValue = value
        target?.setAttribute("value", value)
    }
}

/**
 * [CheckedAttributeMountPoint] is a special [SingleMountPoint] for the html checked
 * attribute with the setter directly and the `setAttribute` method.
 */
class CheckedAttributeMountPoint(upstream: Flow<Boolean>, val target: Element?) : SingleMountPoint<Boolean>(upstream) {
    /**
     * updates the attribute checked in the DOM
     *
     * @param value new value
     * @param value last value (to be replaced)
     */
    override fun set(value: Boolean, last: Boolean?) {
        target?.unsafeCast<HTMLInputElement>()?.checked = value
        target?.unsafeCast<HTMLInputElement>()?.defaultChecked = value
        if(value) target?.setAttribute("checked", "")
        else target?.removeAttribute("checked")
    }
}

/**
 * [SelectedAttributeMountPoint] is a special [SingleMountPoint] for the html selected
 * attribute with the setter directly and the `setAttribute` method.
 */
class SelectedAttributeMountPoint(upstream: Flow<Boolean>, val target: Element?) : SingleMountPoint<Boolean>(upstream) {
    /**
     * updates the attribute selected in the DOM
     *
     * @param value new value
     * @param value last value (to be replaced)
     */
    override fun set(value: Boolean, last: Boolean?) {
        target?.unsafeCast<HTMLOptionElement>()?.selected = value
        target?.unsafeCast<HTMLOptionElement>()?.defaultSelected = value
        if(value) target?.setAttribute("selected", "")
        else target?.removeAttribute("selected")
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


/**
 * mounts a [Flow] of [Tag]s to a constant element
 *
 * @param targetId id of the element to mount to
 * @receiver the [Flow] to mount to this element
 */
fun <X : Element> Flow<Tag<X>>.mount(targetId: String) {
    window.document.getElementById(targetId)?.let {
        it.removeChildren()
        DomMountPoint(this, it)
    }
}

/**
 * appends one or more [Flow]s of [Tag]s to the content of a constant element
 *
 * @param targetId id of the element to mount to
 * @param flows the [Flow]s to mount to this element
 */
fun <X : Element> append(targetId: String, vararg flows: Flow<Tag<X>>) {
    window.document.getElementById(targetId)?.let { element ->
        flows.forEach { flow -> DomMountPoint(flow, element) }
    }
}


/**
 * mounts a static [Tag] to an elements content
 *
 * @param targetId id of the element to mount to
 */
fun <X : Element> Tag<X>.mount(targetId: String) {
    window.document.getElementById(targetId)?.let {
        it.removeChildren()
        it.appendChild(this.domNode)
    }
}

/**
 * appends one or more static [Tag]s to an elements content
 *
 * @param targetId id of the element to mount to
 */
fun <X : Element> append(targetId: String, vararg tags: Tag<X>) {
    window.document.getElementById(targetId)?.let { element ->
        tags.forEach { tag -> element.appendChild(tag.domNode) }
    }
}
