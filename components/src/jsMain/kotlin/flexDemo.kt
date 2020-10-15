import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.selectedIndex
import dev.fritz2.styling.theme.currentTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

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
                f2Flex {
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
                }.apply {
                    f2Box {
                        zIndex { layer(1) }
                        margins(
                            {
                                top { small }
                                bottom { large }
                            },
                            md = { left { normal } }
                        )
                        flex { shrink { "0" } }
                    }.apply {
                        f2Image {
                            width(sm = { normal }, md = { tiny })
                            boxShadow { flat }
                            radius { large }
                        }.apply {
                            src = const("https://bit.ly/2jYM25F")
                            alt = const("Woman paying for a purchase")
                        }
                    }

                    f2Box {
                        zIndex { base }
                        //width { "300px" }
                        margins(
                            {
                                top { small }
                                bottom { large }
                            },
                            md = { left { normal } }
                        )
                    }.apply {
                        f2Text { theme.teaserText }.apply { +"Marketing" }
                        Link {
                            margins { top { tiny } }
                            fontSize { normal }
                            lineHeight { normal }
                            fontWeight { bold }
                        }.apply {
                            href = const("#")
                            +"Finding customers for your new business"
                        }
                        f2Text {
                            margins { top { smaller } }
                            color { dark }
                        }.apply {
                            +"Getting a new business off the ground is a lot of hard work. Here are five ideas you can use to find your first customers."
                        }
                    }
                }
            }
        }.bind()
    }
}

