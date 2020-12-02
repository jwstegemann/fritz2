package dev.fritz2.kitchensink.demos

import dev.fritz2.components.icon
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.contentFrame
import dev.fritz2.kitchensink.base.externalLink
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.welcome(): Div {

    return contentFrame {

        stackUp({
            width { "100%" }
            justifyContent { center }
        }) {
            spacing { normal }
            items {
                icon ({
                    size { "10rem" }
                    margins {
                        top { "2.5rem" }
                    }
                    color { primary }
                }) {
                    fromTheme {fritz2}
                }
                (::p.styled {
                    fontSize { "2rem" }
                    margins {
                        top { "1.5rem" }
                        bottom { "1.5rem" }
                    }
                }) { +"fritz2 component" }
                (::p.styled {
                    fontSize { "1.5rem" }
                    fontWeight { bold }
                    margins {
                        top { "1.5rem" }
                        bottom { ".5rem" }
                    }
                }) { +"Welcome to our components demo!" }


                p { +"This demo shows the component library we added to fritz2 with the release v0.8."
                    +" Remember that this is a preview release, so some of these"
                    +" components might still need some work, while others are not implemented yet. "
                }
                p { +"At this time, our library consists of a basic set of components only."
                    +" We are working hard to improve the existing components, the styling, and the documentation -"
                    +" some new components are already in our development pipeline."
                    +" Please note that there may be changes to the api in future versions towards beta."
                }
                (::p.styled {
                    margins {
                        bottom { "3rem" }
                    }
                }) {
                    +"Your opinions and comments are very welcome. Please visit "
                    externalLink("fritz.dev", "http://fritz2.dev")
                    +" for further information, or go to our github page at "
                    externalLink("https://github.com/jwstegemann/fritz2", "https://github.com/jwstegemann/fritz2")
                    +" to open issues or check out the code."
                }

                p { +"The design of the fritz2 component library is highly inspired by "
                    externalLink("Chakra UI", "https://chakra-ui.com/")
                    +" ."
                }
            }
        }
    }
}