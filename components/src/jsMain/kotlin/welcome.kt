import dev.fritz2.binding.RootStore
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.theme.theme
import dev.fritz2.tracking.tracker
import kotlinx.browser.window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun HtmlElements.welcome(): Div {


    return div {
        stackUp({
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

                }) {

                    p {
                        (::h1.styled {
                            margins{
                                bottom { "1.0rem" }
                            }
                        }) { +"Welcome to the fritz2 components demo" }

                        +"This demo showcases the components we added so far. Remember that this is a snapshot, so some "
                        +"components might still need some work while others are missing. We are working on it!"

                        (::h3.styled {
                            margins{
                                bottom { "0.8rem" }
                                top { "1.0rem" }
                            }
                        }) { +"Hoping for your feedback!" }

                        (::p.styled {
                            margins{
                                all { "1.0rem" }
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
                            +" to open issues or check out the code. Now, let's start with our text elements:"
                        }
                    }

                }

                h1 { +"H1: Text Elements Showcase" }
                p { +"These are some examples of text elements." }
                h2 { +"H2: Paragraph" }
                p { +"Paragraph: Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat!" }
                h3 { +"H3: Paragraph with inner Span" }
                p {
                    +": Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? Cum doloribus amet vel? "
                    (::span.styled {
                        background {
                            color { light }
                        }
                        fontStyle { italic }
                        paddings {
                            left { "0.3rem" }
                            right { "0.3rem" }
                        }
                    }) { +"Span integrated into Paragraph." }
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
                ul {
                    (1..5).map { li { +"List item $it" } }
                }
                h6 { +"H6: O-List" }
                ol {
                    (1..5).map { li { +"Numbered list item $it" } }
                }
            }
        }
    }
}
