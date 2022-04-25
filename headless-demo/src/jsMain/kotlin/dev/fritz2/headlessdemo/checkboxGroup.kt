package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.fill
import dev.fritz2.core.storeOf
import dev.fritz2.headless.components.checkboxGroup
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLFieldSetElement

fun RenderContext.checkboxGroupDemo() {
    data class Newsletter(val id: Int, val title: String, val description: String, val users: Int)

    val mailingList = listOf(
        Newsletter(1, "Newsletter", "Last message sent an hour ago", 621),
        Newsletter(2, "Existing Customers", "Last message sent 2 weeks ago", 1200),
        Newsletter(3, "Trial Users", "Last message sent 4 days ago", 2740)
    )

    val subscriptions = storeOf(emptyList<Newsletter>(), id = "checkboxGroup")

    div("max-w-sm") {
        checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
            value(subscriptions)
            checkboxGroupLabel("text-base font-medium text-gray-900", tag = RenderContext::legend) {
                +"Select some mailing lists"
            }
            div("mt-4 grid grid-cols-1 gap-y-4") {
                mailingList.forEach { option ->
                    checkboxGroupOption(option, id = option.id.toString()) {
                        checkboxGroupOptionToggle(
                            """relative bg-white border rounded-lg shadow-sm p-2 flex cursor-pointer 
                            | focus:outline-none focus:border-2""".trimMargin(),
                            tag = RenderContext::label
                        ) {
                            className(selected.map {
                                if (it) "ring-2 ring-blue-600 border-transparent"
                                else "border-gray-300"
                            })
                            div("flex-1 flex") {
                                div("flex flex-col") {
                                    checkboxGroupOptionLabel("block text-sm font-medium text-gray-900") {
                                        +option.title
                                    }
                                    checkboxGroupOptionDescription("flex items-center text-sm text-gray-500") {
                                        +option.description
                                    }
                                    checkboxGroupOptionDescription("mt-2 text-sm font-medium text-gray-900") {
                                        +"${option.users} users"
                                    }
                                }
                            }
                            selected.render {
                                if (it) {
                                    svg("h-5 w-5 text-blue-700") {
                                        content(HeroIcons.check_circle)
                                        fill("currentColor")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        div("bg-gray-300 mt-4 p-2 rounded-lg ring-2 ring-gray-50") {
            em { +"Selected: " }
            ul("") {
                subscriptions.data.renderEach {
                    li { +"(${it.id}) ${it.title}" }
                }
            }
        }
    }
}