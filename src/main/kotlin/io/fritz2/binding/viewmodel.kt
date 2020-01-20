package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//TODO: abstract?
open class ViewModel<T>() {

}

//TODO: Find a better name
interface Slot<T> {
    fun connect(flow: Flow<T>)
}

//TODO: Find a better name
class ConcreteSlot<T>(val handler: suspend (T) -> Unit) : Slot<T> {
    override fun connect(flow: Flow<T>) {
        GlobalScope.launch {
            flow.collect() {
                handler(it)
            }
        }
    }

    fun <X> map(mapper: suspend (X) -> T): Slot<X> = MappedSlot<X,T>(this, mapper)
}

class MappedSlot<X, T>(val upstream: Slot<T>, val mapper: suspend (X) -> T) : Slot<X> {
    override fun connect(flow: Flow<X>) {
        upstream.connect(flow.map(mapper))
    }
}

typealias Update<T> = (T) -> T

@FlowPreview
@ExperimentalCoroutinesApi
open class Store<T>(val initialData: T) : ViewModel<T>() {

    private val updates = ConflatedBroadcastChannel<Update<T>>()
    private val applyUpdate : suspend (T, Update<T>) -> T = {lastValue, update -> update(lastValue)}
    val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged()

    fun <A> subscribe(actions: Flow<A>, handler: (T,A) -> T) {
        GlobalScope.launch {
            actions.collect {
                val specificUpdate: Update<T> = { t -> handler(t,it) }
                updates.offer(specificUpdate)
            }
        }
    }

}