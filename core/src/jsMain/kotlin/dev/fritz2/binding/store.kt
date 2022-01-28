package dev.fritz2.binding

import dev.fritz2.dom.html.WithJob
import dev.fritz2.identification.Id
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.Lenses
import dev.fritz2.remote.Socket
import dev.fritz2.remote.body
import dev.fritz2.resource.Resource
import dev.fritz2.validation.Validation
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.valid
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Defines a type for transforming one value into the next
 */
typealias Update<D> = suspend (D) -> D

/**
 * Defines a type for handling errors in updates
 */
typealias ErrorHandler<D> = (Throwable, D) -> D

/**
 * Type of elements in the update-queue of a [Store]
 *
 * @property update function describing the step from the old value to the new
 * @property errorHandler describes the handling of errors during an update and the new value in case of error
 */
class QueuedUpdate<D>(
    inline val update: Update<D>,
    inline val errorHandler: (Throwable, D) -> D
)


/**
 * The [Store] is the main type for all data binding activities. It the base class of all concrete Stores like [RootStore], [SubStore], etc.
 */
interface Store<D> : WithJob {

    /**
     * Default error handler printing the error to console and keeping the previous value.
     *
     * @param exception Exception to handle
     * @param oldValue previous value of the [Store]
     */
    fun errorHandler(exception: Throwable, oldValue: D): D {
        console.error("ERROR[$id]: ${exception.message}", exception)
        return oldValue
    }

    /**
     * Factory method to create a [SimpleHandler] mapping the actual value of the [Store] and a given Action to a new value.
     *
     * @param execute lambda that is executed whenever a new action-value appears on the connected event-[Flow].
     */
    fun <A> handle(
        errorHandler: ErrorHandler<D> = ::errorHandler,
        execute: suspend (D, A) -> D
    ) = SimpleHandler<A> { flow, job ->
        flow.onEach { enqueue(QueuedUpdate({ t -> execute(t, it) }, errorHandler)) }
            .launchIn(MainScope() + job)
    }

    /**
     * Factory method to create a [SimpleHandler] that does not take an Action
     *
     * @param errorHandler handles error during update
     * @param execute lambda that is execute for each event on the connected [Flow]
     */
    fun handle(
        errorHandler: ErrorHandler<D> = ::errorHandler,
        execute: suspend (D) -> D
    ) = SimpleHandler<Unit> { flow, job ->
        flow.onEach { enqueue(QueuedUpdate({ t -> execute(t) }, errorHandler)) }
            .launchIn(MainScope() + job)
    }

    /**
     * Factory method to create a [EmittingHandler] taking an action-value and the current store value to derive the new value.
     * An [EmittingHandler] is a [Flow] by itself and can therefore be connected to other [SimpleHandler]s even in other [Store]s.
     *
     * @param errorHandler handles error during update
     * @param execute lambda that is executed for each action-value on the connected [Flow]. You can emit values from this lambda.
     */
    fun <A, E> handleAndEmit(
        errorHandler: ErrorHandler<D> = ::errorHandler,
        execute: suspend FlowCollector<E>.(D, A) -> D
    ) =
        EmittingHandler<A, E>({ inFlow, outFlow, job ->
            inFlow.onEach { enqueue(QueuedUpdate({ t -> outFlow.execute(t, it) }, errorHandler)) }
                .launchIn(MainScope() + job)
        })

    /**
     * factory method to create an [EmittingHandler] that does not take an action in it's [execute]-lambda.
     *
     * @param errorHandler handles error during update
     * @param execute lambda that is executed for each event on the connected [Flow]. You can emit values from this lambda.
     */
    fun <E> handleAndEmit(
        errorHandler: ErrorHandler<D> = ::errorHandler,
        execute: suspend FlowCollector<E>.(D) -> D
    ) =
        EmittingHandler<Unit, E>({ inFlow, outFlow, job ->
            inFlow.onEach { enqueue(QueuedUpdate({ t -> outFlow.execute(t) }, errorHandler)) }
                .launchIn(MainScope() + job)
        })

