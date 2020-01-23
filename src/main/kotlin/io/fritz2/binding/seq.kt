package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map

data class Patch<out T>(val from: Int, val that: List<T>, val replaced: Int)

@ExperimentalCoroutinesApi
private suspend fun <T> compare(oldAndNew: Pair<List<T>, List<T>>): Flow<Patch<T>> = channelFlow {
    val (oldValue, newValue) = oldAndNew
    val size2Compare = if (oldValue.size < newValue.size) {
        //console.log("### Seq: append " + (newValue.size - oldValue.size) + " from " + (oldValue.size))
        channel.send(Patch(oldValue.size, newValue.takeLast(newValue.size - oldValue.size), 0))
        oldValue.size
    } else {
        if (newValue.size < oldValue.size) {
            //console.log("### Seq: delete " + (oldValue.size - newValue.size) + " from " + (newValue.size -1))
            channel.send(Patch(newValue.size, listOf(), oldValue.size - newValue.size))
        }
        newValue.size
    }
    //console.log("### Seq: size to compare: $size2Compare")

    //FIXME: better performance without range?
    for (i in 0 until size2Compare) {
        //console.log("### Seq: inner comparing: $i -> ${last[i]} to ${n[i]}")
        if (oldValue[i] != newValue[i]) channel.send(Patch(i, listOf(newValue[i]), 1))
    }

}

private suspend fun <T> accumulate(accumulator: Pair<List<T>, List<T>>, newValue: List<T>): Pair<List<T>, List<T>> = Pair(accumulator.second, newValue)

@ExperimentalCoroutinesApi
@FlowPreview
fun <T> Store<List<T>>.each(): Flow<Patch<T>>  =
    //TODO: too many instances of empty lists
    data.scan(Pair(listOf<T>(), listOf<T>()), ::accumulate).flatMapConcat(::compare)


//TODO: flatmap needed?
fun <T, X> Flow<Patch<T>>.map(mapper: (T) -> X): Flow<Patch<X>> =
    this.map {
        Patch(it.from, it.that.map(mapper), it.replaced)
    }
