package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.textArea
import kotlinx.coroutines.flow.map

fun RenderContext.textAreaDemo() {

    val description = storeOf("")

    div("max-w-sm") {
        textArea {
            value(description)
            placeholder("fritz2 is super cool")
            textareaLabel("block text-sm font-medium text-gray-700") {
                +"Describe the framework"
            }
            div("mt-1") {
                textareaTextfield(
                    "block w-full sm:text-sm rounded-md disabled:opacity-50"
                ) {
                    className(value.hasError.map {
                        if (it) classes(
                            "border-error-300 text-error-900 placeholder-error-300",
                            "focus:ring-error-500 focus:border-error-500"
                        )
                        else classes(
                            "block border-gray-300 text-gray-900 placeholder-gray-300",
                            "focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white"
                        )
                    })
                }
            }
            textareaDescription("mt-2 text-sm text-gray-500") {
                +"Describe the domain, usage and important notes."
            }
        }

        div("bg-gray-300 mt-8 p-2 rounded-lg ring-2 ring-gray-50") {
            em { +"Description: " }
            description.data.renderText()
        }
    }

    div("mt-8") {
        val hasFocus = storeOf(false)

        textArea("block relative m-2 max-w-2xl text-zinc-800 border rounded-md") {
            className("shadow-github border-blue-600".whenever(hasFocus.data))
            value(description)
            textareaTextfield("block overflow-auto p-2 m-0 w-full max-w-full font-mono text-sm leading-normal align-middle whitespace-pre-wrap break-words bg-white bg-no-repeat rounded-md rounded-b-none border-b border-dashed cursor-text resize-y box-border focus:outline-none") {
                className(value.hasError.map {
                    if (it) "border-error-300 text-error-900 focus:border-error-500"
                    else "border-gray-300 focus:border-blue-500"
                })
                focuss.map { true } handledBy hasFocus.update
                blurs.map { false } handledBy hasFocus.update
            }
            textareaDescription(
                "flex relative justify-between p-2 m-0 text-xs font-normal leading-4 text-gray-600 bg-slate-50 rounded-b-md",
                tag = RenderContext::label
            ) {
                +"Attach files by dragging & dropping, selecting or pasting them."
                a("inline relative leading-4 break-words bg-transparent cursor-pointer box-border hover:outline-0 decoration-0") {
                    href("https://docs.github.com/github/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax")
                    target("_blank")
                    svg("inline-block overflow-visible align-bottom box-border w-4") {
                        attr("aria-hidden", "true")
                        viewBox("0 0 16 16")
                        fill("currentColor")
                        path {
                            d("M14.85 3H1.15C.52 3 0 3.52 0 4.15v7.69C0 12.48.52 13 1.15 13h13.69c.64 0 1.15-.52 1.15-1.15v-7.7C16 3.52 15.48 3 14.85 3zM9 11H7V8L5.5 9.92 4 8v3H2V5h2l1.5 2L7 5h2v6zm2.99.5L9.5 8H11V5h2v3h1.5l-2.51 3.5z")
                        }
                    }
                }
            }
        }
    }
}
