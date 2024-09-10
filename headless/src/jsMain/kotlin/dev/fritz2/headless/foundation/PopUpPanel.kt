package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.PopUpPanelSize.*
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
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.events.UIEvent

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

    /**
     * We keep track of all active [PopUpPanel] instances and associate it with its parent if it is contained in another
     * [PopUpPanel]. This information is used to keep the pop-over opened even when click events originated from nested
     * [PopUpPanel]s, which are usually portalled and not children in the DOM.
     * This solution is heavily inspired by [Floating UI's `useDismiss()` hook](https://floating-ui.com/docs/useDismiss).
     */
    private fun getChildren(): Set<Node> = buildSet {
        var children = childToParent.filterValues { it == domNode }.keys
        while (children.isNotEmpty()) {
            addAll(children)
            children = childToParent.filterValues { it in children }.keys
        }
        if (reference != null) add(reference.domNode)
    }


    private val dismissals: Flow<UIEvent> by lazy {
        merge(
            Window.clicks.filter { event ->
                opened.first()
                        && !domNode.contains(event.target as? Node)
                        && getChildren().none { it.contains(event.target as? Node) }
                        && event.composedPath().none { it == this }
            },
            Window.keydowns.filter { opened.first() && shortcutOf(it) == Keys.Escape }
        )
    }

    /**
     * Executes the given [action] when the [PopUpPanel] is dismissed, i.e. by clicking outside the element or pressing
     * the Escape key.
     *
     * @see closeOnDismiss
     */
    fun onDismiss(action: () -> Unit) {
        dismissals handledBy {
            action()
        }
    }

    /**
     * Closes the [PopUpPanel] when dismissed, i.e. by clicking outside the element or pressing the Escape key.
     *
     * @see onDismiss
     */
    fun OpenClose.closeOnDismiss() {
        dismissals handledBy close
    }


    companion object {
        private var childToParent = emptyMap<Node, Node?>()

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
                    }""".trimIndent(),
                    """.popup-arrow::before {
                        content: '';
                        transform: rotate(45deg);
                        background: inherit;
                        width: 100%;
                        height: 100%;
                    }""".trimIndent(),
                    """.popup-arrow, .popup-arrow::before {
                        position: absolute;
                        z-index: -1;
                    }""".trimIndent(),
                    """.popup-arrow {
                        visibility: hidden;
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
    var size: PopUpPanelSize = None

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
     * For example
     * ```kotlin
     * addMiddleware(offset(10))
     * ```
     *
     * Be aware to follow the recommended precedences by floating-ui's middlewares:
     * https://floating-ui.com/docs/middleware#ordering
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
     *
     * By default, a width and height of `8px` each is set by the default class.
     * If you want to change this, you must provide both properties somehow, depending on your CSS handling / framework.
     *
     * The [offset] from the reference element has a default of `5px` and can be also adapted as needed.
     *
     * Remember that fritz2 is completely CSS framework-agnostic!
     *
     * @param size the size of the arrow using any valid CSS `width` or `height` expression. Defaults to `8` each
     * @param offset the distance between the reference element and the panel in pixels. Defaults to `5`
     */
    fun arrow(size: String = "popup-arrow-default", offset: Int = 5) {
        div(joinClasses(size, "popup-arrow")) {
            arrow = this
            addMiddleware(offset(offset))
            addMiddleware(arrow { element = domNode })
            inlineStyle(computedPosition.mapNotNull { it.middlewareData?.arrow }
                .map {
                    buildString {
                        it.x?.let { x -> append("left: ${x}px;") }
                        it.y?.let { y -> append(" top: ${y}px;") }
                    }
                })
        }
    }

    open fun render() {
        if (reference != null) {

            val computePosition = {
                computePosition(reference.domNode, domNode, config)
                    .then { computedPositionStore.update(it) }
            }

            // call it once to initialize some position definitely inside the page's content.
            // otherwise the page would grow with invisible space in bottom direction.
            computePosition()

            var cleanup: () -> Unit = {}

            opened handledBy {
                if (it) {
                    cleanup = autoUpdate(
                        reference.domNode,
                        domNode,
                        options = obj {
                            animationFrame = true
                            // apply workaround in order to bypass [issue](https://github.com/floating-ui/floating-ui/issues/1740)
                            elementResize = false
                        },
                        update = { computePosition() }
                    )
                } else {
                    cleanup()
                }
            }

            afterMount { _, _ ->
                var parent: Node? = reference.domNode
                while (parent != null) {
                    if (parent in childToParent) break
                    parent = parent.parentNode
                }
                childToParent = childToParent + (domNode to parent)
            }

            beforeUnmount { _, _ ->
                childToParent = childToParent
                    .filterKeys { it != domNode }
                    .mapValues { (_, parent) -> parent.takeIf { it != domNode } }
            }

            attr("data-popup-placement", computedPosition.map { it.placement ?: "" })
            inlineStyle(computedPosition.map { positionReturn ->
                buildString {
                    append("position: ${positionReturn.strategy}; ")
                    positionReturn.x?.let { x -> append("left: ${x}px; ") }
                    positionReturn.y?.let { y -> append("top: ${y}px; ") }
                    when (size) {
                        Min -> "min-width: ${reference.domNode.offsetWidth}px"
                        Max -> "max-width: ${reference.domNode.offsetWidth}px"
                        Exact -> "width: ${reference.domNode.offsetWidth}px"
                        None -> null
                    }?.let { style -> append(style) }
                }
            })

            reference.apply {
                attr(Aria.labelledby, reference.id)
                attr(Aria.controls, this@PopUpPanel.id.whenever(opened))
                attrIfNotSet(Aria.haspopup, ariaHasPopup)
            }

            className(
                opened.transform {
                    if (it) {
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