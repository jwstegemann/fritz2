import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun RenderContext.modalDemo(): Div {

    // Call this once if you don't need to dynamically change overlay.
    //ModalComponent.setOverlayHandler(DefaultOverlay(OverlayMethod.CoveringEach))

    return contentFrame {
        fun createDeepDialogs(count: Int, size: Style<BasicParams>): SimpleHandler<Unit> {
            if (count < 2) {
                return modal {
                    closeButton()
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
                    } handledBy modal {
                        size { normal }
                        closeButton()
                    }
                    clickButton {
                        variant { outline }
                        text("Stacking modals")
                    } handledBy modal { close ->
                        size { normal }
                        content {
                            h1 { +"Simple dialog" }
                            paragraph { +"You can put any content or structure into a modal." }
                            clickButton({
                                margins { top { "1.25rem" } }
                                color { dark }
                                background { color { light } }
                            }) { text("Another modal!") } handledBy modal {
                                size { small }
                                content {
                                    h1 { +"One more to go!" }
                                    paragraph { +"You can stack as many as you like." }
                                    clickButton({
                                        margins { top { "1.25rem" } }
                                        color { dark }
                                        background { color { light } }
                                    }) { text("And another modal!") } handledBy modal {
                                        size { small }
                                        content {
                                            h1 { +"Final message" }
                                            paragraph { +"This is the next level modal dialog." }
                                        }
                                    }
                                }
                                closeButton()
                            }
                        }
                        closeButton()
                    }
                }
            }
        }
        playground {
            source("""
                 clickButton {
                    variant { outline }
                    text("Blank dialog with closeButton")
                } handledBy modal {
                    size { normal }
                    closeButton()
                }
                
                clickButton {
                    variant { outline }
                    text("Stacking modals")
                } handledBy modal { close ->
                    size { normal }
                    content {
                        h1 { +"Simple dialog" }
                        paragraph { +"You can put any content or structure into a modal." }
                        clickButton({
                            margins { top { "1.25rem" } }
                            color { dark }
                            background { color { light } }
                        }) { text("Another modal!") } handledBy modal {
                            size { small }
                            content {
                                h1 { +"One more to go!" }
                                paragraph { +"You can stack as many as you like." }
                                clickButton({
                                    margins { top { "1.25rem" } }
                                    color { dark }
                                    background { color { light } }
                                }) { text("And another modal!") } handledBy modal {
                                    size { small }
                                    content {
                                        h1 { +"Final message" }
                                        paragraph { +"This is the next level modal dialog." }
                                    }
                                }
                            }
                            closeButton()
                        }
                    }
                    closeButton()
                }
            """.trimIndent())
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
                    clickButton {
                        variant { outline }
                        text("Blank dialog with closeButton")
                    } handledBy modal { close ->
                        size { normal }
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
                }
            }
        }

        playground {
            source("""
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
                
                clickButton {
                    variant { outline }
                    text("Blank dialog with closeButton")
                } handledBy modal { close ->
                    size { normal }
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
            """.trimIndent())
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

        playground {
            source("""
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
            """.trimIndent())
        }
        showcaseSection("Sizes")
        paragraph { +"There are four different sizes for your modal to choose from. "
        c("small")
            +", "
            c("normal")
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
            source("""
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
            """.trimIndent())
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
                        closeButton()
                        size { normal }
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
            source("""
                 clickButton {
                    text("verticalFilled")
                } handledBy modal {
                    closeButton()
                    size { normal }
                    variant { verticalFilled }
                    content {
                        h1 { +"Dialog takes all vertical space within the viewport" }
                        paragraph { +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." }
                    }
                }
            """.trimIndent())
        }
    }
}