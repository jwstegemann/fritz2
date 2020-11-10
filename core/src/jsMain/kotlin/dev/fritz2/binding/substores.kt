package dev.fritz2.binding

import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.elementLens
import dev.fritz2.lenses.positionLens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A [Store] that is derived from your [RootStore] or another [SubStore] that represents a part of the data-model of it's parent.
 * Use the .sub-factory-method on the parent [Store] to create it.
 */
class SubStore<R, P, T>(
    private val parent: Store<P>,
    private val lens: Lens<P, T>,
    val root: Store<R>,
    internal val rootLens: Lens<R, T>
) : Store<T>, CoroutineScope by MainScope() {

    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val id: String by lazy { "${parent.id}.${lens.id}".trimEnd('.') }

    /**
     * represents the current value of the [Store]
     */
    override val current: T
        get() = lens.get(parent.current)

    /**
     * Since a [SubStore] is just a view on a [RootStore] holding the real value, it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: QueuedUpdate<T>) {
        root.enqueue(QueuedUpdate({
            try {
                rootLens.apply(it, update.update)
            } catch (e: Throwable) {
                rootLens.apply(it, { oldValue -> update.errorHandler(e, oldValue) })
            }
        }, root::errorHandler))
    }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = handle<T> { _, newValue -> newValue }

    /**
     * the current value of the [SubStore] is derived from the data of it's parent using the given [Lens].
     */
    override val data: Flow<T> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged()
    //TODO: sharedFlow

    /**
     * the current value of the [SubStore] is derived from the data of it's parent using the given [Lens].
     */
//    override val value : T
//        get() = parent.data.map {
//        lens.get(it)
//    }.distinctUntilChanged()

    /**
     * creates a new [SubStore] using this one as it's parent.
     *
     * @param lens a [Lens] describing which part to create the [SubStore] for
     */
    fun <X> sub(lens: Lens<T, X>): SubStore<R, T, X> =
        SubStore(this, lens, root, rootLens + lens)
}

/**
 * creates a [SubStore] using a [RootStore] as parent using a given [IdProvider].
 *
 * @param element current instance of the entity to focus on
 * @param id to identify the same entity (i.e. when it's content changed)
 */
fun <T, I> RootStore<List<T>>.sub(element: T, id: IdProvider<T, I>): SubStore<List<T>, List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens, this, lens)
}

/**
 * creates a [SubStore] using a [RootStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <T> RootStore<List<T>>.sub(index: Int): SubStore<List<T>, List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens, this, lens)
}

/**
 * creates a [SubStore] using another [SubStore] as parent using a given [IdProvider].
 *
 * @param element current instance of the entity to focus on
 * @param idProvider to identify the same entity (i.e. when it's content changed)
 */
fun <R, P, T, I> SubStore<R, P, List<T>>.sub(element: T, idProvider: IdProvider<T, I>): SubStore<R, List<T>, T> {
    val lens = elementLens(element, idProvider)
    return SubStore(this, lens, root, rootLens + lens)
}

/**
 * creates a [SubStore] using a [SubStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <R, P, T> SubStore<R, P, List<T>>.sub(index: Int): SubStore<R, List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens, root, rootLens + lens)
}