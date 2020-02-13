package io.fritz2.examples.remote

import io.fritz2.binding.RootStore
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.remote.body
import io.fritz2.remote.get
import io.fritz2.remote.onError
import io.fritz2.remote.post
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

        val callApi = apply { s : String ->
            URL("https://reqres.in/api/users/$s").get().onError { window.alert("error fetching data: ${it.statusCode}, ${it.body}") }
                .body()
        } andThen update

        val postUpdate = apply {s : String ->
            URL("https://reqres.in/api/users").post("""
                {
                    "name": "$s",
                    "job": "leader"
                }
            """.trimIndent()).body()
        } andThen update

    }

    val myComponent = html {
        div {
            input {
                value = !"hgfhgfhgf"
                store.postUpdate <= changes
            }
            div {
                +"value: "
                store.data.bind()
            }
        }
    }

    myComponent.mount("target")
}
