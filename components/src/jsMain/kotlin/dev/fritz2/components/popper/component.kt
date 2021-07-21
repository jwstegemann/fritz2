package dev.fritz2.components.popper

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.storeOf
import dev.fritz2.components.foundations.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.EventTarget

/**
 * This component creates a popper.
 *
 * A popper should be used for to positioning `content` like `tooltip` or `popover` automatically
 * in the right place near a `trigger`. It will popped up on every event which `handledBy` given handler.
 *
 * A popper mainly consists of
 * [trigger] the Elements which calls the [content]
 * [content] which will be display after call the [trigger]. It will be rendered on an own managed context.
 *
 * It can cen configured by
 * [offset] the space (in px) between [trigger] and  [content]
 * [flipping] if no space on chosen available it will be find a right placement automatically
 * [placement] of the [content] around the [trigger]
 *
 * [trigger] provides two handler which can be used.
 * The first is important to open/toggle the [content] the second close it.
 *
 * [content] provides one handler which can be used to close it.
 *
 * Example:
 * ```kotlin
 * popper {
 *      placement { topStart }
 *      trigger { toggleHandler, closeHandler ->
 *          span {
 *              +"hover me"
 *              mouseenters.events.map { it.currentTarget } handledBy toggle
                mouseleaves.events.map {} handledBy close
 *          }
 *      }
 *      content { closeHandler ->
 *          div {
 *              +"my content"
 *              clicks.events handledBy closeHandler
 *          }
 *      }
 * }
 * ```
 *
 * The popper use an internal `store` [popperStore] of [TriggerInformation] to find the right position of the [content]
 *
 * @see [Placement]
 * @See [Positioning]
 * @See [TriggerInformation]
 *
 */
open class PopperComponent :
    EventProperties<HTMLInputElement> by EventMixin(),
    Component<Div> {

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
                        domRect = target.getBoundingClientRect()
                    )
                }
                else -> {
                    triggerInformation
                }
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
        val scope = Scope()
        lateinit var popperElement: Div

        val active =  this@PopperComponent.popperStore.data.map { it.active }

        ManagedComponent.managedRenderContext(id + "ctx", job, scope).apply {
            popperElement = div({
                Theme().popper.wrapper(this, leftRenderPosition.toInt())
                styling()
            }, baseClass = baseClass, id = id, prefix = prefix) {
                attr("style", style.data)
                attr("data-active", active)
                this@PopperComponent.content.value?.invoke(this, this@PopperComponent.popperStore.close)
            }

            this@PopperComponent.popperStore.data.debounce(100).map { triggerInformation ->
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
    ): Div {
        val popperId = id ?: "fc2-popper-" + randomId()
        context.apply {
            this@PopperComponent.trigger.value?.invoke(
                this,
                this@PopperComponent.popperStore.toggle(popperId),
                this@PopperComponent.popperStore.close
            )
        }
        return renderPopper(styling, baseClass, popperId, prefix)
    }
}