package dev.fritz2.kitchensink.demos

import dev.fritz2.components.icon
import dev.fritz2.components.lineUp
import dev.fritz2.components.popover
import dev.fritz2.components.pushButton
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.popoverDemo(): Div {

    return contentFrame {
        showcaseHeader("Popover")
        paragraph { +"The Popover is a non-modal dialog that floats around a trigger. It's used to display contextual information to the user, and should be paired with a clickable trigger element." }

        showcaseSection("Usage")
        paragraph {
            +"To define a popover, follow these steps create a trigger. This can be a simple HTMLElement or a fritz2 component, e.g an icon or button. "
            +"Add one or more of the following \"areas\" : "
            c("header")
            +","
            c("content")
            +" and "
            c("footer")
            +". The following example uses a"
            c("Pushbutton ")
            +" as trigger and a"
            c("content")
            +" area. "


        }

        componentFrame {
            lineUp {
                items {
                    popover {
                        trigger {
                            pushButton { text("Trigger") }
                        }
                        content {
                            div { +"My popover content" }
                        }
                    }
                }
            }
        }

        playground {
            source(
                """
                popover {
                        trigger {
                            pushButton { text("trigger") }
                        }
                        content {  
                            div { +"My popover content" }
                      }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Trigger")
        paragraph {
            +"""
            As mentioned before, a trigger can be a simple HTML element or a fritz2 component.
            By default, the trigger is marked by an arrow. You can disable this arrow if you wish.
        """.trimIndent()
        }

        componentFrame {
            lineUp {
                items {
                    popover {
                        trigger {
                            pushButton { text("Trigger without marker") }
                        }
                        content {
                            div { +"My popover content" }
                        }
                        hasArrow(false)
                    }
                }
            }
        }

        playground {
            source(
                """
              popover {
                        trigger {
                            pushButton { text("Trigger without marker") }
                        }
                        content {
                            div { +"My popover content" }
                        }
                        hasArrow(false)
                    }
        """.trimIndent()
            )
        }

        showcaseSection("Areas")
        paragraph {
            +"A popover can consist of one or more of these three areas "
            c("header")
            +", "
            c("content")
            +" and"
            c("footer")
            +". You must define at least one of them."

            componentFrame {
                lineUp {
                    items {
                        popover {
                            trigger {
                                pushButton {
                                    text("Areas")
                                }
                            }
                            header("My header area")
                            content {
                                div {
                                    +"My content area"
                                }
                            }
                            footer("My footer area")
                        }
                    }
                }
            }
        }
        playground {
            source(
                """
                 popover {
                            trigger {
                                pushButton { text("Areas") }
                            }
                            header("My header area")
                            content {
                                div { +"My content area" }
                            }
                            footer("My footer area")
                        }
            """.trimIndent()
            )
        }

        showcaseSection("Placement")
        paragraph {
            +"The placement of your popover can be configured. fritz2 offers four predefined placements: "
            c("right")
            +","
            c("top")
            +","
            c("bottom")
            +","
            c("left")
            +", where "
            c("top")
            +" is the default value."
        }

        componentFrame {
            lineUp {

                spacing { large }
                items {
                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { large } }) { fromTheme { arrowRight } }
                        }
                        placement { right }
                        header("Our simple Popover")
                        content {
                            div {
                                +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua."
                            }
                        }

                        footer("Footer")
                    }

                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { large } }) { fromTheme { arrowUp } }
                        }
                        placement { top }
                        header("Our simple Popover")
                        content {
                            div {
                                +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor..."
                            }
                        }
                        footer("Footer")
                    }

                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { large } }) { fromTheme { arrowDown } }
                        }
                        placement { bottom }
                        header("Our simple Popover")
                        content {
                            div {
                                +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor..."
                            }
                        }
                        footer("Footer")
                    }

                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { large } }) { fromTheme { arrowLeft } }
                        }
                        placement { left }
                        header("Our simple Popover")
                        content {
                            div {
                                +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed ..."
                            }
                        }
                        footer("Footer")
                    }
                }
            }
        }

        playground {
            source(
                """
                    popover({
                        margins { right { small } }
                    }) {
                        trigger {
                            icon({ size { large } }) { fromTheme { arrowLeft } }
                        }
                        placement { left } // set your placement here
                        header("Our simple Popover")
                        content {
                            div {
                                +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed ..."
                            }
                        }
                        footer("Footer")
                    }
                    // other examples omitted for legibility

            """.trimIndent()
            )
        }

        showcaseSection("Close button")


        paragraph {

            +" The popover has a default close button - you can hide it, or use your own custom button."
            br { }
            +"Without a close button, the popover can only be closed by pushing the trigger again."

        }
        componentFrame {
            lineUp {
                items {
                    popover({
                        background { color { primary } }
                        border { color { base } }
                        margins { right { small } }
                        color { base }
                    }) {
                        trigger {
                            icon({ size { large } }) { fromTheme { circleInformation } }
                        }
                        placement { top }
                        hasCloseButton(false)
                        header("Popover without close button")
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
                            icon({ size { large } }) { fromTheme { eye } }
                        }
                        placement { bottom }
                        hasArrow(false)
                        closeButton {
                            icon({ size { tiny } }) { fromTheme { eyeOff } }
                        }
                        header("Custom Close Button")
                        content {
                            div {
                                +"At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. "
                            }
                        }
                    }
                }
            }
        }

        playground {
            source(
                """
            // popover without a close button
            popover {
                trigger {
                    icon({ size { large } }) { fromTheme { circleInformation } }
                }
                placement { top }
                hasCloseButton(false)
                header("Popover without close button")
                content {
                    div { /* your content */ }
                }
                footer("Use the Trigger to close the popover ;-)")
            }
            
            // popover with custom close button
            popover {
                trigger {
                    icon({ size { large } }) { fromTheme { eye } }
                }
                placement { bottom }
                hasArrow(false)
                closeButton {
                    icon({ size { tiny } }) { fromTheme { eyeOff } }
                }
                header("Custom Close Button")
                content {
                    div {
                        +"At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. "
                    }
                }
            }
            """.trimIndent()
            )
        }


    }
}