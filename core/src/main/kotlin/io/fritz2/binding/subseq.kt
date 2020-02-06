package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

interface WithId {
    val id: String
}

fun <T : WithId> elementLens(element: T): Lens<List<T>, T> = object : Lens<List<T>, T> {
    override fun get(parent: List<T>): T = checkNotNull(parent.find {
        it.id == element.id
    })

    override fun set(parent: List<T>, value: T): List<T> = parent.map {
        if (it.id == value.id) value else it
    }
}

fun <T : WithId> RootStore<List<T>>.sub(element: T): SubStore<List<T>,List<T>,T> {
    val lens = elementLens(element)
    return SubStore<List<T>,List<T>,T>(this, lens, this, lens)
}

fun <R,P,T : WithId> SubStore<R,P, List<T>>.sub(element: T): SubStore<R,List<T>,T> {
    val lens = elementLens(element)
    return SubStore<R,List<T>,T>(this, lens, rootStore, rootLens + lens)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <T : WithId> RootStore<List<T>>.eachStore(): Seq<SubStore<List<T>, List<T>, T>> = this.each().mapItems {
    sub(it)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <R,P,T : WithId> SubStore<R,P,List<T>>.eachStore(): Seq<SubStore<R, List<T>, T>> = this.each().mapItems {
    sub(it)
}
