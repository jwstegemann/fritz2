package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.components.CheckboxGroupComponent.Companion.checkboxGroupStructure
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.DirectionValues
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.CheckboxSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


/**
 * This class combines the _configuration_ and the core styling of a checkbox group.
 * The rendering itself is also done within the companion object.
 *
 * In order to render a checkbox group use the [checkboxGroup] factory function!
 *
 * This class offers the following _configuration_ features:
 *  - the text label of a checkbox (static or dynamic via a [Flow<String>])
 *  - the background color of the box
 *  - the background color for the checked state
 *  - some predefined styling variants
 *  - offer a list of items ([String])
 *  - offer a list of pre checked items
 *  - choose the direction of checkbox elements (row vs column)
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [CheckboxGroupComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the checkbox.
 *
 * Example usage
 * ```
 * // simple use case showing the core functionality
 * val options = listOf("A", "B", "C")
 * checkboxGroup {
 *      items { options } // provide a list of items
 *      initialSelection { listOf(options[0] + options[1]) } // pre check "A" and "C"
 * } handledBy selectedItemsStore.update // combine the Flow<List<String>> with a fitting handler
 *
 * // use case showing some styling options
 * val options = listOf("A", "B", "C")
 * checkboxGroup({ // this styling is only applied to the enclosing container element!
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
 *      checkboxSize { normal }
 *      borderColor { Theme().colors.secondary }
 *      checkedBackgroundColor { Theme().colors.warning }
 *      items { options } // provide a list of items
 *      initialSelection { listOf(options[0] + options[1]) } // pre check "A" and "C"
 * } handledBy selectedItemsStore.update // combine the Flow<List<String>> with a fitting handler
 * ```
 */
@ComponentMarker
class CheckboxGroupComponent {

    var items: List<String> = emptyList()
    fun items(value: () -> List<String>) {
        items = value()
    }

    // TODO: Think about harmonizing name with ``RadioGroupComponent.selected``
    var initialSelection: List<String> = emptyList()
    fun initialSelection(value: () -> List<String>) {
        initialSelection = value()
    }

    var disabled: Flow<Boolean> = flowOf(false) // @input
    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    var direction: Style<BasicParams> = { CheckboxGroupLayouts.column } // @fieldset
    fun direction(value: CheckboxGroupLayouts.() -> Style<BasicParams>) {
        direction =  CheckboxGroupLayouts.value()
    }

    var checkedBackgroundColor: ColorProperty = Theme().colors.primary // @checkbox @input
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

    var size: CheckboxSizes.() -> Style<BasicParams> = { Theme().checkbox.sizes.normal }
    fun size(value: CheckboxSizes.() -> Style<BasicParams>) {
        size = value
    }

    private class CheckboxGroupEntry(val value: String, val checked: Boolean)

    companion object {

        // TODO: Check how to *centralize* this (compare singleselect and formcontrol)
        // TODO: Change names to ``horizontal`` and ``vertical``?  Row and column is widely used in fritz2 for directions
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

        private fun RenderContext.checkboxGroupContent(
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
                    text(item.value)
                    component.size.invoke(Theme().checkbox.sizes)
                    size { component.size.invoke(Theme().checkbox.sizes) }
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

        fun RenderContext.checkboxGroupStructure(
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


/**
 * This component generates a *group* of checkboxes.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of each box or totally disable it.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [CheckboxGroupComponent]
 *
 * Example usage
 * ```
 * val options = listOf("A", "B", "C")
 * checkboxGroup {
 *     items { options } // provide a list of items
 *     initialSelection { listOf(options[0] + options[1]) } // pre check "A" and "C"
 * } handledBy selectedItemsStore.update // combine the Flow<List<String>> with a fitting handler
 * ```
 *
 * @see CheckboxGroupComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [CheckboxGroupComponent]
 * @return a flow of the _checked_ items
 */
// todo add dropdown multi select
fun RenderContext.checkboxGroup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "checkboxGroupComponent",
    build: CheckboxGroupComponent.() -> Unit = {}
): Flow<List<String>> {
    return checkboxGroupStructure(styling, null, baseClass, id, prefix, build)
}
