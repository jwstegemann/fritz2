package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.binding.const
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.renderAll
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.theme
import dev.fritz2.styling.whenever
import kotlinx.coroutines.flow.*

open class FormControlComponent {
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
                width { thin }
                style { solid }
                color { danger }
            }
        }

        object ControlNames {
            const val inputField = "inputField"
            const val singleSelect = "singleSelect"
            const val multiSelectCheckbox = "multiSelectCheckbox"
        }
    }

    class Control {

        private val overflows: MutableList<String> = mutableListOf()
        var assignee: Pair<String, (HtmlElements.() -> Unit)>? = null

        fun set(controlName: String, component: (HtmlElements.() -> Unit)) {
            if (assignee == null) {
                assignee = Pair(controlName, component)
            } else {
                overflows.add(controlName)
            }
        }

        fun assert() {
            if (overflows.isNotEmpty()) {
                // FIXME: Why throwing stops application?
                console.log(
                    UnsupportedOperationException(
                        message = "Only one control within a formControl is allowed! Accepted control: ${assignee?.first}"
                                + " The following controls are not applied and overflow this form: "
                                + overflows.joinToString(", ")
                                + " Please remove those!"
                    )
                )
            }
        }
    }

    protected val renderStrategies: MutableMap<String, ControlRenderer> = mutableMapOf()
    protected val control = Control()

    var label: String = ""

    fun label(value: () -> String) {
        label = value()
    }

    var invalid: Flow<Boolean> = const(false)

    fun invalid(value: () -> Flow<Boolean>) {
        invalid = value()
    }

    var disabled: Flow<Boolean> = const(false)

    fun disabled(value: () -> Flow<Boolean>) {
        disabled = value()
    }

    var required: Boolean = false

    fun required(value: Boolean) {
        required = value
    }

    var helperText: String? = null

    fun helperText(value: () -> String) {
        helperText = value()
    }

    var errorMessage: Flow<String> = const("")

    fun errorMessage(value: () -> Flow<String>) {
        errorMessage = value()
    }

    open fun inputField(
        styling: BasicParams.() -> Unit = {},
        store: Store<String>? = null,
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = ControlNames.inputField,
        init: Input.() -> Unit
    ) {
        control.set(ControlNames.inputField)
        {
            inputField(styling, store, baseClass, id, prefix) {
                // FIXME: greift zu spät -> Standard Border überschreibt diese Border Infos!
                className = invalidCss.whenever(invalid) { it }
                init()
                // FIXME: Hängt App aktuell auf; nach Patch der Bindings (Speicherleck) anpassen und austesten!
                //disabled.bindAttr("disabled")
            }
        }
    }

    open fun multiSelectCheckbox(
        styling: BasicParams.() -> Unit = {},
        store: Store<String>? = null,
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = ControlNames.inputField,
        init: Input.() -> Unit
    ) {
        control.set(ControlNames.multiSelectCheckbox)
        {
            box({
                background {
                    color { warning }
                }
                border {
                    width { "3px" }
                    style { solid }
                    color { dark }
                }
                textAlign { center }
                color { dark }
            }) {
                p { +"Only placeholder for a checkbox group" }
            }
        }
    }

    fun render(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "formControl",
        renderContext: HtmlElements
    ) {
        control.assignee?.second?.let {
            renderStrategies[control.assignee?.first]?.render(
                styling, baseClass, id, prefix, renderContext, it
            )
        }
        control.assert()
    }

    init {
        renderStrategies[ControlNames.inputField] = SingleControlRenderer(this)
        renderStrategies[ControlNames.multiSelectCheckbox] = ControlGroupRenderer(this)
    }

    fun renderHelperText(renderContext: HtmlElements) {
        renderContext.div {
            helperText?.let {
                (::p.styled {
                    color { theme().colors.dark }
                    fontSize { small }
                    lineHeight { small }
                }) { +it }
            }
        }
    }

    fun renderErrorMessage(renderContext: HtmlElements) {
        renderContext.div {
            invalid.renderAll { it ->
                if (it) {
                    div {
                        errorMessage.renderAll {
                            if (it.isNotEmpty()) {
                                lineUp({
                                    color { theme().colors.danger }
                                    fontSize { small }
                                    lineHeight { small }
                                }) {
                                    spacing { tiny }
                                    items {
                                        icon { fromTheme { arrowUp } }
                                        p { +it }
                                    }
                                }
                            }
                        }.bind()
                    }
                }
            }.bind()
        }
    }

}

interface ControlRenderer {
    fun render(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "formControl",
        renderContext: HtmlElements,
        control: HtmlElements.() -> Unit
    )
}

class SingleControlRenderer(private val component: FormControlComponent) : ControlRenderer {
    override fun render(
        styling: BasicParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String,
        renderContext: HtmlElements,
        control: HtmlElements.() -> Unit
    ) {
        renderContext.stackUp(
            {
                alignItems { start }
                styling()
            },
            baseClass = baseClass,
            id = id,
            prefix = prefix
        ) {
            spacing { theme().space.tiny }
            items {
                label { +component.label }
                control(this)
                component.renderHelperText(this)
                component.renderErrorMessage(this)
            }
        }
    }

}

class ControlGroupRenderer(private val component: FormControlComponent) : ControlRenderer {
    override fun render(
        styling: BasicParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String,
        renderContext: HtmlElements,
        control: HtmlElements.() -> Unit
    ) {
        renderContext.div {
            (::fieldset.styled(baseClass, id, prefix, styling)) {
                legend { +component.label }
                control(this)
                component.renderHelperText(this)
                component.renderErrorMessage(this)
            }
        }
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
    component.render(styling, baseClass, id, prefix, this)
}
