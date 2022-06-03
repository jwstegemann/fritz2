package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.headless.components.radioGroup
import dev.fritz2.headless.foundation.Aria
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLFieldSetElement


fun RenderContext.radioGroupDemo() {
    data class Plan(val name: String, val ram: String, val cpus: String, val disk: String, val price: String)

    val plans = listOf(
        Plan("Hobby", "8GB", "4 CPUs", "160 GB SSD disk", "€40"),
        Plan("Startup", "12GB", "6 CPUs", "256 GB SSD disk", "€80"),
        Plan("Business", "16GB", "8 CPUs", "512 GB SSD disk", "€160"),
        Plan("Enterprise", "32GB", "12 CPUs", "1024 GB SSD disk", "240€"),
    )

    val choice = storeOf<Plan?>(null, id = "radioGroup")

    div("max-w-sm") {
        radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
            value(choice)
            radioGroupLabel("block mb-2 ml-1 text-sm font-medium text-primary-800", tag = RenderContext::legend) {
                +"Select a server size"
            }
            div("space-y-1") {
                plans.forEach { option ->
                    radioGroupOption(option, "first:rounded-t-md last:rounded-b-md", option.name) {
                        className(selected.map {
                            if (it) "bg-primary-700 hover:none text-white"
                            else "bg-primary-100 hover:bg-primary-200 text-primary-800"
                        })

                        radioGroupOptionToggle(
                            """grid grid-rows-2 grid-cols-[auto_1fr_auto] gap-1 py-4 pl-3 pr-5
                                | text-base font-sans rounded-md cursor-pointer
                                | focus:outline-none focus-visible:ring-4 focus-visible:ring-primary-600""".trimMargin()
                        ) {
                            div("row-span-2 pr-2") {
                                div("flex items-center justify-center w-6 h-6 rounded-full") {
                                    className(selected.map {
                                        if(it) "bg-primary-800" else "bg-white"
                                    })
                                    span("h-3 w-3 bg-white rounded-full") {}
                                }
                            }

                            radioGroupOptionLabel("font-medium cursor-pointer") { +option.name }
                            radioGroupOptionDescription("font-medium") { +option.price }
                            radioGroupOptionDescription("text-sm") {
                                span("text-xs") { +option.cpus }
                                span("mx-1") {
                                    attr(Aria.hidden, "true")
                                    +"·"
                                }
                                span { +option.ram }
                            }
                            radioGroupOptionDescription("text-xs text-primary-400") { +"/month" }
                        }
                    }
                }
            }
        }

        div(
            "bg-primary-100 mt-4 p-2.5 rounded ring-2 ring-primary-500 text-sm text-primary-800 shadow-sm",
            id = "result"
        ) {
            span("font-medium") { +"Selected: " }
            span { choice.data.filterNotNull().map { "${it.name} ${it.cpus} ${it.ram} ${it.price}/mo" }.renderText() }
        }
    }
}
