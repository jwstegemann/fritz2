package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.core.transition
import dev.fritz2.headless.components.listbox
import dev.fritz2.headless.foundation.utils.popper.Placement
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


fun RenderContext.listboxDemo() {

    val characters = listOf(
        "Luke" to true,
        "Leia" to false,
        "Chewbakka" to false,
        "Han" to false,
        "C3-PO" to false,
        "R2-D2" to true,
        "Vader" to false,
        "Thrawn" to false
    )

    val bestCharacter = storeOf("Luke", id = "starwars")

    div("max-w-sm") {
        listbox<String>("h-72") {
            value(bestCharacter)
            listboxLabel("sr-only", tag = RenderContext::span) { +"Choose the best Star Wars character" }
            listboxButton(
                """flex items-center justify-end w-full py-2.5 px-4 text-left bg-white rounded
                | cursor-default focus:outline-none focus:ring-4
                | focus:ring-primary-600 border border-primary-600 font-sans text base
                | focus:border-primary-800
                  | text-primary-800""".trimMargin()
            ) {
                span("block truncate w-full") {
                    value.data.renderText()
                }
                icon("w-5 h-5 text-gray-600", content = HeroIcons.selector)
            }

            listboxItems(
                """w-full py-1 overflow-auto text-base bg-white rounded shadow-md max-h-60 ring-1 ring-primary-600 
                    | ring-opacity-5 focus:outline-none origin-top divide-y divide-gray-100""".trimMargin(),
                        tag = RenderContext::ul
            ) {
                placement = Placement.bottomStart
                distance = 5

                transition(opened,
                    "transition duration-100 ease-out",
                    "opacity-0 scale-95",
                    "opacity-100 scale-100",
                    "transition duration-100 ease-in",
                    "opacity-100 scale-100",
                    "opacity-0 scale-95"
                )

                characters.forEach { (entry, disabledState) ->
                    listboxItem(
                        entry,
                        "w-full cursor-default select-none relative py-2 pl-10 pr-4 text-sm disabled:opacity-50",
                        tag = RenderContext::li
                    ) {
                        className(active.combine(disabled) { a, d ->
                            if (a && !d) {
                                "bg-primary-600 text-white"
                            } else {
                                if (d) "text-slate-400" else "text-primary-800"
                            }
                        })

                        disable(disabledState)

                        span {
                            className(selected.map { if (it) "font-semibold" else "font-normal" })
                            +entry
                        }

                        selected.render {
                            if (it) {
                                span("absolute inset-y-0 left-0 flex items-center pl-3") {
                                    icon("w-5 h-5", content = HeroIcons.check)
                                }
                            }
                        }

                        // only needed for automatic testing to explicitly expose the state
                        attr("data-listbox-selected", selected.asString())
                        attr("data-listbox-disabled", disabledState.toString())
                        attr("data-listbox-active", active.asString())
                    }
                }
            }
        }

        div("bg-primary-100 mt-4 p-2 rounded ring-2 ring-primary-500 text-sm text-primary-800 shadow-sm", id = "result") {
            span("font-semibold") { +"Selected: " }
            span { bestCharacter.data.renderText() }
        }
    }
}
