package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import dev.fritz2.styling.params.AlignItemsValues
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.modalDemo(): Div {

    // Call this once if you don't need to dynamically change overlay.
    //ModalComponent.setOverlayHandler(DefaultOverlay(OverlayMethod.CoveringEach))

    return contentFrame {
        fun createDeepDialogs(count: Int, size: Style<BasicParams>): SimpleHandler<Unit> {
            if (count < 2) {
                return modal {
                    size { size }
                    content {
                        h1 { +"Final Dialog" }
                    }
                }
            } else {
                return modal({
                    background { color { "snow" } }
                }) {
                    size { size }
                    variant { auto }
                    closeButton()
                    content {
                        h1 { +"Modal Dialog" }
                        paragraph { +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." }
                        lineUp({
                            margins { top { normal } }
                            justifyContent { end }
                        }) {
                            spacing { small }
                            items {
                                clickButton {
                                    text("open another")
                                } handledBy createDeepDialogs(count - 1, size)
                            }
                        }
                    }
                }
            }
        }

        showcaseHeader("Modal Dialogs")
        paragraph {
            +"Use a modal dialog to support the user with more information about a specific component of your"
            +" application."
        }

        showcaseSection("Usage")
        paragraph { +"Modals can be layered and stacked over one another." }
        componentFrame {
            lineUp({
                alignItems { start }
            }) {
                items {
                    clickButton {
                        variant { outline }
                        text("Blank dialog with closeButton")
                    } handledBy modal { }

                }
            }
        }
        playground {
            source(
                """
                 clickButton {
                    variant { outline }
                    text("Blank dialog with closeButton")
                } handledBy modal { }
            """.trimIndent()
            )
        }

        componentFrame {
            lineUp({
                alignItems { start }
            }) {
                items {
                    clickButton {
                        variant { outline }
                        text("Stacking modals")
                    } handledBy modal {
                        content {
                            h1 { +"Level One!" }
                            paragraph { +"You can put any content or structure into a modal." }
                            clickButton({
                                margins { top { "1.25rem" } }
                                color { dark }
                                background { color { light } }
                            }) { text("open next level") } handledBy modal {
                                size { small }
                                content {
                                    h1 { +"Level Two!" }
                                    paragraph { +"You can stack as many as you like." }
                                    clickButton({
                                        margins { top { "1.25rem" } }
                                        color { dark }
                                        background { color { light } }
                                    }) { text("open final Level") } handledBy modal {
                                        size { small }
                                        content {
                                            h1 { +"Final message" }
                                            paragraph { +"This is the next level modal dialog." }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        playground {
            source(
                """
                clickButton {
                    variant { outline }
                    text("Stacking modals")
                } handledBy modal {
                    content {
                        h1 { +"Level One!" }
                        clickButton {
                            text("open next level")
                        } handledBy modal {
                            // "stacked" modal
                        }
                    }   
                }
            """.trimIndent()
            )
        }

        showcaseSection("CloseButton variants")
        paragraph {
            +"You can customize the close button or even define your own button to do the job."
        }
        componentFrame {
            lineUp({
                alignItems { start }
            }) {
                items {
                    clickButton {
                        variant { outline }
                        text("Blank dialog with custom-styled closeButton")
                    } handledBy modal {
                        closeButton({
                            background { color { danger } }
                            color { base }
                            position {
                                absolute {
                                    left { normal }
                                    top { normal }
                                }
                            }
                            css("transform: rotate(-30deg) translateX(-.5rem)")
                        }) {
                            size { small }
                            text("Close")
                            iconRight()
                        }
                    }
                }
            }
        }

        playground {
            source(
                """
                clickButton {
                    variant { outline }
                    text("<your button title here>")
                } handledBy modal {
                    closeButton({
                        background { color { danger } }
                        color { base }
                        position {
                            absolute {
                                left { normal }
                                top { normal }
                            }
                        }
                        css("transform: rotate(-30deg) translateX(-.5rem)")
                    }) {
                        size { small }
                        text("Close")
                        iconRight()
                    }
                }
            """.trimIndent()
            )
        }
        componentFrame {
            lineUp({
                alignItems { start }
            }) {
                items {
                    clickButton {
                        variant { outline }
                        text("Blank dialog with custom closeButton")
                    } handledBy modal { close ->
                        hasCloseButton(false)
                        content {
                            clickButton({
                                position {
                                    absolute {
                                        right { normal }
                                        bottom { normal }
                                    }
                                }
                            }) { text("Terminate") } handledBy close // use close handler!
                        }
                    }
                }
            }
        }
        playground {
            source(
                """
                clickButton {
                    variant { outline }
                    text("Blank dialog with closeButton")
                } handledBy modal { close -> // inject close handler
                    hasCloseButton(false)
                    content {
                        clickButton ({
                            position {
                                absolute {
                                    right { normal }
                                    bottom { normal }
                                }
                            }
                        }){ text("Terminate") } handledBy close // use close handler!
                    }
                }
            """.trimIndent()
            )
        }

        showcaseSection("Overlay variants")
        paragraph {
            +"Decide what happens with your background when your modal opens. The options are default, create an overlay for each level of dialog opened, or use a styled overlay. Try the below listed variants of overlay and stack a few modals in the example above to see the change."
        }
        componentFrame {
            val overlayVariants = mapOf(
                Pair("Activate default overlay", DefaultOverlay()),
                Pair("Activate overlay for each nested level", DefaultOverlay(OverlayMethod.CoveringEach)),
                Pair("Activate styled overlay", DefaultOverlay(OverlayMethod.CoveringTopMost) {
                    width { "100%" }
                    height { "100%" }
                    position {
                        absolute {
                            horizontal { "0" }
                            vertical { "0" }
                        }
                    }
                    background {
                        image { "https://via.placeholder.com/150x50/?text=BACKGROUND" }
                        repeat { repeat }
                    }
                    css("transform: rotate(-30deg) translateX(-.5rem) scale(200%)")
                    opacity { "0.8" }
                })
            )

            radioGroup(store = ModalComponent.overlay) {
                direction { row }
                label { overlay ->
                    overlayVariants.filter { it.value == overlay }.map {
                        it.key
                    }[0]
                }
                items(overlayVariants.values.toList())
            }
        }
        showcaseSection("Sizes")
        paragraph {
            +"There are four different sizes for your modal to choose from. "
            c("small")
            +", "
            c("normal (default)")
            +", "
            c("large")
            +" and "
            c("full")
            +"."
        }
        componentFrame {
            lineUp({
                alignItems { start }
            }) {
                items {
                    clickButton {
                        text("small")
                    } handledBy createDeepDialogs(30, Theme().modal.sizes.small)
                    clickButton {
                        text("normal")
                    } handledBy createDeepDialogs(30, Theme().modal.sizes.normal)
                    clickButton {
                        text("large")
                    } handledBy createDeepDialogs(30, Theme().modal.sizes.large)
                    clickButton {
                        text("full")
                    } handledBy createDeepDialogs(30, Theme().modal.sizes.full)
                }
            }
        }
        playground {
            source(
                """
                clickButton {
                        text("small")
                    } handledBy modal {
                        size{small} // change size here
                        content {
                            //create your content here
                        }
                    }
            """.trimIndent()
            )
        }
        showcaseSection("Variants")
        paragraph { +"You can also make your modal cover the entire height of your viewport." }
        componentFrame {
            lineUp({
                alignItems { start }
            }) {
                items {
                    clickButton {
                        text("verticalFilled")
                    } handledBy modal {
                        variant { verticalFilled }
                        content {
                            h1 { +"Dialog takes all vertical space within the viewport" }
                            paragraph { +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." }
                        }
                    }
                }
            }
        }
        playground {
            source(
                """
                 clickButton {
                    text("verticalFilled")
                } handledBy modal {
                    variant { verticalFilled }
                    content {
                        h1 { +"<your modal title here>" }
                        // add text here 
                    }
                }
            """.trimIndent()
            )
        }
    }
}