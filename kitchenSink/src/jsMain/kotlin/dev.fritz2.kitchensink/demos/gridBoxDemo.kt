package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.storeOf
import dev.fritz2.components.box
import dev.fritz2.components.gridBox
import dev.fritz2.components.pushButton
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import dev.fritz2.styling.params.AreaName
import dev.fritz2.styling.params.end
import dev.fritz2.styling.params.start
import dev.fritz2.styling.params.styled
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
            externalLink(
                "CSS based grid-layout",
                "https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Grid_Layout"
            )
            +" styling options."
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
                    background { color { warning } }
                    display { flex }
                    css("justify-content: center")
                    css("align-items: center")
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
                }) {
                    // put some arbitrary content into the gridBox!
                    box({
                        size { "60px" }
                        background { color { warning } }
                        display { flex }
                        justifyContent { center }
                        alignItems { center }
                    }) { +"one" }
                    // all following items without styling for better readability!
                    box { +"two" }
                    box { +"three" }
                    box { +"four" }
                    box { +"five" }
                    box { +"six" }
                    box { +"seven" }
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
                            margin { none }
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
                            padding { normal }
                            textAlign { center }
                        }) {
                             +"Drawer"
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

        warningBox {
            p {
                strong { +"Please note:" }
                +" This layout also transforms with screen size. Try resizing your browser window to see "
                +"how the sidebar is replaced into its own row when the space becomes narrow."
                +"The content then appears on a separate row below that."
            }
        }

        showcaseSubSection("Column layout")
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

        showcaseSubSection("Defining Areas")
        paragraph {
            +"In order to group cells together, you can also define areas by referring to the name of the cell type."
            +" This technique is used for the simple togglable drawer above, that appears on the right side by"
            +" the click of the button below the gridbox."
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