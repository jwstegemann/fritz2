package dev.fritz2.examplesdemo

import dev.fritz2.core.*

fun RenderContext.reactive() {

    val store = storeOf("Hello Peter")

    div("p-4 text-sm font-medium text-gray-700") {
        input(id = store.id, baseClass = "block w-full shadow-sm border-gray-300 rounded-md focus:ring-blue-700 focus:border-blue-700") {
            type("text")
            value(store.data)
            changes.values() handledBy store.update
        }
        p("mt-2 text-gray-500") {
            store.data.renderText(into = this)
            tabIndex(0)
        }
    }
}