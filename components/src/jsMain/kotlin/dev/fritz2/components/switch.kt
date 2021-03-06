package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLInputElement

/**
 * This class combines the _configuration_ and the core styling of a switch.
 *
 *
 * This class offers the following _configuration_ features:
 *  - the optional label of a switch (static, dynamic via a [Flow<String>] or customized content of a Div.RenderContext )
 *  - some predefined styling variants (size)
 *  - the style of the switch
 *  - the style checked state
 *  - the style of the dot
 *  - the style of the label
 *  - link an external boolean flow to set the checked state of the box
 *  - link an external boolean flow to set the disabled state of the box
 *  - link events of the switch like ``changes`` with external handlers
 *
 *  This can be done within a functional expression that is the last parameter of the factory function, called
 *  ``build``. It offers an initialized instance of this [SwitchComponent] class as receiver, so every mutating
 *  method can be called for configuring the desired state for rendering the switch.
 *
 * Example usage
 * ```
 * val cheeseStore = storeOf(false)
 * switch(value = cheeseStore) {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 * }
 *
 * // one can handle the events and preselect the control also manually if needed:
 * switch {
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
open class SwitchComponent(protected val value: Store<Boolean>? = null) :
    Component<Label>,
    EventProperties<HTMLInputElement> by EventMixin(),
    ElementProperties<Input> by ElementMixin(),
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    companion object {
        val switchInputStaticCss = staticStyle(
            "switch",
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

    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().switch.sizes.normal }

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

    val labelStyle = ComponentProperty(Theme().switch.label)
    val dotStyle = ComponentProperty<Style<BasicParams>> {}
    var checkedStyle = ComponentProperty(Theme().switch.checked)
    val checked = DynamicComponentProperty(flowOf(false))

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Label {
        val inputId = (id ?: uniqueId()) + "-input"

        return with(context) {
            label({
                this@SwitchComponent.size.value.invoke(Theme().switch.sizes)()
            }, baseClass = baseClass, id = id, prefix = prefix) {
                `for`(inputId)
                input({
                    Theme().switch.input()
                    children("&[checked] + div") {
                        this@SwitchComponent.checkedStyle.value()
                    }
                }, baseClass = switchInputStaticCss, prefix = prefix, id = inputId) {
                    disabled(this@SwitchComponent.disabled.values)
                    readOnly(this@SwitchComponent.readonly.values)
                    type("checkbox")
                    checked(this@SwitchComponent.value?.data ?: this@SwitchComponent.checked.values)
                    this@SwitchComponent.events.value.invoke(this)
                    this@SwitchComponent.value?.let { changes.states() handledBy it.update }
                    className(this@SwitchComponent.severityClassOf(Theme().switch.severity).name)
                    this@SwitchComponent.element.value.invoke(this)
                }

                div({
                    Theme().switch.default()
                    styling()
                }) {
                    div({
                        Theme().switch.dot()
                        this@SwitchComponent.dotStyle.value()
                    }) {}
                }

                this@SwitchComponent.label?.let {
                    div({
                        this@SwitchComponent.labelStyle.value()
                    }) {
                        it(this)
                    }
                }
            }
        }
    }
}

/**
 * This component generates a *single* switch.
 *
 * You can set different kind of properties like the labeltext or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of this box or totally disable it.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [SwitchComponent]
 *
 * Example usage
 * ```
 * // Use a store
 * val cheeseStore = storeOf(false)
 * switch(value=cheeseStore) {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 * }
 *
 * // all state management can also be done manually if needed:
 * switch {
 *      label("with extra cheese") // set the label
 *      size { normal } // choose a predefined size
 *      checked(cheeseStore.data) // link a [Flow<Boolean>] in order to visualize the checked state
 *      events { // open inner context with all DOM-element events
 *          changes.states() handledBy cheeseStore.update // connect the changes event with the state store
 *      }
 * }
 * ```
 *
 * @see SwitchComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param value a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SwitchComponent]
 */
fun RenderContext.switch(
    styling: BasicParams.() -> Unit = {},
    value: Store<Boolean>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "switchComponent",
    build: SwitchComponent.() -> Unit = {}
): Label = SwitchComponent(value).apply(build).render(this, styling, baseClass, id, prefix)
