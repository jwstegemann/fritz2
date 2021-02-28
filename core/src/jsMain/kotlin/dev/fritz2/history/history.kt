package dev.fritz2.history

import dev.fritz2.binding.Store
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlin.math.min


/**
 * factory-method to create a [History] (i.e. for a [Store])
 *
 * @param maxSize history keeps at most this many last values
 * @param initialValue initial content of the history
 */
fun <T> history(maxSize: Int = 10, initialValue: List<T> = emptyList()) = History(maxSize, initialValue)


/**
 * Keeps track of historical values (i.e. of a [Store]) and allows you to navigate back in history
 *
 * @param maxSize history keeps at most this many last values
 * @param initialValue initial content of the history
 */
class History<T>(
    private val maxSize: Int,
    initialValue: List<T>
) {

    private val state: MutableStateFlow<Pair<T?, List<T>>> = MutableStateFlow(null to initialValue)

    /**
     * Gives a [Flow] with the entries of the history.
     */
    val data: Flow<List<T>> = state.map { it.second }

    /**
     * Represents the current entries in history.
     */
    val current: List<T>
        get() = state.value.second

    /**
     * This method is only used when the history is synced to a store.
     * It keeps track of the current value of the store in the first part of the pair and the history in the second.
     * When a new update occurs it is store in first and the old first is pushed to the history.
     */
    private fun enqueue(entry: T) {
        state.value.let { old ->
            state.value = if (old.first != null) {
                old.copy(entry, push(old.first!!, old.second))
            } else {
                old.copy(first = entry)
            }
        }
    }

    private fun push(entry: T, oldList: List<T>): List<T> =
        if (oldList.isEmpty() || entry != oldList.first()) {
            buildList {
                add(entry)
                if (oldList.isNotEmpty()) {
                    addAll(oldList.subList(0, min(maxSize - 1, oldList.size)))
                }
            }
        } else oldList

    /**
     * adds a new value to the history
     *
     * @param entry value to add
     */
    fun add(entry: T) {
        state.value.also { old ->
            state.value = old.copy(second = push(entry, old.second))
        }
    }

    /**
     * gets the last value that has been added to the history.
     * Leaves history unmodified.
     *
     * @throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun last(): T = state.value.second.first()


    /**
     * gets the last value that has been added
     * and removes it from the history.
     *
     * @throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun back(): T = state.value.let { old ->
        state.value = (null to old.second.drop(1))
        old.second.first()
    }

    /**
     * clears the history.
     */
    fun reset() {
        state.value = null to emptyList()
    }

    /**
     * [Flow] describing, if a value is available in the history
     */
    val available by lazy { data.map { it.isNotEmpty() }.distinctUntilChanged() }

    /**
     * syncs this history to a given store, so that each update is automatically stored in history.
     *
     * @param upstream [Store] to sync with
     */
    fun sync(upstream: Store<T>): History<T> = this.apply {
        upstream.data.onEach { enqueue(it) }.catch { t ->
            console.error("ERROR[history@${upstream.id}]: ${t.message}", t)
        }.launchIn(MainScope())
    }
}
