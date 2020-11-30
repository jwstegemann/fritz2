import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun RenderContext.welcome(): Div {

    return stackUp({
        margins { top { huge } }
        alignItems { start }
        padding { "1rem" }
    }) {
        items {
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

                p {
                    (::h1.styled {
                        margins {
                            bottom { "1.0rem" }
                        }
                        fontSize { giant }
                    }) { +"Welcome to the fritz2 components demo" }

                    +"This demo showcases the components we added to the fritz2 v0.8 so far. Remember"
                    +" that this is a snapshot, so some of these"
                    +" components might still need some work while others are not implemented yet. "
                    +" The styling of our components is not finished, and the final 0.8 release will contain"
                    +" a better styling and theme changing demonstration."
                    b { +".. we're on it!" }

                    (::h3.styled {
                        margins {
                            bottom { "0.5rem" }
                            top { "1.0rem" }
                        }
                    }) { +"Hoping for your feedback!" }

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

            h1 { +"H1: Text Elements Showcase" }
            p {
                +"These are some examples of text elements. Text elements are not components - they are simple "
                +"HTML elements that can be customized using our new styling methods."
            }
            (::h2.styled {
                paddings {
                    left { "0.3rem" }
                    right { "0.3rem" }
                }
                background {
                    color { warning }
                }
                color { "white" }
                radius { "10px" }
            }) { +"H2 styled and a paragraph" }
            p { +"Paragraph: Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat!" }
            h3 { +"H3: Paragraph with inner Span" }
            p {
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
            h4 { +"H4: Paragraph with Link" }
            p {
                +"Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? "
                simpleAnchorWithBackground("Quo Vadis?")
                +" Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat."
            }
            h5 { +"H5: U-List" }
            (::ul.styled {
                paddings {
                    left { "0.3rem" }
                    right { "0.3rem" }
                }
                margins { left { "5px" } }
                background {
                    color { dark }
                }
                color { secondary }
                radius { "5px" }
            }) {
                (1..5).map { li { +"List item $it" } }
            }
            h6 { +"H6: Styled O-List" }
            ol {
                (1..5).map { li { +"Numbered list item $it" } }
            }
        }
    }
}

