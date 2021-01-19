package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.TextElement
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.PopoverArrowPlacements
import dev.fritz2.styling.theme.PopoverPlacements
import dev.fritz2.styling.theme.PopoverSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Class for configuring the appearance of a PopoverComponent.
 */
@ComponentMarker
class PopoverComponent {
    companion object {
        val staticCss = staticStyle(
            "popover",
            """
                  display: inline-block;
                  position: relative;  
            """
        )
    }

    var hasCloseButton: Boolean = true
    fun hasCloseButton(value: Boolean) {
        hasCloseButton = value
    }

    var closeButton: (RenderContext.(SimpleHandler<Unit>) -> Unit)? = null
    fun closeButton(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "popover-close-button",
        build: PushButtonComponent.() -> Unit = {}
    ) {
        closeButton = { closeHandle ->
            clickButton({
                Theme().popover.closeButton()
                styling()

            }, baseClass, id, prefix) {
                variant { ghost }
                icon{ fromTheme { close } }
                build()
            }.map { } handledBy closeHandle
        }
    }

    var size: PopoverSizes.() -> Style<BasicParams> = { Theme().popover.size.normal }
    fun size(value: PopoverSizes.() -> Style<BasicParams>) {
        size = value
    }

    var positionStyle: PopoverPlacements.() -> Style<BasicParams> = { Theme().popover.placement.top }
    fun placement(value: PopoverPlacements.() -> Style<BasicParams>) {

        when (value.invoke(Theme().popover.placement)) {
            Theme().popover.placement.top -> arrowPlacement { bottom }
            Theme().popover.placement.right -> arrowPlacement { left }
            Theme().popover.placement.bottom -> arrowPlacement { top }
            Theme().popover.placement.left -> arrowPlacement { right }
        }
        positionStyle = value
    }

    var hasArrow: Boolean = true
    fun hasArrow(value: Boolean) {
        hasArrow = value
    }

    var arrowPlacement: (PopoverArrowPlacements.() -> Style<BasicParams>) = { Theme().popover.arrowPlacement.bottom }
    fun arrowPlacement(value: PopoverArrowPlacements.() -> Style<BasicParams>) {
        arrowPlacement = value
    }

    var trigger: (RenderContext.() -> Unit)? = null
    fun trigger(value: (RenderContext.() -> Unit)) {
        trigger = value
    }

    var header: (RenderContext.() -> Unit)? = null
    fun header(value: (RenderContext.() -> Unit)) {
        header = {
            (::header.styled(prefix = "popover-header") {
                Theme().popover.header()
            }){ value() }
        }
    }

    fun header(value: String) {
        this.header(flowOf(value))
    }

    fun header(value: Flow<String>) {
        header = {
            (::header.styled(prefix = "popover-header") {
                Theme().popover.header()
            }){ value.asText() }
        }
    }

    var footer: (RenderContext.() -> Unit)? = null
    fun footer(value: (RenderContext.() -> Unit)) {
        footer = {
            (::footer.styled(prefix = "popover-footer") {
                Theme().popover.footer()
            }){ value() }
        }
    }

    fun footer(value: String) {
        this.footer(flowOf(value))
    }

    fun footer(value: Flow<String>) {
        footer = {
            (::footer.styled(prefix = "popover-footer") {
                Theme().popover.footer()
            }){ value.asText() }
        }
    }

    var content: (RenderContext.() -> Unit)? = null
    fun content(value: (RenderContext.() -> Unit)) {
        content = {
            (::section.styled(prefix = "popover-content") {
                Theme().popover.section()
            }){ value(this) }
        }
    }

    fun content(value: String) {
        this.content(flowOf(value))
    }

    fun content(value: Flow<String>) {
        content = {
            (::section.styled(prefix = "popover-content") {
                Theme().popover.section()
            }){ value.asText() }
        }
    }

    private fun renderArrow(RenderContext: RenderContext) {
        RenderContext.apply {
            (::div.styled(prefix = "popover-arrow") {
                arrowPlacement.invoke(Theme().popover.arrowPlacement)()
            }){}
        }
    }

    fun renderPopover(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "popover",
        RenderContext: RenderContext,
        closeHandler: SimpleHandler<Unit>) {
        RenderContext.apply {
            (::div.styled(styling, baseClass, id, prefix) {
                positionStyle.invoke(Theme().popover.placement)()
                size.invoke(Theme().popover.size)()
            }){
                if (hasArrow) {
                    renderArrow(this)
                }
                if (hasCloseButton) {
                    if (closeButton == null) {
                        closeButton()
                    }
                    closeButton?.invoke(this, closeHandler)
                }
                header?.invoke(this)
                content?.invoke(this)
                footer?.invoke(this)
            }
        }
    }
}

/**
 * This component enables to render a popover thats floats around a trigger.
 * The trigger can be a simple HTMLElement or a fritz2 component.
 * The Popover can be containing a header, a section and a footer.
 * All "areas" are optional and it containing a simple String, a flowOf<String> or
 * a HTMLElement as well as a fritz2 component. The placement of the Popover is configurable.
 *
 * The popover has a default close Button, you can hide it or you can use your own custom Button.
 * The trigger is marked by an arrow, you can hide the arrow.
 *
 * ```
 * popover {
 *   trigger {
 *      icon { fromTheme { arrowForward } }
 *   }
 *   placement { right }
 *   header(flowOf("Our simple Popover"))
 *   content {
 *      div {
 *          text("My Text in a HTMLTag")
 *      }
 *   }
 *   footer("Footercontent")
 * }
 * ```
 *
 * @see PopoverComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [PopoverComponent]
 */
fun RenderContext.popover(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "popover",
    build: PopoverComponent.() -> Unit = {}
) {
    val component = PopoverComponent().apply(build)
    val clickStore = object : RootStore<Boolean>(false) {
        val toggle = handle {
            !it
        }
    }
    (::div.styled({ }, PopoverComponent.staticCss, null, prefix){
    }){
        (::div.styled(prefix = "popover-trigger") {
            Theme().popover.trigger()
        }) {
            clicks.events.map { } handledBy clickStore.toggle
            component.trigger?.invoke(this)
        }
        clickStore.data.render {
            if (it) {
                component.renderPopover(styling, baseClass,id, prefix, this, clickStore.toggle)
            }
        }
    }
}