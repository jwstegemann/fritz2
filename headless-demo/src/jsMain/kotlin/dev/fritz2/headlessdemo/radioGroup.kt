package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.headlessRadioGroup
import dev.fritz2.headless.foundation.Aria
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

fun RenderContext.radiogroupDemo() {
    data class Plan(val name: String, val ram: String, val cpus: String, val disk: String, val price: String)

    val plans = listOf(
        Plan("Hobby", "8GB", "4 CPUs", "160 GB SSD disk", "€40"),
        Plan("Startup", "12GB", "6 CPUs", "256 GB SSD disk", "€80"),
        Plan("Business", "16GB", "8 CPUs", "512 GB SSD disk", "€160"),
        Plan("Enterprise", "32GB", "12 CPUs", "1024 GB SSD disk", "240€"),
    )

    val choice = storeOf<Plan?>(null)

    div("w-96 m-4") {
        headlessRadioGroup<FieldSet, Plan?>(tag = RenderContext::fieldset) {
            value(choice)
            options = plans
            withKeyboardNavigation()
            radioGroupLabel("sr-only") { +"Server size" }
            div("space-y-2") {
                plans.forEach { option ->
                    radioGroupOption(option) {
                        radioGroupOptionToggle(
                            """relative block bg-white border rounded-lg shadow-sm px-6 py-2 cursor-pointer 
                            | sm:flex sm:justify-between focus:outline-none""".trimMargin(),
                            tag = RenderContext::label
                        ) {
                            className(selected.map {
                                if (it) "ring-2 ring-indigo-500 border-transparent"
                                else "border-gray-300"
                            })
                            div("flex items-center") {
                                div("text-sm") {
                                    radioGroupOptionLabel("font-medium text-gray-900", tag = RenderContext::p) {
                                        +option.name
                                    }
                                    radioGroupOptionDescription("text-gray-500", tag = RenderContext::div) {
                                        p("sm:inline") { +option.cpus }
                                        span("hidden sm:inline sm:mx-1") {
                                            attr(Aria.hidden, "true")
                                            +"·"
                                        }
                                        p("sm:inline") { +option.ram }
                                    }
                                }
                            }
                            radioGroupOptionDescription(
                                "mt-2 flex text-sm sm:mt-0 sm:block sm:ml-4 sm:text-right",
                                tag = RenderContext::div
                            ) {
                                div("font-medium text-gray-900") { +option.price }
                                div("ml-1 text-gray-500 sm:ml-0") { +"""/mo""" }
                            }
                        }
                    }
                }
            }
        }

        div("bg-gray-300 mt-4 px-2 rounded-lg") {
            em { +"Selected: " }
            choice.data.filterNotNull().map { "${it.name} ${it.cpus}·${it.ram}·${it.price}" }.renderText()
        }
    }
}