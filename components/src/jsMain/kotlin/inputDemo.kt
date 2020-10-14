import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.components.f2Flex
import dev.fritz2.components.Input
import dev.fritz2.components.f2LineUp
import dev.fritz2.components.Text
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

            Text { +"Basic" }
            Input {
                placeholder = const("Placeholder")
            }

            Text { +"Basic + Readonly + Custom Styling" }
            Input(
                {
                    //background { color { "lightgrey" } }
                    focus {
                        border {
                            color { dark }
                        }
                        boxShadow { none }
                    }
                },
                type = { text }
            ) {
                value = const("Readonly!")
                readOnly = const(true)
            }


            Text { +"Password" }
            Input(
                type = { password }
            ) {
                placeholder = const("Password")
            }

            Text { +"Basic + Store" }
            Input(store = user) {
                placeholder = const("Name")
            }
            Text { +"changes manually applied to store's update" }
            Input {
                placeholder = const("Name")
                changes.values() handledBy user.update
            }
            f2LineUp {
                margins { vertical { tiny } }
                spacing { tiny }
            }.apply {
                Text {
                    +"given Name:"
                }
                Text({
                    background { color { "lightgrey" } }
                    radius { normal }
                    paddings { horizontal { tiny } }
                }) {
                    user.data.bind()
                }
            }

            Text { +"Sizes" }
            Input(size = { large }) {
                placeholder = const("large")
            }
            Input(size = { normal }) {
                placeholder = const("normal")
            }
            Input(size = { small }) {
                placeholder = const("small")
            }

            Text { +"Variants" }
            Input(variant = { outline }) {
                placeholder = const("outline")
            }
            Input(variant = { filled }) {
                placeholder = const("filled")
            }
        }
    }
}