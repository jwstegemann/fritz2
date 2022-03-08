package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A [Store] that is derived from your [RootStore] or another [SubStore] that represents a part of the data-model of its parent.
 * Use the .sub-factory-method on the parent [Store] to create it.
 */
class SubStore<P, D>(
    val parent: Store<P>,
    private val lens: Lens<P, D>
) : Store<D> {

    //TODO: investigate, if you can use Job from parent instead
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
    override val current: D
        get() = lens.get(parent.current)

    /**
     * Since a [SubStore] is just a view on a [RootStore] holding the real value, it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: Update<D>) {
        parent.enqueue { lens.apply(it, update) }
    }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = handle<D> { _, newValue -> newValue }

    /**
     * the current value of the [SubStore] is derived from the data of it's parent using the given [Lens].
     */
    override val data: Flow<D> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged()

}


/**
 * creates a [SubStore] using a [RootStore] as parent using a given [IdProvider].
 *
 * @param element current instance of the entity to focus on
 * @param id to identify the same entity (i.e. when it's content changed)
 */
fun <D, I> Store<List<D>>.sub(element: D, id: IdProvider<D, I>): SubStore<List<D>, D> {
    val lens = lensOf(element, id)
    return SubStore(this, lens)
}

/**
 * creates a [SubStore] using a [RootStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <D> Store<List<D>>.sub(index: Int): SubStore<List<D>, D> {
    val lens = lensOf<D>(index)
    return SubStore(this, lens)
}
