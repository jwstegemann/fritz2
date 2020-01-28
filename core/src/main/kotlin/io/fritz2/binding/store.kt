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
open class Store<T>(val initialData: T) {

    inner class Handler<A>(inline val handle: (T, A) -> T) {
        fun handle(actions: Flow<A>) {
            GlobalScope.launch {
                actions.collect {
                    val specificUpdate: Update<T> = { t -> handle(t,it) }
                    updates.offer(specificUpdate)
                }
            }
        }

        // syntactical sugar to write slot <= event-stream
        operator fun compareTo(flow: Flow<A>): Int {
            handle(flow)
            return 0
        }
    }

    private val updates = ConflatedBroadcastChannel<Update<T>>()
    private val applyUpdate : suspend (T, Update<T>) -> T = {lastValue, update -> update(lastValue)}
    val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged()

    val update = Handler<T> { _, newValue -> newValue }
}