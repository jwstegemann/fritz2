package io.fritz2.binding

import io.fritz2.flow.asSharedFlow
import io.fritz2.optics.Lens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@FlowPreview
@ExperimentalCoroutinesApi
class SubStore<R, P, T>(private val parent: Store<P>, private val lens: Lens<P, T>, val rootStore: RootStore<R>, val rootLens: Lens<R,T>) : Store<T>() {

    override val id: String by lazy { "${parent.id}.${lens._id}" }

    override suspend fun enqueue(update: Update<T>) {
        rootStore.enqueue {
            rootLens.apply(it, update)
        }
    }

    //FIMXE: Should this be a shared flow (less calls to getters) or not (overhead of shared flow counting references, mem-leak?)
    override val data: Flow<T> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged().asSharedFlow()

    override fun <X> sub(lens: Lens<T,X>): SubStore<R,T,X> = SubStore(this, lens, rootStore, this.rootLens + lens)

}