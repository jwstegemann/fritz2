package dev.fritz2.dom

import dev.fritz2.binding.Patch
import dev.fritz2.binding.mountSingle
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Comment
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

/**
 * Mounts the values of a [Flow] of [WithDomNode]s (mostly [Tag]s) at this point in the DOM.
 *
 * @param job to collect values
 * @param target DOM mounting target
 * @param upstream returns the [Flow] that should be mounted at this point
 */
fun <N : Node> mountDomNode(
    job: Job,
    target: N,
    upstream: Flow<WithDomNode<N>>
) {
    var placeholder: Comment? = document.createComment("")

    target.appendChild(placeholder!!)

    mountSingle(job, upstream) { value, last ->
        if (last?.domNode != null) {
            target.replaceChild(value.domNode, last.domNode)
        } else {
            target.replaceChild(value.domNode, placeholder!!)
            placeholder = null // so it can be garbage collected
        }
    }
}

/**
 * Mounts the values of a [Flow] of [WithDomNode]s (mostly [Tag]s) at this point in the DOM.
 * It is fast then [mountDomNode], but if you mix constant [Tag]s with one or more of mounted [Flow]s,
 * the order ist not guaranteed. Wrap your mounted elements in a constant [Tag] or use
 * [mountDomNode] function instead (for example by setting preserveOrder when binding).
 *
 * @param job to collect values
 * @param target DOM mounting target
 * @param upstream returns the [Flow] that should be mounted at this point
 */
fun <N : Node> mountDomNodeUnordered(
    job: Job,
    target: N,
    upstream: Flow<WithDomNode<N>>
) {
    mountSingle(job, upstream) { value, last ->
        if (last?.domNode != null) {
            target.replaceChild(value.domNode, last.domNode)
        } else {
            target.appendChild(value.domNode)
        }
    }
}

/**
 * Mounts the a [List] of a [Flow] of [WithDomNode]s (mostly [Tag]s) at this point in the DOM.
 *
 * @param job to collect values
 * @param target DOM mounting target
 * @param upstream returns the [Flow] with the [List] of [WithDomNode]s that should be mounted at this point
 */
fun <N : Node> mountDomNodeList(
    job: Job,
    target: N,
    upstream: Flow<List<WithDomNode<N>>>
) {
    val placeholder: Comment = document.createComment("")
    target.appendChild(placeholder)

    mountSingle(job, upstream) { value, last ->
        if (last != null) {
            if (last.isNotEmpty()) {
                if (value.isNotEmpty()) value.forEach { target.insertBefore(it.domNode, last.first().domNode) }
                else target.insertBefore(placeholder, last.first().domNode)
                last.forEach { target.removeChild(it.domNode) }
            } else if (value.isNotEmpty()) {
                value.forEach { target.insertBefore(it.domNode, placeholder) }
                target.removeChild(placeholder)
            }
        } else { // first call
            if (value.isNotEmpty()) {
                value.forEach { target.insertBefore(it.domNode, placeholder) }
                target.removeChild(placeholder)
            }
        }
    }
}

/**
 * Mounts [Patch]es of a [Flow] of [WithDomNode]s (mostly [Tag]s) at this point in the DOM.
 *
 * @param job to collect values
 * @param target DOM mounting target
 * @param upstream returns the [Flow] with the [Patch]es of [WithDomNode]s that should be mounted at this point
 */
fun <N : Node> mountDomNodePatch(
    job: Job,
    target: N,
    upstream: Flow<Patch<WithDomNode<N>>>
) {
    mountSingle(job, upstream) { patch, _ ->
        when (patch) {
            is Patch.Insert -> target.insert(patch.element, patch.index)
            is Patch.InsertMany -> target.insertMany(patch.elements, patch.index)
            is Patch.Delete -> target.delete(patch.start, patch.count)
            is Patch.Move -> target.move(patch.from, patch.to)
        }
    }
}

/**
 * Inserts or appends elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param child Node to insert or append
 * @param index place to insert or append
 */
