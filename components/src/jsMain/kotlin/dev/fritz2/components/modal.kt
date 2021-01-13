package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.render
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.ModalSizes
import dev.fritz2.styling.theme.ModalVariants
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.document
import kotlinx.coroutines.flow.map

typealias ModalRenderContext = HtmlElements.(level: Int) -> Div

enum class OverlayMethod {
    CoveringTopMost,
    CoveringEach
}

interface Overlay {
    val method: OverlayMethod
    val styling: Style<BasicParams>
    fun render(renderContext: HtmlElements, level: Int)
}

class DefaultOverlay(
    override val method: OverlayMethod = OverlayMethod.CoveringTopMost,
    override val styling: Style<BasicParams> = Theme().modal.overlay
) : Overlay {
    override fun render(renderContext: HtmlElements, level: Int) {
        renderContext.box({
            zIndex { modal(level, offset = -1) }
            styling()
        }, prefix = "modal-overlay") {
        }
    }
}

/**
 * This component class offers primarily some configuration options for modal dialogs.
 *
 * The modal can be configured for the following aspects:
 *
 * - the overall size of the acquired space.
 * - some variants dealing with positioning (does not really work yet for all options)
 * - the content itself; can be arbitrary HTML elements or subcomponents
 * - a predefined close button that can be used optionally
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
 * For a detailed understanding have a look into the [ModalComponent.show] function and the
 * ``ModalComponent.Companion.init`` block.
 */
@ComponentMarker
class ModalComponent {

    class ModalsStack : RootStore<List<ModalRenderContext>>(listOf()) {

        fun push(dialog: ModalRenderContext) = handle { stack ->
            stack + dialog
        }

        val pop = handle { stack -> stack.dropLast(1) }
    }

    companion object {
        val stack = ModalsStack()
        val overlay = storeOf<Overlay>(DefaultOverlay())

        fun setOverlayHandler(overlay: Overlay) {
            ModalComponent.overlay.update(overlay)
        }

        init {
            render(document.body!!, false) {
                div(id = "modals") {
                    stack.data.map { it.size }.render { size ->
                        val currentOverlay = overlay.current
                        if (currentOverlay.method == OverlayMethod.CoveringTopMost && size > 0) {
                            currentOverlay.render(this, size)
                        }
                    }
                    stack.data.map { it.withIndex().toList() }.renderEach { (index, modal) ->
                        val currentOverlay = overlay.current
                        if (currentOverlay.method == OverlayMethod.CoveringEach) {
                            div {
                                currentOverlay.render(this, index + 1)
                                modal(index + 1)
                            }
                        } else {
                            modal(index + 1)
                        }
                    }
                }
            }
        }

        fun show(
            styling: BasicParams.() -> Unit = {},
            baseClass: StyleClass? = null,
            id: String? = null,
            prefix: String = "modal",
            build: ModalComponent.(SimpleHandler<Unit>) -> Unit
        ): SimpleHandler<Unit> {
            val close = stack.pop
            val component = ModalComponent().apply { build(close) }

            val modal: ModalRenderContext = { level ->
                box({
                    css("--main-level: ${level}rem;")
                    zIndex { modal(level) }
                    component.size.invoke(Theme().modal.sizes)()
                    component.variant.invoke(Theme().modal.variants)()
                    styling()
                }, baseClass, id, prefix) {
                    if (component.hasCloseButton) {
                        if (component.closeButton == null) {
                            component.closeButton()
                        }
                        component.closeButton?.let { it(this, close) }
                    }

                    component.content?.let { it() }
                }
            }

            return stack.push(modal)
        }
    }

    var content: (RenderContext.() -> Unit)? = null

    fun content(value: RenderContext.() -> Unit) {
        content = value
    }

    var size: ModalSizes.() -> Style<BasicParams> = { Theme().modal.sizes.normal }

    fun size(value: ModalSizes.() -> Style<BasicParams>) {
        size = value
    }

    var variant: ModalVariants.() -> Style<BasicParams> = { Theme().modal.variants.auto }

    fun variant(value: ModalVariants.() -> Style<BasicParams>) {
        variant = value
    }

    var hasCloseButton: Boolean = true
    fun hasCloseButton(value: Boolean) {
        hasCloseButton = value
    }

    var closeButton: (HtmlElements.(SimpleHandler<Unit>) -> Unit)? = null
    fun closeButton(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "modal-close-button",
        build: PushButtonComponent.() -> Unit = {}
    ) {
        closeButton = { closeHandle ->
            clickButton({
                position {
                    absolute {
                        right { none }
                        top { none }
                    }
                }
                styling()
            }, baseClass, id, prefix) {
                variant { ghost }
                icon { fromTheme { close } }
                build()
            }.map { } handledBy closeHandle
        }
    }

}


/**
 * This component provides some modal dialog or messagebox. Basically it just offers a ``div`` that is rendered on a
 * higher [z-index](https://developer.mozilla.org/en-US/docs/Web/CSS/z-index) than the rest of the application.
 * It uses the reserved segments provided by the [dev.fritz2.styling.theme.ZIndices.modal] function that are
 * initialized by [dev.fritz2.styling.theme.Theme.zIndices].
 * That way the top modal will always have the highest ``z-index`` and therefore be on top of the screen.
 *
 * The content and structure within the modal are completely free to model. Further more there are predefined styles
 * to easily choose a fitting size or to choose some other variants of appearance. Last but not least there is a simple
 * closeButton predefined that automatically closes the modal. Of course the closing mechanism is free to be applied
 * with a custom solution, as a [SimpleHandler<Unit>] is injected as parameter into the [build] expression.
 *
 * As this factory function also returns a [SimpleHandler<Unit>], it is easy to combine it directly with some other
 * component that offers some sort of Flow, like a [clickButton].
 *
 * Have a look at some example calls
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
 * } handledBy modal { closeHandler -> // SimpleHandler<Unit> is injected by default
 *     hasCloseButton(false) // disable the integrated close button
 *     content {
 *         p { +"Hello world from a modal!" }
 *         p { +"Please click the X to close this..." }
 *         clickButton { text("Close") } handledBy closeHandler // define a custom button that uses the close handler
 *     }
 * }
 * ```
 *
 * For details about the configuration possibilities have a look at [ModalComponent].
 *
 * @see ModalComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [ModalComponent]
 *              be aware that a [SimpleHandler<Unit>] is injected in order to apply it to some closing flow inside!
 */
fun modal(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "modal",
    build: ModalComponent.(SimpleHandler<Unit>) -> Unit
): SimpleHandler<Unit> {
    return ModalComponent.show(styling, baseClass, id, prefix, build)
}