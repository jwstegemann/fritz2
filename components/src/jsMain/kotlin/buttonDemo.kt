import dev.fritz2.binding.RootStore
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.tracking.tracker
import kotlinx.browser.window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
fun HtmlElements.buttonDemo(): Div {

    val buttonStore = object : RootStore<String>("") {
        val loading = tracker()

        val showMsg = handle { model ->
            loading.track("running...") {
                delay(3000)
                window.alert("geclickt")
            }
            model
        }

    }

    buttonStore.watch()

    return div {
        flexBox({
            direction { column }
            padding { normal }
        }) {
            h1 { +"Button Showcase" }

            stackUp {
                items {
                    lineUp {
                        items {
                            clickButton { text("save") } handledBy buttonStore.showMsg
                            clickButton {
                                loading(buttonStore.loading)
                                text("save")
                            } handledBy buttonStore.showMsg
                            clickButton {
                                loading(buttonStore.loading)
                                text("save")
                                loadingText("saving...")
                            } handledBy buttonStore.showMsg
                        }
                    }

                    lineUp {
                        items {
                            pushButton {
                                variant { outline }
                                icon { fromTheme { arrowUp } }
                                text("save")
                            }
                            pushButton {
                                icon { fromTheme { arrowUp } }
                                loading(buttonStore.loading)
                                text("save")
                            }
                            pushButton {
                                icon { fromTheme { arrowUp } }
                                iconRight()
                                loading(buttonStore.loading)
                                text("save")
                            }
                        }
                    }

                    lineUp {
                        items {
                            pushButton {
                                size { small }
                                variant { outline }
                                icon { fromTheme { arrowUp } }
                                text("save")
                            }
                            pushButton {
                                icon { fromTheme { arrowUp } }
                                loading(buttonStore.loading)
                                text("save")
                            }
                            pushButton {
                                size { large }
                                icon { fromTheme { arrowUp } }
                                iconRight()
                                loading(buttonStore.loading)
                                text("save")
                            }
                        }
                    }

                    lineUp {
                        items {
                            pushButton {
                                icon { fromTheme { arrowUp } }
                            }
                            pushButton {
                                icon { fromTheme { arrowUp } }
                                loading(buttonStore.loading)
                            }
                            pushButton {
                                variant { ghost }
                                icon { fromTheme { arrowUp } }
                                loading(buttonStore.loading)
                            }
                        }
                    }
                }
            }

//            f2StackUp().apply {
//                f2LineUp().apply {
//                    ClickButton("save") handledBy buttonStore.showMsg
//                    ClickButton("save", buttonStore.loading) handledBy buttonStore.showMsg
//                    ClickButton("save", buttonStore.loading, "saving...") handledBy buttonStore.showMsg
//                }
//
//                f2LineUp().apply {
//                    ClickButton(theme.icons.arrowUp) handledBy buttonStore.showMsg
//                    ClickButton(theme.icons.arrowUp, buttonStore.loading) handledBy buttonStore.showMsg
//                }
//
//                f2LineUp().apply {
//                    ClickButton(theme.icons.arrowUp, "save") handledBy buttonStore.showMsg
//                    ClickButton(theme.icons.arrowUp, "save", buttonStore.loading) handledBy buttonStore.showMsg
//                }
//
//                f2LineUp().apply {
//                    ClickButton("save", theme.icons.arrowUp) handledBy buttonStore.showMsg
//                    ClickButton("save", theme.icons.arrowUp, buttonStore.loading) handledBy buttonStore.showMsg
//                }

        }
    }
}
