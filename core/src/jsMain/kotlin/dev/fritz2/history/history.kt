package dev.fritz2.history

import dev.fritz2.binding.Store
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlin.math.min


fun <T> history(maxSize: Int = 10, initialValue: List<T> = emptyList()) =
    History<T>(maxSize, MutableStateFlow(null to initialValue))


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
        console.log("ADD ENTRY")
        try {
            val oldHistory = history.value
            val newHistory = if (oldHistory.first != null) {
                oldHistory.copy(entry, push(oldHistory.first!!, oldHistory.second))
            } else {
                oldHistory.copy(first = entry)
            }
            history.value = newHistory
        } catch (t: Throwable) {
            console.error("ERRRROOOORRRR: ${t.message}", t)
        }
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

    fun last(): T = history.value.second.first()

    fun back(): T {
        val result = history.value.second.first()
        val old = history.value.first
        console.log("++++ result $result")
        val newHistory = history.value.second.drop(1)
        console.log("++++ new history $newHistory")
        history.value = null to newHistory
        return result
    }

    fun reset() {
        history.value = null to emptyList()
    }

    val available by lazy { map { it.isNotEmpty() }.distinctUntilChanged() }

    fun sync(upstream: Store<T>): History<T> = this.apply {
        upstream.data.onEach { enqueue(it) }.launchIn(upstream)
    }
}
