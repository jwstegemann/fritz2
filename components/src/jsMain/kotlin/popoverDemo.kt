import dev.fritz2.binding.const
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.AlignItemsValues
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.popoverDemo(): Div {
    return stackUp({
        alignItems { start }
        padding { "1rem" }
    }) {
        spacing { large }
        items {
            h1 { +"Popover Showcase" }
            p { +"Popover is a non-modal dialog that floats around a trigger. It's used to display contextual information to the user, and should be paired with a clickable trigger element." }
            p {
                +"Click the following icons to open the examples"
            }
            lineUp({
                paddings { top { "2rem" } }
            }) {
                spacing { large }
                items {
                    listOf(
                        Pair(theme().popover.placement.right, theme().icons.arrowForward),
                        Pair(theme().popover.placement.top, theme().icons.arrowUp),
                        Pair(theme().popover.placement.bottom, theme().icons.arrowDown),
                        Pair(theme().popover.placement.left, theme().icons.arrowBack)
                    ).forEach { (placement, icon) ->
                        popover({
                            margins { right { small } }
                        }) {
                            trigger {
                                icon({ size { "2.5rem" } }) { fromTheme { icon } }
                            }
                            placement { placement }
                            header(const("Our simple Popover"))
                            content {
                                div {
                                    text("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. ")
                                }
                            }
                            footer("Footercontent")
                        }
                    }

                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { "2rem" } }) { fromTheme { theme().icons.infoOutline } }
                        }
                        placement { bottom }
                        hasArrow(false)
                        hasCloseButton(false)
                        header(const("Without CloseButton..."))
                        content {
                            div {
                                text("At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                            }
                        }
                        footer("... and arrow")
                    }
                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { "2rem" } }) { fromTheme { theme().icons.view } }
                        }
                        placement { bottom }
                        hasArrow(false)
                        closeButton {
                            icon({
                                fontSize { tiny }
                            }) { fromTheme { viewOff } }
                        }
                        header(const("Custom Close Button..."))
                        content {
                            div {
                                text("At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ")
                            }
                        }
                    }
                }
            }
        }
    }
}
