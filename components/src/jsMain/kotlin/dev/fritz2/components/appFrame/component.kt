package dev.fritz2.components.appFrame

import dev.fritz2.binding.storeOf
import dev.fritz2.binding.watch
import dev.fritz2.components.appFrame
import dev.fritz2.components.buttons.PushButtonComponent
import dev.fritz2.components.clickButton
import dev.fritz2.components.flexBox
import dev.fritz2.components.foundations.CloseButtonMixin
import dev.fritz2.components.foundations.CloseButtonProperty
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.lineUp
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.document
import kotlinx.coroutines.flow.onEach

internal class AppFrameSection<S : BasicParams>(
    val styling: Style<S>? = null,
    val baseClass: StyleClass = StyleClass.None,
    val id: String? = null,
    val context: RenderContext.() -> Unit = {}
)

/**
 * This class combines the _configuration_ and the core rendering of the [appFrame].
 *
 * You can configure the content of the following sections
 * - brand
 * - header
 * - actions
 * - navigation
 * - content
 * - complementary (only rendered if defined)
 * - tablist (only rendered if defined)
 *
 * In addition to that you can define how the sidebarToggle on small screens is rendered.
 * By default is a hamburger-button.
 *
 * The rendering function is used by the component factory functions [appFrame], so it is
 * not meant to be called directly unless you plan to implement your own appFrame.
 */
