package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.utils.popper.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * Base class that provides the functionality to create popup components.
 *
 * Internally the heavy lifting is done by the excellent [Popper](https://popper.js.org/) library.
 */
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
                    """.popper-arrow-default {
                width: 8px;
                height: 8px;
                background: inherit;
            }""".trimIndent(),
                    """.popper-arrow::before {
                        width: 100%;
                        height: 100%;
            }""".trimIndent(),
                    """.popper-arrow, .popper-arrow::before {
                position: absolute;
            }""".trimIndent(),
                    """.popper-arrow {
                visibility: hidden;
            }""".trimIndent(),
            """.popper-arrow::before {
                content: '';
                transform: rotate(45deg);
                background: inherit;
            }""".trimIndent(),
            """.popper.f2-popup-visible .popper-arrow::before {
                visibility: visible;
            }""".trimIndent(),
            """.popper.f2-popup-hidden .popper-arrow::before {
                visibility: hidden;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='bottom'] .popper-arrow::before {
                top: -50%;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='top'] .popper-arrow::before {
                bottom: -50%;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='left'] .popper-arrow::before {
                right: -50%;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='right'] .popper-arrow::before {
                left: -50%;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='bottom'] .popper-arrow {
                top: 0;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='top'] .popper-arrow {
                bottom: 0;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='left'] .popper-arrow {
                right: 0;
            }""".trimIndent(),
                    """.popper[data-popper-placement^='right'] .popper-arrow {
                left: 0;
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

    var flip: Boolean = true
    var skidding = 0
    var distance = 10

    private var showArrow = false
    fun arrow(c: String = "popper-arrow-default") {
        showArrow = true
        div(classes(c, "popper-arrow")) {
            attr("data-popper-arrow", true)
        }
    }

    open fun render() {
        if (reference != null) {
            val modifiers = buildList<Modifier<*>> {
                if (!flip) add(Flip(false))
                if (showArrow) add(Arrow())
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
                        this@PopUpPanel.waitForAnimation()
                        popper.update()
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
