package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.core.Window
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import dev.fritz2.headless.foundation.utils.scrollintoview.HeadlessScrollOptions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.w3c.dom.*
import kotlin.math.max
import kotlin.math.min

/**
 * This class provides the building blocks to implement a combobox.
 *
 * Use the [combobox] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/combobox/).
 */
class Combobox<E : HTMLElement, T>(tag: Tag<E>, id: String?) : Tag<E> by tag, OpenClose() {

    private val componentId: String by lazy { id ?: Id.next() }


    inner class ItemsHook : Hook<Combobox<E, T>, Unit, Unit>() {

        operator fun invoke(items: List<T>) {
            value = { _, _ ->
                internalState.updateItems(items)
            }
        }

        operator fun invoke(items: Flow<List<T>>) {
            value = { _, _ ->
                items handledBy internalState.updateItems
            }
        }
    }

    /**
     * This hook holds the available items [T] which are queried based on the user's input.
     *
     * Invoke this hook in order to configure the desired items.
     * Both static values and [Flow]s are applicable.
     *
     * Example:
     * ```kotlin
     * items(listOf("A", "B", "C"))
     *
     * items(
     *     flow {
     *         emit(listOf("A", "B", "C")
     *         delay(1000L)
     *         emit(listOf("D", "E", "F")
     *     }
     * )
     * ```
     */
    val items: ItemsHook = ItemsHook()

    /**
     * Specifies how the String representation of an Item is generated.
     *
     * This not only needed in order to format the input's text, but also to determine whether an item returned by the
     * [filterBy] function is an exact match.
     */
    var itemFormat: (T) -> String = { it.toString() }


    /**
     * [DatabindingProperty] holding the currently selected item [T].
     *
     * When an item is selected via mouse or keyboard, it will be passed upstream via this binding.
     *
     * The other way around, all items pushed tp this binding will be reflected as the selected item.
     */
    val value: DatabindingProperty<T?> = DatabindingProperty()


    inner class FilterFunctionProperty : Property<(Sequence<T>, String) -> Sequence<T>>() {

        private val default: (Sequence<T>, String) -> Sequence<T> = { items, query ->
            items.filter { item ->
                item.toString().contains(query, ignoreCase = true)
            }
        }

        init {
            var warningShown = false
            value = { items, query ->
                // The following expression checks whether the List of available items is likely to be a List<String>
                // or not. Since we cannot type-check the generic type of a List, we have to check an actual element and
                // assume all elements have the same type. In some edge cases this might not be true but this check is
                // good enough for now :-)
                if (!warningShown && items.firstOrNull()?.let { it !is String } == true) {
                    warnAboutDefaultFilter(this@Combobox.domNode)
                    warningShown = true
                }
                default(items, query)
            }
        }


        /**
         * Enforces the use of the default filter implementation.
         *
         * > __Warning:__ Using this implementation with _non-String_ item types is not recommended unless you can
         * > ensure their `toString()` method equals exactly what the user would type in the input field of the
         * > combobox.
         *
         * In most cases, explicitly specifying a filter via [FilterFunctionProperty.invoke] is the recommended way!
         */
        fun default() {
            value = default
        }

        operator fun invoke(filter: (Sequence<T>, String) -> Sequence<T>) {
            value = filter
        }

        operator fun invoke(fieldSelector: T.() -> String) {
            value = { list, query ->
                list.filter { item ->
                    fieldSelector(item).contains(query, ignoreCase = true)
                }
            }
        }


        internal fun filter(items: Sequence<T>, query: String): Sequence<T> =
            value!!.invoke(items, query)


        private fun warnAboutDefaultFilter(domNode: HTMLElement) {
            console.warn(
                buildString {
                    append("Warning: combobox#$componentId: ")
                    append("You are implicitly using the default filter function in a component whose items are not ")
                    append("of type String! ")
                    appendLine()
                    appendLine()
                    append("This may cause problems since in the default implementation `toString()` is ")
                    append("used to find matching items for each query. Please set a custom filter via the ")
                    append("`filterBy` property.")
                    appendLine()
                    appendLine()
                    append("If the default implementation is explicitly needed, specify it's use via ")
                    append("`filterBy.default()`.")
                    appendLine()
                    appendLine()
                },
                domNode
            )
        }
    }

