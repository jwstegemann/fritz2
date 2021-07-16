package dev.fritz2.components.popper

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.Window
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.w3c.dom.*
import org.w3c.dom.events.EventTarget

data class TriggerInformation(
    val id: String = "",
    val active: Boolean = false,
    val clientRect: DOMRect = DOMRect()
)


class Positioning(
    val triggerInformation: TriggerInformation,
    val element: HTMLDivElement,
    val offset: Double,
    val placement: PopperComponent.Placement,
    val flipping: Boolean = true
) {
    private fun spaceAvailable(placement: PopperComponent.Placement?): Boolean {
        console.log(triggerInformation.clientRect.top)
        console.log(triggerInformation.clientRect.left)
        console.log(triggerInformation.clientRect.height)
        console.log(triggerInformation.clientRect.width)
        console.log(triggerInformation.clientRect.y)
        console.log(triggerInformation.clientRect.x)
        console.log("#####")

        return when (placement) {
            PopperComponent.Placement.Top,
            PopperComponent.Placement.TopStart,
            PopperComponent.Placement.TopEnd -> {
                triggerInformation.clientRect.top - element.offsetHeight > 0
            }
            PopperComponent.Placement.Bottom,
            PopperComponent.Placement.BottomStart,
            PopperComponent.Placement.BottomEnd -> {
                triggerInformation.clientRect.top + element.offsetHeight - window.innerHeight < 0
            }
            PopperComponent.Placement.Left,
            PopperComponent.Placement.LeftStart,
            PopperComponent.Placement.LeftEnd -> {
                triggerInformation.clientRect.left - element.offsetWidth > 0
            }
            PopperComponent.Placement.Right,
            PopperComponent.Placement.RightStart,
            PopperComponent.Placement.RightEnd -> {
                triggerInformation.clientRect.left + triggerInformation.clientRect.width + element.offsetWidth - window.innerWidth < 0
            }
            else -> true
        }
    }

    val position: Pair<Double, Double>
        get() {
            val default = this.placement.position(triggerInformation, element, offset)
            return if (flipping) {
                when {
                    spaceAvailable(this.placement) -> {
                        default
                    }
                    spaceAvailable(this.placement.flipMain) -> {
                        console.info("flipMain")
                        this.placement.flipMain.position(triggerInformation, element, offset)
                    }
                    spaceAvailable(this.placement.flipAlternative) -> {
                        console.info("flipAlternative")
                        this.placement.flipAlternative.position(triggerInformation, element, offset)
                    }
                    spaceAvailable(this.placement.flipAlternative.flipMain) -> {
                        console.info("flipAlternative.flipMain")
                        this.placement.flipAlternative.flipMain.position(triggerInformation, element, offset)
                    }
                    else -> {
                        console.info("default")
                        default
                    }
                }
            } else {
                default
            }
        }


    val inlineStyle = buildString {
        if (triggerInformation.active) {
            append(
                "transform: translate(${(position.first + window.scrollX).toInt()}px, " +
                        "${(position.second + window.scrollY).toInt()}px);"
            )
        }
    }
}

