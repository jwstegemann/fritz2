import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val myItemList = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")

// extend ControlComponent in order to override or extend functions for controls
// and for setting up other renderers!
class MyFormControlComponent : FormControlComponent() {

    // simple convenience function as we cannot provide default parameters for overridden functions!
    fun myMultiSelectCheckbox(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = Companion.ControlNames.checkboxGroup,
        init: CheckboxGroupComponent.() -> Unit
    ): Flow<List<String>> {
        return checkboxGroup(styling, baseClass, id, prefix, init)
    }

    // Define your own renderer
    class MySpecialRenderer(private val component: FormControlComponent) : ControlRenderer {
        override fun render(
            styling: BasicParams.() -> Unit,
            baseClass: StyleClass?,
            id: String?,
            prefix: String,
            renderContext: RenderContext,
            control: RenderContext.() -> Unit
        ) {
            renderContext.lineUp({
                verticalAlign { top }
                alignItems { start }
                styling()
            }, baseClass, id, prefix) {
                items {
                    (::p.styled {
                        alignItems { start }
                        textAlign { right }
                        minHeight { full }
                        height { full }
                    }){ +component.label }

                    stackUp({
                        width { full }
                        alignItems { start }
                        borders {
                            left {
                                color { light }
                                width { fat }
                            }
                        }
                        paddings {
                            left {
                                normal
                            }
                        }
                    }) {
                        spacing { tiny }
                        items {
                            (::fieldset.styled { component.direction() }) {
                                control(this)
                            }
                            component.renderHelperText(this)
                            component.renderErrorMessage(this)
                        }
                    }
                }
            }
        }
    }

    init {
        // Overrule default strategy for ``multiSelectCheckbox``
        // You could also add a new *control* function with a corresponding renderer of course
        renderStrategies[Companion.ControlNames.checkboxGroup] = MySpecialRenderer(this)
    }
}

fun RenderContext.myFormControl(
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
fun RenderContext.formControlDemo(): Div {
    val solution = "fritz2"
    val framework = storeOf("")

    val mySelectedItems = listOf("ffffff", "222222")

    val selectedItemsStore = RootStore(mySelectedItems)

    return stackUp({
        padding { "1rem" }
        alignItems { start }
    }) {
        spacing { large }

        items {
            h1 { +"FormControl Showcase" }
            p {
                +"FormControls take a single form element and take care of styling and validation. You cannot have more than one form element in a FormControl."
            }
            h3 { +"Required Input with a store and dynamic error message" }
            formControl {
                label { "Please input the name of your favorite Kotlin based web framework." }
                required { true }
                helperText { "You shouldn't need a hint." }
                errorMessage {
                    framework.data.map {
                        // if something is wrong, just send a none empty string to errorMessage!
                        // the control will handle the rest for you :-)
                        if (it.isNotEmpty() && it.toLowerCase() != solution) {
                            "'$it' is completely wrong."
                        } else ""
                    }
                }
                //just use the appropriate *single element* control with its specific API!
                inputField(store = framework) {
                    placeholder("$solution for example")
                }
                // throws an exception -> only one (and the first) control is allowed!
                inputField {
                    placeholder("This control throws an exception because a form control may only contain one control.")
                }
            }

            val loveString = "I love fritz2 with all my heart."
            val hateString = "I hate your guts, fritz2!"
            val loveStore = object : RootStore<Boolean>(true) {
                val changedMyMind = handleAndEmit<Boolean, String> { _, checked ->
                    emit(if (checked) loveString else hateString)
                    checked
                }
            }
            val textStore = RootStore<String>(loveString)
            loveStore.changedMyMind handledBy textStore.update


            h3 { +"Form with a single checkbox, custom color, form control label and helpertext" }
            formControl {
                label { "Label us interested: How do you feel about fritz2? We would really love to hear your opinion. " }
                helperText { "So good to have options." }
                checkbox(
                    {},
                    id = "check1"
                ) {
                    text(textStore.data)
                    size { large }
                    borderColor { theme().colors.secondary }
                    checkedBackgroundColor { theme().colors.warning }
                    checked { loveStore.data }
                    events {
                        changes.states() handledBy loveStore.changedMyMind
                    }
                }
            }

            h3 { +"Form with a small checkbox group, no label, no helpertext, formlayout horizontal" }
            formControl {
                direction { row } // must be applied to formcontrol instead of checkboxGroup
                checkboxGroup(
                    {},
                    id = "checkGroup1"
                ) {
                    items { myItemList }
                    size { small }
                    initialSelection { mySelectedItems }
                } handledBy selectedItemsStore.update
            }
            (::div.styled {
                background {
                    color { theme().colors.light }
                }
                paddings {
                    left { "0.5rem" }
                    right { "0.5rem" }
                }
                radius { "5%" }
            }) {
                h4 { +"Selected:" }
                ul {
                    selectedItemsStore.data.renderEach { selectedItem ->
                        li { +selectedItem }
                    }
                }
            }
            // use your own formControl! Pay attention to the derived component receiver.
            h3 { +"Custom FormControl" }
            p {
                +"This control has overridden the control function to implement a special control. It was combined with a hand made renderer for the surrounding custom structure."
            }
            myFormControl {
                label { "Label next to the control for a change" }
                helperText { "Helper text below control" }
                direction { row }
                myMultiSelectCheckbox {
                    items { myItemList }
                    initialSelection { mySelectedItems }
                }
            }
        }
    }
}
