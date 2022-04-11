package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.textArea
import kotlinx.coroutines.flow.map

fun RenderContext.textAreaDemo() {

    val content = storeOf("")

    div("max-w-sm") {
        val hasFocus = storeOf(false)

        textArea("block relative text-zinc-800 border rounded-md") {
            value(content)
            placeholder("Enter your text")
            className(hasFocus.data.map { if(it) "shadow-github border-blue-600" else ""})
            textareaTextfield("block overflow-auto p-2 m-0 w-full max-w-full font-mono text-sm leading-normal align-middle whitespace-pre-wrap break-words bg-white bg-no-repeat border-b border-dashed rounded-md rounded-b-none cursor-text resize-y box-border focus:outline-none") {
                className(value.hasError.map {
                    if (it) "border-b-error-300 text-error-900 focus:border-b-error-500"
                    else "border-b-gray-300 focus:border-b-blue-500"
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

        div("bg-white text-zinc-800 mt-8 p-2 border rounded-md") {
            em { +"Content: " }
            content.data.renderText()
        }
    }
}
