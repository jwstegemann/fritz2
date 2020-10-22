import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.binding.watch
import dev.fritz2.components.flexBox
import dev.fritz2.components.formControl
import dev.fritz2.components.inputField
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun HtmlElements.formControlDemo(): Div {
    val solution = "fritz2"
    val framework = storeOf("fritz2")

    return div {
        flexBox({
            padding { "1rem" }
            direction { column }
        }) {
            h4 { +"FormControl" }
            formControl {
                label { "Input the right thing (powered by inputField-component" }
                helperText { "Please input the name of your favorite Kotlin based web framework!!!" }
                //invalid(framework.data.map { it == solution })
                invalid { framework.data.map { it != solution } }
                errorMessage { "Serious error!!!" }
                // just use the appropriate *single element* control with its specific API!
                inputField(store = framework) {
                    placeholder = const("fritz2")
                    //invalid = const(true) // const(this@formControl.invalid)
                }
            }
            h4 { +"Debug Store:" }
            p { framework.data.bind() }
        }
    }
}
