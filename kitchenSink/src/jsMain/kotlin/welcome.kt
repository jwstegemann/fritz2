import dev.fritz2.components.icon
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.params.styled
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.welcome(): Div {

    return contentFrame {

        stackUp({
        }) {
            spacing { normal }
            items {
                icon({
                    size { "10rem" }
                    margins {
                        top { "2.5rem" }
                    }
                    color { primary }
                }) {
                    fromTheme { fritz2 }
                }
                (::p.styled {
                    fontSize { "2rem" }
                    margins {
                        top { "1.5rem" }
                        bottom { "1.5rem" }
                    }
                }) { +"fritz2 component library" }
                (::p.styled {
                    fontSize { "1.5rem" }
                    fontWeight { bold }
                    margins {
                        top { "1.5rem" }
                        bottom { ".5rem" }
                    }
                }) { +"components tailored to fritz2" }


                paragraph {
                    +"""
            This is a set of ready to go components to build reactive themable web-apps using fritz2
            following a consistent design system.
            """.trimIndent()
                }

                paragraph {
                    +"""
            While the components can be used out of the box with the default theme they are designed to be
            customizable and can easily be styled and composed to fit your needs.
            """.trimIndent()
                }

                warningBox {
                    +" Remember that this is a preview release."
                    +" Most components still need some work others are not implemented yet and the api has to be completed and streamlined."
                }

                (::p.styled {
                    margins {
                        bottom { "3rem" }
                    }
                }) {
                    +"Your opinions and comments are very welcome. Please visit "
                    a {
                        href("http://fritz2.dev")
                        +"fritz.dev"
                    }
                    +" for further information, or go to our github page at "
                    a {
                        href("https://github.com/jwstegemann/fritz2")
                        +"https://github.com/jwstegemann/fritz2"
                    }
                    +" to open issues and check out the code."
                }

                p {
                    +"The design and concepts of the fritz2 component library is highly inspired by the one and only"
                    a {
                        href("https://chakra-ui.com/")
                        +"Chakra UI"
                    }
                    +" ."
                }
            }
        }
    }

}