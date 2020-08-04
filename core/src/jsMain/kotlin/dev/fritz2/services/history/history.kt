package dev.fritz2.services.history

import dev.fritz2.binding.Store
import kotlinx.coroutines.flow.*
import kotlin.math.min


fun <T> history(maxSize: Int = 10, initialValue: List<T> = emptyList()) =
    History(maxSize, MutableStateFlow(initialValue))

class History<T>(val maxSize: Int, private val history: MutableStateFlow<List<T>>) : StateFlow<List<T>> by history {

    fun addEntry(entry: T) {
        val oldHistory = history.value
        if (oldHistory.isEmpty() || entry != oldHistory.first()) {
            history.value = buildList {
                add(entry)
                if (oldHistory.isNotEmpty()) {
                    addAll(oldHistory.subList(0, min(maxSize, oldHistory.size)))
                }
            }
        }
    }

    fun last(): T = history.value.first()

    fun back(): T = history.value.let {
        history.value = it.drop(1)
        it.first()
    }

    fun reset() {
        history.value = emptyList()
    }

    val available by lazy { map { it.isNotEmpty() }.distinctUntilChanged() }

    //FIXME: not working!
    fun sync(upstream: Store<T>, drop: Int = 1) {
        upstream.data.drop(drop).onEach { addEntry(it) }.launchIn(upstream)
    }
}
