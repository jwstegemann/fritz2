package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.utils.popper.*
import kotlinx.coroutines.flow.Flow
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
    private val opened: Flow<Boolean>,
    private val fullWidth: Boolean = true,
    private val reference: Tag<HTMLElement>?,
    private val popperDiv: HtmlTag<HTMLDivElement> = renderContext.div(POPUP_HIDDEN) {}, //never add other classes to popperDiv, they will be overridden
    tag: Tag<C> = tagFactory(popperDiv, classes, id, scope) {}
) : Tag<C> by tag {

    companion object {
        private const val POPUP_HIDDEN = "fritz2-popup-hidden"
        private const val POPUP_VISIBLE = "fritz2-popup-visible"
        private const val POPUP_HIDDEN_FULL = "fritz2-popup-hidden-full"
        private const val POPUP_VISIBLE_FULL = "fritz2-popup-visible-full"
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
            """.popper.$POPUP_VISIBLE_FULL .popper-arrow::before, .popper.$POPUP_VISIBLE .popper-arrow::before {
                visibility: visible;
            }""".trimIndent(),
            """.popper.$POPUP_HIDDEN_FULL .popper-arrow::before, .popper.$POPUP_HIDDEN .popper-arrow::before {
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
                    """.$POPUP_VISIBLE {
                visibility: visible;
            }""".trimIndent(),
                    """.$POPUP_HIDDEN {
                visibility: hidden;
            }""".trimIndent(),
                    """.$POPUP_VISIBLE_FULL {
                width: 100%;
                visibility: visible;
            }""".trimIndent(),
                    """.$POPUP_HIDDEN_FULL {
                width: 100%;
                visibility: hidden;
            }""".trimIndent()
                )
            )
        }
    }

    private val visibleClasses = "popper ${if (fullWidth) POPUP_VISIBLE_FULL else POPUP_VISIBLE}"
    private val hidden = "popper ${if (fullWidth) POPUP_HIDDEN_FULL else POPUP_HIDDEN}"

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

            reference.apply {
                attr(Aria.labelledby, reference.id)
                attr(Aria.controls, id.whenever(opened))
                attr(Aria.haspopup, "true")
            }
            opened handledBy {
                if (it) {
                    popperDiv.domNode.className = visibleClasses
                    this@PopUpPanel.waitForAnimation()
                    popper.update()
                    setFocus()
                } else {
                    this@PopUpPanel.waitForAnimation()
                    popperDiv.domNode.className = hidden
                }
            }
        }
    }
}
