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
                """flex items-center justify-end w-full py-2 pl-3 pr-3 text-left bg-white rounded-lg 
                | shadow-md cursor-default focus:outline-none focus:ring-2 focus:ring-opacity-75 
                | focus:ring-white focus:ring-offset-blue-700 focus:ring-offset-2 
                | focus:border-blue-600 sm:text-sm""".trimMargin()
            ) {
                span("block truncate w-full") {
                    value.data.renderText()
                }
                svg("w-5 h-5 text-gray-400") { content(HeroIcons.selector) }
            }

            listboxItems(
                """w-full py-1 overflow-auto text-base bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black 
                    | ring-opacity-5 focus:outline-none sm:text-sm""".trimMargin(),
                tag = RenderContext::ul
            ) {
                placement = Placement.bottomStart

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
                        "w-full cursor-default select-none relative py-2 pl-10 pr-4",
                        tag = RenderContext::li
                    ) {
                        className(active.combine(disabled) { a, d ->
                            if (a && !d) {
                                "text-blue-900 bg-blue-100"
                            } else {
                                if (d) "text-gray-300" else "text-gray-900"
                            }
                        })

                        disable(disabledState)

                        span {
                            className(selected.map { if (it) "font-medium" else "font-normal" })
                            +entry
                        }

                        selected.render {
                            if (it) {
                                span("text-blue-600 absolute inset-y-0 left-0 flex items-center pl-3") {
                                    svg("w-5 h-5") { content(HeroIcons.check) }
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

        div("bg-gray-300 mt-4 p-2 rounded-lg ring-2 ring-gray-50") {
            em { +"Selected: " }
            span { bestCharacter.data.renderText() }
        }
    }
}
