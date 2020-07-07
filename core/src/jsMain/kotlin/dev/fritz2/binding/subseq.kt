package dev.fritz2.binding

import dev.fritz2.lenses.elementLens
import dev.fritz2.lenses.idProvider
import dev.fritz2.lenses.positionLens
import kotlinx.coroutines.flow.map

/**
 * factory-method to create a [SubStore] using a [RootStore] as parent using a given [idProvider].
 *
 * @param element current instance of the entity to focus on
 * @param id [idProvider] to identify the same entity (i.e. when it's content changed)
 */
fun <T> RootStore<List<T>>.sub(element: T, id: idProvider<T>): SubStore<List<T>, List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens, this, lens)
}

/**
 * factory-method to create a [SubStore] using a [RootStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <T> RootStore<List<T>>.sub(index: Int): SubStore<List<T>, List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens, this, lens)
}

/**
 * factory-method to create a [SubStore] using another [SubStore] as parent using a given [idProvider].
 *
 * @param element current instance of the entity to focus on
 * @param id [idProvider] to identify the same entity (i.e. when it's content changed)
 */
fun <R, P, T> SubStore<R, P, List<T>>.sub(element: T, id: idProvider<T>): SubStore<R, List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens, rootStore, rootLens + lens)
}

/**
 * factory-method to create a [SubStore] using a [SubStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <R, P, T> SubStore<R, P, List<T>>.sub(index: Int): SubStore<R, List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens, rootStore, rootLens + lens)
}

/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List] using a given [idProvider].
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 *
 * @param id [idProvider] to identify the same entity (i.e. when it's content changed)
 */
fun <T> RootStore<List<T>>.each(id: idProvider<T>): Seq<SubStore<List<T>, List<T>, T>> =
    this.data.each(id).map {
        sub(it, id)
    }

/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List] without [idProvider]
 * using the index in the list (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 */
fun <T> RootStore<List<T>>.each(): Seq<SubStore<List<T>, List<T>, T>> =
    this.data.map { it.withIndex().toList() }.eachIndex().map { (i, _) ->
        sub(i)
    }

/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List] using a given [idProvider]
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 *
 * @param id [idProvider] to identify the same entity (i.e. when it's content changed)
 */
fun <R, P, T> SubStore<R, P, List<T>>.each(id: idProvider<T>): Seq<SubStore<R, List<T>, T>> =
    this.data.each().map {
        sub(it, id)
    }

/**
 * convenience-method to create a [Seq] of [SubStores], one for each element of the [List] without [idProvider]
 * using the index in the list (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 */
fun <R, P, T> SubStore<R, P, List<T>>.each(): Seq<SubStore<R, List<T>, T>> =
    this.data.map { it.withIndex().toList() }.eachIndex().map { (i, _) ->
        sub(i)
    }
