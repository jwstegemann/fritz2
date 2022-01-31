package dev.fritz2.headlessdemo.navigation

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headlessdemo.utils.HeroIcons
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private const val selectedClasses = "bg-primary-600 text-white"
private const val unselectedClasses = "text-white hover:bg-primary-400 hover:text-white"

// Menu visibility (mobile):
data class MenuVisibilityClasses(val background: String, val menu: String, val closeButton: String)

private val menuHiddenClasses = MenuVisibilityClasses(
    background = "opacity-0",
    menu = "-translate-x-full",
    closeButton = "opacity-0"
)
private val menuVisibleClasses = MenuVisibilityClasses(
    background = "opacity-100",
    menu = "translate-x-0",
    closeButton = "opacity-100"
)

// TODO: Combine stores (or at least hide-/show-calls) if possible

object MenuContentVisibilityStore : RootStore<MenuVisibilityClasses>(menuHiddenClasses) {
    val show = handle { menuVisibleClasses }
    val hide = handle { menuHiddenClasses }
}

object MenuContainerVisibilityStore : RootStore<String>("invisible") {
    val show = handle { "visible" }
    val hideDelayed = handle {
        delay(200L)
        "invisible"
    }
}

fun RenderContext.navigationEntry(
    navigation: Navigation,
    label: String = "",
    target: String = "",
    subpages: List<Page> = listOf()
) {

    val currentTarget = navigation.router.data

    val isAnythingSelected: Flow<Boolean> = currentTarget.map {
        target == it || subpages.any { sub -> sub.target == it }
    }


    fun <T> Flow<Boolean>.map(trueValue: T, falseValue: T): Flow<T> =
        this.map { if (it) trueValue else falseValue }

    fun highlightIfSelected(target: String): Flow<List<String>> =
        currentTarget
            .map { if (it == target) selectedClasses else unselectedClasses }
            .map { listOf(it) }


    fun RenderContext.subpage(name: String, target: String) {
        button("w-full group flex flex-grow items-center px-2 py-2 text-sm font-medium rounded-md") {
            // TODO: Animate
            classList(highlightIfSelected(target))

            svg("mr-2 w-4 h-4") {
                content(HeroIcons.link)
            }
            +name
            clicks handledBy MenuContentVisibilityStore.hide
            clicks handledBy MenuContainerVisibilityStore.hideDelayed
            clicks.map { target } handledBy navigation.router.navTo
        }
    }

    div {
        button("w-full flex group items-center px-2 py-2 text-base font-medium rounded-md") {
            classList(highlightIfSelected(target))

            span("flex flex-grow") { +label }
            if (subpages.isNotEmpty()) {
                isAnythingSelected.render {
                    svg("w-4 h-4") {
                        content(if (it) HeroIcons.chevron_down else HeroIcons.chevron_right)
                    }
                }
            }

            clicks handledBy MenuContentVisibilityStore.hide
            clicks handledBy MenuContainerVisibilityStore.hideDelayed
            clicks.map { target } handledBy navigation.router.navTo
        }

        if (subpages.isNotEmpty()) {
            div {
                classList(isAnythingSelected.map("", "hidden").map(::listOf))
                subpages.forEach { sub ->
                    subpage(sub.name, sub.target)
                }
            }
        }
    }

}
