package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.headless.components.listbox
import kotlinx.coroutines.flow.flowOf

fun RenderContext.fooListbox() {

val characters = listOf(
    "Luke" to true,
    "Leia" to false,
    "Chewbakka" to false,
    "Han" to false
)

val bestCharacter = storeOf("Luke")

listbox<String> {
    value(bestCharacter)
    listboxButton {
        span {
            value.data.renderText()
        }
    }

    listboxItems {
        openClose(data = flowOf(true))
        characters.forEach { (entry, disabledState) ->
            listboxItem(entry) {
                span { +entry }
            }
        }
    }
}

}