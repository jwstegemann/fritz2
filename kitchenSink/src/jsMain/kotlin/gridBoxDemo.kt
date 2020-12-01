import dev.fritz2.components.box
import dev.fritz2.components.gridBox
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.AreaName
import dev.fritz2.styling.params.end
import dev.fritz2.styling.params.start
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.gridBoxDemo(): Div {
    // example from https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout/Layout_using_Named_Grid_Lines

    return contentFrame {
        val grid = object {
            val HEADER: AreaName = "header"
            val SIDEBAR: AreaName = "sidebar"
            val CONTENT: AreaName = "content"
            val FOOTER: AreaName = "footer"
        }
        showcaseHeader("Grid Layout")
        paragraph {
            +"Use the grid layout to display elements on top of other elements. Also, this layout transforms with "
            +"screensize. Try resizing your browser window to see what it does when the available space becomes narrow."
        }
        br {}
        paragraph {
            gridBox({
                fontSize { normal }
                columns {
                    repeat(9) { "9fr" }
                    //repeat(autoFill) { "9fr" }
                }

                autoRows {
                    minmax("200px", auto)
                    //minmax(minContent, auto)
                    //minmax("100px", auto)
                }

                areas(
                    sm = {
                        with(grid) {
                            row(HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER)
                            row(SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR)
                            row(CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT)
                            row(FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER)
                        }
                    },
                    md = {
                        with(grid) {
                            row(HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER)
                            row(SIDEBAR, SIDEBAR, SIDEBAR, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT)
                            row(FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER)
                        }
                    }
                )
                gap { large }
                //autoFlow { dense }
                //raw("place-items: stretch flex-end;")
                //justifyContent { spaceEvenly }
                //alignItems { start }
            }) {
                box({
                    grid { area { grid.HEADER } }
                    //bgColor { "green" }
                    background {
                        color { light }
                    }
                    paddings { all { "0.2rem" } }
                }) {
                    (::p.styled {
                        padding { "1rem" }
                    }) { +"Header" }
                }
                box({
                    grid { area { grid.SIDEBAR } }
                    background { color { primary } }
                    color { light }
                    paddings { all { "0.2rem" } }
                }) {
                    (::p.styled {
                        padding { "1rem" }
                    }) { +"Sidebar" }
                }
                box({
                    paddings { all { "0.2rem" } }
                    grid(sm = { area { grid.CONTENT } })
                    background(
                        sm = {
                            image { "https://via.placeholder.com/150/?text=Klein" }
                            repeat { repeat }
                            positions {
                                horizontal { center }
                                vertical { center }
                            }
                            /*
        sizes {
            horizontal { "100%" }
            vertical { "80%" }
        }
         */
                        },
                        md = {
                            image { "https://via.placeholder.com/300/A9A9A9?text=Gross" }
                            repeat { repeat }
                            positions {
                                horizontal { center }
                                vertical { center }
                            }
                        }

                    )
                }) {
                    (::p.styled {
                        padding { "1rem" }
                    }){ +"Content" }
                }
                box({
                    grid { area { grid.FOOTER } }
                    background { color { light } }
                    paddings { all { "0.2rem" } }
                }) {
                    (::p.styled {
                        padding { "1rem" }
                    }) { +"Footer" }
                }
                box({
                    margin { normal }
                    paddings { all { "0.2rem" } }
                    grid(
                        sm = {
                            row {
                                start { grid.HEADER.start }
                                //end { grid.FOOTER.end }
                                //end { span(grid.SIDEBAR) }
                                end { grid.CONTENT.end }
                            }
                            column {
                                start { grid.CONTENT.start }
                                end { grid.CONTENT.end }
                            }
                        },
                        md = {
                            row {
                                start { grid.HEADER.start }
                                end { span(2) }
                            }
                            column {
                                start { grid.CONTENT.start }
                                end { grid.CONTENT.end }
                            }
                        }
                    )
                    //bgColor { "rgba(255, 0, 0, 0.5)" }
                    background {
                        //blendMode { darken }
                        color { primary_hover }
                    }
                    paddings { all { "0.2rem" } }
                }) {
                    (::p.styled {
                        padding { "1rem" }
                    }) { +"Overlay" }
                }
            }
        }
    }
}