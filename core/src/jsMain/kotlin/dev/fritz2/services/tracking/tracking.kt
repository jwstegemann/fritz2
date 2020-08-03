package dev.fritz2.services.tracking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

/**
 * tracks running transactions (inside a [Store])
 *
 * @param defaultTransaction default transactions text (used if not specified when [track] is called)
 * @param debounceTimeout debounces values in the [Flow] of running transaction by this value
 * @param state stores the actual running transaction or null
 */
class Tracker(
    private val defaultTransaction: String = "...",
    debounceTimeout: Long = 100,
    private val state: MutableStateFlow<String?> = MutableStateFlow(null)
) : Flow<String?> by state.debounce(debounceTimeout) {

    /**
     * tracks a given operation
     *
     * @param transaction text describing the transaction
     * @param operation function to track
     */
    suspend fun <T> track(transaction: String = defaultTransaction, operation: suspend () -> T): T {
        state.value = transaction
        return operation().also { state.value = null }
    }

}