package dev.fritz2.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*

/**
 * Defines a type for transforming one value into the next
 */
typealias Update<D> = suspend (D) -> D

/**
 * [Store] interface is the main type for all two-way data binding activities.
 */
interface Store<D> : WithJob {

    /**
     * Factory method to create a [SimpleHandler] mapping the actual value of the [Store] and a given Action to a new value.
     *
     * @param execute lambda that is executed whenever a new action-value appears on the connected event-[Flow].
     */
    fun <A> handle(
        execute: suspend (D, A) -> D
    ) = SimpleHandler<A> { flow, job ->
        flow.onEach { enqueue { d -> execute(d, it) } }
            .catch { d -> errorHandler(d) }
            .launchIn(MainScope() + job)
    }

    /**
     * Factory method to create a [SimpleHandler] that does not take an Action
     *
     * @param execute lambda that is executed for each event on the connected [Flow]
     */
    fun handle(
        execute: suspend (D) -> D
    ) = SimpleHandler<Unit> { flow, job ->
        flow.onEach { enqueue { d -> execute(d) } }
            .catch { d -> errorHandler(d) }
            .launchIn(MainScope() + job)
    }

    /**
     * Factory method to create a [EmittingHandler] taking an action-value and the current store value to derive the new value.
     * An [EmittingHandler] is a [Flow] by itself and can therefore be connected to other [SimpleHandler]s even in other [Store]s.
     *
     * @param execute lambda that is executed for each action-value on the connected [Flow]. You can emit values from this lambda.
     */
    fun <A, E> handleAndEmit(
        execute: suspend FlowCollector<E>.(D, A) -> D
    ) = EmittingHandler<A, E>({ inFlow, outFlow, job ->
            inFlow.onEach { enqueue { d -> outFlow.execute(d, it) } }
                .catch { d -> errorHandler(d) }
                .launchIn(MainScope() + job)
        })

    /**
     * factory method to create an [EmittingHandler] that does not take an action in it's [execute]-lambda.
     *
     * @param execute lambda that is executed for each event on the connected [Flow]. You can emit values from this lambda.
     */
    fun <E> handleAndEmit(
        execute: suspend FlowCollector<E>.(D) -> D
    ) =
        EmittingHandler<Unit, E>({ inFlow, outFlow, job ->
            inFlow.onEach { enqueue { d -> outFlow.execute(d) } }
                .catch { d -> errorHandler(d) }
                .launchIn(MainScope() + job)
        })

    /**
     * abstract method defining, how this [Store] handles an [Update]
     *
     * @param update the [Update] to handle
     */
    suspend fun enqueue(update: Update<D>)

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
     * Creates a new [Store] that contains data derived by a given [Lens].
     *
     * @param lens: a [Lens] describing the two-way data binding of the derived [Store].
     */
    fun <X> map(lens: Lens<D, X>): Store<X> = SubStore(this, lens)
}

/**
 * A [Store] can be initialized with a given value.
 *
 * @param initialData first current value of this [Store]
 * @param id id of this [Store]. Ids of derived [Store]s will be concatenated.
 */
open class RootStore<D>(
    initialData: D,
    override val id: String = Id.next()
) : Store<D> {
    override val path: String = ""

    private val state: MutableStateFlow<D> = MutableStateFlow(initialData)
    private val queue = Channel<Update<D>>(Channel.UNLIMITED)

    /**
     * [Job] used as parent job on all coroutines started in [Handler]s in the scope of this [Store]
     */
    override val job: Job = MainScope().launch(start = CoroutineStart.UNDISPATCHED) {
        queue.consumeEach { update ->
            try {
                state.value = update(state.value)
            } catch (t: Throwable) {
                errorHandler(t)
            }
        }
    }

    /**
     * Emits a [Flow] with the current data of this [Store].
     * The [Flow] internal data is only changed, when the value differs from the last one to avoid calculations
     * and updates that are not necessary.
     *
     * Actual data therefore is derived by applying the updates on the internal channel one by one to get the next value.
     */
    final override val data: Flow<D> = state.asStateFlow()

    /**
     * Represents the current data of this [Store].
     */
    override val current: D
        get() = state.value

    /**
     * in a [RootStore] an [Update] is handled by applying it to the internal [StateFlow].
     */
    override suspend fun enqueue(update: Update<D>): Unit = queue.send(update)

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = this.handle<D> { _, newValue -> newValue }
}

/**
 * Convenience function to create a simple [Store] without any handlers, etc.
 *
 * @param initialData first current value of this [Store]
 * @param id id of this store. Ids of derived [Store]s will be concatenated.
 */
fun <D> storeOf(initialData: D, id: String = Id.next()): Store<D> = RootStore(initialData, id)
