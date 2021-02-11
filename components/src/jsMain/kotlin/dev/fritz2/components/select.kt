import dev.fritz2.binding.Store
import dev.fritz2.components.*
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.selectedValue
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.className
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
 *  The functional expression ``build``, which is the last parameter of the factory function, offers
 *  an initialized instance of this [SelectFieldComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the selectField.
 *
 *  For a detailed explanation and examples of usage, have a look at the [selectField] function itself.
 */
@ComponentMarker
class SelectFieldComponent<T> : InputFormProperties by InputFormMixinMixin(), SeverityProperties by SeverityMixin() {

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
        hover {
            border {
                color { dark }
            }
        }

        disabled {
            background {
                color { base }
            }
            color { disabled }
            hover {
                border {
                    color { lightGray }
                }
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

    val placeholder = ComponentProperty("...")
    val variant = ComponentProperty<SelectFieldVariants.() -> Style<BasicParams>> { outline }
    val label = ComponentProperty<(item: T) -> String> { it.toString() }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { normal }
    val icon = ComponentProperty<Icons.() -> IconDefinition> { chevronDown }

    val selectedItem = NullableDynamicComponentProperty<T>(flowOf(null))

    class EventsContext<T>(val selected: Flow<T>) {
    }

    val events = ComponentProperty<EventsContext<T>.() -> Unit> {}
}

/**
 * This function generates a selectField element.
 *
 * You have to pass a store in order to handle the selected value,
 * and the events will be connected automatically.
 *
 * A basic use case:
 * ```
 * val myOptions = listOf("black", "red", "yellow")
 * val selectedItem = storeOf("red") // preselect "red"
 * selectField (items = myOptions, store = selectedItem) {
 * }
 * ```
 *
 * Preselect nothing and set a placeholder text:
 * ```
 * val myOptions = listOf("black", "red", "yellow")
 * val selectedItem = storeOf<String?>(null)
 * selectField (items = myOptions, store = selectedItem) {
 *      placeholder("My Placeholder") // will be shown until some item is selected!
 * }
 * ```
 *
 * Customize the appearance:
 * ```
 * selectField (items = myOptions, store = selectedItem) {
 *      icon { fromTheme { circleAdd } }
 *      size { large }
 *      variant { flushed }
 * }
 * ```
 *
 * Set a specific label:
 * ```
 * val persons = listOf(Person("John Doe", 37), Person("Jane Doe", 35))
 * val selectedItem = storeOf(persons[0])
 * selectField(items = persons, store = selectedItem) {
 *      label { it.name } // pass a lambda expression to create a label string of an specific type
 * }
 * ```
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param items a list of all available options
 * @param store for backing up the preselected item and reflecting the selection automatically.
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SelectFieldComponent]
 *
 */
fun <T> RenderContext.selectField(
    styling: BasicParams.() -> Unit = {},
    items: List<T>,
    store: Store<T>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "selectField",
    build: SelectFieldComponent<T>.() -> Unit,
) {
    val component = SelectFieldComponent<T>().apply(build)
    val internalStore = SingleSelectionStore()

    val grpId = id ?: uniqueId()

    (store?.data ?: component.selectedItem.values)
        .map { selectedItem ->
            items.indexOf(selectedItem).let { if (it == -1) null else it }
        } handledBy internalStore.update

    (::div.styled(styling, baseClass + SelectFieldComponent.staticCss, grpId, prefix) {}){

        (::select.styled(styling, baseClass) {
            component.basicSelectStyles()
            component.variant.value.invoke(Theme().select.variants)()
            component.size.value.invoke(Theme().select.sizes)()
        }){
            disabled(component.disabled.values)

            internalStore.data.render {
                if (it == null) {
                    option {
                        value("null")
                        selected(true)
                        +component.placeholder.value
                    }
                }
            }

            items.withIndex().forEach { (index, item) ->
                val checkedFlow = internalStore.data.map { it == index }.distinctUntilChanged()
                option {
                    value(index.toString())
                    selected(checkedFlow)
                    +component.label.value(item)
                }
            }

            className(component.severityClassOf(Theme().select.severity, prefix))

            changes.selectedValue().map { it.toInt() } handledBy internalStore.toggle
        }

        (::div.styled(prefix = "icon-wrapper") {
            component.size.value.invoke(Theme().select.sizes)()
            component.iconWrapperStyle()
        }){
            icon({
                component.iconStyle()
            }) { def(component.icon.value(Theme().icons)) }
        }
    }

    SelectFieldComponent.EventsContext(internalStore.toggle.map { items[it] }).apply {
        component.events.value(this)
        store?.let { selected handledBy it.update }
    }
}
