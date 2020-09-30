package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.states
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun HtmlElements.checkboxGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "checkboxGroupComponent",
    build: CheckboxGroupComponent.() -> Unit = {}
): Flow<List<String>> {

    val component = CheckboxGroupComponent().apply(build)

    class CheckboxGroupEntry(val value: String, val checked: Boolean)

    val selectedStore = object : RootStore<List<String>>(component.selectedItems) {
        val modifyList = handle<CheckboxGroupEntry> { list, item ->
            if (null != list.find { it == item.value }) { // item is in list
                if (!item.checked) {
                    list - item.value
                } else list
            } else { // item is not in list
                if (item.checked) {
                    list + item.value
                } else list
            }
        }
    }

    (::fieldset.styled(
        baseClass = baseClass,
        id = id,
        prefix = prefix) {
        styling() // attach user styling to container only
    }) {
        component.items.withIndex().forEach { item ->

            val checkThisBox = selectedStore.data.map { selectedItems ->
                null != selectedItems.find { it == item.value }
            }

            checkbox(
                {},
                id = "$id-checkbox-${item.index}"
            ) {
                text = const(item.value)
                checkboxSize { component.checkboxSize }
                borderColor { component.borderColor }
                backgroundColor { component.backgroundColor }
                checkedBackgroundColor { component.checkedBackgroundColor }
                checked { checkThisBox }
                disabled { component.disabled }
                events {
                    changes.states().map { checked ->
                        CheckboxGroupEntry(item.value, checked)
                    } handledBy selectedStore.modifyList
                }
            }

        }
    }
    return selectedStore.data
}

class CheckboxGroupComponent {

    var items: List<String> = emptyList()
    fun items(value: () -> List<String>) {
        items = value()
    }

    var selectedItems: List<String> = emptyList()
    fun selectedItems(value: () -> List<String>) {
        selectedItems = value()
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

    var checkboxSize: Style<BasicParams> = { CheckboxGroupSizes.normal } // @label
    fun checkboxSize(value: CheckboxGroupSizes.() -> Style<BasicParams>) {
        checkboxSize = CheckboxGroupSizes.value()
    }

    companion object {

        // todo duplicated code in checkboxcomponent.checkboxsizes
        // todo replace px with rem where not explicit (not due for 0.8 snapshot)
        object CheckboxGroupSizes {
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
                    radii {
                        top { smaller }
                        bottom { smaller }
                        left { smaller }
                        right { smaller }
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
                    radii {
                        top { normal }
                        bottom { normal }
                        left { normal }
                        right { normal }
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
                    radii {
                        top { larger }
                        bottom { larger }
                        left { larger }
                        right { larger }
                    }
                }
            }
        }
    }
}