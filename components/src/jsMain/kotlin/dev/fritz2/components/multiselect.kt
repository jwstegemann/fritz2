package dev.fritz2.components

import dev.fritz2.binding.*
import dev.fritz2.components.CheckboxGroupComponent.Companion.checkboxGroupStructure
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.states
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.CheckboxSizes
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

// todo add dropdown multi select

fun HtmlElements.checkboxGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "checkboxGroupComponent",
    build: CheckboxGroupComponent.() -> Unit = {}
): Flow<List<String>> {
    return checkboxGroupStructure(styling, null, baseClass, id, prefix, build)
}

class CheckboxGroupComponent {

    var items: List<String> = emptyList()
    fun items(value: () -> List<String>) {
        items = value()
    }

    var initialSelection: List<String> = emptyList()
    fun initialSelection(value: () -> List<String>) {
        initialSelection = value()
    }

    var disabled: Flow<Boolean> = const(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    var direction: Style<BasicParams> = { CheckboxGroupLayouts.column } // @fieldset
    fun direction(value: CheckboxGroupLayouts.() -> Style<BasicParams>) {
        direction =  CheckboxGroupLayouts.value()
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

    var size: CheckboxSizes.() -> Style<BasicParams> = { theme().checkbox.sizes.normal }
    fun size(value: CheckboxSizes.() -> Style<BasicParams>) {
        size = value
    }

    private class CheckboxGroupEntry(val value: String, val checked: Boolean)

    companion object {

        object CheckboxGroupLayouts { // @ fieldset
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

        private fun HtmlElements.checkboxGroupContent(
            id: String?,
            component: CheckboxGroupComponent
        ): Flow<List<String>> {

            val selectedStore = object : RootStore<List<String>>(component.initialSelection) {
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
            component.items.withIndex().forEach { item ->

                val checkThisBox = selectedStore.data.map { selectedItems ->
                    null != selectedItems.find { it == item.value }
                }

                checkbox(id = "$id-checkbox-${item.index}") {
                    text = const(item.value)
                    component.size.invoke(theme().checkbox.sizes)
                    size { component.size.invoke(theme().checkbox.sizes) }
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
            return selectedStore.data
        }

        fun HtmlElements.checkboxGroupStructure(
            containerStyling: BasicParams.() -> Unit = {},
            selectedItemsStore: RootStore<List<String>>? = null,
            baseClass: StyleClass? = null,
            id: String? = null,
            prefix: String = "checkboxGroupComponent",
            build: CheckboxGroupComponent.() -> Unit = {}
        ): Flow<List<String>> {

            val component = CheckboxGroupComponent().apply(build)
            var selItems: Flow<List<String>> = flowOf(emptyList())

            if (null == selectedItemsStore) { // group is not used in form control
                (::fieldset.styled(
                    baseClass = baseClass,
                    id = id,
                    prefix = prefix
                ) {
                    containerStyling()
                    component.direction()
                }) {

                    // outside of form controls, returning the flow works just fine
                    selItems = checkboxGroupContent(id, component)
                }
            } else {
                // when rendered in a form control, store ensures timely binding
                checkboxGroupContent(
                    id, component
                ).handledBy(selectedItemsStore.update)
            }
            return selItems
        }
    }
}