import dev.fritz2.components.box
import dev.fritz2.components.lineUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled

fun RenderContext.colorDemo(): Div {
    return contentFrame {
        showcaseHeader("Colors")
        lineUp ({
            margins {
                top { "3rem"}
            }
        }) {
            items {
                box({
                    width { "90%" }
                    height { "3rem" }
                    background {
                        color { primary }
                    }
                    border {
                        width { fat }
                        color { primary }
                    }
                    hover {
                        width { "100%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { primary }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"primary"}
                }

            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "85%" }
                    height { "3rem" }
                    background {
                        color { primary_hover }
                    }
                    border {
                        width { fat }
                        color { "transparent" }
                    }
                    hover {
                        width { "95%" }
                    }
                    radius { "1.3rem" }
                }) {
                    (::h1.styled {
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { primary_hover }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"primary_hover"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "80%" }
                    height { "3rem" }
                    background {
                        color { secondary }
                    }
                    border {
                        width { fat }
                        color { secondary }
                    }
                    hover {
                        width { "90%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { secondary }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"secondary"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "75%" }
                    height { "3rem" }
                    background {
                        color { tertiary }
                    }
                    border {
                        width { fat }
                        color { tertiary }
                    }
                    hover {
                        width { "85%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { tertiary }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"tertiary"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "70%" }
                    height { "3rem" }
                    background {
                        color { dark }
                    }
                    border {
                        width { fat }
                        color { dark }
                    }
                    hover {
                        width { "80%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { dark }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"dark"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "65%" }
                    height { "3rem" }
                    background {
                        color { light }
                    }
                    border {
                        width { fat }
                        color { light }
                    }
                    hover {
                        width { "75%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { light }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"light"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "60%" }
                    height { "3rem" }
                    background {
                        color { light_hover }
                    }
                    border {
                        width { fat }
                        color { "transparent" }
                    }
                    hover {
                        width { "70%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { light_hover }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"light_hover"}
                }
            }
        }

        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "55%" }
                    height { "3rem" }
                    background {
                        color { info }
                    }
                    border {
                        width { fat }
                        color { info }
                    }
                    hover {
                        width { "65%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { info }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"info"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "50%" }
                    height { "3rem" }
                    background {
                        color { success }
                    }
                    border {
                        width { fat }
                        color { success }
                    }
                    hover {
                        width { "60%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { success }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"success"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "45%" }
                    height { "3rem" }
                    background {
                        color { warning }
                    }
                    border {
                        width { fat }
                        color { warning }
                    }
                    hover {
                        width { "55%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { warning }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"warning"}
                }
            }
        }
        lineUp ({
            margins {
                top { "1rem"}
            }
        }) {
            items {
                box({
                    width { "40%" }
                    height { "3rem" }
                    background {
                        color { danger }
                    }
                    border {
                        width { fat }
                        color { danger }
                    }
                    hover {
                        width { "50%" }
                    }
                    radius { "1.3rem" }
                    textAlign { right }
                }) {
                    (::h1.styled {
                        textAlign { right }
                        width { maxContent }
                        background {
                            color { base }
                        }
                        radii {
                            left{ "1rem" }
                        }
                        color { danger }
                        paddings {
                            bottom { tiny }
                            left { small }
                            right { small }
                        }
                    }) { +"danger"}
                }
            }
        }


    }
}