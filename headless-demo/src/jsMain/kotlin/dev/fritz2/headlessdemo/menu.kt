package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.headlessMenu
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

    val action = storeOf("")

    div("w-72 mb-4") {
        div("w-full h-72") {
            headlessMenu("inline-block text-left") {
                openClose(storeOf(false))
                div {
                    menuButton(
                        """inline-flex justify-center w-full px-4 py-2 text-sm font-medium text-black 
                        | bg-white rounded-md focus:outline-none focus-visible:ring-2 
                        | focus-visible:ring-white focus-visible:ring-opacity-75""".trimMargin()
                    ) {
                        +"Options"
                        svg("w-5 h-5 ml-2 -mr-1") {
                            content(HeroIcons.chevron_down)
                        }
                    }
                }

                menuItems(
                    """w-56 max-h-56 overflow-y-auto border-white border-2 bg-white divide-y 
                    | divide-gray-100 rounded-md shadow-lg ring-1 ring-black ring-opacity-5 
                    | focus:outline-none origin-top-left""".trimMargin()
                ) {
                    placement = Placement.bottomStart

                    //tag.className(Visibility.dropOn(opened))

                    entries.forEach { entry ->
                        menuItem(
                            """group flex rounded-md items-center w-full px-2 py-2 text-sm 
                            | disabled:opacity-50""".trimMargin()
                        ) {
                            tag.apply {
                                className(active.combine(disabled) { a, d ->
                                    if (a && !d) {
                                        "bg-violet-500 text-white"
                                    } else {
                                        if (d) "text-gray-300" else "text-gray-900"
                                    }
                                })
                                svg("w-5 h-5 mr-2") { content(entry.icon) }
                                +entry.label
                                if (entry.disabled) disable(true)
                                selected.map { entry.label } handledBy action.update
                            }
                        }
                    }
                }
            }
        }


        div("bg-gray-300 mt-4 p-2 rounded-lg ring-2 ring-gray-50") {
            em { +"Execute Action: " }
            span { action.data.map { "$it file..." }.renderText() }
        }
    }
}