    /**
     * Filter function used to query the available [items] based on the user's input.
     *
     * By default, the [toString] representation of each item is used to find items matching the current query.
     * In most cases, however, a specific field (e.g. a country's name) is needed in order to get meaningful results!
     *
     * The `filterBy` property takes any function with the following signature:
     * `(Sequence<Country>, String) -> Sequence<Country>`.
     *
     * Since in practice many filterings rely on a single `String` property, a getter function returning a `String`
     * (`T.() -> String`) can be passed as well.
     *
     * > __Important:__ Using non-String items and not providing a specialized filter function may result in undefined
     * > behavior regarding the filter results! This is due to default implementation using `toString()` to find
     * > matching items for each query.
     * > If you still need to use the default filter function it can explicitly be set via
     * > [filterBy.default][FilterFunctionProperty.default].
     *
     * Example:
     * ```kotlin
     * data class Country(val code: String, val name: String)
     *
     * // ...
     *
     * filterBy(Country::name)
     *
     * // OR
     *
     * filterBy { countries, query ->
     *     countries.filter { country ->
     *         country.name.contains(query, ignoreCase = true)
     *     }
     * }
     * ```
     */
    val filterBy: FilterFunctionProperty = FilterFunctionProperty()


    inner class SelectionStrategyProperty : Property<(Sequence<T>, String) -> QueryResult<T>>() {

        private fun isExactMatch(item: T, query: String) =
            itemFormat(item).contentEquals(query, ignoreCase = true)

        private fun buildItemListResult(itemSequence: Sequence<T>, query: String): QueryResult.ItemList<T> {
            val (itemList, count) = itemSequence
                .mapIndexed(::Item)
                .fold(emptyList<Item<T>>() to 0) { (list, count), item ->
                    (list + item) to (count + 1)
                }

            return QueryResult.ItemList(
                query,
                itemList.take(maximumDisplayedItems),
                truncated = count > maximumDisplayedItems
            )
        }

        private val autoSelectMatch: (Sequence<T>, String) -> QueryResult<T> = { itemSequence, query ->
            when (val exactMatch = itemSequence.find { isExactMatch(it, query) }) {
                null -> buildItemListResult(itemSequence, query)
                else -> QueryResult.ExactMatch(exactMatch)
            }
        }


        init {
            value = ::buildItemListResult
        }

        fun autoSelectMatch() {
            value = autoSelectMatch
        }

        fun manual() {
            value = ::buildItemListResult
        }


        internal fun buildResult(filteredItems: Sequence<T>, query: String): QueryResult<T> =
            value!!.invoke(filteredItems, query)
    }

    /**
     * This property allows the configuration of the selection strategy of the combobox.
     *
     * The selection strategy controls whether an item should automatically be selected if the user types in an exact
     * match in the combobox's input or not.
     *
     * The following strategies can be configured:
     * - [autoSelectMatch()][SelectionStrategyProperty.autoSelectMatch] (__default__): Automatically select elements if
     *   they are an exact match
     * - [manual()][SelectionStrategyProperty.manual]: Render exact matches as part of the regular dropdown for the user
     *   to manually select. If no selection is made, the input is reset to the last selected value.
     *
     * Example:
     * ```kotlin
     * combobox {
     *     selectionStrategy.autoSelectMatches()
     *     // OR
     *     selectionStrategy.manual()
     * }
     * ```
     */
    val selectionStrategy: SelectionStrategyProperty = SelectionStrategyProperty()


    /**
     * Number of maximum suggested items displayed in the dropdown.
     *
     * If more items match the query than specified in this threshold, only the first n items are displayed.
     */
    var maximumDisplayedItems: Int = 20

    /**
     * Number of milliseconds to wait and debounce before the items are queried based on the user's input.
     *
     * This debouncing is in place to prevent unnecessary executions of the [filterBy] functions when the user quickly
     * inputs new text in a short amount of time (i.e. fast typing).
     *
     * The value can be adjusted in order to fine-tune the performance:
     * - A lower value may lead to a better perceived responsiveness but may slow to component down if a large amount
     *   of items is present and/or the user has a weak system
     * - A higher value may lower the perceived responsiveness but can in turn result in a better performance with large
     *   amounts of data
     *
     * > Important:__ In most cases, the default value should be the optimal value. It is a well-balanced compromise
     * > between responsiveness and general performance.
     */
    var inputDebounceMillis: Long = 50L