fun <N : Node> N.insertOrAppend(child: Node, index: Int) {
    if (index == childNodes.length) appendChild(child)
    else childNodes.item(index)?.let {
        insertBefore(child, it)
    }
}

/**
 * Inserts or appends elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param element from type [WithDomNode]
 * @param index place to insert or append
 */
fun <N: Node> N.insert(element: WithDomNode<N>, index: Int): Unit = insertOrAppend(element.domNode, index)

/**
 * Inserts a [List] of elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param elements [List] of [WithDomNode]s elements to insert
 * @param index place to insert or append
 */
fun <N: Node> N.insertMany(elements: List<WithDomNode<N>>, index: Int) {
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

/**
 * Deletes elements from the DOM.
 *
 * @receiver target DOM-Node
 * @param start position for deleting
 * @param count of elements to delete
 */
fun <N: Node> N.delete(start: Int, count: Int) {
    var itemToDelete = childNodes.item(start)
    repeat(count) {
        itemToDelete?.let {
            itemToDelete = it.nextSibling
            removeChild(it)
        }
    }
}

/**
 * Moves elements from on place to another in the DOM.
 *
 * @receiver target DOM-Node
 * @param from position index
 * @param to position index
 */
fun <N: Node> N.move(from: Int, to: Int) {
    val itemToMove = childNodes.item(from)
    if (itemToMove != null) insertOrAppend(itemToMove, to)
}

/**
 * Occurs when the targeted html element is not present in document.
 *
 * @param targetId id which used for mounting
 */
class MountTargetNotFoundException(targetId: String) :
    Exception("html document contains no element with id: $targetId")

/**
 * Mounts a [List] of [Tag]s to a constant element in the static html file.
 *
 * @param targetId id of the element to mount to
 * @receiver the [Flow] to mount to this element
 * @throws MountTargetNotFoundException if target element with [targetId] not found
 */
fun List<Tag<HTMLElement>>.mount(targetId: String) {
    document.getElementById(targetId)?.let { parent ->
        parent.removeChildren()
        this.forEach { parent.appendChild(it.domNode) }
    } ?: throw MountTargetNotFoundException(targetId)
}

/**
 * Mounts a [Tag] to a constant element in the static html file.
 *
 * @param targetId id of the element to mount to
 * @receiver the [Tag] to mount to this element
 * @throws MountTargetNotFoundException if target element with [targetId] not found
 */
fun <E: Element> Tag<E>.mount(targetId: String) {
    document.getElementById(targetId)?.let { parent ->
        parent.removeChildren()
        parent.appendChild(this.domNode)
    } ?: throw MountTargetNotFoundException(targetId)
}

/**
 * Appends one or more [List]s of [Tag]s to the content of a constant element.
 *
 * @param targetId id of the element to mount to
 * @param tagLists the [List]s of [Tag]s to mount to this element
 * @throws MountTargetNotFoundException if target element with [targetId] not found
 */
fun append(targetId: String, vararg tagLists: List<Tag<HTMLElement>>) {
    window.document.getElementById(targetId)?.let { parent ->
        for(tagList in tagLists)
            for(tag in tagList) parent.appendChild(tag.domNode)
    } ?: throw MountTargetNotFoundException(targetId)
}

/**
 * Appends one or more static [Tag]s to an elements content.
 *
 * @param targetId id of the element to mount to
 * @param tags [Tag]s to append
 */
fun <X : Element> append(targetId: String, vararg tags: Tag<X>) {
    window.document.getElementById(targetId)?.let { parent ->
        tags.forEach { tag -> parent.appendChild(tag.domNode) }
    } ?: throw MountTargetNotFoundException(targetId)
}

/**
 * Appends one or more static [Tag]s to the document's body.
 *
 * @param tags [Tag]s to append
 */
fun <X : Element> appendToBody(vararg tags: Tag<X>) {
    window.document.getElementsByTagName("body").item(0)?.let { element ->
        tags.forEach { tag -> element.appendChild(tag.domNode) }
    } ?: throw MountTargetNotFoundException("body")
}