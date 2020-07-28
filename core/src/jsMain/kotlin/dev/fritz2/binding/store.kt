package dev.fritz2.binding

import dev.fritz2.flow.asSharedFlow
import dev.fritz2.lenses.Lens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * defines a type for transforming one value into the next
 */
typealias Update<T> = suspend (T) -> T


/**
 * The [Store] is the main type for all data binding activities. It the base class of all concrete Stores like [RootStore], [SubStore], etc.
 */
interface Store<T> : CoroutineScope {

    /**
     * factory method to create a [SimpleHandler] mapping the actual value of the [Store] and a given Action to a new value.
     *
     *
     * @param execute lambda that is executed whenever a new action-value appears on the connected event-[Flow].
     */
    fun <A> handle(execute: suspend (T, A) -> T) = SimpleHandler<A> {
        launch {
            it.collect {
                enqueue { t -> execute(t, it) }
            }
        }
    }

    /**
     * factory method to create a [SimpleHandler] that does not take an Action
     *
     * @param execute lambda that is execute for each event on the connected [Flow]
     */
    fun handle(execute: suspend (T) -> T) = SimpleHandler<Unit> {
        launch {
            it.collect {
                enqueue { t -> execute(t) }
            }
        }
    }

    /**
     * factory method to create a [EmittingHandler] taking an action-value and the current store value to derive the new value.
     * An [EmittingHandler] is a [Flow] by itself and can therefore be connected to other [SimpleHandler]s even in other [Store]s.
     *
     * @param bufferSize number of values to buffer
     * @param execute lambda that is executed for each action-value on the connected [Flow]. You can emit values from this lambda.
     */
    //FIXME: why no suspend on execute
    fun <A, E> handleAndOffer(bufferSize: Int = 1, execute: suspend SendChannel<E>.(T, A) -> T) =
        EmittingHandler<A, E>(bufferSize) { inFlow, outChannel ->
            launch {
                inFlow.collect {
                    enqueue { t -> outChannel.execute(t, it) }
                }
            }
        }

    /**
     * factory method to create an [EmittingHandler] that does not take an action in it's [execute]-lambda.
     *
     * @param bufferSize number of values to buffer
     * @param execute lambda that is executed for each event on the connected [Flow]. You can emit values from this lambda.
     */
    fun <E> handleAndOffer(bufferSize: Int = 1, execute: suspend SendChannel<E>.(T) -> T) =
        EmittingHandler<Unit, E>(bufferSize) { inFlow, outChannel ->
            launch {
                inFlow.collect {
                    enqueue { t -> outChannel.execute(t) }
                }
            }
        }

    /**
     * abstract method defining, how this [Store] handles an [Update]
     *
     * @param update the [Update] to handle
     */
    suspend fun enqueue(update: Update<T>)

    /**
     * base-id of this [Store]. ids of depending [Store]s are concatenated separated by a dot.
     */
    val id: String

    /**
     * the [Flow] representing the current value of the [Store]. Use this to bind it to ui-elements or derive calculated values by using [map] for example.
     */
    val data: Flow<T>

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    val update: Handler<T>

    /**
     * calls a handler on each new value of the [Store]
     */
    fun syncBy(handler: Handler<Unit>) {
        data.drop(1).map { Unit } handledBy handler
    }
}

/**
 * A [Store] can be initialized with a given value. Use a [RootStore] to "store" your model and create [SubStore]s from here.
 *
 * @param initialData: the first current value of this [Store]
 * @param id: the id of this store. ids of [SubStore]s will be concatenated.
 * @param bufferSize: number of values to buffer
 */
open class RootStore<T>(
    initialData: T,
    override val id: String = "",
    dropInitialData: Boolean = false,
    bufferSize: Int = 1
) : Store<T>, CoroutineScope by MainScope() {

    private val updates = BroadcastChannel<Update<T>>(bufferSize)
    private val applyUpdate: suspend (T, Update<T>) -> T = { lastValue, update -> update(lastValue) }

    /**
     * in a [RootStore] an [Update] is handled by sending it to the internal [updates]-channel.
     */
    override suspend fun enqueue(update: Update<T>) {
        updates.send(update)
    }

    /**
     * the current value of a [RootStore] is derived be applying the updates on the internal channel one by one to get the next value.
     * the [Flow] only emit's a new value, when the value is differs from the last one to avoid calculations and updates that are not necessary.
     * This has to be a SharedFlow, because the updated should only be applied once, regardless how many depending values or ui-elements or bound to it.
     */
    override val data = updates.asFlow().scan(initialData, applyUpdate)
        .drop(if (dropInitialData) 1 else 0)
        .distinctUntilChanged()
        .asSharedFlow()

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = this.handle<T> { _, newValue -> newValue }

    /**
     * create a [SubStore] that represents a certain part of your data model.
     *
     * @param lens: a [Lens] describing, which part of your data model you will create [SubStore] for. Use @Lenses to let your compiler
     * create the lenses for you or use the buildLens-factory-method.
     */
    fun <X> sub(lens: Lens<T, X>) = SubStore(this, lens, this, lens)
}