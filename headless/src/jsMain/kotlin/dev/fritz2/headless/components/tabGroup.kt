package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import kotlinx.browser.document
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * This class provides the building blocks to implement a tab-group.
 *
 * Use [tabGroup] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/tabgroup/)
 */
class TabGroup<C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag {

    private class DisabledTabStore(initial: List<Boolean>) : RootStore<List<Boolean>>(initial) {
        val addTab = handle { state -> state + false }

        fun disabledHandler(index: Int) = handle<Boolean> { state, disabled ->
            state.withIndex().map { if (it.index == index) disabled else it.value }
        }
    }

    private val disabledTabs = DisabledTabStore(emptyList())
    val value by lazy { DatabindingProperty<Int>() }

    /**
     * This is a "gate-keeper" of the external data flow for all internal usage!
     *
     * "External" data from `TabDatabindingHook` is *dirty*, which means there is no control, whether the index
     * is valid (enabled and in bounds!) or not. So we have to check and correct the index for internal usage.
     *
     * In order to return the corrected value to the outside, see the `render` method and the handling also by
     * `selectDefaultTab`.
     *
     * Not optimal solution, but without a buffering internal store I cannot imagine a solution with only one central
     * location for correcting the stream.
     */
    val selected by lazy {
        // set a databinding if user has not provided one
        if (!value.isSet) value(storeOf(0))

        value.data.combine(disabledTabs.data) { index, disabledStates ->
            selectDefaultTab(0, index, disabledStates)
        }
    }

    private val isActive: Store<Int?> = storeOf(null)

    private val state by lazy { selected.combine(disabledTabs.data, ::Pair) }

    private infix fun <T> Flow<T>.handledBy(handler: (Int, T, List<Boolean>) -> Int): Unit? =
        value.handler?.invoke(
            state.flatMapLatest { (currentIndex, disabledTabs) ->
                this.map { nextIndex ->
                    if (disabledTabs.all { it }) -1 else handler(currentIndex, nextIndex, disabledTabs)
                }
            })

    private fun <T> withActiveUpdates(decorated: (Int, T, List<Boolean>) -> Int): (Int, T, List<Boolean>) -> Int =
        { currentIndex, payload, disabledTabs ->
            decorated(currentIndex, payload, disabledTabs).also { isActive.update(it) }
        }

    private fun nextByClick(currentIndex: Int, nextIndex: Int, disabledTabs: List<Boolean>) =
        if (disabledTabs[nextIndex]) currentIndex
        else nextIndex

    private fun nextByKeys(currentIndex: Int, direction: Direction, disabledTabs: List<Boolean>) =
        generateSequence {
            disabledTabs.withIndex().let {
                if (direction == Direction.Next) it else it.reversed()
            }
        }.flatten()
            .drop(
                when (direction) {
                    Direction.Next -> currentIndex + 1
                    Direction.Previous -> disabledTabs.size - currentIndex
                }
            ).take(disabledTabs.size + 1)
            .firstOrNull { !it.value }?.index ?: -1

    @Suppress("UNUSED_PARAMETER")
    private fun firstByKey(currentIndex: Int, payload: Unit, disabledTabs: List<Boolean>) =
        disabledTabs.indexOf(false)

    @Suppress("UNUSED_PARAMETER")
    private fun lastByKey(currentIndex: Int, payload: Unit, disabledTabs: List<Boolean>) =
        disabledTabs.lastIndexOf(false)

    @Suppress("UNUSED_PARAMETER")
    private fun selectDefaultTab(currentIndex: Int, desiredIndex: Int, disabledTabs: List<Boolean>) =
        disabledTabs.take(minOf(desiredIndex, disabledTabs.size - 1) + 1).lastIndexOf(false)

    val componentId: String by lazy { id ?: value.id ?: Id.next() }

    private fun tabId(index: Int) = "$componentId-tab-list-tab-$index"
    private fun panelId(index: Int) = "$componentId-tab-panels-panel-$index"

