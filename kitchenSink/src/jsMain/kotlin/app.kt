import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.mount
import dev.fritz2.routing.router
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.render
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun RenderContext.simpleLinkWithBackground(linkUri: String, linkText: String): A {
    return (::a.styled {
        fontSize { large }
        color { warning }
        hover {
            color { light }
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
        color { warning }
        hover {
            color { light }
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

fun RenderContext.simpleAnchor(linkText: String): A {
    return (::a.styled {
        fontSize { large }
        hover {
            color { warning }
        }
    }) {
        +linkText
        href("#$linkText")
    }
}

val themes = listOf<ExtendedTheme>(SmallFonts(), LargeFonts())

object ThemeStore : RootStore<Int>(0) {
    val selectTheme = handle<Int> { _, index ->
        Theme.use(themes[index])
        index
    }
}


@ExperimentalCoroutinesApi
fun main() {
    val router = router("")

    render(themes.first()) { theme ->
        lineUp({
            alignItems { stretch }
            color { dark }
            minHeight { "100%" }
        }) {
            items {
                stackUp({
                    padding { "1.0rem" }
                    minWidth { "200px" }
                    minHeight { "100%" }
                    display { flex }
                    wrap { nowrap }
                    direction { column }
                    alignItems { flexEnd }
                    background { color { dark } }
                    color { light }
                    paddings {
                        top { "50px" }
                    }
                }, id = "menue-left")
                {
                    items {
                        (::p.styled {
                            paddings {
                                bottom { "2.0rem" }
                            }
                        }) {
                            simpleAnchor("welcome")
                        }

                        simpleAnchor("flexbox")
                        simpleAnchor("gridbox")
                        simpleAnchor("stack")
                        simpleAnchor("icons")
                        simpleAnchor("spinner")
                        simpleAnchor("buttons")
                        simpleAnchor("popover")
                        simpleAnchor("modal")
                        simpleAnchor("input")
                        simpleAnchor("multiselect")
                        simpleAnchor("singleselect")
                        simpleAnchor("formcontrol")
                        simpleAnchor("datatable")

                        (::a.styled {
                            tooltip("visit us on", "www.fritz2.dev") { left }()
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
                    router.data.render { site ->
                        when (site) {
                            "icons" -> iconsDemo()
                            "spinner" -> spinnerDemo()
                            "input" -> inputDemo()
                            "buttons" -> buttonDemo()
                            "formcontrol" -> formControlDemo()
                            "flexbox" -> flexBoxDemo(theme)
                            "gridbox" -> gridBoxDemo()
                            "multiselect" -> multiSelectDemo()
                            "singleselect" -> singleSelectDemo()
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
