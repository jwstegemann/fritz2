package dev.fritz2.binding

import dev.fritz2.flow.asSharedFlow
import dev.fritz2.format.Format
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.Lenses
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*

/**
 * defines a type for transforming one value into the next
 */
typealias Update<T> = suspend (T) -> T

/**
 * defines a type for handling errors in updates
 */
typealias ErrorHandler<T> = (Throwable, T) -> T

/**
 * represents the default transaction
 */
const val defaultTransaction = "..."

/**
 * type of elements in the update-queue of a [Store]
 *
 * @property update function describing the step from the old value to the new
 * @property errorHandler describes the handling of errors during an update and the new value in case of error
 * @property transaction transaction done by this update
 */
class QueuedUpdate<T>(
    inline val update: Update<T>,
    inline val errorHandler: (Throwable, T) -> T,
    val transaction: String
)


/**
 * The [Store] is the main type for all data binding activities. It the base class of all concrete Stores like [RootStore], [SubStore], etc.
 */
interface Store<T> : CoroutineScope {

    /**
     * default error handler printing the error an keeping the previous value
     *
     * @param exception Exception to handle
     * @param oldValue previous value of the [Store]
     */
    fun errorHandler(exception: Throwable, oldValue: T): T {
        console.error("ERROR[$id]: ${exception.message}", exception)
        return oldValue
    }

    /**
     * factory method to create a [SimpleHandler] mapping the actual value of the [Store] and a given Action to a new value.
     *
     * @param execute lambda that is executed whenever a new action-value appears on the connected event-[Flow].
     */
    fun <A> handle(
        errorHandler: ErrorHandler<T> = ::errorHandler,
        transaction: String = defaultTransaction,
        execute: suspend (T, A) -> T
    ) = SimpleHandler<A> {
        it.onEach { enqueue(QueuedUpdate({ t -> execute(t, it) }, errorHandler, transaction)) }
            .launchIn(this)
    }

    /**
     * factory method to create a [SimpleHandler] that does not take an Action
     *
     * @param execute lambda that is execute for each event on the connected [Flow]
     */
    fun handle(
        errorHandler: ErrorHandler<T> = ::errorHandler,
        transaction: String = defaultTransaction,
        execute: suspend (T) -> T
    ) = SimpleHandler<Unit> {
        it.onEach { enqueue(QueuedUpdate({ t -> execute(t) }, errorHandler, transaction)) }
            .launchIn(this)
    }

    /**
     * factory method to create a [OfferingHandler] taking an action-value and the current store value to derive the new value.
     * An [OfferingHandler] is a [Flow] by itself and can therefore be connected to other [SimpleHandler]s even in other [Store]s.
     *
     * @param bufferSize number of values to buffer
     * @param execute lambda that is executed for each action-value on the connected [Flow]. You can emit values from this lambda.
     */
    fun <A, E> handleAndOffer(
        errorHandler: ErrorHandler<T> = ::errorHandler,
        transaction: String = defaultTransaction,
        bufferSize: Int = 1,
        execute: suspend SendChannel<E>.(T, A) -> T
    ) =
        OfferingHandler<A, E>(bufferSize) { inFlow, outChannel ->
            inFlow.onEach { enqueue(QueuedUpdate({ t -> outChannel.execute(t, it) }, errorHandler, transaction)) }
                .launchIn(this)
        }

    /**
     * factory method to create an [OfferingHandler] that does not take an action in it's [execute]-lambda.
     *
     * @param bufferSize number of values to buffer
     * @param execute lambda that is executed for each event on the connected [Flow]. You can emit values from this lambda.
     */
    fun <E> handleAndOffer(
        errorHandler: ErrorHandler<T> = ::errorHandler,
        transaction: String = defaultTransaction,
        bufferSize: Int = 1,
        execute: suspend SendChannel<E>.(T) -> T
    ) =
        OfferingHandler<Unit, E>(bufferSize) { inFlow, outChannel ->
            inFlow.onEach { enqueue(QueuedUpdate({ t -> outChannel.execute(t) }, errorHandler, transaction)) }
                .launchIn(this)
        }

    /**
     * abstract method defining, how this [Store] handles an [Update]
     *
     * @param update the [Update] to handle
     */
    suspend fun enqueue(update: QueuedUpdate<T>)

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

    /**
     * calls a handler on each new value of the [Store]
     */
    fun syncBy(handler: Handler<T>) {
        data.drop(1) handledBy handler
    }
}

/**
 * calls a handler on each new value of the [Store]
 */
inline fun <T, R> Store<T>.syncBy(handler: Handler<R>, crossinline mapper: suspend (T) -> R) {
    data.drop(1).map(mapper) handledBy handler
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

    private val updates = BroadcastChannel<QueuedUpdate<T>>(bufferSize)
    private val applyUpdate: suspend (T, QueuedUpdate<T>) -> T = { lastValue, queuedUpdate ->
        try {
            queuedUpdate.update(lastValue)
        } catch (e: Throwable) {
            queuedUpdate.errorHandler(e, lastValue)
        }
    }

    /**
     * in a [RootStore] an [Update] is handled by sending it to the internal [updates]-channel.
     */
    override suspend fun enqueue(update: QueuedUpdate<T>) {
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
     * @param lens: a [Lens] describing, which part of your data model you will create [SubStore] for.
     * Use @[Lenses] annotation to let your compiler
     * create the lenses for you or use the buildLens-factory-method.
     */
    fun <X> sub(lens: Lens<T, X>): SubStore<T, T, X> =
        SubStore(this, lens, this, lens)

    /**
     * creates a new [SubStore] using the given [Format] to convert the
     * value of type [T] to a [String] and vice versa.
     *
     * @param format a [Format] for the type [T]
     */
    fun using(format: Format<T>): SubStore<T, T, String> =
        SubStore(this, format.lens, this, format.lens)
}

/**
 * convenience method to create a simple [RootStore] without any handlers, etc.
 *
 * @param initialData the first current value of this [Store]
 * @param id the id of this store. ids of [SubStore]s will be concatenated.
 */
fun <T> storeOf(initialData: T, id: String = "") = RootStore(initialData, id)