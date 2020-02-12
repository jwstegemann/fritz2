package io.fritz2.remote

import io.fritz2.binding.RootStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.w3c.dom.url.URL
import org.w3c.fetch.*
import kotlin.browser.window
import kotlin.js.json

@ExperimentalCoroutinesApi
@FlowPreview
open class RemoteStore<M>(initialValue: M) : RootStore<M>(initialValue) {

    inner class AsyncHandler<A>(inline val handler: (A) -> Deferred<(M) -> M>) {
        fun handle(actions: Flow<A>): Unit {
            GlobalScope.launch {
                actions.collect {
                    enqueue(handler(it).await())
                }
            }
        }

        suspend fun handle(action: A): Unit {
            enqueue(handler(action).await())
        }

        // syntactical sugar to write slot <= event-stream
        operator fun compareTo(flow: Flow<A>): Int {
            handle(flow)
            return 0
        }
    }


}


@ExperimentalCoroutinesApi
fun URL.get(): Flow<String> = flow {
    emit(window.fetch(this@get).await().text().await())
}