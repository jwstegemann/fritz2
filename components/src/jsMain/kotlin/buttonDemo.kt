import dev.fritz2.binding.RootStore
import dev.fritz2.binding.invoke
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.theme.theme
import dev.fritz2.tracking.tracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@ExperimentalCoroutinesApi
fun RenderContext.buttonDemo(): Div {

    val modal = modal({
        minHeight { "0" }
    }) {
        size { theme().modal.sizes.small }
        closeButton()
        items {
            lineUp {
                items {
                    icon({ color { "darkgreen" } }) { fromTheme { checkCircle } }
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

                        h3 { +"Choose from variants like outline, ghost and more. Icons can be on either side of the text." }
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

                        h3 { +"The button sizes work for all variants, of course." }
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
}
