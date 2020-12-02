import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

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
        showcaseHeader("Gridbox")
        paragraph {
            +"Use a Gridbox to create arbitrary complex layouts."
            +" It does not offer special component properties, but instead relies on the "
            simpleLinkWithBackground(
                "https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Grid_Layout",
                "CSS based grid-layout"
            )
            +"styling options."
        }
        showcaseSection("Usage")
        paragraph {
            +"In order to create a Gridbox, just pass some specialized grid styling information."
            +" In this simple example, a five column based grid layout was defined, but seven items where inserted."
            +" The surplus items will be rendered into a second row:"
        }
        componentFrame {
            gridBox({
                columns { repeat(5) { "1fr" } }
                gap { normal }
                children("div") {
                    size { "60px" }
                    background { color { "gold" } }
                }
            }) {
                div { +"one" }
                div { +"two" }
                div { +"three" }
                div { +"four" }
                div { +"five" }
                div { +"six" }
                div { +"seven" }
            }
        }
        playground {
            source(
                """
                gridBox({
                    columns { repeat(5) { "1fr" } }
                    gap { normal }
                    children("div") {
                        size { "60px" }
                        background { color { "gold" } }
                    }
                }) {
                    div { +"one" }
                    div { +"two" }
                    div { +"three" }
                    div { +"four" }
                    div { +"five" }
                    div { +"six" }
                    div { +"seven" }
                }                    
                """.trimIndent()
            )
        }

        showcaseSection("Complex layout")
        paragraph {
            +"Now have a look at this rather complex layout (including responsive behaviour), in order to "
            +"learn about a very helpful technique based on Kotlin's"
            c("objects")
            +", for defining the overall column layout."
        }
        componentFrame {
            val toggle = storeOf(false)
            gridBox({
                fontSize { normal }
                columns {
                    repeat(3) { "1fr" }
                }

                css(
                    sm = "grid-template-rows: 1fr 1fr 3fr 1fr",
                    md = "grid-template-rows: 1fr 4fr 1fr"
                )

                areas(
                    sm = {
                        with(grid) {
                            row(HEADER, HEADER, HEADER)
                            row(SIDEBAR, SIDEBAR, SIDEBAR)
                            row(CONTENT, CONTENT, CONTENT)
                            row(FOOTER, FOOTER, FOOTER)
                        }
                    },
                    md = {
                        with(grid) {
                            row(HEADER, HEADER, HEADER)
                            row(SIDEBAR, CONTENT, CONTENT)
                            row(FOOTER, FOOTER, FOOTER)
                        }
                    }
                )
                gap { large }
            }) {
                box({
                    grid { area { grid.HEADER } }
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
                            image { "https://via.placeholder.com/300/?text=small%20sized%20device" }
                            repeat { repeat }
                            positions {
                                horizontal { center }
                                vertical { center }
                            }
                        },
                        md = {
                            image { "https://via.placeholder.com/300/A9A9A9?text=medium%20sized%20device" }
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
                toggle.data.render { show ->
                    if (show) {
                        box({
                            margin { normal }
                            paddings { all { "0.2rem" } }
                            grid(
                                sm = {
                                    row {
                                        start { grid.HEADER.start }
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
                                        start { "3" }
                                        end { grid.CONTENT.end }
                                    }
                                }
                            )
                            background {
                                color { primary_hover }
                            }
                            paddings { all { "0.2rem" } }
                            // TODO: How to animate a little? Fade in sideways
                            css("text-overflow: ellipsis;")
                            css("transition: opacity .2s, transform .2s;")
                            css("white-space: pre;")
                        }) {
                            (::p.styled {
                                padding { "1rem" }
                            }) { +"Drawer" }
                        }
                    }
                }
            }
            pushButton({
                margins { top { normal } }
            }) {
                text("Toggle Drawer!")
                events {
                    clicks.events.map { !toggle.current } handledBy toggle.update
                }
            }
        }

        // TODO: infoBox Auslagern
        (::blockquote.styled {
            borders {
                left {
                    color { rgb(221, 107, 32) }
                    width { "4px" }
                    style { solid }
                }
            }
            radius { "4px" }
            background {
                color { rgb(254, 235, 200) }
            }
            margins {
                top { normal }
            }
            paddings {
                right { normal }
                left { small }
                top { small }
                bottom { small }
            }
        }) {
            p {
                strong { +"Please note:" }
                +" This layout also transforms with screen size. Try resizing your browser window to see "
                +"how the sidebar is replaced into its own row when the space becomes narrow."
                +"The content then appears on a separate row below that."
            }
        }

        showcaseSubHeader("Column layout")
        paragraph {
            +"Use a simple Kotlin object in order to define the "
            strong { +"types" }
            +" of columns."
            +" Then you can refer to its properties later within the column definitions:"
        }
        playground {
            source(
                """
                // define the kinds of cells
                val grid = object {
                    val HEADER: AreaName = "header"
                    val SIDEBAR: AreaName = "sidebar"
                    val CONTENT: AreaName = "content"
                    val FOOTER: AreaName = "footer"
                }                    
                
                gridBox({
                    columns {
                        repeat(3) { "1fr" }
                    }
                                    
                    // refer to those -> easy refactoring included
                    areas(
                        with(grid) {
                            row(HEADER, HEADER, HEADER)
                            row(SIDEBAR, CONTENT, CONTENT) // mix cell types in one row
                            row(FOOTER, FOOTER, FOOTER)
                        }
                    )
                }) // ...
                """.trimIndent()
            )
        }
        paragraph {
            +"For defining the content, also use the object fields:"
        }
        playground {
            source(
                """
                gridBox({
                    // as above
                }) {
                    box({
                        grid { area { grid.HEADER } } // refer to the cell type 
                    }) {
                        // put the special content for this cell type
                    }
                    // next type
                    box({
                        grid { area { grid.CONTENT } }  
                    }) {
                        // ...
                    }
                    // and so on!
                }
                """.trimIndent()
            )
        }

        showcaseSubHeader("Defining Areas")
        paragraph {
            +"In order to group cells together, you can also define areas by referring to the name of the cell type."
            +" This technique is used for a simple togglable drawer that appears on the right side with the click of a"
            +" button."
        }
        playground {
            source(
                """
                gridBox({
                    // as above
                }) {
                    // define the drawer
                    box({
                        row {
                            start { grid.HEADER.start } // refer to the cell type and specify the starting
                            end { span(2) } // occupy just two rows
                        }
                        column {
                            start { "3" } // start at the third vertical gap
                            end { grid.CONTENT.end } // refer to another cell type and the exact position  
                        }
                    }) {
                        // put the special content for the drawer
                    }
                }
            """.trimIndent()
            )
        }
    }
}