    /**
     * abstract method defining, how this [Store] handles an [Update]
     *
     * @param update the [Update] to handle
     */
    suspend fun enqueue(update: QueuedUpdate<D>)

    /**
     * [id] of this [Store].
     * ids of depending [Store]s are concatenated and separated by a dot.
     */
    val id: String

    /**
     * Path of this [Store] derived from the underlying model.
     * Paths of depending [Store]s are concatenated and separated by a dot.
     */
    val path: String

    /**
     * the [Flow] representing the current value of the [Store]. Use this to bind it to ui-elements or derive calculated values by using [map] for example.
     */
    val data: Flow<D>

    /**
     * represents the current value of the [Store]
     */
    val current: D

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    val update: Handler<D>

    /**
     * calls a handler on each new value of the [Store]
     */
    fun syncBy(handler: Handler<Unit>) {
        data.drop(1).map { } handledBy handler
    }

    /**
     * calls a handler on each new value of the [Store]
     */
    fun syncBy(handler: Handler<D>) {
        data.drop(1) handledBy handler
    }

    fun <I> syncWith(socket: Socket, resource: Resource<D, I>) {
        val session = socket.connect()
        var last: D? = null
        session.messages.body.map {
            val received = resource.deserialize(it)
            last = received
            received
        } handledBy update

        data.drop(1) handledBy {
            if (last != it) session.send(resource.serialize(it))
        }
    }

    /**
     * create a [SubStore] that represents a certain part of your data model.
     *
     * @param lens: a [Lens] describing, which part of your data model you will create [SubStore] for.
     * Use @[Lenses] annotation to let your compiler
     * create the lenses for you or use the buildLens-factory-method.
     */
    fun <X> sub(lens: Lens<D, X>): SubStore<D, X> =
        SubStore(this, lens)
}

/**
 * calls a handler on each new value of the [Store]
 */
inline fun <T, R> Store<T>.syncBy(handler: Handler<R>, crossinline mapper: suspend (T) -> R) {
    data.drop(1).map(mapper) handledBy handler
}

fun <T, I> Store<List<T>>.syncWith(socket: Socket, resource: Resource<T, I>) {
    val session = socket.connect()
    var last: List<T>? = null
    session.messages.body.map {
        val received = resource.deserializeList(it)
        last = received
        received
    } handledBy update

    data.drop(1) handledBy {
        if (last != it) session.send(resource.serializeList(it))
    }
}

/**
 * A [Store] can be initialized with a given value.
 * Use a [RootStore] to "store" your model and create [SubStore]s from here.
 *
 * @param initialData first current value of this [Store]
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
open class RootStore<D>(
    initialData: D,
    override val id: String = Id.next()
) : Store<D> {

    private val state: MutableStateFlow<D> = MutableStateFlow(initialData)
    private val mutex = Mutex()

    override val path: String = ""

    /**
     * [Job] used as parent job on all coroutines started in [Handler]s in the scope of this [Store]
     */
    override val job: Job = Job()

    /**
     * Emits a [Flow] with the current data of this [RootStore].
     * The [Flow] internal data is only changed, when the value differs from the last one to avoid calculations
     * and updates that are not necessary.
     *
     * Actual data therefore is derived by applying the updates on the internal channel one by one to get the next value.
     */
    override val data: Flow<D> = state.asStateFlow()

    /**
     * Represents the current data of this [RootStore].
     */
    override val current: D
        get() = state.value

    /**
     * in a [RootStore] an [Update] is handled by applying it to the internal [StateFlow].
     */
    override suspend fun enqueue(update: QueuedUpdate<D>) {
        try {
            mutex.withLock {
                state.value = update.update(state.value)
            }
        } catch (e: Throwable) {
            update.errorHandler(e, state.value)
        }
    }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = this.handle<D> { _, newValue -> newValue }
}

