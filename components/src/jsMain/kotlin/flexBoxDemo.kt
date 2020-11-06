import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.watch
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.render
import dev.fritz2.dom.selectedIndex
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun HtmlElements.flexBoxDemo(themeStore: ThemeStore, themes: List<ExtendedTheme>, theme: ExtendedTheme): Div {

    return div {
        div {


            flexBox({
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
                minHeight { "80%" }
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
                    (::img.styled {
                        width(sm = { small }, md = { smaller })
                        boxShadow { flat }
                        radius { large }
                    }) {
                        src = const("https://www.fritz2.dev/images/fritz_info_1.jpg")
                        alt = const("Random image for flex layout demonstration")
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
                    (::p.styled { theme.teaserText }) { +"Teaser texts can be styled as well" }
//                    p {
//                        // TODO: Way too complicated - needs to get some convenient API! (But how?)
//                        className = themeStore.data.map { i -> staticStyle("foo", themes[i].teaserText).name }
//                        +"Marketing"
//                    }
                    (::h1.styled { theme.sizes.large }) { +"Flex Layouts Showcase" }
                    (::p.styled {
                        paddings {
                            all { "0.8rem" }
                            left { "0" }
                        }

                    }) {
                        +"Flex layouts provide a better of using space on websites and handle containers of unknown sizes. While we showcase our flex layout, let's use the opportunity to also show off theme selection with this small example:"
                    }
                    (::p.styled {
                        paddings {
                            all { "0.8rem" }
                            left { "0" }
                        }
                    }) {
                        themeStore.data.map { currentThemeIndex ->
                            radioGroup {
                                items { themes.map { it.name } }
                                selected { themes[currentThemeIndex].name }
                                radioSize { normal }
                            }.map { selected ->
                                themes.indexOf(
                                    themes.find {
                                        selected == it.name
                                    }
                                )
                            } handledBy themeStore.selectTheme
                        }.watch()
                    }
                }
            }

        }
    }
}
