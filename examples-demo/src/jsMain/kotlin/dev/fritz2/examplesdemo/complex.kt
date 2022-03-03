package dev.fritz2.examplesdemo

import dev.fritz2.core.*
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.storeOf
import dev.fritz2.validation.validation
import kotlinx.coroutines.flow.onEach

fun RenderContext.complex() {

    class Message(override val path: String, val text: String): ValidationMessage {
        override val isError: Boolean = true
    }

    val validation = validation<String, Message> {
        if(!Regex.fromLiteral("""/\S+@\S+\.\S+/""").matches(it.data))
            add(Message(it.path,"Not a valid mail address"))
    }

    val store = storeOf("", validation)

    div("p-4 text-sm font-medium text-gray-700") {
        input("block w-full shadow-sm border-gray-300 rounded-md focus:ring-blue-700 focus:border-blue-700") {
            type("text")
            placeholder("Enter e-mail address")
            value(store.data)
            changes.values() handledBy store.update
        }
        store.messages.onEach { console.log(it.joinToString()) }.renderEach {
            p("mt-2 text-red-500") {
                +it.text
            }
        }
    }
}