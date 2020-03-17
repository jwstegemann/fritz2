package io.fritz2.binding

import io.fritz2.flow.asSharedFlow
import io.fritz2.optics.Lens
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*


typealias Update<T> = (T) -> T

class Handler<A>(inline val handle: (Flow<A>) -> Unit) {
    // syntactical sugar to write slot <= event-stream
    operator fun compareTo(flow: Flow<A>): Int {
        handle(flow)
        return 0
    }
}

class Applicator<A,X>(inline val mapper: suspend (A) -> Flow<X>)

@ExperimentalCoroutinesApi
abstract class Store<T> : CoroutineScope by MainScope() {

    //TODO: another factory for (A) -> X (map instead of flatMapConcat)
    @FlowPreview
    infix fun <A,X> Applicator<A,X>.andThen(nextHandler: Handler<X>) = Handler<A> {
        nextHandler.handle(it.flatMapConcat(this.mapper))
    }

    //TODO: andThen for other Applyers

    inline fun <A> handle(crossinline handler: (T, A) -> T) = Handler<A> {
        launch {
            it.collect {
                enqueue { t ->  handler(t,it) }
            }
        }
    }

    inline fun handle(crossinline handler: (T) -> T) = Handler<Unit> {
        launch {
            it.collect {
                enqueue { t ->  handler(t) }
            }
        }
    }

    fun <A,X> apply(mapper: suspend (A) -> Flow<X>) = Applicator<A,X>(mapper)

    abstract suspend fun enqueue(update: Update<T>)

    abstract val id: String

    abstract val data: Flow<T>
    val update = handle<T> { _, newValue -> newValue }

    abstract fun <X> sub(lens: Lens<T, X>): Store<X>
}

@FlowPreview
@ExperimentalCoroutinesApi
open class RootStore<T>(private val initialData: T, override val id: String = "", bufferSize: Int = 1)  : Store<T>() {

    //private val scope = CoroutineScope(Job())

    //TODO: best capacity?
    private val updates = BroadcastChannel<Update<T>>(bufferSize)
    private val applyUpdate : suspend (T, Update<T>) -> T = { lastValue, update -> update(lastValue) }

    override suspend fun enqueue(update: Update<T>) {
        updates.send(update)
    }

    override val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged().asSharedFlow()

    override fun <X> sub(lens: Lens<T, X>) = SubStore<T,T,X>(this, lens, this, lens)
}
