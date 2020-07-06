package dev.fritz2.binding

import dev.fritz2.flow.asSharedFlow
import dev.fritz2.lenses.Lens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MapStore<P, T>(private val parent: Store<P>,
                     private val mapper: StoreMapper<P,T>): Store<T>()
{
    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val id: String by lazy { "${parent.id}.${mapper._id}" }

    /**
     * Since a [MapStore] is just a view on a [SubStore] holding the reference to the [RootStore], it forwards the [Update] to its parent of type [SubStore].
     */
    override suspend fun enqueue(update: Update<T>) {
        parent.enqueue {
            mapper.apply(it, update)
        }
    }

    /**
     * the current value of the [MapStore] is derived from the data of it's parent using the given [StoreMapper].
     */
    override val data: Flow<T> = parent.data.map {
        mapper.get(it)
    }.distinctUntilChanged().asSharedFlow()


}
