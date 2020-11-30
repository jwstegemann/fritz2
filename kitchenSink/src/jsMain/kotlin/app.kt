import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.mount
import dev.fritz2.routing.Router
import dev.fritz2.routing.router
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.RadiiContext
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.render
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun RenderContext.showcaseHeader(text: String) {
    (::h1.styled {
    }) { +text }
}

fun RenderContext.showcaseSection(text: String) {
    (::h3.styled {
        borders {
            left {
                width { "6px" }
                style { solid }
                color { primary }
            }
        }
        margins { top { "3rem !important" } }
        paddings { left { smaller } }
    }) { +text }
}


val componentFrame: Style<BasicParams> = { // Auslagerung von Style
    width { "100%" }
    border {
        width { thin }
        color { light }
    }
    radius { larger }
    padding { normal }
}

fun RenderContext.componentFrame(init: Div.() -> Unit): Div { //Auslagerung von Komponente
    return (::div.styled {
        width { "100%" }
        border {
            width { thin }
            color { light }
        }
        radius { larger }
        padding { normal }
    }){
        init()
    }
}

fun RenderContext.simpleLinkWithBackground(linkUri: String, linkText: String): A {
    return (::a.styled {
        fontSize { large }
        color { primary }
        hover {
            color { secondary }
            background { color { dark } }
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
        color { primary }
        hover {
            color { secondary }
            background { color { dark } }
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

fun RenderContext.navAnchor(linkText: String, href: String): Div {
    return (::div.styled {
        radius { normal }
        border {
            width { none }
        }
        hover {
            background {
                color { light_hover }
            }
        }
        paddings {
            top { tiny }
            bottom { tiny }
            left { small }
            right { small }
        }
    }){
        (::a.styled {
            fontSize { normal }
            fontWeight { semiBold }
            color { dark }
        }) {
            +linkText
            href(href)
        }
    }
}

val themes = listOf<ExtendedTheme>(SmallFonts(), LargeFonts())

object ThemeStore : RootStore<Int>(0) {
    val selectTheme = handle<Int> { _, index ->
        Theme.use(themes[index])
        index
    }
}


fun RenderContext.menuAnchor(linkText: String, router: Router<String>): Div {

    val selected = style("prefix") {
        width { "90%" }
        radius { normal }
        border {
            width { none }
        }
        background { color { secondary } }
        paddings {
            top { tiny }
            bottom { tiny }
            left { small }
            right { small }
        }
    }

    val isActive = router.data.map { hash ->
        console.log(hash)  //druckt in die Konsole im Browser
        hash == linkText //map den reinkommenden Wert des Flow auf einen Boolean
    }.distinctUntilChanged().onEach { if (it) PlaygroundComponent.update() }

    return (::div.styled {
        width { "90%" }
        radius { normal }
        border {
            width { none }
        }
        hover {
            background {
                color { light_hover }
            }
        }
        paddings {
            top { tiny }
            bottom { tiny }
            left { small }
            right { small }
        }
    }) {
        className(selected.whenever(isActive).name) // der Name der StyleClass wird das zu stylende Element (in diesem Fall der Div-Container) angehängt
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

val showcaseCode = staticStyle(
    "showcase-code", """
            padding: 2px 0.25rem;
            font-size: 0.875em;
            white-space: nowrap;
            line-height: normal;
            color: rgb(128, 90, 213);
            font-family: SFMono-Regular, Menlo, Monaco, Consolas, monospace;
    """
)

fun RenderContext.c(text: String) {
    span(showcaseCode.name) { +text }
}


@ExperimentalCoroutinesApi
fun main() {
    val router = router("")

    render(themes.first()) { theme ->
        navBar {
            brand {
                (::a.styled {
                    tooltip("visit us on", "www.fritz2.dev") { right }()
                    after {
                        textAlign { center }
                        background { color { primary } }
                        color { light }
                    }
                    alignItems { end }
                }) {
                    href("https://www.fritz2.dev/")
                    target("fritz2")

                    icon({
                        size { "2.5rem" }
                        color { primary }
                    }) { fromTheme { fritz2 } }

                    (::span.styled {
                        margins { left { normal } }
                        verticalAlign { sub }
                        fontSize { larger }
                        fontWeight { lighter }
                    }) { +"fritz2 - component library" }
                }
            }

            actions {
                lineUp {
                    items {
                        navAnchor("Documentation", "https://fritz2.dev")
                        navAnchor("API", "https://fritz2.dev")
                        navAnchor("Examples", "https://fritz2.dev")
                        navAnchor("Github", "https://fritz2.dev")
                    }
                }
            }
        }

        lineUp({
            alignItems { stretch }
            color { dark }
            minHeight { "100%" }
        }) {
            items {
                stackUp({
                    margins {
                        top { larger }
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
                            //width { "100%" }
                            paddings {
                                bottom { "2.0rem" }
                            }
                        }) {
                            menuAnchor("Welcome", router)
                        }

                        menuAnchor("flexbox", router)
                        menuAnchor("gridbox", router)
                        menuAnchor("stack", router)
                        menuAnchor("icons", router)
                        menuAnchor("spinner", router)
                        menuAnchor("buttons", router)
                        menuAnchor("popover", router)
                        menuAnchor("modal", router)
                        menuAnchor("input", router)
                        menuAnchor("checkboxes", router)
                        menuAnchor("radios", router)
                        menuAnchor("switch", router)
                        menuAnchor("formcontrol", router)
                        menuAnchor("datatable", router)
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
                    router.data.render { site ->
                        when (site) {
                            "icons" -> iconsDemo()
                            "spinner" -> spinnerDemo()
                            "input" -> inputDemo()
                            "buttons" -> buttonDemo()
                            "formcontrol" -> formControlDemo()
                            "flexbox" -> flexBoxDemo(theme)
                            "gridbox" -> gridBoxDemo()
                            "checkboxes" -> checkboxesDemo()
                            "radios" -> radiosDemo()
                            "switch" -> switchDemo()
                            "stack" -> stackDemo()
                            "modal" -> modalDemo()
                            "popover" -> popoverDemo()
                            "welcome" -> welcome()
                            "datatable" -> tableDemo()
                            else -> welcome()
                        }
                    }
                }
            }
        }
    }.mount("target")
}
