package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class Patch<out T>(val from: Int, val that: List<T>, val replaced: Int)

class Seq<T>(initValue: List<T>,
             private val channel: ConflatedBroadcastChannel<Patch<T>> = ConflatedBroadcastChannel<Patch<T>>(Patch(0, initValue,0)),
             private val flow: Flow<Patch<T>> = channel.asFlow().distinctUntilChanged()) : Flow<Patch<T>> by flow {

    private var last: List<T> = initValue
    
    fun value(): List<T> = last

    //TODO: maybe better performance with iterable? zip?
    @ExperimentalCoroutinesApi
    suspend fun set(n: List<T>): Unit {
//        console.log("### Seq: compare $n to $last")
        val size2Compare = if (last.size < n.size) {
//            console.log("### Seq: append " + (n.size - last.size) + " from " + (last.size))
            channel.send(Patch(last.size, n.takeLast(n.size - last.size), 0))
            last.size
        }
        else {
            if (n.size < last.size) {
//                console.log("### Seq: delete " + (last.size - n.size) + " from " + (n.size -1))
                channel.send(Patch(n.size, listOf(), last.size - n.size ))
            }
            n.size
        }

//        console.log("### Seq: size to compare: $size2Compare")

        //FIXME: better performance without range?
        for (i in 0 until size2Compare) {
//            console.log("### Seq: inner comparing: $i -> ${last[i]} to ${n[i]}")
            if (last[i] != n[i]) channel.send(Patch(i, listOf(n[i]), 1))
        }

        last = n
    }
}
