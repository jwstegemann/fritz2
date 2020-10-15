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
fun <T> history(maxSize: Int = 10, initialValue: List<T> = emptyList()) =
    History<T>(maxSize, MutableStateFlow(null to initialValue))


/**
 * Keeps track of historical values (i.e. of a [Store]) and allows you to navigate back in history
 *
 * @param maxSize history keeps at most this many last values
 * @param history [MutableStateFlow] representing the history's state
 */
class History<T>(private val maxSize: Int, private val history: MutableStateFlow<Pair<T?, List<T>>>) :
    Flow<List<T>> by history.map({ it.second }) {

    /*
     * This method is only used when the history is synced to a store.
     * It keeps track of the current value of the store in the first part of the pair and the history in the second.
     * When a new update occurs it is store in first and the old first is pushed to the history.
     */
    private fun enqueue(entry: T) {
        history.value.let { old ->
            history.value = if (old.first != null) {
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
        history.value.also { old ->
            history.value = old.copy(second = push(entry, old.second))
        }
    }

    /**
     * gets the last value that has been added to the history.
     * Leaves history unmodified.
     *
     * @throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun last(): T = history.value.second.first()


    /**
     * gets the last value that has been added
     * and removes it from the history.
     *
     * @throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun back(): T = history.value.let { old ->
        history.value = (null to old.second.drop(1))
        old.second.first()
    }

    /**
     * clears the history.
     */
    fun reset() {
        history.value = null to emptyList()
    }

    /**
     * [Flow] describing, if a value is available in the history
     */
    val available by lazy { map { it.isNotEmpty() }.distinctUntilChanged() }

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
