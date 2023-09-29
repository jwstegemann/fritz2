package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.core.transition
import dev.fritz2.headless.components.menu
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.offset
import dev.fritz2.headless.foundation.utils.floatingui.utils.Placement
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import dev.fritz2.headlessdemo.result
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


fun RenderContext.menuDemo() {
    data class MenuEntry(val label: String, val icon: String, val disabled: Boolean = false)

    val entries = listOf(
        MenuEntry("Duplicate", HeroIcons.duplicate, true),
        MenuEntry("Archive", HeroIcons.archive),
        MenuEntry("Move", HeroIcons.share),
        MenuEntry("Delete", HeroIcons.trash),
        MenuEntry("Edit", HeroIcons.pencil),
        MenuEntry("Copy", HeroIcons.clipboard_copy, true),
        MenuEntry("Encrypt", HeroIcons.key)
    )

    val action = storeOf("", id = "selectedAction")

    div("max-w-sm") {
        div("w-full h-72") {
            menu("inline-block text-left", id = "menu") {
                div {
                    menuButton(
                        """inline-flex justify-center items-center sm:col-start-2 px-4 py-2.5   
                            | rounded shadow-sm
                            | border border-transparent 
                            | text-sm font-sans text-white 
                            | hover:bg-primary-900 
                            | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
                    ) {
                        className(opened.map { if (it) "bg-primary-900" else "bg-primary-800" })
                        opened.map { if (it) "Close Menu" else "Open Menu" }.renderText()
                        icon("w-5 h-5 ml-2 -mr-1", content = HeroIcons.chevron_down)
                    }
                }

                menuItems(
                    """w-56 max-h-56 overflow-y-auto origin-top-left z-30
                        | bg-white rounded shadow-md divide-y divide-gray-100
                        | border-white border-2  
                        | focus:outline-none""".trimMargin()
                ) {
                    placement = PlacementValues.bottomStart
                    addMiddleware(offset(5))

                    transition(
                        opened,
                        "transition-all duration-100 ease-ease-out",
                        "opacity-0 scale-95",
                        "opacity-100 scale-100",
                        "transition-all duration-100 ease-ease-out",
                        "opacity-100 scale-100",
                        "opacity-0 scale-95"
                    )

                    entries.forEach { entry ->
                        menuItem(
                            """group flex items-center w-full px-2 py-2
                                | disabled:opacity-50
                                | text-sm""".trimMargin()
                        ) {
                            className(active.combine(disabled) { a, d ->
                                if (a && !d) {
                                    "bg-primary-600 text-white"
                                } else {
                                    if (d) "text-slate-400" else "text-primary-800"
                                }
                            })
                            icon("w-4 h-4 mr-2", content = entry.icon)
                            +entry.label
                            if (entry.disabled) disable(true)
                            selected.map { entry.label } handledBy action.update

                            // only needed for automatic testing to explicitly expose the state
                            attr("data-menu-disabled", disabled.asString())
                            attr("data-menu-active", active.asString())
                        }
                    }
                }
            }
        }


        result {
            span("font-medium") { +"Execute Action: " }
            span { action.data.map { "$it file..." }.renderText() }
        }
    }
}
