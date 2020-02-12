package io.fritz2.binding

import io.fritz2.optics.Lens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias Update<T> = (T) -> T

class Handler<A>(inline val handle: (Flow<A>) -> Unit) {
    // syntactical sugar to write slot <= event-stream
    operator fun compareTo(flow: Flow<A>): Int {
        handle(flow)
        return 0
    }
}

abstract class Store<T> {

    inline fun <A> handle(crossinline handler: (T, A) -> T) = Handler<A> {
        GlobalScope.launch {
            it.collect {
                enqueue { t -> handler(t,it) }
            }
        }
    }

    inline fun <A> handle(crossinline handler: (T,A) -> Any) = Handler<A> {
        GlobalScope.launch {
            it.collect {
                enqueue { t ->
                    handler(t, it)
                    t
                }
            }
        }
    }

    infix fun <X> Flow<X>.andThen(nextHandler: Handler<X>) {
        nextHandler.handle(this)
    }

    abstract fun enqueue(update: Update<T>)

    abstract val id: String

    abstract val data: Flow<T>
    val update = handle<T> { _, newValue -> newValue }

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