package io.fritz2.binding

import io.fritz2.optics.Lens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias Update<T> = (T) -> T

abstract class AbstractStore<T> {

    abstract fun enqueue(update: Update<T>)

    inner class Handler<A>(inline val handler: (T, A) -> T) {
        fun handle(actions: Flow<A>): Unit {
            GlobalScope.launch {
                actions.collect {
                    enqueue { t -> handler(t,it) }
                }
            }
        }

        fun handle(action: A): Unit {
            enqueue { t -> handler(t,action) }
        }

        // syntactical sugar to write slot <= event-stream
        operator fun compareTo(flow: Flow<A>): Int {
            handle(flow)
            return 0
        }
    }

    abstract val data: Flow<T>
    val update: Handler<T> = Handler<T> { _, newValue -> newValue }

    abstract fun <X> sub(lens: Lens<T, X>): AbstractStore<X>
}

@FlowPreview
@ExperimentalCoroutinesApi
open class Store<T>(private val initialData: T) : AbstractStore<T>() {
    private val updates = ConflatedBroadcastChannel<Update<T>>()
    private val applyUpdate : suspend (T, Update<T>) -> T = {lastValue, update -> update(lastValue)}

    override fun enqueue(update: Update<T>) {
        updates.offer(update)
    }

    override val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged()

    override fun <X> sub(lens: Lens<T, X>) = SubStore<T,T,X>(this, lens, this, lens)
}