package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

//TODO: abstract?
open class ViewModel<T>() {

}

class Slot<T>(val handler: suspend (T) -> Unit) {
    fun connect(flow: Flow<T>) {
        GlobalScope.launch {
            flow.collect() {
                handler(it)
            }
        }
    }
}


@FlowPreview
open class Store<T> @ExperimentalCoroutinesApi constructor(val data: Var<T>) : ViewModel<T>() {

    @ExperimentalCoroutinesApi
    val update = Slot<T> {
        data.set(it)
    }

}