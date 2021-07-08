package dev.fritz2.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme

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

    private var footer: Pair<Style<BoxParams>?, (RenderContext.() -> Unit)?>? = null
    fun footer(styling: Style<BoxParams>? = null, context: RenderContext.() -> Unit) {
        footer = styling to context
    }

    private var tabs: Pair<Style<FlexParams>?, (RenderContext.() -> Unit)?>? = null
    fun tabs(styling: Style<FlexParams>? = null, context: RenderContext.() -> Unit) {
        tabs = styling to context
    }

    private var navbar = Pair<Style<BoxParams>?, (RenderContext.() -> Unit)?>(null, null)
    fun navbar(styling: Style<BoxParams>? = null, context: RenderContext.() -> Unit) {
        navbar = styling to context
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
            box({
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
            }) {
                flexBox({
                    height { Theme().appFrame.headerHeight }
                    Theme().appFrame.header()
                    this@AppFrameComponent.header.first?.invoke()
                }, prefix = "header") {
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
                        Theme().appFrame.navbar()
                        this@AppFrameComponent.navbar.first?.invoke()
                    }, prefix = "navbar") {
                        this@AppFrameComponent.navbar.second?.invoke(this)
                    }
                    this@AppFrameComponent.footer?.let { footer ->
                        section({
                            Theme().appFrame.footer()
                            footer.first?.invoke()
                        }) {
                            footer.second?.invoke(this)
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
            }, styling, baseClass, id, prefix) {
                this@AppFrameComponent.main.second?.invoke(this)
            }

            this@AppFrameComponent.tabs?.let { tabs ->
                flexBox({
                    grid { area { "footer" } }
                    direction { row }
                    alignItems { center }
                    justifyContent { spaceEvenly }
                    Theme().appFrame.tabs()
                    tabs.first?.invoke()
                }, prefix = "tabs") {
                    tabs.second?.invoke(this)
                }
            }
        }
    }
}

/**
 * This component implements a standard responsive layout for web-apps.
 *
 * It offers the following sections
 * - sidebar with brand, navbar section and optional footer on the left
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
 * short example:
 * ```kotlin
 * appFrame {
 *     brand {
 *         //...
 *     }
 *     navbar {
 *         //...
 *     }
 *     footer { //optional
 *         //...
 *     }
 *     header { //optional
 *         //...
 *     }
 *     actions { //optional
 *         //...
 *     }
 *     main {
 *         //...
 *     }
 *     tabs { //optional
 *         //...
 *     }
 *}
 * ```
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.appFrame(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "main",
    build: AppFrameComponent.() -> Unit = {}
) {
    AppFrameComponent().apply(build).render(this, styling, baseClass, id, prefix)
}
