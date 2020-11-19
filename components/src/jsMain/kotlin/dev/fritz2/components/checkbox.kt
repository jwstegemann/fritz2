package dev.fritz2.components

import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.CheckboxSizes
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLInputElement

/**
 * This class combines the _configuration_ and the core styling of a checkbox.
 *
 * In order to render a checkbox use the [checkbox] factory function!
 *
 * This class offers the following _configuration_ features:
 *  - the text label of a checkbox (static or dynamic via a [Flow<String>])
 *  - the background color of the box
 *  - the background color for the checked state
 *  - some predefined styling variants
 *  - link an external boolean flow to set the checked state of the box
 *  - link events of the checkbox like ``changes`` with external handlers
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [CheckboxComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the checkbox.
 *
 * Example usage
 * ```
 * checkbox {
 *      text("with extra cheese") // set the label
 *      checkboxSize { normal } // choose a predefined size
 *      borderColor { theme().colors.secondary } // set up the border color of the box itself
 *      checkedBackgroundColor { theme().colors.warning } // set the color of the checked state
 *      checked { cheeseStore.data } // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 */
class CheckboxComponent {
    companion object {
        val checkboxLabelStyles: Style<BasicParams> = { // @label
            before {
                radii {// overwritten by CheckboxSizes.small only
                    top { normal }
                    bottom { normal }
                    left { normal }
                    right { normal }
                }
                border {
                    style { solid }
                    width { "0.1rem" }
                }
            }
            margins {
                right { "1.0rem" }
            }
        }

        // todo replace px in sizes (in default theme) with rem/theme values where not explicit
        // todo using theme colors in static styles probably does not work when changing themes
        val checkboxInputStaticCss = staticStyle( // @input
            "checkbox",
            """
            position: absolute;
            height: 1px;                
            width: 1px;                
            overflow: hidden;
            clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
            clip: rect(1px, 1px, 1px, 1px);
            outline: none;
            &:focus + label::before {
                box-shadow: 0 0 1px ${theme().colors.dark}; 
            }
            &:disabled + label {
                color: ${theme().colors.disabled};
                cursor: not-allowed;
            }
            &:disabled + label::before {
                color: ${theme().colors.disabled};
                opacity: 0.3;
                cursor: not-allowed;
                boxShadow: none;
            }
            &:focus{
                outline: none;
            }
            """
        )

        val checkboxLabelStaticCss = staticStyle( // @input
            "checkboxlabel",
            """
            position: relative;            
            display: block;
            &::before {
                content: '';
                outline: none;
                position: relative;
                display: inline-block;
                box-shadow: 0 0 1px ${theme().colors.dark} inset;
                vertical-align: middle;
            }
            """
        )
    }

    var size: CheckboxSizes.() -> Style<BasicParams> = { theme().checkbox.sizes.normal }
    fun size(value: CheckboxSizes.() -> Style<BasicParams>) {
        size = value
    }

    var icon: IconDefinition = theme().icons.check
    fun icon(value: () -> IconDefinition ) {
        icon = value()
    }

    var text: Flow<String>? = null
    fun text(value: String) {
        text(flowOf(value))
    }
    fun text(value: Flow<String>) {
        text = value
    }

    var labelStyle: Style<BasicParams> = { theme().checkbox.label() }
    fun labelStyle(value: () -> Style<BasicParams>) {
        labelStyle = value()
    }
    var checkedStyle: Style<BasicParams> = { theme().checkbox.checked() }
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
 * This component generates a *single* checkbox.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of this box or totally disable it.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [CheckboxComponent]
 *
 * Example usage
 * ```
 * checkbox {
 *      text("with extra cheese") // set the label
 *      checkboxSize { normal } // choose a predefined size
 *      borderColor { theme().colors.secondary } // set up the border color of the box itself
 *      checkedBackgroundColor { theme().colors.warning } // set the color of the checked state
 *      checked { cheeseStore.data } // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 *
 * @see CheckboxComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [CheckboxComponent]
 */
// todo add checkmark
fun RenderContext.checkbox(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "checkboxComponent",
    build: CheckboxComponent.() -> Unit = {}
) {
    val component = CheckboxComponent().apply(build)
    val inputId = id?.let { "$it-input" }

    (::label.styled(
        baseClass = baseClass,
        id = id,
        prefix = prefix
    ) {
        component.size.invoke(theme().checkbox.sizes)()
        styling()
    }) {
        inputId?.let {
            `for`(inputId)
        }

        (::input.styled(
            baseClass = CheckboxComponent.checkboxInputStaticCss,
            prefix = prefix,
            id = inputId
        ) {

        }) {
            type("checkbox")
            checked(component.checked)
            disabled(component.disabled)
            component.events?.invoke(this)
        }

        component.checked.render { checked ->
            if (checked) {
                (::div.styled({
                    component.size.invoke(theme().checkbox.sizes)()
                    theme().checkbox.default()
                    styling()
                    component.checkedStyle()
                }) {}) {
                    icon(theme().checkbox.icon) { fromTheme { component.icon } }
                }
            } else {
                (::div.styled({
                    component.size.invoke(theme().checkbox.sizes)()
                    theme().checkbox.default()
                    styling()
                }
                ) {}) {
                    icon({
                        theme().checkbox.icon()
                        css("visibility:hidden;")
                    }
                    ) { fromTheme { component.icon } }
                }
            }
        }
        component.text?.let {
            (::span.styled() {
                component.labelStyle()
            }){
                it.asText()
            }
        }
    }
}