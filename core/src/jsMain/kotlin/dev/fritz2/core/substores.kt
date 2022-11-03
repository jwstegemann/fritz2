package dev.fritz2.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A [Store] that is derived from a parent [Store] mapping its data in both ways by a given [Lens].
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
     * Since a [SubStore] is just a view on a [parent] [Store] holding the real value,
     * it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: Update<D>) {
        parent.enqueue { lens.apply(it, update) }
    }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = handle<D> { _, newValue -> newValue }

    /**
     * the current value of the [Store] is derived from the data of it's parent using the given [Lens].
     */
    override val data: Flow<D> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged()

    override fun errorHandler(cause: Throwable) {
        parent.errorHandler(cause)
    }

}

/**
 * Creates a new [Store] containing the element for the given [element] and [idProvider] from the original [Store]'s [List].
 *
 * @param element current instance of the entity to focus on
 * @param idProvider to identify the same entity (i.e. when it's content changed)
 */
fun <D, I> Store<List<D>>.sub(element: D, idProvider: IdProvider<D, I>): Store<D> =
    SubStore(this, lensOf(element, idProvider))

/**
 * Creates a new [Store] containing the element for the given [index] from the original [Store]'s [List]
 *
 * @param index position in the list to point to
 */
fun <D> Store<List<D>>.sub(index: Int): Store<D> =
    SubStore(this, lensOf(index))

/**
 * Creates a new [Store] containing the corresponding value for the given [key] from the original [Store]'s [Map].
 *
 * @param key in the map to point to
 */
fun <K, V> Store<Map<K, V>>.sub(key: K): Store<V> =
    SubStore(this, lensOf(key))

/**
 * on a [Store] of nullable data this creates a [Store] with a nullable parent and non-nullable value.
 * It can be called using a [Lens] on a non-nullable parent (that can be created by using the @[Lenses]-annotation),
 * but you have to ensure, that the resulting [Store] is never used, when it's parent's value is null.
 * Otherwise, a [NullPointerException] is thrown.
 *
 * @param lens [Lens] to use to create the [Store]
 */
fun <P, T> Store<P?>.sub(lens: Lens<P & Any, T>): Store<T> =
    sub(lens.toNullableLens())

/**
 * on a [Store] of nullable data this creates a [Store] with a nullable parent and non-nullable value.
 * It can be called using a [Lens] on a non-nullable parent (that can be created by using the @[Lenses]-annotation),
 * but you have to provide a [default] value. When updating the value of the resulting [Store] to this [default] value,
 * null is used instead updating the parent. When this [Store]'s value would be null according to it's parent's
 * value, the [default] value will be used instead.
 *
 * @param default value to translate null to and from
 */
fun <T> Store<T?>.orDefault(default: T): Store<T> =
    sub(defaultLens(this.id, default))