package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
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
            checkboxGroupLabel("py-2 sr-only", tag = RenderContext::legend) {
                +"Select some mailing lists"
            }
            div("space-y-2") {
                mailingList.forEach { option ->
                    checkboxGroupOption(option, id = option.id.toString()) {
                        checkboxGroupOptionToggle(
                            """grid grid-rows-3 grid-cols-[auto_1fr] gap-1 p-4
                                | text-base font-sans rounded-md cursor-pointer
                                | focus:outline-none focus-visible:ring-4 focus-visible:ring-primary-600""".trimMargin()
                        ) {

                            className(selected.map {
                                if (it) "bg-primary-700 hover:bg-primary-800 text-white"
                                else "bg-primary-100 hover:bg-primary-200 text-primary-800"
                            })

                            div("row-span-3 pr-2") {
                                div("""flex items-center w-5 h-5 mt-0.5
                                        | bg-white border rounded""".trimMargin()) {
                                    selected.render(into = this) {
                                        if (it) icon("text-primary-700", content = HeroIcons.check)
                                    }
                                }
                            }
                            checkboxGroupOptionLabel("font-medium cursor-pointer") { +option.title }
                            checkboxGroupOptionDescription("text-sm text-primary-800") { +option.description }
                            checkboxGroupOptionDescription("mt-2 text-xs text-primary-400") {
                                +"${option.users} users"
                            }
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
            span { subscriptions.data.map { s -> s.joinToString { it.title } }.renderText() }
        }
    }
}