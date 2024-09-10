package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.headless.components.tooltip

val loremIpsum = """
        Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore 
        et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. 
        Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.""".trimIndent()

fun RenderContext.stresstest() {
    div("grid grid-cols-10 gap-4") {
        (1..500).forEach {
            button("bg-gray-200 rounded-sm h-32") {
                +"Hover $it"
            }.tooltip("max-w-max") {
                arrow()
                div("max-w-96 p-4 bg-gray-500") {
                    h1("text-lg font-medium") {
                        +it.toString()
                    }
                    +loremIpsum
                }
            }
        }
    }
}