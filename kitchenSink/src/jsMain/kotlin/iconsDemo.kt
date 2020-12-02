import dev.fritz2.components.gridBox
import dev.fritz2.components.icon
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.iconsDemo(): Div {

    val icons = listOf(
        Theme().icons.add,
        Theme().icons.all,
        Theme().icons.archive,
        Theme().icons.arrowDown,
        Theme().icons.arrowLeftDown,
        Theme().icons.arrowLeftUp,
        Theme().icons.arrowLeft,
        Theme().icons.arrowRightDown,
        Theme().icons.arrowRightUp,
        Theme().icons.arrowRight,
        Theme().icons.arrowUp,
        Theme().icons.attachment,
        Theme().icons.ban,
        Theme().icons.barChartAlt,
        Theme().icons.barChart,
        Theme().icons.board,
        Theme().icons.book,
        Theme().icons.bookmark,
        Theme().icons.calendar,
        Theme().icons.call,
        Theme().icons.camera,
        Theme().icons.caretDown,
        Theme().icons.caretLeft,
        Theme().icons.caretRight,
        Theme().icons.caretUp,
        Theme().icons.check,
        Theme().icons.chevronDoubleDown,
        Theme().icons.chevronDoubleLeft,
        Theme().icons.chevronDoubleRight,
        Theme().icons.chevronDoubleUp,
        Theme().icons.chevronDown,
        Theme().icons.chevronLeft,
        Theme().icons.chevronRight,
        Theme().icons.chevronUp,
        Theme().icons.circleAdd,
        Theme().icons.circleArrowDown,
        Theme().icons.circleArrowLeft,
        Theme().icons.circleArrowRight,
        Theme().icons.circleArrowUp,
        Theme().icons.circleCheck,
        Theme().icons.circleError,
        Theme().icons.circleHelp,
        Theme().icons.circleInformation,
        Theme().icons.circleRemove,
        Theme().icons.circleWarning,
        Theme().icons.clipboardCheck,
        Theme().icons.clipboardList,
        Theme().icons.clipboard,
        Theme().icons.clock,
        Theme().icons.close,
        Theme().icons.cloudDownload,
        Theme().icons.cloudUpload,
        Theme().icons.cloud,
        Theme().icons.computer,
        Theme().icons.copy,
        Theme().icons.creditCard,
        Theme().icons.delete,
        Theme().icons.documentAdd,
        Theme().icons.documentCheck,
        Theme().icons.documentDownload,
        Theme().icons.documentEmpty,
        Theme().icons.documentRemove,
        Theme().icons.document,
        Theme().icons.download,
        Theme().icons.drag,
        Theme().icons.editAlt,
        Theme().icons.edit,
        Theme().icons.email,
        Theme().icons.expand,
        Theme().icons.export,
        Theme().icons.externalLink,
        Theme().icons.eyeOff,
        Theme().icons.eye,
        Theme().icons.favorite,
        Theme().icons.filterAlt,
        Theme().icons.filter,
        Theme().icons.folderAdd,
        Theme().icons.folderCheck,
        Theme().icons.folderDownload,
        Theme().icons.folderRemove,
        Theme().icons.folder,
        Theme().icons.grid,
        Theme().icons.heart,
        Theme().icons.home,
        Theme().icons.image,
        Theme().icons.inbox,
        Theme().icons.laptop,
        Theme().icons.linkAlt,
        Theme().icons.link,
        Theme().icons.list,
        Theme().icons.location,
        Theme().icons.lock,
        Theme().icons.logOut,
        Theme().icons.map,
        Theme().icons.megaphone,
        Theme().icons.menu,
        Theme().icons.messageAlt,
        Theme().icons.message,
        Theme().icons.mobile,
        Theme().icons.moon,
        Theme().icons.notificationOff,
        Theme().icons.notification,
        Theme().icons.optionsHorizontal,
        Theme().icons.optionsVertical,
        Theme().icons.pause,
        Theme().icons.percentage,
        Theme().icons.pin,
        Theme().icons.play,
        Theme().icons.refresh,
        Theme().icons.remove,
        Theme().icons.search,
        Theme().icons.select,
        Theme().icons.send,
        Theme().icons.settings,
        Theme().icons.share,
        Theme().icons.shoppingCartAdd,
        Theme().icons.shoppingCart,
        Theme().icons.sort,
        Theme().icons.speakers,
        Theme().icons.stop,
        Theme().icons.sun,
        Theme().icons.switch,
        Theme().icons.table,
        Theme().icons.tablet,
        Theme().icons.tag,
        Theme().icons.undo,
        Theme().icons.unlock,
        Theme().icons.userAdd,
        Theme().icons.userCheck,
        Theme().icons.userRemove,
        Theme().icons.user,
        Theme().icons.users,
        Theme().icons.volumeOff,
        Theme().icons.volumeUp,
        Theme().icons.warning,
        Theme().icons.zoomIn,
        Theme().icons.zoomOut,
        Theme().icons.fritz2
    )

    return contentFrame {
        showcaseHeader("Icons")

        gridBox({
            columns {
                repeat(5) { "1fr" }
            }
            gap { normal }
            margins { top { "3rem" } }
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