package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.menu
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

menu {
    menuButton { /* ... */ }

    menuItems {
        entries.forEach { entry ->
            menuItem {
                className(active.combine(disabled) { a, d ->
                    if (a && !d) "bg-violet-500 text-white"
                    else {
                        if (d) "text-gray-300" else "text-gray-900"
                    }
                })

                svg { content(entry.icon) }
                +entry.label

                if (entry.disabled) disable(true)

                selected.map { entry.label } handledBy action.update
            }
        }
    }
}

}
