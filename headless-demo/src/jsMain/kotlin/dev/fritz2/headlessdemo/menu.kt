package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.storeOf
import dev.fritz2.core.transition
import dev.fritz2.headless.components.menu
import dev.fritz2.headless.foundation.utils.popper.Placement
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
                        """inline-flex justify-center items-center rounded border border-transparent 
                        | shadow-sm px-4 py-2.5 bg-primary-800 text-base font-sans text-white hover:bg-primary-900 
                        | focus:outline-none focus:ring-2 focus:ring-offset-1 focus:ring-primary-800 sm:col-start-2 
                        | sm:text-sm""".trimMargin()
                    ) {
                        +"Close Menu"
                        icon("w-5 h-5 ml-2 -mr-1", content = HeroIcons.chevron_down)
                    }
                }

                menuItems(
                    """w-56 max-h-56 overflow-y-auto border-white border-2 bg-white divide-y 
                    | divide-gray-100 rounded shadow-md 
                    | focus:outline-none origin-top-left""".trimMargin()
                ) {
                    placement = Placement.bottomStart
                    distance = 5

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
                            """group flex items-center w-full px-2 py-2 text-sm 
                            | disabled:opacity-50""".trimMargin()
                        ) {
                            className(active.combine(disabled) { a, d ->
                                if (a && !d) {
                                    "bg-primary-600 text-white"
                                } else {
                                    if (d) "text-slate-300" else "text-primary-800"
                                }
                            })
                            icon("w-5 h-5 mr-2", content = entry.icon)
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


        div("bg-primary-200 mt-4 p-2 rounded ring-2 ring-primary-500 text-sm text-primary-800 shadow-sm", id = "result") {
            span("font-semibold") { +"Execute Action: " }
            span { action.data.map { "$it file..." }.renderText() }
        }
    }
}
