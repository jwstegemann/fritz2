package dev.fritz2.examplesdemo

import dev.fritz2.core.*

fun RenderContext.fundamentals() {
    render {
        val store = storeOf("Hello, fritz2!")

        div("p-4 text-sm font-medium text-gray-700 bg-white") {
            div("flex flex-col gap-2") {
                label {
                    +"Input"
                    `for`(store.id)
                }
                input("p-2 border border-1 border-gray-300 rounded-sm", id = store.id) {
                    placeholder("Add some input")
                    value(store.data)
                    changes.values() handledBy store.update
                }
                p { +"Value" }
                store.data.render { content ->
                    p("p-2 bg-gray-100 border border-1 border-gray-300 rounded-sm") {
                        +content
                    }
                }
                button("p-2 bg-blue-400 text-white border border-1 border-gray-300 rounded-md") {
                    +"Capitalize"
                    clicks handledBy store.handle { it.uppercase() }
                }
            }
        }
    }
}