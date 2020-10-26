import dev.fritz2.binding.*
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

// extend ControlComponent in order to override or extend functions for controls
// and for setting up other renderers!
class MyFormControlComponent : FormControlComponent() {

    // simply convenience function as we cannot provide default parameters for overridden functions!
    fun myMultiSelectCheckbox(
        styling: BasicParams.() -> Unit = {},
        store: Store<String>? = null,
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = Companion.ControlNames.inputField,
        init: Input.() -> Unit
    ) {
        multiSelectCheckbox(styling, store, baseClass, id, prefix, init)
    }

    // override default implementation to customize control rendering
    override fun multiSelectCheckbox(
        styling: BasicParams.() -> Unit,
        store: Store<String>?,
        baseClass: StyleClass?,
        id: String?,
        prefix: String,
        init: Input.() -> Unit
    ) {
        control.set(Companion.ControlNames.multiSelectCheckbox)
        {
            box({
                background {
                    color { light }
                }
                border {
                    width { "2px" }
                    style { dashed }
                    color { dark }
                }
                textAlign { center }
                color { dark }
                radius { small }
            }) {
                p { +"My substitute for the default checkbox component!" }
            }
        }
    }

    // Define your own renderer
    class MySpecialRenderer(private val component: FormControlComponent) : ControlRenderer {
        override fun render(
            styling: BasicParams.() -> Unit,
            baseClass: StyleClass?,
            id: String?,
            prefix: String,
            renderContext: HtmlElements,
            control: HtmlElements.() -> Unit
        ) {
            renderContext.div {
                lineUp({
                    alignItems { start }
                    border {
                        width { "1px" }
                        style { solid }
                        color { light }
                    }
                    styling()
                }, baseClass, id, prefix) {
                    spacing { tiny }
                    items {
                        p { +component.label }
                        stackUp {
                            spacing { tiny }
                            items {
                                control(this)
                                component.renderHelperText(this)
                                component.renderErrorMessage(this)
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        // Overrule default strategy for ``multiSelectCheckbox``
        // You could also add a new *control* function with a corresponding renderer of course
        renderStrategies[Companion.ControlNames.multiSelectCheckbox] = MySpecialRenderer(this)
    }
}

fun HtmlElements.myFormControl(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "formControl",
    build: MyFormControlComponent.() -> Unit = {}
) {
    val component = MyFormControlComponent().apply(build)
    component.render(styling, baseClass, id, prefix, this)
}

@ExperimentalCoroutinesApi
fun HtmlElements.formControlDemo(): Div {
    val solution = "fritz2"
    val framework = storeOf("")

    return div {
        stackUp({
            padding { "1rem" }
            alignItems { start }
        }) {
            spacing { large }
            items {
                h1 { +"FormControl Showcase" }
                h4{ +"Form with input control, required flag, passed store and dynamic error message"}
                formControl {
                    label { "Please input the name of your favorite Kotlin based web framework" }
                    required { true }
                    helperText { "Hint: You are probably gonna to use it, as you are here ;-)" }
                    errorMessage {
                        framework.data.map {
                            // if something is wrong, just send a none empty string to errorMessage!
                            // the control will handle the rest for you :-)
                            if (it.isNotEmpty() && it.toLowerCase() != solution) {
                                "'$it' is the wrong answer! Even Fritz could do it twice as good as you :-P"
                            } else ""
                        }
                    }
                    // just use the appropriate *single element* control with its specific API!
                    inputField(store = framework) {
                        placeholder = const("$solution for example")
                    }
                    // throws an exception -> only one (and the first) control is allowed!
                    inputField {
                    }
                }

                h4{ +"Form with checkbox control"}
                formControl {
                    label { "Please choose your favorite Kotlin based web framework" }
                    helperText { "Choose wisely!" }
                    multiSelectCheckbox { }
                }

                // use your own formControl! Pay attention to the derived component receiver.
                h4{ +"Custom FormControl"}
                ul {
                    li { +"overridden control function to implement a special control element" }
                    li { +"combined with a hand made renderer for the surrounding, custom structure"}
                }
                myFormControl {
                    label { "The label is placed aside of the control. Just to be different..." }
                    helperText { "Help is beneath control" }
                    myMultiSelectCheckbox { }
                }
            }
        }
    }
}
