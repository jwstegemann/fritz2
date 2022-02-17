package dev.fritz2.headlessdemo


import dev.fritz2.core.RenderContext
import dev.fritz2.core.fill
import dev.fritz2.core.transition
import dev.fritz2.core.viewBox
import dev.fritz2.headless.components.popOver
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.utils.popper.Placement
import kotlinx.coroutines.flow.map


fun RenderContext.popOverDemo() {
    data class Solution(val name: String, val description: String, val icon: String)

    val solutions = listOf(
        Solution("Insights", "Measure actions your users take", HeroIcons.academic_cap),
        Solution("Automations", "Create your own targeted content", HeroIcons.adjustments),
        Solution("Reports", "Keep track of your growth", HeroIcons.archive)
    )

    popOver {
        popOverButton(
            """text-white group bg-indigo-600 px-3 py-2 rounded-md inline-flex items-center text-base 
                | font-medium hover:text-opacity-100 focus:outline-none focus-visible:ring-2 hover:bg-indigo-700 
                | focus-visible:ring-indigo focus-visible:ring-opacity-75""".trimMargin()
        ) {
            className(opened.map { if (it) "" else "text-opacity-90" })
            span { +"Solutions" }
            svg("ml-2 h-5 w-5 group-hover:text-opacity-80 transition ease-in-out duration-150") {
                content(HeroIcons.chevron_down)
                fill("currentColor")
            }
        }

        popOverPanel("z-10 w-screen max-w-sm px-4 sm:px-0 lg:max-w-3xl") {
            placement = Placement.bottomStart

            transition(opened,
                "transition ease-out duration-200",
                "opacity-0 translate-y-1",
                "opacity-100 translate-y-0",
                "transition ease-in duration-150",
                "opacity-100 translate-y-0",
                "opacity-0 translate-y-1"
            )

            div("overflow-hidden rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 origin-top-left") {

                div("relative grid gap-8 bg-white p-7 lg:grid-cols-2") {
                    solutions.forEach { item ->
                        a(
                            """flex items-center p-2 -m-3 transition duration-150 ease-in-out rounded-lg 
                                |hover:bg-gray-50 focus:outline-none focus-visible:ring focus-visible:ring-indigo-500 
                                |focus-visible:ring-opacity-50""".trimMargin()
                        ) {
                            attr("key", "{$item.name}")
                            div(
                                """flex items-center justify-center flex-shrink-0 w-10 h-10 p-1 rounded-lg 
                                    |bg-orange-100 text-orange-600 sm:h-12 sm:w-12""".trimMargin()
                            ) {
                                svg {
                                    content(item.icon)
                                    attr(Aria.hidden, "true")
                                    fill("currentColor")
                                    viewBox("0 0 20 20")
                                }
                            }
                            div("ml-4") {
                                p("text-sm font-medium text-gray-900") { +item.name }
                                p("text-sm text-gray-500") { +item.description }
                            }
                        }
                    }

                }
                div("p-4 bg-gray-50") {
                    a(
                        """flow-root px-2 py-2 transition duration-150 ease-in-out rounded-md hover:bg-gray-100 
                            |focus:outline-none focus-visible:ring focus-visible:ring-indigo-500 
                            |focus-visible:ring-opacity-50""".trimMargin()
                    ) {
                        span("flex items-center") {
                            span("text-sm font-medium text-gray-900") {
                                +"Documentation"
                            }
                        }
                        span("block text-sm text-gray-500") {
                            +"Start integrating products and tools"
                        }
                    }
                }

            }
        }
    }
}
