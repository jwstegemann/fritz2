package dev.fritz2.dom

import dev.fritz2.binding.Patch
import dev.fritz2.binding.Store
import dev.fritz2.binding.mountSimple
import dev.fritz2.binding.sub
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.dom.html.TagContext
import dev.fritz2.lenses.IdProvider
import dev.fritz2.utils.Myer
import kotlinx.browser.window
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.*
import kotlinx.dom.clear
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import kotlin.collections.set

/**
 * Creates a [Div] as context for the mounted content using the given job and adds it to the receiver of this function
 * inheriting its scope.
 * Also adds a style-class "mount-point" that adds a "display: contents" property to the created [Div] and
 * an attribute "data-mount-point" to the created [Div].
 *
 * @param mountJob [Job] to use downstream from this mountpoint
 * @receiver TagContext parent to add the [Div] to
 */
inline fun TagContext.mountContext(mountJob: Job) =
    register(Div(baseClass = "mount-point", job = mountJob, scope = this.scope)) {
        it.attr("data-mount-point", true)
    }

/**
 * Implementation of [RenderContext] that forwards all registrations of children to the element it proxies.
 * Also adds "data-mount-point" as a marker-attribute to the element it proxies.
 *
 * @param mountJob [Job] to use downstream from this context
 * @param proxee [Tag] to proxy
 */
class ProxyContext<T : HTMLElement>(
    mountJob: Job,
    proxee: Tag<T>
) : RenderContext(job = mountJob, scope = proxee.scope, domNode = proxee.domNode, tagName = "") {
    init {
        proxee.attr("data-mount-point", true)
    }
}

internal val dummyDom = window.document.createElement("div") as HTMLElement

/**
 * Implementation of [RenderContext] that just renders its children but does not add them anywhere to the Dom.
 *
 * @param mountJob [Job] to use downstream from this context
 * @param mountScope [Scope] to use downstream from this context
 */
class DummyContext(
    mountJob: Job,
    mountScope: Scope,
) : RenderContext(job = mountJob, scope = mountScope, domNode = dummyDom, tagName = "") {
    override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
        content(element)
        return element
    }
}


/**
 * Uses the [content]-lambda to render a subtree for each value on the [upstream]-[Flow] and
 * mounts it to the DOM either
 *  - creating a new context-[Div] as a child of the receiver and adding the content as children to this [Div]
 *  - of, if [into] is set, replacing all children of this [Tag].
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param upstream the [Flow] that should be mounted at this point
 * @param content lambda definining what to render for a given value on [upstream]
 */
@OptIn(InternalCoroutinesApi::class)
inline fun <V> TagContext.mount(
    into: RenderContext?,
    upstream: Flow<V>,
    crossinline content: RenderContext.(V) -> Unit
) {
    val target = if (into != null) ProxyContext(Job(job), into) else mountContext(Job(job))

    mountSimple(this.job, upstream) { data ->
        target.job.cancelChildren()
        target.domNode.clear()
        target.content(data)
    }
}

/**
 * Accumulates a [Pair] and a [List] to a new [Pair] of [List]s
 *
 * @param accumulator [Pair] of two [List]s
 * @param newValue new [List] to accumulate
 */
fun <T> accumulate(
    accumulator: Pair<List<T>, List<T>>,
    newValue: List<T>
): Pair<List<T>, List<T>> = Pair(accumulator.second, newValue)


/**
 * Compares each new [List] on [upstream] to its predecessor to create [Patch]es using Myer's diff-algorithm.
 * For each element that is newly inserted it uses the [content]-lambda to render a subtree.
 * The resulting [Patch]es are then applied to the DOM either
 *  - creating a new context-[Div] as a child of the receiver
 *  - or, if [into] is set, replacing all children of this [Tag].
 *  Keep in mind, that if you do not offer an [idProvider] a changed value of a list-item will render a new subtree
 *  for this item. When you provide an [idProvider] though, a new subtree will only be rendered if a new id appears
 *  in the list, and you are responsible for updating the dynamic content (by using sub-[Store]s).
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param idProvider optional function to identify a unique entity in the list
 * @param upstream the [Flow] that should be mounted
 * @param content lambda definining what to render for a given value on [upstream]
 */
