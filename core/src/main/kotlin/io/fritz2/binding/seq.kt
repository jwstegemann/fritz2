package io.fritz2.binding

import io.fritz2.optics.WithId
import io.fritz2.utils.Myer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

sealed class Patch<out T> {
    data class Insert<T>(val element: T, val index: Int) : Patch<T>() {
        override fun <R> map(mapping: (T) -> R): Patch<R> = Insert(mapping(element), index)
    }

    //data class InsertMany<T>(val elements: List<T>, val index: Int) : Patch<T>()

    data class Delete<T>(val start: Int, val count: Int = 1) : Patch<T>() {
        override fun <R> map(mapping: (T) -> R): Patch<R> = this.unsafeCast<Patch<R>>() //Delete(start, count)
    }

    //data class Move(val from: Int, val to: Int)

    abstract fun <R> map(mapping: (T) -> R): Patch<R>
}


inline class Seq<T>(val data: Flow<Patch<T>>) {
    fun <X> map(mapper: (T) -> X): Seq<X> {
        return Seq(data.map {
            it.map(mapper)
        })
    }
}

private suspend inline fun <T> accumulate(accumulator: Pair<List<T>, List<T>>, newValue: List<T>) =
    Pair(accumulator.second, newValue)


fun <T : WithId> Flow<List<T>>.each(): Seq<T> =
    Seq(this.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat { (old, new) ->
        Myer.diff(old, new) { a, b ->
            a.id == b.id
        }
    })


fun <T> Flow<List<T>>.each(): Seq<T> =
    Seq(this.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat { (old, new) ->
        Myer.diff(old, new) { a, b -> a == b }
    })


