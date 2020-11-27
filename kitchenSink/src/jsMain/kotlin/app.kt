import dev.fritz2.components.*
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.mount
import dev.fritz2.routing.Router
import dev.fritz2.routing.router
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.RadiiContext
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.renderElement
import dev.fritz2.styling.theme.theme
import dev.fritz2.styling.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

val componentFrame: Style<BasicParams> = { // Auslagerung von Style
    width { "100%"}
    border {
        width { thin}
        color { light }
    }
    radius { larger }
    padding{ normal }
}

fun RenderContext.componentFrame(init: Div.() -> Unit): Div { //Auslagerung von Komponente
    return (::div.styled {
        width { "100%"}
        border {
            width { thin}
            color { light }
        }
        radius { larger }
        padding{ normal }
    }){
        init()
    }
}

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


fun RenderContext.menuAnchor(linkText: String, router: Router<String>): Div {

    val selected = style ({
            width { "90%" }
            radius { normal }
            border {
                width { none }
            }
            // TODO: Hintergrund auf tertiary wenn Seite ausgewählt


            background {
                color { theme().toRGBA(primary, 0.3) }
            }

            paddings {
                top { tiny }
                bottom { tiny }
                left { small }
                right { small }
            }
        },"prefix")


    val isActive = router.map { hash ->
        console.log(hash)  //druckt in die Konsole im Browser
        hash == linkText //map den reinkommenden Wert des Flow auf einen Boolean
    }.distinctUntilChanged()

    return (::div.styled {
        width { "90%" }
        radius { normal }
        border {
            width { none }
        }
        // TODO: Hintergrund auf tertiary wenn Seite ausgewählt

        hover {
            background {
                color { theme().toRGBA(light, 0.5) }
            }
        }
        paddings{
            top { tiny }
            bottom { tiny }
            left { small }
            right { small }
        }
    }) {
        className ( selected.whenever(isActive){ it }) // der Name der StyleClass wird das zu stylende Element (in diesem Fall der Div-Container) angehängt
        nonHoverAnchor(linkText)
    }
}



fun RenderContext.nonHoverAnchor(linkText: String): A {
    return (::a.styled {
        fontSize { small }
        fontWeight { "500" }
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
                            margins {
                                top { smaller }
                            }
                            padding { "1rem" }
                            minWidth { "200px" }
                            minHeight { "100%" }
                            display { flex }
                            wrap { nowrap }
                            direction { column }
                            alignItems { flexStart }
                            background { color { base } }
                            color { dark }
                            paddings {
                                top { "50px" }
                            }
                            /*
                            borders {
                                right {
                                    width { "2px" }
                                    color { light }
                                }
                            }*/
                        }, id = "menue-left")
                        {
                            items {
                                (::p.styled {
                                    paddings {
                                        bottom { "2.0rem" }
                                    }
                                }) {
                                    menuAnchor("Welcome", router)
                                }

                                menuAnchor("Flexbox", router)
                                menuAnchor("Gridbox", router)
                                menuAnchor("Stack", router)
                                menuAnchor("Icons", router)
                                menuAnchor("Spinner", router)
                                menuAnchor("Buttons", router)
                                menuAnchor("Popover", router)
                                menuAnchor("Modal", router)
                                menuAnchor("Input", router)
                                menuAnchor("Multiselect", router)
                                menuAnchor("Singleselect", router)
                                menuAnchor("Formcontrol", router)


                                (::a.styled {
                                    theme().tooltip.write("visit us on", "www.fritz2.dev"){right}()
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
