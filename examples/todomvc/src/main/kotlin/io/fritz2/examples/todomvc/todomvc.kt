package io.fritz2.examples.todomvc

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val myComponent = html {
        div {
            +"Here we go again!"
        }
    }

    myComponent.mount("target")
}