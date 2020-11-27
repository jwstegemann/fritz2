package dev.fritz2.components

import dev.fritz2.binding.RootStore


import dev.fritz2.components.RadioGroupComponent.Companion.radioGroupStructure
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.RadioSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement

// todo implement defaultChecked for radio, checkbox
// todo add dropdown single select

class RadioComponent {

    var size: RadioSizes.() -> Style<BasicParams> = { Theme().radio.sizes.normal }
    fun size(value: RadioSizes.() -> Style<BasicParams>) {
        size = value
    }

    var text: Flow<String> = flowOf("") // @label
    fun text(value: Flow<String>) {
        text = value
    }

    var backgroundColor: Style<BasicParams> = {} // @label
    fun backgroundColor(value: () -> ColorProperty) {
        backgroundColor = {
            css("&::before { background-color: ${value()};}")
        }
    }

    var borderColor: Style<BasicParams> = {} // @label
    fun borderColor(value: () -> ColorProperty) {
        borderColor = {
            css("&::before { border-color: ${value()};}")
        }
    }

    var checkedBackgroundColor: Style<BasicParams> = {} // @input
    fun checkedBackgroundColor(value: () -> ColorProperty) {
        checkedBackgroundColor = {
            css("&:checked + label::before { background-color: ${value()};}")
        }
    }

    var events: (WithEvents<HTMLInputElement>.() -> Unit)? = null // @input
    fun events(value: WithEvents<HTMLInputElement>.() -> Unit) {
        events = value
    }

    // todo: for user, these are only distinguished  from Input.xxx by signature
    var checked: Flow<Boolean> = flowOf(false) // @input

