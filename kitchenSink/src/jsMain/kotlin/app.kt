import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.P
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.mount
import dev.fritz2.routing.Router
import dev.fritz2.routing.router
import dev.fritz2.styling.name
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.render
import dev.fritz2.styling.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun RenderContext.showcaseHeader(text: String) {
    (::h1.styled {
        fontFamily { "Inter, sans-serif" }
        margins {
            top { "2rem" }
            bottom { ".25rem" }
        }
        lineHeight { Theme().lineHeights.tiny }
        fontWeight { "700" }
        fontSize { Theme().fontSizes.huge }
        letterSpacing { Theme().letterSpacings.small }
    }) { +text }
}

fun RenderContext.showcaseSubHeader(text: String) {
    (::h2.styled {
        fontFamily { "Inter, sans-serif" }
        margins {
            top { "4rem" }
            bottom { ".5rem" }
        }
        lineHeight { Theme().lineHeights.small }
        fontWeight { "600" }
        fontSize { Theme().fontSizes.larger }
        letterSpacing { Theme().letterSpacings.small }
    }) { +text}
}

fun RenderContext.showcaseSection(text: String) {
    (::h3.styled {
        fontFamily { "Inter, sans-serif" }
        lineHeight { Theme().lineHeights.smaller }
        fontWeight { "600" }
        fontSize { Theme().fontSizes.large }
        letterSpacing { Theme().letterSpacings.small }

        borders {
            left {
                width { "6px" }
                style { solid }
                color { primary }
            }
        }
        radii { left { small } }
        margins { top { "3rem !important" } }
        paddings { left { smaller } }
    }) { +text }
}

fun RenderContext.paragraph(init: P.() -> Unit): P {
    return (::p.styled {
        fontFamily { "Inter, sans-serif" }
        margins {
            top { "1.25rem" }
        }
        lineHeight { Theme().lineHeights.larger }
        fontWeight { "400" }
        fontSize { Theme().fontSizes.normal }
        letterSpacing { Theme().letterSpacings.small }
    })  {
        init()
    }
}

fun RenderContext.contentFrame(init: Div.() -> Unit): Div {
    return (::div.styled {
        margins {
            top { "2rem" }
        }
        maxWidth { "48rem" }
        paddings {
            top { huge }
            left { normal }
        }
    }){
        init()
    }
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
        margins {
            top { "1.25rem" }
        }
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

val welcome_ = "Welcome"
val icons_ = "Icons"
val spinner_ = "Spinner"
val input_ = "Input"
val buttons_ = "Buttons"
val formcontrol_ = "Formcontrol"
val flexbox_ = "Flexbox"
val gridbox_ = "Gridbox"
val checkboxes_ = "Checkboxes"
val radios_ = "Radios"
val switch_ = "Switch"
val stack_ = "Stack"
val modal_ = "Modal"
val popover_ = "Popover"
val datatable_ = "Datatable"


object ThemeStore : RootStore<Int>(0) {
    val selectTheme = handle<Int> { _, index ->
        Theme.use(themes[index])
        index
    }
}

fun RenderContext.menuHeader(init: P.() -> Unit): P {
    return (::p.styled {
        paddings {
            top { large }
            left { small }
            right { small }
        }
        fontSize{small}
        fontWeight { "700" }
        color { tertiary }
    })  {
        init()
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
                }, id = "menue-left")
                {
                    spacing{tiny}
                    items {
                        (::p.styled {
                            width { "100%" }
                            margins { top{ huge } }
                            paddings {
                                bottom { "1rem" }
                            }
                        }) {
                            menuAnchor(welcome_, router)
                        }
                        menuHeader { +"LAYOUT" }
                        menuAnchor(flexbox_, router)
                        menuAnchor(gridbox_, router)
                        menuAnchor(stack_, router)

                        menuHeader { +"FORMS" }
                        menuAnchor(buttons_, router)
                        menuAnchor(checkboxes_, router)
                        menuAnchor(formcontrol_, router)
                        menuAnchor(input_, router)
                        menuAnchor(radios_, router)
                        menuAnchor(switch_, router)
                        menuAnchor(datatable_, router)

                        menuHeader { +"FEEDBACK" }
                        menuAnchor(spinner_, router)

                        menuHeader { +"OVERLAY" }
                        menuAnchor(modal_, router)
                        menuAnchor(popover_, router)

                        menuHeader { +"ICONS" }
                        menuAnchor(icons_, router)
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
                            icons_ -> iconsDemo()
                            spinner_ -> spinnerDemo()
                            input_ -> inputDemo()
                            buttons_ -> buttonDemo()
                            formcontrol_ -> formControlDemo()
                            flexbox_ -> flexBoxDemo(theme)
                            gridbox_ -> gridBoxDemo()
                            checkboxes_ -> checkboxesDemo()
                            radios_ -> radiosDemo()
                            switch_ -> switchDemo()
                            stack_ -> stackDemo()
                            modal_ -> modalDemo()
                            popover_ -> popoverDemo()
                            welcome_ -> welcome()
                            datatable_ -> tableDemo()
                            else -> welcome()
                        }
                    }
                }
            }
        }
    }.mount("target")
}
