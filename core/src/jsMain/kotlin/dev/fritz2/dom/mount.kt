package dev.fritz2.dom

import dev.fritz2.binding.Patch
import dev.fritz2.binding.mountSingle
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.dom.html.TagContext
import dev.fritz2.lenses.LensException
import dev.fritz2.utils.Myer
import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.dom.clear
import org.w3c.dom.Comment
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Node


fun <T> mount(parentJob: Job, upstream: Flow<T>, collect: suspend (T) -> Unit) {
    (MainScope() + parentJob).launch(start = CoroutineStart.UNDISPATCHED) {
        upstream.onEach(collect).catch {
            when (it) {
                is LensException -> {
                }
                else -> console.error(it)
            }
            // do not do anything here but canceling the coroutine, because this is an expected
            // behaviour when dealing with filtering, renderEach and idProvider
            cancel("error mounting", it)
        }.collect()
    }
}


//TODO: collect all registered children in a documentfragment first?
class MountPoint(
    override val job: Job,
    override val scope: Scope,
) : Tag<HTMLDivElement>(job = job, scope = scope, tagName = "div", baseClass = "contents")

@OptIn(InternalCoroutinesApi::class)
inline fun <E : Element, T : Tag<E>, V> TagContext.mountPoint(
    parentJob: Job,
    target: Tag<E>,
    upstream: Flow<V>,
    crossinline render: suspend RenderContext.(V) -> Unit
): MountPoint {
    val newJob = Job(parentJob)
    return register(MountPoint(job = newJob, scope = scope)) { mp ->
        mount(parentJob, upstream) { data ->
            newJob.cancelChildren()
            mp.domNode.clear()
            mp.render(data)
        }
    }
}


class DummyContext(override val job: Job, override val scope: Scope) : TagContext {
    override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
        content(element)
        return element
    }
}

//seq
inline fun <E : Element, T : Tag<E>, V> TagContext.mountPoint(
    parentJob: Job,
    target: Tag<E>,
    upstream: Flow<List<V>>,

    crossinline render: TagContext.(V) -> RenderContext
): MountPoint {
    return register(MountPoint(job = job, scope = scope)) { mp ->
        val jobs = mutableMapOf<Node, Job>()
        val patchesFlow = upstream.scan(Pair(emptyList(), emptyList()), Tag.Companion::accumulate).map { (old, new) ->
            Myer.diff(old, new).map { patch ->
                patch.map(job) { value, newJob ->
                    render(DummyContext(newJob, scope), value).also {
                        jobs[it.domNode] = newJob
                    }
                }
            }
        }
        mount(parentJob, patchesFlow) { patches ->
            patches.forEach { patch ->
                when (patch) {
                    is Patch.Insert -> mp.domNode.insert(patch.element, patch.index)
                    is Patch.InsertMany -> mp.domNode.insertMany(patch.elements, patch.index)
                    is Patch.Delete -> mp.domNode.delete(patch.start, patch.count) { node ->
                        val job = jobs.remove(node)
                        if (job != null) job.cancelChildren()
                        else console.error("could not cancel renderEach-jobs!")
                    }
                    is Patch.Move -> mp.domNode.move(patch.from, patch.to)
                }
            }
        }
    }
}


//class Fragment(
//    override val job: Job,
//    override val scope: Scope,
//) : TagContext, WithJob, WithDomNode<DocumentFragment>, WithScope {
//
//    override val domNode: DocumentFragment = document.createDocumentFragment()
//
//    //TODO: move from tag to TagContext
//    override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
//        content(element)
//        domNode.appendChild(element.domNode)
//        return element
//    }
//}

//fun TagContext.fragment(newJob: Job, content: TagContext.() -> Unit) = Fragment(newJob, scope).also(content)

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
fun <N : Node> N.insert(element: WithDomNode<N>, index: Int): Unit = insertOrAppend(element.domNode, index)

/**
 * Inserts a [List] of elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param elements [List] of [WithDomNode]s elements to insert
 * @param index place to insert or append
 */
fun <N : Node> N.insertMany(elements: List<WithDomNode<N>>, index: Int) {
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
fun <N : Node> N.delete(start: Int, count: Int, cancelJob: (Node) -> Unit) {
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
fun <N : Node> N.move(from: Int, to: Int) {
    val itemToMove = childNodes.item(from)
    if (itemToMove != null) insertOrAppend(itemToMove, to)
}
