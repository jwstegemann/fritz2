package dev.fritz2.kitchensink.demos

import dev.fritz2.components.lineUp
import dev.fritz2.components.tooltip
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun RenderContext.tooltipDemo(): Div {
    return contentFrame {
        showcaseHeader("Tooltip")

        paragraph {
            +"The"
            c("tooltip")
            +"is more beautiful than the standard title that appears when a user interacts with an element."
        }

        showcaseSection("Usage")
        paragraph {
            +"In fritz2, tooltips can be added to each stylable element. It's possible to use multiline."
        }
        componentFrame {
            lineUp {
                items {

                    (::span.styled(){
                        tooltip("visit us on www.fritz2.dev") { top }()
                    }) { +"singleline tooltip"}

                    (::span.styled(){
                        tooltip("visit us on", "www.fritz2.dev") { top }()
                    }) { +"multiline tooltip"}


                }
            }
        }
        playground {
            source(
                """
                    (::span.styled(){
                        tooltip("visit us on www.fritz2.dev") { top }()
                    }) { +"singleline tooltip"}

                    (::span.styled(){
                        tooltip("visit us on", "www.fritz2.dev") { top }()
                    }) { +"multiline tooltip"}
                """
            )
        }

        showcaseSection("Placements")
        paragraph {
            +"The"
            c("tooltip")
            +" can be displayed on the"
            c("top")
            +","
            c("right")
            +","
            c("bottom")
            +", and"
            c("left")
            +" side of the styled element."
        }
        componentFrame {
            lineUp {
                items {

                    (::span.styled(){
                        tooltip("placement top") { top }()
                    }) { +"top"}

                    (::span.styled(){
                        tooltip("placement right") { right }()
                    }) { +"right"}

                    (::span.styled(){
                        tooltip("placement bottom") { bottom }()
                    }) { +"bottom"}

                    (::span.styled(){
                        tooltip("placement left") { left }()
                    }) { +"left"}

                }
            }
        }
        playground {
            source(
                """
                      (::span.styled(){
                        tooltip("placement top") { top }()
                    }) { +"top"}

                    (::span.styled(){
                        tooltip("placement right") { right }()
                    }) { +"right"}

                    (::span.styled(){
                        tooltip("placement bottom") { bottom }()
                    }) { +"bottom"}

                    (::span.styled(){
                        tooltip("placement left") { left }()
                    }) { +"left"}
                """
            )
        }

    }
}

