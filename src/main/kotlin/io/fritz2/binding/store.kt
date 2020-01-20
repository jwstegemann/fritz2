package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias Update<T> = (T) -> T

@FlowPreview
@ExperimentalCoroutinesApi
operator fun <M,A> Flow<A>.rangeTo(slot: Store<M>.Slot<A>) {
    slot.handle(this)
}

@FlowPreview
@ExperimentalCoroutinesApi
open class Store<T>(val initialData: T) {

    inner class Slot<A>(val handler: (T, A) -> T) {
        fun handle(actions: Flow<A>) {
            GlobalScope.launch {
                actions.collect {
                    val specificUpdate: Update<T> = { t -> handler(t,it) }
                    updates.offer(specificUpdate)
                }
            }
        }
    }

    private val updates = ConflatedBroadcastChannel<Update<T>>()
    private val applyUpdate : suspend (T, Update<T>) -> T = {lastValue, update -> update(lastValue)}
    val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged()

    val update = Slot<T> { _, newValue -> newValue }
}