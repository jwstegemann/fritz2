package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.components.SwitchComponent.Companion.switchInputStaticCss
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.RadioSizes
import dev.fritz2.styling.theme.SwitchSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLInputElement

/**
 * This class combines the _configuration_ and the core styling of a switch.
 *
 *
 * This class offers the following _configuration_ features:
 *  - the optional label of a switch (static, dynamic via a [Flow<String>] or customized content of a Div.RenderContext )
 *  - some predefined styling variants (size)
 *  - the style of the switch
 *  - the style checked state
 *  - the style of the dot
 *  - the style of the label
 *  - link an external boolean flow to set the checked state of the box
 *  - link an external boolean flow to set the disabled state of the box
 *  - link events of the switch like ``changes`` with external handlers
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [SwitchComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the switch.
 *
 * Example usage
 * ```
 * val cheeseStore = storeOf(false)
 * switch(store = cheeseStore) {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 * }
 *
 * // one can handle the events and preselect the control also manually if needed:
 * switch {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 *      element {
 *          // exposes the underlying HTML input element for direct access. Use with caution!
 *      }
 * }
 * ```
 */
@ComponentMarker
class SwitchComponent :
    EventProperties<HTMLInputElement> by Event(),
    ElementProperties<Input> by Element(),
    InputFormProperties by InputForm() {

    companion object {
        val switchInputStaticCss = staticStyle(
            "switch",
            """
            position: absolute;
            height: 1px;                
            width: 1px;                
            overflow: hidden;
            clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
            clip: rect(1px, 1px, 1px, 1px);
            outline: none;           
            &:focus{
                outline: none;
            }           
            """
        )
    }

    var size = ComponentProperty<SwitchSizes.() -> Style<BasicParams>> { Theme().switch.sizes.normal }

    var label: (Div.() -> Unit)? = null
    fun label(value: String) {
        label = {
            +value
        }
    }

    fun label(value: Flow<String>) {
        label = {
            value.asText()
        }
    }

    fun label(value: (Div.() -> Unit)) {
        label = value
    }

    var labelStyle: Style<BasicParams> = { Theme().switch.label() }
    fun labelStyle(value: () -> Style<BasicParams>) {
        labelStyle = value()
    }

    var dotStyle: Style<BasicParams> = { }
    fun dotStyle(value: () -> Style<BasicParams>) {
        dotStyle = value()

    }

    var checkedStyle: Style<BasicParams> = { Theme().switch.checked() }
    fun checkedStyle(value: () -> Style<BasicParams>) {
        checkedStyle = value()
    }

    var checked = DynamicComponentProperty(flowOf(false))
}

/**
 * This component generates a *single* switch.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of this box or totally disable it.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [SwitchComponent]
 *
 * Example usage
 * ```
 * // Use a store
 * val cheeseStore = storeOf(false)
 * switch(store=cheeseStore) {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 * }
 *
 * // all state management can also be done manually if needed:
 * switch {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 *
 * @see SwitchComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SwitchComponent]
 */
fun RenderContext.switch(
    styling: BasicParams.() -> Unit = {},
    store: Store<Boolean>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "switchComponent",
    build: SwitchComponent.() -> Unit = {}
): Label {
    val component = SwitchComponent().apply(build)
    val inputId = (id ?: uniqueId()) + "-input"


    return (::label.styled(
        baseClass = baseClass,
        id = id,
        prefix = prefix
    ) {
        component.size.value.invoke(Theme().switch.sizes)()
    }) {
        `for`(inputId)
        (::input.styled(
            baseClass = switchInputStaticCss,
            prefix = prefix,
            id = inputId
        ) {
            Theme().switch.input()
            children("&[checked] + div") {
                component.checkedStyle()
            }
        }) {
            component.element.value.invoke(this)
            disabled(component.disabled.values)
            readOnly(component.readonly.values)
            type("checkbox")
            checked(store?.data ?: component.checked.values)
            component.events.value.invoke(this)
            store?.let { changes.states() handledBy it.update }
        }


        (::div.styled() {
            Theme().switch.default()
            styling()
        }) {
            (::div.styled() {
                Theme().switch.dot()
                component.dotStyle()
            }) {

            }
        }

        component.label?.let {
            (::div.styled() {
                component.labelStyle()
            }){
                it(this)
            }
        }
    }
}
