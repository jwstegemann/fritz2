package dev.fritz2.examplesdemo

import dev.fritz2.core.*
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.storeOf
import dev.fritz2.validation.validation

fun RenderContext.complex() {

    class Message(override val path: String, val text: String): ValidationMessage {
        override val isError: Boolean = true
    }

    val mailRegex = Regex("""\S+@\S+\.\S+""")
    val validation = validation<String, Message> {
        if(!mailRegex.matches(it.data))
            add(Message(it.path,"Not a valid mail address"))
    }

    val store = storeOf("", validation = validation)

    div("p-4 text-sm font-medium text-gray-700") {
        input(id = store.id, baseClass = "block w-full shadow-sm border-gray-300 rounded-md focus:ring-blue-700 focus:border-blue-700") {
            type("text")
            placeholder("Enter e-mail address")
            value(store.data)
            changes.values() handledBy store.update
        }
        store.messages.renderEach {
            p("mt-2 text-red-500") {
                +it.text
            }
        }
    }
}