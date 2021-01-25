import dev.fritz2.binding.Store
import dev.fritz2.components.ComponentMarker
import dev.fritz2.components.icon
import dev.fritz2.components.optionSelect
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Select
import dev.fritz2.dom.selectedValue
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.*

/**
 * This class offers configuration for a selectField element:
 *  - element size
 *  - placeholder text
 *  - icon
 *  - predefined styling variants
 *  - the text which is shown -> label
 *  - disabling the element
 *
 *  The functional expression ``init``, which is the last parameter of the factory function, offers
 *  an initialized instance of this [SelectFieldComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the selectField.
 *
 *  * For a detailed explanation and examples of usage, have a look at the [selectField] function itself.
 */
@ComponentMarker
class SelectFieldComponent<T> {
    companion object {
        val staticCss = staticStyle(
            "selectFieldContainer",
            """
                  width: 50%;
                  height: fit-content;
                  position: relative;
                  outline:none;
                  appearance:none;
              """
        )
    }

    val basicSelectStyles: Style<BasicParams> = {

        width { full }
        paddings {
            left { "0.75rem" }
            right { "1.5rem" }
            bottom { "1px" }
        }
        lineHeight { normal }
        fontSize { "1rem" }
        height { "2.5rem" }
        radius { normal }
        focus {
            focus {
                border {
                    color { primary }
                }
                boxShadow { outline }
            }
        }

        disabled {
            background {
                color { disabled }
            }
        }
        position { relative { } }
        minWidth { "2.5rem" }

        css("outline: 0px;")
        css("appearance: none;")
        css("transition: all 0.2s ease 0s;")
    }

    val iconWrapperStyle: Style<BasicParams> = {
        position { absolute { } }
        display { inlineFlex }
        css("-webkit-box-align: center;")
        css("align-items: center;")
        css("-webkit-box-pack: center;")
        css("justify-content: center;")
        css("right: 0.5rem;")
        css("pointer-events: none;")
        css("top: 50%;")
        css("transform: translateY(-50%);")
    }

    val iconStyle: Style<BasicParams> = {
        height { "var(--select-icon-size)" }
    }

    var placeholder: Flow<T>? = null

    fun placeholder(value: T) {
        placeholder = flowOf(value)
    }

    fun placeholder(value: Flow<T>) {
        placeholder = value
    }


    var items: Flow<List<T>> = flowOf(emptyList())

    fun items(value: List<T>) {
        items = flowOf(value)
    }

    fun items(value: Flow<List<T>>) {
        items = value
    }

    var variant: SelectFieldVariants.() -> Style<BasicParams> = { Theme().select.variants.outline }

    fun variant(value: SelectFieldVariants.() -> Style<BasicParams>) {
        variant = value
    }

    var size: SelectFieldSizes.() -> Style<BasicParams> = { Theme().select.sizes.normal }

    fun size(value: SelectFieldSizes.() -> Style<BasicParams>) {
        size = value
    }


    var icon: IconDefinition = Theme().icons.chevronDown
    fun icon(value: Icons.() -> IconDefinition) {
        icon = Theme().icons.value()
    }

    var label: ((item: T) -> String) = { it.toString() }
    fun label(value: (item: T) -> String) {
        label = value
    }


    var value: ((item: T) -> String) = { it.toString() }
    fun value(param: (item: T) -> String) {
        value = param
    }

    var disabled: Flow<Boolean> = flowOf(false)

    fun disabled(value: Boolean) {
        disabled = flowOf(value)
    }

    fun disabled(value: Flow<Boolean>) {
        disabled = value
    }

    fun renderPlaceholderAndOptions(
        renderContext: Select,
        store: Store<T>,
        optionsMap: MutableMap<String, T>,
    ): Map<String, T> {
        renderContext.apply {

            placeholder!!.combine(items) { placeholder, items ->
                listOf(placeholder) + items
            }.renderEach { item ->
                handleRendering(optionsMap, item, store, this, this@SelectFieldComponent)
            }
        }

        return optionsMap
    }

    fun renderOptions(renderContext: Select, store: Store<T>, optionsMap: MutableMap<String, T>): Map<String, T> {

        renderContext.apply {
            items.renderEach { item ->
                handleRendering(optionsMap, item, store, this, this@SelectFieldComponent)
            }
        }

        return optionsMap
    }

    private fun handleRendering(
        optionsMap: MutableMap<String, T>,
        item: T,
        store: Store<T>,
        renderContext: RenderContext,
        selectComponent: SelectFieldComponent<T>,
    ): Option {

        optionsMap[item.toString()] = item
        val selected = store.data.map { it == item }

        return renderContext.optionSelect {

            value { selectComponent.value(item) }
            label { selectComponent.label(item) }
            selected(selected)
        }

    }

}

/**
 * This function generates a selectField element.
 *
 * You have to pass a store in order to handle the selected value,
 * and the events will be connected automatically.
 *
 * A basic use case:
 *
 *    val myOptions = listOf("black", "red", "yellow")
 *    val selectedItem = storeOf("")
 *
 *    selectField (store = selectedItem) {
 *          placeholder("My Placeholder")
 *          options(myOptions)
 *    }
 *
 *  Customize icon:
 *
 *    selectField(store = selectedItem) {
 *          placeholder("circleAdd icon")
 *          icon { fromTheme { circleAdd }}
 *    }
 *
 *  Variants and sizes:
 *
 *    selectField(store = selectedItem) {
 *          size { large }
 *          variant { flushed }
 *    }
 *
 *  Disable the component:
 *
 *  selectField(store = selectedItem) {
 *       options(myOptions)
 *       disabled(true)
 *  }
 *
 *  Set a specific label:
 *
 *  val persons = listOf(Person("John Doe", 37), Person("Jane Doe", 35))
 *  val store = storeOf(persons[0])
 *
 *   selectField(store = store) {
 *       options(persons)
 *       label { it.name }
 *  }
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store  a [Store] that holds the selected value of the select component
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param init a lambda expression for setting up the component itself. Details in [SelectFieldComponent]
 *
 */

fun <T> RenderContext.selectField(
    styling: BasicParams.() -> Unit = {},
    store: Store<T>,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "selectField",
    init: SelectFieldComponent<T>.() -> Unit,
) {
    val component = SelectFieldComponent<T>().apply(init)
    val optionsMap = mutableMapOf<String, T>()

    (::div.styled(styling, baseClass + SelectFieldComponent.staticCss, id, prefix) {


    }){
        (::select.styled(styling, baseClass) {
            component.basicSelectStyles()
            component.variant.invoke(Theme().select.variants)()
            component.size.invoke(Theme().select.sizes)()

        }){
            disabled(component.disabled)
            val mapContainingOptions = when (component.placeholder) {
                null -> component.renderOptions(this, store, optionsMap)
                else -> component.renderPlaceholderAndOptions(this, store, optionsMap)
            }

            changes.selectedValue()
                .map {
                    mapContainingOptions[it] ?: error("Error mapping selected value in selectField component.")
                } handledBy store.update

        }
        (::div.styled(prefix = "icon-wrapper") {
            component.size.invoke(Theme().select.sizes)()
            component.iconWrapperStyle()
        }){
            icon({
                component.iconStyle()
            }) { fromTheme { component.icon } }
        }
    }
}

