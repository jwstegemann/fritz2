package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.headless.components.switch
import dev.fritz2.headless.components.switchWithLabel
import dev.fritz2.headless.foundation.Aria
import kotlinx.coroutines.flow.map

fun RenderContext.switchDemo() {
    val switchState = storeOf(false, id = "switch")
    val switchWithLabelState = storeOf(true, id = "switchWithLabel")

    div("max-w-sm") {
        switch(
            """relative inline-flex flex-shrink-0 h-6 w-11
                | cursor-pointer rounded-full
                | border-2 border-transparent ring-1 ring-primary-400
                | transition-colors ease-in-out duration-200 
                | focus:outline-none focus:ring-4 focus:ring-primary-600
            """.trimMargin(),
        ) {
            value(switchState)
            className(enabled.map { if (it) "bg-primary-700" else "bg-primary-200" })
            span("sr-only") { +"Use setting" }
            span(
                """inline-block h-5 w-5 
                    | rounded-full bg-white shadow pointer-events-none 
                    | ring-0 
                    | transform transition ease-in-out duration-200
                """.trimMargin(),
            ) {
                className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
                attr(Aria.hidden, "true")
            }
        }

        switchWithLabel("flex items-center justify-between mt-4 p-4 bg-primary-200 rounded-lg") {
            value(switchWithLabelState)
            span("flex-grow flex flex-col") {
                switchLabel("block mb-1 text-sm font-medium text-primary-800", tag = RenderContext::span) {
                    +"Use fritz2 with tailwind?"
                }
                switchDescription("mt-1 text-xs text-primary-700", tag = RenderContext::span) {
                    +"The web's favourite utility-first CSS framework"
                }
            }
            switchToggle(
                """relative inline-flex flex-shrink-0 h-6 w-11
                | cursor-pointer rounded-full
                | border-2 border-transparent ring-1 ring-primary-400  
                | transition-colors ease-in-out duration-200 
                | focus:outline-none focus:ring-4 focus:ring-primary-600
                """.trimMargin(),
            ) {
                className(enabled.map { if (it) "bg-primary-700" else "bg-primary-300" })
                span("sr-only") { +"Use setting" }
                span(
                    """inline-block h-5 w-5 
                    | rounded-full bg-white shadow pointer-events-none 
                    | ring-0 
                    | transform transition ease-in-out duration-200
                    """.trimMargin(),
                ) {
                    className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
                    attr(Aria.hidden, "true")
                }
            }
        }
    }
}
