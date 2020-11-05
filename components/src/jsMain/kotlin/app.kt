import dev.fritz2.binding.const
import dev.fritz2.components.*
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.routing.router
import dev.fritz2.styling.params.AlignItemsValues
import dev.fritz2.styling.theme.render
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

//fun HtmlElements.myLink(
//    styling: BasicParams.() -> Unit = {},
//    baseClass: StyleClass? = null,
//    id: String? = null,
//    prefix: String = "my-link",
//    init: A.() -> Unit
//): A =
//    ::a.styled(styling, baseClass, id, prefix) {
//        fontSize { giant }
//    }(init)
//
//
//fun HtmlElements.myRedLink(
//    styling: BasicParams.() -> Unit = {},
//    baseClass: StyleClass? = null,
//    id: String? = null,
//    prefix: String = "my-red-link",
//    init: A.() -> Unit
//): A =
//    ::myLink.styled(styling, baseClass, id, prefix) {
//        color { danger }
//    }(init)
//
//
//fun HtmlElements.myBorderedRedLink(
//    styling: BasicParams.() -> Unit = {},
//    baseClass: StyleClass? = null,
//    id: String? = null,
//    prefix: String = "mbrl",
//    init: A.() -> Unit
//): A =
//    ::myRedLink.styled(styling, baseClass, id, prefix) {
//        border {
//            width { "1px" }
//        }
//    }(init)

fun HtmlElements.simpleLinkWithBackground(linkUri: String, linkText: String): A {
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
        href = const(linkUri)
    }
}

fun HtmlElements.simpleAnchorWithBackground(linkText: String): A {
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
        href = const("#$linkText")
    }
}

fun HtmlElements.simpleAnchor(linkText: String): A {
    return (::a.styled {
        fontSize { large }
        hover {
            color {
                theme().colors.warning
            }
        }
    }) {
        +linkText
        href = const("#$linkText")
    }
}

@ExperimentalCoroutinesApi
fun main() {
    val themes = listOf<ExtendedTheme>(
        SmallFonts(),
        LargeFonts()
    )

    val router = router("")

    render { theme: ExtendedTheme ->
        themeProvider {
            // grab the ThemeStore to pass it down towards components, that need to access either the
            // data or to change the selected theme
            val store = themeStore
            // could be the default? (So only need to set it to ``false`` actively?)
            resetCss { true }
            // Override default theme by a new list of themes
            themes { themes }
            items {
                lineUp({
                    alignItems { stretch }
                    color { dark }
                }) {
                    items {
                        stackUp({
                            padding { "1.0rem" }
                            minWidth { "200px" }
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
                                simpleAnchor("icons")
                                simpleAnchor("input")
                                simpleAnchor("multiselect")
                                simpleAnchor("singleselect")
                                simpleAnchor("formcontrol")
                                simpleAnchor("stack")
                                simpleAnchor("buttons")
                                simpleAnchor("modal")

                                (::a.styled {
                                    paddings {
                                        top { "1.5rem" }
                                    }
                                    alignItems { end }
                                }) {
                                    href = const("https://www.fritz2.dev/")
                                    target = const("fritz2")
                                    title = const("fritz2.dev")

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
                                    "icons" -> iconsDemo()
                                    "input" -> inputDemo()
                                    "buttons" -> buttonDemo()
                                    "formcontrol" -> formControlDemo()
                                    // textdemo currently on welcome page, copied, not called
                                    "flexbox" -> flexBoxDemo(store, themes, theme)
                                    "gridbox" -> gridBoxDemo()
                                    "multiselect" -> multiSelectDemo()
                                    "singleselect" -> singleSelectDemo()
                                    "stack" -> stackDemo(theme)
                                    "modal" -> modalDemo()
                                    "welcome" -> welcome()
                                    else -> welcome()
                                }
                            }.bind()
                        }
                    }
                }
            }
        }
    }.mount("target")
}
