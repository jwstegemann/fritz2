package dev.fritz2.examplesdemo

import dev.fritz2.core.RenderContext

fun RenderContext.simple() {

    fun RenderContext.greet(name: String) {
        h2 {
            +"Hello $name!"
        }
    }

    div("p-4") {
        greet("Peter")
        greet("Paul")
        greet("Marry")
    }
}