package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.RadioSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * This class combines the _configuration_ and the core styling of a radio group.
 * The rendering itself is also done within the companion object.
 *
 * In order to render a checkbox group use the [radioGroup] factory function!
 *
 * This class offers the following _configuration_ features:
 *  - the items as a flowOf(List<T>)
 *  - the label(mapping) of a switch (static, dynamic via a [Flow<String>] or customized content of a Div.RenderContext ) the the example below
 *  - some predefined styling variants (size)
 *  - the style of the items (radio)
 *  - the style selected state
 *  - the style of the label
 *  - link an external boolean flow to set the disabled state of the box
 *  - choose the direction of checkbox elements (row vs column)
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [RadioGroupComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the checkbox.
 *
 * Example usage
 * ```
 * // simple use case showing the core functionality
 * val options = listOf("A", "B", "C")
 * val myStore = storeOf(<String>)
 * radioGroup(store = myStore) {
 *      items { flowOf(options) } or use items(options) // provide a list of items you can
 * }
 *
 * // use case showing some styling options and a store of List<Pair<Int,String>>
 *   val myPairs = listOf((1 to "ffffff"), (2 to "rrrrrr" ), (3 to "iiiiii"), (4 to "tttttt"), ( 5 to "zzzzzz"), (6 to "222222"))
 *  val myStore = storeOf(<List<Pair<Int,String>>)
 * radioGroup(store = myStore) {
 *      label {
 *          it.second
 *      }
 *      size { large }
 *      items { flowOf(options) } or use items(options) // provide a list of items you can
 *      selectedStyle {{
 *           background { color {"green"}}
 *      }}
 * }
 * ```
 */
@ComponentMarker
class RadioGroupComponent<T> {
    companion object {
        object RadioGroupLayouts {
            val column: Style<BasicParams> = {
                display {
                    inlineGrid
                }
            }
            val row: Style<BasicParams> = {
                display {
                    inlineFlex
                }
            }
        }
    }
    var items: Flow<List<T>> = flowOf(emptyList())

    fun items(value: List<T>) {
        items = flowOf(value)
    }
    fun items(value: () -> Flow<List<T>>) {
        items = value()
    }

    var icon: IconDefinition = Theme().icons.check
    fun icon(value: () -> IconDefinition) {
        icon = value()
    }

    var label: ((item: T)  -> String) =  {it.toString()}
    fun label (value: (item: T)  -> String) {
        label = value
    }

    var disabled: Flow<Boolean> = flowOf(false)
    fun disabled(value: Flow<Boolean>) {
        disabled = value
    }

    fun disabled(value:  Boolean) {
        disabled = flowOf(value)
    }

    var direction: Style<BasicParams> = RadioGroupLayouts.column
    fun direction(value: RadioGroupLayouts.() -> Style<BasicParams>) {
        direction =  RadioGroupLayouts.value()
    }

    var size: RadioSizes.() -> Style<BasicParams> = { Theme().radio.sizes.normal }
    fun size(value: RadioSizes.() -> Style<BasicParams>) {
        size = value
    }

    var itemStyle: Style<BasicParams> = { Theme().radio.default() }
    fun itemStyle(value: () -> Style<BasicParams>) {
        itemStyle = value()
    }

    var labelStyle: Style<BasicParams> = { Theme().radio.label() }
    fun labelStyle(value: () -> Style<BasicParams>) {
        labelStyle = value()
    }

    var selectedStyle: Style<BasicParams> = { Theme().radio.selected() }
    fun selectedStyle(value: () -> Style<BasicParams>) {
        selectedStyle = value()
    }

}




/**
 * This component generates a group of radio buttons.
 *
 * You can set different kinds of properties like the label text, or styling aspects like the colors of the
 * background, the label, or the selected item. It needs a store containing the currently selected item.
 *
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [RadioGroupComponent].
 *
 * Example usage
 * ```
 * val options = listOf("A", "B", "C")
 * val store = storeOf("A")
 * radioGroup(store = store) {
 *     items { options } // provide a list of items
 * }
 * ```
 *
 * @see RadioGroupComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store holds the currently selected option among all options passed via [items] context
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [RadioGroupComponent]
 */
fun <T>RenderContext.radioGroup(
    styling: BasicParams.() -> Unit = {},
    store: Store<T>,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "radioGroupComponent",
    build: RadioGroupComponent<T>.() -> Unit = {}
) {
    val component = RadioGroupComponent<T>().apply(build)

    val grpId = id ?: uniqueId()
    (::div.styled(styling, baseClass, id, prefix) {
        component.direction()
    }) {
        component.items.renderEach { item ->
            val checkedFlow = store.data.map { it == item }.distinctUntilChanged()
            radio(styling = component.itemStyle, id = grpId + "-grp-item-" + uniqueId()) {
                size { component.size.invoke(Theme().radio.sizes) }
                labelStyle { component.labelStyle }
                selectedStyle { component.selectedStyle }
                label(component.label(item))
                selected(checkedFlow)
                disabled (component.disabled)
                events {
                    changes.states().map{ item } handledBy store.update
                }
            }
        }
    }
}