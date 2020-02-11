package io.fritz2.examples.routing

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("") {

        val changePage = Handler<String> { model, newPage ->
            Router.set("page", newPage)
            model
        }

    }

    val myComponent = html {
        section {
            h1 {
                +"Page: "
                +Router.listenFor("page")
            }
            label {
                +"New Page"
                input {
                    store.changePage <= changes
                }
            }
        }
    }

    myComponent.mount("target")
}