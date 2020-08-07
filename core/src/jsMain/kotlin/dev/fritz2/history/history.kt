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

    init {
        history.onEach {
            console.log("### history:")
            console.log("###    -> ${it.first}")
            console.log("###    -> ${it.second}")
            console.log("")
        }.launchIn(MainScope())
    }

    private fun enqueue(entry: T) {
        val oldHistory = history.value
        val newHistory = if (oldHistory.first != null) {
            oldHistory.copy(entry, push(oldHistory.first!!, oldHistory.second))
        } else {
            oldHistory.copy(first = entry)
        }
        history.value = newHistory
    }

    private fun push(entry: T, oldList: List<T>): List<T> =
        if (oldList.isEmpty() || entry != oldList.first()) {
            buildList {
                add(entry)
                if (isNotEmpty()) {
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
        console.log("ADD ENTRY")
        try {
            val oldHistory = history.value
            val newHistory = oldHistory.copy(second = push(entry, oldHistory.second))
            history.value = newHistory
        } catch (t: Throwable) {
            console.error("ERRRROOOORRRR: ${t.message}", t)
        }
    }

    /**
     * gets the last value that has been added to the history.
     * Leaves history unmodified.
     * Throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun last(): T = history.value.second.first()


    /**
     * gets the last value that has been added
     * and removes it from the history.
     * Throws [IndexOutOfBoundsException] if called on an empty history.
     */
    fun back(): T {
        val result = history.value.second.first()
        val old = history.value.first
        console.log("++++ result $result")
        val newHistory = history.value.second.drop(1)
        console.log("++++ new history $newHistory")
        history.value = null to newHistory
        return result
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
     */
    fun sync(upstream: Store<T>): History<T> = this.apply {
        upstream.data.onEach { enqueue(it) }.catch { t ->
            console.error("ERROR[history@${upstream.id}]: ${t.message}", t)
        }.launchIn(upstream)
    }
}
