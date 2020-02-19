package io.fritz2.examples.remote

import io.fritz2.binding.RootStore
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.remote.*
import kotlinx.coroutines.*
import kotlin.browser.window


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("start") {

        val sampleApi = remote("https://reqresss.in/api/users")
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
                    "job": "leader"
                }
            """.trimIndent())
                .onErrorLog()
                .body()
        } andThen update

    }

    val myComponent = html {
        div {
            label {
                +"get for id"
            }
            input {
                value = !"start"
                store.sampleGet <= changes
            }
            hre {  }
            label {
                +"post for id"
            }
            input {
                value = !"start"
                store.samplePost <= changes
            }
            hre {  }
            div {
                +"result: "
                store.data.bind()
            }

        }
    }

    myComponent.mount("target")
}
