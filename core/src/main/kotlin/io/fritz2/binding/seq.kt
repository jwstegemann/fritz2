package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map


data class Patch<out T>(val from: Int, val that: List<T>, val replaced: Int)

typealias Seq<T> = Flow<Patch<T>>

//TODO: a way to call this just map?
inline fun <T, X> Seq<T>.mapItems(crossinline mapper: (T) -> X): Flow<Patch<X>> =
    this.map {
        Patch(it.from, it.that.map(mapper), it.replaced)
    }


@ExperimentalCoroutinesApi
private inline fun <T> compare(crossinline different: (T, T) -> Boolean): suspend (Pair<List<T>, List<T>>) -> Seq<T> =
    { oldAndNew: Pair<List<T>, List<T>> ->
        channelFlow {
            val (oldValue, newValue) = oldAndNew
            //console.log("old = $oldValue; new = $newValue")
            val size2Compare = if (oldValue.size < newValue.size) {
                //console.log("### Seq: append " + (newValue.size - oldValue.size) + " from " + (oldValue.size))
                channel.send(Patch(oldValue.size, newValue.takeLast(newValue.size - oldValue.size), 0))
                oldValue.size
            } else {
                if (newValue.size < oldValue.size) {
                    //console.log("### Seq: delete " + (oldValue.size - newValue.size) + " from " + (newValue.size -1))
                    channel.send(Patch(newValue.size, emptyList<T>(), oldValue.size - newValue.size))
                }
                newValue.size
            }
            //console.log("### Seq: size to compare: $size2Compare")

            //FIXME: better performance without range?
            for (i in 0 until size2Compare) {
                //TODO: batch changed items in a row to one patch
                //console.log("### Seq: inner comparing: $i -> ${oldValue[i]} to ${newValue[i]}")
                if (different(oldValue[i], newValue[i])) channel.send(Patch(i, listOf(newValue[i]), 1))
            }
        }
    }

private suspend inline fun <T> accumulate(accumulator: Pair<List<T>, List<T>>, newValue: List<T>) =
    Pair(accumulator.second, newValue)

@ExperimentalCoroutinesApi
@FlowPreview
fun <T: withId> AbstractStore<List<T>>.each(): Seq<T>  =
    data.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat(compare {a,b ->
        a.id != b.id
    })

@ExperimentalCoroutinesApi
@FlowPreview
fun <T> AbstractStore<List<T>>.each(): Seq<T>  =
    data.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat(compare {a,b ->
        a != b
    })
