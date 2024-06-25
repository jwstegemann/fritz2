package dev.fritz2.core

import kotlinx.atomicfu.atomic
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import org.w3c.dom.events.Event

/**
 * Defines a type for transforming one value into the next
 */
typealias Update<D> = suspend (D) -> D

/**
 * [Store] interface is the main type for all two-way data binding activities.
 */
interface Store<D> {

    /**
     * [Job] for launching coroutines in.
     */
    val job: Job

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
     * Returns the current value of the [Store].
     *
     * Note: Only use this property when explicitly needed.
     * Most of the time, following the reactive approach via a combination of [data][dev.fritz2.core.Store.data] and
     * [render][dev.fritz2.core.RenderContext.render] is the more suitable option.
     */
    val current: D

    /**
     * A simple [Handler] taking the given action-value as the new value for the [Store].
     */
    val update: Handler<D>

    /**
     * abstract method defining, how this [Store] handles an [Update]
     *
     * @param update the [Update] to handle
     */
    suspend fun enqueue(update: Update<D>)

    /**
     * Factory method to create a [SimpleHandler] mapping the actual value of the [Store] and a given Action to a new value.
     *
     * @param execute lambda that is executed whenever a new action-value appears on the connected event-[Flow].
     */
    fun <A> handle(
        execute: suspend (D, A) -> D
    ) = SimpleHandler<A> { flow, job ->
        val executeJob = flow.onEach { enqueue { d -> withContext(NonCancellable) { execute(d, it) } } }
            .catch { d -> errorHandler(d) }
            .launchIn(MainScope() + job)
        this.job.invokeOnCompletion { executeJob.cancel() }
    }

    /**
     * Factory method to create a [SimpleHandler] that does not take an Action
     *
     * @param execute lambda that is executed for each event on the connected [Flow]
     */
    fun handle(
        execute: suspend (D) -> D
    ) = SimpleHandler<Unit> { flow, job ->
        val executeJob = flow.onEach { enqueue { d -> withContext(NonCancellable) { execute(d) } } }
            .catch { d -> errorHandler(d) }
            .launchIn(MainScope() + job)
        this.job.invokeOnCompletion { executeJob.cancel() }
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
        val executeJob = inFlow.onEach { enqueue { d -> withContext(NonCancellable) { outFlow.execute(d, it) } } }
            .catch { d -> errorHandler(d) }
            .launchIn(MainScope() + job)
        this.job.invokeOnCompletion { executeJob.cancel() }
    })

    /**
     * Factory method to create an [EmittingHandler] that does not take an action in it's [execute]-lambda.
     *
     * @param execute lambda that is executed for each event on the connected [Flow]. You can emit values from this lambda.
     */
    fun <E> handleAndEmit(
        execute: suspend FlowCollector<E>.(D) -> D
    ) =
        EmittingHandler<Unit, E>({ inFlow, outFlow, job ->
            val executeJob = inFlow.onEach { enqueue { d -> withContext(NonCancellable) { outFlow.execute(d) } } }
                .catch { d -> errorHandler(d) }
                .launchIn(MainScope() + job)
            this.job.invokeOnCompletion { executeJob.cancel() }
        })

    /**
     * Default error handler printing the error to console.
     *
     * @param cause Throwable to handle
     */
    fun errorHandler(cause: Throwable): Unit = printErrorIgnoreLensException(cause)

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
 * @param job Job to be used by the [Store]
 * @param id id of this [Store]. Ids of derived [Store]s will be concatenated.
 */
