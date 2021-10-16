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


class MountContext(
    override val job: Job,
    override val scope: Scope,
) : Div(job = job, scope = scope, baseClass = "mount-point") {
    init {
        attr("data-mount-point", true)
    }
}

class ProxyContext<T : HTMLElement>(
    override val job: Job,
    override val scope: Scope,
    private val proxee: WithDomNode<T>
) : RenderContext(job = job, scope = scope, domNode = proxee.domNode, tagName = "") {
    init {
        attr("data-mount-point", true)
    }
}


internal val dummyDom = window.document.createElement("div") as HTMLElement

class DummyContext(
    override val job: Job,
    override val scope: Scope,
) : RenderContext(job = job, scope = scope, domNode = dummyDom, tagName = "") {
    override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
        content(element)
        return element
    }
}


@OptIn(InternalCoroutinesApi::class)
inline fun <V> TagContext.mount(
    target: RenderContext?,
    upstream: Flow<V>,
    crossinline content: suspend RenderContext.(V) -> Unit
): RenderContext {
    val newJob = Job(target?.job ?: this.job)

    val doMount = { into: RenderContext ->
        mountSimple(target?.job ?: this.job, upstream) { data ->
            newJob.cancelChildren()
            into.domNode.clear()
            into.content(data)
        }
    }

    return target?.also { doMount(ProxyContext(target.job, target.scope, it)) }
        ?: register(MountContext(job = this.job, scope = scope)) { doMount(it) }
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


inline fun <V> TagContext.mount(
    target: RenderContext?,
    upstream: Flow<List<V>>,
    noinline idProvider: IdProvider<V, *>?,
    crossinline content: RenderContext.(V) -> RenderContext
): RenderContext = mountPatches(target, upstream) { upstream, jobs ->
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

inline fun <V> TagContext.mount(
    target: RenderContext?,
    store: Store<List<V>>,
    noinline idProvider: IdProvider<V, *>,
    crossinline content: RenderContext.(Store<V>) -> RenderContext
): RenderContext = mount(target, store.data, idProvider) { value ->
    content(store.sub(value, idProvider))
}


inline fun <V> TagContext.mount(
    target: RenderContext?,
    store: Store<List<V>>,
    crossinline content: RenderContext.(Store<V>) -> RenderContext
): RenderContext = mountPatches(target, store.data) { upstream, jobs ->
    upstream.map { it.withIndex().toList() }.eachIndex().map { patch ->
        listOf(patch.map(job) { value, newJob ->
            content(DummyContext(newJob, scope), store.sub(value.index)).also {
                jobs[it.domNode] = newJob
            }
        })
    }
}


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


inline fun <V> TagContext.mountPatches(
    target: RenderContext?,
    upstream: Flow<List<V>>,
    crossinline createPatches: (Flow<List<V>>, MutableMap<Node, Job>) -> Flow<List<Patch<RenderContext>>>,
): RenderContext {
    val doMount = { into: RenderContext ->
        val jobs = mutableMapOf<Node, Job>()

        mountSimple(target?.job ?: this.job, createPatches(upstream, jobs)) { patches ->
            patches.forEach { patch ->
                when (patch) {
                    is Patch.Insert -> into.domNode.insert(patch.element, patch.index)
                    is Patch.InsertMany -> into.domNode.insertMany(patch.elements, patch.index)
                    is Patch.Delete -> into.domNode.delete(patch.start, patch.count) { node ->
                        val job = jobs.remove(node)
                        if (job != null) job.cancelChildren()
                        else console.error("could not cancel renderEach-jobs!")
                    }
                    is Patch.Move -> into.domNode.move(patch.from, patch.to)
                }
            }
        }
    }

    return target?.also { doMount(ProxyContext(target.job, target.scope, it)) }
        ?: register(MountContext(job = this.job, scope = scope)) { doMount(it) }
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
