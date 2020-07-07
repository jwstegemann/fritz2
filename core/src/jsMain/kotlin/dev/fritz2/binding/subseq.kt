package dev.fritz2.binding

import dev.fritz2.lenses.elementLens
import dev.fritz2.lenses.idProvider

/**
 * factory-method to create a [SubStore] using a [RootStore] as parent.
 */
fun <T> RootStore<List<T>>.sub(element: T, id: idProvider<T>): SubStore<List<T>, List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens, this, lens)
}


/**
 * factory-method to create a [SubStore] using another [SubStore] as parent.
 */
fun <R, P, T> SubStore<R, P, List<T>>.sub(element: T, id: idProvider<T>): SubStore<R, List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens, rootStore, rootLens + lens)
}


/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List].
 * You can also call [eachElement] and inside it's lambda create the [SubStore] using [sub].
 */
fun <T> RootStore<List<T>>.eachStore(id: idProvider<T>): Seq<SubStore<List<T>, List<T>, T>> =
    this.data.eachElement().map {
        sub(it, id)
    }


/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List].
 * You can also call [eachElement] and inside it's lambda create the [SubStore] using [sub].
 */
fun <R, P, T> SubStore<R, P, List<T>>.eachStore(id: idProvider<T>): Seq<SubStore<R, List<T>, T>> =
    this.data.eachIndex().map {
        sub(it, id)
    }
