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

typealias Slot<T> = (Flow<T>) -> Unit

@FlowPreview
class Store<T> @ExperimentalCoroutinesApi constructor(val data: Var<T>) : ViewModel<T>() {

    @ExperimentalCoroutinesApi
    val update: Slot<T> = { slot ->
        GlobalScope.launch {
            slot.collect() {
                data.set(it)
            }
        }
    }

}