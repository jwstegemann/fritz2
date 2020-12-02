import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf


@ExperimentalCoroutinesApi
fun RenderContext.tooltipDemo(): Div {
    return contentFrame {
        showcaseHeader("Tooltip")

        paragraph {
            +"The "
            c("tooltip")
            +"is more beautiful than a standard title that appears when a user interacts with an element."
        }

        showcaseSection("Usage")
        paragraph {
            +"In fritz2, tooltips can be added on each styleable element. Its possible to use multiline."
        }
        componentFrame {
            lineUp {
                items {

                    (::span.styled(){
                        tooltip("visit us on www.fritz2.dev") { top }()
                    }) { +"singleline tooltip"}

                    (::span.styled(){
                        tooltip("visit us on", "www.fritz2.dev") { top }()
                    }) { +"multiline tooltip"}


                }
            }
        }
        playground {
            source(
                """
                    (::span.styled(){
                        tooltip("visit us on www.fritz2.dev") { top }()
                    }) { +"singleline tooltip"}

                    (::span.styled(){
                        tooltip("visit us on", "www.fritz2.dev") { top }()
                    }) { +"multiline tooltip"}
                """
            )
        }

        showcaseSection("Placements")
        paragraph {
            +"The"
            c("tooltip")
            +" can display on "
            c("top")
            +","
            c("right")
            +","
            c("bottom")
            +","
            c("left")
            +" side of the styled element."
        }
        componentFrame {
            lineUp {
                items {

                    (::span.styled(){
                        tooltip("placement top") { top }()
                    }) { +"top"}

                    (::span.styled(){
                        tooltip("placement right") { right }()
                    }) { +"right"}

                    (::span.styled(){
                        tooltip("placement bottom") { bottom }()
                    }) { +"bottom"}

                    (::span.styled(){
                        tooltip("placement left") { left }()
                    }) { +"left"}

                }
            }
        }
        playground {
            source(
                """
                      (::span.styled(){
                        tooltip("placement top") { top }()
                    }) { +"top"}

                    (::span.styled(){
                        tooltip("placement right") { right }()
                    }) { +"right"}

                    (::span.styled(){
                        tooltip("placement bottom") { bottom }()
                    }) { +"bottom"}

                    (::span.styled(){
                        tooltip("placement left") { left }()
                    }) { +"left"}
                """
            )
        }

    }
}

