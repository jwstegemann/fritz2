import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class Var<T>(initValue: T) {
    private val channel = ConflatedBroadcastChannel<T>(initValue)

    @FlowPreview
    fun flow(): Flow<T> = channel.asFlow().distinctUntilChanged()

    suspend fun set(value: T): Unit = channel.send(value)
}


abstract class SingleMountPoint<T>(upstream: Flow<T>) {
    init {
        GlobalScope.launch {
            upstream.collect {
                set(it, last)
                last = it
            }
        }
    }

    private var last: T? = null

    abstract fun set(value: T, last: T?): Unit
}
