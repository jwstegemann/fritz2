package dev.fritz2.headlessdemo

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headlessdemo.navigation.*
import dev.fritz2.headlessdemo.utils.ComponentDemoIcons
import dev.fritz2.headlessdemo.utils.HeroIcons
import dev.fritz2.headlessdemo.utils.renderTailwind
import dev.fritz2.headlessdemo.utils.require
import kotlinx.browser.document
import kotlinx.coroutines.flow.map
import kotlinx.dom.addClass


private val navigation = Navigation(navigationDefinition())

private val allPages: List<Page> = navigation.categories.flatMap { cat ->
    cat.pages.flatMap { page ->
        page.subpages + page
    }
}


// Actual rendering of the page:

fun RenderContext.overview() {
    renderOverview(navigation)
}

private fun RenderContext.navigationHeader() = div("px-4") {
    div("flex flex-row items-center pr-4") {
        // TODO: USe fritz2 icon
        svg("w-16 h-16 mr-4 text-white") {
            content(HeroIcons.template)
        }

        div {
            p("text-2xl font-semibold text-white whitespace-nowrap") {
                +"Component Demo"
            }
            p("text-sm text-gray-300 font-mono whitespace-nowrap") {
                +"fritz2-tailwind"
            }
        }
    }
}

fun RenderContext.navigationContent() =
    navigation.categories.forEach { category ->
        div {
            div("px-2 py-2 text-xs font-bold uppercase text-gray-300") {
                +category.name
            }
            navigationEntries(category.pages)
        }
    }

fun RenderContext.navigationEntries(pages: List<PageWithSubpages>) {
    pages.forEach {
        navigationEntry(navigation, it.name, it.target, it.subpages)
    }
}

fun main() {
    require("./styles.css")

    renderTailwind {
        // TODO: Get rid of this ugly workaround
        document.body?.let {
            it.addClass("overflow-hidden")
        }

        div("h-screen flex overflow-hidden bg-gray-50") {

            // Off-canvas menu container
            div("fixed inset-0 flex z-40 lg:hidden") {
                classList(MenuContainerVisibilityStore.data.map { listOf(it) })

                attr("role", "dialog")
                attr("aria-modal", "true")

                // Overlay to make the menu modal
                div("fixed inset-0 bg-gray-600 bg-opacity-75 transition-opacity ease-linear duration-300") {
                    classList(MenuContentVisibilityStore.data.map { listOf(it.background) })
                    attr("aria-hidden", "true")
                }

                // Actual menu overlay
                div("relative flex-1 flex flex-col max-w-xs w-full pt-5 pb-4 bg-primary-500 transition ease-in-out duration-300 transform") {
                    classList(MenuContentVisibilityStore.data.map { listOf(it.menu) })

                    // Close-button
                    div("absolute top-0 right-0 -mr-12 pt-2 ease-in-out duration-300") {
                        classList(MenuContentVisibilityStore.data.map { listOf(it.closeButton) })

                        button("ml-1 flex items-center justify-center h-10 w-10 rounded-full focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white") {
                            type("button")
                            span("sr-only") { +"""Close sidebar""" }

                            svg("h-6 w-6 text-white") {
                                attr("aria-hidden", "true")
                                content(HeroIcons.x)
                            }

                            clicks handledBy MenuContentVisibilityStore.hide
                            clicks handledBy MenuContainerVisibilityStore.hideDelayed
                        }
                    }
                    navigationHeader()
                    div("mt-5 flex-1 h-0 overflow-y-auto") {
                        nav("px-2 space-y-1") {
                            navigationContent()
                        }
                    }
                }
                div("flex-shrink-0 w-14") {
                    attr("aria-hidden", "true")
                    // Dummy element to force sidebar to shrink to fit close icon
                }
            }

            // Static sidebar for desktop
            div("hidden lg:flex lg:flex-shrink-0") {
                div("flex flex-col") {
                    div("flex flex-col flex-grow pt-5 pb-4 bg-primary-500 overflow-y-auto") {
                        navigationHeader()
                        div("mt-5 flex-grow flex flex-col") {
                            nav("flex-1 px-2 bg-primary-500 space-y-4") {
                                navigationContent()
                            }
                        }
                    }
                }
            }

            // Top menu-bar and main content
            div("flex flex-col w-0 flex-1 overflow-hidden") {
                div("pl-4 pr-8 md:pl-8 flex flex-row items-center gap-4 relative z-10 flex-shrink-0 flex h-16 bg-white shadow") {
                    button("px-4 py-2 m-2 lg:hidden border-transparent text-primary hover:bg-primary-50 focus:ring-primary-400") {
                        svg("w-5 h-5 -ml-1 mr-3") {
                            content(HeroIcons.menu)
                        }
                        clicks handledBy MenuContentVisibilityStore.show
                        clicks handledBy MenuContainerVisibilityStore.show
                    }

                    navigation.currentPage.render {
                        div("flex flex-grow text-xl font-semibold text-primary") {
                            +it.name
                        }
                    }

                    a {
                        href("https://github.com/jwstegemann/fritz2")
                        target("_blank")
                        svg("h-6 -w-6 pr-2 text-gray-500") {
                            content(ComponentDemoIcons.github)
                        }
                    }

                    a {
                        href("https://www.fritz2.dev")
                        target("_blank")
                        svg("h-6 -w-6 text-gray-500") {
                            content(ComponentDemoIcons.fritz2)
                        }
                    }
                }
                main("flex-1 relative overflow-y-auto focus:outline-none") {
                    div {
                        navigation.currentPage.render {
                            it.content(this)
                        }
                    }
                }
            }
        }
    }
}
