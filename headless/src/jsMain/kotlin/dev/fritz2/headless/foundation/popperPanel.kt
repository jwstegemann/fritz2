package dev.fritz2.headless.foundation

import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.utils.popper.*

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.HTMLElement

abstract class PopUpPanel<C : HtmlTag<HTMLElement>>(
    renderContext: RenderContext,
    tagFactory: TagFactory<C>,
    classes: String?,
    id: String?,
    scope: ScopeContext.() -> Unit,
    private val openCloseDelegate: OpenClose,
    private val reference: HtmlTag<HTMLElement>?,
    private val popperDiv: Div = renderContext.div() {}, //never add classes to popperDiv
    val tag: C = tagFactory(popperDiv, classes, id, scope) {}
) : RenderContext by tag {

    var placement: Placement = Placement.auto
    var strategy: Strategy = Strategy.absolute

    //    var showArrow: Boolean = false
    var flip: Boolean = true
    var skidding = 0
    var distance = 10

    fun closeOnEscape() {
        Window.keydowns.events.filter { shortcutOf(it) == Keys.Escape }
            .mapNotNull { if (openCloseDelegate.opened.first()) Unit else null } handledBy openCloseDelegate.close
    }

    fun closeOnBlur() {
        tag.blurs.events.mapNotNull {
            if (it.relatedTarget == reference?.domNode) null else Unit
        } handledBy openCloseDelegate.close
    }

    open fun render() {
        //TODO: showing and styling arrow here
//        if (showArrow) {
//            popperDiv.apply {
//                div("popper-arrow") {
//                    attr("data-popper-arrow", true)
//                }
//            }
//        }

        if (reference != null) {
            val modifiers = buildList<Modifier<*>> {
                if (!flip) add(Flip(false))
//                if (showArrow) add(Arrow())
                if (skidding != 0 || distance != 0) add(Offset(skidding, distance))
            }

            val popper = createPopper(
                reference.domNode, popperDiv.domNode, PopperOptionsInit(
                    placement,
                    strategy,
                    * modifiers.toTypedArray()
                )
            )

            job.invokeOnCompletion { popper.destroy() }

            if (openCloseDelegate.openClose.isSet) {
                reference.apply {
                    attr(Aria.labelledby, reference.id)
                    attr(Aria.controls, tag.id.whenever(openCloseDelegate.opened))
                    attr(Aria.haspopup, "true")
                }
                openCloseDelegate.opened handledBy {
                    if (it) {
                        popperDiv.domNode.className = "popper visible w-full"
                        tag.setFocus()
                    } else {
                        popperDiv.domNode.className = "popper invisible w-full"
                    }
                }
            }
        }
    }
}
