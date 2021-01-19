package dev.fritz2.dom

import dev.fritz2.binding.Patch
import dev.fritz2.binding.mountSingle
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Comment
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
 * @param upstream [Flow] of [List] of [Patch]es of [WithDomNode]s that should be mounted at this point
 * @cancelJob lambda expression to cancel coroutines when not needed anymore
 */
fun <N : Node> mountDomNodePatch(
    job: Job,
    target: N,
    upstream: Flow<List<Patch<WithDomNode<N>>>>,
    cancelJob: (Node) -> Unit
) {
    mountSingle(job, upstream) { patches, _ ->
        patches.forEach { patch ->
            when (patch) {
                is Patch.Insert -> target.insert(patch.element, patch.index)
                is Patch.InsertMany -> target.insertMany(patch.elements, patch.index)
                is Patch.Delete -> target.delete(patch.start, patch.count, cancelJob)
                is Patch.Move -> target.move(patch.from, patch.to)
            }
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
private fun <N : Node> N.insertOrAppend(child: Node, index: Int) {
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
private fun <N : Node> N.insert(element: WithDomNode<N>, index: Int): Unit = insertOrAppend(element.domNode, index)

/**
 * Inserts a [List] of elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param elements [List] of [WithDomNode]s elements to insert
 * @param index place to insert or append
 */
private fun <N : Node> N.insertMany(elements: List<WithDomNode<N>>, index: Int) {
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
private fun <N : Node> N.delete(start: Int, count: Int, cancelJob: (Node) -> Unit) {
    var itemToDelete = childNodes.item(start)
    repeat(count) {
        itemToDelete?.let {
            cancelJob(it)
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
private fun <N : Node> N.move(from: Int, to: Int) {
    val itemToMove = childNodes.item(from)
    if (itemToMove != null) insertOrAppend(itemToMove, to)
}
