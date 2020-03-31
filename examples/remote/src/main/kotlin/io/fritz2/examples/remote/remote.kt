package io.fritz2.examples.remote

import io.fritz2.binding.RootStore
import io.fritz2.binding.const
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.dom.values
import io.fritz2.remote.body
import io.fritz2.remote.onErrorLog
import io.fritz2.remote.remote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("start") {

        val sampleApi = remote("https://reqres.in/api/users")
            .acceptJson()

        val sampleGet = apply { s : String ->
            sampleApi.get(s)
                .onErrorLog()
                .body()
        } andThen update

        val samplePost = apply {s : String ->
            sampleApi.post(body = """
                {
                    "name": "$s",
                    "job": "programmer"
                }
            """.trimIndent())
                .onErrorLog()
                .body()
        } andThen update

    }

    val myComponent = html {
        div {
            label {
                text("get for id")
            }
            input {
                value = const("start")
                store.sampleGet <= changes.values()
            }
            hr { }
            label {
                text("post for id")
            }
            input {
                value = const("start")
                store.samplePost <= changes.values()
            }
            hr { }
            div {
                text("result: ")
                store.data.bind()
            }

        }
    }

    myComponent.mount("target")
}
