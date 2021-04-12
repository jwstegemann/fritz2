package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Div
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
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement

/**
 * This class combines the _configuration_ and the core styling of a switch.
 *
 *
 * This class offers the following _configuration_ features:
 *  - the optional label of a switch (static, dynamic via a [Flow<String>] or customized content of a Div.RenderContext )
 *  - some predefined styling variants (size)
 *  - the style of the selected state
 *  - the style of the radio
 *  - the style of the label
 *  - the groupname
 *  - link an external boolean flow to set the selected state of the box
 *  - link an external boolean flow to set the disabled state of the box
 *  - link events of the switch like ``changes`` with external handlers
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [RadioComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the radio.
 *
 * Example usage
 * ```
 * radio {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      selected(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 *      element {
 *          // exposes the underlying HTML input element for direct access. Use with caution!
 *      }
 * }
 * ```
 */
open class RadioComponent(protected val store: Store<Boolean>? = null) :
    Component<Label>,
    EventProperties<HTMLInputElement> by EventMixin(),
    ElementProperties<Input> by ElementMixin(),
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    companion object {
        val radioInputStaticCss = staticStyle(
            "radioInput",
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
            &:focus + label::before {
                box-shadow: 0 0 1px ${Theme().colors.gray700};
            }
            &:disabled + label {
                color: ${Theme().colors.disabled};
                cursor: not-allowed;
            }
            &:disabled + label::before {
                opacity: 0.3;
                cursor: not-allowed;
                boxShadow: none;
                color: ${Theme().colors.disabled};
            }
            """
        )
    }

    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().radio.sizes.normal }

    private var label: (Div.() -> Unit)? = null
    fun label(value: String) {
        label = {
            +value
        }
    }

    fun label(value: Flow<String>) {
        label = {
            value.asText()
        }
    }

    fun label(value: (Div.() -> Unit)) {
        label = value
    }

    val labelStyle = ComponentProperty(Theme().radio.label)
    val selectedStyle = ComponentProperty(Theme().radio.selected)
    val selected = DynamicComponentProperty(flowOf(false))
    val groupName = DynamicComponentProperty(flowOf(""))

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Label {
        val inputId = id?.let { "$it-input" }
        val alternativeGroupname = id?.let { "$it-groupName" }
        val inputName = groupName.values.map {
            if (it.isEmpty()) {
                alternativeGroupname ?: ""
            } else {
                it
            }
        }

        return with(context) {
            (::label.styled(
                baseClass = baseClass,
                id = id,
                prefix = prefix
            ) {
                this@RadioComponent.size.value.invoke(Theme().radio.sizes)()
            }) {
                inputId?.let {
                    `for`(inputId)
                }
                (::input.styled(
                    baseClass = radioInputStaticCss,
                    prefix = prefix,
                    id = inputId
                ) {
                    Theme().radio.input()
                    children("&[checked] + div") {
                        this@RadioComponent.selectedStyle.value()
                    }
                }) {
                    disabled(this@RadioComponent.disabled.values)
                    readOnly(this@RadioComponent.readonly.values)
                    type("radio")
                    name(inputName)
                    checked(this@RadioComponent.store?.data ?: this@RadioComponent.selected.values)
                    value("X")
                    className(this@RadioComponent.severityClassOf(Theme().radio.severity).name)
                    this@RadioComponent.store?.let { changes.states() handledBy it.update }
                    this@RadioComponent.events.value.invoke(this)
                    this@RadioComponent.element.value.invoke(this)
                }

                (::div.styled() {
                    Theme().radio.default()
                    styling()
                }) { }

                this@RadioComponent.label?.let {
                    (::div.styled() {
                        this@RadioComponent.labelStyle.value()
                    }){
                        it(this)
                    }
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
 * [RadioComponent]
 *
 * Example usage
 * ```
 * val cheeseStore = storeOf(false)
 * radio(store = cheeseStore) {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 * }
 *
 * // one can handle the events and preselect the control also manually if needed:
 * radio {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      selected(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 *
 * @see RadioComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [RadioComponent]
 */
fun RenderContext.radio(
    styling: BasicParams.() -> Unit = {},
    store: Store<Boolean>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "radioComponent",
    build: RadioComponent.() -> Unit = {}
): Label = RadioComponent(store).apply(build).render(this, styling, baseClass, id, prefix)

