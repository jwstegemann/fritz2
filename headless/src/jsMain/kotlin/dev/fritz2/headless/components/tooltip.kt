package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.Tag
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.dom.html.shortcutOf
import dev.fritz2.dom.merge
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.OpenClose
import dev.fritz2.headless.foundation.OpenCloseDelegate
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.utils.popper.*
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class HeadlessTooltip<C : HTMLElement>(
    val target: Tag<HTMLElement>,
    tag: Tag<C>,
) : Tag<C> by tag, OpenClose by OpenCloseDelegate() {

    var placement: Placement = Placement.auto
    var showArrow: Boolean = true
    var strategy: Strategy = Strategy.absolute
    var flip: Boolean = true
    var skidding = 0
    var distance = 10

    fun render() {
        attr("role", "tooltip")
        target.attr(Aria.describedby, id)

        if (showArrow) {
            div("popper-arrow") {
                attr("data-popper-arrow", true)
            }
        }

        merge(target.mouseenters, target.mouseleaves, target.focusins, target.focusouts) handledBy toggle
        //TODO: check for memory leak
        Window.keydowns.events.filter { shortcutOf(it) == Keys.Escape }.map {} handledBy close


        val modifiers = buildList<Modifier<*>> {
            if (!flip) add(Flip(false))
            if (showArrow) add(Arrow())
            if (skidding != 0 || distance != 0) add(Offset(skidding, distance))
        }

        val popper = createPopper(
            target.domNode, domNode, PopperOptionsInit(
                placement,
                strategy,
                * modifiers.toTypedArray()
            )
        )

        job.invokeOnCompletion { popper.destroy() }

        className(opened.map {
            if (it) "popper visible" else "popper invisible"
        })
    }

    init {
        openClose(storeOf(false))
    }
}

//FIXME: is HtmlTag here ok?
fun <C : HTMLElement> Tag<HTMLElement>.headlessTooltip(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessTooltip<C>.() -> Unit
) {
    tag(annex, classes, id ?: Id.next(), scope) {
        HeadlessTooltip(this@headlessTooltip, this).run {
            initialize()
            render()
        }
    }
}


fun HtmlTag<HTMLElement>.headlessTooltip(
    classes: String? = null,
    id: String? = null,
    internalScope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessTooltip<HTMLDivElement>.() -> Unit
) = headlessTooltip(classes, id, internalScope, RenderContext::div, initialize)