inline fun <V> TagContext.mount(
    into: RenderContext?,
    upstream: Flow<List<V>>,
    noinline idProvider: IdProvider<V, *>?,
    crossinline content: RenderContext.(V) -> RenderContext
) = mountPatches(into, upstream) { upstream, jobs ->
    upstream.scan(Pair(emptyList(), emptyList()), ::accumulate).map { (old, new) ->
        val diff = if (idProvider != null) Myer.diff(old, new, idProvider) else Myer.diff(old, new)
        diff.map { patch ->
            patch.map(job) { value, newJob ->
                content(DummyContext(newJob, scope), value).also {
                    jobs[it.domNode] = newJob
                }
            }
        }
    }
}

/**
 * Compares each new [List] on [store]'s data-[Flow] to its predecessor to create [Patch]es using Myer's diff-algorithm.
 * For each element that is newly inserted it uses the [content]-lambda to render a subtree.
 * The resulting [Patch]es are then applied to the DOM either
 *  - creating a new context-[Div] as a child of the receiver
 *  - or, if [into] is set, replacing all children of this [Tag].
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param idProvider function to identify a unique entity in the list
 * @param store the [Store] that's values should be mounted at
 * @param content lambda definining what to render for a given value on [store]'s data-[Flow]
 */
inline fun <V> TagContext.mount(
    into: RenderContext?,
    store: Store<List<V>>,
    noinline idProvider: IdProvider<V, *>,
    crossinline content: RenderContext.(Store<V>) -> RenderContext
) = mount(into, store.data, idProvider) { value ->
    content(store.sub(value, idProvider))
}

/**
 * Compares each new [List] on [store]'s data-[Flow] to its predecessor element by element to create [Patch]es.
 * For each element that is newly inserted it uses the [content]-lambda to render a subtree.
 * The resulting [Patch]es are then applied to the DOM either
 *  - creating a new context-[Div] as a child of the receiver
 *  - or, if [into] is set, replacing all children of this [Tag].
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param store the [Store] that's values should be mounted at
 * @param content lambda definining what to render for a given value on [store]'s data-[Flow]
 */
inline fun <V> TagContext.mount(
    into: RenderContext?,
    store: Store<List<V>>,
    crossinline content: RenderContext.(Store<V>) -> RenderContext
) = mountPatches(into, store.data) { upstream, jobs ->
    upstream.map { it.withIndex().toList() }.eachIndex().map { patch ->
        listOf(patch.map(job) { value, newJob ->
            content(DummyContext(newJob, scope), store.sub(value.index)).also {
                jobs[it.domNode] = newJob
            }
        })
    }
}


/**
 * Compares each new [List] on a [Flow] to its predecessor element by element to create [Patch]es.
 *
 * @return [Flow] of [Patch]es
 */
fun <V> Flow<List<V>>.eachIndex(): Flow<Patch<V>> =
    this.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
        val oldSize = old.size
        val newSize = new.size
        when {
            oldSize < newSize -> flowOf<Patch<V>>(
                Patch.InsertMany(
                    new.subList(oldSize, newSize).reversed(),
                    oldSize
                )
            )
            oldSize > newSize -> flowOf<Patch<V>>(Patch.Delete(newSize, (oldSize - newSize)))
            else -> emptyFlow()
        }
    }


/**
 * Mounts a [Flow] of [Patch]es to the DOM either
 *  - creating a new context-[Div] as a child of the receiver
 *  - or, if [into] is set, replacing all children of this [Tag].
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param upstream the [Flow] that should be mounted
 * @param createPatches lambda defining, how to compare two versions of a [List]
 */
inline fun <V> TagContext.mountPatches(
    into: RenderContext?,
    upstream: Flow<List<V>>,
    crossinline createPatches: (Flow<List<V>>, MutableMap<Node, Job>) -> Flow<List<Patch<RenderContext>>>,
) {
    val target = if (into != null) ProxyContext(job, into) else mountContext(job)
    val jobs = mutableMapOf<Node, Job>()

    mountSimple(target.job, createPatches(upstream, jobs)) { patches ->
        patches.forEach { patch ->
            when (patch) {
                is Patch.Insert -> target.domNode.insert(patch.element, patch.index)
                is Patch.InsertMany -> target.domNode.insertMany(patch.elements, patch.index)
                is Patch.Delete -> target.domNode.delete(patch.start, patch.count) { node ->
                    val job = jobs.remove(node)
                    if (job != null) job.cancelChildren()
                    else console.error("could not cancel renderEach-jobs!")
                }
                is Patch.Move -> target.domNode.move(patch.from, patch.to)
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
