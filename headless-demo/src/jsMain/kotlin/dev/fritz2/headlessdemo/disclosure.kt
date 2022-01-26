package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.headlessDisclosure
import kotlinx.coroutines.flow.map

fun RenderContext.disclosureDemo() {
    val faqs = listOf(
        "What's the best thing about Switzerland?" to
                """I don't know, but the flag is a big plus. Lorem ipsum dolor sit amet consectetur adipisicing
                    | elit. Quas cupiditate laboriosam fugiat.""".trimMargin(),
        "How do you make holy water?" to
                """You boil the hell out of it. Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    | Magnam aut tempora vitae odio inventore fuga aliquam nostrum quod porro.
                    | Delectus quia facere id sequi expedita natus. """.trimMargin(),
        "How do you call somebody without body and without nose?" to
                """Nobody knows. Lorem ipsum dolor sit amet consectetur adipisicing elit. Culpa,
                    | voluptas ipsa quia excepturi, quibusdam natus exercitationem sapiente
                    | tempore labore voluptatem. """.trimMargin(),
        "Why did the invisible man turn down the job offer?" to
                """He couldn't see himself doing it. Lorem ipsum dolor sit, amet consectetur adipisicing elit.
                    | Eveniet perspiciatis officiis corrupti tenetur. Temporibus ut voluptatibus,
                    | perferendis sed unde rerum deserunt eius. """.trimMargin(),
    )

    div("max-w-7xl mx-auto py-12 px-4 sm:py-16 sm:px-6 lg:px-8") {
        div("max-w-3xl mx-auto divide-y-2 divide-gray-200 rounded-xl") {
            h2("text-center text-3xl font-extrabold text-gray-900 sm:text-4xl") {
                +"Frequently asked questions"
            }
            dl("mt-6 space-y-6 divide-y divide-gray-200") {
                faqs.forEach { (question, answer) ->
                    headlessDisclosure("pt-6") {
                        dt("text-lg") {
                            /* <!-- Expand/collapse question button --> */
                            disclosureButton(
                                """text-left w-full flex justify-between items-start 
                                    | text-gray-400""".trimMargin()
                            ) {
                                type("button")
                                span("font-medium text-gray-900") { +question }
                                span("ml-6 h-7 flex items-center") {
                                    opened.render(into = this) {
                                        svg("h-6 w-6 transform") {
                                            if(it) content(HeroIcons.chevron_up)
                                            else content(HeroIcons.chevron_down)
                                        }
                                    }
                                }
                            }
                        }
                        disclosurePanel("mt-2 pr-12", tag = RenderContext::dd) {
                            p("text-base text-gray-700") { +answer }
                        }
                    }
                }
            }
        }
    }
}