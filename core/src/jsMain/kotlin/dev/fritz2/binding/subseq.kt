package dev.fritz2.binding

import dev.fritz2.lenses.WithId
import dev.fritz2.lenses.elementLens

/**
 * factory-method to create a [SubStore] using a [RootStore] as parent.
 */
fun <T : WithId> RootStore<List<T>>.sub(element: T): SubStore<List<T>, List<T>, T> {
    val lens = elementLens(element)
    return SubStore(this, lens, this, lens)
}


/**
 * factory-method to create a [SubStore] using another [SubStore] as parent.
 */
fun <R, P, T : WithId> SubStore<R, P, List<T>>.sub(element: T): SubStore<R, List<T>, T> {
    val lens = elementLens(element)
    return SubStore(this, lens, rootStore, rootLens + lens)
}


/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List].
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 */
fun <T : WithId> RootStore<List<T>>.eachStore(): Seq<SubStore<List<T>, List<T>, T>> = this.data.each().map {
    sub(it)
}


/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List].
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 */
fun <R, P, T : WithId> SubStore<R, P, List<T>>.eachStore(): Seq<SubStore<R, List<T>, T>> = this.data.each().map {
    sub(it)
}
