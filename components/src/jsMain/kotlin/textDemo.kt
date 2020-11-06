import dev.fritz2.binding.const
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.textDemo(): Div {

    return div {
        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            items {

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
                    }) { +" Span integrated into Paragraph. " }
                    +"Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat!"
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
