import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.html.*
import dev.fritz2.dom.mount
import dev.fritz2.dom.selectedIndex
import dev.fritz2.dom.values
import dev.fritz2.routing.router
import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.currentTheme
import dev.fritz2.styling.theme.render
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

val themes = listOf<Pair<String, ExtendedTheme>>(
    ("small Fonts") to SmallFonts(),
    ("large Fonts") to LargeFonts()
)

@ExperimentalCoroutinesApi
fun main() {
    currentTheme = themes.first().second

    val router = router("")

    render { theme: ExtendedTheme ->
        console.log("####" + (currentTheme is ExtendedTheme))
        section {
            Flex({
                height { "60px" }
                wrap { nowrap }
                direction { row }
                justifyContent { spaceEvenly }
                alignItems { center }
            }) {
                Link({
                    flex {
                        //grow { "2" }
                        //order { "1" }
                        //alignSelf { flexStart }
                    }
                }) {
                    href = const("#")
                    +"flex"
                }
                Link {
                    href = const("#grid")
                    +"grid"
                }
                Link {
                    href = const("#input")
                    +"input"
                }
            }
            router.render { site ->
                when (site) {
                    "grid" -> gridDemo()
                    "input" -> inputDemo()
                    else -> flexDemo(theme)
                }
            }.bind()
        }
    }.mount("target")
}

@ExperimentalCoroutinesApi
fun HtmlElements.flexDemo(theme: ExtendedTheme): Div {

    val themeStore = object : RootStore<Int>(0) {
        val selectTheme = handle<Int> { _, index ->
            currentTheme = themes[index].second
            index
        }
    }

    return div {
        themeStore.data.map {
            div {
                Select {
                    value = themeStore.data.map { i -> themes[i].first }
                    themes.forEach {
                        option { +it.first }
                    }

                    changes.selectedIndex() handledBy themeStore.selectTheme
                }
                Flex({
                    margin { small }
                    padding { small }
                    border {
                        style { solid }
                        width { thin }
                        color { light }
                    }
                    radius { tiny }
                    boxShadow { flat }
                    direction(sm = { column }, md = { row })
                }) {
                    Box({
                        zIndex { layer(1) }
                        margins(
                            {
                                top { small }
                                bottom { large }
                            },
                            md = { left { normal } }
                        )
                        flex { shrink { "0" } }
                    }) {
                        image({
                            width(sm = { normal }, md = { tiny })
                            boxShadow { flat }
                            radius { large }
                        }) {
                            src = const("https://bit.ly/2jYM25F")
                            alt = const("Woman paying for a purchase")
                        }
                    }
                    Box({
                        zIndex { base }
                        //width { "300px" }
                        margins(
                            {
                                top { small }
                                bottom { large }
                            },
                            md = { left { normal } }
                        )
                    }) {
                        Text(theme.teaserText) { +"Marketing" }
                        Link({
                            margins { top { tiny } }
                            fontSize { normal }
                            lineHeight { normal }
                            fontWeight { bold }
                        }) {
                            href = const("#")
                            +"Finding customers for your new business"
                        }
                        Text({
                            margins { top { smaller } }
                            color { dark }
                        }) {
                            +"Getting a new business off the ground is a lot of hard work. Here are five ideas you can use to find your first customers."
                        }
                    }
                    LineUp {
                        Button() {
                            Icon(theme.icons.arrowUp)
                            +"Normal"
                        }
                        Button(variant = { outline }) { +"Outline" }
                        Button(variant = { ghost }, color = theme.colors.info) { +"Ghost" }
                        Button(variant = { link }) { +"Link" }

                    }
                }
            }
        }.bind()
    }
}

