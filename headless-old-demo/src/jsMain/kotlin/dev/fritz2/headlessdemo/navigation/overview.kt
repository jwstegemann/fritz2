package dev.fritz2.headlessdemo.navigation

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headlessdemo.utils.HeroIcons


fun RenderContext.renderOverview(navigation: Navigation, classes: String? = null, id: String? = null) {
    navigation.categories.filter { it.displayInOverview }.forEach { category ->
        div("m-4 md:m-8") {
            span("text-base font-semibold text-primary") {
                +category.name
            }

            div("flex flex-wrap gap-8 mt-4 mb-8") {
                category.pages.forEach { page ->
                    div("w-full sm:w-1/2 md:w-1/2 xl:w-1/4 cursor-pointer") {
                        // card
                        div("bg-white overflow-hidden shadow rounded-lg divide-y divide-gray-200 text-gray-900") {
                            // header
                            div("h-36 flex items-center justify-center bg-gray-100 overflow-hidden") {
                                page.preview?.let {
                                    it(this)
                                } ?: run {
                                    div("flex flex-col items-center") {
                                        svg("h-6 w-6 mb-2 text-primary") {
                                            content(HeroIcons.x_circle)
                                        }
                                        span("text-xs text-gray-400") {
                                            +"No preview available"
                                        }
                                    }
                                }
                            }

                            // content
                            div("flex flex-row items-center m-4") {
                                svg("w-4 h-4 mr-2 text-primary") {
                                    content(HeroIcons.arrow_right)
                                }
                                span("flex flex-grow text-lg ") {
                                    +page.name
                                }
                                page.subpages.size.let {
                                    if (it > 0) {
                                        span("px-2 py-1 rounded-full bg-gray-400 text-xs text-white") {
                                            +"+ $it subpage${if (it > 1) "s" else ""}"
                                        }
                                    }
                                }
                            }
                        }
                        clicks.map { page.target } handledBy navigation.router.navTo
                    }
                }
            }
        }
    }
}