    var orientation = Orientation.Horizontal

    fun render() {
        attr("id", componentId)
        // We need to emit all internal changes to the outside for realising two-way-data-binding!
        // This includes the automatic correction by `selectDefaultTab` of `selected` setup.
        selected handledBy ::selectDefaultTab
    }

    inner class TabList<CL : HTMLElement>(tag: Tag<CL>) : Tag<CL> by tag {

        private var nextIndex = 0

        private val backwardsKey by lazy {
            if (orientation == Orientation.Horizontal) Keys.ArrowLeft else Keys.ArrowDown
        }
        private val forwardKey by lazy {
            if (orientation == Orientation.Horizontal) Keys.ArrowRight else Keys.ArrowUp
        }

        fun render() {
            attr("role", Aria.Role.tablist)
            attr(Aria.orientation, orientation.toString().lowercase())

            keydowns.mapNotNull { event ->
                when (shortcutOf(event)) {
                    backwardsKey -> Direction.Previous
                    forwardKey -> Direction.Next
                    else -> null
                }.also {
                    if (it != null) {
                        event.stopImmediatePropagation()
                        event.preventDefault()
                    }
                }
            } handledBy withActiveUpdates(::nextByKeys)

            keydowns.filter { setOf(Keys.Home, Keys.PageUp).contains(shortcutOf(it)) }.map {
                it.stopImmediatePropagation()
                it.preventDefault()
            } handledBy withActiveUpdates(::firstByKey)

            keydowns.filter { setOf(Keys.End, Keys.PageDown).contains(shortcutOf(it)) }.map {
                it.stopImmediatePropagation()
                it.preventDefault()
            } handledBy withActiveUpdates(::lastByKey)
        }

        inner class Tab<CT : HTMLElement>(
            tag: Tag<CT>,
            val index: Int
        ) : Tag<CT> by tag {

            // no value should appear when list is still empty
            val disabled = disabledTabs.data.mapNotNull { it.getOrNull(index) }
            val disable by lazy { disabledTabs.disabledHandler(index) }
            val active: Flow<Boolean> = isActive.data.mapNotNull { it == index }.distinctUntilChanged()

            fun render() {
                attr("tabindex", selected.map { if (it == index) "0" else "-1" })
                attr(Aria.selected, selected.map { it == index }.asString())
                attr(Aria.controls, selected.map { if (it == index) panelId(index) else null })
                clicks.map { index } handledBy withActiveUpdates(::nextByClick)
                active.filter { it } handledBy { domNode.focus() }
                blurs.map { null } handledBy isActive.update
            }
        }

        /**
         * Factory function to create a [tab].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/tabgroup/#tab)
         */
        fun <CT : HTMLElement> RenderContext.tab(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            initialize: Tab<CT>.() -> Unit
        ): Tag<CT> {
            addComponentStructureInfo("tab", this@tab.scope, this)
            return tag(this, classes, tabId(nextIndex), scope) {
                disabledTabs.addTab()
                Tab(this, nextIndex++).run {
                    initialize()
                    render()
                }
            }
        }

        /**
         * Factory function to create a [tab] with a [HTMLButtonElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/tabgroup/#tab)
         */
        fun RenderContext.tab(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            initialize: Tab<HTMLButtonElement>.() -> Unit
        ) = tab(classes, scope, RenderContext::button, initialize)
    }

    /**
     * Factory function to create a [tabList].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/tabgroup/#tablist)
     */
    fun <CL : HTMLElement> RenderContext.tabList(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        initialize: TabList<CL>.() -> Unit
    ): Tag<CL> {
        addComponentStructureInfo("tabList", this@tabList.scope, this)
        return tag(this, classes, "$componentId-tab-list", scope) {
            TabList(this).run {
                initialize()
                render()
            }
        }
    }

