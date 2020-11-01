import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.selectedIndex
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun HtmlElements.flexBoxDemo(themeStore: ThemeStore, themes: List<ExtendedTheme>, theme: ExtendedTheme): Div {

    return div {
        div {
            singleSselect {
                value = themeStore.data.map { i -> themes[i].name }
                themes.forEach {
                    option { +it.name }
                }

                changes.selectedIndex() handledBy themeStore.selectTheme
            }
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
                        width(sm = { normal }, md = { smaller })
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
                    (::p.styled { theme.teaserText }) { +"Marketing" }
//                    p {
//                        // TODO: Way too complicated - needs to get some convenient API! (But how?)
//                        className = themeStore.data.map { i -> staticStyle("foo", themes[i].teaserText).name }
//                        +"Marketing"
//                    }
                    (::a.styled {
                        margins { top { tiny } }
                        fontSize { normal }
                        lineHeight { normal }
                        fontWeight { bold }
                    }) {
                        href = const("#")
                        +"Finding customers for your new business"
                    }
                    (::p.styled {
                        margins { top { smaller } }
                        color { dark }
                    }) {
                        +"Getting a new business off the ground is a lot of hard work. Here are five ideas you can use to find your first customers."
                    }
                }
            }
        }
    }
}
