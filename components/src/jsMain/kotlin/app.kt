import dev.fritz2.binding.const
import dev.fritz2.components.Flex
import dev.fritz2.components.Link
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
                Link {
                    href = const("#buttons")
                    +"buttons"
                }

            }
            router.render { site ->
                when (site) {
                    "grid" -> gridDemo()
                    "input" -> inputDemo()
                    "buttons" -> buttonDemo(theme)
                    else -> flexDemo(theme)
                }
            }.bind()
        }
    }.mount("target")
}
