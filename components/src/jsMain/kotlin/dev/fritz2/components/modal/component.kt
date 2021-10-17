package dev.fritz2.components.modal

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.storeOf
import dev.fritz2.binding.watch
import dev.fritz2.components.flexBox
import dev.fritz2.components.foundations.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.identification.Id
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.*
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import org.w3c.dom.get

/**
 * Alias for reducing boilerplate in various places, as this extension signature is used quite often within modal
 * source code.
 */
typealias ModalRenderContext = RenderContext.(level: Int) -> Div

/**
 * Enum that categorizes the methods of an overlay implementation.
 */
enum class OverlayMethod {
    /**
     * Only one overlay will be rendered, just beneath the top most modal shown, so that it covers the whole rest
     * of the screen including other modals opened before.
     */
    CoveringTopMost,

    /**
     * An overlay is rendered for each modal opened, so there are arbitrary pairs of overlay and modal on top.
     * The overall effect is often that the screen becomes darker and darker if the overlay effect is applying
     * a transparent color to the screen.
     */
    CoveringEach
}

/**
 * This interface defines the overlay type.
 * It can be used to create custom overlay functionalities.
 */
interface Overlay {
    val method: OverlayMethod
    val styling: Style<BasicParams>
    fun render(renderContext: RenderContext, level: Int)
}

/**
 * Utility function to calculate the final z-index of an overlay or modal.
 * Both types are based upon the level of the modal in order to cover other modals already rendered.
 */
internal fun ZIndices.modal(level: Int, offset: Int = 0): Property {
    return modal raiseBy (10 * (level - 1) + offset)
}

/**
 * Default implementation of an overlay, that simply uses one ``Div`` as surface to apply some styling like
 * covering the screen with some transparent color.
 */
class DefaultOverlay(
    override val method: OverlayMethod = OverlayMethod.CoveringTopMost,
    override val styling: Style<BasicParams> = Theme().modal.overlay
) : Overlay {
    override fun render(renderContext: RenderContext, level: Int) {
        renderContext.div({
            zIndex { modal(level, -1) }
            styling()
        }, prefix = "modal-overlay") {
        }
    }
}

/**
 * This component class offers primarily some configuration options for modal dialogs.
 *
 * The content and structure within the modal are completely free to model. Further more there are predefined styles
 * to easily choose a fitting size or to choose some other variants of appearance. Last but not least there is a simple
 * closeButton predefined that automatically closes the modal. Of course the closing mechanism is free to be applied
 * with a custom solution, as a [SimpleHandler<Unit>] is injected as parameter into the [content] property.
 *
 * The modal can be configured for the following aspects:
 *
 * - the overall size of the acquired space.
 * - some variants dealing with positioning (does not really work yet for all options)
 * - the content itself; can be arbitrary HTML elements or subcomponents
 * - a predefined close button that can be used optionally
 *
 * Example usages
 * ```
 * // use integrated close button
 * clickButton {
 *     text("Open")
 * } handledBy modal {
 *     content { // provide arbitrary content
 *         p { +"Hello world from a modal!" }
 *         p { +"Please click the X to close this..." }
 *     }
 * }
 *
 * // apply custom close button
 * clickButton {
 *     text("Open")
 * } handledBy modal {
 *     hasCloseButton(false) // disable the integrated close button
 *     content { closeHandler -> // SimpleHandler<Unit> is injected by default
 *         p { +"Hello world from a modal!" }
 *         p { +"Please click the X to close this..." }
 *         clickButton { text("Close") } handledBy closeHandler // define a custom button that uses the close handler
 *     }
 * }
 * ```
 *
 * There are two possible overall strategies for rendering the overlay:
 * - only once, directly below the top modal (the default behaviour)
 * - before *every* new modal, so the more gets stacked, the more overlays gets rendered!
 *
 * This can be configured via the [ModalComponent.Companion.setOverlayHandler] once or via a fitting handler of
 * [ModalComponent.Companion.overlay] for dynamic use cases.
 *
 * The actual rendering of the overlay is done within a separate interface called [Overlay].
 * There is currently one implementation [DefaultOverlay] that offers the possibility to freely inject the styling,
 * so for most use cases it might be sufficient to just use the former.
 * If there is a need to render a _different structure_ or to bypass the [dev.fritz2.styling.theme.Theme.zIndices]
 * management, a custom implementation is the way to go.
 * The interface also enforces to pass the rendering strategy identifier via the [Overlay.method] property.
 *
 * For a detailed understanding have a look the ``ModalComponent.Companion.init`` block.
 */
