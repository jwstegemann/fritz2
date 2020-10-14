import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.values
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.inputDemo(): Div {

    val user = storeOf("John Doe")

    return div {
        f2Flex {
            direction { column }
            padding { normal }
        }.apply {
            h1 { +"Input Showcase" }

            f2Text().apply() { +"Basic" }
            f2Input().apply() {
                placeholder = const("Placeholder")
            }

            f2Text().apply() { +"Basic + Readonly + Custom Styling" }
            f2Input {
                //background { color { "lightgrey" } }
                focus {
                    border {
                        color { dark }
                    }
                    boxShadow { none }
                }
                type { text }
            }.apply() {
                value = const("Readonly!")
                readOnly = const(true)
            }


            f2Text().apply() { +"Password" }
            f2Input {
                type { password }
            }.apply() {
                placeholder = const("Password")
            }

            f2Text().apply() { +"Basic + Store" }
            f2Input {
                store { user }
            }.apply() {
                placeholder = const("Name")
            }
            f2Text().apply() { +"changes manually applied to store's update" }
            f2Input().apply() {
                placeholder = const("Name")
                changes.values() handledBy user.update
            }
            f2LineUp {
                margins { vertical { tiny } }
                spacing { tiny }
            }.apply {
                f2Text().apply() { +"given Name:" }
                f2Text {
                    background { color { "lightgrey" } }
                    radius { normal }
                    paddings { horizontal { tiny } }
                }.apply() {
                    user.data.bind()
                }
            }

            f2Text().apply() { +"Sizes" }
            f2Input { inputSize { large } }.apply() {
                placeholder = const("large")
            }
            f2Input { inputSize { normal } }.apply() {
                placeholder = const("normal")
            }
            f2Input { inputSize { small } }.apply() {
                placeholder = const("small")
            }

            f2Text().apply() { +"Variants" }
            f2Input {
                variant { outline }
            }.apply() {
                placeholder = const("outline")
            }
            f2Input {
                variant { filled }
            }.apply() {
                placeholder = const("filled")
            }
        }
    }
}

