package io.fritz2.binding

import io.fritz2.optics.Lens
import io.fritz2.optics.elementLens
import io.fritz2.optics.WithId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun <T : WithId> RootStore<List<T>>.sub(element: T): SubStore<List<T>,List<T>,T> {
    val lens = elementLens(element)
    return SubStore(this, lens, this, lens)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <R,P,T : WithId> SubStore<R,P, List<T>>.sub(element: T): SubStore<R,List<T>,T> {
    val lens = elementLens(element)
    return SubStore(this, lens, rootStore, rootLens + lens)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <T : WithId> RootStore<List<T>>.eachStore(): Seq<SubStore<List<T>, List<T>, T>> = this.data.each().map {
    sub(it)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <R,P,T : WithId> SubStore<R,P,List<T>>.eachStore(): Seq<SubStore<R, List<T>, T>> = this.data.each().map {
    sub(it)
}
