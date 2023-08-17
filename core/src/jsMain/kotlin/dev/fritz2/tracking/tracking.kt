package dev.fritz2.tracking

import dev.fritz2.core.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

/**
 * convenience method for creating a [Tracker]
 */
fun tracker(debounceTimeout: Long = 100): Tracker = Tracker(debounceTimeout)

/**
 * tracks running transactions (e.g. inside a [Store])
 *
 * @param debounceTimeout denounces values in the [Flow] of running transaction by this value
 */
class Tracker(
    private val debounceTimeout: Long,
) {

    private val state: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Gives a [Flow] to check if a transaction is running.
     */
    val data: Flow<Boolean> = state.debounce(debounceTimeout)

    /**
     * Represents the current transaction which is running or null.
     */
    val current: Boolean
        get() = state.value

    /**
     * Tracks a given [operation].
     *
     * Works also with unsafe operations that throw exceptions, as the tracking gets stopped. The exceptions are
     * not swallowed though.
     *
     * @param operation function to track
     */
    suspend fun <T> track(operation: suspend () -> T): T {
        state.value = true
        return try {
            operation()
        } finally {
            state.value = false
        }
    }
}
