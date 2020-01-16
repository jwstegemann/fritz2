package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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


@FlowPreview
open class Store<T> @ExperimentalCoroutinesApi constructor(val data: Var<T>) : ViewModel<T>() {

    @ExperimentalCoroutinesApi
    val update = ConcreteSlot<T> {
        data.set(it)
    }

}