import dev.fritz2.components.gridBox
import dev.fritz2.components.icon
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.iconsDemo(): Div {

    val icons = listOf(
        theme().icons.add,
        theme().icons.all,
        theme().icons.archive,
        theme().icons.arrowDown,
        theme().icons.arrowLeftDown,
        theme().icons.arrowLeftUp,
        theme().icons.arrowLeft,
        theme().icons.arrowRightDown,
        theme().icons.arrowRightUp,
        theme().icons.arrowRight,
        theme().icons.arrowUp,
        theme().icons.attachment,
        theme().icons.ban,
        theme().icons.barChartAlt,
        theme().icons.barChart,
        theme().icons.board,
        theme().icons.book,
        theme().icons.bookmark,
        theme().icons.calendar,
        theme().icons.call,
        theme().icons.camera,
        theme().icons.caretDown,
        theme().icons.caretLeft,
        theme().icons.caretRight,
        theme().icons.caretUp,
        theme().icons.check,
        theme().icons.chevronDoubleDown,
        theme().icons.chevronDoubleLeft,
        theme().icons.chevronDoubleRight,
        theme().icons.chevronDoubleUp,
        theme().icons.chevronDown,
        theme().icons.chevronLeft,
        theme().icons.chevronRight,
        theme().icons.chevronUp,
        theme().icons.circleAdd,
        theme().icons.circleArrowDown,
        theme().icons.circleArrowLeft,
        theme().icons.circleArrowRight,
        theme().icons.circleArrowUp,
        theme().icons.circleCheck,
        theme().icons.circleError,
        theme().icons.circleHelp,
        theme().icons.circleInformation,
        theme().icons.circleRemove,
        theme().icons.circleWarning,
        theme().icons.clipboardCheck,
        theme().icons.clipboardList,
        theme().icons.clipboard,
        theme().icons.clock,
        theme().icons.close,
        theme().icons.cloudDownload,
        theme().icons.cloudUpload,
        theme().icons.cloud,
        theme().icons.computer,
        theme().icons.copy,
        theme().icons.creditCard,
        theme().icons.delete,
        theme().icons.documentAdd,
        theme().icons.documentCheck,
        theme().icons.documentDownload,
        theme().icons.documentEmpty,
        theme().icons.documentRemove,
        theme().icons.document,
        theme().icons.download,
        theme().icons.drag,
        theme().icons.editAlt,
        theme().icons.edit,
        theme().icons.email,
        theme().icons.expand,
        theme().icons.export,
        theme().icons.externalLink,
        theme().icons.eyeOff,
        theme().icons.eye,
        theme().icons.favorite,
        theme().icons.filterAlt,
        theme().icons.filter,
        theme().icons.folderAdd,
        theme().icons.folderCheck,
        theme().icons.folderDownload,
        theme().icons.folderRemove,
        theme().icons.folder,
        theme().icons.grid,
        theme().icons.heart,
        theme().icons.home,
        theme().icons.image,
        theme().icons.inbox,
        theme().icons.laptop,
        theme().icons.linkAlt,
        theme().icons.link,
        theme().icons.list,
        theme().icons.location,
        theme().icons.lock,
        theme().icons.logOut,
        theme().icons.map,
        theme().icons.megaphone,
        theme().icons.menu,
        theme().icons.messageAlt,
        theme().icons.message,
        theme().icons.mobile,
        theme().icons.moon,
        theme().icons.notificationOff,
        theme().icons.notification,
        theme().icons.optionsHorizontal,
        theme().icons.optionsVertical,
        theme().icons.pause,
        theme().icons.percentage,
        theme().icons.pin,
        theme().icons.play,
        theme().icons.refresh,
        theme().icons.remove,
        theme().icons.search,
        theme().icons.select,
        theme().icons.send,
        theme().icons.settings,
        theme().icons.share,
        theme().icons.shoppingCartAdd,
        theme().icons.shoppingCart,
        theme().icons.sort,
        theme().icons.speakers,
        theme().icons.stop,
        theme().icons.sun,
        theme().icons.switch,
        theme().icons.table,
        theme().icons.tablet,
        theme().icons.tag,
        theme().icons.undo,
        theme().icons.unlock,
        theme().icons.userAdd,
        theme().icons.userCheck,
        theme().icons.userRemove,
        theme().icons.user,
        theme().icons.users,
        theme().icons.volumeOff,
        theme().icons.volumeUp,
        theme().icons.warning,
        theme().icons.zoomIn,
        theme().icons.zoomOut,
        theme().icons.fritz2
    )

    return stackUp({
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

