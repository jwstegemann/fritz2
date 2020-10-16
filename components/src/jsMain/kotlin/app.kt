import dev.fritz2.components.anchor
import dev.fritz2.components.flexBox
import dev.fritz2.dom.html.render
import dev.fritz2.dom.mount
import dev.fritz2.routing.router
import dev.fritz2.styling.theme.currentTheme
import dev.fritz2.styling.theme.render
import kotlinx.coroutines.ExperimentalCoroutinesApi

val themes = listOf<Pair<String, ExtendedTheme>>(
    ("small Fonts") to SmallFonts(),
    ("large Fonts") to LargeFonts()
)

@ExperimentalCoroutinesApi
fun main() {
    currentTheme = themes.first().second

    val router = router("")

    render { theme: ExtendedTheme ->
        section {
            flexBox {
                height { "60px" }
                wrap { nowrap }
                direction { row }
                justifyContent { spaceEvenly }
                alignItems { center }

                hugo {
                    anchor {
                        // TODO: Ist noch hässlich!
                        href.invoke("#")
                        hugo {
                            +"Components"
                        }
                    }
                }
            }
            router.render { site ->
                when (site) {
//                    "grid" -> gridDemo()
//                    "input" -> inputDemo()
//                    "buttons" -> buttonDemo(theme)
//                    "formcontrol" -> formControlDemo()
                    else -> componentDemo(theme)
                }
            }.bind()
        }
    }.mount("target")
}
