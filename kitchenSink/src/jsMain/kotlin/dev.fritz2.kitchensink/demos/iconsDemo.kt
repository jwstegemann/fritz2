package dev.fritz2.kitchensink.demos

import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.window
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
fun RenderContext.iconsDemo(): Div {

    val icons = listOf(
        Theme().icons.add,
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
        paragraph {
            +"The "
            c("icon")
            +" component offers a way to use SVG based icons within an application."
        }
        paragraph {
            +"fritz2 comes with a basic set of icons for common use cases, but you can easily include any custom"
            +" icon."
        }

        warningBox {
            p { +"Most of our included icons are taken from the excellent mono icons project." }
            p {
                +"Please have look at their "
                externalLink("project page", "https://icons.mono.company/")
                +"."
            }
            p {
                +"The icons are licensed under the "
                externalLink("MIT license", "https://github.com/mono-company/mono-icons/blob/master/LICENSE.md")
                +"."
            }
        }

        showcaseSection("Usage")
        paragraph {
            +"The easiest way to include an icon is to provide one of the identifiers from the"
            c("theme")
            +" which represents one of our icons. You can adjust the size and color by simply adding some"
            +" styling rules."
        }

        componentFrame {
            lineUp({
                alignItems { center }
            }) {
                spacing { huge }
                items {
                    icon { fromTheme { favorite } }

                    icon({
                        size { larger }
                        // .2rem)
                    }) { fromTheme { fritz2 } }

                    icon({
                        size { giant }
                        color { warning }
                    }) { fromTheme { warning } }
                }
            }
        }

        playground {
            source(
                """
                icon { fromTheme { favorite } }

                icon({
                    size { lager }
                }) { fromTheme { fritz2 } }

                icon({
                    size { giant }
                    color { warning }
                }) { fromTheme { warning } }                
            """.trimIndent()
            )
        }

        showcaseSection("Custom icon")
        paragraph {
            +"You can include a custom icon by providing a "
            c("displayName")
            +", "
            c("viewBox")
            +" string, and the "
            c("svg")
            +" definitions."
        }
        paragraph {
            +"For example, include the "
            externalLink("github mark", "https://github.com/logos")
            +" to create a nice button:"
        }
        componentFrame {
            pushButton {
                variant { outline }
                size { small }
                text("Visit us on Github!")
                icon {
                    def = IconDefinition(
                        displayName = "github",
                        viewBox = "0 0 496 512",
                        svg = """
                    <path d="M165.9 397.4c0 2-2.3 3.6-5.2 3.6-3.3.3-5.6-1.3-5.6-3.6 0-2 2.3-3.6 5.2-3.6 3-.3 5.6 1.3 
                    5.6 3.6zm-31.1-4.5c-.7 2 1.3 4.3 4.3 4.9 2.6 1 5.6 0 6.2-2s-1.3-4.3-4.3-5.2c-2.6-.7-5.5.3-6.2 
                    2.3zm44.2-1.7c-2.9.7-4.9 2.6-4.6 4.9.3 2 2.9 3.3 5.9 2.6 2.9-.7 4.9-2.6 
                    4.6-4.6-.3-1.9-3-3.2-5.9-2.9zM244.8 8C106.1 8 0 113.3 0 252c0 110.9 69.8 205.8 169.5 239.2 12.8 
                    2.3 17.3-5.6 17.3-12.1 0-6.2-.3-40.4-.3-61.4 0 0-70 15-84.7-29.8 0 0-11.4-29.1-27.8-36.6 0 
                    0-22.9-15.7 1.6-15.4 0 0 24.9 2 38.6 25.8 21.9 38.6 58.6 27.5 72.9 20.9 2.3-16 8.8-27.1 
                    16-33.7-55.9-6.2-112.3-14.3-112.3-110.5 0-27.5 7.6-41.3 23.6-58.9-2.6-6.5-11.1-33.3 2.6-67.9 
                    20.9-6.5 69 27 69 27 20-5.6 41.5-8.5 62.8-8.5s42.8 2.9 62.8 8.5c0 0 48.1-33.6 69-27 13.7 34.7 
                    5.2 61.4 2.6 67.9 16 17.7 25.8 31.5 25.8 58.9 0 96.5-58.9 104.2-114.8 110.5 9.2 7.9 17 22.9 17 
                    46.4 0 33.7-.3 75.4-.3 83.6 0 6.5 4.6 14.4 17.3 12.1C428.2 457.8 496 362.9 496 252 496 113.3 383.5 
                    8 244.8 8zM97.2 352.9c-1.3 1-1 3.3.7 5.2 1.6 1.6 3.9 2.3 5.2 1 1.3-1 
                    1-3.3-.7-5.2-1.6-1.6-3.9-2.3-5.2-1zm-10.8-8.1c-.7 1.3.3 2.9 2.3 3.9 1.6 1 3.6.7 
                    4.3-.7.7-1.3-.3-2.9-2.3-3.9-2-.6-3.6-.3-4.3.7zm32.4 35.6c-1.6 1.3-1 4.3 1.3 6.2 2.3 2.3 
                    5.2 2.6 6.5 1 1.3-1.3.7-4.3-1.3-6.2-2.2-2.3-5.2-2.6-6.5-1zm-11.4-14.7c-1.6 1-1.6 3.6 0 5.9 1.6 
                    2.3 4.3 3.3 5.6 2.3 1.6-1.3 1.6-3.9 0-6.2-1.4-2.3-4-3.3-5.6-2z" />                                        
                """.trimIndent()
                    )
                }
                events {
                    clicks.events.onEach { window.open("https://github.com/jwstegemann/fritz2") }
                        .launchIn(MainScope())
                }
            }
        }

        playground {
            source(
                """
                // define the icon itself:
                val githubMark = IconDefinition(
                        displayName = "github",
                        viewBox = "0 0 496 512",
                        svg = ""${'"'}
                        <path d="M165.9 397.4c0 2-2.3 3.6-5.2 3.6-3.3.3-5.6-1.3-5.6-3.6 0-2 2.3-3.6 5.2-3.6 3-.3 5.6 1.3 
                        5.6 3.6zm-31.1-4.5c-.7 2 1.3 4.3 4.3 4.9 2.6 1 5.6 0 6.2-2s-1.3-4.3-4.3-5.2c-2.6-.7-5.5.3-6.2 
                        2.3zm44.2-1.7c-2.9.7-4.9 2.6-4.6 4.9.3 2 2.9 3.3 5.9 2.6 2.9-.7 4.9-2.6 
                        4.6-4.6-.3-1.9-3-3.2-5.9-2.9zM244.8 8C106.1 8 0 113.3 0 252c0 110.9 69.8 205.8 169.5 239.2 12.8 
                        2.3 17.3-5.6 17.3-12.1 0-6.2-.3-40.4-.3-61.4 0 0-70 15-84.7-29.8 0 0-11.4-29.1-27.8-36.6 0 
                        0-22.9-15.7 1.6-15.4 0 0 24.9 2 38.6 25.8 21.9 38.6 58.6 27.5 72.9 20.9 2.3-16 8.8-27.1 
                        16-33.7-55.9-6.2-112.3-14.3-112.3-110.5 0-27.5 7.6-41.3 23.6-58.9-2.6-6.5-11.1-33.3 2.6-67.9 
                        20.9-6.5 69 27 69 27 20-5.6 41.5-8.5 62.8-8.5s42.8 2.9 62.8 8.5c0 0 48.1-33.6 69-27 13.7 34.7 
                        5.2 61.4 2.6 67.9 16 17.7 25.8 31.5 25.8 58.9 0 96.5-58.9 104.2-114.8 110.5 9.2 7.9 17 22.9 17 
                        46.4 0 33.7-.3 75.4-.3 83.6 0 6.5 4.6 14.4 17.3 12.1C428.2 457.8 496 362.9 496 252 496 113.3 383.5 
                        8 244.8 8zM97.2 352.9c-1.3 1-1 3.3.7 5.2 1.6 1.6 3.9 2.3 5.2 1 1.3-1 
                        1-3.3-.7-5.2-1.6-1.6-3.9-2.3-5.2-1zm-10.8-8.1c-.7 1.3.3 2.9 2.3 3.9 1.6 1 3.6.7 
                        4.3-.7.7-1.3-.3-2.9-2.3-3.9-2-.6-3.6-.3-4.3.7zm32.4 35.6c-1.6 1.3-1 4.3 1.3 6.2 2.3 2.3 
                        5.2 2.6 6.5 1 1.3-1.3.7-4.3-1.3-6.2-2.2-2.3-5.2-2.6-6.5-1zm-11.4-14.7c-1.6 1-1.6 3.6 0 5.9 1.6 
                        2.3 4.3 3.3 5.6 2.3 1.6-1.3 1.6-3.9 0-6.2-1.4-2.3-4-3.3-5.6-2z" />                                        
                    ""${'"'}.trimIndent()
                    )
                    
                // use it somewhere 
                icon { def = githubMark }
                """.trimIndent()
            )
        }

        showcaseSection("Gallery")
        paragraph {
            +"The following gallery shows all the included icons from our default theme."
            +" The name under each icon is exactly the name you have to provide for the "
            c("fromTheme")
            +" property."
        }
        gridBox({
            columns(
                sm = { repeat(2) { "1fr" } },
                md = { repeat(4) { "1fr" } }
            )
            gap { normal }
            margins { top { giant } }
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