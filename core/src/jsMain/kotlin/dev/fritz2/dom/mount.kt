package dev.fritz2.dom

import dev.fritz2.binding.Patch
import dev.fritz2.binding.mountSimple
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.dom.html.WithJob
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.dom.clear
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

/**
 * Defines type for a handler for lifecycle-events
 */
typealias DomLifecycleHandler = suspend (WithDomNode<Element>, Any?) -> Unit

internal class DomLifecycleListener(
    val target: WithDomNode<Element>,
    val payload: Any? = null,
    val handler: DomLifecycleHandler
)

/**
 * External interface to access the MountPoint where the lifecycle of [HtmlTag]s and subtrees is handled.
 */
interface MountPoint {

    /**
     * Registers a [DomLifecycleHandler] on a given target that ist called right after the target is mounted to the DOM.
     *
     * @param target the element the lifecycle-handler will be registered for
     * @param payload some optional data that might be used by the [handler] to do its work
     * @param handler defines, what to do (with [payload]), when [target] has just been mounted to the DOM
     */
    fun afterMount(target: WithDomNode<Element>, payload: Any? = null, handler: DomLifecycleHandler)

    /**
     * Registers a [DomLifecycleHandler] on a given target that ist called right before the target is removed from the DOM.
     *
     * @param target the element the lifecycle-handler will be registered for
     * @param payload some optional data that might be used by the [handler] to do its work
     * @param handler defines, what to do (with [payload]), when [target] has just been mounted to the DOM
     */
    fun beforeUnmount(target: WithDomNode<Element>, payload: Any? = null, handler: DomLifecycleHandler)
}

internal abstract class MountPointImpl : MountPoint, WithJob {
    suspend fun runBeforeUnmounts() {
        if (beforeUnmountListeners != null) {
            beforeUnmountListeners!!.map {
                (MainScope() + job).launch {
                    it.handler(it.target, it.payload)
                }
            }.joinAll()
            beforeUnmountListeners!!.clear()
        }
    }

    suspend fun runAfterMounts() {
        if (afterMountListeners != null) {
            afterMountListeners!!.map {
                (MainScope() + job).launch {
                    it.handler(it.target, it.payload)
                }
            }
            afterMountListeners!!.clear()
        }
    }

    private var afterMountListeners: MutableList<DomLifecycleListener>? = null

    private var beforeUnmountListeners: MutableList<DomLifecycleListener>? = null

    override fun afterMount(target: WithDomNode<Element>, payload: Any?, handler: DomLifecycleHandler) {
        if (afterMountListeners == null) afterMountListeners = mutableListOf()
        afterMountListeners!!.add(DomLifecycleListener(target, payload, handler))
    }

    override fun beforeUnmount(target: WithDomNode<Element>, payload: Any?, handler: DomLifecycleHandler) {
        if (beforeUnmountListeners == null) beforeUnmountListeners = mutableListOf()
        beforeUnmountListeners!!.add(DomLifecycleListener(target, payload, handler))
    }
}

internal val MOUNT_POINT_KEY = Scope.Key<MountPoint>("MOUNT_POINT")

/**
 * Allows to access the nearest [MountPoint] from any [HtmlTag]
 */
fun HtmlTag<*>.mountPoint(): MountPoint? = this.scope[MOUNT_POINT_KEY]

/**
 * Convenience method to register lifecycle handler for after a [HtmlTag] is mounted
 *
 * @param handler [DomLifecycleHandler] to be called on this [HtmlTag] after it is mounted to the DOM
 * @param payload optional payload the handler requires
 * @receiver the [HtmlTag] to register the lifecycle handler for
 */
fun <T : Element> HtmlTag<T>.afterMount(payload: Any? = null, handler: DomLifecycleHandler) {
    this.scope[MOUNT_POINT_KEY]?.afterMount(this, payload, handler)
}

/**
 * Convenience method to register lifecycle handler for before a [HtmlTag] is unmounted
 *
 * @param handler [DomLifecycleHandler] to be called on this [HtmlTag] before it is removed from the DOM
 * @param payload optional payload the handler requires
 * @receiver the [HtmlTag] to register the lifecycle handler for
 */
