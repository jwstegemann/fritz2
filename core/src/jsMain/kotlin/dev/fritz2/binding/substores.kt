package dev.fritz2.binding

import dev.fritz2.flow.asSharedFlow
import dev.fritz2.format.Format
import dev.fritz2.format.FormatStore
import dev.fritz2.lenses.Lens
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
    internal val rootStore: RootStore<R>,
    internal val rootLens: Lens<R, T>
) : Store<T>() {

    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val id: String by lazy { "${parent.id}.${lens._id}" }

    /**
     * Since a [SubStore] is just a view on a [RootStore] holding the real value, it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: Update<T>) {
        rootStore.enqueue {
            rootLens.apply(it, update)
        }
    }

    /**
     * the current value of the [SubStore] is derived from the data of it's parent using the given [Lens].
     */
    override val data: Flow<T> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged().asSharedFlow()

    /**
     * factory-method to create another [SubStore] using this one as it's parent.
     *
     * @param lens a [Lens] describing which part to create the [SubStore] for
     */
    fun <X> sub(lens: Lens<T, X>): SubStore<R, T, X> = SubStore(this, lens, rootStore, rootLens + lens)

    /**
     * a factory-method to create a [FormatStore] from this [Store] using the given [Format] to convert the current value as well as [Update]s
     */
    infix fun using(format: Format<T>) = FormatStore(this, rootStore, rootLens, format)

    infix fun <X> mapWith(mapper: StoreMapper<T, X>) = MapStore(this, rootStore, mapper)
}