    /**
     * Number of milliseconds to wait and debounce before the query results are actually rendered in the dropdown.
     *
     * This debouncing ensures that the dropdown items are only rendered once if new query-results arrive
     * within the given time period (rendering is expensive).
     *
     * The value can be adjusted in order to fine-tune the performance:
     * - A lower value may lead to a better perceived responsiveness but may quickly lead to a dramatically worse
     *   render performance
     * - A higher value may lower the perceived responsiveness but generally results in a more efficient rendering
     *
     * > Important:__ In most cases, the default value should be the optimal value. It is a well-balanced compromise
     *      * > between responsiveness and general performance.
     */
    var renderDebounceMillis: Long = 50L


    private val activeIndexStore = storeOf<Int?>(null)


    private var panelReference: Tag<HTMLElement>? = null

    private var input: Tag<HTMLInputElement>? = null

    private var label: Tag<HTMLElement>? = null


    private data class InternalState<T>(
        val items: List<T> = emptyList(),
        val query: String = "",
        val opened: Boolean = false
    )


    /**
     * Represents an item consisting of its associated current [index] as well as its [value].
     *
     * Instances of this class are part of the [ItemList][QueryResult.ItemList] [QueryResult] and are passed to the
     * [comboboxItem][ComboboxItems.comboboxItem] brick in order for it to know which item it represents.
     */
    data class Item<T> internal constructor(
        val index: Int,
        val value: T
    )

    /**
     * Represents the result produced by a query.
     *
     * There are two concrete types of results:
     * - [ExactMatch]: The query matches exactly one item and should automatically be taken as the selection.
     *   If automatic selections are disabled, this type is never emitted.
     * - [ItemList]: The query returned one or more possible matches which should be rendered in the selection dropdown.
     *   In order to do so, results of this type are exposed to the user via the [ComboboxItems.results] flow.
     *
     * For more information refer to the [official documentation](https://www.fritz2.dev/headless/combobox/).
     *
     * @see ComboboxItems.comboboxItem
     */
    sealed interface QueryResult<T> {

        /**
         * Represents an exact match of an [item] produced by a query.
         *
         * See [QueryResult] for more information.
         */
        value class ExactMatch<T> internal constructor(val item: T) : QueryResult<T>

        /**
         * Represents a list of [items] produced by a [query] that should be displayed in the selection dropdown.
         *
         * The [truncated] property is set to `true if the filter function specified via [filterBy] returned more than
         * [maximumDisplayedItems] items.
         *
         * See [QueryResult] for more information.
         */
        data class ItemList<T> internal constructor(
            val query: String,
            val items: List<Item<T>>,
            val truncated: Boolean
        ) : QueryResult<T>
    }


    private val internalState = object : RootStore<InternalState<T>>(InternalState(), job) {

        val updateItems: Handler<List<T>> = handle { current, items ->
            current.copy(items = items)
        }

        val updateQuery: EmittingHandler<String, String> = handleAndEmit { current, query ->
            current.copy(query = query, opened = true).also {
                emit(query)
            }
        }

        val resetQuery: EmittingHandler<Unit, Unit> = handleAndEmit { current, _ ->
            current.copy(query = "").also {
                emit(Unit)
            }
        }

        val setOpened: Handler<Boolean> = handle { current, opened ->
            current.copy(opened = opened)
        }

        val select: EmittingHandler<T, T> = handleAndEmit { current, selection ->
            current.copy(query = "", opened = false).also {
                emit(selection)
            }
        }


        private fun computeQueryResult(items: List<T>, query: String): QueryResult<T> =
            filterBy
                .filter(items.asSequence(), query)
                .let { itemSequence ->
                    selectionStrategy.buildResult(itemSequence, query)
                }

        @OptIn(FlowPreview::class)
        val queryResults: Flow<QueryResult<T>> =
            merge(
                // Emit initial data straight-away to avoid flickering upon first opening of the dropdown:
                flowOnceOf(
                    computeQueryResult(current.items, current.query)
                ),
                // All subsequent states are computed based on the changing internal state:
                data
                    .drop(1)
                    .filter { it.opened }
                    .debounce(inputDebounceMillis)
                    .mapLatest { state -> computeQueryResult(state.items, state.query) }
                    .debounce(renderDebounceMillis)
            )

        init {
            queryResults.mapNotNull { (it as? QueryResult.ExactMatch<T>)?.item } handledBy select
        }
    }


    init {
        openState(storeOf(false))
    }


    /**
     * Factory function to create a [comboboxPanelReference].
     *
     * This brick may be used to specify an alternative anchor for the combobox's dropdown. This may be useful when
     * there are other decorative elements positioned around the input element.
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxItems)
     */
    fun <ER : HTMLElement> RenderContext.comboboxPanelReference(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<ER>>,
        content: Tag<ER>.() -> Unit
    ): Tag<ER> =
        tag(this, classes, "", scope) {
            content()
        }.also {
            panelReference = it
        }

