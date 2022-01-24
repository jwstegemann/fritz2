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
import dev.fritz2.validation.isValid
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
 * @param initialData: the first current value of this [Store]
 * @param id: the id of this store. ids of [SubStore]s will be concatenated.
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

open class ValidatingStore<D, T, M>(
    initialData: D,
    private val validation: Validation<D, T, M>,
    val validateOnUpdate: Boolean = false,
    override val id: String = Id.next()
) : RootStore<D>(initialData, id) {

    private val validationMessages: MutableStateFlow<List<M>> = MutableStateFlow(emptyList())

    val messages: Flow<List<M>> = validationMessages.asStateFlow()

    /**
     * Resets the validation result.
     *
     * @param messages list of messages to reset to. Default is an empty list.
     */
    fun resetMessages(messages: List<M> = emptyList()) {
        validationMessages.value = messages
    }

    fun validate(data: D, metadata: T? = null): List<M> =
        validation(data, metadata).also { validationMessages.value = it }

    init {
        if(validateOnUpdate) this.syncBy(this.handle<D> { _, newValue ->
            validate(newValue)
            newValue
        })
    }
}

///**
// * Finds the first [ValidationMessage] matching the given [predicate].
// * If no such element was found, nothing gets called afterwards.
// */
//fun <M> Flow<List<M>>.find(predicate: (M) -> Boolean): Flow<M?> = this.map { it.find(predicate) }
//
///**
// * Returns a [Flow] of list containing only
// * [ValidationMessage]s matching the given [predicate].
// */
//fun <M> Flow<List<M>>.filter(predicate: (M) -> Boolean): Flow<List<M>> = this.map { it.filter(predicate) }

//fun <D, T, M : ValidationMessage> ValidatingStore<D, T, M>.validate(data: D) = this.validate(data, null)

val <M : ValidationMessage> Flow<List<M>>.valid: Flow<Boolean>
    get() = this.map { it.isValid() }

/**
 * convenience method to create a simple [RootStore] without any handlers, etc.
 *
 * @param initialData the first current value of this [Store]
 * @param id the id of this store. ids of [SubStore]s will be concatenated.
 */
fun <D> storeOf(initialData: D, id: String = Id.next()) = RootStore(initialData, id)

fun <D, T, M> storeOf(
    initialData: D,
    validation: Validation<D, T, M>,
    validateOnUpdate: Boolean = false,
    id: String = Id.next()
) = ValidatingStore(initialData, validation, validateOnUpdate, id)