package dev.fritz2.binding

import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.elementLens
import dev.fritz2.remote.Socket
import dev.fritz2.remote.body
import dev.fritz2.repositories.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*

/**
 * creates a new [DetachedStore]
 *
 * @param lens describes, which part of the parent's model a [DetachedStore] is created for
 * @param initialData start value of the new [Store]
 */
fun <T, X> Store<T>.detach(lens: Lens<T, X>, initialData: X) = DetachedStore(initialData, this, lens)

/**
 * creates a new [DetachedStore]s
 *
 * @param element describes, which element of the parent's [List] a [DetachedStore] is created for
 * @param idProvider function to derive a unique id from an instance
 */
fun <T, I> Store<List<T>>.detach(element: T, idProvider: IdProvider<T, I>, initialData: T = element) =
    DetachedStore(initialData, this, elementLens(element, idProvider))


/**
 * A Sub-[Store] that is detached from its parent, i.e. it does not forward updates upstream
 * (but receives and forwards downstream updates).
 * This can be useful, when you want to address a [Handler] different from update at your RootStore
 * to deal with new values of this sub-model (or below).
 *
 * @property initialData start value of this [Store]
 * @property parent parent [Store]
 * @property lens definition, which sub-model is represented by this [Store]
 */
class DetachedStore<T, P>(private val initialData: T, private val parent: Store<P>, private val lens: Lens<P, T>) :
    Store<T>, CoroutineScope by MainScope() {
    private val state = MutableStateFlow(initialData)

    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val id: String by lazy { "${parent.id}.${lens.id}".trimEnd('.') }

    /**
     * represents the current value of the [Store]
     */
    override val current: T
        get() = state.value

    /**
     * Since a [SubStore] is just a view on a [RootStore] holding the real value, it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: QueuedUpdate<T>) {
        state.value = update.update(state.value)
    }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = handle<T> { _, newValue -> newValue }

    /**
     * the current value of the [DetachedStore] is derived from the data of it's parent using the given [Lens].
     */
    override val data: Flow<T> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged()
    //TODO: sharedFlow

    /**
     * creates a new [SubStore] using this one as it's parent.
     *
     * @param lens a [Lens] describing which part to create the [SubStore] for
     */
    fun <X> sub(lens: Lens<T, X>): SubStore<T, T, X> =
        SubStore(this, lens, this, lens)

    /**
     * the current value of the [DetachedStore] is derived from the data of it's parent using the given [Lens].
     */
    val detached: Flow<T> = state

    /**
     * calls a handler on each new detached value of the [Store]
     */
    override fun syncBy(handler: Handler<Unit>) {
        detached.drop(1).map { Unit } handledBy handler
    }

    /**
     * calls a handler on each new detached value of the [Store]
     */
    override fun syncBy(handler: Handler<T>) {
        detached.drop(1) handledBy handler
    }

    override fun <I> syncWith(socket: Socket, resource: Resource<T, I>) {
        val session = socket.connect()
        var last: T? = null
        session.messages.body.map {
            val received = resource.serializer.read(it)
            last = received
            received
        } handledBy update

        detached.drop(1).onEach {
            if (last != it) session.send(resource.serializer.write(it))
        }.watch()
    }
}

fun <T, P, I> DetachedStore<List<T>, P>.syncWith(socket: Socket, resource: Resource<T, I>) {
    val session = socket.connect()
    var last: List<T>? = null
    session.messages.body.map {
        val received = resource.serializer.readList(it)
        last = received
        received
    } handledBy update

    detached.drop(1).onEach {
        if (last != it) session.send(resource.serializer.writeList(it))
    }.watch()
}