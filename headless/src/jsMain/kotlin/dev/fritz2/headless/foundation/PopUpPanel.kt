package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.utils.floatingui.core.ComputePositionConfig
import dev.fritz2.headless.foundation.utils.floatingui.core.ComputePositionReturn
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.arrow
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.flip
import dev.fritz2.headless.foundation.utils.floatingui.core.middleware.offset
import dev.fritz2.headless.foundation.utils.floatingui.dom.autoUpdate
import dev.fritz2.headless.foundation.utils.floatingui.dom.computePosition
import dev.fritz2.headless.foundation.utils.floatingui.obj
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import dev.fritz2.headless.foundation.utils.floatingui.utils.StrategyValues
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.transform
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

/**
 * Enum-Class to set the width of the Popup to
 * [Min]: Minimum the width of the reference
 * [Max]: Maximum the width of the reference
 * [Exact]: Exactly the width of the reference
 *
 * Or leaving it as [None] to disable any restrictions
 *
 * @see PopUpPanel.size
 */
enum class PopUpPanelSize {
    Min,
    Max,
    Exact,
    None
}

/**
 * Base class that provides the functionality to create popup components.
 *
 * Internally the heavy lifting is done by the excellent [Floating-UI](https://floating-ui.com/) library.
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
    private val ariaHasPopup: String,
    tag: Tag<C> = tagFactory(renderContext, classes, id, scope) {},
    private val config: ComputePositionConfig = obj {}
) : Tag<C> by tag, ComputePositionConfig by config {

    fun getChildren(): Set<Node> = buildSet {
        var children = parents.filterValues { it == domNode }.keys
        while (children.isNotEmpty()) {
            addAll(children)
            children = parents.filterValues { it in children }.keys
        }
    }

    companion object {
        private var parents = emptyMap<Node, Node?>()

        private const val FRITZ2_POPUP_HIDDEN = "fritz2-popup-hidden"
        private const val FRITZ2_POPUP_VISIBLE = "fritz2-popup-visible"

        /**
         * Use this class for adding the CSS attribute `position=relative` for a component, that uses [PopUpPanel].
         * Remember that the referenced tag for the popup will need this, so that the popup can align properly.
         */
        const val POPUP_RELATIVE = "fritz2-popup-relative"

        private const val POPUP_VISIBLE_CLASSES = "popup $FRITZ2_POPUP_VISIBLE"
        private const val POPUP_HIDDEN_CLASSES = "popup $FRITZ2_POPUP_HIDDEN"

        init {
            addGlobalStyles(
                listOf(
                    """.$POPUP_RELATIVE {
                        position: relative;
                    }""".trimIndent(),
                    """.popup[data-popup-reference-hidden] {
                visibility: hidden;
                pointer-events: none;
            }""".trimIndent(),
                    """.popup-arrow-default {
                width: 8px;
                height: 8px;
                background: inherit;
            }""".trimIndent(),
                    """.popup-arrow::before {
                        width: 100%;
                        height: 100%;
            }""".trimIndent(),
                    """.popup-arrow, .popup-arrow::before {
                position: absolute;
            }""".trimIndent(),
                    """.popup-arrow {
                visibility: hidden;
            }""".trimIndent(),
                    """.popup-arrow::before {
                content: '';
                transform: rotate(45deg);
                background: inherit;
            }""".trimIndent(),
                    """.popup-arrow::before, .popup.$FRITZ2_POPUP_VISIBLE .popup-arrow::before {
                visibility: visible;
            }""".trimIndent(),
                    """.popup-arrow::before, .popup.$FRITZ2_POPUP_HIDDEN .popup-arrow::before {
                visibility: hidden;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='bottom'] .popup-arrow::before {
                top: -50%;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='top'] .popup-arrow::before {
                bottom: -50%;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='left'] .popup-arrow::before {
                right: -50%;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='right'] .popup-arrow::before {
                left: -50%;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='bottom'] .popup-arrow {
                top: 0;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='top'] .popup-arrow {
                bottom: 0;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='left'] .popup-arrow {
                right: 0;
            }""".trimIndent(),
                    """.popup[data-popup-placement^='right'] .popup-arrow {
                left: 0;
            }""".trimIndent(),
                    """.popup[data-popup-placement='bottom'] > .transform {
                transform-origin: top;
            }""".trimIndent(),
                    """.popup[data-popup-placement='bottom-start'] > .transform {
                transform-origin: top left;
            }""".trimIndent(),
                    """.popup[data-popup-placement='bottom-right'] > .transform {
                transform-origin: top right;
            }""".trimIndent(),
                    """.popup[data-popup-placement='top'] > .transform {
                transform-origin: bottom;
            }""".trimIndent(),
                    """.popup[data-popup-placement='top-start'] > .transform {
                transform-origin: bottom left;
            }""".trimIndent(),
                    """.popup[data-popup-placement='top-right'] > .transform {
                transform-origin: bottom right;
            }""".trimIndent(),
                    """.popup[data-popup-placement='left'] > .transform {
                transform-origin: right;
            }""".trimIndent(),
                    """.popup[data-popup-placement='left-start'] > .transform {
                transform-origin: top right;
            }""".trimIndent(),
                    """.popup[data-popup-placement='left-end'] > .transform {
                transform-origin: bottom right;
            }""".trimIndent(),
                    """.popup[data-popup-placement='right'] > .transform {
                transform-origin: left;
            }""".trimIndent(),
                    """.popup[data-popup-placement='right-start'] > .transform {
                transform-origin: top left;
            }""".trimIndent(),
                    """.popup[data-popup-placement='right-end'] > .transform {
                transform-origin: bottom left;
            }""".trimIndent(),
                    """.$FRITZ2_POPUP_VISIBLE {
                visibility: visible;
            }""".trimIndent(),
                    """.$FRITZ2_POPUP_HIDDEN {
                visibility: hidden;
            }""".trimIndent(),
                )
            )
        }
    }

    /**
     * This field allows to set the allowed width range
     * for the Popup element.
     *
     * The Popup might have [Min]imum, [Max]imum or [Exact]ly the reference elements width.
     *
     * Leaving it as [None] will disable any restrictions (Default behaviour)
     *
     * @see PopUpPanel.size
     */
    var size: PopUpPanelSize = PopUpPanelSize.None

    private val computedPositionStore: Store<ComputePositionReturn> = storeOf(obj {})

    /**
     * The Position calculated by the underlying FloatingUI Library. Can be used for custom rendering inside the Popup
     *
     * Check https://floating-ui.com/docs/computePosition#return-value for detailed documentation.
     */
    val computedPosition = computedPositionStore.data

    init {
        placement = PlacementValues.bottom
        strategy = StrategyValues.fixed
        addMiddleware(flip())
    }


    /**
     * Adds a new Middleware to the array of middlewares.
     *
     * Check https://floating-ui.com/docs/middleware for available middlewares.
     *
     * @see ComputePositionConfig.middleware
     */
    fun addMiddleware(middleware: Middleware) {
        this.middleware = (this.middleware ?: emptyArray()) + middleware
    }

    private var arrow: Tag<HTMLElement>? = null

    /**
     * Adds an arrow to the PopupPanel. The exact position will be calculated by the FloatingUI component and can be
     * collected from [computedPosition]. The arrow points to the reference element.
     */
    fun arrow(c: String = "popup-arrow-default") {
        div(classes(c, "popup-arrow")) {
            arrow = this
            addMiddleware(arrow { element = domNode })
            addMiddleware(offset(5))
            inlineStyle(computedPosition.mapNotNull { it.middlewareData?.arrow }
                .map { "left: ${it.x}px; top: ${it.y}px;" })
        }
    }

    open fun render() {
        if (reference != null) {

            val computePosition = {
                computePosition(reference.domNode, domNode, config)
                    .then { computedPositionStore.update(it) }
            }

            val cleanup = autoUpdate(
                reference.domNode,
                domNode,
                options = obj { animationFrame = true },
                update = { computePosition() }
            )

            afterMount { _, _ -> computePosition() }

            beforeUnmount { _, _ -> cleanup.invoke() }

            afterMount { _, _ ->
                var parent: Node? = reference.domNode
                while (parent != null) {
                    if (parent in parents) break
                    parent = parent.parentNode
                }
                parents = parents + (domNode to parent)
            }

            beforeUnmount { _, _ ->
                parents = parents
                    .filterKeys { it != domNode }
                    .mapValues { (_, parent) -> parent.takeIf { it != domNode } }
            }

            attr("data-popup-placement", computedPosition.map { it.placement ?: "" })
            inlineStyle(computedPosition.map {
                listOfNotNull(
                    "position: ${it.strategy}", "left: ${it.x}px", "top: ${it.y}px",
                    when (size) {
                        PopUpPanelSize.Min -> "min-width: ${reference.domNode.offsetWidth}px"
                        PopUpPanelSize.Max -> "max-width: ${reference.domNode.offsetWidth}px"
                        PopUpPanelSize.Exact -> "width: ${reference.domNode.offsetWidth}px"
                        PopUpPanelSize.None -> null
                    }
                ).joinToString("; ")
            })

            reference.apply {
                attr(Aria.labelledby, reference.id)
                attr(Aria.controls, this@PopUpPanel.id.whenever(opened))
                attrIfNotSet(Aria.haspopup, ariaHasPopup)
            }

            className(
                opened.transform {
                    if (it) {
                        computePosition()
                        emit(true)
                        this@PopUpPanel.waitForAnimation()
                    } else {
                        this@PopUpPanel.waitForAnimation()
                        emit(false)
                    }
                },
                false
            ) { isOpen -> if (isOpen) POPUP_VISIBLE_CLASSES else POPUP_HIDDEN_CLASSES }
        }
    }
}