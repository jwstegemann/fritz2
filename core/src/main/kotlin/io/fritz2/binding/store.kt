package io.fritz2.binding

import io.fritz2.optics.Lens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * An update on a store is a function to infer the next model from the current.
 */
typealias Update<T> = (T) -> T

/**
 * A Store is the plave to "store" the data, on which changes you want to react.
 *
 * @param T Type of the data that this store holds
 */
abstract class Store<T> {

    /**
     * Enqueue a specific update of you modle.
     *
     * @param update update to queue
     */
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

    abstract val id: String

    abstract val data: Flow<T>
    val update: Handler<T> = Handler<T> { _, newValue -> newValue }

    abstract fun <X> sub(lens: Lens<T, X>): Store<X>
}

@FlowPreview
@ExperimentalCoroutinesApi
open class RootStore<T>(private val initialData: T, override val id: String = "")  : Store<T>() {
    private val updates = ConflatedBroadcastChannel<Update<T>>()
    private val applyUpdate : suspend (T, Update<T>) -> T = {lastValue, update -> update(lastValue)}

    override fun enqueue(update: Update<T>) {
        updates.offer(update)
    }

    override val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged()

    override fun <X> sub(lens: Lens<T, X>) = SubStore<T,T,X>(this, lens, this, lens)
}