open class PopperComponent :
    EventProperties<HTMLInputElement> by EventMixin(),
    Component<Unit> {

    companion object {
        const val leftRenderPosition: Double = 9999.0
    }

    val offset = ComponentProperty(10.0)
    val flipping = ComponentProperty(true)
    val content = ComponentProperty<(RenderContext.(SimpleHandler<Unit>) -> Unit)?>(null)
    val trigger = ComponentProperty<(RenderContext.(SimpleHandler<EventTarget?>, SimpleHandler<Unit>) -> Unit)?>(null)

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

    sealed class Placement(
        val position: (trigger: TriggerInformation, element: HTMLDivElement, offset: Double) -> Pair<Double, Double>,
        val flipMain: Placement,
        val flipAlternative: Placement
    ) {
        object Top : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + trigger.clientRect.left + trigger.clientRect.width * .5 - element.offsetWidth * .5
                val top = trigger.clientRect.top - offset - element.offsetHeight
                left to top
            },
            flipMain = Bottom,
            flipAlternative = Left
        )

        object TopStart : Placement(
            position = { trigger, element, offset ->
                val left = leftRenderPosition + trigger.clientRect.left
                val top = trigger.clientRect.top - offset - element.offsetHeight
                left to top.toDouble()
            },
            flipMain = BottomStart,
            flipAlternative = LeftStart
        )

        object TopEnd : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + trigger.clientRect.left + trigger.clientRect.width - element.offsetWidth
                val top = trigger.clientRect.top - offset - element.offsetHeight
                left to top.toDouble()
            },
            flipMain = BottomEnd,
            flipAlternative = LeftEnd
        )

        object Bottom : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + trigger.clientRect.left + trigger.clientRect.width * .5 - element.offsetWidth * .5
                val top = trigger.clientRect.top + offset + trigger.clientRect.height
                left to top.toDouble()
            },
            flipMain = Top,
            flipAlternative = Left
        )

        object BottomStart : Placement(
            position = { trigger, element, offset ->
                val left = leftRenderPosition + trigger.clientRect.left
                val top = trigger.clientRect.top + offset + trigger.clientRect.height
                left to top.toDouble()
            },
            flipMain = TopStart,
            flipAlternative = LeftStart
        )

        object BottomEnd : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + trigger.clientRect.left + trigger.clientRect.width - element.offsetWidth
                val top = trigger.clientRect.top + offset + trigger.clientRect.height
                left to top
            },
            flipMain = TopEnd,
            flipAlternative = LeftEnd
        )

        object Left : Placement(
            position = { trigger, element, offset ->
                val left = leftRenderPosition - offset + trigger.clientRect.left - element.offsetWidth
                val top = trigger.clientRect.top + trigger.clientRect.height * .5 - element.offsetHeight * .5
                left to top
            },
            flipMain = Right,
            flipAlternative = Top
        )

        object LeftStart : Placement(
            position = { trigger, element, offset ->
                val left = leftRenderPosition - offset + trigger.clientRect.left - element.offsetWidth
                val top = trigger.clientRect.top
                left to top.toDouble()
            },
            flipMain = RightStart,
            flipAlternative = TopStart
        )

        object LeftEnd : Placement(
            position = { trigger, element, offset ->
                val left = leftRenderPosition - offset + trigger.clientRect.left - element.offsetWidth
                val top = trigger.clientRect.top + trigger.clientRect.height - element.offsetHeight
                left to top.toDouble()
            },
            flipMain = RightEnd,
            flipAlternative = TopEnd
        )

        object Right : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + offset + trigger.clientRect.left + trigger.clientRect.width
                val top = trigger.clientRect.top + trigger.clientRect.height * .5 - element.offsetHeight * .5
                left to top
            },
            flipMain = Left,
            flipAlternative = Top
        )

        object RightStart : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + offset + trigger.clientRect.left + trigger.clientRect.width
                val top = trigger.clientRect.top
                left to top.toDouble()
            },
            flipMain = LeftStart,
            flipAlternative = TopStart
        )

        object RightEnd : Placement(
            position = { trigger, element, offset ->
                val left =
                    leftRenderPosition + offset + trigger.clientRect.left + trigger.clientRect.width
                val top = trigger.clientRect.top + trigger.clientRect.height - element.offsetHeight
                left to top
            },
            flipMain = LeftEnd,
            flipAlternative = TopEnd
        )
    }

    private val popperStore = object : RootStore<TriggerInformation>(TriggerInformation()) {
        fun toggle(id: String) = handle<EventTarget?> { triggerInformation, eventTarget ->
            when {
                triggerInformation.active -> {
                    triggerInformation.copy(active = false)
                }
                eventTarget != null -> {
                    val target = eventTarget as HTMLElement
                    TriggerInformation(
                        id = id,
                        active = true,
                        clientRect = target.getBoundingClientRect()
                    )
                }
                else -> {
                    triggerInformation
                }
            }
        }

        val windowClicked = handle<Array<EventTarget>> { triggerInformation, composedPath ->
            console.info(composedPath)
            if( triggerInformation.active && composedPath.firstOrNull { it.asDynamic().id == triggerInformation.id } == null ) {
                triggerInformation.copy(active = false)
            } else {
                triggerInformation
            }
        }

        val close = handle { triggerInformation ->
            triggerInformation.copy(active = false)
        }
    }

    private fun renderPopper(
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String,
        prefix: String
    ): Div {
        val style = storeOf("")
        val job = Job()
        lateinit var popperElement: Div

        val active =  this@PopperComponent.popperStore.data.map { it.active }

        ManagedComponent.managedRenderContext(id + "ctx", job).apply {

            popperElement = div({
                Theme().popper.wrapper(this, leftRenderPosition.toInt())
                styling()
            }, baseClass = baseClass, id = id, prefix = prefix) {
                attr("style", style.data)
                attr("data-active", active)
                this@PopperComponent.content.value?.invoke(this, this@PopperComponent.popperStore.close)
            }

            this@PopperComponent.popperStore.data.debounce(100).map { triggerInformation ->
                console.info(triggerInformation.toString())
                this@PopperComponent.getStyling(triggerInformation, popperElement)
            } handledBy style.update
        }

        return popperElement
    }

    private fun getStyling(triggerInformation: TriggerInformation, element: Div): String {
        return if (triggerInformation.id.isNotEmpty()) {
            Positioning(
                triggerInformation,
                element.domNode,
                this@PopperComponent.offset.value,
                this@PopperComponent.placement.value.invoke(PlacementContext),
                this@PopperComponent.flipping.value
            ).inlineStyle
        } else {
            ""
        }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val popperId = id ?: "fc2-popper-" + randomId()
        context.apply {
            this@PopperComponent.trigger.value?.invoke(
                this,
                this@PopperComponent.popperStore.toggle(popperId),
                this@PopperComponent.popperStore.close
            )
            attr("data-popper", popperId)
        }
        renderPopper(styling, baseClass, popperId, prefix)
    }
}