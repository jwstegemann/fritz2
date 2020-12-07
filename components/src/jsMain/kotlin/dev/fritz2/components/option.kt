package dev.fritz2.components

import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLOptionElement

class OptionComponent {


    var value: String = ""
    fun value(param: () -> String) {
        value = param()
    }

    var label: String? = null
    fun label(value: () -> String) {
        label = value()
    }

    var selected: Flow<Boolean> = flowOf(false)
    fun selected(value: () -> Flow<Boolean>) {
        selected = value()
    }

    fun selected(value: Flow<Boolean>) {
        selected = value
    }

    var events: (WithEvents<HTMLOptionElement>.() -> Unit)? = null
    fun events(value: WithEvents<HTMLOptionElement>.() -> Unit) {
        events = value
    }

    fun getText(): String = if (label != null) "$label" else "$value"

}


fun RenderContext.optionSelect(
    build: OptionComponent.() -> Unit = {},
): Option {
    val component = OptionComponent().apply(build)

    return (::option.styled(prefix = "option") {}){

        value(component.value)
        selected(component.selected)

        +component.getText()



    }

}