    /**
     * Factory function to create a [tabList] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/tabgroup/#tablist)
     */
    fun RenderContext.tabList(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: TabList<HTMLDivElement>.() -> Unit
    ): Tag<HTMLDivElement> = tabList(classes, scope, RenderContext::div, initialize)


    inner class TabPanels<CP : HTMLElement>(
        tag: Tag<CP>
    ) : Tag<CP> by tag {

        private var panels = mutableListOf<RenderContext.() -> Tag<HTMLElement>>()

        private var nextIndex = 0

        fun render() {
            selected.render { index ->
                if (index != -1) {
                    // the index is always in bounds, so no further check is needed! See `selected` for details.
                    panels[index]()
                }
            }
        }

        /**
         * Factory function to create a [panel].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/tabgroup/#panel)
         */
        fun <CT : HTMLElement> RenderContext.panel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            content: Tag<CT>.() -> Unit
        ) {
            val currentIndex = nextIndex
            panels.add {
                tag(this, classes, panelId(currentIndex), scope) {
                    addComponentStructureInfo("parent is panel", this@add.scope, this)
                    content()
                    attr("role", Aria.Role.tabpanel)
                    attr(Aria.labelledby, tabId(currentIndex))
                }
            }
            nextIndex += 1
        }

        /**
         * Factory function to create a [panel] with a [HTMLDivElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/tabgroup/#panel)
         */
        fun RenderContext.panel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLDivElement>.() -> Unit
        ) = panel(classes, scope, RenderContext::div, content)
    }

    /**
     * Factory function to create a [tabPanels].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/tabgroup/#tabpanels)
     */
    fun <CP : HTMLElement> RenderContext.tabPanels(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CP>>,
        initialize: TabPanels<CP>.() -> Unit
    ): Tag<CP> {
        addComponentStructureInfo("tabPanels", this@tabPanels.scope, this)
        return tag(this, classes, "$componentId-tab-panels", scope) {
            TabPanels(this).run {
                initialize()
                render()
            }
        }
    }

    /**
     * Factory function to create a [tabPanels] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/tabgroup/#tabpanels)
     */
    fun RenderContext.tabPanels(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: TabPanels<HTMLDivElement>.() -> Unit
    ): Tag<HTMLDivElement> = tabPanels(classes, scope, RenderContext::div, initialize)
}

/**
 * Factory function to create a [TabGroup].
 *
 * API-Sketch:
 * ```kotlin
 * tabGroup() {
 *     val value: DatabindingProperty<Int> // optional
 *     val selected: Flow<Int>
 *     var orientation: Orientation
 *
 *     tabList() {
 *         // for each tab {
 *             tab() {
 *                 val index: Int
 *                 val disabled: Flow<Int>
 *                 val disable: SimpleHandler<Int>
 *             }
 *         // }
 *     }
 *
 *     tabPanels() {
 *         // for each tab {
 *             panel() { }
 *         // }
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/tabgroup/#tabgroup)
 */
fun <C : HTMLElement> RenderContext.tabGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: TabGroup<C>.() -> Unit
): Tag<C> {
    addComponentStructureInfo("tabGroup", this@tabGroup.scope, this)
    return tag(this, classes, id, scope) {
        TabGroup(this, id).run {
            initialize()
            render()
        }
    }
}

/**
 * Factory function to create a [TabGroup] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * tabGroup() {
 *     val value: DatabindingProperty<Int> // optional
 *     val selected: Flow<Int>
 *     var orientation: Orientation
 *
 *     tabList() {
 *         // for each tab {
 *             tab() {
 *                 val index: Int
 *                 val disabled: Flow<Int>
 *                 val disable: SimpleHandler<Int>
 *             }
 *         // }
 *     }
 *
 *     tabPanels() {
 *         // for each tab {
 *             panel() { }
 *         // }
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/tabgroup/#tabgroup)
 */
fun RenderContext.tabGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: TabGroup<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = tabGroup(classes, id, scope, RenderContext::div, initialize)
