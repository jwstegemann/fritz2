import dev.fritz2.components.Box
import dev.fritz2.components.Grid
import dev.fritz2.components.Text
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.AreaName
import dev.fritz2.styling.params.end
import dev.fritz2.styling.params.rgba
import dev.fritz2.styling.params.start
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.gridDemo(): Div {
    // example from https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout/Layout_using_Named_Grid_Lines
    return div {
        val grid = object {
            val HEADER: AreaName = "header"
            val SIDEBAR: AreaName = "sidebar"
            val CONTENT: AreaName = "content"
            val FOOTER: AreaName = "footer"
        }
        Grid({
            fontSize { normal }
            columns {
                repeat(9) { "9fr" }
                //repeat(autoFill) { "9fr" }
            }

            autoRows {
                minmax("100px", auto)
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
            Box({
                grid { area { grid.HEADER } }
                //bgColor { "green" }
                background {
                    color { "lime" }
                }
            }) {
                Text { +"Header" }
            }
            Box({
                grid { area { grid.SIDEBAR } }
                background { color { "yellow" } }
            }) {
                Text { +"Sidebar" }
            }
            Box({
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
                Text { +"Content" }
            }
            Box({
                grid { area { grid.FOOTER } }
                background { color { "lime" } }
            }) {
                Text { +"Footer" }
            }
            Box({
                margin { normal }
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
                    color { rgba(255, 0, 0, 0.5) }
                }
            }) {
                Text { +"Overlay" }
            }
        }
    }
}
