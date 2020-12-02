import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun RenderContext.stackDemo(): Div {
    val item: RenderContext.(String, String) -> Unit = { color, text ->
        box({
            background { color { color } }
            size { "40px" }
        }) { p { +text } }
    }

    return contentFrame {
        showcaseHeader("Stacks")
        paragraph {
            +"A stack is a layout component that allows arbitrary elements to be stacked either in a vertical or"
            +" horizontal way."
            +"We offer dedicated components for each use case"
        }

        showcaseSection("Usage")
        paragraph {
            +"In order to stack up items horizontally, just use the "
            c("lineUp")
            +" component and put some "
            c("items")
            +"in it:"
        }
        componentFrame {
            lineUp {
                items {
                    item("gold", "1")
                    item("tomato", "2")
                    item("pink", "3")
                }
            }
        }
        playground {
            source(
                """
                lineUp {
                    items {
                        box({
                            background { color { "gold" } }
                            size { "40px" }
                        }) { p { +"1" } }
                        box({
                            background { color { "tomato" } }
                            size { "40px" }
                        }) { p { +"2" } }
                        box({
                            background { color { "pink" } }
                            size { "40px" }
                        }) { p { +"3" } }
                    }
                }                    
                """.trimIndent()
            )
        }

        paragraph {
            +"You can also use "
            c("stackUp")
            +" to stack items vertically:"
        }
        componentFrame {
            stackUp {
                items {
                    item("gold", "1")
                    item("tomato", "2")
                    item("pink", "3")
                }
            }
        }
        playground {
            source(
                """
                stackUp {
                    items {
                        box { p { +"1" } } // simplified for readability!
                        box { p { +"2" } }
                        box { p { +"3" } }
                    }
                }                    
                """.trimIndent()
            )
        }

        showcaseSection("Customization")
        paragraph {
            +"Stacks can be customized by..."
            ul {
                li {
                    +"... defining the "
                    c("spacing")
                    +"between each item"
                }
                li {
                    +"... changing the order by setting "
                    c("reverse")
                    +"to"
                    c("true")
                }
            }
        }
        componentFrame {
            lineUp {
                spacing { huge }
                items {
                    stackUp {
                        spacing { tiny }
                        reverse { true }
                        items {
                            item("gold", "se")
                            item("tomato", "ver")
                            item("pink", "in")
                        }
                    }
                    item("gold", "1")
                    item("tomato", "2")
                }
            }
        }
        playground {
            source(
                """
                lineUp {
                    spacing { huge }
                    items {
                        stackUp {
                            spacing { tiny }
                            reverse { true }
                            items { // will be rendered in reverse order!
                                box { p { +"se" } }
                                box { p { +"ver" } }
                                box { p { +"in" } }
                            }
                        }
                        box { p { +"1" } }
                        box { p { +"2" } }
                    }
                }
                """.trimIndent()
            )
        }
    }

}