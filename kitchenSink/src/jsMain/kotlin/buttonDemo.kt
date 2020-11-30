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

    val buttonStore = object : RootStore<String>("") {
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
    return div {
        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            items {
                h1 { +"Showcase Buttons" }

                stackUp({alignItems { start }}) {
                    items {

                        h3 { +"Buttons have different clicked-animations: text, icon, and text with icon." }
                        componentFrame {
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
                        }

                        h3 { +"Choose from variants like outline, ghost and more. Icons can be on either side of the text." }
                        componentFrame {
                            lineUp {
                                items {
                                    pushButton {
                                        variant { outline }
                                        icon { fromTheme { check } }
                                        loading(buttonStore.loading)
                                        text("save")
                                    }
                                    pushButton {
                                        variant { ghost }
                                        icon { fromTheme { check } }
                                        iconRight()
                                        loading(buttonStore.loading)
                                        text("save")
                                    }
                                }
                            }
                        }

                        h3 { +"The button sizes work for all variants, of course." }
                        componentFrame {
                            lineUp {
                                items {
                                    pushButton {
                                        size { small }
                                        variant { solid }
                                        icon { fromTheme { check } }
                                        text("save")
                                    }
                                    pushButton {
                                        icon { fromTheme { check } }
                                        variant { link }
                                        loading(buttonStore.loading)
                                        text("save")
                                    }
                                    pushButton {
                                        size { large }
                                        icon { fromTheme { check } }
                                        iconRight()
                                        variant { outline }
                                        loading(buttonStore.loading)
                                        text("save")
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