    /**
     * Factory function to create a [comboboxPanelReference] with an [HTMLDivElement] as default root [Tag].
     *
     * This brick may be used to specify an alternative anchor for the combobox's dropdown. This is useful when
     * there are other decorative elements positioned around the input element.
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxItems)
     */
    fun RenderContext.comboboxPanelReference(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLDivElement>.() -> Unit
    ): Tag<HTMLDivElement> =
        comboboxPanelReference(classes, scope, tag = RenderContext::div, content)


    inner class ComboboxInput(tag: Tag<HTMLInputElement>) : Tag<HTMLInputElement> by tag {

        private fun handleKeyboardSelections() {
            val selectionKeydowns = keydowns {
                if (shortcutOf(key) in setOf(Keys.ArrowUp, Keys.ArrowDown, Keys.Enter, Keys.Home, Keys.End))
                    preventDefault()
            }

            internalState.queryResults.flatMapLatest { result ->
                when (result) {
                    is QueryResult.ExactMatch<T> -> flowOnceOf(null)
                    is QueryResult.ItemList<T> -> selectionKeydowns
                        .mapNotNull { event ->
                            shortcutOf(event).takeIf {
                                it in setOf(Keys.ArrowUp, Keys.ArrowDown, Keys.Enter, Keys.Home, Keys.End)
                            }
                        }
                        .mapNotNull { shortcut ->
                            val index = activeIndexStore.current ?: -1
                            val lastIndex = result.items.size - 1
                            when (shortcut) {
                                Keys.Home -> 0
                                Keys.ArrowUp -> max(index - 1, 0)
                                Keys.ArrowDown -> min(index + 1, lastIndex)
                                Keys.End -> lastIndex
                                else -> null
                            }
                        }
                }
            } handledBy activeIndexStore.update

            internalState.queryResults.flatMapLatest { result ->
                selectionKeydowns.mapNotNull {
                    val active = activeIndexStore.current
                    if (result is QueryResult.ItemList<T> && active != null && shortcutOf(it) == Keys.Enter) {
                        result.items.getOrNull(active)?.value
                    } else null
                }
            } handledBy internalState.select
        }

        fun render() {
            value(
                merge(
                    internalState.select.map { itemFormat(it) },
                    value.data.flatMapLatest { value ->
                        internalState.resetQuery.map { value?.let { itemFormat(it) } ?: "" }
                    },
                    // Update the input every time the user types in a new value. This is needed because `mountSimple`
                    // (used internally by `value()`) does not work with repeating identical values. This is needed,
                    // however, when the previous selection should be re-applied to the input.
                    inputs.values()
                ).distinctUntilChanged()
            )

            inputs.values() handledBy internalState.updateQuery


            focusins handledBy {
                open()
                domNode.select()
            }

            Window.keydowns.mapNotNull { event ->
                if (internalState.current.opened && shortcutOf(event) == Keys.Tab) false
                else null
            } handledBy internalState.setOpened

            selects.map { true } handledBy internalState.setOpened

            handleKeyboardSelections()


            // aria-controls is set automatically by the PopUpPanel
            attr("role", Aria.Role.combobox)
            attr(Aria.expanded, opened.asString())
            attr(Aria.autocomplete, "list")
        }
    }

    /**
     * Factory function to create a [comboboxInput].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxInput)
     */
    fun RenderContext.comboboxInput(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        initialize: ComboboxInput.() -> Unit
    ): Tag<HTMLInputElement> {
        addComponentStructureInfo("combobox-input", this.scope, this)
        return input(classes, "$componentId-input", scope) {
            with(ComboboxInput(this)) {
                initialize()
                render()
            }
        }.also {
            input = it
        }
    }


