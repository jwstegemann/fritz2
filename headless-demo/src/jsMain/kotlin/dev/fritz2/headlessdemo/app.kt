package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.SHOW_COMPONENT_STRUCTURE
import dev.fritz2.headlessdemo.components.*
import dev.fritz2.headlessdemo.foundation.testTrapFocus
import dev.fritz2.routing.routerOf

sealed interface Page {
    val content: RenderContext.() -> Unit
}

data class DemoPage(val title: String, val description: String, override val content: RenderContext.() -> Unit) : Page

data class TestDrive(override val content: RenderContext.() -> Unit) : Page

val pages: Map<String, Page> = mapOf(
    "checkboxGroup" to DemoPage(
        "Headless Checkboxgroup",
        """Checkbox groups give you the same functionality as native HTML checkbox inputs, without any of the styling. 
            |They're perfect for building out custom UIs for multi selection.
        """.trimMargin(),
        RenderContext::checkboxGroupDemo,
    ),
    "dataCollection" to DemoPage(
        "Headless DataCollection",
        """A collection handles sorting, filtering of and selecting item form a collection.""".trimMargin(),
        RenderContext::collectionDemo,
    ),
    "disclosure" to DemoPage(
        "Headless Disclosure",
        """A simple, accessible foundation for building custom UIs that show and hide content, like togglable
            | accordion panels.
        """.trimMargin(),
        RenderContext::disclosureDemo,
    ),
    "inputfield" to DemoPage(
        "Headless Input",
        "Easily create accessible, fully customizable single line text inputs.",
        RenderContext::inputFieldDemo,
    ),
    "listbox" to DemoPage(
        "Headless Listbox",
        """Listboxes are a great foundation for building custom, accessible select menus for your app, 
            |complete with robust support for keyboard navigation.
        """.trimMargin(),
        RenderContext::listboxDemo,
    ),
    "menu" to DemoPage(
        "Headless Menu",
        """Menus offer an easy way to build custom, accessible dropdown components with robust support for keyboard
            | navigation.
        """.trimMargin(),
        RenderContext::menuDemo,
    ),
    "modal" to DemoPage(
        "Headless Modal",
        """Menus offer an easy way to build custom, accessible dropdown components with robust support for keyboard
            | navigation.
        """.trimMargin(),
        RenderContext::modalDemo,
    ),
    "popover" to DemoPage(
        "Headless Popover",
        """Popovers are perfect for floating panels with arbitrary content like navigation menus, mobile menus and
            | flyout menus.
        """.trimMargin(),
        RenderContext::popOverDemo,
    ),
    "radioGroup" to DemoPage(
        "Headless Radiogroup",
        """Radio Groups give you the same functionality as native HTML radio inputs, without any of the styling. 
            |They're perfect for building out custom UIs for single selection.
        """.trimMargin(),
        RenderContext::radioGroupDemo,
    ),
    "switch" to DemoPage(
        "Headless Switch",
        """Switches are a pleasant interface for toggling a value between two states, and offer the same 
            |semantics and keyboard navigation as native checkbox elements.
        """.trimMargin(),
        RenderContext::switchDemo,
    ),
    "tabGroup" to DemoPage(
        "Headless Tabs",
        """Easily create accessible, fully customizable tab interfaces, with robust focus management and keyboard
            | navigation support.
        """.trimMargin(),
        RenderContext::tabsDemo,
    ),
    "textarea" to DemoPage(
        "Headless Textarea",
        "Easily create accessible, fully customizable multi-line text inputs.",
        RenderContext::textAreaDemo,
    ),
    "tooltip" to DemoPage(
        "Headless Tooltip",
        "Some information that is displayed, whenever you hover a target element using your pointer device.",
        RenderContext::tooltipDemo,
    ),
    "toast" to DemoPage(
        "Headless Toast",
        "Display notification-like content in arbitrary positions on the screen.",
        RenderContext::toastDemo,
    ),
    "focus" to TestDrive(RenderContext::testTrapFocus),
)

fun RenderContext.overview() {
    div("flex flex-col justify-start items-center h-screen") {
        h1("mb-8 tracking-tight font-bold text-gray-900 text-4xl") {
            span("block sm:inline") { +"fritz2" }
            span("block text-primary-800 sm:inline") { +"Headless Demos" }
        }
        div("w-3/4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-12") {
            pages.filter { it.value is DemoPage }.map { (k, v) -> k to v as DemoPage }.forEach { (key, value) ->
                a(
                    """-m-3 p-3 pr-5 flex items-start rounded-lg hover:bg-gray-50 hover:ring-2 hover:ring-white 
                    | ring-offset-2 ring-offset-primary-600 hover:outline-none shadow-lg rounded-lg bg-white 
                    | opacity-80 hover:opacity-100 transition ease-in-out duration-150
                    """.trimMargin(),
                ) {
                    href("#")
                    icon("flex-shrink-0 h-6 w-6 text-primary-800", content = HeroIcons.support)
                    div("ml-4") {
                        p("text-base font-medium text-gray-900") { +value.title }
                        p("mt-1 text-sm text-gray-500") { +value.description }
                    }

                    href("#$key")
                }
            }
        }
    }
}

fun main() {
    val router = routerOf("")

    render {
        router.data.render { route ->
            div("p-4", scope = { set(SHOW_COMPONENT_STRUCTURE, true) }) {
                (pages[route]?.content ?: RenderContext::overview)()
            }
        }
    }
}
