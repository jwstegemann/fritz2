package dev.fritz2.headlessdemo.demos

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.transition
import dev.fritz2.headless.components.HeadlessListbox
import dev.fritz2.headless.components.headlessListbox
import dev.fritz2.headless.foundation.utils.popper.Placement
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.headless.validation.ComponentValidator
import dev.fritz2.headless.validation.Severity
import dev.fritz2.headless.validation.WithValidator
import dev.fritz2.headlessdemo.renderTailwind
import dev.fritz2.identification.Inspector
import dev.fritz2.utils.classes
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import dev.fritz2.headlessdemo.require

class MyListbox<T>(initialize: MyListbox<T>.() -> Unit) {

    var entries: List<Pair<T, Boolean>> = emptyList()

    val value = HeadlessListbox.ListboxDatabindingHook<T>()

    fun RenderContext.render(classes: String?, id: String?) = div("w-full") {

        headlessListbox<T>(classes("w-72", classes), id) {
            openClose(storeOf(false))
            value.use(this@MyListbox.value)
            listboxLabel("sr-only", tag = RenderContext::span) { +"Choose the best Star Wars character" }
            listboxButton(
                """flex items-center justify-end w-full py-2 pl-3 pr-3 text-left bg-white rounded-lg
                | shadow-md cursor-default focus:outline-none focus:ring-2 focus:ring-opacity-75 
                | focus:ring-white focus:ring-offset-orange-300 focus:ring-offset-2
                | focus:border-indigo-500 sm:text-sm""".trimMargin()
            ) {
                span("block truncate w-full") {
                    value.data.renderText()
                }
                svg("w-5 h-5 text-gray-400") {
                    //content(HeroIcons.selector)
                }
            }

            listboxItems(
                "w-full py-1 overflow-auto text-base bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm",
                tag = RenderContext::ul
            ) {
                placement = Placement.bottomStart

                //tag.className(Visibility.dropOn(opened))

                entries.forEach { (entry, disabledState) ->
                    listboxItem(
                        entry,
                        "w-full cursor-default select-none relative py-2 pl-10 pr-4",
                        tag = RenderContext::li
                    ) {
                        tag.apply {
                            className(active.combine(disabled) { a, d ->
                                if (a && !d) {
                                    "text-amber-900 bg-amber-100"
                                } else {
                                    if (d) "text-gray-300" else "'text-gray-900"
                                }
                            })

                            disable(disabledState)

                            span {
                                className(selected.map { if (it) "font-medium" else "font-normal" })
                                +entry.toString()
                            }

                            selected.render {
                                if (it) {
                                    span("text-amber-600 absolute inset-y-0 left-0 flex items-center pl-3") {
                                        svg("w-5 h-5") {
                                            //content(HeroIcons.check)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            listboxValidationMessages { messages ->
                transition(
                    enter = "transition duration-100 ease-out",
                    enterStart = "transform scale-95 opacity-0",
                    enterEnd = "transform scale-100 opacity-100",
                    leave = "transition duration-75 ease-out",
                    leaveStart = "transform scale-100 opacity-100",
                    leaveEnd = "transform scale-95 opacity-0"
                )
                messages.forEach { div("my-2") { +it.message } }
            }
        }
    }

    init {
        initialize()

    }
}

fun <T> RenderContext.myListbox(
    classes: String? = null,
    id: String? = null,
    initialize: MyListbox<T>.() -> Unit
): Div = MyListbox<T>(initialize).run { render(classes, id) }


fun main() {
    require("./styles.css")
    renderTailwind {

        val bestCharacter = object : RootStore<String>("Luke", "listbox"),
            WithValidator<String, Unit> {
            override val validator = object : ComponentValidator<String, Unit>() {
                @OptIn(ExperimentalStdlibApi::class)
                override fun validate(inspector: Inspector<String>, metadata: Unit): List<ComponentValidationMessage> =
                    buildList {
                        inspector.apply {
                            if (this.data == "Vader") add(
                                ComponentValidationMessage(this.path, Severity.Warning, "Do not favour the dark side!")
                            ) else if (this.data == "Thrawn") add(
                                ComponentValidationMessage(this.path, Severity.Success, "This is so true!")
                            )

                        }
                    }
            }

            init {
                validate(Unit)
            }
        }

        div("w-full h-72 bg-gradient-to-r from-amber-300 to-orange-500 rounded-lg p-5") {
            myListbox<String> {
                value(bestCharacter)
                entries = listOf(
                    "Luke" to true,
                    "Leia" to false,
                    "Chewbakka" to false,
                    "Han" to false,
                    "C3-PO" to false,
                    "R2-D2" to true,
                    "Vader" to false,
                    "Thrawn" to false
                )
            }
        }

        div("bg-gray-300") {
            span { bestCharacter.data.renderText() }
        }
    }
}
