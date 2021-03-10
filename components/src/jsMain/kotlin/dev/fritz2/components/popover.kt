package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.PopoverArrowPlacements
import dev.fritz2.styling.theme.PopoverPlacements
import dev.fritz2.styling.theme.PopoverSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.document
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Class for configuring the appearance of a PopoverComponent.
 */
@ComponentMarker
open class PopoverComponent : Component<Unit>,
    CloseButtonProperty by CloseButtonMixin(
        "popover-close-button",
        Theme().popover.closeButton
    ) {
    companion object {
        val staticCss = staticStyle(
            "popover",
            """
                  display: inline-block;
                  position: relative;  
            """
        )
    }

    val size = ComponentProperty<PopoverSizes.() -> Style<BasicParams>> { Theme().popover.size.normal }

    private var placementStyle: PopoverPlacements.() -> Style<BasicParams> = { Theme().popover.placement.top }
    fun placement(value: PopoverPlacements.() -> Style<BasicParams>) {

        when (value.invoke(Theme().popover.placement)) {
            Theme().popover.placement.top -> arrowPlacement { bottom }
            Theme().popover.placement.right -> arrowPlacement { left }
            Theme().popover.placement.bottom -> arrowPlacement { top }
            Theme().popover.placement.left -> arrowPlacement { right }
        }
        placementStyle = value
    }

    val closeOnBlur =  ComponentProperty(true)
    val closeOnEscape =  ComponentProperty(true)

    val hasArrow = ComponentProperty(true)
    val arrowPlacement = ComponentProperty<PopoverArrowPlacements.() -> Style<BasicParams>> {
        Theme().popover.arrowPlacement.bottom
    }
    val toggle = ComponentProperty<(RenderContext.() -> Unit)?>(null)

    private var header: (RenderContext.() -> Unit)? = null
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

    private var footer: (RenderContext.() -> Unit)? = null
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

    private var content: (RenderContext.() -> Unit)? = null
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

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val clickStore = object : RootStore<Boolean>(false) {
            val toggle = handle {
                !it
            }
            val close = handleAndEmit<Boolean, Unit> { open, close ->
                if (open && close) {
                    emit(Unit)
                }
                open
            }

            init {
                close handledBy toggle
            }
        }


        val popoverId = id ?: "popover" + randomId()
        context.apply {

            if (closeOnEscape.value) {
                Window.keyups.map {
                    it.keyCode == Keys.Escape.code
                } handledBy clickStore.close
            }

            (::div.styled({ }, staticCss, null, prefix) {
            }){
                (::div.styled(prefix = "popover-toggle", id = "popover-toggle-$popoverId") {
                    Theme().popover.toggle()
                }) {
                    attr("data-popover-for", popoverId)
                    clicks.events.map { } handledBy clickStore.toggle
                    toggle.value?.invoke(this)
                }
                clickStore.data.render {
                    if (it) {
                        renderPopover(styling, baseClass, popoverId, prefix, this, clickStore.toggle)
                    }
                }
                clickStore.data.render {
                    if( it ) {
                        try {
                            document.getElementById(popoverId).asDynamic().focus()
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
    }

    private fun renderPopover(
        styling: BoxParams.() -> Unit = {},
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = "popover",
        RenderContext: RenderContext,
        closeHandler: SimpleHandler<Unit>
    ) {
        RenderContext.apply {

            (::section.styled(styling, baseClass, id, prefix) {
                placementStyle.invoke(Theme().popover.placement)()
                size.value.invoke(Theme().popover.size)()
                focus {
                    css("outline:none")
                }
            }){
                attr("tabindex", "-1")
                if (hasArrow.value) {
                    renderArrow(this)
                }
                if (hasCloseButton.value) {
                    closeButtonRendering.value(this) handledBy closeHandler
                }
                header?.invoke(this)
                content?.invoke(this)
                footer?.invoke(this)

                if (closeOnBlur.value) {
                    blurs.events.debounce(200).map { } handledBy closeHandler
                }

            }
        }
    }

    private fun renderArrow(RenderContext: RenderContext) {
        RenderContext.apply {
            (::div.styled(prefix = "popover-arrow") {
                arrowPlacement.value.invoke(Theme().popover.arrowPlacement)()
            }){}
        }
    }
}

/**
 * This component enables to render a popover thats floats around a toggle element.
 * The toggle can be a simple HTMLElement or a fritz2 component.
 * The Popover can be containing a header, a section and a footer.
 * All "areas" are optional and it containing a simple String, a flowOf<String> or
 * a HTMLElement as well as a fritz2 component. The placement of the Popover is configurable.
 *
 * The popover has a default close Button, you can hide it or you can use your own custom Button.
 * The toggle element is marked by an arrow, you can hide the arrow.
 *
 * ```
 * popover {
 *   toggle {
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
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "popover",
    build: PopoverComponent.() -> Unit = {}
) {
    PopoverComponent().apply(build).render(this, styling, baseClass, id, prefix)
}