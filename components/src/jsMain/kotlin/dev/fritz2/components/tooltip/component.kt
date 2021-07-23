package dev.fritz2.components.tooltip

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.foundations.EventMixin
import dev.fritz2.components.foundations.EventProperties
import dev.fritz2.components.popper
import dev.fritz2.components.popper.Placement
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.span
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement



open class TooltipComponent :
    EventProperties<HTMLInputElement> by EventMixin(),
    Component<Unit> {

    /**
     * TODO to be discussed
     */
    private var text: Flow<List<String>> = emptyFlow()
    fun text(vararg value: String) {
        text = flowOf(value.asList())
    }

    fun text(value: String) {
        text = flowOf(listOf(value))
    }

    fun text(value: Flow<String>) {
        text = value.map { listOf(it) }
    }

    fun text(value: List<String>) {
        text = flowOf(value)
    }

    fun text(value: Flow<List<String>>) {
        text = value
    }

    /**
     * PlacementContext
     * uses the [Placement] of [dev.fritz2.components.popper.PopperComponent]
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
    ) {
        context.apply {
            popper({
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
                    this@TooltipComponent.text.renderEach { text ->
                        span { +text }
                    }
                }
            }
        }
    }
}