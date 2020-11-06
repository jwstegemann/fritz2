import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.components.flexBox
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.routing.router
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.currentTheme
import dev.fritz2.styling.theme.render
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.js.Promise
import kotlin.js.Promise.Companion.all

/*
val themes = listOf<Pair<String, ExtendedTheme>>(
    ("small Fonts") to SmallFonts(),
    ("large Fonts") to LargeFonts()
)

 */


fun HtmlElements.myLink(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "my-link",
    init: A.() -> Unit
): A =
    ::a.styled(styling, baseClass, id, prefix) {
        fontSize { giant }
    }(init)


fun HtmlElements.myRedLink(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "my-red-link",
    init: A.() -> Unit
): A =
    ::myLink.styled(styling, baseClass, id, prefix) {
        color { danger }
    }(init)


fun HtmlElements.myBorderedRedLink(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "mbrl",
    init: A.() -> Unit
): A =
    ::myRedLink.styled(styling, baseClass, id, prefix) {
        border {
            width { "1px" }
        }
    }(init)

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
            radius { "1rem" }
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

                section {
                    lineUp({
                        minHeight { "100%" }
                        height { "100%" }
                        alignItems { flexStart }
                    }) {
                        items {
                            stackUp({
                                minHeight { "100%" }
                                height { "100%" }
                                padding { "1.0rem" }
                                minWidth { "10%" }
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

                                    simpleAnchor("text")
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

                                    br {}
                                    a {
                                        href = const("https://www.fritz2.dev/")
                                        target = const("fritz2")
                                        img {
                                            src = const("https://www.fritz2.dev/images/fritz2_logo_small_white.svg")
                                            width = const(35)
                                            height = const(35)
                                        }
                                    }
                                }
                            }
                            (::div.styled(id = "content-right") {
                                paddings {
                                    all { "2.0rem" }
                                }
                            }) {

                                router.render { site ->
                                    when (site) {
                                        "icons" -> iconsDemo()
                                        "input" -> inputDemo()
                                        "buttons" -> buttonDemo()
                                        "formcontrol" -> formControlDemo()
                                        "text" -> textDemo()
                                        "flexbox" -> flexBoxDemo(store, themes, theme)
                                        "gridbox" -> gridBoxDemo()
                                        "multiselect" -> multiSelectDemo()
                                        "singleselect" -> singleSelectDemo()
                                        "stack" -> stackDemo(theme)
                                        "modal" -> modalDemo()
                                        else -> textDemo()
                                    }
                                }.bind()
                            }
                        }
                    }
                }
            }
        }
    }.mount("target")
}
