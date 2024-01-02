package dev.fritz2.headlessdemo.components


import dev.fritz2.core.RenderContext
import dev.fritz2.core.transition
import dev.fritz2.headless.components.popOver
import dev.fritz2.headless.foundation.PopUpPanelSize
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.core.MiddlewareReturn
import dev.fritz2.headless.foundation.utils.floatingui.core.MiddlewareState
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.offset
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlin.js.Promise


fun RenderContext.popOverDemo() {
    data class Solution(val name: String, val description: String, val icon: RenderContext.() -> Unit)

    val solutions = listOf(
        Solution(
            "fritz2", "Cool web framework for building modern SPAs",
            { fritz2("w-10 h-10 text-primary-800") }
        ),
        Solution(
            "Headless", "Create fully functional and customized components",
            { icon("w-10 h-10 text-primary-800", content = HeroIcons.academic_cap) }
        ),
        Solution(
            "Tailwind", "Nice CSS framework for styling your application",
            { icon("w-10 h-10 text-primary-800", content = HeroIcons.color_swatch) }
        )
    )

    popOver(id = "popOver") {
        popOverButton(
            """inline-flex justify-center w-40 px-4 py-2 sm:col-start-2
            | rounded shadow-sm bg-primary-800
            | border border-transparent
            | text-sm text-white
            | hover:bg-primary-900
            | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
        ) {
            className(opened.map { if (it) "" else "text-opacity-90" })
            opened.map { if (it) "Close Popover" else "Open Popover" }.renderText()
            opened.render { isOpen ->
                icon("w-5 h-5 ml-2 -mr-1", content = if (isOpen) HeroIcons.chevron_up else HeroIcons.chevron_down)
            }
        }

        popOverPanel(
            """z-30 max-w-sm lg:max-w-3xl px-0  
            | bg-white overflow-hidden rounded-lg shadow-lg ring-1 ring-black ring-opacity-5
            | focus:outline-none
            """.trimMargin()
        ) {
            placement = PlacementValues.bottomStart
            addMiddleware(offset(5))

            transition(
                opened,
                "transition ease-out duration-200",
                "opacity-0 translate-y-1",
                "opacity-100 translate-y-0",
                "transition ease-in duration-150",
                "opacity-100 translate-y-0",
                "opacity-0 translate-y-1"
            )

            div("relative grid gap-8 p-7 lg:grid-cols-2") {
                solutions.forEach { item ->
                    a(
                        """flex items-center p-2 -m-3 
                        | transition duration-150 ease-in-out rounded-lg 
                        | hover:bg-primary-200 
                        | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
                    ) {
                        attr("key", "{$item.name}")
                        attr("tabindex", "0")
                        div(
                            """flex items-center justify-center flex-shrink-0 w-10 h-10 sm:h-12 sm:w-12 p-1 
                            | rounded-lg 
                            | bg-primary-100""".trimMargin()
                        ) {
                            item.icon(this)
                        }
                        div("ml-4") {
                            p("text-sm font-medium text-primary-900") { +item.name }
                            p("text-sm text-primary-800") { +item.description }
                        }
                    }
                }

            }
            div("flow-root p-4 transition duration-150 ease-in-out bg-primary-100") {
                span("flex items-center") {
                    span("text-sm font-medium text-primary-900") {
                        +"Advice"
                    }
                }
                span("block text-sm text-primary-800") {
                    +"Start using a powerful tech-stack for beautiful, modern SPAs."
                }
            }
        }
    }
}
