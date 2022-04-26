package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.classes
import dev.fritz2.core.storeOf
import dev.fritz2.headless.components.inputField
import kotlinx.coroutines.flow.map

fun RenderContext.inputFieldDemo() {

    val name = storeOf("", id="inputField")

    div("max-w-sm") {

        inputField("mb-4") {
            value(name)
            placeholder("The name is...")
            inputLabel("block text-sm font-medium text-gray-700") {
                +"Enter the framework's name"
            }
            div("mt-1") {
                inputTextfield(
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
            inputDescription("mt-2 text-sm text-gray-500") {
                +"The name should reflect the concept of the whole framework."
            }
        }

        div("bg-gray-300 mt-8 p-2 rounded-lg ring-2 ring-gray-50", id = "result") {
            em { +"Name: " }
            name.data.renderText()
        }
    }

}
