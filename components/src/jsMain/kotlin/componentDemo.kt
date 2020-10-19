import dev.fritz2.binding.const
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun HtmlElements.componentDemo(theme: ExtendedTheme): Div {
    return div {
        box {
            flexBox {
                gridBox {
                    +"Bin da"
                }
            }
        }

        h1 { +"Stacks" }
        stackUp({
            border {
                width { thin }
                color { dark }
                style { solid }
            }
        }) {
            spacing { normal }
            children {
                lineUp({
                    border {
                        width { thin }
                        color { secondary }
                        style { solid }
                    }
                    width { "100%" }
                    alignItems { start }
                }) {
                    spacing { huge }
                    reverse { true }
                    children {
                        p { +"A" }
                        p { +"B" }
                        p { +"C" }
                    }
                }
                lineUp({
                    border {
                        width { thin }
                        color { secondary }
                        style { solid }
                    }
                    width { "100%" }
                    alignItems { center }
                }) {
                    spacing { huge }
                    children {
                        icon({
                            size { large }
                            color { "purple" }
                        }) { fromTheme { arrowUp } }
                        (::img.styled {
                            size { tiny }
                        }) {
                            src = const("https://bit.ly/2jYM25F")
                        }
                        p { +"D" }
                        p { +"E" }
                        p { +"F" }
                    }
                }
            }
        }
    }
}