    /**
     * Factory function to create a [comboboxLabel].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxLabel)
     */
    fun <CL : HTMLElement> RenderContext.comboboxLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ): Tag<CL> {
        addComponentStructureInfo("combobox-label", this.scope, this)
        return tag(this, classes, "$componentId-label", scope, content).also { label = it }
    }

    /**
     * Factory function to create a [comboboxLabel] with an [HTMLLabelElement] as default root [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxLabel)
     */
    fun RenderContext.comboboxLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ): Tag<HTMLLabelElement> =
        comboboxLabel(classes, scope, tag = RenderContext::label, content)


    inner class ComboboxItems<EI : HTMLElement>(
        renderContext: RenderContext,
        tagFactory: TagFactory<Tag<EI>>,
        classes: String?,
        scope: ScopeContext.() -> Unit,
    ) : PopUpPanel<EI>(
        renderContext,
        tagFactory,
        classes,
        id = "${componentId}-items",
        scope,
        this@Combobox.opened,
        reference = panelReference ?: input ?: input { },
        ariaHasPopup = Aria.HasPopup.listbox,
    ) {

        /**
         * Emits the current query result each time the user updates the query and the result is meant to be rendered
         * in the selection dropdown.
         *
         * The emitted [QueryResult.ItemList] holds both the current [query][QueryResult.ItemList.query] and the
         * associated list of [items][QueryResult.ItemList.items].
         *
         * The dropdown can be populated by iterating over the [items][QueryResult.ItemList.items], while the
         * [query][QueryResult.ItemList.query] may optionally be used to highlight the matching passage of the item's
         * String representation.
         *
         * For more information refer to the [official documentation](https://www.fritz2.dev/headless/combobox/).
         *
         * @see comboboxItem
         */
        val results: Flow<QueryResult.ItemList<T>> =
            internalState.queryResults.mapNotNull { it as? QueryResult.ItemList<T> }


        private fun itemId(index: Int) = "$componentId-item-$index"


        inner class ComboboxItem<EJ : HTMLElement>(
            tag: Tag<EJ>,
            val active: Flow<Boolean>,
            val selected: Flow<Boolean>
        ) : Tag<EJ> by tag

        /**
         * Factory function to create a [comboboxItem].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxItem)
         */
        fun <EJ : HTMLElement> RenderContext.comboboxItem(
            item: Item<T>,
            classes: String? = null,
            scope: ScopeContext.() -> Unit = {},
            tag: TagFactory<Tag<EJ>>,
            initialize: ComboboxItem<EJ>.() -> Unit
        ): Tag<EJ> {
            addComponentStructureInfo("combobox-item", this.scope, this)
            return tag(this, classes, itemId(item.index), scope) {
                with(ComboboxItem(
                    this,
                    active = activeIndexStore.data.map { activeIndex -> item.index == activeIndex },
                    selected = value.data.map { selected -> item.value == selected }
                )) {
                    clicks.map { item.value } handledBy internalState.select
                    mouseenters.mapNotNull { item.index } handledBy activeIndexStore.update

                    active.filter { it } handledBy {
                        domNode.scrollIntoView(HeadlessScrollOptions)
                    }

                    initialize()
                }
            }
        }

        /**
         * Factory function to create a [comboboxItem] with an [HTMLDivElement] as default root [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxItem)
         */
        fun RenderContext.comboboxItem(
            item: Item<T>,
            classes: String? = null,
            scope: ScopeContext.() -> Unit = {},
            initialize: ComboboxItem<HTMLDivElement>.() -> Unit
        ): Tag<HTMLDivElement> =
            comboboxItem(item, classes, scope, tag = RenderContext::div, initialize)


        override fun render() {
            super.render()

            attr(Aria.activedescendant, activeIndexStore.data.map { it?.let(::itemId) })

            onDismiss {
                internalState.resetQuery()
                close()
            }
        }
    }

    /**
     * Factory function to create a [comboboxItems].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxItems)
     */
    fun <EI : HTMLElement> comboboxItems(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        tag: TagFactory<Tag<EI>>,
        initialize: ComboboxItems<EI>.() -> Unit
    ) {
        portal {
            addComponentStructureInfo("combobox-items", this.scope, this)
            ComboboxItems(this, tag, classes, scope).run {
                size = PopUpPanelSize.Exact
                placement = PlacementValues.bottomStart
                initialize()
                render()
            }
        }
    }

    /**
     * Factory function to create a [comboboxItems] with an [HTMLDivElement] as default root [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxItems)
     */
    fun comboboxItems(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        initialize: ComboboxItems<HTMLDivElement>.() -> Unit
    ) {
        comboboxItems(classes, scope, tag = RenderContext::div, initialize)
    }


    /**
     * Factory function to create a [comboboxValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxValidationMessages)
     */
    fun <EV : HTMLElement> RenderContext.comboboxValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<EV>>,
        initialize: ValidationMessages<EV>.() -> Unit
    ) {
        value.validationMessages.renderIf({ it.isNotEmpty() }) {
            addComponentStructureInfo("comboboxValidationMessages", this@comboboxValidationMessages.scope, this)
            tag(this, classes, "$componentId-${ValidationMessages.ID_SUFFIX}", scope) {
                initialize(ValidationMessages(value.validationMessages, this))
            }
        }
    }

    /**
     * Factory function to create a [comboboxValidationMessages] with an [HTMLDivElement] as default root [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/combobox/#comboboxValidationMessages)
     */
    fun RenderContext.comboboxValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) {
        comboboxValidationMessages(classes, scope, tag = RenderContext::div, initialize)
    }


    fun render() {
        attr("id", componentId)

        if (!value.isSet) {
            warnAboutMissingDatabinding(propertyName = "value", componentName = "combobox", componentId, domNode)
        }

        hook(items)


        value.data.mapNotNull { it } handledBy internalState.select
        value.handler?.invoke(this, internalState.select)

        opened handledBy internalState.setOpened
        openState.handler?.invoke(this, internalState.data.map { it.opened }.distinctUntilChanged())

        internalState.data.map { null } handledBy activeIndexStore.update


        label?.let {
            attr(Aria.labelledby, it.id)
        }
    }
}