    var disabled: Flow<Boolean> = flowOf(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    companion object {
        val radioLabelStyles: Style<BasicParams> = { // @label
            before {
                radii {
                    right { "50%" }
                    left { "50%" }
                    top { "50%" }
                    bottom { "50%" }
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

        val radioInputStaticCss = staticStyle(
            "radioInput",
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
            &:focus + label::before {
                box-shadow: 0 0 1px ${Theme().colors.dark};
            }
            &:disabled + label {
                color: ${Theme().colors.disabled};
                cursor: not-allowed;
            }
            &:disabled + label::before {
                opacity: 0.3;
                cursor: not-allowed;
                boxShadow: none;
                color: ${Theme().colors.disabled};
            }
            """
        )

        val radioLabelStaticCss = staticStyle(
            "radiolabel",
            """
            display: block;
            position: relative;
            &::before {
                content: '';
                outline: none;
                position: relative;
                display: inline-block;
                vertical-align: middle;
                box-shadow: 0 0 1px ${Theme().colors.dark} inset;
            }
            """
        )
    }
}

// TODO: Check if this is the best encapsulation - possible to integrate into a companion object of some component?
// TODO: Check if signature is really necessary? If only internally used, some parameters might be obsolete?
private fun RenderContext.radio(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "radioComponent",
    build: RadioComponent.() -> Unit = {}
) {
    val component = RadioComponent().apply(build)

    (::div.styled(
        baseClass = baseClass,
        id = id,
        prefix = prefix
    ) {
        styling() // attach user styling to container only
    }) {
        (::input.styled(
            baseClass = RadioComponent.radioInputStaticCss,
            id = "$id-input",
            prefix = prefix) {
            component.checkedBackgroundColor()

        }) {
            type("radio")
            name("$id-groupname")
            checked(component.checked)
            disabled(component.disabled)
            value(component.text)
            component.events?.invoke(this)
        }
        (::label.styled(
            baseClass = RadioComponent.radioLabelStaticCss,
            id = "$id-label",
            prefix = prefix) {
            RadioComponent.radioLabelStyles()
            component.size.invoke(Theme().radio.sizes)()
            component.backgroundColor()
            component.borderColor()
        }) {
            `for`("$id-input")
            component.text.asText()
        }
    }
}


/**
 * This class combines the _configuration_ and the core styling of a radio button group.
 * The rendering itself is also done within the companion object.
 *
 * In order to render a radio button group use the [radioGroup] factory function!
 *
 * This class offers the following _configuration_ features:
 *  - the text label of a radio button (static or dynamic via a [Flow<String>])
 *  - the background color of the radio
 *  - the background color for the selected radio
 *  - some predefined styling variants
 *  - offer a list of items ([String])
 *  - offer [String] of pre selected item
 *  - choose the direction of radio elements (row vs column)
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [RadioGroupComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the radio button group.
 *
 * Example usage
 * ```
 * // simple use case showing the core functionality
 * val options = listOf("A", "B", "C")
 * radioGroup {
 *     items { options } // provide a list of items
 *     selected { options[1] } // pre select "B"
 * } handledBy selectedItemStore.update // combine the Flow<String> with a fitting handler
 *
 * // use case showing some styling options
 * val options = listOf("A", "B", "C")
 * radioGroup({ // this styling is only applied to the enclosing container element!
 *      background {
 *          color { "deeppink" }
 *      }
 *      border {
 *          color { dark }
 *          style { solid }
 *          size { normal }
 *      }
 * }) {
 *      // those predefined styles are applied especially to specific inner elements!
 *      radioSize { normal }
 *      borderColor { Theme().colors.secondary }
 *      checkedBackgroundColor { Theme().colors.warning }
 *      items { options } // provide a list of items
 *      selected { options[1] } // pre select "B"
 * } handledBy selectedItemStore.update // combine the Flow<String> with a fitting handler
 * ```
 */
@ComponentMarker
class RadioGroupComponent {

    var direction: Style<BasicParams> = { RadioGroupLayouts.column } // @fieldset
    fun direction(value: RadioGroupLayouts.() -> Style<BasicParams>) {
        direction =  RadioGroupLayouts.value()
    }

    var items: List<String> = emptyList()
    fun items(value: () -> List<String>) {
        items = value()
    }

    var selected: String = ""
    fun selected(value: () -> String) {
        selected = value()
    }

    var disabled: Flow<Boolean> = flowOf(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    // TODO: Consider a color from the theme!
    var checkedBackgroundColor: ColorProperty = "gray" // @checkbox @input
    fun checkedBackgroundColor(value: Colors.() -> ColorProperty) {
        checkedBackgroundColor = Theme().colors.value()
    }

    // TODO: Consider a color from the theme!
    var backgroundColor: ColorProperty = "white" // @label
    fun backgroundColor(value: Colors.() -> ColorProperty) {
        backgroundColor = Theme().colors.value()
    }

    // TODO: Consider a color from the theme!
    var borderColor: ColorProperty = "black"  // @label
    fun borderColor(value: Colors.() -> ColorProperty) {
        borderColor = Theme().colors.value()
    }

    var size: RadioSizes.() -> Style<BasicParams> = { Theme().radio.sizes.normal }
    fun size(value: RadioSizes.() -> Style<BasicParams>) {
        size = value
    }

    companion object {

        // TODO: Check how to *centralize* this (compare multiselect and formcontrol)
        // TODO: Change names to ``horizontal`` and ``vertical``?  Row and column is widely used in fritz2 for directions
        object RadioGroupLayouts { // @ fieldset
            val column: Style<BasicParams> = {
                display {
                    block
                }
                flex {
                    DirectionValues.column
                }
            }
            val row: Style<BasicParams> = {
                display {
                    inlineFlex
                }
                flex {
                    DirectionValues.row
                }
            }
        }

        private fun RenderContext.radioGroupContent(
            id: String?,
            component: RadioGroupComponent
        ): Flow<String> {

            val selectedStore = RootStore(component.selected)

            component.items.withIndex().forEach { item ->
                radio(id = "$id-radio-${item.index}") {
                    disabled { component.disabled }
                    checked = selectedStore.data.map { selItem ->
                        item.value == selItem
                    }
                    borderColor { component.borderColor }
                    backgroundColor { component.backgroundColor }
                    checkedBackgroundColor { component.checkedBackgroundColor }
                    size { component.size.invoke(Theme().radio.sizes) }
                    events {
                        changes.values() handledBy selectedStore.update
                    }
                    text(flowOf(item.value))
                }
            }
            return selectedStore.data
        }

        fun RenderContext.radioGroupStructure(
            containerStyling: BasicParams.() -> Unit = {},
            selectedItemStore: RootStore<String>? = null,
            baseClass: StyleClass? = null,
            id: String? = null,
            prefix: String = "radioGroupComponent",
            build: RadioGroupComponent.() -> Unit = {}
        ): Flow<String> {
            val component = RadioGroupComponent().apply(build)
            var sel: Flow<String> = flowOf("")

            if (null == selectedItemStore) {
                (::fieldset.styled(
                    baseClass = baseClass,
                    id = id,
                    prefix = prefix
                ) {
                    containerStyling()
                    component.direction()
                }) {
                    // outside of form controls, returning the flow works just fine
                    sel = radioGroupContent(id, component)
                }
            } else {
                // when rendered in a form control, store ensures timely binding
                radioGroupContent(
                    id, component
                ).handledBy(selectedItemStore.update)
            }
            return sel
        }
    }
}


/**
 * This component generates a *group* of radio buttons.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the selected item. It returns a [Flow<String>] with the currently selected item, so it
 * can be easily passed to an appropriate handler like the update handler of a store.
 *
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [RadioGroupComponent]
 *
 * Example usage
 * ```
 * val options = listOf("A", "B", "C")
 * radioGroup {
 *     items { options } // provide a list of items
 *     selected { options[1] } // pre select "B"
 * } handledBy selectedItemStore.update // combine the Flow<String> with a fitting handler
 * ```
 *
 * @see RadioGroupComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [RadioGroupComponent]
 * @return a flow of the _selected_ item
 */
fun RenderContext.radioGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "radioGroupComponent",
    build: RadioGroupComponent.() -> Unit = {}
): Flow<String> {
    return radioGroupStructure(styling, null, baseClass, id, prefix, build)
}
