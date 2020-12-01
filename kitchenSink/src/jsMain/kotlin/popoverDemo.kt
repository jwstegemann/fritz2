import dev.fritz2.components.icon
import dev.fritz2.components.lineUp
import dev.fritz2.components.popover
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.popoverDemo(): Div {
    return contentFrame {
        showcaseHeader("Popover")
        paragraph { +"Popover is a non-modal dialog that floats around a trigger. It's used to display contextual information to the user, and should be paired with a clickable trigger element." }
        paragraph {
            +"Click the following icons to open the examples"
        }
        componentFrame {
            lineUp {
                spacing { large }
                items {
                    listOf(
                        Pair(Theme().popover.placement.right, Theme().icons.arrowRight),
                        Pair(Theme().popover.placement.top, Theme().icons.arrowUp),
                        Pair(Theme().popover.placement.bottom, Theme().icons.arrowDown),
                        Pair(Theme().popover.placement.left, Theme().icons.arrowLeft)
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
                        background { color { primary } }
                        border { color { base } }
                        margins { right { small } }
                        color { base }
                    }) {
                        trigger {
                            icon({ size { "2rem" } }) { fromTheme { Theme().icons.circleInformation } }
                        }
                        placement { bottom }
                        hasArrow(false)
                        hasCloseButton(false)
                        header("Customized")
                        content {
                            div {
                                +"background-color"
                                br {}
                                +"border-color"
                                br {}
                                +"hidden close Button"
                                br {}
                                +"and hidden arrow"
                            }
                        }
                        footer("Use the Trigger to close the popover ;-)")
                    }
                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { "2rem" } }) { fromTheme { Theme().icons.eye } }
                        }
                        placement { bottom }
                        hasArrow(false)
                        closeButton {
                            icon({
                                fontSize { tiny }
                            }) { fromTheme { eyeOff } }
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