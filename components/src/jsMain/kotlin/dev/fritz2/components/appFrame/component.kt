package dev.fritz2.components.appFrame

import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.components.buttons.PushButtonComponent
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme

/**
 * This class combines the _configuration_ and the core rendering of the [appFrame].
 *
 * You can configure the content of the following sections
 * - brand
 * - header
 * - actions
 * - navigation
 * - main
 * - complementary (only rendered if defined)
 * - tablist (only rendered if defined)
 *
 * In addition to that you can define how the sidebarToggle on small screens is rendered.
 * By default is a hamburger-button.
 *
 * The rendering function is used by the component factory functions [appFrame], so it is
 * not meant to be called directly unless you plan to implement your own appFrame.
 */
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

    private val sidebarStatus = storeOf(false)
    private val toggleSidebar = sidebarStatus.handle { !it }

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

    private var brand = Pair<Style<FlexParams>?, (RenderContext.() -> Unit)?>(null, null)
    fun brand(styling: Style<FlexParams>? = null, context: RenderContext.() -> Unit) {
        brand = styling to context
    }

    private var header = Pair<Style<FlexParams>?, (RenderContext.() -> Unit)?>(null, null)
    fun header(styling: Style<FlexParams>? = null, context: RenderContext.() -> Unit) {
        header = styling to context
    }

    private var actions = Pair<Style<BoxParams>?, (RenderContext.() -> Unit)?>(null, null)
    fun actions(styling: Style<BoxParams>? = null, context: RenderContext.() -> Unit) {
        actions = styling to context
    }

    private var main = Pair<Style<BoxParams>?, (RenderContext.() -> Unit)?>(null, null)
    fun main(styling: Style<BoxParams>? = null, context: RenderContext.() -> Unit) {
        main = styling to context
    }

    private var complementary: Pair<Style<BoxParams>?, (RenderContext.() -> Unit)?>? = null
    fun complementary(styling: Style<BoxParams>? = null, context: RenderContext.() -> Unit) {
        complementary = styling to context
    }

    private var tablist: Pair<Style<FlexParams>?, (RenderContext.() -> Unit)?>? = null
    fun tablist(styling: Style<FlexParams>? = null, context: RenderContext.() -> Unit) {
        tablist = styling to context
    }

    private var navigation = Pair<Style<BoxParams>?, (RenderContext.() -> Unit)?>(null, null)
    fun navigation(styling: Style<BoxParams>? = null, context: RenderContext.() -> Unit) {
        navigation = styling to context
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
                    this@AppFrameComponent.brand.first?.invoke()
                }, prefix = "brand") {
                    this@AppFrameComponent.brand.second?.invoke(this)
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
                    this@AppFrameComponent.header.first?.invoke()
                }, prefix = "header") {
                    lineUp({
                        alignItems { dev.fritz2.styling.params.AlignItemsValues.center }
                    }) {
                        spacing { tiny }
                        items {
                            clickButton({
                                display(md = { dev.fritz2.styling.params.DisplayValues.none })
                                padding { none }
                                margins { left { "-.5rem" } }
                            }) {
                                this@AppFrameComponent.sidebarToggle.value.invoke(this)
                            } handledBy this@AppFrameComponent.toggleSidebar
                            this@AppFrameComponent.header.second?.invoke(this)
                        }
                    }
                    section({
                        this@AppFrameComponent.actions.first?.invoke()
                    }, prefix = "actions") {
                        this@AppFrameComponent.actions.second?.invoke(this)
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
                        Theme().appFrame.navigation()
                        this@AppFrameComponent.navigation.first?.invoke()
                    }, prefix = "navigation", scope = {
                        set(AppFrameScope.Navigation, true)
                    }) {
                        this@AppFrameComponent.navigation.second?.invoke(this)
                    }
                    this@AppFrameComponent.complementary?.let { complementary ->
                        section({
                            Theme().appFrame.complementary()
                            complementary.first?.invoke()
                        }, scope = {
                            set(AppFrameScope.Complementary, true)
                        }) {
                            complementary.second?.invoke(this)
                        }
                    }
                }
            }

            main({
                grid { area { "main" } }
                overflow { auto }
                Theme().appFrame.main()
                styling()
                this@AppFrameComponent.main.first?.invoke()
            }, styling, baseClass, id, prefix, {
                set(AppFrameScope.Main, true)
            }) {
                this@AppFrameComponent.main.second?.invoke(this)
            }

            this@AppFrameComponent.tablist?.let { tablist ->
                flexBox({
                    grid { area { "tablist" } }
                    direction { row }
                    alignItems { center }
                    justifyContent { spaceEvenly }
                    Theme().appFrame.tablist()
                    tablist.first?.invoke()
                }, prefix = "tablist", scope = {
                    set(AppFrameScope.Tablist, true)
                }) {
                    tablist.second?.invoke(this)
                }
            }
        }
    }
}