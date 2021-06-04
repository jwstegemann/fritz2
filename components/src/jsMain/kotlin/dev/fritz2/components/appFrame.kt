package dev.fritz2.components

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.TextElement
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This class combines the _configuration_ and the core rendering of the app-frame.
 *
 * You can configure the content of the following sections
 * - brand
 * - header
 * - actions
 * - nav
 * - main
 * - footer (only rendered if defined)
 * - tabs (only rendered if defined)
 *
 * In addition to that you can define how the sidebarToggle on small screens is rendered.
 * By default is a hamburger-button.
 *
 * The rendering function is used by the component factory functions [appFrame], so it is
 * not meant to be called directly unless you plan to implement your own appFrame.
 */
@ExperimentalCoroutinesApi
open class AppFrameComponent : Component<Unit> {
    companion object {
        init {
            // Needs to reference header height from Theme even though static style needs to be used to style the body.
            // Header height might not update when the Theme is changed.
            // FIXME: Find more elegant solution to style body
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
                    grid-template-rows: ${Theme().appFrame.headerHeight} 1fr min-content;
                    grid-auto-columns: min-content 1fr;
                    padding: 0;
                    margin: 0; 
                }
             """.trimIndent()
            )
        }
    }

    private val sidebarStatus = storeOf(false)
    private val toggleSidebar = sidebarStatus.handle { !it }

    private val openSideBar = staticStyle("open-sidebar",
        """@media (max-width: ${Theme().breakPoints.md}) {
                transform: translateX(0) !important;
         }""".trimIndent()
    )

    private val showBackdrop = staticStyle("show-backdrop",
        """@media (max-width: ${Theme().breakPoints.md}) {
                left : 0 !important;
                opacity: 1 !important;
        }""".trimIndent()
    )

    private fun mobileSidebar(topPosition: Property): Style<BasicParams> = {
        zIndex { appFrame }
        width(sm = { Theme().appFrame.mobileSidebarWidth }, md = { unset })
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
                transform .4s ease-in,
                visibility .4s linear;        
            """.trimIndent()
        )
        boxShadow(sm = { flat }, md = { none })
    }

    val brand = ComponentProperty<Div.() -> Unit> {}
    val header = ComponentProperty<RenderContext.() -> Unit> {}
    val actions = ComponentProperty<RenderContext.() -> Unit> {}
    val sidebarToggle = ComponentProperty<RenderContext.(SimpleHandler<Unit>) -> Unit> { sidebarToggle ->
        clickButton({
            display(md = { none })
            padding { none }
            margins { left { "-.5rem" } }
        }) {
            variant { link }
            icon { fromTheme { menu } }
        } handledBy sidebarToggle
    }
    val nav = ComponentProperty<TextElement.() -> Unit> {}
    val main = ComponentProperty<TextElement.() -> Unit> {}
    val footer = ComponentProperty<(TextElement.() -> Unit)?>(null)
    val tabs = ComponentProperty<(Div.() -> Unit)?>(null)


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box({
                display(
                    sm = { block },
                    md = { none })
                opacity { "0" }
                background { color { "rgba(0,0,0,0.8)" } }
                position {
                    fixed {
                        top { "0" }
                        left { "-110vh" }
                    }
                }
                width { "100vw" }
                height { "min(100vh, 100%)" }
                css("height: -webkit-fill-available;")
                zIndex { appFrame - 10 }
                css("transition: opacity .3s ease-in;")
            }, prefix = "backdrop") {
                className(this@AppFrameComponent.showBackdrop.whenever(this@AppFrameComponent.sidebarStatus.data).name)
                clicks handledBy this@AppFrameComponent.toggleSidebar
            }

            header({
                grid(sm = { area { "header" } }, md = { area { "brand" } })
                this@AppFrameComponent.mobileSidebar("none")()
                height(sm = { Theme().appFrame.headerHeight }, md = { unset })
            }) {
                className(this@AppFrameComponent.openSideBar.whenever(this@AppFrameComponent.sidebarStatus.data).name)
                flexBox({
                    height { Theme().appFrame.headerHeight }
                    Theme().appFrame.brand()
                }) {
                    this@AppFrameComponent.brand.value(this)
                }
            }

            header({
                grid { area { "header" } }
            }) {
                flexBox({
                    height { Theme().appFrame.headerHeight }
                    Theme().appFrame.header()
                }) {
                    lineUp({
                        alignItems { center }
                    }) {
                        spacing { tiny }
                        items {
                            this@AppFrameComponent.sidebarToggle.value(this, this@AppFrameComponent.toggleSidebar)
                            this@AppFrameComponent.header.value(this)
                        }
                    }
                    section {
                        this@AppFrameComponent.actions.value(this)
                    }
                }
            }

            aside({
                grid(sm = { area { "main" } }, md = { area { "sidebar" } })
                this@AppFrameComponent.mobileSidebar(Theme().appFrame.headerHeight)()
                overflow { hidden }
                height(sm = { "calc(100% - ${Theme().appFrame.headerHeight})" }, md = { unset })
                Theme().appFrame.sidebar()
            }) {
                className(this@AppFrameComponent.openSideBar.whenever(this@AppFrameComponent.sidebarStatus.data).name)

                flexBox({
                    direction { column }
                    alignItems { stretch }
                    justifyContent { spaceBetween }
                    height { full }
                    maxHeight { "-webkit-fill-available" }
                    overflow { auto }
                }) {
                    section({
                        Theme().appFrame.nav()
                    }) {
                        this@AppFrameComponent.nav.value(this)
                    }
                    this@AppFrameComponent.footer.value?.let { footer ->
                        section({
                            Theme().appFrame.footer()
                        }) {
                            footer(this)
                        }
                    }
                }
            }

            main({
                grid { area { "main" } }
                overflow { dev.fritz2.styling.params.OverflowValues.auto }
                Theme().appFrame.main()
                styling()
            }, styling, baseClass, id, prefix) {
                this@AppFrameComponent.main.value(this)
            }

            this@AppFrameComponent.tabs.value?.let { tabs ->
                flexBox({
                    grid { area { "footer" } }
                    direction { row }
                    alignItems { center }
                    justifyContent { spaceEvenly }
                    Theme().appFrame.tabs()
                }) {
                    tabs(this)
                }
            }
        }
    }
}

/**
 * This component implements a standard responsive layout for web-apps.
 *
 * It offers the following sections
 * - sidebar with brand, nav section and optional footer on the left
 * - header at the top with actions section on the right
 * - main section
 * - optional navigation tabs at the bottom of main section
 *
 * The sidebar is moved off-screen on small screens and can be opened by a hamburger-button,
 * that appears at the left edge of the header.
 *
 * The component is responsible for rendering all these section at the right sizes and places
 * at different viewport-sizes and on different devices.
 *
 * You can adopt the appearance of all sections by adjusting the [Theme].
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
@ExperimentalCoroutinesApi
fun RenderContext.appFrame(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "app",
    build: AppFrameComponent.() -> Unit = {}
) {
    AppFrameComponent().apply(build).render(this, styling, baseClass, id, prefix)
}
