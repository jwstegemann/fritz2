import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun RenderContext.welcome(): Div {

    return contentFrame {
        (::p.styled {
            paddings {
                all { "1.0rem" }
            }
            border {
                width { thin }
            }
            radius { "15px" }
            boxShadow { flat }

        }) {

            paragraph {
                showcaseHeader( "Welcome to the fritz2 components demo" )

                +"This demo showcases the components we added to the fritz2 v0.8 so far. Remember"
                +" that this is a snapshot, so some of these"
                +" components might still need some work while others are not implemented yet. "
                +" The styling of our components is not finished, and the final 0.8 release will contain"
                +" a better styling and theme changing demonstration."
                b { +".. we're on it!" }

                showcaseSection("Hoping for your feedback!" )

                (::p.styled {
                    margins {
                        all { "1.0rem" }
                        top { "0" }
                        left { "0" }
                    }
                }) {
                    +"We value your opinions, praise and criticism. Please visit "
                    simpleLinkWithBackground("fritz2.dev", "http://fritz2.dev")
                    +" for further information, or go to our github page at "
                    simpleLinkWithBackground(
                        "https://github.com/jwstegemann/fritz2",
                        "https://github.com/jwstegemann/fritz2"
                    )
                    +" to open issues or check out the code. Now, let's start with our simple text elements."
                }
            }

        }

        showcaseHeader("H1: Text Elements Showcase" )
        paragraph {
            +"These are some examples of text elements. Text elements are not components - they are simple "
            +"HTML elements that can be customized using our new styling methods."
        }
        (::h2.styled {
            margins { top { "1.25rem" } }
            paddings {
                left { "0.3rem" }
                right { "0.3rem" }
            }
            background {
                color { primary }
            }
            width { maxContent }
            color { "white" }
            radius { "10px" }
        }) { +"H2 styled and a paragraph" }
        paragraph { +"Paragraph: Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat!" }
        showcaseSection("H3: Paragraph with inner Span" )
        paragraph {
            +"Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? Cum doloribus amet vel? "
            (::span.styled {
                background {
                    color { light }
                }
                fontStyle { italic }
                paddings {
                    left { "0.3rem" }
                    right { "0.3rem" }
                }
            }) { +"Span inside Paragraph." }
            +" Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum"
            +" in quia, provident corporis nulla corrupti placeat!"
        }
        (::h4.styled {
            margins {
                top { "1.25rem" }
            }
        }) { +"H4: Paragraph with Link" }
        paragraph {
            +"Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? "
            simpleAnchorWithBackground("Quo Vadis?")
            +" Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat."
        }
        (::h5.styled {
            margins {
                top { "1.25rem" }
            }
        }) { +"H5: U-List" }
        (::ul.styled {
            paddings {
                right { "0.3rem" }
                left { "0.3rem" }
            }
            margins {
                top { "1.25rem" }
            }
            background {
                color { dark }
            }
            color { secondary }
            radius { "5px" }
        }) {
            (1..5).map { li { +"List item $it" } }
        }
        (::h6.styled {
            margins {
                top { "1.25rem" }
            }
        }) { +"H6: Styled O-List" }
        (::ol.styled {
            margins {
                top { "1.25rem" }
            }
        }) {
            (1..5).map { li { +"Numbered list item $it" } }
        }
    }
}