package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.headless.components.tabGroup
import kotlinx.coroutines.flow.map

fun RenderContext.tabsDemo() {

    data class Posting(val id: Int, val title: String, val date: String, val commentCount: Int, val shareCount: Int)

    val categories = mapOf(
        "Recent" to listOf(
            Posting(1, "Does drinking coffee make you smarter?", "5h ago", 5, 2),
            Posting(2, "So you've bought coffee... now what?", "2h ago", 3, 2)
        ),
        "Popular" to listOf(
            Posting(1, "Is tech making coffee better or worse?", "Jan 7", 29, 16),
            Posting(2, "The most innovative things happening in coffee", "Mar 19", 24, 12)
        ),
        "Trending" to listOf(
            Posting(1, "Ask Me Anything: 10 answers to your questions about coffee", "2d ago", 9, 5),
            Posting(2, "The worst advice we've ever heard about coffee", "4d ago", 1, 2)
        )
    )

    tabGroup("max-w-sm", id = "tabGroup") {
        tabList("flex p-1 space-x-1 bg-primary-900/20 rounded-md") {
            categories.keys.forEach { category ->
                tab(
                    """w-full py-2.5 leading-5
                    | text-sm font-medium rounded
                    | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
                ) {
                    className(selected.map { sel ->
                        if (sel == index) "bg-primary-800 text-white shadow-md"
                        else "text-primary-100 hover:bg-primary-900/[0.12]"
                    })
                    +category
                }
            }
        }
        tabPanels("mt-2") {
            categories.values.forEach { postings ->
                panel("""bg-white rounded p-3
                       | focus:outline-none focus-visible:ring-4 focus-visible:ring-primary-600""".trimMargin()
                ) {
                    ul {
                        postings.forEach { posting ->
                            li("relative p-3 rounded-md") {
                                h3("text-sm font-medium leading-5") {
                                    +posting.title
                                }
                                ul("flex mt-2 space-x-1 text-xs font-normal leading-4 text-primary-800") {
                                    li { +posting.date }
                                    li { +"·" }
                                    li { +"${posting.commentCount} comments" }
                                    li { +"·" }
                                    li { +"${posting.shareCount} shares" }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
