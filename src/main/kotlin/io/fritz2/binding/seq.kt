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
        val size2Compare = if (last.size < n.size) {
            channel.send(Patch(last.size, n.takeLast(n.size - last.size), 0))
            last.size
        }
        else {
            if (n.size < last.size) {
                //println("delete " + (last.size - n.size) + " from " + (n.size -1))
                channel.send(Patch(n.size, listOf(), last.size - n.size ))
            }
            n.size
        }

        //println(s"compare to $size2Compare")

        for (i in 0 .. size2Compare) {
            if (last[i] != n[i]) channel.send(Patch(i, listOf(n[i]), 1))
        }
    }
}
