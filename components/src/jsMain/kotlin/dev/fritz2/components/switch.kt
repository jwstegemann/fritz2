package dev.fritz2.components

import dev.fritz2.components.SwitchComponent.Companion.switchIconStaticCss
import dev.fritz2.components.SwitchComponent.Companion.switchInputStaticCss
import dev.fritz2.components.SwitchComponent.Companion.switchLabel
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
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
 * switch {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked { cheeseStore.data } // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 */
@ComponentMarker
class SwitchComponent {
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
        val switchLabel = staticStyle("switchComponent", """
            &[data-disabled] {
                opacity: .5    
            }
        """)
        val switchIconStaticCss = staticStyle("switchIcon",
            """
            &[data-disabled] {
                background-color:var(--cb-disabled) !important;
            }
        """
        )
    }

    var size: SwitchSizes.() -> Style<BasicParams> = { Theme().switch.sizes.normal }
    fun size(value: SwitchSizes.() -> Style<BasicParams>) {
        size = value
    }

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

    var events: (WithEvents<HTMLInputElement>.() -> Unit)? = null // @input
    fun events(value: WithEvents<HTMLInputElement>.() -> Unit) {
        events = value
    }

    var checked: Flow<Boolean> = flowOf(false) // @input
    fun checked(value: () -> Flow<Boolean>) {
        checked = value()
    }

    var disabled: Flow<Boolean> = flowOf(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }
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
 * switch {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked { cheeseStore.data } // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 *
 * @see SwitchComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SwitchComponent]
 */
fun RenderContext.switch(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "switchComponent",
    build: SwitchComponent.() -> Unit = {}
): Label {
    val component = SwitchComponent().apply(build)
    val inputId = (id ?: uniqueId()) + "-input"

    val labelClass = if( baseClass == null ) {
        switchLabel
    } else {
        baseClass + switchLabel
    }

    return (::label.styled(
        baseClass = labelClass,
        id = id,
        prefix = prefix
    ) {
        component.size.invoke(Theme().switch.sizes)()
    }) {
       `for`(inputId)
        attr("data-disabled", component.disabled)
        (::input.styled(
            baseClass = switchInputStaticCss,
            prefix = prefix,
            id = inputId
        ){ children("&:focus + div") {
            border {
                color { "#3182ce" }
            }
            boxShadow { outline }
        }}) {
            type("checkbox")
            checked(component.checked)
            disabled(component.disabled)
            component.events?.invoke(this)
        }

        component.checked.render { checked ->
                (::div.styled(switchIconStaticCss){
                    Theme().switch.default()
                    styling()
                    if( checked ) {
                        component.checkedStyle()
                    }
                }) {
                    attr("data-disabled", component.disabled)
                    (::div.styled() {
                        Theme().switch.dot()
                        component.dotStyle()
                       if( checked ) {
                           css("transform:translateX(calc(var(--sw-width) - var(--sw-height)));")
                       }
                    }) {

                    }
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