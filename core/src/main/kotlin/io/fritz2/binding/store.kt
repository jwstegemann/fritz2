package io.fritz2.binding

import io.fritz2.optics.Lens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias Update<T> = (T) -> T

interface Store<T> {

    fun enqueue(update: Update<T>)

    val data: Flow<T>

    fun <X> sub(lens: Lens<T, X>): Store<X>
}

@FlowPreview
@ExperimentalCoroutinesApi
open class RootStore<T>(initialData: T) : Store<T> {
    private val updates = ConflatedBroadcastChannel<Update<T>>()
    private val applyUpdate : suspend (T, Update<T>) -> T = {lastValue, update -> update(lastValue)}

    override fun enqueue(update: Update<T>) {
        updates.offer(update)
    }

    inner class Handler<A>(inline val handle: (T, A) -> T) {
        fun handle(actions: Flow<A>): Unit {
            GlobalScope.launch {
                actions.collect {
                    enqueue { t -> handle(t,it) }
                }
            }
        }

        fun handle(action: A): Unit {
            enqueue { t -> handle(t,action) }
        }

        // syntactical sugar to write slot <= event-stream
        operator fun compareTo(flow: Flow<A>): Int {
            handle(flow)
            return 0
        }
    }
    val update: Handler<T> = Handler { _, newValue -> newValue }

    override val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged()

    override fun <X> sub(lens: Lens<T, X>) = SubStore(this, lens, this, lens)
}