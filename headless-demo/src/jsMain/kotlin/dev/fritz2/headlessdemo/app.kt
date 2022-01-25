package dev.fritz2.headlessdemo.demos

import dev.fritz2.dom.html.RenderContext
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
    )
)

fun RenderContext.overview() {
    div("flex flex-col justify-center items-center h-screen") {
        h1("mb-4 text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl") {
            span("block xl:inline") { +"""fritz2""" }
            span("block text-indigo-600 xl:inline") { +""" Headless Demos""" }
        }
        div("w-96 rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 overflow-hidden") {

            div("relative grid gap-6 bg-white px-5 py-6 sm:gap-8 sm:p-8") {
                pages.forEach {
                    a("-m-3 p-3 flex items-start rounded-lg hover:bg-gray-50 transition ease-in-out duration-150") {
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
                                d("M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536 9.192l-3.536 3.536M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-5 0a4 4 0 11-8 0 4 4 0 018 0z")
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

}

fun main() {
    require("./styles.css")

    val router = routerOf("")

    renderTailwind {
        router.data.render { route ->
            console.log(route)
            div("w-full h-full bg-gradient-to-r from-amber-300 to-orange-500 rounded-lg p-4") {
                (pages[route]?.content ?: RenderContext::overview)()
            }
        }
    }
}
