package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.headlessSwitch
import dev.fritz2.headless.components.headlessSwitchWithLabel
import dev.fritz2.headless.foundation.Aria
import kotlinx.coroutines.flow.map

fun RenderContext.switchDemo() {

    val switchState = storeOf(false)
    val switchWithLabelState = storeOf(true)

    headlessSwitch(
        """relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full 
        |cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 
        |focus:ring-offset-2 focus:ring-indigo-500""".trimMargin()
    ) {
        value(switchState)
        className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
        span("sr-only") { +"Use setting" }
        span(
            """pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 
            |transition ease-in-out duration-200""".trimMargin()
        ) {
            className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
            attr(Aria.hidden, "true")
        }
    }

    headlessSwitchWithLabel("w-96 bg-white flex items-center justify-between mt-4 p-4 rounded-xl") {
        value(switchWithLabelState)
        span("flex-grow flex flex-col") {
            switchLabel("text-sm font-medium text-gray-900", tag = RenderContext::span) {
                +"Available to hire"
            }
            switchDescription("text-sm text-gray-500", tag = RenderContext::span) {
                +"Nulla amet tempus sit accumsan."
            }
        }
        switchToggle(
            """relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full 
            |cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 
            |focus:ring-offset-2 focus:ring-indigo-500""".trimMargin()
        ) {
            className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
            span("sr-only") { +"Use setting" }
            span(
                """pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 
            |transition ease-in-out duration-200""".trimMargin()
            ) {
                className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
                attr(Aria.hidden, "true")
            }
        }
    }

}