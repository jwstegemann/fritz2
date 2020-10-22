package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.binding.const
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.renderAll
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.theme
import dev.fritz2.styling.whenever
import kotlinx.coroutines.flow.*

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


class FormControlComponent {
    companion object {
        val staticCss = staticStyle(
            "formControl",
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

        val invalidCss = staticStyle("inputField-invalid") {
            boxShadow {
                theme().shadows.danger
            }
            border {
                color { danger }
            }
        }
    }

    var label: String = ""

    fun label(value: () -> String) {
        label = value()
    }

    // TODO: Expose as fun + FLow
    var invalid: Flow<Boolean> = const(false)

    var disabled: Boolean = false
    var required: Boolean = false

    fun invalid(value: () -> Flow<Boolean>) {
        invalid = value()
    }

    var helperText: String? = null

    fun helperText(value: () -> String) {
        helperText = value()
    }

    var errorMessage: String? = null

    fun errorMessage(value: () -> String) {
        errorMessage = value()
    }

    // In Struktur (Map!?) packen -> einfacher prüf- und handlebar!
    var inputField: (HtmlElements.() -> Unit)? = null
    var checkbox: (HtmlElements.() -> Unit)? = null

    // in interne Klasse kapseln!
    var controlOverflows: Map<String, (HtmlElements.() -> Unit)?> = mapOf()
    var control: Pair<(HtmlElements.() -> Unit), ((renderContext: HtmlElements, control: HtmlElements.() -> Unit) -> Unit)>? =
        null

    fun inputField(
        styling: BasicParams.() -> Unit = {},
        store: Store<String>? = null,
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "inputField",
        init: Input.() -> Unit
    ) {
        // Besser in Struktur setzen
        // ggf. über interne private Funktion, die zudem eine Kollisionsstruktur füllt!
        // ``setControl()``
        inputField = {
            inputField({
                // TODO: ggf. auch intern auslagern, sofern das wiederverwendbar für anderen controls ist!
                styling()
            }, store, baseClass, id, prefix) {
                init()
                className = invalidCss.whenever(invalid) { it }
            }
        }
    }

    // TODO: Wrapper-Funktionen für alle anderen Controls!

    // Als Strategien auslagern?
    // -> neue Strategie kann hinzugefügt werden!
    fun renderSingleControl(renderContext: HtmlElements, control: HtmlElements.() -> Unit) {
        renderContext.stackUp({
            alignItems { start }
        }) {
            spacing { theme().space.tiny }
            items {
                label { +label }
                control(this)
                helperText?.let {
                    (::p.styled {
                        color { theme().colors.dark }
                        fontSize { small }
                        lineHeight { small }
                    }) { +it }
                }
                div {
                    invalid.renderAll {
                        if (it) {
                            errorMessage?.let {
                                lineUp({
                                    color { theme().colors.danger }
                                    fontSize { small }
                                    lineHeight { small }
                                    spacing { "0" }
                                }) {
                                    items {
                                        icon { fromTheme { arrowUp } }
                                        p { +it }
                                    }
                                }
                            }
                        }
                    }.bind()
                }
            }
        }
    }

    fun renderControlGroup(renderContext: HtmlElements, control: HtmlElements.() -> Unit) {
        renderContext.p { +"Ganz andere Struktur eines formControls!" }
    }
}


fun HtmlElements.formControl(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "formControl",
    build: FormControlComponent.() -> Unit = {}
) {
    val component = FormControlComponent().apply(build)
    // Aus Struktur heraus aufrufen!
    // -> Mehrfache Belegung kann einfach erkannt werden!
    if (component.inputField != null) {
        component.renderSingleControl(this, component.inputField!!)
    } else if (component.checkbox != null) {
        component.renderControlGroup(this, component.checkbox!!)
    }
    // bei weiteren != null -> Exception werfen! (Entwickler können erkennen, dass etwas falsch ist)

}

/*
fun HtmlElements.myOwnFormControl(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "formControl",
    build: MyExtendedFormControlComponent.() -> Unit = {}
) {
}
 */