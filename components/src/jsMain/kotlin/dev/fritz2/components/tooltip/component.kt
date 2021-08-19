package dev.fritz2.components.tooltip

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.foundations.EventMixin
import dev.fritz2.components.foundations.EventProperties
import dev.fritz2.components.popup
import dev.fritz2.components.popup.Placement
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLInputElement


open class TooltipComponent(private val textFromParam: String?) :
    EventProperties<HTMLInputElement> by EventMixin(),
    Component<Unit> {

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
     * uses the [Placement] of [dev.fritz2.components.popup.PopupComponent]
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
}