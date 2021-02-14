package dev.fritz2.components

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.Store
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.TextElement
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.name
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
open class PwaComponent() {

    companion object {
        init {
            staticStyle(
                """
                body {
                    height: 100vh;
                    max-height: -webkit-fill-available;
                    width: 100vw;
                    display: grid;
                    grid-template-areas:
                        "brand header"
                        "sidebar main"
                        "sidebar footer";
                    grid-template-rows: ${Theme().pwa.headerHeight} 1fr min-content;
                    grid-auto-columns: min-content 1fr;
                    padding: 0;
                    margin: 0; 
                }
             """.trimIndent()
            )
        }
    }

    val sidebarStatus = storeOf(false)
    val toggleSidebar = sidebarStatus.handle { !it }

    val openSideBar = staticStyle(
        "open-sidebar", """
      @media (max-width: ${Theme().breakPoints.md}) {
        transform: translateX(0) !important;
      }
    """.trimIndent()
    )

    val showBackdrop = staticStyle(
        "show-backdrop", """
        @media (max-width: ${Theme().breakPoints.md}) {
            left : 0 !important;
            opacity: 1 !important;
        }
    """.trimIndent()
    )

    fun mobileSidebar(topPosition: Property): Style<BasicParams> = {
        //FIXME: add to Theme!
        zIndex { "5000" }
        width(sm = { Theme().pwa.mobileSidebarWidth }, md = { unset })
        css(sm = "transform: translateX(-110vw);", md = "transform: unset;")
        position(sm = {
            fixed { top { topPosition } }
        }, md = {
            relative { top { none } }
        })
        css(
            """
            max-height: -webkit-fill-available;
            will-change: transform;
            transition: 
                transform .6s ease-in,
                visibility .6s linear .6s;        
            """.trimIndent()
        )
        boxShadow(sm = { raised }, md = { none })
    }

    var brand = ComponentProperty<Div.() -> Unit> {}
    var header = ComponentProperty<RenderContext.() -> Unit> {}
    var actions = ComponentProperty<RenderContext.() -> Unit> {}
    var sidebarToggle = ComponentProperty<RenderContext.(SimpleHandler<Unit>) -> Unit> { sidebarToggle ->
        clickButton({
            display(md = { dev.fritz2.styling.params.DisplayValues.none })
            padding { none }
            margins { left { "-.5rem" } }
        }) {
            variant { link }
            icon { fromTheme { menu } }
        } handledBy sidebarToggle
    }
    var nav = ComponentProperty<TextElement.() -> Unit> {}
    var main = ComponentProperty<TextElement.() -> Unit> {}
    var footer = ComponentProperty<(TextElement.() -> Unit)?>(null)
    var tabs = ComponentProperty<(Div.() -> Unit)?>(null)


}

@ExperimentalCoroutinesApi
fun RenderContext.pwa(
    styling: BasicParams.() -> Unit = {},
    store: Store<String>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "pwa",
    build: PwaComponent.() -> Unit = {}
) {
    val component = PwaComponent().apply(build)

    box({
        display(
            sm = { dev.fritz2.styling.params.DisplayValues.block },
            md = { dev.fritz2.styling.params.DisplayValues.none })
        opacity { "0" }
        background { color { "rgba(0,0,0,0.6)" } }
        position {
            fixed {
                top { "0" }
                left { "-110vh" }
            }
        }
        width { "100vw" }
        height { "100vh" }
        css("height: -webkit-fill-available;")
        zIndex { "4000" }
        css(
            """
            transition: 
                opacity .3s ease-in;        
        """.trimIndent()
        )
    }, prefix = "backdrop") {
        className(component.showBackdrop.whenever(component.sidebarStatus.data).name)
        clicks handledBy component.toggleSidebar
    }

    (::header.styled {
        grid(sm = { area { "header" } }, md = { area { "brand" } })
        component.mobileSidebar("none")()
        height(sm = { Theme().pwa.headerHeight }, md = { unset })
    }) {
        className(component.openSideBar.whenever(component.sidebarStatus.data).name)
        flexBox({
            height { Theme().pwa.headerHeight }
            Theme().pwa.brand()
        }) {
            component.brand.value(this)
        }
    }

    (::header.styled {
        grid { area { "header" } }
    }) {
        flexBox({
            height { Theme().pwa.headerHeight }
            Theme().pwa.header()
        }) {
            lineUp({
                alignItems { dev.fritz2.styling.params.AlignItemsValues.center }
            }) {
                spacing { tiny }
                items {
                    component.sidebarToggle.value(this, component.toggleSidebar)
                    component.header.value(this)
                }
            }
            section {
                component.actions.value(this)
            }
        }
    }

    (::aside.styled {
        grid(sm = { area { "main" } }, md = { area { "sidebar" } })
        component.mobileSidebar(Theme().pwa.headerHeight)()
        maxHeight(sm = { "calc(100vh - ${Theme().pwa.headerHeight})" }, md = { unset })
        overflow { hidden }
        height(sm = { "calc(100vh - ${Theme().pwa.headerHeight})" }, md = { unset })
        Theme().pwa.sidebar()
    }) {
        className(component.openSideBar.whenever(component.sidebarStatus.data).name)

        flexBox({
            direction { dev.fritz2.styling.params.DirectionValues.column }
            alignItems { dev.fritz2.styling.params.AlignItemsValues.stretch }
            justifyContent { dev.fritz2.styling.params.JustifyContentValues.spaceBetween }
            height { full }
            maxHeight { "-webkit-fill-available" }
            overflow { auto }
        }) {
            (::section.styled {
                Theme().pwa.nav()
            }) {
                component.nav.value(this)
            }
            component.footer.value?.let { footer ->
                (::section.styled {
                    Theme().pwa.footer()
                }) {
                    footer(this)
                }
            }
        }
    }

    (::main.styled {
        grid { area { "main" } }
        overflow { dev.fritz2.styling.params.OverflowValues.auto }
        Theme().pwa.main()
    }) {
        component.main.value(this)
    }

    component.tabs.value?.let { tabs ->
        flexBox({
            grid { area { "footer" } }
            direction { dev.fritz2.styling.params.DirectionValues.row }
            alignItems { dev.fritz2.styling.params.AlignItemsValues.center }
            justifyContent { dev.fritz2.styling.params.JustifyContentValues.spaceEvenly }
            Theme().pwa.tabs()
//        overflow { auto }
//        PwaStyles.main()
//        height { "calc(100vh - ${PwaStyles.headerHeight})" }
        }) {
            tabs(this)
        }
    }
}