open class AppFrameComponent : Component<Unit>,
    CloseButtonProperty by CloseButtonMixin("sidebar-close-button", {
        margins { left { auto } }
        display(sm = { unset }, md = { none })
        Theme().appFrame.sidebarClose()
    }) {
    companion object {
        init {
            // Needs to reference header height from Theme even though static style needs to be used to style the body.
            // Header height might not update when the Theme is changed.
            // FIXME: Find more elegant solution to style body
            staticStyle(
                """
                body {
                    height: 100vh;
                    overflow: hidden;
                    max-height: -webkit-fill-available;
                    width: 100vw;
                    display: grid;
                    grid-template-areas:
                        "brand header"
                        "sidebar content"
                        "sidebar tablist";
                    grid-template-rows: ${Theme().appFrame.headerHeight} 1fr min-content;
                    grid-auto-columns: min-content 1fr;
                    padding: 0;
                    margin: 0; 
                }
                """.trimIndent()
            )
        }
    }

    private var sidebarWith: Property? = null

    /**
     * sets the min-width of sidebar
     *
     * @param value value between 0 - 100
     */
    fun sidebarWith(value: Int) { sidebarWith = "${value}vw"}

    private var mobileSidebarWith: Property? = null

    /**
     * sets the min-with of mobile sidebar
     *
     * @param value value between 0 - 100
     */
    fun mobileSidebarWith(value: Int) { mobileSidebarWith = "${value}vw"}

    private val sidebarStatus = storeOf(false)
    private val toggleSidebar = sidebarStatus.handle { !it }
    val closeSidebar = sidebarStatus.handle { false }

    private val openSideBar = staticStyle(
        "open-sidebar",
        """@media (max-width: ${Theme().breakPoints.md}) {
                transform: translateX(0) !important;
         }""".trimIndent()
    )

    private val showBackdrop = staticStyle(
        "show-backdrop",
        """@media (max-width: ${Theme().breakPoints.md}) {
                left : 0 !important;
                opacity: 1 !important;
        }""".trimIndent()
    )

    private fun mobileSidebar(topPosition: Property): Style<BasicParams> = {
        zIndex { appFrame }
        width(sm = { this@AppFrameComponent.mobileSidebarWith ?: Theme().appFrame.mobileSidebarWidth }, md = { unset })
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

    private var brand = AppFrameSection<FlexParams>()
    fun brand(
        styling: Style<FlexParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        brand = AppFrameSection(styling, baseClass, id, context)
    }

    private var header = AppFrameSection<FlexParams>()
    fun header(
        styling: Style<FlexParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        header = AppFrameSection(styling, baseClass, id, context)
    }

    private var actions = AppFrameSection<BoxParams>()
    fun actions(
        styling: Style<BoxParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        actions = AppFrameSection(styling, baseClass, id, context)
    }

    private var content = AppFrameSection<BoxParams>()
    fun content(
        styling: Style<BoxParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        content = AppFrameSection(styling, baseClass, id, context)
    }

    private var complementary: AppFrameSection<BoxParams>? = null
    fun complementary(
        styling: Style<BoxParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        complementary = AppFrameSection(styling, baseClass, id, context)
    }

    private var tablist: AppFrameSection<FlexParams>? = null
    fun tablist(
        styling: Style<FlexParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        tablist = AppFrameSection(styling, baseClass, id, context)
    }

    private var navigation = AppFrameSection<BoxParams>()
    fun navigation(
        styling: Style<BoxParams>? = null,
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        context: RenderContext.() -> Unit
    ) {
        navigation = AppFrameSection(styling, baseClass, id, context)
    }

    val sidebarToggle = ComponentProperty<PushButtonComponent.() -> Unit> {
        variant { ghost }
        icon { menu }
        type { Theme().colors.neutral }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        Window.touchends.events
            .onEach { document.documentElement?.scrollTo(0.0,0.0) }.watch()

        context.apply {
            div({
                display(
                    sm = { block },
                    md = { none })
                position {
                    fixed {
                        top { "0" }
                        left { "-110vh" }
                    }
                }
                width { "100vw" }
                height { "min(100vh, 100%)" }
                css("height: -webkit-fill-available;")
                zIndex { appFrame raiseBy -10 }
                Theme().appFrame.backdrop()
            }, prefix = "backdrop") {
                className(this@AppFrameComponent.showBackdrop.whenever(this@AppFrameComponent.sidebarStatus.data).name)
                clicks handledBy this@AppFrameComponent.toggleSidebar
            }

            header({
                grid(sm = { area { "header" } }, md = { area { "brand" } })
                this@AppFrameComponent.mobileSidebar("none")()
                height(sm = { Theme().appFrame.headerHeight }, md = { unset })
            }, scope = {
                set(AppFrameScope.Brand, true)
            }) {
                className(this@AppFrameComponent.openSideBar.whenever(this@AppFrameComponent.sidebarStatus.data).name)
                flexBox({
                    height { Theme().appFrame.headerHeight }
                    Theme().appFrame.brand()
                    this@AppFrameComponent.brand.styling?.invoke()
                }, this@AppFrameComponent.brand.baseClass, this@AppFrameComponent.brand.id, "brand") {
                    this@AppFrameComponent.brand.context(this)
                    if (this@AppFrameComponent.hasCloseButton.value) {
                        this@AppFrameComponent.closeButtonRendering.value(this) handledBy
                                this@AppFrameComponent.closeSidebar
                    }
                }
            }

            header({
                grid { area { "header" } }
            }, scope = {
                set(AppFrameScope.Header, true)
            }) {
                flexBox({
                    height { Theme().appFrame.headerHeight }
                    Theme().appFrame.header()
                    this@AppFrameComponent.header.styling?.invoke()
                }, this@AppFrameComponent.header.baseClass, this@AppFrameComponent.header.id, "header") {
                    lineUp({
                        alignItems { center }
                    }) {
                        spacing { tiny }
                        items {
                            clickButton({
                                display(md = { none })
                                padding { none }
                                margins { left { "-.5rem" } }
                            }) {
                                this@AppFrameComponent.sidebarToggle.value(this)
                            } handledBy this@AppFrameComponent.toggleSidebar
                            this@AppFrameComponent.header.context(this)
                        }
                    }
                    section({
                        this@AppFrameComponent.actions.styling?.invoke()
                    }, this@AppFrameComponent.actions.baseClass, this@AppFrameComponent.actions.id, "actions") {
                        this@AppFrameComponent.actions.context(this)
                    }
                }
            }

            aside({
                grid(sm = { area { "content" } }, md = { area { "sidebar" } })
                this@AppFrameComponent.mobileSidebar(Theme().appFrame.headerHeight)()
                overflow { hidden }
                height(sm = { "calc(100% - ${Theme().appFrame.headerHeight})" }, md = { unset })
                minWidth { this@AppFrameComponent.sidebarWith ?: Theme().appFrame.sidebarWidth }
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
                        Theme().appFrame.navigation()
                        this@AppFrameComponent.navigation.styling?.invoke()
                    }, this@AppFrameComponent.navigation.baseClass, this@AppFrameComponent.navigation.id,
                        "navigation", { set(AppFrameScope.Navigation, true) }) {
                        this@AppFrameComponent.navigation.context(this)
                    }
                    this@AppFrameComponent.complementary?.let { complementary ->
                        section({
                            Theme().appFrame.complementary()
                            complementary.styling?.invoke()
                        }, complementary.baseClass, complementary.id,
                            "complementary", { set(AppFrameScope.Complementary, true) }) {
                            complementary.context(this)
                        }
                    }
                }
            }

            main({
                position { relative {  } }
                grid { area { "content" } }
                overflow { auto }
                Theme().appFrame.content()
                styling()
                this@AppFrameComponent.content.styling?.invoke()
            }, this@AppFrameComponent.content.baseClass + baseClass,
                this@AppFrameComponent.content.id ?: id, "content", {
                    set(AppFrameScope.Content, true)
                }
            ) {
                this@AppFrameComponent.content.context(this)
            }

            this@AppFrameComponent.tablist?.let { tablist ->
                flexBox({
                    grid { area { "tablist" } }
                    direction { row }
                    alignItems { center }
                    justifyContent { spaceEvenly }
                    Theme().appFrame.tablist()
                    tablist.styling?.invoke()
                }, tablist.baseClass, tablist.id, "tablist", { set(AppFrameScope.Tablist, true) }) {
                    tablist.context(this)
                }
            }
        }
    }
}