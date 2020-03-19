package io.fritz2.flow

/*
 * from https://github.com/RBusarow/kotlinx.coroutines/blob/SharedFlow/kotlinx-coroutines-core/common/src/flow/internal/SharedFlow.kt
 * can be removed, when PR https://github.com/Kotlin/kotlinx.coroutines/pull/1716 is closed
 */

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@ExperimentalCoroutinesApi
internal fun <T> Flow<T>.asCachedFlow(): Flow<T> {

    var cache: T? = null

    return onEach { value ->
        // While flowing, also record all values in the cache.
        cache = value
    }.onStart {
        // Before emitting any values in sourceFlow,
        // emit any cached values starting with the oldest.
        cache?.let { emit(it) }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
internal fun <T> Flow<T>.asSharedFlow(scope: CoroutineScope = GlobalScope): Flow<T> = SharedFlow(this, scope)

/**
 * An auto-resetting [broadcast] flow.  It tracks the number of active collectors, and automatically resets when
 * the number reaches 0.
 *
 * `SharedFlow` has an optional [cache], where the latest _n_ elements emitted by the source Flow will be replayed to
 * late collectors.
 *
 * ### Upon reset
 * 1) The underlying [BroadcastChannel] is closed. A new BroadcastChannel will be created when a new collector starts.
 * 2) The cache is reset.  New collectors will not receive values from before the reset, but will generate a new cache.
 */
@FlowPreview
@ExperimentalCoroutinesApi
internal class SharedFlow<T>(
    private val sourceFlow: Flow<T>,
    private val scope: CoroutineScope
) : Flow<T> {

    private var refCount = 0
    private var cache: T? = null
    private val mutex = Mutex(false)

    @InternalCoroutinesApi
    public override suspend fun collect(
        collector: FlowCollector<T>
    ) = collector.emitAll(createFlow())

    // Replay happens per new collector, if cacheHistory > 0.
    private suspend fun createFlow(): Flow<T> = getChannel()
            .asFlow()
            .replayIfNeeded()
            .onCompletion { onCollectEnd() }

    private suspend fun getChannel(): BroadcastChannel<T> = mutex.withLock {
        refCount++
        lazyChannelRef.value
    }

    // lazy holder for the BroadcastChannel, which is reset whenever all collection ends
    private var lazyChannelRef = createLazyChannel()

    // must be lazy so that the broadcast doesn't begin immediately after a reset
    private fun createLazyChannel() = lazy(LazyThreadSafetyMode.NONE) {
        sourceFlow.cacheIfNeeded()
            // UNDISPATCHED is important, otherwise you have to wait before the first downstream flow can connect (otherwise there is a deadlock)
            .broadcastIn(scope, CoroutineStart.UNDISPATCHED)
    }

    private fun Flow<T>.replayIfNeeded(): Flow<T> =
        onStart {
            cache?.let { emit(it) }
        }

    private fun Flow<T>.cacheIfNeeded(): Flow<T> =
        onEach { value ->
            // While flowing, also record all values in the cache.
            cache = value
        }

    private suspend fun onCollectEnd() = mutex.withLock { if (--refCount == 0) reset() }

    private fun reset() {
        cache = null

        lazyChannelRef.value.cancel()
        lazyChannelRef = createLazyChannel()
    }
}