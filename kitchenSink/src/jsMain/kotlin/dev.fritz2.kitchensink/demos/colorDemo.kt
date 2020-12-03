package dev.fritz2.kitchensink.demos

import dev.fritz2.components.box
import dev.fritz2.components.lineUp
import dev.fritz2.components.tooltip
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled

fun RenderContext.colorDemo(): Div {
    return div {
        lineUp({
            margins {
                top { huge }
            }
        }) {
            items {
                box({
                    width { "90%" }
                    //height { "2.35rem" }
                    background {
                        color { primary }
                    }
                    border {
                        width { fat }
                        color { primary }
                    }
                    hover {
                        width { "100%" }
                        tooltip("#319795") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { primary }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"primary" }
                }

            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "85%" }
                    //height { "2.35rem" }
                    background {
                        color { primary_hover }
                    }
                    border {
                        width { fat }
                        color { "transparent" }
                    }
                    hover {
                        width { "95%" }
                        tooltip("rgb(49,151,149, 0.3)") { right }()
                    }
                    radius { "1.3rem" }
                }) {
                    (::p.styled {
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { primary_hover }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"primary_hover" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "80%" }
                    //height { "2.35rem" }
                    background {
                        color { secondary }
                    }
                    border {
                        width { fat }
                        color { secondary }
                    }
                    hover {
                        width { "90%" }
                        tooltip("#b2f5ea") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { secondary }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"secondary" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "75%" }
                    //height { "2.35rem" }
                    background {
                        color { tertiary }
                    }
                    border {
                        width { fat }
                        color { tertiary }
                    }
                    hover {
                        width { "85%" }
                        tooltip("#718096") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { tertiary }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"tertiary" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "70%" }
                    //height { "2.35rem" }
                    background {
                        color { dark }
                    }
                    border {
                        width { fat }
                        color { dark }
                    }
                    hover {
                        width { "80%" }
                        tooltip("#2d3748") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { dark }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"dark" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "65%" }
                    //height { "2.35rem" }
                    background {
                        color { light }
                    }
                    border {
                        width { fat }
                        color { light }
                    }
                    hover {
                        width { "75%" }
                        tooltip("#e2e8f0") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { light }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"light" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "60%" }
                    //height { "2.35rem" }
                    background {
                        color { light_hover }
                    }
                    border {
                        width { fat }
                        color { "transparent" }
                    }
                    hover {
                        width { "70%" }
                        tooltip("rgb(226,232,240, 0.5)") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { light_hover }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"light_hover" }
                }
            }
        }

        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "55%" }
                    //height { "2.35rem" }
                    background {
                        color { info }
                    }
                    border {
                        width { fat }
                        color { info }
                    }
                    hover {
                        width { "65%" }
                        tooltip("#3182ce") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { info }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"info" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "50%" }
                    //height { "2.35rem" }
                    background {
                        color { success }
                    }
                    border {
                        width { fat }
                        color { success }
                    }
                    hover {
                        width { "60%" }
                        tooltip("#28a745") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { success }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"success" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "45%" }
                    //height { "2.35rem" }
                    background {
                        color { warning }
                    }
                    border {
                        width { fat }
                        color { warning }
                    }
                    hover {
                        width { "55%" }
                        tooltip("#ffc107") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { warning }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"warning" }
                }
            }
        }
        lineUp({
            margins {
                top { tiny }
            }
        }) {
            items {
                box({
                    width { "40%" }
                    //height { "2.35rem" }
                    background {
                        color { danger }
                    }
                    border {
                        width { fat }
                        color { danger }
                    }
                    hover {
                        width { "50%" }
                        tooltip("#dc3545") { right }()
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::p.styled {
                        textAlign { right }
                        width { "8rem" }
                        background {
                            color { base }
                        }
                        radii {
                            left { "1rem" }
                        }
                        color { danger }
                        paddings {
                            left { small }
                            right { small }
                        }
                    }) { +"danger" }
                }
            }
        }
    }
}
