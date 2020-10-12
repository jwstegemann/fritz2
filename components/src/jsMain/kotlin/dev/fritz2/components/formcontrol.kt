package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.theme

/**
 * FormControl: https://chakra-ui.com/formcontrol
 *
 * - role="group" in erstes Element
 * - isDisabled, isInvalid, isRequired werden nach unten durchgereicht
 *   -> muss auf Komponentenebene verarbeitet werden
 *   -> muss also irgendwie in den Kontext hinein gereicht werden, damit die
 *      Sub-Elemente das ggf. abgreifen können!
 *
 * 1.) bei "Text"-Input:
 * <div>
 *     <label>
 *     <input />
 *     <p>Help</p> // <div>Error</div>
 * </div>
 *
 * 2.) bei Radios / Checkbox
 * <fieldset>
 *     <legend>
 *     <div role="radiogroup">
 *         <div einzelnes Radio-Element>
 *         ...
 *     </div>
 *
 * </fieldset>
 *
 * 3.) bei Select
 *
 * <div>
 *     <label>
 *     <div>
 *         <select>
 *         </select>
 *     </div>
 * </div>
 * -> Man muss beim Rendern wissen, was im Kontext steht!!!
 */
class FormControlComponentContext(prefix: String) : BasicComponentContext(prefix) {
    companion object Foundation {
        val cssClass = staticStyle(
            "FormControl",
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
            """
        )

        // Ich muss wissen, *wie* die Struktur gebaut werden soll.
        // Das muss aber aus Begriffen / Typen abgeleitet werden, die der Anwender kennt!
        // Leider überschneiden sich dabei die semantischen Typen mit der notwendigen
        // Stukturierung!
        object ControlType {
            const val input = "input"
            const val switch = "switch"
            const val singleSelect = "singleSelect" // Problem CheckBox+Radio <-> Select
            const val multiSelect = "multiSelect"  // Problem CheckBox+Radio <-> Select
        }

        val controlRenderer: ControlRenderer = DelegatingControlRenderer()
    }

    var label: String = ""

    fun label(value: () -> String) {
        label = value()
    }

    // TODO: How to expose boolean values?
    var invalid: Boolean = false
    var disabled: Boolean = false
    var required: Boolean = false

    var helperText: String? = null

    fun helperText(value: () -> String) {
        helperText = value()
    }

    var errorMessage: String? = null

    fun errorMessage(value: () -> String) {
        errorMessage = value()
    }

    var type: String = ControlType.input

    fun type(value: ControlType.() -> String) {
        type = ControlType.value()
    }

    var control: HtmlElements.() -> Unit? = { null }

    fun control(value: HtmlElements.() -> Unit) {
        control = value
    }
}

interface ControlRenderer {
    fun render(context: FormControlComponentContext, renderContext: HtmlElements)
}

class DelegatingControlRenderer() : ControlRenderer {
    private val labelBasedRenderer: ControlRenderer = LabelBasedControlRenderer()
    private val fieldsetBasedRenderer: ControlRenderer = FieldsetBasedControlRenderer()

    override fun render(context: FormControlComponentContext, renderContext: HtmlElements) {
        when (context.type) {
            FormControlComponentContext.Foundation.ControlType.input -> labelBasedRenderer.render(
                context,
                renderContext
            )
            FormControlComponentContext.Foundation.ControlType.singleSelect -> fieldsetBasedRenderer.render(
                context,
                renderContext
            )
            else -> LabelBasedControlRenderer().render(context, renderContext)
        }
    }
}

class LabelBasedControlRenderer : ControlRenderer {
    override fun render(context: FormControlComponentContext, renderContext: HtmlElements) {
        renderContext.apply {
            f2StackUp {
                alignItems { start }
                spacing { theme().space.tiny }
            }.apply() {
                label { +context.label }
                context.control(this)
                context.helperText?.let {
                    f2Text {
                        color { theme().colors.dark }
                        fontSize { small }
                        lineHeight { small }
                    }.apply() { +it }
                }
                if (context.invalid) {
                    context.errorMessage?.let {
                        f2LineUp {
                            color { theme().colors.danger }
                            fontSize { small }
                            lineHeight { small }
                            spacing { "0" }
                        }.apply() {
                            Icon(theme().icons.arrowUp)
                            f2Text().apply() { +it }
                        }
                    }
                }
            }
        }
    }
}

class FieldsetBasedControlRenderer : ControlRenderer {
    override fun render(context: FormControlComponentContext, renderContext: HtmlElements) {
        renderContext.apply {
            f2Text().apply() { +"Placeholder for SingleSelect!" }
        }
    }
}

fun HtmlElements.f2FormControl(build: Context<FormControlComponentContext> = {}): Component<Div> {
    val context = FormControlComponentContext("f2FormControl")
        .apply(build)

    return Component { init ->
        div(context.cssClass, content = {
            attr("role", "group")
            init()
            FormControlComponentContext.controlRenderer.render(context, this)
        })
    }
}
