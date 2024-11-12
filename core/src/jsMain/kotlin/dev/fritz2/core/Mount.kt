package dev.fritz2.core

import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
 * External interface to access the MountPoint where the lifecycle of [Tag]s and subtrees is handled.
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


    private val mutex = Mutex()

    suspend fun runBeforeUnmounts() = withContext(NonCancellable) {
        mutex.withLock {
            beforeUnmountListeners.forEach {
                try {
                    it.handler(it.target, it.payload)
                } catch (e: Exception) {
                    console.error("Error in beforeUnmounts", e)
                }
            }
            beforeUnmountListeners.clear()
        }
    }


    suspend fun runAfterMounts() = withContext(NonCancellable) {
        afterMountListeners.forEach {
            try {
                it.handler(it.target, it.payload)
            } catch (e: Exception) {
                console.error("Error in afterMounts", e)
            }
        }
        afterMountListeners.clear()
    }

    private val afterMountListeners: MutableList<DomLifecycleListener> = mutableListOf()

    private val beforeUnmountListeners: MutableList<DomLifecycleListener> = mutableListOf()

    override fun afterMount(target: WithDomNode<Element>, payload: Any?, handler: DomLifecycleHandler) {
        afterMountListeners.add(DomLifecycleListener(target, payload, handler))
    }

    override fun beforeUnmount(target: WithDomNode<Element>, payload: Any?, handler: DomLifecycleHandler) {
        beforeUnmountListeners.add(DomLifecycleListener(target, payload, handler))
    }
}

val MOUNT_POINT_KEY = Scope.Key<MountPoint>("MOUNT_POINT")

/**
 * Allows to access the nearest [MountPoint] from any [WithScope]
 */
fun WithScope.mountPoint(): MountPoint? = this.scope[MOUNT_POINT_KEY]

/**
 * Convenience method to register lifecycle handler for after a [Tag] is mounted
 *
 * @param handler [DomLifecycleHandler] to be called on this [Tag] after it is mounted to the DOM
 * @param payload optional payload the handler requires
 * @receiver the [Tag] to register the lifecycle handler for
 */
fun <T : Element> Tag<T>.afterMount(payload: Any? = null, handler: DomLifecycleHandler) {
    mountPoint()?.afterMount(this, payload, handler)
}

/**
 * Convenience method to register lifecycle handler for before a [Tag] is unmounted
 *
 * @param handler [DomLifecycleHandler] to be called on this [Tag] before it is removed from the DOM
 * @param payload optional payload the handler requires
 * @receiver the [Tag] to register the lifecycle handler for
 */
fun <T : Element> Tag<T>.beforeUnmount(payload: Any? = null, handler: DomLifecycleHandler) {
    mountPoint()?.beforeUnmount(this, payload, handler)
}

internal class MountContext<T : HTMLElement>(
    override val job: Job,
    val target: Tag<T>,
    mountScope: Scope = target.scope,
) : Tag<HTMLElement>, MountPointImpl() {

    override val domNode: HTMLElement = target.domNode
    override val id = target.id
    override val baseClass = target.baseClass
    override fun className(value: String) = target.className(value)
    override fun className(value: Flow<String>, initial: String) = target.className(value, initial)

    override val annex: RenderContext = target.annex

    override val scope: Scope = Scope(mountScope).apply { set(MOUNT_POINT_KEY, this@MountContext) }

    override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
        return target.register(element, content)
    }

    init {
        target.beforeUnmount { _, _ -> runBeforeUnmounts() }
    }
}

internal class BuildContext(
    override val job: Job,
    target: Tag<*>,
    mountScope: Scope,
) : RenderContext, MountPointImpl() {

    override val scope: Scope = Scope(mountScope).apply { set(MOUNT_POINT_KEY, this@BuildContext) }

    override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
        content(element)
        return element
    }

    init {
        target.beforeUnmount { _, _ -> runBeforeUnmounts() }
    }
}

/**
 * Defines the dedicated style class, that any mount-point will get by default.
 * Its only purpose is to exclude the mount-point tag to appear in the visual representation of the DOM.
 */
const val MOUNT_POINT_STYLE_CLASS = "mount-point"

internal val SET_MOUNT_POINT_DATA_ATTRIBUTE: Tag<*>.() -> Unit = {
    attr("data-mount-point", true)
}

/**
 * collects the values of a given [Flow] one by one.
 * Use this for data-types that represent a single (simple or complex) value.
 *
 * @param parentJob parent Job for starting a new coroutine
 * @param upstream returns the Flow that should be mounted at this point
 * @param collect function which getting called when values are changing (rerender)
 */
