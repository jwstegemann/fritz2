package io.fritz2.examples.remote

import io.fritz2.binding.RootStore
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.remote.RemoteStore
import io.fritz2.remote.get
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.url.URL
import org.w3c.fetch.Body
import kotlin.browser.window


data class QueryParams(val q: String)

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("start") {
        inner class Middleware<A,X>(val handler: Handler<X>, val mapping: suspend (A) -> Flow<X> ) {

            fun handle(actions: Flow<A>): Unit {
                handler.handle(actions.flatMapLatest(mapping))
            }

        }

        val callApi = Middleware<String, String>(update) {
            URL("https://api.github.com").get()
        }

        val test = Handler<String> {model, action ->
            console.log("model: $model")
            console.log("action: $action")
            action
        }

    }

    val myComponent = html {
        div {
            input {
                value = !"Hallo"
                store.callApi.handle(changes)
            }
            div {
                +"value: "
                store.data.bind()
            }
        }
    }

    myComponent.mount("target")
}