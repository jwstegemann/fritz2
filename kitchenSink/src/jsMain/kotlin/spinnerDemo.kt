import dev.fritz2.components.box
import dev.fritz2.components.lineUp
import dev.fritz2.components.spinner
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.spinnerDemo(): Div {
    return stackUp({
        alignItems { start }
        padding { "1rem" }
    }) {
        spacing { large }
        items {
            h1 { +"Spinner Showcase" }
            p { +"A spinner is an animated element, that is used to signalize a long running process." }
            p { +"There are two flavours of spinner built into fritz2:" }
            h2 { +"Pure CSS based" }
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner { }
                            p { +"vanilla" }
                        }
                    }
                    stackUp {
                        items {
                            spinner {
                                size { fat }
                            }
                            p { +"fat" }
                        }
                    }
                    stackUp {
                        items {
                            spinner {
                                size { thin }
                            }
                            p { +"thin" }
                        }
                    }
                    stackUp {
                        items {
                            spinner {
                                speed { "2s" }
                            }
                            p { +"configure turning speed" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                color { warning }
                                size { "3em" }
                            }) {
                                size { fat }
                                speed { "1.5s" }
                            }
                            p { +"custom styleable too" }
                        }
                    }
                }
            }
            h2 { +"Icon based" }
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner {
                                icon { favorite }
                                speed { "1s" }
                            }
                            p { +"choose any icon" }
                        }
                    }
                    stackUp {
                        items {
                            box({
                                background { color { dark } }
                                padding { normal }
                            }) {
                                spinner({
                                    color { warning }
                                    size { "5em" }
                                }) {
                                    icon { fritz2 }
                                    speed { "1.5s" }
                                }
                            }
                            p { +"completly cutomizable too!" }
                        }
                    }
                }
            }
        }
    }
}