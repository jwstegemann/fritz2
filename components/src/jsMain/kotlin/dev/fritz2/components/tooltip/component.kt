package dev.fritz2.components.tooltip

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.foundations.EventMixin
import dev.fritz2.components.foundations.EventProperties
import dev.fritz2.components.popup
import dev.fritz2.components.popup.Placement
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement

/**
 * This component creates a Tooltip
 *
 * A `tooltip` should be used to display fast information for the user.
 * The individual `text` will be shown on hover the `RenderContext` in which be called.
 *
 * This class offers the following _configuration_ features:
 * [text] can be a `vararg`, a flow, a list, a flow of list of String or a simple string,
 * optional can be use the @property textFromParam.
 * [placement] of the [text] around the `RenderContext`in which be called.
 * Available placements are `top`, `topStart`, `topEnd`, `bottom`, `bottomStart`, `bottomEnd`, `left`, `leftStart`,
 * `leftEnd`, `right`, `rightStart`, `rightEnd`.
 *
 * The `render` function of [TooltipComponent] uses the fritz2 [dev.fritz2.components.popup.PopupComponent]
 *
 * Example usage:
 * ```
 *   span {
 *       +"hover me"
 *       tooltip("my Tooltip on right side") {
 *           placement { right }
 *       }
 *   }
 *
 *   span {
 *       +"hover me for see a multiline tooltip"
 *       tooltip("first line", "second line"){}
 *   }
 *
 *   span {
 *       +"hover me for custom colored tooltip"
 *       tooltip({
 *           color { danger.mainContrast }
 *           background {
 *               color { danger.main }
 *           }
 *       }) {
 *           text(listOf("first line", "second line"))
 *           placement { TooltipComponent.PlacementContext.bottomEnd }
 *       }
 *   }
 * ```
 *
 * @see [Placement]
 *
 */

open class TooltipComponent(private val textFromParam: String?) :
    EventProperties<HTMLInputElement> by EventMixin(),
    Component<Div> {

    private var textFromContext: Flow<List<String>> = emptyFlow()
    fun text(vararg value: String) {
        textFromContext = flowOf(value.asList())
    }

    fun text(value: String) {
        textFromContext = flowOf(listOf(value))
    }

    fun text(value: Flow<String>) {
        textFromContext = value.map { listOf(it) }
    }

    fun text(value: List<String>) {
        textFromContext = flowOf(value)
    }

    fun text(value: Flow<List<String>>) {
        textFromContext = value
    }

    private fun content() = if (textFromParam != null) flowOf(listOf(textFromParam)) else textFromContext

    /**
     * PlacementContext
     * uses [Placement] of [dev.fritz2.components.popup.PopupComponent]
     */
    object PlacementContext {
        val top = Placement.Top
        val topStart = Placement.TopStart
        val topEnd = Placement.TopEnd
        val bottom = Placement.Bottom
        val bottomStart = Placement.BottomStart
        val bottomEnd = Placement.BottomEnd
        val left = Placement.Left
        val leftStart = Placement.LeftStart
        val leftEnd = Placement.LeftEnd
        val right = Placement.Right
        val rightStart = Placement.RightStart
        val rightEnd = Placement.RightEnd
    }

    val placement = ComponentProperty<PlacementContext.() -> Placement> { Placement.Top }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Div = with(context) {
        popup({
            this as BoxParams
            Theme().tooltip.base.invoke()
            styling.invoke()
        }, id = id ?: "") {
            placement { this@TooltipComponent.placement.value.invoke(PlacementContext) }
            offset(5.0)
            trigger { toggle, close ->
                context.domNode.apply {
                    mouseenters.events.map {
                        it.currentTarget
                    } handledBy toggle
                    mouseleaves.events.map { } handledBy close
                }
            }
            content {
                this@TooltipComponent.content().renderEach { text ->
                    span { +text }
                }
            }
        }
    }
}