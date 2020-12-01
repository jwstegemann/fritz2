import dev.fritz2.components.box
import dev.fritz2.components.lineUp
import dev.fritz2.components.spinner
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.spinnerDemo(): Div {
    return contentFrame {
        showcaseHeader("Spinner")
        paragraph { +"A spinner is an animated element, that is used to signalize a long running process." }
        paragraph { +"There are two flavours of spinner built into fritz2:" }
        showcaseSection("Pure CSS based")
        componentFrame {
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
                                color { primary }
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
        }
        showcaseSection("Icon based")
        componentFrame {
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner({
                                color { "#FF1080" }
                            }) {
                                icon { heart }
                                speed { "1s" }
                            }
                            p { +"choose any icon" }
                        }
                    }
                    stackUp {
                        items {
                            box({
                                background { color { primary } }
                                padding { normal }
                            }) {
                                spinner({
                                    color { base }
                                    size { "5em" }
                                }) {
                                    icon { fritz2 }
                                    speed { "1.5s" }
                                }
                            }
                            p { +"completly customizable too!" }
                        }
                    }
                }
            }
        }
    }
}