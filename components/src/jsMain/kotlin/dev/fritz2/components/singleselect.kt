package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.components.RadioGroupComponent.Companion.radioGroupStructure
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.RadioSizes
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement

// todo fix no size default
// todo implement defaultChecked for radio, checkbox
// todo add dropdown single select

fun HtmlElements.radioGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "radioGroupComponent",
    build: RadioGroupComponent.() -> Unit = {}
): Flow<String> {
    return radioGroupStructure(styling, null, baseClass, id, prefix, build)
}

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
    fun selected(value: () ->  String) {
        selected = value()
    }

    var disabled: Flow<Boolean> = const(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    var checkedBackgroundColor: ColorProperty = "gray" // @checkbox @input
    fun checkedBackgroundColor(value: () -> ColorProperty) {
        checkedBackgroundColor = value()
    }

    var backgroundColor: ColorProperty = "white" // @label
    fun backgroundColor(value: () -> ColorProperty) {
        backgroundColor = value()
    }

    var borderColor: ColorProperty = "black"  // @label
    fun borderColor(value: () -> ColorProperty) {
        borderColor = value()
    }

    var size: RadioSizes.() -> Style<BasicParams> = { theme().radio.sizes.normal }
    fun size(value: RadioSizes.() -> Style<BasicParams>) {
        size = value
    }

    companion object {

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

        private fun HtmlElements.radioGroupContent(
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
                    size { component.size.invoke(theme().radio.sizes) }
                    events {
                        changes.values() handledBy selectedStore.update
                    }
                    text = const(item.value)
                }
            }
            return selectedStore.data
        }

        fun HtmlElements.radioGroupStructure(
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

private fun HtmlElements.radio(
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
        prefix = prefix) {
        styling() // attach user styling to container only
    }) {
        (::input.styled(
            baseClass = RadioComponent.radioInputStaticCss,
            id = "$id-input",
            prefix = prefix) {
            component.checkedBackgroundColor()

        }) {
            type = const("radio")
            name = const("$id-groupname")
            checked = component.checked
            disabled = component.disabled
            value = component.text
            component.events?.invoke(this)
        }
        (::label.styled(
            baseClass = RadioComponent.radioLabelStaticCss,
            id = "$id-label",
            extension = "$id-input", // for
            prefix = prefix) {
            RadioComponent.radioLabelStyles()
            component.size.invoke(theme().radio.sizes)()
            component.backgroundColor()
            component.borderColor()
        }) {
            component.text.bind()
        }
    }
}

class RadioComponent {

    var size: RadioSizes.() -> Style<BasicParams> = { theme().radio.sizes.normal }
    fun size(value: RadioSizes.() -> Style<BasicParams>) {
        size = value
    }

    var text: Flow<String> = const("") // @label
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
    var checked: Flow<Boolean> = const(false) // @input

    var disabled: Flow<Boolean> = const(false) // @input
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
                box-shadow: 0 0 1px ${theme().colors.dark};
            }
            &:disabled + label {
                color: ${theme().colors.disabled};
                cursor: not-allowed;
            }
            &:disabled + label::before {
                opacity: 0.3;
                cursor: not-allowed;
                boxShadow: none;
                color: ${theme().colors.disabled};
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
                box-shadow: 0 0 1px ${ theme().colors.dark} inset;
            }
            """
        )
    }
}