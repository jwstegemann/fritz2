package dev.fritz2.binding

import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.elementLens
import dev.fritz2.lenses.positionLens
import kotlinx.coroutines.flow.map

/**
 * creates a [SubStore] using a [RootStore] as parent using a given [IdProvider].
 *
 * @param element current instance of the entity to focus on
 * @param id to identify the same entity (i.e. when it's content changed)
 */
fun <T, I> RootStore<List<T>>.sub(element: T, id: IdProvider<T, I>): SubStore<List<T>, List<T>, T> {
    val lens = elementLens(element, id)
    return SubStore(this, lens, this, lens)
}

/**
 * creates a [SubStore] using a [RootStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <T> RootStore<List<T>>.sub(index: Int): SubStore<List<T>, List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens, this, lens)
}

/**
 * creates a [SubStore] using another [SubStore] as parent using a given [IdProvider].
 *
 * @param element current instance of the entity to focus on
 * @param idProvider to identify the same entity (i.e. when it's content changed)
 */
fun <R, P, T, I> SubStore<R, P, List<T>>.sub(element: T, idProvider: IdProvider<T, I>): SubStore<R, List<T>, T> {
    val lens = elementLens(element, idProvider)
    return SubStore(this, lens, root, rootLens + lens)
}

/**
 * creates a [SubStore] using a [SubStore] as parent using the index in the list
 * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 *
 * @param index position in the list to point to
 */
fun <R, P, T> SubStore<R, P, List<T>>.sub(index: Int): SubStore<R, List<T>, T> {
    val lens = positionLens<T>(index)
    return SubStore(this, lens, root, rootLens + lens)
}

/**
 * creates a [Seq] of [SubStore]s, one for each element of the [List] using a given [IdProvider].
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 *
 * @param idProvider to identify the same entity (i.e. when it's content changed)
 */
fun <T, I> RootStore<List<T>>.each(idProvider: IdProvider<T, I>): Seq<SubStore<List<T>, List<T>, T>> =
    this.data.each(idProvider).map {
        sub(it, idProvider)
    }

/**
 * creates a [Seq] of [SubStore]s, one for each element of the [List] without [IdProvider]
 * using the index in the list (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 */
fun <T> RootStore<List<T>>.each(): Seq<SubStore<List<T>, List<T>, T>> =
    this.data.map { it.withIndex().toList() }.eachIndex().map { (i, _) ->
        sub(i)
    }

/**
 * creates a [Seq] of [SubStore]s, one for each element of the [List] using a given [IdProvider]
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 *
 * @param idProvider to identify the same entity (i.e. when it's content changed)
 */
fun <R, P, T, I> SubStore<R, P, List<T>>.each(idProvider: IdProvider<T, I>): Seq<SubStore<R, List<T>, T>> =
    this.data.each().map {
        sub(it, idProvider)
    }

/**
 * creates a [Seq] of [SubStore]s, one for each element of the [List] without [IdProvider]
 * using the index in the list (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
 * You can also call [each] and inside it's lambda create the [SubStore] using [sub].
 */
fun <R, P, T> SubStore<R, P, List<T>>.each(): Seq<SubStore<R, List<T>, T>> =
    this.data.map { it.withIndex().toList() }.eachIndex().map { (i, _) ->
        sub(i)
    }
