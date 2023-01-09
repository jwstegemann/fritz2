package dev.fritz2.examplesdemo

import dev.fritz2.core.*
import dev.fritz2.routing.routerOf

data class DemoPage(val title: String, val description: String, val content: RenderContext.() -> Unit)

val pages = mapOf(
    "start" to DemoPage(
        "Start",
        "Starting example showing fritz2 HTML DSL",
        RenderContext::start
    ),
    "simple" to DemoPage(
        "Simple",
        "Simple example showing how to structure your fritz2 code",
        RenderContext::simple
    ),
    "reactive" to DemoPage(
        "Reactive",
        "Reactive example showing fritz2 two-way data-binding",
        RenderContext::reactive
    ),
    "complex" to DemoPage(
        "Complex",
        "Complex example showing fritz2 validation",
        RenderContext::complex
    ),
    "fundamentals" to DemoPage(
        "Fundamentals",
        "small example to demonstrate the fundamental concepts and functions",
        RenderContext::fundamentals
    )
)

fun RenderContext.overview() {
    div("flex flex-col justify-start items-center h-screen") {
        h1("mb-8 tracking-tight font-bold text-gray-900 text-4xl") {
            span("block sm:inline") { +"fritz2" }
            span("block text-blue-800 sm:inline") { +" Examples Demos" }
        }
        div("w-3/4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-12") {
            pages.forEach {
                a(
                    """-m-3 p-3 flex items-start rounded-lg hover:bg-gray-50 hover:ring-2 hover:ring-white 
                    | ring-offset-2 ring-offset-amber-400 hover:outline-none shadow-lg rounded-lg bg-white 
                    | opacity-80 hover:opacity-100 transition ease-in-out duration-150""".trimMargin()
                ) {
                    href("#")
                    /* <!-- Heroicon name: outline/support --> */
                    svg("flex-shrink-0 h-6 w-6 text-blue-800") {
                        xmlns("http://www.w3.org/2000/svg")
                        fill("none")
                        viewBox("0 0 24 24")
                        attr("stroke", "currentColor")
                        attr("aria-hidden", "true")
                        path {
                            attr("stroke-linecap", "round")
                            attr("stroke-linejoin", "round")
                            attr("stroke-width", "2")
                            d(
                                """M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536  
                                | 9.192l-3.536 3.536M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-5 0a4 4 0 11-8 0 
                                | 4 4 0 018 0z""".trimMargin()
                            )
                        }
                    }
                    div("ml-4") {
                        p("text-base font-medium text-gray-900") { +it.value.title }
                        p("mt-1 text-sm text-gray-500") { +it.value.description }
                    }

                    href("#${it.key}")
                }
            }
        }
    }

}

fun main() {

    val router = routerOf("")

    render {
        router.data.render { route ->
            div("w-full h-screen bg-white p-4") {
                (pages[route]?.content ?: RenderContext::overview)()
            }
        }
    }
}
