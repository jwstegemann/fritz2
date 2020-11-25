package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.Store
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.DirectionValues
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.CheckboxSizes
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement


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
 *      borderColor { theme().colors.secondary }
 *      checkedBackgroundColor { theme().colors.warning }
 *      items { options } // provide a list of items
 *      initialSelection { listOf(options[0] + options[1]) } // pre check "A" and "C"
 * } handledBy selectedItemsStore.update // combine the Flow<List<String>> with a fitting handler
 * ```
 */
class CheckboxGroupComponent<T> {
    companion object {
        object CheckboxGroupLayouts {
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
    fun items(value: () -> Flow<List<T>>) {
        items = value()
    }

    var icon: IconDefinition = theme().icons.check
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

    var direction: Style<BasicParams> = CheckboxGroupLayouts.column
    fun direction(value: CheckboxGroupLayouts.() -> Style<BasicParams>) {
        direction =  CheckboxGroupLayouts.value()
    }

    var size: CheckboxSizes.() -> Style<BasicParams> = { theme().checkbox.sizes.normal }
    fun size(value: CheckboxSizes.() -> Style<BasicParams>) {
        size = value
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
fun <T>RenderContext.checkboxGroup(
    styling: BasicParams.() -> Unit = {},
    store: Store<List<T>>,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "checkboxGroupComponent",
    build: CheckboxGroupComponent<T>.() -> Unit = {}
) {
    val component = CheckboxGroupComponent<T>().apply(build)

    val toggle = store.handle<T> { list, item ->
        if( list.contains(item) ) {
            list - item
        } else {
            list + item
        }
    }


    val grpId = id ?: uniqueId()
    (::div.styled(styling, baseClass, id, prefix) {
        component.direction()
    }) {
        component.items.renderEach { item ->
            val checkedFlow = store.data.map { it.contains(item) }.distinctUntilChanged()
            checkbox(id = grpId + "-grp-item-" + uniqueId()) {
                size { component.size.invoke(theme().checkbox.sizes) }
                icon { component.icon }
                label(component.label(item))
                checked { checkedFlow }
                disabled { component.disabled }
                events {
                   changes.states().map{ item } handledBy toggle
                }
            }
        }
    }
}

