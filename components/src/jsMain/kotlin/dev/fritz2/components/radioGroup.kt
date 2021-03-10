package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

/**
 * This class combines the _configuration_ and the core styling of a radio group.
 * The rendering itself is also done within the companion object.
 *
 * In order to render a checkbox group use the [radioGroup] factory function!
 *
 * This class offers the following _configuration_ features:
 *  - the items as a ``List<T>``
 *  - optionally set a predefined item; if nothing is set or ``null``, nothing gets selected at first
 *  - the label(mapping) of a switch (static, dynamic via a [Flow<String>] or customized content of a Div.RenderContext ) the the example below
 *  - some predefined styling variants (size)
 *  - the style of the items (radio)
 *  - the style selected state
 *  - the style of the label
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
 * val myStore = storeOf("B") // or ``null`` to select nothing
 * radioGroup(items = options, store = myStore) {
 * }
 *
 * // one can handle the events and preselected item also manually if needed:
 * val options = listOf("A", "B", "C")
 * radioGroup(items = options) {
 *      selectedItem("A") // or ``null`` (default) if nothing should be selected at all
 *      events {
 *          selected handledBy someStoreOfString
 *      }
 * }
 *
 * // use case showing some styling options and a store of List<Pair<Int,String>>
 * val myPairs = listOf((1 to "ffffff"), (2 to "rrrrrr" ), (3 to "iiiiii"), (4 to "tttttt"), ( 5 to "zzzzzz"), (6 to "222222"))
 * val myStore = storeOf(<List<Pair<Int,String>>)
 * radioGroup(items = myPairs, store = myStore) {
 *     label { it.second }
 *     size { large }
 *     selectedStyle {
 *          background { color {"green"} }
 *     }
 * }
 * ```
 */
@ComponentMarker
open class RadioGroupComponent<T>(protected val items: List<T>, protected val store: Store<T>? = null) :
    Component<Unit>,
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

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

    val label = ComponentProperty<(item: T) -> String> { it.toString() }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().radio.sizes.normal }

    val direction = ComponentProperty<RadioGroupLayouts.() -> Style<BasicParams>> { column }
    val itemStyle = ComponentProperty(Theme().radio.default)
    val labelStyle = ComponentProperty(Theme().radio.label)
    val selectedStyle = ComponentProperty(Theme().radio.selected)

    val selectedItem = NullableDynamicComponentProperty<T>(emptyFlow())

    class EventsContext<T>(val selected: Flow<T>) {
    }

    val events = ComponentProperty<EventsContext<T>.() -> Unit> {}

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val internalStore = SingleSelectionStore()
        val grpId = id ?: uniqueId()

        context.apply {
            (::div.styled(styling, baseClass, id, prefix) {
                direction.value(RadioGroupLayouts)()
            }) {
                (store?.data ?: selectedItem.values)
                    .map { selectedItem ->
                        items.indexOf(selectedItem).let { if (it == -1) null else it }
                    } handledBy internalStore.update

                items.withIndex().forEach { (index, item) ->
                    val checkedFlow = internalStore.data.map { it == index }.distinctUntilChanged()
                    radio(styling = itemStyle.value, id = grpId + "-grp-item-" + uniqueId()) {
                        this.size { this@RadioGroupComponent.size.value.invoke(Theme().radio.sizes) }
                        labelStyle(labelStyle.value)
                        selectedStyle(selectedStyle.value)
                        label(this@RadioGroupComponent.label.value(item))
                        selected(checkedFlow)
                        disabled(this@RadioGroupComponent.disabled.values)
                        severity(this@RadioGroupComponent.severity.values)
                        events {
                            changes.states().map { index } handledBy internalStore.toggle
                        }
                    }
                }
            }
            EventsContext(internalStore.toggle.map { items[it] }).apply {
                events.value(this)
                store?.let { selected handledBy it.update }
            }
        }
    }
}


/**
 * This component generates a *group* of radio buttons.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the selected item.
 *
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [RadioGroupComponent]
 *
 * Example usage
 * ```
 * val options = listOf("A", "B", "C")
 * radioGroup(items = options, store = selectedItemStore) {
 *     selectedItem(options[1]) // pre select "B", or ``null`` (default) to select nothing
 * }
 * ```
 *
 * @see RadioGroupComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param items a list of all available options
 * @param store for backing up the preselected item and reflecting the selection automatically.
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [RadioGroupComponent]
 * @return a flow of the _selected_ item
 */
fun <T> RenderContext.radioGroup(
    styling: BasicParams.() -> Unit = {},
    items: List<T>,
    store: Store<T>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "radioGroupComponent",
    build: RadioGroupComponent<T>.() -> Unit = {}
) {
    RadioGroupComponent(items, store).apply(build).render(this, styling, baseClass, id, prefix)
}