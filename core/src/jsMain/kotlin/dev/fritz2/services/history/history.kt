package dev.fritz2.services.history

import dev.fritz2.binding.Store
import kotlinx.coroutines.flow.*
import kotlin.math.min


fun <T> history(maxSize: Int = 10, initialValue: List<T> = emptyList()) =
    History<T>(maxSize, MutableStateFlow(null to initialValue))


class History<T>(private val maxSize: Int, private val history: MutableStateFlow<Pair<T?, List<T>>>) :
    Flow<List<T>> by history.map({ it.second }) {

    private fun enqueue(entry: T) {
        history.value.also { old ->
            if (entry == old.first) return
            history.value = old.copy(
                entry, if (old.first != null) {
                    push(old.first!!, old.second)
                } else {
                    old.second
                }
            )
        }
    }

    private fun push(entry: T, old: List<T>): List<T> = old.ifEmpty {
        buildList {
            add(entry)
            if (old.isNotEmpty()) addAll(old.subList(0, min(maxSize, old.size)))
        }
    }

    fun add(entry: T) {
        history.value.also { old ->
            history.value = old.copy(second = push(entry, old.second))
        }
    }

    fun last(): T = history.value.second.first()

    fun back(): T = history.value.let {
        history.value = it.copy(null, it.second.drop(1))
        return it.second.first()
    }

    fun reset() {
        history.value = null to emptyList()
    }

    val available by lazy { map { it.isNotEmpty() }.distinctUntilChanged() }

    fun sync(upstream: Store<T>): History<T> = this.apply {
        upstream.data.onEach { enqueue(it) }.catch { t ->
            console.error("ERROR[history]: ${t.message}", t)
        }.launchIn(upstream)
    }
}
