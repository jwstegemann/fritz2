import dev.fritz2.components.*
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.mount
import dev.fritz2.routing.router
import dev.fritz2.styling.params.RadiiContext
import dev.fritz2.styling.theme.renderElement
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun RenderContext.simpleLinkWithBackground(linkUri: String, linkText: String): A {
    return (::a.styled {
        fontSize { large }
        color {
            theme().colors.warning
        }
        hover {
            color {
                theme().colors.light
            }
            background { color { theme().colors.dark } }
            radius { "5%" }
        }
        paddings {
            left { "0.3rem" }
            right { "0.3rem" }
        }
    }) {
        +linkText
        href(linkUri)
    }
}

fun RenderContext.simpleAnchorWithBackground(linkText: String): A {
    return (::a.styled {
        fontSize { large }
        color {
            theme().colors.warning
        }
        hover {
            color {
                theme().colors.light
            }
            background { color { theme().colors.dark } }
        }
        radius { "5%" }
        paddings {
            left { "0.3rem" }
            right { "0.3rem" }
        }
    }) {
        +linkText
        href("#$linkText")
    }
}

fun RenderContext.simpleAnchor(linkText: String): A {
    return (::a.styled {
        fontSize { large }
        hover {
            color {
                theme().colors.warning
            }
        }
    }) {
        +linkText
        href("#$linkText")
    }
}

fun RenderContext.menuAnchor(linkText: String): Div {
    return (::div.styled {
        radius { ".5rem" }
        border {
            width { "0" }
        }
        hover {
            background {
                color { light }
            }
        }
        paddings{
            top { tiny }
            bottom { tiny }
            left { small }
            right { small }
        }
    }) {
        nonHoverAnchor("$linkText")
    }
}



fun RenderContext.nonHoverAnchor(linkText: String): A {
    return (::a.styled {
        fontSize { small }
    }) {
        +linkText
        href("#$linkText")
    }
}

@ExperimentalCoroutinesApi
fun main() {
    val themes = listOf<ExtendedTheme>(
        SmallFonts(),
        LargeFonts()
    )

    val router = router("")

    renderElement { theme: ExtendedTheme ->
        themeProvider {
            themes { themes }
            items {
                lineUp({
                    alignItems { stretch }
                    color { dark }
                    minHeight { "100%" }
                }) {
                    items {
                        stackUp({
                            padding { "1rem" }
                            minWidth { "200px" }
                            minHeight { "100%" }
                            display { flex }
                            wrap { nowrap }
                            direction { column }
                            alignItems { flexStart }
                            background { color { white } }
                            color { dark }
                            paddings {
                                top { "50px" }
                            }
                            borders {
                                right {
                                    width { "2px" }
                                    color { light }
                                }
                            }
                        }, id = "menue-left")
                        {
                            items {
                                (::p.styled {
                                    paddings {
                                        bottom { "2.0rem" }
                                    }
                                }) {
                                    simpleAnchor("Welcome")
                                }




                                menuAnchor("Flexbox")
                                menuAnchor("Gridbox")
                                menuAnchor("Stack")
                                menuAnchor("Icons")
                                menuAnchor("Spinner")
                                menuAnchor("Buttons")
                                menuAnchor("Popover")
                                menuAnchor("Modal")
                                menuAnchor("Input")
                                menuAnchor("Multiselect")
                                menuAnchor("Singleselect")
                                menuAnchor("Formcontrol")

                                (::a.styled {
                                    theme().tooltip.write("visit us on", "www.fritz2.dev"){left}()
                                    after {
                                        textAlign { center }
                                        background { color { warning } }
                                        color { dark }
                                    }
                                    paddings {
                                        top { "1.5rem" }
                                    }
                                    alignItems { end }
                                }) {
                                    href("https://www.fritz2.dev/")
                                    target("fritz2")

                                    icon({
                                        size { "3rem" }
                                        hover {
                                            color { warning }
                                        }
                                    }) {
                                        fromTheme {
                                            fritz2
                                        }
                                    }
                                }
                            }
                        }
                        (::div.styled(id = "content-right") {
                            paddings {
                                all { "2.0rem" }
                            }
                            width {
                                "100%"
                            }
                        }) {

                            // todo we might want a better flex demo
                            // todo we might want a dedicated theme demo (or use formcontrol (rename) --> all
                            //  together)
                            router.render { site ->
                                when (site) {
                                    "Icons" -> iconsDemo()
                                    "Spinner" -> spinnerDemo()
                                    "Input" -> inputDemo()
                                    "Buttons" -> buttonDemo()
                                    "Formcontrol" -> formControlDemo()
                                    "Flexbox" -> flexBoxDemo(themeStore, themes, theme)
                                    "Gridbox" -> gridBoxDemo()
                                    "Multiselect" -> multiSelectDemo()
                                    "Singleselect" -> singleSelectDemo()
                                    "Stack" -> stackDemo()
                                    "Modal" -> modalDemo()
                                    "Popover" -> popoverDemo()
                                    "Welcome" -> welcome()
                                    else -> welcome()
                                }
                            }
                        }
                    }
                }
            }
        }
    }.mount("target")
}