open class ModalComponent :
    ManagedComponent<SimpleHandler<Unit>>,
    CloseButtonProperty by CloseButtonMixin("modal-close-button", {
        position {
            absolute {
                right { smaller }
                top { smaller }
            }
        }
    }) {

    class ModalsStack : RootStore<List<ModalRenderContext>>(listOf()) {

        fun push(dialog: ModalRenderContext) = handle { stack ->
            stack + dialog
        }

        val pop = handle { stack ->
            stack.dropLast(1)
        }
    }

    companion object {
        private val stack = ModalsStack()
        val overlay = storeOf<Overlay>(DefaultOverlay())
        private val globalId = "f2c-modals-${Id.next()}"
        private val job = Job()
        private val scope = Scope()
        private val myStaticStyle = staticStyle("disableOverflowForModal", "overflow:hidden !important;")

        fun setOverlayHandler(overlay: Overlay) {
            ModalComponent.overlay.update(overlay)
        }

        init {
            stack.data.map { modals ->
                configureBodyScrolling(modals)
                ManagedComponent.managedRenderContext(globalId, job, scope).apply {
                    val currentOverlay = overlay.current
                    if (currentOverlay.method == OverlayMethod.CoveringTopMost && modals.isNotEmpty()) {
                        currentOverlay.render(this, modals.size)
                    }
                    modals.withIndex().toList().forEach { (index, modal) ->
                        if (currentOverlay.method == OverlayMethod.CoveringEach) {
                            div {
                                currentOverlay.render(this, index + 1)
                                modal(index + 1)
                            }
                        } else this.modal(index + 1)
                    }
                }
            }.watch()
        }

        private fun configureBodyScrolling(modals: List<ModalRenderContext>) {
            val bodyElementClasses = document.getElementsByTagName("body")[0]?.classList
            if (modals.isNotEmpty()) {
                bodyElementClasses?.add(myStaticStyle.name)
            } else {
                bodyElementClasses?.remove(myStaticStyle.name)
            }
        }

    }

    val content = ComponentProperty<(RenderContext.(SimpleHandler<Unit>) -> Unit)?>(null)

    @Deprecated(message = "Use width property instead.")
    val size = ComponentProperty<(ModalSizes.() -> Style<BasicParams>)?>(null)

    @Deprecated(message = "Use placement property instead.")
    val variant = ComponentProperty<ModalVariants.() -> Style<BasicParams>> { Theme().modal.variants.auto }

    enum class Placement {
        TOP, CENTER, BOTTOM, STRETCH
    }

    object PlacementContext {
        val top = Placement.TOP
        val center = Placement.CENTER
        val bottom = Placement.BOTTOM
        val stretch = Placement.STRETCH

        fun flexValueOf(placement: Placement) = when (placement) {
            Placement.TOP -> "flex-start"
            Placement.CENTER -> "center"
            Placement.BOTTOM -> "flex-end"
            Placement.STRETCH -> "stretch"
        }

        fun externalScrollingPossible(placement: Placement) = placement == Placement.TOP
    }

    val placement = ComponentProperty<PlacementContext.() -> Placement> { Placement.TOP }

    object WidthContext {
        val small = "small"
        val normal = "normal"
        val large = "large"
        val full = "full"

        fun asCssWidthExpression(value: Property) = when (value) {
            small -> Theme().modal.widths.small
            normal -> Theme().modal.widths.normal
            large -> Theme().modal.widths.large
            full -> Theme().modal.widths.full
            else -> value
        }
    }

    val width = ComponentProperty<WidthContext.() -> String> { normal }

    override fun render(
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): SimpleHandler<Unit> {
        val close = stack.pop

        val modal: ModalRenderContext = { level ->
            flexBox({
                zIndex { modal(level) }
                position {
                    fixed {
                        left { "0px" }
                        top { "0px" }
                    }
                }
                width { "100vw" }
                height { "100vh" }
                if (PlacementContext.externalScrollingPossible(this@ModalComponent.placement.value(PlacementContext))) {
                    overflow { auto }
                }
                justifyContent { center }
                alignItems { PlacementContext.flexValueOf(this@ModalComponent.placement.value(PlacementContext)) }
            }) {
                div({
                    css("--modal-level: ${level}rem;")
                    zIndex { modal(level, 1) }
                    position { relative { } }
                    Theme().modal.base()
                    if (this@ModalComponent.size.value != null) {
                        // TODO: remove if-branch when ``size`` gets removed; keep only else body!
                        this@ModalComponent.size.value!!.invoke(Theme().modal.sizes)()
                    } else {
                        Theme().modal.width(
                            this,
                            this@ModalComponent.width.value(WidthContext),
                            WidthContext.asCssWidthExpression(this@ModalComponent.width.value(WidthContext))
                        )
                    }
                    if (!PlacementContext.externalScrollingPossible(
                            this@ModalComponent.placement.value(PlacementContext)
                        )
                    ) {
                        Theme().modal.internalScrolling()
                    }
                    styling(this)
                }, baseClass, id, prefix) {
                    if (this@ModalComponent.hasCloseButton.value) {
                        this@ModalComponent.closeButtonRendering.value(this) handledBy close
                    }
                    this@ModalComponent.content.value?.let { it(close) }
                }
            }
        }

        return stack.push(modal)
    }
}