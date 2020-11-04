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
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement

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

    var checkedBackgroundColor: ColorProperty = "black" // @checkbox @input
    fun checkedBackgroundColor(value: () -> ColorProperty) {
        checkedBackgroundColor = value()
    }

    var backgroundColor: ColorProperty = "white" // @label
    fun backgroundColor(value: () -> ColorProperty) {
        backgroundColor = value()
    }

    var borderColor: ColorProperty = "grey"  // @label
    fun borderColor(value: () -> ColorProperty) {
        borderColor = value()
    }

    var radioSize: Style<BasicParams> = { RadioComponent.Companion.RadioSizes.normal } // @label
    fun radioSize(value: RadioComponent.Companion.RadioSizes.() -> Style<BasicParams>) {
        radioSize = RadioComponent.Companion.RadioSizes.value()
    }

    companion object {
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
                    radioSize { component.radioSize }
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
            component.radioSize()
            component.backgroundColor()
            component.borderColor()
        }) {
            component.text.bind()
        }
    }
}

class RadioComponent {

    var radioSize: Style<BasicParams> = { RadioSizes.small } // @label
    fun radioSize(value: RadioSizes.() -> Style<BasicParams>) {
        radioSize = RadioSizes.value()
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

        // todo replace px with rem where not explicit (not due for 0.8 snapshot)
        object RadioSizes { // @label
            val small: Style<BasicParams> = {
                fontSize { small }
                lineHeight { small }
                before {
                    height { "10px" }
                    width { "10px" }
                    margins {
                        right { "4px" }
                    }
                    position {
                        relative {
                            bottom { "1px" }
                        }
                    }
                }
            }
            val normal: Style<BasicParams> = {
                fontSize { normal }
                lineHeight { normal }
                before {
                    height { "20px" }
                    width { "20px" }
                    margins {
                        right { "7px" }
                    }
                    position {
                        relative {
                            bottom { "2px" }
                        }
                    }
                }
            }
            val large: Style<BasicParams> = {
                fontSize { larger }
                lineHeight { larger }
                before {
                    height { "30px" }
                    width { "30px" }
                    margins {
                        right { "10px" }
                    }
                    position {
                        relative {
                            bottom { "3px" }
                        }
                    }
                }
            }
        }

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
                margins {
                    right { "1.0rem" }
                }
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