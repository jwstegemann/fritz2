package dev.fritz2.kitchensink.demos

import dev.fritz2.components.box
import dev.fritz2.components.flexBox
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.responsiveDemo(): Div {

    return contentFrame {

        showcaseHeader("Responsiveness")

        paragraph {
            +"fritz2 strives to make responsive styling with a mobile-first approach as easy as possible."
        }

        paragraph {
            +"""The theme defines four breakpoints of different viewport-sizes ("""
            c("sm")
            +","
            c("md")
            +","
            c("lg")
            +", and"
            c("xl")
            +"""). You can set each property independently for these viewport-sizes:""".trimIndent()
        }

        playground {
            source(
                """
                    (::div.styled {
                        font-size(sm = { tiny }, lg = { normal })
                        width(sm = { full }, lg = { "768px" })
                        color { primary }
                    }) { + "my styled div" }
                """
            )
        }

        paragraph {
            +"""In accordance with mobile-first, when no value is given for a particular size, the next smaller one will be applied. In the example above, the"""
            c("font-size")
            +"will be"
            c("tiny")
            +"for "
            c("sm")
            +"and"
            c("md")
            +"and"
            c("normal")
            +"for"
            c("lg")
            +"and"
            c("xl")
            +". If only one value is given, it will be used for all sizes."
        }


        showcaseSection("Responsive Example")

        paragraph {
            +"This small example demonstrates responsive styling with fritz2. Resize your viewport to see the effect:"
        }

        componentFrame(padding = false) {
            flexBox({
                padding { small }
                direction(sm = { column }, md = { row })
            }) {
                box({
                    zIndex { layer(1) }
                    margins(
                        {
                            top { small }
                            bottom { small }
                        },
                        md = { left { normal } }
                    )
                    flex { shrink { "0" } }
                }) {
                    (::img.styled {
                        width(sm = { full }, md = { wide.small })
                        boxShadow { flat }
                        radius { large }
                    }) {
                        src("https://bit.ly/3qthIO3")
                        alt("Photo by Lauren York on Unsplash")
                    }
                }

                box({
                    zIndex { base }
                    margins(
                        {
                            top { small }
                            bottom { large }
                        },
                        md = { left { normal } }
                    )
                }) {
                    (::p.styled {
                        textTransform { capitalize }
                        color { info }
                        fontWeight { semiBold }
                        fontSize { smaller }
                    }) {
                        +"Photo by Lauren York on Unsplash"
                    }
                    (::h1.styled {
                        fontSize { large }
                        fontWeight { bold }
                    }) { +"Flex Layouts Showcase" }
                    (::p.styled {
                        paddings {
                            all { small }
                            left { none }
                        }
                        fontSize { small }
                    }) {
                        +"""Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut 
                        labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam."""
                    }
                }
            }
        }
        playground {
            source(
                """
                    flexBox({
                        padding { small }
                        direction(sm = { column }, md = { row })
                    }) {
                        box({
                            margins(
                                {
                                    top { small }
                                    bottom { small }
                                },
                                md = { left { normal } }
                            )
                            flex { shrink { "0" } }
                        }) {
                            (::img.styled {
                                width(sm = { full }, md = { wide.small })
                                boxShadow { flat }
                                radius { large }
                            }) {
                                src("https://bit.ly/3qthIO3")
                                alt("Photo by Lauren York on Unsplash")
                            }
                        }
        
                        box({
                            zIndex { base }
                            margins(
                                sm = {
                                    top { small }
                                    bottom { large }
                                },
                                md = { left { normal } }
                            )
                        }) {
                            (::p.styled {
                                textTransform { capitalize }
                                color { info }
                                fontWeight { semiBold }
                                fontSize { smaller }
                            }) {
                                +"Photo by Lauren York on Unsplash"
                            }
                            
                            (::h1.styled {
                                fontSize { large }
                                fontWeight { bold }
                            }) { +"Flex Layouts Showcase" }
                            
                            (::p.styled {
                                paddings {
                                    all { small }
                                    left { none }
                                }
                                fontSize { small }
                            }) {
                                +"Lorem ipsum dolor sit amet..." 
                            }
                        }
                    }
                """
            )
        }


    }
}



