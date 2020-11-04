import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.modalDemo(): Div {

    // Call this once, if you don't need to *dynamically* change overlay!
    //ModalComponent.setOverlayHandler(DefaultOverlay(OverlayMethod.CoveringEach))

    return box({
        margin { normal }
    }) {

        fun createDeepDialogs(count: Int, size: Style<BasicParams>): SimpleHandler<Unit> {
            if (count < 2) {
                return modal {
                    closeButton()
                    size { size }
                    items {
                        h1 { +"Final Dialog!" }
                    }
                }
            } else {
                return modal({
                    background { color { "snow" } }
                }) {
                    size { size }
                    variant { theme().modal.variants.auto }
                    closeButton()
                    items {
                        h1 { +"Modal Dialog!" }
                        p { +"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." }
                        lineUp({
                            margins { top { normal } }
                            justifyContent { end }
                        }) {
                            spacing { small }
                            items {
                                clickButton {
                                    text("open further")
                                } handledBy createDeepDialogs(count - 1, size)
                            }
                        }
                    }
                }
            }
        }

        h1 { +"Modal Showcase" }

        stackUp {
            items {
                h4 { +"Basic Concepts" }
                lineUp({
                    justifyContent { center }
                }) {
                    items {
                        clickButton {
                            variant { outline }
                            text("blank + closeButton")
                        } handledBy modal {
                            size { theme().modal.sizes.normal }
                            closeButton()
                        }
                        clickButton {
                            variant { outline }
                            text("blank + custom styled closeButton")
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
                            text("content + user defined buttons")
                        } handledBy modal { close -> /* pass in a handler for custom close management */
                            items {
                                h1 { +"My simple dialog" }
                                p { +"You can put arbitrary content and structure within a modal." }
                                p { +"And of course you can define your own close button or other buttons" }
                                lineUp({
                                    justifyContent { end }
                                    margins {
                                        top { normal }
                                    }
                                }) {
                                    spacing { small }
                                    items {
                                        clickButton({
                                            color { info }
                                            background { color { light } }
                                        }) { text("Give me more!") } handledBy modal {
                                            size { theme().modal.sizes.small }
                                            closeButton()
                                            items {
                                                h1 { +"Final message" }
                                                p { +"I am a modal on the next level!" }
                                            }
                                        }
                                        clickButton { text("Abort") } handledBy close // use close handler!
                                    }
                                }
                            }
                        }
                    }
                }

                h4 { +"Overlay" }
                lineUp({
                    justifyContent { center }
                }) {
                    items {
                        clickButton {
                            variant { outline }
                            text("Reset standard overlay")
                        }.map { DefaultOverlay() } handledBy ModalComponent.overlay.update

                        clickButton {
                            variant { outline }
                            text("Activate overlay for each nested level")
                        }.map { DefaultOverlay(OverlayMethod.CoveringEach) } handledBy ModalComponent.overlay.update

                        clickButton {
                            variant { outline }
                            text("Activate styled overlay")
                        }.map {
                            DefaultOverlay(OverlayMethod.CoveringTopMost) {
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
                            }
                        } handledBy ModalComponent.overlay.update
                    }
                }

                h4 { +"Sizes" }
                lineUp({
                    justifyContent { center }
                }) {
                    items {
                        clickButton {
                            text("full")
                        } handledBy createDeepDialogs(30, theme().modal.sizes.full)
                        clickButton {
                            text("large")
                        } handledBy createDeepDialogs(30, theme().modal.sizes.large)
                        clickButton {
                            text("normal")
                        } handledBy createDeepDialogs(30, theme().modal.sizes.normal)
                        clickButton {
                            text("small")
                        } handledBy createDeepDialogs(30, theme().modal.sizes.small)
                    }
                }

                h4 { +"Variants" }
                lineUp({
                    justifyContent { center }
                }) {
                    items {
                        clickButton {
                            text("verticalFilled")
                        } handledBy modal {
                            closeButton()
                            size { theme().modal.sizes.normal }
                            variant { theme().modal.variants.verticalFilled }
                            items {
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


