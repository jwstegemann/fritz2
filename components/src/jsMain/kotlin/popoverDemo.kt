import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.theme.theme
import dev.fritz2.tracking.tracker
import kotlinx.browser.window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
fun HtmlElements.popoverDemo(): Div {
    return div {
        flexBox({
            direction { column }
            padding { normal }
        }) {
            h1 { +"Popover Showcase" }
            p { +"Popover is a non-modal dialog that floats around a trigger. It's used to display contextual information to the user, and should be paired with a clickable trigger element."}
            br{}
            span {
                +"Click the following icons to open the examples"
            }
            br{}
            span{
                popover({
                    margins { right { small } }
                }) {
                    trigger {
                        icon { fromTheme { theme().icons.arrowForward } }
                    }
                    placement{right}
                    header(const("Our simple Popover"))
                    content {
                        div{
                            text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. ")
                        }
                    }
                    footer("Footercontent")
                }
                popover({
                    margins { right { small } }
                })  {
                    trigger {
                        icon { fromTheme { theme().icons.arrowUp } }
                    }
                    placement{top}
                    header(const("Our simple Popover"))
                    content {
                        div{
                            text("At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                        }
                    }
                    footer("Footercontent")
                }
                popover({
                    margins { right { small } }
                })  {
                    trigger {
                        icon { fromTheme { theme().icons.arrowDown } }
                    }
                    placement{bottom}
                    header(const("Our simple Popover"))
                    content {
                        div{
                            text("Lorem at vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                        }
                    }
                    footer("Footercontent")
                }
                popover({
                    margins { right { small } }
                })  {
                    trigger {
                        icon { fromTheme { theme().icons.arrowBack } }
                    }
                    placement{left}
                    header(const("Our simple Popover"))
                    content {
                        div{
                            text("At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                        }
                    }
                    footer("Footercontent")
                }
                popover({
                    margins { right { small } }
                })  {
                    trigger {
                        icon { fromTheme { theme().icons.infoOutline } }
                    }
                    placement{bottom}
                    hasArrow(false)
                    hasCloseButton(false)
                    header(const("Without CloseButton..."))
                    content {
                        div{
                            text("At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                        }
                    }
                    footer("... and arrow")
                }
                popover({
                    margins { right { small } }
                })  {
                    trigger {
                        icon { fromTheme { theme().icons.view } }
                    }
                    placement{bottom}
                    hasArrow(false)
                    closeButton(  build =
                        {
                            icon({
                                fontSize { tiny }
                            }) { fromTheme { viewOff } }
                        }
                    )
                    header(const("Custom Close Button..."))
                    content {
                        div{
                            text("At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                        }
                    }
                }
            }
        }
    }
}
