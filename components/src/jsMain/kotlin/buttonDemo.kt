import dev.fritz2.binding.RootStore
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.theme.theme
import dev.fritz2.tracking.tracker
import kotlinx.browser.window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun HtmlElements.buttonDemo(): Div {

    val buttonStore = object : RootStore<String>("") {
        val loading = tracker()
        val finish = storeOf(false)

        val showMsg = handle { model ->
            loading.track("running...") {
                flowOf(false) handledBy finish.update
                delay(3000)
                flowOf(true) handledBy finish.update
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

            buttonStore.finish.data.filter { it }.map { Unit } handledBy modal({
                minHeight { "0" }
            }) {
                size { theme().modal.sizes.small }
                closeButton()
                items {
                    lineUp {
                        items {
                            icon({ color { "darkgreen" } }) { fromTheme { checkCircle } }
                            p { +"Your data has been saved successfully!" }
                        }
                    }
                }
            }

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
                                icon { fromTheme { check } }
                                text("save")
                            }
                            pushButton {
                                icon { fromTheme { check } }
                                loading(buttonStore.loading)
                                text("save")
                            }
                            pushButton {
                                icon { fromTheme { check } }
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
                                icon { fromTheme { check } }
                                text("save")
                            }
                            pushButton {
                                icon { fromTheme { check } }
                                loading(buttonStore.loading)
                                text("save")
                            }
                            pushButton {
                                size { large }
                                icon { fromTheme { check } }
                                iconRight()
                                loading(buttonStore.loading)
                                text("save")
                            }
                        }
                    }

                    lineUp {
                        items {
                            pushButton {
                                icon { fromTheme { check } }
                            }
                            pushButton {
                                icon { fromTheme { check } }
                                loading(buttonStore.loading)
                            }
                            pushButton {
                                variant { ghost }
                                icon { fromTheme { check } }
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
//                    ClickButton(theme.icons.check) handledBy buttonStore.showMsg
//                    ClickButton(theme.icons.check, buttonStore.loading) handledBy buttonStore.showMsg
//                }
//
//                f2LineUp().apply {
//                    ClickButton(theme.icons.check, "save") handledBy buttonStore.showMsg
//                    ClickButton(theme.icons.check, "save", buttonStore.loading) handledBy buttonStore.showMsg
//                }
//
//                f2LineUp().apply {
//                    ClickButton("save", theme.icons.check) handledBy buttonStore.showMsg
//                    ClickButton("save", theme.icons.check, buttonStore.loading) handledBy buttonStore.showMsg
//                }

        }
    }
}
