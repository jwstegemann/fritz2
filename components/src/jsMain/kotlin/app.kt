import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.dom.selectedIndex
import dev.fritz2.routing.router
import dev.fritz2.styling.params.AreaName
import dev.fritz2.styling.params.end
import dev.fritz2.styling.params.rgba
import dev.fritz2.styling.params.start
import dev.fritz2.styling.theme.currentTheme
import dev.fritz2.styling.theme.render
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
            flex({
                height { "60px" }
                wrap { nowrap }
                direction { row }
                justifyContent { spaceEvenly }
                alignItems { center }
            }) {
                link({
                    flex {
                        //grow { "2" }
                        //order { "1" }
                        //alignSelf { flexStart }
                    }
                }) {
                    href = const("#")
                    +"flex"
                }
                link {
                    href = const("#grid")
                    +"grid"
                }
            }
            router.render { site ->
                when (site) {
                    "grid" -> gridDemo()
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
                sel {
                    value = themeStore.data.map { i -> themes[i].first }
                    themes.forEach {
                        option { +it.first }
                    }

                    changes.selectedIndex() handledBy themeStore.selectTheme
                }
                flex({
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
                    box({
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
                    box({
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
                        text(theme.teaserText) { +"Marketing" }
                        link({
                            margins { top { tiny } }
                            fontSize { normal }
                            lineHeight { normal }
                            fontWeight { bold }
                        }) {
                            href = const("#")
                            +"Finding customers for your new business"
                        }
                        text({
                            margins { top { smaller } }
                            color { dark }
                        }) {
                            +"Getting a new business off the ground is a lot of hard work. Here are five ideas you can use to find your first customers."
                        }
                    }
                    box {
                        Button() {
                            Icon(
                                def = IconDefinition(
                                    "test",
                                    path = "M12 4C12.2652 4 12.5196 4.10536 12.7071 4.29289L18.7071 10.2929C19.0976 10.6834 19.0976 11.3166 18.7071 11.7071C18.3166 12.0976 17.6834 12.0976 17.2929 11.7071L13 7.41421L13 19C13 19.5523 12.5523 20 12 20C11.4477 20 11 19.5523 11 19L11 7.41421L6.70711 11.7071C6.31658 12.0976 5.68342 12.0976 5.29289 11.7071C4.90237 11.3166 4.90237 10.6834 5.29289 10.2929L11.2929 4.29289C11.4804 4.10536 11.7348 4 12 4Z"
                                )
                            )

                            +"Normal"
                        }
                        Button({
                            margins { left { small } }
                        },
                            variant = { outline }
                        ) {
                            +"Outline"
                        }
                        Button(
                            {
                                margins { left { small } }
                            },
                            variant = { ghost },
                            color = theme.colors.info
                        ) {
                            +"Ghost"
                        }
                        Button({
                            margins { left { small } }
                        },
                            variant = { link }
                        ) {
                            +"Link"
                        }

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
        grid({
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
            box({
                grid { area { grid.HEADER } }
                //bgColor { "green" }
                background {
                    color { "lime" }
                }
            }) {
                text { +"Header" }
            }
            box({
                grid { area { grid.SIDEBAR } }
                background { color { "yellow" } }
            }) {
                text { +"Sidebar" }
            }
            box({
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
                text { +"Content" }
            }
            box({
                grid { area { grid.FOOTER } }
                background { color { "lime" } }
            }) {
                text { +"Footer" }
            }
            box({
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
                text { +"Overlay" }
            }
        }
    }
}