/**
 * Factory function to create a [Combobox].
 *
 * API-Sketch:
 * ```kotlin
 * combobox<T> {
 *     val items: ItemsHook()
 *     // params: List<T> / Flow<List<T>>
 *
 *     var itemFormat: (T) -> String
 *
 *     val value: DatabindingProperty<T>
 *
 *     var filterBy: FilterFunctionProperty
 *     // params: (Sequence<T>, String) -> Sequence<T> / T.() -> String
 *
 *     val selectionStrategy: SelectionStrategyProperty
 *     // methods: autoSelectMatches() / manual()
 *
 *     var maximumDisplayedItems: Int = 20
 *     var inputDebounceMillis: Long = 50L
 *     var renderDebounceMillis: Long = 50L
 *
 *     comboboxPanelReference() { }
 *     comboboxInput() { }
 *     comboboxLabel() { }
 *     comboboxItems() {
 *         // inherited by `PopUpPanel`
 *         var placement: Placement
 *         var strategy: Strategy
 *         var flip: Boolean
 *         var skidding: Int
 *         var distance: int
 *
 *         val results: Flow<QueryResult.ItemList<T>>
 *
 *         // state.render {
 *             // for each QueryResult.ItemList<T>.Item<T> {
 *                 comboboxItem(Item<T>) { }
 *             // }
 *         // }
 *     }
 *     comboboxValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/listbox/#listbox)
 */
fun <E : HTMLElement, T> RenderContext.combobox(
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
    tag: TagFactory<Tag<E>>,
    initialize: Combobox<E, T>.() -> Unit
): Tag<E> {
    addComponentStructureInfo("combobox", this.scope, this)
    return tag(this, classes, id, scope) {
        with(Combobox<E, T>(this, id)) {
            initialize()
            render()
        }
    }
}

/**
 * Factory function to create a [Combobox]  with an [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * combobox<T> {
 *     val items: ItemsHook()
 *     // params: List<T> / Flow<List<T>>
 *
 *     var itemFormat: (T) -> String
 *
 *     val value: DatabindingProperty<T>
 *
 *     var filterBy: FilterFunctionProperty
 *     // params: (Sequence<T>, String) -> Sequence<T> / T.() -> String
 *
 *     val selectionStrategy: SelectionStrategyProperty
 *     // methods: autoSelectMatches() / manual()
 *
 *     var maximumDisplayedItems: Int = 20
 *     var inputDebounceMillis: Long = 50L
 *     var renderDebounceMillis: Long = 50L
 *
 *     comboboxPanelReference() { }
 *     comboboxInput() { }
 *     comboboxLabel() { }
 *     comboboxItems() {
 *         // inherited by `PopUpPanel`
 *         var placement: Placement
 *         var strategy: Strategy
 *         var flip: Boolean
 *         var skidding: Int
 *         var distance: int
 *
 *         val results: Flow<QueryResult.ItemList<T>>
 *
 *         // state.render {
 *             // for each QueryResult.ItemList<T>.Item<T> {
 *                 comboboxItem(Item<T>) { }
 *             // }
 *         // }
 *     }
 *     comboboxValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/listbox/#listbox)
 */
fun <T> RenderContext.combobox(
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
    initialize: Combobox<HTMLDivElement, T>.() -> Unit
): Tag<HTMLDivElement> =
    combobox(classes, id, scope, tag = RenderContext::div, initialize)