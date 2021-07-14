package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.components.foundations.*
import dev.fritz2.dom.EventContext
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.selectedValue
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement

/**
 * This class offers configuration for a [selectField] element:
 *  - element size
 *  - placeholder text
 *  - icon
 *  - predefined styling variants
 *  - the text which is shown -> label
 *  - disabling the element
 *
 *  The functional expression `build`, which is the last parameter of the factory function, offers
 *  an initialized instance of this [SelectFieldComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the [selectField].
 *
 *  For a detailed explanation and examples of usage, have a look at the [selectField] function itself.
 */
open class SelectFieldComponent<T>(protected val items: List<T>, protected val value: Store<T>? = null) :
    Component<Unit>,
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    companion object {
        val staticCss = staticStyle(
            "selectFieldContainer",
            """
                  width: 100%;
                  height: fit-content;
                  position: relative;
                  outline:none;
                  appearance:none;
              """
        )
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

    val selectedItem = NullableDynamicComponentProperty<T>(emptyFlow())

    class EventsContext<T>(private val element: RenderContext, val selected: Flow<T>) :
        EventContext<HTMLElement> by element

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
            (this@SelectFieldComponent.value?.data ?: this@SelectFieldComponent.selectedItem.values)
                .map { selectedItem ->
                    this@SelectFieldComponent.items.indexOf(selectedItem).let { if (it == -1) null else it }
                } handledBy internalStore.update

            div({}, styling, baseClass + staticCss, grpId, prefix) {
                select({
                    this@SelectFieldComponent.variant.value.invoke(Theme().select.variants)()
                    this@SelectFieldComponent.size.value.invoke(Theme().select.sizes)()
                }, styling, baseClass) {
                    disabled(this@SelectFieldComponent.disabled.values)

                    internalStore.data.render {
                        if (it == null) {
                            option {
                                value("null")
                                selected(true)
                                +this@SelectFieldComponent.placeholder.value
                            }
                        }
                    }

                    this@SelectFieldComponent.items.withIndex().forEach { (index, item) ->
                        val checkedFlow = internalStore.data.map { it == index }.distinctUntilChanged()
                        option {
                            value(index.toString())
                            selected(checkedFlow)
                            +this@SelectFieldComponent.label.value(item)
                        }
                    }

                    className(this@SelectFieldComponent.severityClassOf(Theme().select.severity).name)

                    changes.selectedValue().map { it.toInt() } handledBy internalStore.toggle
                }

                div({
                    this@SelectFieldComponent.size.value.invoke(Theme().select.sizes)()
                    this@SelectFieldComponent.iconWrapperStyle()
                }, prefix = "icon-wrapper") {
                    icon({
                        this@SelectFieldComponent.iconStyle()
                    }) { def(this@SelectFieldComponent.icon.value(Theme().icons)) }
                }
                EventsContext(this, internalStore.toggle.map { this@SelectFieldComponent.items[it] }).apply {
                    this@SelectFieldComponent.events.value(this)
                    this@SelectFieldComponent.value?.let { selected handledBy it.update }
                }
            }
        }
    }
}

/**
 * This function generates a [selectField] element.
 *
 * You have to pass a store as value in order to handle the selected value,
 * and the events will be connected automatically.
 *
 * A basic use case:
 * ```
 * val myOptions = listOf("black", "red", "yellow")
 * val selectedItem = storeOf("red") // preselect "red"
 * selectField (items = myOptions, value = selectedItem) {
 * }
 * ```
 *
 * Preselect nothing and set a placeholder text:
 * ```
 * val myOptions = listOf("black", "red", "yellow")
 * val selectedItem = storeOf<String?>(null)
 * selectField (items = myOptions, value = selectedItem) {
 *      placeholder("My Placeholder") // will be shown until some item is selected!
 * }
 * ```
 *
 * Customize the appearance:
 * ```
 * selectField (items = myOptions, value = selectedItem) {
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
 * selectField(items = persons, value = selectedItem) {
 *      label { it.name } // pass a lambda expression to create a label string of an specific type
 * }
 * ```
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param items a list of all available options
 * @param value for backing up the preselected item and reflecting the selection automatically.
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 * @param build a lambda expression for setting up the component itself. Details in [SelectFieldComponent]
 *
 */
fun <T> RenderContext.selectField(
    styling: BasicParams.() -> Unit = {},
    items: List<T>,
    value: Store<T>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "selectField",
    build: SelectFieldComponent<T>.() -> Unit,
) {
    SelectFieldComponent(items, value).apply(build).render(this, styling, baseClass, id, prefix)
}
