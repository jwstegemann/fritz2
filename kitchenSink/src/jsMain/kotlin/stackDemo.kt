import dev.fritz2.components.box
import dev.fritz2.components.icon
import dev.fritz2.components.lineUp
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun RenderContext.stackDemo(): Div {
    return contentFrame {
        showcaseHeader("StackUps and LineUps")
        paragraph {
            +"Our stack containers accept any element. A LineUp allows you to align its contents"
            +" horizontally, while a StackUp puts the elements on top of each other. You may also "
            +" reverse the order of the contents."
        }
        lineUp({
            border {
                width { normal }
                color { dark }
                style { solid }
            }
            padding { small }
            width { "100%" }
            alignItems { center }
        }) {
            items {
                (::h4.styled {
                    fontSize { "1.2em" }
                    color { primary }
                    css("writing-mode: vertical-rl")
                }) { +"stackUp → 3 stacked boxes, each containing a lineUp" }
                stackUp({
                    padding { small }
                    width { "100%" }
                    radius { "5%" }
                }) {
                    spacing { normal }
                    items {
                        (::h4.styled {
                            fontSize { "1.2em" }
                            color { dark }
                        }) { +"lineUp → horizontally arranged items in the boxes below" }
                        lineUp({
                            border {
                                width { normal }
                                color { dark }
                                style { solid }
                            }
                            padding { small }
                            width { "100%" }
                            radius { "5%" }
                        }) {
                            spacing { normal }
                            reverse { true }
                            items {
                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { warning }
                                    }
                                    radius { "5%" }
                                }) {
                                    +"The order of"
                                }

                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { warning }
                                    }
                                    radius { "5%" }
                                }) {
                                    +"these boxes"
                                }
                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { warning }
                                    }
                                    radius { "5%" }
                                }) {
                                    +"is reversed"
                                }
                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { dark }
                                    }
                                    color { light }
                                    radius { "5%" }
                                }) {
                                    icon({
                                        size { "4rem" }
                                        color { warning }
                                    }) {
                                        fromTheme {
                                            refresh
                                        }
                                    }
                                }
                            }
                        }
                        lineUp({
                            border {
                                width { normal }
                                color { dark }
                                style { solid }
                            }
                            justifyContent { center }
                            padding { small }
                            width { "100%" }
                            radius { "5%" }
                        }) {
                            spacing { normal }
                            items {
                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { danger }
                                    }
                                    color { base }
                                    radius { "5%" }
                                }) {
                                    +"These boxes are"
                                }

                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { danger }
                                    }
                                    color { base }
                                    radius { "5%" }
                                }) {
                                    +"next to an image "
                                }

                                (::img.styled {
                                    width { normal }
                                    boxShadow { flat }
                                    radius { smaller }
                                }) {
                                    src("https://www.fritz2.dev/static/fritz2_state.001.png")
                                    alt("Random image for flex layout demonstration")
                                }
                            }
                        }
                        lineUp({
                            border {
                                width { normal }
                                color { dark }
                                style { solid }
                            }
                            padding { small }
                            width { "100%" }
                            radius { "5%" }
                        }) {
                            spacing { normal }
                            items {
                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { info }
                                    }
                                    color { base }
                                    radius { "5%" }
                                }) {
                                    +"But these lined up"
                                }

                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { info }
                                    }
                                    color { base }
                                    radius { "5%" }
                                }) {
                                    +"boxes are in"
                                }
                                box({
                                    margin { normal }
                                    paddings { all { "0.5rem" } }
                                    background {
                                        color { info }
                                    }
                                    color { base }
                                    radius { "5%" }
                                }) {
                                    +"the right order"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}