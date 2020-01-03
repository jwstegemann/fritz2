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

@FlowPreview
class Store<T> @ExperimentalCoroutinesApi constructor(val data: Var<T>) : ViewModel<T>() {

    @ExperimentalCoroutinesApi
    fun updateOn(updates: Flow<T>) {
        GlobalScope.launch {
            updates.collect() {
                data.set(it)
            }
        }
    }

}