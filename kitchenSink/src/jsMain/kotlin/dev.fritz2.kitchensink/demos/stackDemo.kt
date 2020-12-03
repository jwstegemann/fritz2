package dev.fritz2.kitchensink.demos

import dev.fritz2.components.box
import dev.fritz2.components.flexBox
import dev.fritz2.components.lineUp
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import dev.fritz2.kitchensink.flexbox_
import dev.fritz2.kitchensink.gridbox_
import dev.fritz2.styling.params.Color
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun RenderContext.stackDemo(): Div {
    val item: RenderContext.(String, String) -> Unit = { color, text ->
        box({
            display { flex }
            justifyContent { center }
            alignItems { center }
            background { color { color } }
            color { "white" }
            size { "40px" }
        }) { p { +text } }
    }

    return contentFrame {
        showcaseHeader("Stacks")
        paragraph {
            +"A stack is a layout component which allows arbitrary elements to be aligned either in a vertical or"
            +" horizontal way. We offer dedicated components for each use case:"
        }
        ul {
            li {
                c("stackUp")
                +"for vertical alignment"
            }
            li {
                c("lineUp")
                +"for horizontal alignment"
            }
        }
        paragraph {
            +"They are basically specialized "
            internalLink("Flexboxes", flexbox_)
            +" which expose a built-in way to set the alignment direction and define the spacing "
            +" between the items."
        }
        paragraph {
            +"You can put arbitrary content into the stack components, like just one HTML element, a complex"
            +" structure of elements, or other components of course."
        }

        showcaseSection("Usage")
        paragraph {
            +"In order to line up items horizontally, just use the "
            c("lineUp")
            +" component and put some "
            c("items")
            +" in it:"
        }
        componentFrame {
            lineUp {
                items {
                    item(Theme().colors.primary, "1")
                    item(Theme().colors.danger, "2")
                    item(Theme().colors.warning, "3")
                }
            }
        }
        playground {
            source(
                """
                lineUp {
                    items {
                        // put some arbitrary content into the lineUp!
                        box({
                            display { flex }
                            justifyContent { center }
                            alignItems { center }
                            background { color { color } }
                            color { "white" }
                            size { "40px" }                        
                        }) { p { +"1" } }
                        // all following items without styling for better readability!
                        box { p { +"2" } }
                        box { p { +"3" } }
                    }
                }                    
                """.trimIndent()
            )
        }

        paragraph {
            +"Use "
            c("stackUp")
            +" to stack items vertically:"
        }
        componentFrame {
            stackUp {
                items {
                    item(Theme().colors.primary, "1")
                    item(Theme().colors.danger, "2")
                    item(Theme().colors.warning, "3")
                }
            }
        }
        playground {
            source(
                """
                stackUp {
                    items {
                        box { p { +"1" } } // simplified for readability
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
                    +".. defining the "
                    c("spacing")
                    +" between each item"
                }
                li {
                    +".. changing the order by setting "
                    c("reverse")
                    +" to"
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
                            item(Theme().colors.primary, "se")
                            item(Theme().colors.danger, "ver")
                            item(Theme().colors.warning, "re")
                        }
                    }
                    item(Theme().colors.danger, "1")
                    item(Theme().colors.primary, "2")
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
                            items { // will be rendered in reverse order
                                box { p { +"se" } }
                                box { p { +"ver" } }
                                box { p { +"re" } }
                            }
                        }
                        box { p { +"1" } }
                        box { p { +"2" } }
                    }
                }
                """.trimIndent()
            )
        }

        showcaseSection("Other layout techniques")

        warningBox {
            p {
                strong { +"Tip:" }
                +" Favor the application of a "
                internalLink("Gridbox", gridbox_)
                +" over complex styling for stack components."
            }
            p {
                +"The "
                externalLink(
                    "CSS grid model ",
                    "https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Grid_Layout"
                )
                +" offers much more control over the layout than flexbox approaches."
            }
        }

        paragraph {
            +"The default layout of our stack components is based on the following properties:"
        }
        ul {
            li {
                c("align-items = flex-start")
            }
            li {
                c("justify-content = flex-start")
            }
        }
        paragraph {
            +"In order to adopt the alignment to your needs, you need to apply the appropriate values via the"
            c("styling")
            +" attribute of the components:"
        }
        componentFrame {
            val sizedBox: RenderContext.(Int, Int) -> Unit = { w, h ->
                box({
                    background { color { "gold" } }
                    width { "${w}px" }
                    height { "${h}px" }
                    textAlign { center }
                }) { p { +"${w}x${h}" } }
            }

            lineUp({
                justifyContent { center }
                alignItems { center }
            }) {
                items {
                    stackUp({
                        alignItems { end }
                    }) {
                        items {
                            sizedBox(60, 60)
                            sizedBox(100, 100)
                            sizedBox(80, 30)
                        }
                    }
                    sizedBox(100, 40)
                    sizedBox(80, 80)
                }
            }
        }
        playground {
            source(
                """
                val sizedBox: RenderContext.(Int, Int) -> Unit = { w, h ->
                    box({
                        width { "${'$'}{w}px" }
                        height{ "${'$'}{h}px" }
                    }) {  }
                }
    
                lineUp({
                    justifyContent { center } // put the whole stack into the middle
                    alignItems { center } // put the items of the lineUp in the middle of the cross axis
                }) {
                    items {
                        stackUp({
                            alignItems { end } // put the vertical column items on the left side
                        }) {
                            items {
                                sizedBox(60, 60)
                                sizedBox(100, 100)
                                sizedBox(80, 30)
                            }
                        }
                        sizedBox(100, 40)
                        sizedBox(80, 80)
                    }
                }                    
                """.trimIndent()
            )
        }

    }

}