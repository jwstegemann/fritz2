import dev.fritz2.binding.const
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun HtmlElements.stackDemo(theme: ExtendedTheme): Div {
    return box({
        padding { small }
    }) {
        h1 { +"Stacks" }
        lineUp({
            border {
                width { fat }
                color { dark }
                style { solid }
            }
            padding { small }
            width { "100%" }
            alignItems { center }
        }) {
            items {
                (::h4.styled {
                    fontSize { "1.2em" }
                    color { primary }
                    css("writing-mode: vertical-rl")
                }) { +"stackUp → vertical" }
                stackUp({
                    padding { small }
                    width { "100%" }
                }) {
                    spacing { normal }
                    items {
                        (::h4.styled {
                            fontSize { "1.2em" }
                            color { dark }
                        }) { +"lineUp → horizontal" }
                        lineUp({
                            border {
                                width { fat }
                                color { dark }
                                style { solid }
                            }
                            padding { small }
                            width { "100%" }
                            alignItems { start }
                        }) {
                            spacing { huge }
                            reverse { true }
                            items {
                                p { +"A" }
                                p { +"B" }
                                p { +"C" }
                            }
                        }
                        lineUp({
                            border {
                                width { fat }
                                color { dark }
                                style { solid }
                            }
                            padding { small }
                            width { "100%" }
                            alignItems { center }
                        }) {
                            spacing { huge }
                            items {
                                icon({
                                    size { small }
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
    }
}