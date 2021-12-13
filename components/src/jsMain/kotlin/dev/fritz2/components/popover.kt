package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.components.foundations.CloseButtonMixin
import dev.fritz2.components.foundations.CloseButtonProperty
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.Key
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.keys
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.PopoverArrowPlacements
import dev.fritz2.styling.theme.PopoverPlacements
import dev.fritz2.styling.theme.PopoverSizes
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLElement

/**
 * This class combines the _configuration_ and rendering a popover that floats around a toggle element.
 * The [toggle] can be any HTMLElement or a fritz2 component.
 * The popover can be containing a [header], a [content] and a [footer].
 * All "areas" are optional and it can contain a simple [String], a [Flow<String>] or
 * a [RenderContext] as well as a fritz2 component. The placement of the popover is configurable.
 *
 * The popover has a default close-button, which you can hide or you can use your own custom close-button.
 * The [toggle] element is marked by an arrow, but you can hide the arrow if you want.
 *
 * Example usage:
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
 *   footer("Footer content")
 * }
 * ```
 */
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

    val closeOnBlur = ComponentProperty(true)
    val closeOnEscape = ComponentProperty(true)

    val hasArrow = ComponentProperty(true)
    val arrowPlacement = ComponentProperty<PopoverArrowPlacements.() -> Style<BasicParams>> {
        Theme().popover.arrowPlacement.bottom
    }
    val toggle = ComponentProperty<(RenderContext.() -> Unit)?>(null)

    private var header: (RenderContext.() -> Unit)? = null
    fun header(value: (RenderContext.() -> Unit)) {
        header = {
            header({
                Theme().popover.header()
            }, prefix = "popover-header") { value() }
        }
    }

    fun header(value: String) {
        this.header(flowOf(value))
    }

    fun header(value: Flow<String>) {
        header = {
            header({
                Theme().popover.header()
            }, prefix = "popover-header") { value.asText() }
        }
    }

    private var footer: (RenderContext.() -> Unit)? = null
    fun footer(value: (RenderContext.() -> Unit)) {
        footer = {
            footer({
                Theme().popover.footer()
            }, prefix = "popover-footer") { value() }
        }
    }

    fun footer(value: String) {
        this.footer(flowOf(value))
    }

    fun footer(value: Flow<String>) {
        footer = {
            footer({
                Theme().popover.footer()
            }, prefix = "popover-footer") { value.asText() }
        }
    }

    private var content: (RenderContext.() -> Unit)? = null
    fun content(value: (RenderContext.() -> Unit)) {
        content = {
            section({
                Theme().popover.section()
            }, prefix = "popover-content") { value(this) }
        }
    }

    fun content(value: String) {
        this.content(flowOf(value))
    }

    fun content(value: Flow<String>) {
        content = {
            section({
                Theme().popover.section()
            }, prefix = "popover-content") { value.asText() }
        }
    }

    private val visible = object : RootStore<Boolean>(false) {
        val toggle = handle { !it }
        val closeOnKey = handle<Key> { _, _ -> false }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {

            if (this@PopoverComponent.closeOnEscape.value) {
                Window.keyups.keys(Keys.Escape) handledBy this@PopoverComponent.visible.closeOnKey
            }

            div(staticCss.name, id) {
                div({ Theme().popover.toggle() }, prefix = "popover-toggle") {
                    this@PopoverComponent.toggle.value?.invoke(this)
                    clicks handledBy this@PopoverComponent.visible.toggle
                }
                lateinit var popoverElement: HTMLElement
                this@PopoverComponent.visible.data.render {
                    if (it) {
                        popoverElement = this@PopoverComponent.renderPopover(
                            this,
                            styling,
                            baseClass,
                            prefix
                        )
                    }
                }
                this@PopoverComponent.visible.data handledBy {
                    if (it) popoverElement.focus()
                }
            }
        }
    }

    private fun renderPopover(
        context: RenderContext,
        styling: BoxParams.() -> Unit = {},
        baseClass: StyleClass = StyleClass.None,
        prefix: String
    ): HTMLElement = with(context) {
        section({
            this@PopoverComponent.placementStyle.invoke(Theme().popover.placement)()
            this@PopoverComponent.size.value.invoke(Theme().popover.size)()
            focus {
                css("outline:none")
            }
        }, styling, baseClass, prefix = prefix) {
            attr("tabindex", "-1")
            if (this@PopoverComponent.hasArrow.value) {
                div({
                    this@PopoverComponent.arrowPlacement.value.invoke(Theme().popover.arrowPlacement)()
                }, prefix = "popover-arrow") {}
            }
            if (this@PopoverComponent.hasCloseButton.value) {
                this@PopoverComponent.closeButtonRendering.value(this) handledBy
                        this@PopoverComponent.visible.toggle
            }
            this@PopoverComponent.header?.invoke(this)
            this@PopoverComponent.content?.invoke(this)
            this@PopoverComponent.footer?.invoke(this)

            if (this@PopoverComponent.closeOnBlur.value) {
                blurs.map {}.debounce(100) handledBy this@PopoverComponent.visible.toggle
            }
        }.domNode
    }
}

/**
 * Creates a popover component.
 *
 * @see PopoverComponent
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.popover(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "popover",
    build: PopoverComponent.() -> Unit = {}
) = PopoverComponent().apply(build).render(this, styling, baseClass, id, prefix)