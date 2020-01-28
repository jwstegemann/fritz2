package io.fritz2.binding

import io.fritz2.optics.elementLens
import io.fritz2.optics.withId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

fun <T : withId> Store<List<T>>.sub(element: T): SubStore<List<T>,List<T>,T> {
    val lens = elementLens(element)
    return SubStore<List<T>,List<T>,T>(this, lens, this, lens)
}

fun <R,P,T : withId> SubStore<R,P, List<T>>.sub(element: T): SubStore<R,List<T>,T> {
    val lens = elementLens(element)
    return SubStore<R,List<T>,T>(this, lens, rootStore, rootLens + lens)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <T : withId> Store<List<T>>.eachStore(): Seq<SubStore<List<T>, List<T>, T>> = this.each().mapItems {
    sub(it)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <R,P,T : withId> SubStore<R,P,List<T>>.eachStore(): Seq<SubStore<R, List<T>, T>> = this.each().mapItems {
    sub(it)
}
