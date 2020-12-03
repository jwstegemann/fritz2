package dev.fritz2.kitchensink.demos

import dev.fritz2.components.box
import dev.fritz2.components.lineUp
import dev.fritz2.components.spinner
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.spinnerDemo(): Div {
    return contentFrame {
        showcaseHeader("Spinner")
        paragraph { +"A spinner is an animated element that is used to indicate a long running process." }

        showcaseSection("Thickness")
        paragraph {
            +"You can change the thickness of the spinner using the size property. Choose between "
            c("thin")
            +", "
            c("normal")
            +", and "
            c("fat")
            +", or use a custom size."
        }
        componentFrame {
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
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
                                size { normal }
                            }
                            p { +"normal" }
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
                }
            }
        }
        playground {
            source("""
                spinner {
                    size { thin } // change thickness here
                }
                """.trimIndent())
        }
        showcaseSection("Speed")
        paragraph {
            +"Let your spinner turn as fast as you like. Simply choose the time it should take the spinner to "
            +" complete one round."
        }
        componentFrame {
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner({
                                color { dark }
                                size { "3em" }
                            }) {
                                size { fat }
                                speed { ".5s" }
                            }
                            p { +"0.5s" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                color { dark }
                                size { "3em" }
                            }) {
                                size { fat }
                                speed { "1s" }
                            }
                            p { +"1s" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                color { dark }
                                size { "3em" }
                            }) {
                                size { fat }
                                speed { "3s" }
                            }
                            p { +"3s" }
                        }
                    }
                }
            }
        }
        playground {
            source("""
                spinner {
                    speed { ".5s" } // change speed here
                }
            """.trimIndent())
        }
        showcaseSection("Size")
        paragraph {
            +"You can change the size using the size property offered by the fritz2 design language."
        }
        componentFrame {
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner({
                                size { "1rem" }
                            }){
                                size { normal }
                            }
                            p { +"1rem" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                size { "3rem" }
                            }){
                                size { normal }
                            }
                            p { +"3rem" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                size { "5rem" }
                            }){
                                size { normal }
                            }
                            p { +"5rem" }
                        }
                    }
                }
            }
        }
        playground {
            source("""
                spinner({
                    size { "1rem" } // change size here
                }){
                    // other properties here
                }
            """.trimIndent())
        }
        showcaseSection("Color")
        paragraph {
            +"Choose the color of your spinner. You can use a custom color, or choose from one of the fritz2 theme "
            +" colors. There are "
            c("primary")
            +", "
            c("secondary")
            +", "
            c("tertiary")
            +", "
            c("info")
            +", "
            c("success")
            +", "
            c("light")
            +", "
            c("dark")
            +", "
            c("warning")
            +", and "
            c("danger")
            +" to choose from."
        }
        componentFrame {
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner({
                                color { primary }
                                size { "2em" }
                            }) {
                                size { fat }
                            }
                            p { +"primary" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                color { dark }
                                size { "2em" }
                            }) {
                                size { fat }
                            }
                            p { +"dark" }
                        }
                    }
                    stackUp {
                        items {
                            spinner({
                                color { danger }
                                size { "2em" }
                            }) {
                                size { fat }
                            }
                            p { +"danger" }
                        }
                    }
                }
            }
        }
        playground {
            source("""
                spinner({
                    color { primary } // change color here
                }) {
                    // other properties here
                }s
            """.trimIndent())
        }


        showcaseSection("Icon based")
        paragraph {
            +"You can choose an icon instead of a spinner to indicate a running process as well."
        }
        componentFrame {
            lineUp({
                alignItems { flexEnd }
            }) {
                items {
                    stackUp {
                        items {
                            spinner({
                                size { "5rem" }
                                color { "#FF1080" }
                            }) {
                                icon { heart }
                                speed { "2s" }
                            }
                            p { +"Choose any icon" }
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
                            p { +"Completely customizable, too!" }
                        }
                    }
                }
            }
        }
        playground {
            source("""
                spinner({
                    size { "5rem" } // this is size, not thickness
                    color { "#FF1080" }
                }) {
                    icon { heart } // change icon here
                    speed { "2s" }
                }
            """.trimIndent())
        }
    }
}