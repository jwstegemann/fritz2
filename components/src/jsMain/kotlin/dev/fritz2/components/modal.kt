package dev.fritz2.components

import dev.fritz2.binding.*
import dev.fritz2.dom.appendToBody
import dev.fritz2.dom.html.render
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.ModalStyles
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.combine
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.renderAll

typealias ModalRenderContext = HtmlElements.(level: Int) -> Unit

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
    override val styling: Style<BasicParams> = theme().modal.overlay
) : Overlay {
    override fun render(renderContext: HtmlElements, level: Int) {
        renderContext.box({
            zIndex { modal(level, offset = -1) }
            styling()
        }, prefix = "modal-overlay") {
        }
    }
}

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
            action(overlay) handledBy ModalComponent.overlay.update
        }

        init {
            appendToBody(render {
                div(id = "modals") {
                    overlay.data.combine(stack.data) { o, m -> Pair(m, o) }.renderAll {
                        if (it.second.method == OverlayMethod.CoveringTopMost && it.first.isNotEmpty()) {
                            it.second.render(this, it.first.size)
                        }
                        for ((index, modal) in it.first.withIndex()) {
                            if (it.second.method == OverlayMethod.CoveringEach) {
                                it.second.render(this, index + 1)
                            }
                            modal(index + 1)
                        }
                    }.bind()
                }
            })
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
                    component.size.invoke(theme().modal)()
                    component.variant.invoke(theme().modal)()
                    styling()
                }, baseClass, id, prefix) {
                    if( component.hasCloseButton ) {
                        if( component.closeButton == null ) {
                            component.closeButton()
                        }
                        component.closeButton?.let { it(this, close) }
                    }

                    component.items?.let { it() }
                }
            }

            return stack.push(modal)
        }
    }

    var items: (HtmlElements.() -> Unit)? = null

    fun items(value: HtmlElements.() -> Unit) {
        items = value
    }

    var size: ModalStyles.() -> Style<BasicParams> = { theme().modal.sizes.normal }

    fun size(value: ModalStyles.() -> Style<BasicParams>) {
        size = value
    }

    var variant: ModalStyles.() -> Style<BasicParams> = { theme().modal.variants.auto }

    fun variant(value: ModalStyles.() -> Style<BasicParams>) {
        variant = value
    }

    var hasCloseButton: Boolean = true
    fun hasCloseButton(value: Boolean ) {
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
        closeButton = { close ->
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
                icon { fromTheme { smallClose } }
                build()
            }.map { Unit } handledBy close
        }
    }

}

fun modal(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "modal",
    build: ModalComponent.(SimpleHandler<Unit>) -> Unit
): SimpleHandler<Unit> {
    return ModalComponent.show(styling, baseClass, id, prefix, build)
}