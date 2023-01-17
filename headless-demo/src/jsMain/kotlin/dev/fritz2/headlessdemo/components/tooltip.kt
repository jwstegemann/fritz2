package dev.fritz2.headlessdemo.components


import dev.fritz2.core.RenderContext
import dev.fritz2.core.placeholder
import dev.fritz2.core.type
import dev.fritz2.headless.components.tooltip


fun RenderContext.tooltipButton(idPrefix: String) {
    button(
        """inline-flex justify-center w-32 px-4 py-2 sm:col-start-2
            | rounded shadow-sm bg-primary-800   
            | border border-transparent 
            | text-sm text-white 
            | hover:bg-primary-900 
            | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin(),
        id = "$idPrefix-reference"
    ) {
        +"Some Button"
    }.tooltip("px-2 py-1 bg-slate-400 rounded text-sm text-white", id = "$idPrefix-tooltip") {
        arrow()
        +"Some more Information"
    }
}

fun RenderContext.tooltipInput(idPrefix: String) {
    input(
        """block w-32 py-2.5 px-4 
            | bg-white rounded
            | border border-primary-600
            | font-sans text-sm text-primary-800 placeholder:text-slate-400 
            | hover:border-primary-800 
            | focus:outline-none focus:ring-4 focus:ring-primary-600 focus:border-primary-800""".trimMargin(),
        id = "$idPrefix-reference"
    ) {
        placeholder("some input")
        type("text")
    }.tooltip("px-2 py-1 bg-slate-400 rounded text-sm text-white", id = "$idPrefix-tooltip") {
        arrow()
        +"Some more Information"
    }
}

fun RenderContext.tooltipDemo() {
    div("w-screen h-screen flex flex-col items-stretch -m-4 ") {
        div("flex justify-center p-4") { tooltipButton("top") }
        div("flex flex-row flex-1 items-center") {
            div("hidden sm:block p-4") { tooltipInput("left") }
            div("flex flex-1 justify-center p-4") {
                tooltipButton("center")
            }
            div("hidden sm:block p-4") { tooltipButton("right") }
        }
        div("flex justify-center p-4") { tooltipInput("bottom") }
    }
}
