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

    // Call this once, if you don't need to *dynamically* change overlay!
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
                        p { +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." }
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

        stackUp({
            alignItems { start }
        }) {
            items {
                showcaseSection("Basic Dialog options")
                // TODO: Text is too big for buttons
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
                                text("Blank dialog with custom-styled closeButton")
                            } handledBy modal {
                                closeButton({
                                    background { color { danger } }
                                    color { "snow" }
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
                                text("Content and user defined buttons")
                            } handledBy modal { close -> /* pass in a handler for custom close management */
                                hasCloseButton(false)
                                content {
                                    h1 { +"Simple dialog" }
                                    p { +"You can put any content or structure into a modal." }
                                    p { +"And of course you can define your own close button or other buttons." }
                                    lineUp({
                                        alignItems { start }
                                        margins {
                                            top { normal }
                                        }
                                    }) {
                                        spacing { small }
                                        items {
                                            clickButton({
                                                color { dark }
                                                background { color { light } }
                                            }) { text("Give me more!") } handledBy modal {
                                                size { small }
                                                content {
                                                    h1 { +"Final message" }
                                                    p { +"This is the next level modal dialog." }
                                                }
                                            }
                                            clickButton { text("Abort") } handledBy close // use close handler!
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                showcaseSection("Choosing an overlay")
                paragraph {
                    +"Decide what happens with your background when your modal opens. The options are default, create an overlay for each level of dialog opened, or use a styled overlay."
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
                componentFrame {
                    lineUp({
                        alignItems { start }
                    }) {
                        items {
                            clickButton {
                                text("full")
                            } handledBy createDeepDialogs(30, Theme().modal.sizes.full)
                            clickButton {
                                text("large")
                            } handledBy createDeepDialogs(30, Theme().modal.sizes.large)
                            clickButton {
                                text("normal")
                            } handledBy createDeepDialogs(30, Theme().modal.sizes.normal)
                            clickButton {
                                text("small")
                            } handledBy createDeepDialogs(30, Theme().modal.sizes.small)
                        }
                    }
                }

                showcaseSection("Variants")
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
                                    p { +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}