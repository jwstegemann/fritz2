package dev.fritz2.examplesdemo

import dev.fritz2.core.RenderContext

fun RenderContext.start() {
    div("p-4") {
        h2 {
            +"Hello Peter!"
        }
    }
}
