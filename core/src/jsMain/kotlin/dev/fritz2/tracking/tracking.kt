package dev.fritz2.tracking

import dev.fritz2.binding.Store
import kotlinx.coroutines.flow.*

/**
 * convenience method for creating a [Tracker]
 */
fun tracker(defaultTransaction: String = "...", debounceTimeout: Long = 100): Tracker =
    Tracker(defaultTransaction, debounceTimeout)

/**
 * tracks running transactions (e.g. inside a [Store])
 *
 * @param defaultTransaction default transactions text (used if not specified when [track] is called)
 * @param debounceTimeout denounces values in the [Flow] of running transaction by this value
 * @param state stores the actual running transaction or null
 */
class Tracker(
    val defaultTransaction: String,
    private val debounceTimeout: Long,
    val state: MutableStateFlow<String?> = MutableStateFlow(null)
) : Flow<Boolean> by (state.debounce(debounceTimeout).distinctUntilChanged().map { it != null }) {

    /**
     * tracks a given operation
     *
     * @param transaction text describing the transaction
     * @param operation function to track
     */
    inline fun <T> track(transaction: String = defaultTransaction, operation: () -> T): T {
        state.value = transaction
        return operation().also { state.value = null }
    }

    /**
     * return a [Flow] to check, if a certain transaction is running
     *
     * @param transaction name of transaction to monitor
     */
    operator fun invoke(transaction: String): Flow<Boolean> =
        state.debounce(debounceTimeout).distinctUntilChanged().map {
            it != null && it == transaction
        }
}