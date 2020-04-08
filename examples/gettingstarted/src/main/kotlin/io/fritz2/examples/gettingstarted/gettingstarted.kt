package io.fritz2.examples.gettingstarted

import io.fritz2.binding.RootStore
import io.fritz2.binding.const
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.dom.values
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<String>("", id = "model") {
        val addADot = handle { model ->
            "$model."
        }
    }

    val gettingstarted = html {
        div {
            div("form-group") {
                label(`for` = store.id) {
                    text("Input")
                }
                input("form-control", id = store.id) {
                    placeholder = const("Add some input")
                    value = store.data

                    store.update <= changes.values()
                }
            }
            div("form-group") {
                label {
                    text("Value")
                }
                div("form-control") {
                    store.data.bind()
                    attr("readonly", "true")
                }
            }
            div("form-group") {
                button("btn btn-primary") {
                    text("Add a dot")
                    store.addADot <= clicks
                }
            }
        }
    }

    gettingstarted.mount("target")
}