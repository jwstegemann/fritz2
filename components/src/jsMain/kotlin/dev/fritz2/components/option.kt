package dev.fritz2.components

import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * This class offers the configuration for an option element
 *
 *  you can configure the following:
 *
 *  - value
 *  - label
 *  - selection
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [OptionComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the checkbox.
 *
 *  // use case
 *
 *  optionSelect {
 *   value { value text }
 *   label { label text }
 *   selected( isSelected)
 *  }
 *
 *
 */
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

    fun getText(): String = if (label != null) "$label" else "$value"

}


/**
 * This component generates a single option element
 *
 * you can set :
 *  - value which should returned
 *  - text which should displayed  -> label
 *  - the selected status
 *
 *
 *  // use case
 *
 *  optionSelect {
 *   value { value text }
 *   label { label text }
 *   selected( isSelected)
 *  }
 *
 */
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