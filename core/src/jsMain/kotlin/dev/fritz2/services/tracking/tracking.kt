package dev.fritz2.services.tracking

import dev.fritz2.binding.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

/**
 * convenience method for creating a [Tracker]
 */
fun tracker(defaultTransaction: String = "...", debounceTimeout: Long = 100): Tracker =
    object : Tracker(defaultTransaction, debounceTimeout) {}

/**
 * tracks running transactions (e.g. inside a [Store])
 *
 * @param defaultTransaction default transactions text (used if not specified when [track] is called)
 * @param debounceTimeout denounces values in the [Flow] of running transaction by this value
 * @param state stores the actual running transaction or null
 */
abstract class Tracker(
    private val defaultTransaction: String,
    debounceTimeout: Long,
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