package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.invoke
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
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

    return contentFrame {

        showcaseHeader("Buttons")

        paragraph {
            +"""
            Using a Button, you can trigger an action which can be handled by your Store or by another
            component, like launching a modal dialog. 
            """.trimIndent()
        }

        showcaseSection("Usage")
        paragraph {
            +"Define your button by adding text and / or an icon to its content and setting the color. A"
            c("pushButton")
            +" gives you full control over the underlying HTML-button. The"
            c("clickButton")
            +" exposes the Flow of click-events, so you can conveniently connect it to a Handler or another component."
        }

        componentFrame {
            lineUp {
                items {
                    clickButton { text("click me") } handledBy modal

                    pushButton {
                        icon { fromTheme { arrowLeft } }
                        color { danger }
                        text("previous")
                    }

                    pushButton {
                        icon { fromTheme { arrowRight } }
                        iconRight()
                        color { warning }
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
                        color { danger }
                        text("previous")
                    }

                    pushButton {
                        icon { fromTheme { arrowRight } }
                        iconRight()
                        color { warning }
                        text("next")
                    }

                    pushButton { icon { fromTheme { check } } }
                """
            )
        }

        showcaseSection("Variants")
        paragraph {
            +"fritz2 offers three different flavours of buttons for the various use cases: "
            c("solid")
            +", "
            c("outline")
            +", "
            c("ghost")
            +", and "
            c("link")
        }
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
        paragraph {
            +"Choose from on three predefined sizes ("
            c("small")
            +", "
            c("normal")
            +", or  "
            c("large")
            +"), or scale your button to your needs using the styling parameter."
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
        paragraph {
            +"Connect a button to a "
            c("Tracker")
            +" to show its loading state. You can specify a different text which is shown while loading."
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



