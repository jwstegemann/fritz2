package dev.fritz2.headless.foundation

import dev.fritz2.core.*
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
    private val popperDiv: HtmlTag<HTMLDivElement> = renderContext.div("f2-popup-hidden") {}, //never add other classes to popperDiv, they will be overridden
    tag: Tag<C> = tagFactory(popperDiv, classes, id, scope) {}
) : Tag<C> by tag {

    companion object {
        init {
            addGlobalStyles(
                listOf(
                    """.popper[data-popper-reference-hidden] {
                visibility: hidden;
                pointer-events: none;
            }""".trimIndent(),
                    """.popper-arrow, .popper-arrow::before {
                position: absolute;
                width: 8px;
                height: 8px;
                background: inherit;
            }""".trimIndent(),
                    """.popper-arrow {
                visibility: hidden;
            }""".trimIndent(),
                    """.popper-arrow::before {
                visibility: visible;
                content: '';
                transform: rotate(45deg);
            }""".trimIndent(),
                    """.popper[data-popper-placement^='top'] > .popper-arrow {
                bottom: -4px;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='bottom'] > .popper-arrow {
                top: -4px;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='left'] > .popper-arrow {
                right: -4px;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='right'] > .popper-arrow {
                left: -4px;
            }""".trimIndent(),
                    """.popper[data-popper-placement='bottom'] > .transform {
                transform-origin: top;
            }""".trimIndent(),
                    """.popper[data-popper-placement='bottom-start'] > .transform {
                transform-origin: top left;
            }""".trimIndent(),
                    """.popper[data-popper-placement='bottom-right'] > .transform {
                transform-origin: top right;
            }""".trimIndent(),
                    """.popper[data-popper-placement='top'] > .transform {
                transform-origin: bottom;
            }""".trimIndent(),
                    """.popper[data-popper-placement='top-start'] > .transform {
                transform-origin: bottom left;
            }""".trimIndent(),
                    """.popper[data-popper-placement='top-right'] > .transform {
                transform-origin: bottom right;
            }""".trimIndent(),
                    """.popper[data-popper-placement='left'] > .transform {
                transform-origin: right;
            }""".trimIndent(),
                    """.popper[data-popper-placement='left-start'] > .transform {
                transform-origin: top right;
            }""".trimIndent(),
                    """.popper[data-popper-placement='left-end'] > .transform {
                transform-origin: bottom right;
            }""".trimIndent(),
                    """.popper[data-popper-placement='right'] > .transform {
                transform-origin: left;
            }""".trimIndent(),
                    """.popper[data-popper-placement='right-start'] > .transform {
                transform-origin: top left;
            }""".trimIndent(),
                    """.popper[data-popper-placement='right-end'] > .transform {
                transform-origin: bottom left;
            }""".trimIndent(),
                    """.f2-popup-visible {
                width: 100%;
                visibility: visible;
            }""".trimIndent(),
                    """.f2-popup-hidden {
                width: 100%;
                visibility: hidden;
            }""".trimIndent()
                )
            )
        }
    }


    var placement: Placement = Placement.auto
    var strategy: Strategy = Strategy.absolute

    //    var showArrow: Boolean = false
    var flip: Boolean = true
    var skidding = 0
    var distance = 10

    fun closeOnEscape() {
        Window.keydowns.filter { shortcutOf(it) == Keys.Escape }
            .mapNotNull { if (openCloseDelegate.opened.first()) Unit else null } handledBy openCloseDelegate.close
    }

    fun closeOnBlur() {
        attrIfNotSet("tabindex", "0")
        blurs.mapNotNull {
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
                        popperDiv.domNode.className = "popper f2-popup-visible"
                        setFocus()
                    } else {
                        this@PopUpPanel.waitForAnimation()
                        popperDiv.domNode.className = "popper f2-popup-hidden"
                    }
                }
            }
        }
    }
}
