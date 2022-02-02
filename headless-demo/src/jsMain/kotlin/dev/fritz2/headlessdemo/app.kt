package dev.fritz2.headlessdemo.demos

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.d
import dev.fritz2.dom.html.href
import dev.fritz2.headlessdemo.*
import dev.fritz2.routing.routerOf

data class DemoPage(val title: String, val description: String, val content: RenderContext.() -> Unit)

val pages = mapOf<String, DemoPage>(
    "listbox" to DemoPage(
        "Headless Listbox",
        """Listboxes are a great foundation for building custom, accessible select menus for your app, 
            |complete with robust support for keyboard navigation.""".trimMargin(),
        RenderContext::listboxDemo
    ),
    "checkboxGroup" to DemoPage(
        "Headless Checkboxgroup",
        """Checkbox groups give you the same functionality as native HTML checkbox inputs, without any of the styling. 
            |They're perfect for building out custom UIs for multi selection.""".trimMargin(),
        RenderContext::checkboxDemo
    ),
    "radioGroup" to DemoPage(
        "Headless Radiogroup",
        """Radio Groups give you the same functionality as native HTML radio inputs, without any of the styling. 
            |They're perfect for building out custom UIs for single selection.""".trimMargin(),
        RenderContext::radiogroupDemo
    ),
    "menu" to DemoPage(
        "Headless Menu",
        """Menus offer an easy way to build custom, accessible dropdown components with robust support for keyboard
            | navigation.""".trimMargin(),
        RenderContext::menuDemo
    ),
    "modal" to DemoPage(
        "Headless Modal",
        """Menus offer an easy way to build custom, accessible dropdown components with robust support for keyboard
            | navigation.""".trimMargin(),
        RenderContext::modalDemo
    ),
    "disclosure" to DemoPage(
        "Headless Disclosure",
        """A simple, accessible foundation for building custom UIs that show and hide content, like togglable
            | accordion panels.""".trimMargin(),
        RenderContext::disclosureDemo
    ),
    "popover" to DemoPage(
        "Headless Popover",
        """Popovers are perfect for floating panels with arbitrary content like navigation menus, mobile menus and
            | flyout menus.""".trimMargin(),
        RenderContext::popoverDemo
    ),
    "tabs" to DemoPage(
        "Headless Tabs",
        """Easily create accessible, fully customizable tab interfaces, with robust focus management and keyboard
            | navigation support.""".trimMargin(),
        RenderContext::tabsDemo
    ),
    "textfield" to DemoPage(
        "Headless Input and Textarea",
        "Easily create accessible, fully customizable text inputs.",
        RenderContext::textfieldDemo
    ),
    "switch" to DemoPage(
        "Headless Switch",
        """Switches are a pleasant interface for toggling a value between two states, and offer the same 
            |semantics and keyboard navigation as native checkbox elements.""".trimMargin(),
        RenderContext::switchDemo
    )
)

fun RenderContext.overview() {
    div("flex flex-col justify-start items-center h-screen") {
        h1("mb-8 tracking-tight font-bold text-gray-900 text-4xl") {
            span("block sm:inline") { +"fritz2" }
            span("block text-indigo-600 sm:inline") { +" Headless Demos" }
        }
        div("w-3/4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-12") {
            pages.forEach {
                a(
                    """-m-3 p-3 flex items-start rounded-lg hover:bg-gray-50 hover:ring-2 hover:ring-white 
                    | ring-offset-2 ring-offset-amber-400 hover:outline-none shadow-lg rounded-lg bg-white 
                    | opacity-80 hover:opacity-100 transition ease-in-out duration-150""".trimMargin()
                ) {
                    href("#")
                    /* <!-- Heroicon name: outline/support --> */
                    svg("flex-shrink-0 h-6 w-6 text-indigo-600") {
                        xmlns("http://www.w3.org/2000/svg")
                        fill("none")
                        viewBox("0 0 24 24")
                        attr("stroke", "currentColor")
                        attr("aria-hidden", "true")
                        path {
                            attr("stroke-linecap", "round")
                            attr("stroke-linejoin", "round")
                            attr("stroke-width", "2")
                            d(
                                """M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536  
                                | 9.192l-3.536 3.536M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-5 0a4 4 0 11-8 0 
                                | 4 4 0 018 0z""".trimMargin()
                            )
                        }
                    }
                    div("ml-4") {
                        p("text-base font-medium text-gray-900") { +it.value.title }
                        p("mt-1 text-sm text-gray-500") { +it.value.description }
                    }

                    href("#${it.key}")
                }
            }
        }
    }

}

fun main() {
    require("./styles.css")

    val router = routerOf("")

    renderTailwind {
        router.data.render { route ->
            div("w-full h-screen bg-gradient-to-r from-amber-300 to-orange-500 p-4") {
                (pages[route]?.content ?: RenderContext::overview)()
            }
        }
    }
}
