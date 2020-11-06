import dev.fritz2.components.box
import dev.fritz2.components.gridBox
import dev.fritz2.components.icon
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.iconsDemo(): Div {

    val icons = listOf(
        theme().icons.copy,
        theme().icons.search,
        theme().icons.search2,
        theme().icons.moon,
        theme().icons.sun,
        theme().icons.add,
        theme().icons.smallAdd,
        theme().icons.settings,
        theme().icons.checkCircle,
        theme().icons.lock,
        theme().icons.unlock,
        theme().icons.view,
        theme().icons.viewOff,
        theme().icons.download,
        theme().icons.delete,
        theme().icons.repeat,
        theme().icons.repeatClock,
        theme().icons.edit,
        theme().icons.chevronLeft,
        theme().icons.chevronRight,
        theme().icons.chevronDown,
        theme().icons.chevronUp,
        theme().icons.arrowBack,
        theme().icons.arrowForward,
        theme().icons.arrowUp,
        theme().icons.arrowUpDown,
        theme().icons.arrowDown,
        theme().icons.externalLink,
        theme().icons.link,
        theme().icons.plusSquare,
        theme().icons.calendar,
        theme().icons.chat,
        theme().icons.time,
        theme().icons.arrowRight,
        theme().icons.arrowLeft,
        theme().icons.atSign,
        theme().icons.attachment,
        theme().icons.upDown,
        theme().icons.star,
        theme().icons.email,
        theme().icons.phone,
        theme().icons.dragHandle,
        theme().icons.spinner,
        theme().icons.close,
        theme().icons.smallClose,
        theme().icons.notAllowed,
        theme().icons.triangleDown,
        theme().icons.triangleUp,
        theme().icons.infoOutline,
        theme().icons.bell,
        theme().icons.info,
        theme().icons.question,
        theme().icons.questionOutline,
        theme().icons.warning,
        theme().icons.warningTwo,
        theme().icons.check,
        theme().icons.minus,
        theme().icons.hamburger,
        theme().icons.fritz2
    )

    return div {

        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            items {


                h1 {
                    +"Icons Showcase"
                }

                gridBox({
                    columns {
                        repeat(7) { "1fr" }
                    }
                    gap { normal }
                    margin { small }
                    maxWidth { "80%" }
                }) {
                    icons.forEach {
                        stackUp({
                            alignItems { center }
                            border {
                                style { solid }
                                color { light }
                                width { normal }
                            }
                            padding { small }
                            fontSize { small }
                        }) {
                            spacing { tiny }
                            items {
                                icon { fromTheme { it } }
                                p { +it.displayName }
                            }
                        }
                    }
                }
            }
        }
    }
}
