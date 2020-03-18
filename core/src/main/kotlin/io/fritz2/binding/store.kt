package io.fritz2.binding

import io.fritz2.flow.asSharedFlow
import io.fritz2.optics.Lens
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*


typealias Update<T> = (T) -> T

class Handler<A>(inline val execute: (Flow<A>) -> Unit) {
    // syntactical sugar to write slot <= event-stream
    operator fun compareTo(flow: Flow<A>): Int {
        execute(flow)
        return 0
    }
}


@FlowPreview
@ExperimentalCoroutinesApi
class Applicator<A, X>(inline val execute: suspend (A) -> Flow<X>) {
    infix fun andThen(nextHandler: Handler<X>) = Handler<A> {
        nextHandler.execute(it.flatMapConcat(this.execute))
    }

    infix fun <Y> andThen(nextApplicator: Applicator<X, Y>): Applicator<A, Y> = Applicator {
        execute(it).flatMapConcat(nextApplicator.execute)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
abstract class Store<T> : CoroutineScope by MainScope() {

    //TODO: another factory for (A) -> X (map instead of flatMapConcat)

    inline fun <A> handle(crossinline execute: (T, A) -> T) = Handler<A> {
        launch {
            it.collect {
                enqueue { t -> execute(t, it) }
            }
        }
    }

    inline fun handle(crossinline execute: (T) -> T) = Handler<Unit> {
        launch {
            it.collect {
                enqueue { t -> execute(t) }
            }
        }
    }

    fun <A, X> apply(mapper: suspend (A) -> Flow<X>) = Applicator(mapper)

    abstract suspend fun enqueue(update: Update<T>)

    abstract val id: String

    abstract val data: Flow<T>
    val update = handle<T> { _, newValue -> newValue }

    abstract fun <X> sub(lens: Lens<T, X>): Store<X>
}

@FlowPreview
@ExperimentalCoroutinesApi
open class RootStore<T>(initialData: T, override val id: String = "", bufferSize: Int = 1) : Store<T>() {

    //TODO: best capacity?
    private val updates = BroadcastChannel<Update<T>>(bufferSize)
    private val applyUpdate: suspend (T, Update<T>) -> T = { lastValue, update -> update(lastValue) }

    override suspend fun enqueue(update: Update<T>) {
        updates.send(update)
    }

    override val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged().asSharedFlow()

    override fun <X> sub(lens: Lens<T, X>) = SubStore(this, lens, this, lens)
}
