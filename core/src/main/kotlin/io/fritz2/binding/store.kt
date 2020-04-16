package io.fritz2.binding

import io.fritz2.flow.asSharedFlow
import io.fritz2.optics.Lens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


typealias Update<T> = (T) -> T

open class Handler<A>(inline val execute: (Flow<A>) -> Unit) {
    // syntactical sugar to write slot <= event-stream
    operator fun compareTo(flow: Flow<A>): Int {
        execute(flow)
        return 0
    }
}


class EmittingHandler<A, E>(bufferSize: Int, inline val execute: (Flow<A>, SendChannel<E>) -> Unit) : Flow<E> {

    internal val channel = BroadcastChannel<E>(bufferSize)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<E>) {
        collector.emitAll(channel.asFlow())
    }

    // syntactical sugar to write slot <= event-stream
    operator fun compareTo(flow: Flow<A>): Int {
        execute(flow, channel)
        return 0
    }
}


//FIXME: we need an Applicator, that can access the actual model
class Applicator<A, X>(inline val execute: suspend (A) -> Flow<X>) {
    infix fun andThen(nextHandler: Handler<X>) = Handler<A> {
        nextHandler.execute(it.flatMapConcat(this.execute))
    }

    infix fun <Y> andThen(nextApplicator: Applicator<X, Y>): Applicator<A, Y> = Applicator {
        execute(it).flatMapConcat(nextApplicator.execute)
    }
}


abstract class Store<T> : CoroutineScope by MainScope() {

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

    //FIXME: why no suspend on execute
    inline fun <A, E> handleAndEmit(bufferSize: Int = 1, crossinline execute: SendChannel<E>.(T, A) -> T) =
        EmittingHandler<A, E>(bufferSize) { inFlow, outChannel ->
            launch {
                inFlow.collect {
                    enqueue { t -> outChannel.execute(t, it) }
                }
            }
        }

    inline fun <A, E> handleAndEmit(bufferSize: Int = 1, crossinline execute: SendChannel<E>.(T) -> T) =
        EmittingHandler<Unit, E>(bufferSize) { inFlow, outChannel ->
            launch {
                inFlow.collect {
                    enqueue { t -> outChannel.execute(t) }
                }
            }
        }

    fun <A, X> apply(mapper: suspend (A) -> Flow<X>) = Applicator(mapper)

    abstract suspend fun enqueue(update: Update<T>)

    abstract val id: String

    abstract val data: Flow<T>
    val update = handle<T> { _, newValue -> newValue }
}


open class RootStore<T>(initialData: T, override val id: String = "", bufferSize: Int = 1) : Store<T>() {

    //TODO: best capacity?
    private val updates = BroadcastChannel<Update<T>>(bufferSize)
    private val applyUpdate: suspend (T, Update<T>) -> T = { lastValue, update -> update(lastValue) }

    override suspend fun enqueue(update: Update<T>) {
        updates.send(update)
    }

    override val data = updates.asFlow().scan(initialData, applyUpdate).distinctUntilChanged().asSharedFlow()

    fun <X> sub(lens: Lens<T, X>) = SubStore(this, lens, this, lens)
}


interface ModelId<T> {
    val id: String
    fun <X> sub(lens: Lens<T, X>): ModelId<X>
}


class ModelIdRoot<T>(override val id: String = "") : ModelId<T> {
    override fun <X> sub(lens: Lens<T, X>): ModelId<X> = ModelIdSub(this, lens, this, lens)
}


class ModelIdSub<R, P, T>(
    private val parent: ModelId<P>,
    private val lens: Lens<P, T>,
    val rootStore: ModelIdRoot<R>,
    val rootLens: Lens<R, T>
): ModelId<T> {
    override val id: String by lazy { "${parent.id}.${lens._id}" }
    override fun <X> sub(lens: Lens<T, X>): ModelIdSub<R, T, X> = ModelIdSub(this, lens, rootStore, this.rootLens + lens)
}