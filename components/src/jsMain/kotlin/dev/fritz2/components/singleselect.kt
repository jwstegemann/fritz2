package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement

// todo implement defaultChecked for radio, checkbox
// todo remove hardcoded color names from checkbox and radio

fun HtmlElements.radioGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "radioGroupComponent",
    build: RadioGroupComponent.() -> Unit = {}
): Flow<String> {

    val component = RadioGroupComponent().apply(build)

    val selectedStore = RootStore(component.selected)

    (::fieldset.styled(
        baseClass = baseClass,
        id = id,
        prefix = prefix) {
        styling() // attach user styling to container only
    }) {
        component.items.withIndex().forEach { item ->
            radio(
                styling = {},
                id = "$id-radio-${item.index}",
                prefix = prefix
            ) {
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
    }
    return selectedStore.data
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

    var radioSize: Style<BasicParams> = { RadioGroupSizes.normal } // @label
    fun radioSize(value: RadioGroupSizes.() -> Style<BasicParams>) {
        radioSize = RadioGroupSizes.value()
    }

    companion object {

        // todo duplicated code in radiocomponent.radiosizes
        // todo replace px with rem where not explicit (not due for 0.8 snapshot)
        object RadioGroupSizes {
            val small: Style<BasicParams> = {
                fontSize { small }
                before {
                    height { "10px" }
                    width { "10px" }
                    border {
                        width { "1px" }
                    }
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
                before {
                    height { "20px" }
                    width { "20px" }
                    border {
                        width { "2px" }
                    }
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
                before {
                    height { "30px" }
                    width { "30px" }
                    border {
                        width { "3px" }
                    }
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
            RadioComponent.radioStyles()
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
    fun checked(value: () -> Flow<Boolean>) {
        checked = value()
    }
    var disabled: Flow<Boolean> = const(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    companion object {

        // todo when using with checkboxgroup, duplicated css code is passed instead of this
        // todo replace px with rem where not explicit (not due for 0.8 snapshot)
        object RadioSizes {
            val small: Style<BasicParams> = {
                fontSize { small }
                before {
                    height { "10px" }
                    width { "10px" }
                    border {
                        width { "1px" }
                    }
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
                before {
                    height { "20px" }
                    width { "20px" }
                    border {
                        width { "2px" }
                    }
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
                before {
                    height { "30px" }
                    width { "30px" }
                    border {
                        width { "3px" }
                    }
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

        val radioLabelStyles: Style<BasicParams> = {
            before {
                radii {
                    right { "50%" }
                    left { "50%" }
                    top { "50%" }
                    bottom { "50%" }
                }
            }
        }

        val radioStyles: Style<BasicParams> = {
            lineHeight { normal }
            radius { normal }
            fontWeight { normal }
            paddings { horizontal { small } }
            border {
                width { thin }
                style { solid }
                color { light }
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
                &:checked + label::before {
                    border-style: solid;
                    outline: none;
                }
                &:focus + label::before {
                    box-shadow: 0 0px 4px grey;
                }
                &:disabled + label {
                    color: #878787;
                    cursor: not-allowed;
                }
                &:disabled + label::before {
                    opacity: 0.3;
                    cursor: not-allowed;
                    boxShadow: none;
                    color: #575757;
                }
                &:focus{
                    outline: none;
                }
            """
        )

        val radioLabelStaticCss = staticStyle(
            "radiolabel",
            """
            display: block;
            position: relative;
            margin-right: 1.0rem;
            &::before {
                content: '';
                position: relative;
                display: inline-block;
                vertical-align: middle;
                box-shadow: 0 0 2px #575757 inset;
            }
            """
        )
    }
}


// todo add multiselect
// todo update everything below this line to new concepts
//
//class SingleSelectionContext<T>(val selected: Flow<T>)
//
//internal object SingleSelectFoundation {
//    val label = staticStyle(
//        "singleSelectLabel",
//        """
//        margin-right: 0.5rem;
//        """
//    )
//    val select = staticStyle(
//        "singleSelect",
//        """
//        border-width: 1px;
//        &:disabled {
//            opacity: 0.5;
//            cursor: not-allowed;
//            boxShadow: none;
//        }
//        &:focus {
//            outline: none;
//        }
//        &:hover {
//            outline:none;
//        }
//        """
//    )
//    val option = staticStyle(
//        "singleSelectOption",
//        """
//        box-shadow: 0 0 1px #444444 inset;
//        """
//    )
//}

//object SingleSelectSizes {
//    val small: Style<BasicParams> = {
//        fontSize { smaller }
//        radii {
//            top { smaller }
//            bottom { smaller }
//            left { smaller }
//            right { smaller }
//        }
//    }
//    val normal: Style<BasicParams> = {
//        fontSize { normal }
//        radii {
//            top { normal }
//            bottom { normal }
//            left { normal }
//            right { normal }
//        }
//    }
//    val large: Style<BasicParams> = {
//        fontSize { larger }
//        radii {
//            top { larger }
//            bottom { larger }
//            left { larger }
//            right { larger }
//        }
//    }
//}
//
//fun <T : Any> HtmlElements.f2SingleSelect(
//    styles: Style<BasicParams> = {},
//    legend: Flow<String>? = null,
//    options: Flow<List<T>>,
//    selected: Flow<T>,
//    inactive: Flow<Boolean>,
//    size: SingleSelectSizes.() -> Style<BasicParams> = { normal },
//    borderColor: ColorProperty = theme().colors.primary,
//    backgroundColor: ColorProperty = rgb(255, 255, 255),
//    selectedBackgroundColor: ColorProperty = theme().colors.secondary,
//    layoutHorizontally: Boolean = false,
//    optionToString: T.() -> String = Any::toString,
//    id: String = uniqueId(),
//    init: SingleSelectionContext<T>.() -> Any
//): Div {
//
//    return div(id = id) {
//        if (legend != null) {
//            label(
//                id = "label-$id",
//                `for` = "select-$id"
//            ) {
//                legend.bind()
//            }
//        }
//        select(
//            "select-$id"
//        ) {
//            inactive.bindAttr("disabled")
//            options.map { it.withIndex().toList() }.each().map { indexedT ->
//                render {
//                    option(
//                        "option-$id-${indexedT.index}"
//                    ) {
//                        value = const(indexedT.index.toString())
//                        selected.map { selT ->
//                            if (indexedT.value == selT)
//                                this@select.selectedIndex = const(indexedT.index)
//                        }
//                        +indexedT.value.optionToString()
//                    }
//                }
//            }.bind()
//
//            SingleSelectionContext(
//                changes.selectedIndex().flatMapLatest { index ->
//                    options.map { list ->
//                        list[index]
//                    }
//                }
//            ).init()
//        }
//    }
//}