/**
 * A [ValidatingStore] is a [RootStore] which also contains a [Validation] for its model and by default applies it
 * to every update.
 *
 * This store is intentionally configured to validate the data on each update, that is why the [validateAfterUpdate]
 * parameter is set to ``true`` by default.
 *
 * There might be special situations where it is reasonable to disable this behaviour and to prefer applying the
 * validation individually within custom handlers, for example if a model should only be validated after the user
 * has completed his input or if meta-data is needed for the validation process. Then be aware of the fact,
 * that the call of the [validate] function actually updates the [messages] [Flow] already.
 *
 * If the new data is not passed to the store's [data] flow afterwards, the messages are out of sync with the
 * actual data state!
 * This could lead to false assumptions and might produce hard to detect bugs in your application context.
 *
 * So for scenarios where the model in a store should always be valid, do not use this store implementation!
 * Instead, strive for a custom store implementation where you can handle the validation completely by yourself.
 * As the validation is simply a call on the thin [Validation] wrapper, it is really easy to manage manually.
 *
 * Using this store you should always insert exactly the data, that leads to the current validation messages!
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] function to use at the data on this [Store].
 * @param validateAfterUpdate flag to decide if a new value gets automatically validated after setting it to the [Store].
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
open class ValidatingStore<D, T, M>(
    initialData: D,
    private val validation: Validation<D, T, M>,
    val validateAfterUpdate: Boolean = true,
    override val id: String = Id.next()
) : RootStore<D>(initialData, id) {

    private val validationMessages: MutableStateFlow<List<M>> = MutableStateFlow(emptyList())

    /**
     * [Flow] of the [List] of validation-messages.
     * Use this [Flow] to render out the validation-messages and to detect the valid state of the current [data] [Flow].
     */
    val messages: Flow<List<M>> = validationMessages.asStateFlow()

    /**
     * Resets the validation result.
     *
     * Beware that cleaning the messages should not be done, if the [data] [Flow] remains in an invalid state.
     * Please refer to the class's description for details about the need for a sound data and messages state.
     *
     * @param messages list of messages to reset to. Default is an empty list.
     */
    protected fun resetMessages(messages: List<M> = emptyList()) {
        validationMessages.value = messages
    }

    /**
     * Validates the given [data] by using the optional [metadata] to update the
     * [messages] list and returning them.
     * Use this method from inside your [Handler]s to publish
     * the new state of the validation result via the [messages] flow.
     *
     * @param data data to validate
     * @param metadata optional metadata for validation
     * @return [List] of messages
     */
    protected fun validate(data: D, metadata: T? = null): List<M> =
        validation(data, metadata).also { validationMessages.value = it }

    init {
        if(validateAfterUpdate) this.syncBy(this.handle<D> { _, newValue ->
            validate(newValue)
            newValue
        })
    }
}

/**
 * Checks if a [Flow] of a [List] of [ValidationMessage]s is valid.
 */
val <M : ValidationMessage> Flow<List<M>>.valid: Flow<Boolean>
    get() = this.map { it.valid }

/**
 * Convenience function to create a simple [RootStore] without any handlers, etc.
 *
 * @param initialData first current value of this [Store]
 * @param id id of this store. Ids of [SubStore]s will be concatenated.
 */
fun <D> storeOf(initialData: D, id: String = Id.next()) = RootStore(initialData, id)

/**
 * Convenience function to create a simple [ValidatingStore] without any handlers, etc.
 * The created [Store] validates its model after every update automatically.
 *
 * @param initialData first current value of this [Store]
 * @param validation [Validation] instance to use at the data on this [Store].
 * @param id id of this [Store]. Ids of [SubStore]s will be concatenated.
 */
fun <D, T, M> storeOf(
    initialData: D,
    validation: Validation<D, T, M>,
    id: String = Id.next()
) = ValidatingStore(initialData, validation, true, id)