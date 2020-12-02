import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.flexBoxDemo(theme: ExtendedTheme): Div {


    return contentFrame {

        showcaseHeader("Flex Layouts")
        paragraph {
            +"The main idea behind the flex layout is to give a container the ability to alter its items' width,"
            +" height, and order to best fill the available space. The container is basically a box which has the css"
            +" property"
            c("display: flex")
            +" attached."

        }
        componentFrame {
            lineUp({

            }) {
                items {
                    flexBox({
                        width { full }

                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({

                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }
                                }) {
                                    +"Box ${index + 1}"
                                }
                            }

                    }
                }

            }

        }
        playground {
            source(
                """
                      listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                }) {
                                    +"Box (index + 1)"
                                }
                            }
                
            """.trimIndent()
            )
        }

        showcaseSection("Flex-Direction")
        paragraph {
            +"fritz2 provides these well known flex-direction properties : "
            c("row")
            +"|"
            c("row-reverse")
            +"|"
            c("column")
            +"|"
            c("column-reverse")
            +"|"
        }

        componentFrame {

            stackUp {
                items {
                    flexBox({
                        width { full }
                        direction { row }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        direction { rowReverse }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        direction { column }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        direction { columnReverse }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }

                }
            }

        }

        playground {
            source(
                """
                 flexBox({
                        width { full }
                        direction { row } // set your direction property here
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }


                                }) {
                                    +"Box (index + 1)"
                                }
                            }
                    }
                   
            """.trimIndent()
            )
        }

        showcaseSection("Justify Content")
        paragraph {
            +"Justify Content defines the alignment along the main axis. Our flexbox provides the default properties:"
            c("flext-start")
            +"|"
            c("flex-end")
            +"|"
            c("center")
            +"|"
            c("space-between")
            +"|"
            c("space-around")
            +"|"
            c("space-evenly")


        }
        componentFrame {
            stackUp {
                items {

                    flexBox({
                        width { full }
                        justifyContent { flexStart }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }

                    flexBox({
                        width { full }
                        justifyContent { flexEnd }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }

                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        justifyContent { center }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }

                                    margin { smaller }
                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        justifyContent { spaceBetween }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        justifyContent { spaceAround }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        justifyContent { spaceEvenly }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }
                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }

                }
            }
        }
        playground {
            source(
                """
             flexBox({
                        width { full }
                        justifyContent { spaceEvenly } // set your justifyContent property here
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf("gold", "tomato", "lightseagreen")
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }


                                }) {
                                    +"Box (index + 1)"
                                }
                            }
                    }
        """.trimIndent()
            )
        }


        showcaseSection("Flex Wrap")
        paragraph {
            +"Flex items, by default, try to fit into one line. With flex wrap, you can change this behavior and"
            +" allow the items to wrap as needed."
            +"fritz2 provides these common properties for flex-wrap: "
            c("nowrap")
            +"|"
            c("wrap")
            +"|"
            c("wrap-reverse")


        }
        componentFrame {

            stackUp {
                items {
                    flexBox({
                        width { full }
                        wrap { nowrap }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf(
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen"
                        )
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({

                        width { full }
                        wrap { wrap }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf(
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen"
                        )
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                    flexBox({
                        width { full }
                        wrap { wrapReverse }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf(
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen"
                        )
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box ${index + 1}"
                                }
                            }
                    }
                }
            }
        }

        playground {
            source(
                """
                flexBox({
                        width { full }
                        wrap { wrapReverse }
                        border {
                            width { thin }
                            style { solid }
                            color { dark }

                        }
                        radius { normal }
                    }) {
                        listOf(
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen",
                            "gold",
                            "tomato",
                            "lightseagreen"
                        )
                            .forEachIndexed { index, value ->
                                box({
                                    background { color { value } }
                                    width { "150px" }
                                    height { "50px" }
                                    color { light }
                                    textAlign { center }
                                    margin { smaller }

                                }) {
                                    +"Box (index + 1)"
                                }
                            }
                    }
            """.trimIndent()
            )
        }
    }
}