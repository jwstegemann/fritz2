package dev.fritz2.examplesdemo

import dev.fritz2.core.RenderContext

fun RenderContext.greeter() {

    fun RenderContext.greet(name: String) {
        div {
            h2 {
                +"Hello $name!"
            }
        }
    }

    greet("Peter")
}