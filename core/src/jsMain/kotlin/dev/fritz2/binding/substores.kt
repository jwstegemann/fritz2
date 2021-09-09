package dev.fritz2.binding

import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.elementLens
import dev.fritz2.lenses.positionLens
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A [Store] that is derived from your [RootStore] or another [SubStore] that represents a part of the data-model of it's parent.
 * Use the .sub-factory-method on the parent [Store] to create it.
 */
class SubStore<P, T>(
    val parent: Store<P>,
    private val lens: Lens<P, T>
) : Store<T> {
    /**
     * [Job] used as parent job on all coroutines started in [Handler]s in the scope of this [Store]
     */
    override val job: Job = Job()


    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val id: String by lazy { "${parent.id}.${lens.id}".trimEnd('.') }

    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val path: String by lazy { "${parent.path}.${lens.id}".trimEnd('.') }

    /**
     * represents the current value of the [Store]
     */
    override val current: T
        get() = lens.get(parent.current)

    /**
     * Since a [SubStore] is just a view on a [RootStore] holding the real value, it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: QueuedUpdate<T>) {
        parent.enqueue(QueuedUpdate({
            try {
                lens.apply(it, update.update)
            } catch (e: Throwable) {
                lens.apply(it) { oldValue -> update.errorHandler(e, oldValue) }
            }
        }, parent::errorHandler))
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

}


/**
 * creates a [SubStore] using a [RootStore] as parent using a given [IdProvider].
 *
 * @param element current instance of the entity to focus on
 * @param id to identify the same entity (i.e. when it's content changed)
 */
fun <T, I> Store<List<T>>.sub(element: T, id: IdProvider<T, I>): SubStore<List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens)
}

/**
 * creates a [SubStore] using a [RootStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <T> Store<List<T>>.sub(index: Int): SubStore<List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens)
}
