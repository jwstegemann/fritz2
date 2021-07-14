package dev.fritz2.components


import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.input
import dev.fritz2.styling.name
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.InputFieldVariants
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLInputElement

/**
 * This class deals with the _configuration_ of an input element.
 *
 * The inputField can be configured for the following aspects:
 *  - the size of the element
 *  - some predefined styling variants
 *  - the element options of the HTML input element can be set.
 *    [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Attributes)
 *
 *  * For a detailed explanation and examples of usage have a look at the [inputField] function!
 */
open class InputFieldComponent(protected val valueStore: Store<String>?) :
    Component<Unit>,
    EventProperties<HTMLInputElement> by EventMixin(),
    ElementProperties<Input> by ElementMixin(),
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    companion object {

        val staticCss = staticStyle(
            "inputBox",
            """
                display: inline-flex;
                position: relative;
                vertical-align: middle;
                height: 2.5rem;
                appearance: none;
                align-items : center;
                justify-content: center;
                transition: all 250ms;
                white-space: nowrap;
                outline: none;
                width: 100%
            """
        )

    }

    val variant = ComponentProperty<InputFieldVariants.() -> Style<BasicParams>> { Theme().input.variants.outline }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().input.sizes.normal }

    val value = DynamicComponentProperty(flowOf(""))
    val placeholder = DynamicComponentProperty(flowOf(""))
    val type = DynamicComponentProperty(flowOf(""))
    val step = DynamicComponentProperty(flowOf(""))

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            input({
                this@InputFieldComponent.size.value.invoke(Theme().input.sizes)()
                this@InputFieldComponent.variant.value.invoke(Theme().input.variants)()
            }, styling, baseClass + staticCss, id, prefix) {
                disabled(this@InputFieldComponent.disabled.values)
                readOnly(this@InputFieldComponent.readonly.values)
                placeholder(this@InputFieldComponent.placeholder.values)
                value(this@InputFieldComponent.value.values)
                type(this@InputFieldComponent.type.values)
                step(this@InputFieldComponent.step.values)
                className(this@InputFieldComponent.severityClassOf(Theme().input.severity).name)
                this@InputFieldComponent.valueStore?.let {
                    value(it.data)
                    changes.values() handledBy it.update
                }
                this@InputFieldComponent.events.value.invoke(this)
                this@InputFieldComponent.element.value.invoke(this)
            }
        }
    }
}


/**
 * This component generates a text based input field.
 *
 * You can optionally pass in a store in order to set the value and react to updates _automatically_.
 *
 * There are options to choose from predefined sizes and some variants from the Theme.
 *
 * To enable or disable it or to make it readOnly just use the well known attributes of the HTML
 * [input element](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/Input). To manually set the value or
 * react to a change refer also to its event's. All that can be achieved via the [ElementMixin.element] property!
 *
 * ```
 * inputField(value = dataStore /* inject a store so all user inputs are automatically reflected! */) {
 *     placeholder("Placeholder") // render a placeholder text for empty field
 * }
 *
 * // all state management can also be done manually if needed:
 * val someStore = storeOf("")
 * inputField {
 *     placeholder("Enter text")
 *     value(someStore.data) // connect a flow to the component for setting its value
 *     events {
 *         changes.values() handledBy someStore.update // connect an handler for emitting the user input made
 *     }
 *     element {
 *         // exposes the underlying HTML input element for direct access. Use with caution!
 *     }
 * }
 *
 * // apply predefined size and variant
 * inputField(value = dataStore) {
 *      size { small } // render a smaller input
 *      variant { filled } // fill the background with ``light`` color
 *      placeholder("Placeholder") // render a placeholder text for empty field
 * }
 *
 * // Of course you can apply custom styling as well
 * inputField({ // just use the ``styling`` parameter!
 *      background {
 *          color { dark }
 *      }
 *      radius { "1rem" }
 * },
 * value = dataStore) {
 *      size { small } // render a smaller input
 *      placeholder("Placeholder") // render a placeholder text for empty field
 * }
 * ```
 *
 * @see InputFieldComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param value optional [Store] that holds the data of the input
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [InputFieldComponent]
 */
fun RenderContext.inputField(
    styling: BasicParams.() -> Unit = {},
    value: Store<String>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "inputField",
    build: InputFieldComponent.() -> Unit = {}
) {
    InputFieldComponent(value).apply(build).render(this, styling, baseClass, id, prefix)
}