@ExperimentalCoroutinesApi
fun HtmlElements.gridDemo(): Div {
    // example from https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout/Layout_using_Named_Grid_Lines
    return div {
        val grid = object {
            val HEADER: AreaName = "header"
            val SIDEBAR: AreaName = "sidebar"
            val CONTENT: AreaName = "content"
            val FOOTER: AreaName = "footer"
        }
        Grid({
            fontSize { normal }
            columns {
                repeat(9) { "9fr" }
                //repeat(autoFill) { "9fr" }
            }

            autoRows {
                minmax("100px", auto)
                //minmax(minContent, auto)
                //minmax("100px", auto)
            }

            areas(
                sm = {
                    with(grid) {
                        row(HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER)
                        row(SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR, SIDEBAR)
                        row(CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT)
                        row(FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER)
                    }
                },
                md = {
                    with(grid) {
                        row(HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER, HEADER)
                        row(SIDEBAR, SIDEBAR, SIDEBAR, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT, CONTENT)
                        row(FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER, FOOTER)
                    }
                }
            )
            gap { large }
            //autoFlow { dense }
            //raw("place-items: stretch flex-end;")
            //justifyContent { spaceEvenly }
            //alignItems { start }
        }) {
            Box({
                grid { area { grid.HEADER } }
                //bgColor { "green" }
                background {
                    color { "lime" }
                }
            }) {
                Text { +"Header" }
            }
            Box({
                grid { area { grid.SIDEBAR } }
                background { color { "yellow" } }
            }) {
                Text { +"Sidebar" }
            }
            Box({
                grid(sm = { area { grid.CONTENT } })
                background(
                    sm = {
                        image { "https://via.placeholder.com/150/?text=Klein" }
                        repeat { repeat }
                        positions {
                            horizontal { center }
                            vertical { center }
                        }
                        /*
                        sizes {
                            horizontal { "100%" }
                            vertical { "80%" }
                        }
                         */
                    },
                    md = {
                        image { "https://via.placeholder.com/300/A9A9A9?text=Gross" }
                        repeat { repeat }
                        positions {
                            horizontal { center }
                            vertical { center }
                        }
                    }
                )
            }) {
                Text { +"Content" }
            }
            Box({
                grid { area { grid.FOOTER } }
                background { color { "lime" } }
            }) {
                Text { +"Footer" }
            }
            Box({
                margin { normal }
                grid(
                    sm = {
                        row {
                            start { grid.HEADER.start }
                            //end { grid.FOOTER.end }
                            //end { span(grid.SIDEBAR) }
                            end { grid.CONTENT.end }
                        }
                        column {
                            start { grid.CONTENT.start }
                            end { grid.CONTENT.end }
                        }
                    },
                    md = {
                        row {
                            start { grid.HEADER.start }
                            end { span(2) }
                        }
                        column {
                            start { grid.CONTENT.start }
                            end { grid.CONTENT.end }
                        }
                    }
                )
                //bgColor { "rgba(255, 0, 0, 0.5)" }
                background {
                    //blendMode { darken }
                    color { rgba(255, 0, 0, 0.5) }
                }
            }) {
                Text { +"Overlay" }
            }
        }
    }
}

@ExperimentalCoroutinesApi
fun HtmlElements.inputDemo(): Div {

    val user = storeOf("John Doe")

    return div {
        Flex({
            direction { column }
            padding { normal }
        }) {
            h1 { +"Input Showcase" }

            Text { +"Basic" }
            Input {
                placeholder = const("Placeholder")
            }

            Text { +"Basic + Readonly + Custom Styling" }
            Input(
                {
                    //background { color { "lightgrey" } }
                    focus {
                        border {
                            color { dark }
                        }
                        boxShadow { none }
                    }
                },
                type = { text }
            ) {
                value = const("Readonly!")
                readOnly = const(true)
            }


            Text { +"Password" }
            Input(
                type = { password }
            ) {
                placeholder = const("Password")
            }

            Text { +"Basic + Store" }
            Input(store = user) {
                placeholder = const("Name")
            }
            Text { +"changes manually applied to store's update" }
            Input {
                placeholder = const("Name")
                changes.values() handledBy user.update
            }
            LineUp(theme().space.tiny,
                styles = {
                    margins { vertical { tiny } }
                }
            ) {
                Text {
                    +"given Name:"
                }
                Text({
                    background { color { "lightgrey" } }
                    radius { normal }
                    paddings { horizontal { tiny } }
                }) {
                    user.data.bind()
                }
            }

            Text { +"Sizes" }
            Input(size = { large }) {
                placeholder = const("large")
            }
            Input(size = { normal }) {
                placeholder = const("normal")
            }
            Input(size = { small }) {
                placeholder = const("small")
            }

            Text { +"Variants" }
            Input(variant = { outline }) {
                placeholder = const("outline")
            }
            Input(variant = { filled }) {
                placeholder = const("filled")
            }
        }
    }
}