package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.href
import dev.fritz2.core.transition
import dev.fritz2.headless.components.disclosure

fun RenderContext.disclosureDemo() {
    val faqs = listOf<Pair<String, RenderContext.() -> Unit>>(
        "What's the best thing about fritz2?" to {
            p {
                +"""We are strongly biased - there are so many things you can name. One outstanding aspect for us is
                    | the fact, that it is an open source project, which makes so much fun to develop!
                """.trimMargin()
            }
            p {
                +"But we are curious to know, what you like about fritz2? So please give us feedback!"
            }
        },
        "Can you explain the power of the headless approach?" to {
            p {
                +"""Difficult. Once you have grasped it, you inherently loose the capability to explain the principle 
                    | to others... 😁 SCNR! Of course we can try. Have a look at this 
                """.trimMargin()
                a("text-primary hover:text-primary-700") {
                    attr("tabindex", "-1")
                    href("www.fritz2.dev/blog/posts/paradigm-shift-for-components/")
                    +"article"
                }
                +" about it and consult the documentation."
            }
        },
        "Will there ever be a fritz3?" to {
            p {
                +"""Nobody knows. But it would rather be a fritz22 😉 """.trimMargin()
            }
        },
        "Why you choose so often four items for your headless examples?" to {
            p {
                +"""Four is a nice number! Think of episode IV, the sense of everything (42) starts with a 4...
                    | and last but not least: We have never thought about it to be honest!
                """.trimMargin()
            }
        },
    )

    div("max-w-3xl") {
        div("py-8 px-4") {
            h1("text-left text-gray-800 font-medium text-lg") {
                +"Frequently asked questions"
            }
            dl("mt-6 space-y-6") {
                faqs.withIndex().forEach { (index, faq) ->
                    val (question, answer) = faq
                    disclosure("", id = "disclosure-$index") {
                        dt("text-base") {
                            /* <!-- Expand/collapse question button --> */
                            disclosureButton(
                                """relative z-10 flex justify-between items-start w-full my-2 p-4
                                | bg-primary-800 rounded-lg hover:bg-primary-900 
                                | text-left text-white 
                                | focus:outline-none focus:ring-4 focus:ring-primary-600
                                """.trimMargin(),
                            ) {
                                span("font-medium") { +question }
                                span("ml-6 h-7 flex items-center") {
                                    opened.render(into = this) { isOpen ->
                                        icon(
                                            "w-5 h-5 ml-2 -mr-1",
                                            content = if (isOpen) HeroIcons.chevron_up else HeroIcons.chevron_down,
                                        )
                                    }
                                }
                            }
                        }
                        disclosurePanel(
                            "-mt-6 pt-7 pb-4 pl-4 pr-12 bg-primary-100 rounded-lg origin-top",
                            tag = RenderContext::dd,
                        ) {
                            transition(
                                "transition duration-100 ease-out",
                                "opacity-0 scale-y-95",
                                "opacity-100 scale-y-100",
                                "transition duration-100 ease-in",
                                "opacity-100 scale-y-100",
                                "opacity-0 scale-y-95",
                            )
                            div("text-base text-gray-700") { answer(this) }
                        }
                    }
                }
            }
        }
    }
}
