package io.fritz2.binding

import io.fritz2.optics.Lens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@FlowPreview
@ExperimentalCoroutinesApi
class SubStore<R, P, T>(private val parent: AbstractStore<P>, private val lens: Lens<P, T>, val rootStore: Store<R>, val rootLens: Lens<R, T>) : AbstractStore<T>() {

    override fun enqueue(update: Update<T>) {
        rootStore.enqueue {
            rootLens.map(it, update)
        }
    }

    override val data: Flow<T> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged()

    override fun <X> sub(lens: Lens<T, X>): SubStore<R,T,X> = SubStore<R,T,X>(this, lens, rootStore, this.rootLens + lens)

}