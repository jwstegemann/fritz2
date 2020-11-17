import dev.fritz2.components.icon
import dev.fritz2.components.lineUp
import dev.fritz2.components.popover
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.popoverDemo(): Div {
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
                            header("Our simple Popover")
                            content {
                                div {
                                    +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua."
                                }
                            }
                            footer("Footercontent")
                        }
                    }

                    popover({
                        background { color { warning } }
                        border { color { dark }}
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { "2rem" } }) { fromTheme { theme().icons.infoOutline } }
                        }
                        placement { bottom }
                        hasArrow(false)
                        hasCloseButton(false)
                        header("Customized")
                        content {
                            div {
                                +"background-color"
                                br{}
                                +"border-color"
                                br{}
                                +"hidden close Button"
                                br{}
                                +"and hidden arrow"
                            }
                        }
                        footer("Use the Trigger to close the popover ;-)")
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
                        header("Custom Close Button...")
                        content {
                            div {
                                +"At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. "
                            }
                        }
                    }
                }
            }
        }
    }
}
