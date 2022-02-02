package dev.fritz2.headless.foundation

import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.Tag
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.dom.html.shortcutOf
import dev.fritz2.headless.foundation.utils.popper.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

abstract class PopUpPanel<C : HTMLElement>(
    renderContext: RenderContext,
    tagFactory: TagFactory<Tag<C>>,
    classes: String?,
    id: String?,
    scope: ScopeContext.() -> Unit,
    private val openCloseDelegate: OpenClose,
    private val reference: Tag<HTMLElement>?,
    private val popperDiv: HtmlTag<HTMLDivElement> = renderContext.div("invisible") {}, //never add other classes to popperDiv, they will be overridden
    tag: Tag<C> = tagFactory(popperDiv, classes, id, scope) {}
) : Tag<C> by tag {

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
        blurs.events.mapNotNull {
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
                    attr(Aria.controls, id.whenever(openCloseDelegate.opened))
                    attr(Aria.haspopup, "true")
                }
                openCloseDelegate.opened handledBy {
                    if (it) {
                        popperDiv.domNode.className = "popper visible w-full"
                        setFocus()
                    } else {
                        tag.waitForAnimation()
                        popperDiv.domNode.className = "popper invisible w-full"
                    }
                }
            }
        }
    }
}
