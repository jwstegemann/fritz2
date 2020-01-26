package io.fritz2.examples.todomvc

import io.fritz2.binding.Store
import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import io.fritz2.binding.map


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