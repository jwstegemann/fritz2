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

class Applicator<A,X>(inline val mapper: suspend (A) -> Flow<X>)

abstract class Store<T> {

    //TODO: another factory for (A) -> X (map instead of flatMapConcat)
    infix fun <A,X> Applicator<A,X>.andThen(nextHandler: Handler<X>) = Handler<A> {
        nextHandler.handle(it.flatMapConcat(this.mapper))
    }

    //TODO: andThen for other Applyers

    inline fun <A> handle(crossinline handler: (T, A) -> T) = Handler<A> {
        GlobalScope.launch {
            it.collect {
                enqueue { t -> handler(t,it) }
            }
        }
    }

    fun <A,X> apply(mapper: suspend (A) -> Flow<X>) = Applicator<A,X>(mapper)

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