inline fun <T> mountSimple(parentJob: Job, upstream: Flow<T>, crossinline collect: suspend (T) -> Unit) {
    (MainScope() + parentJob).launch(start = CoroutineStart.UNDISPATCHED) {
        upstream.distinctUntilChanged().mapLatest { collect(it);it }.catch {
            when (it) {
                is CollectionLensGetException -> {}
                else -> console.error(it)
            }
            // do not do anything here but canceling the coroutine, because this is an expected
            // behaviour when dealing with filtering, renderEach and idProvider
            cancel("error mounting", it)
        }.collect()
    }
}

/**
 * Mounts a [Flow] of [Patch]es to the DOM either
 *  - creating a new context-Div as a child of the receiver
 *  - or, if [into] is set, replacing all children of this [Tag].
 *
 * @param into if set defines the target to mount the content to (replacing its static content)
 * @param upstream the [Flow] that should be mounted
 * @param batch hide [into] while rendering patches. Useful to avoid flickering when you make many changes (like sorting)
 * @param createPatches lambda defining, how to compare two versions of a [List]
 */
internal fun <V> RenderContext.mountPatches(
    into: Tag<HTMLElement>?,
    upstream: Flow<List<V>>,
    batch: Boolean,
    createPatches: Tag<HTMLElement>.(Flow<List<V>>, MutableMap<Node, MountPointImpl>) -> Flow<List<Patch<Tag<HTMLElement>>>>,
) {
    val target = into?.apply {
        this.domNode.clear()
        SET_MOUNT_POINT_DATA_ATTRIBUTE()
    } ?: div(MOUNT_POINT_STYLE_CLASS, content = SET_MOUNT_POINT_DATA_ATTRIBUTE)

    val mountPoints = mutableMapOf<Node, MountPointImpl>()

    mountSimple(
        target.job,
        createPatches(target, upstream.onEach { if (batch) target.inlineStyle("visibility: hidden;") }, mountPoints)
    ) { patches ->
        withContext(NonCancellable) {
            patches.forEach { patch ->
                when (patch) {
                    is Patch.Insert -> insert(target.domNode, mountPoints, patch.element, patch.index)
                    is Patch.InsertMany -> insertMany(target.domNode, mountPoints, patch.elements, patch.index)
                    is Patch.Delete -> delete(target.domNode, mountPoints, patch.start, patch.count)
                    is Patch.Move -> move(target.domNode, patch.from, patch.to)
                }
            }
        }
        if (batch) {
            kotlinx.browser.window.awaitAnimationFrame()
            target.inlineStyle("")
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
private fun insertOrAppend(target: Node, child: Node, index: Int) {
    if (index == target.childNodes.length) target.appendChild(child)
    else target.childNodes.item(index)?.let {
        target.insertBefore(child, it)
    }
}

/**
 * Inserts or appends elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param element from type [WithDomNode]
 * @param index place to insert or append
 */
private suspend inline fun insert(
    target: Node,
    mountPoints: MutableMap<Node, MountPointImpl>,
    element: WithDomNode<*>,
    index: Int
) {
    insertOrAppend(target, element.domNode, index)
    mountPoints[element.domNode]?.runAfterMounts()
}

/**
 * Inserts a [List] of elements to the DOM.
 *
 * @receiver target DOM-Node
 * @param elements [List] of [WithDomNode]s elements to insert
 * @param index place to insert or append
 */
private suspend inline fun insertMany(
    target: Node,
    mountPoints: MutableMap<Node, MountPointImpl>,
    elements: List<WithDomNode<*>>,
    index: Int
) {
    val f = document.createDocumentFragment()
    for (child in elements) {
        f.append(child.domNode)
        mountPoints[child.domNode]?.runAfterMounts()
    }
    insertOrAppend(target, f, index)
}

/**
 * Deletes elements from the DOM.
 *
 * @receiver target DOM-Node
 * @param start position for deleting
 * @param count of elements to delete
 */
private suspend inline fun delete(target: Node, mountPoints: MutableMap<Node, MountPointImpl>, start: Int, count: Int) {
    var itemToDelete = target.childNodes.item(start)
    repeat(count) {
        itemToDelete?.let {
            itemToDelete = it.nextSibling
            mountPoints.remove(it)?.let { mountPoint ->
                mountPoint.runBeforeUnmounts()
                mountPoint.job.cancelChildren()
                target.removeChild(it)
            }
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
private fun move(target: Node, from: Int, to: Int) {
    val itemToMove = target.childNodes.item(from)
    if (itemToMove != null) insertOrAppend(target, itemToMove, to)
}

