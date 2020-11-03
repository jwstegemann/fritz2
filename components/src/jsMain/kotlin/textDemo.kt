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
                h1 { +"H1 Heading" }
                h2 { +"H2 Heading" }
                h3 { +"H3 Heading" }
                h4 { +"H4 Heading" }
                h5 { +"H5 Heading" }
                h6 { +"H6 Heading" }
                p { +"Paragraph: Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel? Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat!" }
                p {
                    +"Span: Lorem ipsum dolor sit amet consectetur adipisicing elit. Cum doloribus amet vel?"
                    (::span.styled {
                        background {
                            color { light }
                        }
                        fontStyle { italic }
                    }) { +" Span is integrated into a container like a <p> as in this case. " }
                    +" Expedita sit praesentium dolores obcaecati possimus sapiente voluptatem doloribus, ipsum harum in quia, provident corporis nulla corrupti placeat!"
                }
                p {
                    +"Hier folgt ein "
                    a {
                        +"Link"
                        const("#nowhere")
                    }
                }
                ul {
                    (1..5).map { li { +"Item $it" } }
                }
                ol {
                    (1..5).map { li { +"Item $it" } }
                }
            }
        }
    }
}
