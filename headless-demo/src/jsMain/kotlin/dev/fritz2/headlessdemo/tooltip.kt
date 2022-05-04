package dev.fritz2.headlessdemo


import dev.fritz2.core.RenderContext
import dev.fritz2.core.placeholder
import dev.fritz2.core.type
import tooltip


fun RenderContext.tooltipButton(idPrefix: String) {
    button(
        """w-32 inline-flex justify-center rounded-md border border-transparent 
            | shadow-sm px-4 py-2 bg-blue-700 text-base font-medium text-white hover:bg-blue-800 
            | focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-600 sm:col-start-2 
            | sm:text-sm""".trimMargin(),
        id = "$idPrefix-reference"
    ) {
        +"Some Button"
    }.tooltip("text-sm text-gray-50 bg-gray-700 px-2 py-1 rounded", id = "$idPrefix-tooltip") {
        arrow()
        +"Some more Information"
    }
}

fun RenderContext.tooltipInput(idPrefix: String) {
    input(
        """shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-32 h-10 sm:text-sm 
            | border-gray-300 rounded-md""".trimMargin(),
        id = "$idPrefix-reference"
    ) {
        placeholder("some input")
        type("text")
    }.tooltip("text-sm text-gray-50 bg-gray-700 px-2 py-1 rounded", id = "$idPrefix-tooltip") {
        arrow()
        +"Some more Information"
    }
}

fun RenderContext.tooltipDemo() {
    div("-m-4 w-screen h-screen flex flex-col items-stretch") {
        div("p-4 flex justify-center") { tooltipButton("top") }
        div("flex flex-row flex-1 items-center") {
            div("hidden sm:block p-4") { tooltipInput("left") }
            div("flex-1 p-4 flex justify-center") {
                tooltipButton("center")
            }
            div("hidden sm:block p-4") { tooltipButton("right") }
        }
        div("p-4 flex justify-center") { tooltipInput("bottom") }
    }
}
