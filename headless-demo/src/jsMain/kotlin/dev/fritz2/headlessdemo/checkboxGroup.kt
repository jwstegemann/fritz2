package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.headlessCheckboxGroup
import kotlinx.coroutines.flow.map

fun RenderContext.checkboxDemo() {
    data class Newsletter(val id: Int, val title: String, val description: String, val users: Int)

    val mailingList = listOf(
        Newsletter(1, "Newsletter", "Last message sent an hour ago", 621),
        Newsletter(2, "Existing Customers", "Last message sent 2 weeks ago", 1200),
        Newsletter(3, "Trial Users", "Last message sent 4 days ago", 2740)
    )

    val subscriptions = storeOf(emptyList<Newsletter>())

    div("w-96 m-4") {
        headlessCheckboxGroup<FieldSet, Newsletter>(tag = RenderContext::fieldset) {
            value(subscriptions)
            withKeyboardNavigation()
            checkboxGroupLabel("text-base font-medium text-gray-900", tag = RenderContext::legend) {
                +"Select some mailing lists"
            }
            div("mt-4 grid grid-cols-1 gap-y-4") {
                mailingList.forEach { option ->
                    checkboxGroupOption(option) {
                        checkboxGroupOptionToggle(
                            """relative bg-white border rounded-lg shadow-sm p-2 flex cursor-pointer 
                            | focus:outline-none focus:border-2""".trimMargin(),
                            tag = RenderContext::label
                        ) {
                            className(selected.map {
                                if (it) "ring-2 ring-indigo-500 border-transparent"
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
                                    svg("h-5 w-5 text-indigo-600") {
                                        xmlns("http://www.w3.org/2000/svg")
                                        viewBox("0 0 20 20")
                                        fill("currentColor")
                                        attr("aria-hidden", "true")
                                        path {
                                            attr("fill-rule", "evenodd")
                                            d(
                                                """M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 
                                            | 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"""
                                                    .trimMargin()
                                            )
                                            attr("clip-rule", "evenodd")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        div("bg-gray-300 mt-4 px-2 rounded-lg") {
            em { +"Selected: " }
            ul("") {
                subscriptions.data.renderEach {
                    li { +"(${it.id}) ${it.title}" }
                }
            }
        }
    }
}