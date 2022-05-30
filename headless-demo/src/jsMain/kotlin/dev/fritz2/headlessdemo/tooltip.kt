package dev.fritz2.headlessdemo


import dev.fritz2.core.RenderContext
import dev.fritz2.core.placeholder
import dev.fritz2.core.type
import tooltip


fun RenderContext.tooltipButton(idPrefix: String) {
    button(
        """w-32 inline-flex justify-center rounded border border-transparent 
            | shadow-sm px-4 py-2 bg-primary-800 text-base text-white hover:bg-primary-900 
            | focus:outline-none focus:ring-4 focus:ring-primary-600 sm:col-start-2 
            | sm:text-sm""".trimMargin(),
        id = "$idPrefix-reference"
    ) {
        +"Some Button"
    }.tooltip("text-sm text-white bg-slate-400 px-2 py-1 rounded", id = "$idPrefix-tooltip") {
        arrow()
        +"Some more Information"
    }
}

fun RenderContext.tooltipInput(idPrefix: String) {
    input(
        """focus:ring-4 focus:ring-primary-600 block w-32 h-10 sm:text-sm text-primary-800
            | border border-primary-600 focus:outline-none hover:border-primary-800 placeholder:text-slate-400 rounded""".trimMargin(),
        id = "$idPrefix-reference"
    ) {
        placeholder("some input")
        type("text")
    }.tooltip("text-sm text-white bg-slate-400 px-2 py-1 rounded", id = "$idPrefix-tooltip") {
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
