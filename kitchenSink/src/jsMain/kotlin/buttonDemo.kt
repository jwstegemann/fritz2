import dev.fritz2.binding.RootStore
import dev.fritz2.binding.invoke
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tracking.tracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
fun RenderContext.buttonDemo(): Div {

    val modal = modal({
        minHeight { "0" }
    }) {
        size { small }
        closeButton()
        content {
            lineUp {
                items {
                    icon({ color { "darkgreen" } }) { fromTheme { circleCheck } }
                    p { +"Your data has been saved successfully." }
                }
            }
        }
    }

    val buttonStore = object : RootStore<Int>(0) {
        val loading = tracker()

        val showMsg = handle { model ->
            loading.track("running...") {
                delay(3000)
                modal()
            }
            model
        }
    }
    buttonStore.watch()

    // TODO: Check why ``handledBy`` does not work without enclosing ``div``! (only with ``stackUp``)
    return stackUp({
            maxWidth { "48rem" }
            margins { top { huge } }
            alignItems { start }
            padding { "1rem" }
        }) {
            items {
                showcaseHeader("Buttons Showcase")

                stackUp({
                    maxWidth { "48rem" }
                    alignItems { start }
                }) {
                    items {
                        p {
                            +"""
                            Using a Button you can trigger an action that can be handled by your Store or by another
                            component, i.e. launching a modal dialog. 
                            """.trimIndent()
                        }

                        showcaseSection("Usage")
                        p {
                            +"Define your button by adding text and / or an icon to its content. A"
                            c("pushButton")
                            +"gives you full controll over the underlying HTML-button. The"
                            c("clickButton")
                            +"exposes the Flow of click-events, so you can connect it conveniently to a Handler or another component."
                        }

                        componentFrame {
                            lineUp {
                                items {
                                    clickButton { text("click me") } handledBy modal

                                    pushButton {
                                        icon { fromTheme { arrowLeft } }
                                        text("previous")
                                    }

                                    pushButton {
                                        icon { fromTheme { arrowRight } }
                                        iconRight()
                                        text("next")
                                    }

                                    pushButton { icon { fromTheme { check } } }
                                }
                            }
                        }
                        playground {
                            source(
                                """
                                    clickButton { text("click me") } handledBy modal

                                    pushButton {
                                        icon { fromTheme { arrowLeft } }
                                        text("previous")
                                    }

                                    pushButton {
                                        icon { fromTheme { arrowRight } }
                                        iconRight()
                                        text("next")
                                    }

                                    pushButton { icon { fromTheme { check } } }
                                """
                            )
                        }

                        showcaseSection("Variants")
                        p {
                            +"""
                                fritz2 offers three different flavours of buttons for the various use cases.
                            """.trimIndent()

                        }

                        h3 { +"Choose from variants like outline, ghost and more. Icons can be on either side of the text." }
                        componentFrame {
                            lineUp {
                                items {
                                    clickButton {
                                        text("solid")
                                        variant { solid } // default
                                    }
                                    clickButton {
                                        text("outline")
                                        variant { outline }
                                    }
                                    clickButton {
                                        text("ghost")
                                        variant { ghost }
                                    }
                                    clickButton {
                                        text("link")
                                        variant { link }
                                    }
                                }
                            }
                        }
                        playground {
                            source(
                                """
                                    clickButton { 
                                        text("solid")
                                        variant { solid }
                                    }
                                    
                                    clickButton {
                                        text("outline")
                                        variant { outline } // default
                                    }
                                    
                                    clickButton {
                                        text("ghost")
                                        variant { ghost } 
                                    }
                                    
                                    clickButton {
                                        text("link")
                                        variant { link } 
                                    }
                                """
                            )
                        }

                        showcaseSection("Sizes")
                        p {
                            +"""
                                Buttons are available in three predefined sizes.
                            """.trimIndent()
                        }
                        componentFrame {
                            lineUp {
                                items {
                                    clickButton {
                                        text("small")
                                        size { small }
                                    }
                                    clickButton {
                                        text("normal")
                                        size { normal } // default
                                    }
                                    clickButton {
                                        text("large")
                                        size { large }
                                    }
                                }
                            }
                        }
                        playground {
                            source(
                                """
                                   clickButton {
                                        text("small")
                                        size { small }
                                    }
                                    clickButton {
                                        text("normal")
                                        size { normal } // default
                                    }
                                    clickButton {
                                        text("large")
                                        size { large }
                                    }
                                """
                            )
                        }

                        showcaseSection("Loading State")
                        p {
                            +"""
                                Connect the Button to a Tracker to show it's loading state. 
                                You can specify a different text that is shown while loading.
                            """.trimIndent()
                        }
                        componentFrame {
                            lineUp {
                                items {
                                    clickButton {
                                        text("play")
                                        loading(buttonStore.loading)
                                    } handledBy buttonStore.showMsg

                                    clickButton {
                                        text("play")
                                        loading(buttonStore.loading)
                                        loadingText("playing...")
                                        variant { outline }
                                    } handledBy buttonStore.showMsg

                                    clickButton {
                                        icon { fromTheme { play } }
                                        text("play")
                                        loading(buttonStore.loading)
                                    } handledBy buttonStore.showMsg

                                    clickButton {
                                        icon { fromTheme { play } }
                                        variant { ghost }
                                        loading(buttonStore.loading)
                                    } handledBy buttonStore.showMsg
                                }
                            }
                        }
                        playground {
                            source(
                                """
                                    val buttonStore = object : RootStore<Int>(0) {
                                        val loading = tracker()
                                
                                        val showMsg = handle { model ->
                                            loading.track("running...") {
                                                delay(3000)
                                                modal()
                                            }
                                            model
                                        }
                                    }

                                    clickButton { text("play") } handledBy buttonStore.showMsg

                                    clickButton {
                                        icon { fromTheme { play } }
                                        text("play")
                                        loadingText("playing")
                                    } handledBy buttonStore.showMsg

                                    clickButton {
                                        icon { fromTheme { play } }
                                        loading(buttonStore.loading)
                                        variant { outline }
                                    } handledBy buttonStore.showMsg
                                """
                            )
                        }
                    }
                }

                showcaseSection("Configuration")
                span { +" add link to dokka (iframe?) " }
            }
        }
    }