fun <T : Element> HtmlTag<T>.beforeUnmount(payload: Any? = null, handler: DomLifecycleHandler) {
    this.scope[MOUNT_POINT_KEY]?.beforeUnmount(this, payload, handler)
}

internal class MountContext<T : HTMLElement>(
    override val job: Job,
    val target: HtmlTag<T>,
    mountScope: Scope = target.scope,
) : RenderContext, MountPointImpl() {

    override val scope: Scope = Scope(mountScope).apply { set(MOUNT_POINT_KEY, this@MountContext) }

    override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
        return target.register(element, content)
    }
}

internal class BuildContext(
    override val job: Job,
    mountScope: Scope,
) : RenderContext, MountPointImpl() {

    override val scope: Scope = Scope(mountScope).apply { set(MOUNT_POINT_KEY, this@BuildContext) }

    override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
        content(element)
        return element
    }
}


internal const val MOUNT_POINT_STYLE_CLASS = "mount-point"
internal val SET_MOUNT_POINT_DATA_ATTRIBUTE: HtmlTag<HTMLElement>.() -> Unit = {
    attr("data-mount-point", true)
}


/**
 * Mounts a [Flow] of [Patch]es to the DOM either
 *  - creating a new context-[Div] as a child of the receiver
 *  - or, if [into] is set, replacing all children of this [HtmlTag].
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param upstream the [Flow] that should be mounted
 * @param createPatches lambda defining, how to compare two versions of a [List]
 */
internal fun <V> RenderContext.mountPatches(
    into: HtmlTag<HTMLElement>?,
    upstream: Flow<List<V>>,
    createPatches: (Flow<List<V>>, MutableMap<Node, MountPointImpl>) -> Flow<List<Patch<HtmlTag<HTMLElement>>>>,
) {
    val target = into?.apply {
        this.domNode.clear()
        SET_MOUNT_POINT_DATA_ATTRIBUTE()
    }
        ?: div(MOUNT_POINT_STYLE_CLASS, content = SET_MOUNT_POINT_DATA_ATTRIBUTE)

    val mountPoints = mutableMapOf<Node, MountPointImpl>()

    mountSimple(target.job, createPatches(upstream, mountPoints)) { patches ->
        patches.forEach { patch ->
            when (patch) {
                is Patch.Insert -> {
                    target.domNode.insert(patch.element, patch.index)
                    val mountPointImpl = mountPoints[patch.element.domNode]
                    if (mountPointImpl != null) mountPointImpl.runAfterMounts()
                    else console.error("No MountPoint found for node ${patch.element}. This should not have happened!")
                }
                is Patch.InsertMany -> {
                    target.domNode.insertMany(patch.elements, patch.index)
                    patch.elements.forEach { element ->
                        val mountPointImpl = mountPoints[element.domNode]
                        if (mountPointImpl != null) mountPointImpl.runAfterMounts()
                        else console.error("No MountPoint found for node $element. This should not have happened!")
                    }
                }
                is Patch.Delete -> target.domNode.delete(patch.start, patch.count, target.job) { node ->
                    val mountPointImpl = mountPoints.remove(node)
                    if (mountPointImpl != null) {
                        mountPointImpl.job.cancelChildren()
                        mountPointImpl.runBeforeUnmounts()
                    } else console.error("No MountPoint found for node $node. This should not have happened!")
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
        for (child in elements) appendChild(child.domNode)
    } else {
        childNodes.item(index)?.let {
            for (child in elements) {
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
suspend fun <N : Node> N.delete(start: Int, count: Int, parentJob: Job, cancelJob: suspend (Node) -> Unit) {
    var itemToDelete = childNodes.item(start)
    repeat(count) {
        itemToDelete?.let {
            //FIXME: get parentJob here?
            (MainScope() + parentJob).launch {
                cancelJob(it)
                removeChild(it)
            }
            itemToDelete = it.nextSibling
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

