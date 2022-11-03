package dev.fritz2.history

import dev.fritz2.core.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


/**
 * factory-method to create a [History]
 *
 * @param capacity max number of entries in history
 * @param initialValue initial content of the history
 */
fun <T> history(capacity: Int = 0, initialValue: List<T> = emptyList()) =
    History(capacity, initialValue)

/**
 * factory-method to create a [History] synced with the given [Store],
 * so that each update is automatically stored in history.
 *
 * @receiver [Store] to sync with
 * @param synced if true, the the history will sync with store updates
 * @param capacity max number of entries in history
 * @param initialEntries initial entries in history
 */
fun <D> Store<D>.history(capacity: Int = 0, initialEntries: List<D> = emptyList(), synced: Boolean = true) =
    History(capacity, initialEntries).apply {
        if(synced) this@history.data.handledBy { push(it) }
    }


/**
 * Keeps track of historical values (i.e. of a [Store]) and allows you to navigate back in history
 *
 * @param capacity max number of entries in history
 * @param initialEntries initial entries in history
 */
class History<T>(
    private val capacity: Int,
    initialEntries: List<T>,
) {
    init {
        require(initialEntries.size <= capacity) {
            "history: initialEntries size of ${initialEntries.size} is greater then capacity of $capacity"
        }
    }

    private val state: MutableStateFlow<List<T>> = MutableStateFlow(initialEntries)

    /**
     * Gives a [Flow] with the entries of the history.
     */
    val data: Flow<List<T>> = state

    /**
     * Represents the current entries in history.
     */
    val current: List<T> get() = state.value

    /**
     * Pushes a new [entry] to the history
     */
    fun push(entry: T) {
        if(state.value.isEmpty()) state.value = state.value + entry
        else if(state.value.last() != entry) state.value = state.value.let {
            if(it.size == capacity) it.drop(1) else it
        } + entry
    }

    /**
     * Gets the lastest history-entry that has been added
     * and removes it from the history.
     *
     * @throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun back(): T =
        if(state.value.size < 2) throw IndexOutOfBoundsException()
        else state.value.dropLast(1).also { state.value = it }.last()

    /**
     * clears the history.
     */
    fun clear() {
        if(state.value.isNotEmpty()) state.value = listOf(state.value.last())
    }

    /**
     * [Flow] describing, if a value is available in the history
     */
    val available by lazy { data.map { it.size > 1 }.distinctUntilChanged() }
}