open class RootStore<D>(
    initialData: D,
    job: Job,
    override val id: String = Id.next()
) : Store<D> {
    override val path: String = ""

    /**
     * Internal [MutableSharedFlow] which holds the Store's value.
     *
     * The provided initial value is _not_ emitted by this Flow!
     * The initial value is included in the [data] Flow instead.
     *
     * __Important:__ Do _not_ use this Flow to manipulate the Store's value directly as the change would not be
     * reflected elsewhere (e.g. in [current])!
     *
     * The intended way of updating the value is by queueing an update operation in the [queue].
     * Use the [update] Handler for additional convenience.
     */
    private val state: MutableSharedFlow<D> = MutableSharedFlow(replay = 1)

    private val queue = Channel<Update<D>>(Channel.UNLIMITED)


    /**
     * Internal backing field of the [current] property needed for thread-safety reasons.
     *
     * __Important:__ Do _not_ use this field to manipulate the Store's current value directly as the change would
     * not be reflected elsewhere (e.g. in the [data] flow)!
     *
     * The current value is automatically updated when an update operation is processed instead.
     */
    private val atomicCurrent = atomic(initialData)

    override val current: D
        get() = atomicCurrent.value


    /**
     * [Job] used as parent job on all coroutines started in [Handler]s in the scope of this [Store]
     */
    final override val job: Job = (MainScope() + job).launch(start = CoroutineStart.UNDISPATCHED) {
        activeJobs.incrementAndGet()
        queue.consumeEach { update ->
            try {
                val updatedValue = update(current)
                atomicCurrent.value = updatedValue
                state.emit(updatedValue)
            } catch (t: Throwable) {
                errorHandler(t)
            }
        }
    }.apply { invokeOnCompletion { activeJobs.decrementAndGet() } }

    /**
     * Emits a [Flow] with the current data of this [Store].
     *
     * The [Flow's][Flow] internal data is updated on _every_ update, ignoring whether it actually changed or not.
     * Use [distinctUntilChanged][Flow.distinctUntilChanged] if duplicate calculations or rendering operations need to
     * be avoided.
     *
     * Example:
     * ```
     * val store = storeOf(0)
     * store.data.distinctUntilChanged().render { ... }
     * //        ^^^^^^^^^^^^^^^^^^^^^^^
     * //        ∟ explicitly avoid duplicate collection of values
     * //          if needed
     * ```
     *
     * The actual data is derived by applying the updates on the internal channel one by one to get the next value.
     */
    final override val data: Flow<D> = flow {
        try {
            activeFlows.incrementAndGet()
            emit(state.onStart { emit(initialData) })
            this@RootStore.job.join()
            emit(emptyFlow())
        } finally {
            activeFlows.decrementAndGet()
        }
    }.flatMapLatest { it }


    /**
     * in a [RootStore] an [Update] is handled by applying it to the internal [StateFlow].
     */
    override suspend fun enqueue(update: Update<D>): Unit = queue.send(update)

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = this.handle<D> { _, newValue -> newValue }

    private val withJob = object : WithJob {
        override val job: Job = this@RootStore.job
        override fun errorHandler(cause: Throwable) = this@RootStore.errorHandler(cause)
    }

    /**
     * Allows to use the [WithJob]-Context of this Store and to run [handledBy] on the Store-Job
     */
    fun runWithJob(init: WithJob.() -> Unit) = withJob.init()

    /**
     * Connects a [Flow] to a [Handler].
     *
     * @param handler [Handler] that will be called for each action/event on the [Flow]
     * @receiver [Flow] of action/events to bind to a [Handler]
     */
    protected infix fun <A> Flow<A>.handledBy(handler: Handler<A>) = runWithJob { this@handledBy handledBy handler }

    /**
     * Connects a [Flow] to a suspendable [execute] function.
     *
     * @param execute function that will be called for each action/event on the [Flow]
     * @receiver [Flow] of action/events to bind to
     */
    protected infix fun <A> Flow<A>.handledBy(execute: suspend (A) -> Unit) =
        runWithJob { this@handledBy handledBy execute }

    /**
     * Connects [Event]s to a [Handler].
     *
     * @receiver [Flow] which contains the [Event]
     * @param handler that will handle the fired [Event]
     */
    protected infix fun <E : Event> Flow<E>.handledBy(handler: Handler<Unit>) =
        runWithJob { this@handledBy handledBy handler }

    /**
     * Connects a [Flow] to a suspendable [execute] function.
     *
     * @receiver [Flow] which contains the [Event]
     * @param execute function that will handle the fired [Event]
     */
    protected infix fun <E : Event> Flow<E>.handledBy(execute: suspend (E) -> Unit) =
        runWithJob { this@handledBy handledBy execute }

    companion object {
        private val activeFlows = atomic(0)
        private val activeJobs = atomic(0)

        /**
         * Count of active [Store.data]-Flows, can be used to detect memory-leaks
         */
        val ACTIVE_FLOWS get() = activeFlows.value

        /**
         * Count of active [Store.job]-Instances, can be used to detect memory-leaks
         */
        val ACTIVE_JOBS get() = activeJobs.value

        fun resetCounters() {
            activeFlows.value = 0
            activeJobs.value = 0
        }

        init {
            window.asDynamic().fritz2 = {}
            window.asDynamic().fritz2.active_jobs = { ACTIVE_JOBS }
            window.asDynamic().fritz2.active_flows = { ACTIVE_FLOWS }
        }
    }
}

/**
 * Convenience function to create a simple [Store] without any handlers, etc.
 *
 * @param initialData first current value of this [Store]
 * @param job Job to be used by the [Store]
 * @param id id of this store. Ids of derived [Store]s will be concatenated.
 */
fun <D> storeOf(initialData: D, job: Job, id: String = Id.next()): Store<D> =
    RootStore(initialData, job, id)

/**
 * Convenience function to create a simple [Store] without any handlers, etc.
 *
 * @param initialData first current value of this [Store]
 * @param job Job to be used by the [Store]
 * @param id id of this store. Ids of derived [Store]s will be concatenated.
 */
fun <D> WithJob.storeOf(initialData: D, job: Job = this.job, id: String = Id.next()): Store<D> =
    RootStore(initialData, job, id)
