package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.name
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLInputElement

/**
 * This class combines the _configuration_ and the core styling of a checkbox.
 *
 *
 * This class offers the following _configuration_ features:
 *  - the label of a switch (static, dynamic via a [Flow<String>] or customized content of a Div.RenderContext )
 *  - some predefined styling variants (size)
 *  - the style of the checkbox
 *  - the style checked state
 *  - the style of the label
 *  - the checked icon ( use our icon library of our theme )
 *  - link an external boolean flow to set the checked state of the box
 *  - link an external boolean flow to set the disabled state of the box
 *  - link events of the checkbox like ``changes`` with external handlers
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [CheckboxComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the checkbox.
 *
 * Example usage
 * ```
 * checkbox {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 *      element {
 *          // exposes the underlying HTML input element for direct access. Use with caution!
 *      }
 * }
 * ```
 */
@ComponentMarker
open class CheckboxComponent(protected val store: Store<Boolean>?) :
    Component<Label>,
    EventProperties<HTMLInputElement> by EventMixin(),
    ElementProperties<Input> by ElementMixin(),
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    companion object {
        val checkboxInputStaticCss = staticStyle(
            "checkbox",
            """
            position: absolute;
            height: 1px;                
            width: 1px;                
            overflow: hidden;
            clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
            clip: rect(1px, 1px, 1px, 1px);
            outline: none;           
            &:focus{
                outline: none;
            }           
            """
        )
    }

    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().checkbox.sizes.normal }
    val icon = ComponentProperty<Icons.() -> IconDefinition> { Theme().icons.check }

    private var labelField: (RenderContext.() -> Unit)? = null

    fun label(value: String) {
        labelField = {
            span { +value }
        }
    }

    fun label(value: Flow<String>) {
        labelField = {
            span { value.asText() }
        }
    }

    fun label(value: (RenderContext.() -> Unit)) {
        labelField = value
    }

    val labelStyle = ComponentProperty(Theme().checkbox.label)
    val checked = DynamicComponentProperty(flowOf(false))
    var checkedStyle = ComponentProperty(Theme().checkbox.checked)

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Label = with(context) {
        (::label.styled(
            baseClass = baseClass,
            id = id,
            prefix = prefix
        ) {
            size.value.invoke(Theme().checkbox.sizes)()
        }) {
            val inputId = id?.let { "$it-input" }
            inputId?.let {
                `for`(inputId)
            }
            (::input.styled(
                baseClass = checkboxInputStaticCss,
                prefix = prefix,
                id = inputId
            ) {
                Theme().checkbox.input()
                children("&[checked] + div") {
                    checkedStyle.value()
                }
            }) {
                disabled(disabled.values)
                readOnly(readonly.values)
                type("checkbox")
                checked(store?.data ?: checked.values)
                className(severityClassOf(Theme().checkbox.severity, prefix).name)
                store?.let { changes.states() handledBy it.update }
                events.value.invoke(this)
                element.value.invoke(this)
            }

            (::div.styled() {
                Theme().checkbox.default()
                styling()
            }) {
                icon({
                    Theme().checkbox.icon()
                }) {
                    def(icon.value(Theme().icons))
                }
            }

            labelField?.let {
                (::div.styled {
                    labelStyle.value()
                }){
                    it(this)
                }
            }
        }

    }
}

/**
 * This component generates a *single* checkbox.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of this box or totally disable it.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [CheckboxComponent]
 *
 * Example usage
 * ```
 * val cheeseStore = storeOf(false)
 * checkbox(store = cheeseStore) {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 * }
 *
 * // one can handle the events and preselect the control also manually if needed:
 * checkbox {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 *
 * @see CheckboxComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [CheckboxComponent]
 */
fun RenderContext.checkbox(
    styling: BasicParams.() -> Unit = {},
    store: Store<Boolean>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "checkboxComponent",
    build: CheckboxComponent.() -> Unit = {}
): Label = CheckboxComponent(store).apply(build).render(this, styling, baseClass, id, prefix)
