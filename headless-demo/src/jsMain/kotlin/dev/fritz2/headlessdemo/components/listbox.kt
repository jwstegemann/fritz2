package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.core.transition
import dev.fritz2.headless.components.listbox
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.flip
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.offset
import dev.fritz2.headless.foundation.utils.floatingui.obj
import dev.fritz2.headless.foundation.utils.floatingui.utils.Placement
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import dev.fritz2.headlessdemo.result
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
                """flex items-center justify-end w-full py-2.5 px-4 
                    | bg-white rounded cursor-default
                    | border border-primary-600 
                    | font-sans text-sm text-left text-primary-800
                    | hover:border-primary-800 
                    | focus:outline-none focus:ring-4 focus:ring-primary-600 focus:border-primary-800""".trimMargin()
            ) {
                span("block truncate w-full") {
                    value.data.renderText()
                }
                icon("w-5 h-5 text-gray-600", content = HeroIcons.selector)
            }

            listboxItems(
                """w-full max-h-60 py-1 overflow-auto origin-top z-30
                    | bg-white rounded shadow-md divide-y divide-gray-100
                    | ring-1 ring-primary-600 ring-opacity-5 
                    | focus:outline-none""".trimMargin(),
                        tag = RenderContext::ul
            ) {
                placement = PlacementValues.bottomStart
                addMiddleware(offset(5))

                transition(
                    opened,
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
                        """w-full relative py-2 pl-10 pr-4
                            | cursor-default select-none disabled:opacity-50
                            | text-sm""".trimMargin(),
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
                                span("absolute left-0 inset-y-0 flex items-center pl-3") {
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

        result {
            span("font-medium") { +"Selected: " }
            span { bestCharacter.data.renderText() }
        }
    }
}
