package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.scrollintoview.*
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.math.max
import kotlin.math.min

/**
 * This data class groups all basic elements of a data collection in one object.
 *
 * @param data one-way data-binding [Flow] of a [List] of some data type [T] as data source
 * @param idProvider an optional [IdProvider] to identify items.
 * @param id an optional id for the root [Tag] of the data collection
 */
data class CollectionData<T>(val data: Flow<List<T>>, val idProvider: IdProvider<T, *>?, val id: String?)

class CollectionDataProperty<T> : Property<CollectionData<T>>() {
    operator fun invoke(data: List<T>, idProvider: IdProvider<T, *>? = null, id: String? = null) {
        value = CollectionData(flowOf(data), idProvider, id)
    }

    operator fun invoke(data: Flow<List<T>>, idProvider: IdProvider<T, *>? = null, id: String? = null) {
        value = CollectionData(data, idProvider, id)
    }

    operator fun invoke(store: Store<List<T>>, idProvider: IdProvider<T, *>? = null) {
        value = CollectionData(store.data, idProvider, store.id)
    }
}

/**
 * This [Property] alike class exposes the selection configuration of a data collection.
 * There are two modes available:
 * - [single] which needs a [DatabindingProperty] of [T]
 * - [multi] which needs a [DatabindingProperty] of [List] of [T]
 *
 * Of course both can be omitted if no selection is needed.
 */
class SelectionMode<T> {
    val single = DatabindingProperty<T?>()
    val multi = DatabindingProperty<List<T>>()

    val isSet: Boolean
        get() = single.isSet || multi.isSet

    fun use(other: SelectionMode<T>) {
        other.single.value?.let { single.use(it) }
        other.multi.value?.let { multi.use(it) }
    }
}

/**
 * This [Property] offers a nice API for configuring the [ScrollIntoViewOptions] which in term configures all
 * the scrolling behaviour if scrolling is enabled by the styling of a component.
 */
class ScrollIntoViewProperty : Property<ScrollIntoViewOptions>() {
    operator fun invoke(options: ScrollIntoViewOptions) {
        value = options
    }

    operator fun invoke(
        behavior: ScrollBehavior = ScrollBehavior.smooth,
        mode: ScrollMode = ScrollMode.ifNeeded,
        vertical: ScrollPosition = ScrollPosition.nearest,
        horizontal: ScrollPosition = ScrollPosition.nearest
    ) {
        value = ScrollIntoViewOptionsInit(behavior, mode, vertical, horizontal)
    }
}

/**
 * This class provides the building blocks to implement a all kind of data presentation UIs like data-tables or
 * grid-lists and so on.
 *
 * Use [dataCollection] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/datacollection/)
 */
@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class DataCollection<T, C : HTMLElement>(tag: Tag<C>) : Tag<C> by tag {

    val data = CollectionDataProperty<T>()

    inline fun isSame(a: T?, b: T) = data.value?.idProvider?.let { id ->
        a != null && id(a) == id(b)
    } ?: (a == b)

    inline fun indexOfItem(list: List<T>, item: T?) =
        if (item == null) -1
        else data.value?.idProvider?.let { id ->
            list.indexOfFirst { id(it) == id(item) }
        } ?: list.indexOf(item)

    private val sorting = object : RootStore<SortingOrder<T>?>(null) {}
    val sortBy = sorting.update
    val toggleSorting = sorting.handle<Sorting<T>> { old, newSorting ->
        if (old?.sorting == newSorting) {
            val newDirection = when (old.direction) {
                SortDirection.NONE -> SortDirection.ASC
                SortDirection.ASC -> SortDirection.DESC
                SortDirection.DESC -> SortDirection.NONE
            }
            old.copy(direction = newDirection)
        } else {
            SortingOrder(newSorting, SortDirection.ASC)
        }
    }

    private val filtering = object : RootStore<((List<T>) -> List<T>)?>(null) {}
    val filterBy = filtering.update
    fun filterByText(toString: (T) -> String = { it.toString() }) = filtering.handle<String> { _, text ->
        { it.filter { toString(it).lowercase().contains(text.lowercase()) } }
    }

    fun sortingDirection(s: Sorting<T>) = sorting.data.map {
        it?.let {
            if (it.sorting == s) it.direction else SortDirection.NONE
        } ?: SortDirection.NONE
    }

    val selection = SelectionMode<T>()

    inner class DataCollectionSortButton<CS : HTMLElement>(val sorting: Sorting<T>, tag: Tag<CS>) :
        Tag<CS> by tag {
        val direction = sortingDirection(sorting)

        fun render() {
            clicks.map { sorting } handledBy toggleSorting
        }
    }

    /**
     * Factory function to create a [dataCollectionSortButton].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionsortbutton)
     */
    fun <CS : HTMLElement> RenderContext.dataCollectionSortButton(
        sort: Sorting<T>,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CS>>,
        initialize: DataCollectionSortButton<CS>.() -> Unit
    ) {
        tag(this, classes, id, scope) {
            DataCollectionSortButton(sort, this).run {
                initialize()
                render()
            }
        }
    }

    /**
     * Factory function to create a [dataCollectionSortButton].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionsortbutton)
     */
    fun <CS : HTMLElement> RenderContext.dataCollectionSortButton(
        comparatorAscending: Comparator<T>,
        comparatorDescending: Comparator<T>,
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CS>>,
        initialize: DataCollectionSortButton<CS>.() -> Unit
    ) = dataCollectionSortButton(
        Sorting(comparatorAscending, comparatorDescending),
        classes,
        id,
        internalScope,
        tag,
        initialize
    )

    /**
     * Factory function to create a [dataCollectionSortButton] with a [HTMLButtonElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionsortbutton)
     */
    fun RenderContext.dataCollectionSortButton(
        sort: Sorting<T>,
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: DataCollectionSortButton<HTMLButtonElement>.() -> Unit
    ) = dataCollectionSortButton(sort, classes, id, internalScope, RenderContext::button, initialize)

    /**
     * Factory function to create a [dataCollectionSortButton] with a [HTMLButtonElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionsortbutton)
     */
    fun RenderContext.dataCollectionSortButton(
        comparatorAscending: Comparator<T>,
        comparatorDescending: Comparator<T>,
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: DataCollectionSortButton<HTMLButtonElement>.() -> Unit
    ) = dataCollectionSortButton(
        Sorting(comparatorAscending, comparatorDescending),
        classes,
        id,
        internalScope,
        initialize
    )


    inner class DataCollectionItems<CI : HTMLElement>(tag: Tag<CI>, val collectionId: String?) : Tag<CI> by tag {
        val scrollIntoView = ScrollIntoViewProperty()

        val items = if (data.isSet) {
            data.value!!.data.flatMapLatest { rawItems ->
                filtering.data.flatMapLatest { filterFunction ->
                    sorting.data.map { sortOrder ->
                        (filterFunction?.invoke(rawItems) ?: rawItems).let { filteredItems ->
                            sortOrder?.let {
                                when (it.direction) {
                                    SortDirection.NONE -> filteredItems
                                    SortDirection.ASC -> filteredItems.sortedWith(it.sorting.comparatorAscending)
                                    SortDirection.DESC -> filteredItems.sortedWith(it.sorting.comparatorDescending)
                                }
                            } ?: filteredItems
                        }
                    }
                }
            }.shareIn(MainScope() + job, SharingStarted.Eagerly, 1)
        } else flowOf(emptyList())

        private val activeItem = object : RootStore<Pair<T, Boolean>?>(null) {}

        private fun selectItem(itemsToSelect: Flow<T>) {
            if (selection.isSet) {
                if (selection.single.isSet) {
                    selection.single.handler?.let {
                        it(selection.single.data.flatMapLatest { current ->
                            itemsToSelect.map { item ->
                                if (isSame(current, item)) null else item
                            }
                        })
                    }
                } else {
                    selection.multi.handler?.let {
                        it(selection.multi.data.flatMapLatest { current ->
                            itemsToSelect.map { item ->
                                data.value?.idProvider?.let { id ->
                                    if (current.any { id(it) == id(item) }) current.filter { id(it) != id(item) } else current + item
                                } ?: if (current.contains(item)) current - item else current + item
                            }
                        })
                    }
                }
            }
        }

        fun render() {
            attr("tabindex", "0")
            attrIfNotSet("role", Aria.Role.list)

            //reset active Item when leaving dataCollectionItems
            merge(
                mouseleaves.filter { domNode != document.activeElement },
                focusouts
            ).map { null } handledBy activeItem.update

            items.flatMapLatest { list ->
                activeItem.data.flatMapLatest { current ->
                    val index = indexOfItem(list, current?.first)
                    keydowns.mapNotNull { event ->
                        when (shortcutOf(event)) {
                            Keys.ArrowUp -> list[max(index - 1, 0)]
                            Keys.ArrowDown -> list[min(index + 1, list.size - 1)]
                            Keys.Home -> list[0]
                            Keys.End -> list[list.size - 1]
                            else -> null
                        }?.let {
                            event.preventDefault()
                            event.stopImmediatePropagation()
                            it to true
                        }
                    }
                }
            } handledBy activeItem.update

            if (selection.isSet) {
                selectItem(items.flatMapLatest { list ->
                    activeItem.data.flatMapLatest { current ->
                        keydowns.filter {
                            setOf(Keys.Enter, Keys.Space).contains(shortcutOf(it))
                        }.mapNotNull { event ->
                            current?.first?.also {
                                event.preventDefault()
                                event.stopImmediatePropagation()
                            }
                        }
                    }
                }.distinctUntilChanged())
            }
        }

        inner class DataCollectionItem<CI : HTMLElement>(
            private val item: T,
            val collectionItemId: String?,
            tag: Tag<CI>
        ) : Tag<CI> by tag {
            val selected =
                if (selection.isSet) {
                    (if (selection.single.isSet) selection.single.data.map { isSame(it, item) }
                    else selection.multi.data.map { list -> list.any { isSame(it, item) } }).distinctUntilChanged()
                } else flowOf(false)

            val active = activeItem.data.map { isSame(it?.first, item) }.distinctUntilChanged()

            fun render() {
                attrIfNotSet("role", Aria.Role.listitem)

                // selection events
                if (selection.isSet) {
                    selectItem(clicks.map { item })
                }

                active.flatMapLatest { isActive ->
                    console.log("XXX")
                    mousemoves.mapNotNull {
                        if (!isActive) (item to false)
                        else null
                    }
                } handledBy activeItem.update

                if (scrollIntoView.isSet) {
                    activeItem.data.drop(1).filter { it != null && isSame(it.first, item) && it.second } handledBy {
                        scrollIntoView(domNode, scrollIntoView.value!!)
                    }
                }
            }
        }

        /**
         * Factory function to create a [dataCollectionItem].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionitem)
         */
        fun <CI : HTMLElement> RenderContext.dataCollectionItem(
            item: T,
            classes: String? = null,
            id: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CI>>,
            initialize: DataCollectionItem<CI>.() -> Unit
        ): Tag<CI> {
            val itemId = if (collectionId != null) {
                if (id != null) "$collectionId-$id"
                else if (data.value?.idProvider != null) "$collectionId-${data.value!!.idProvider!!(item).toString()}"
                else null
            } else {
                id ?: data.value?.idProvider?.invoke(item).toString()
            }

            return tag(
                this,
                if (selection.isSet) classes(classes, "cursor-pointer") else classes,
                itemId,
                scope
            ) {
                DataCollectionItem(item, itemId, this).run {
                    initialize()
                    render()
                }
            }
        }

        /**
         * Factory function to create a [dataCollectionItem] with a [HTMLDivElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionitem)
         */
        fun RenderContext.dataCollectionItem(
            item: T,
            classes: String? = null,
            id: String? = null,
            internalScope: (ScopeContext.() -> Unit) = {},
            initialize: DataCollectionItem<HTMLDivElement>.() -> Unit
        ) = dataCollectionItem(item, classes, id, internalScope, RenderContext::div, initialize)

    }

    /**
     * Factory function to create a [dataCollectionItems].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionitems)
     */
    fun <CI : HTMLElement> RenderContext.dataCollectionItems(
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CI>>,
        initialize: DataCollectionItems<CI>.() -> Unit
    ) {
        val collectionId = id ?: data.value?.id
        tag(this, classes, collectionId, scope) {
            DataCollectionItems(this, collectionId).run {
                initialize()
                render()
            }
        }
    }

    /**
     * Factory function to create a [dataCollectionItems] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollectionitems)
     */
    fun RenderContext.dataCollectionItems(
        classes: String? = null,
        id: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: DataCollectionItems<HTMLDivElement>.() -> Unit
    ) = dataCollectionItems(classes, id, internalScope, RenderContext::div, initialize)


}

/**
 * Factory function to create a [DataCollection].
 *
 * API-Sketch:
 * ```kotlin
 * dataColection<T>() {
 *     val data: CollectionDataProperty<T>
 *     val sortBy: SimpleHandler<SortingOrder<T>?>
 *     val toggleSorting: SimpleHandler<Sorting<T>>
 *     val filterBy: SimpleHandler<((List<T>) -> List<T>)?>
 *     val selection: SelectionMode<T>
 *
 *     fun filterByText(toString: (T) -> String) : SimpleHandler<String>
 *
 *     // use multiple times
 *     dataCollectionSortButton(sort: Sorting<T>) {
 *         val direction: Flow<SortDirection>
 *     }
 *
 *     dataCollectionItems() {
 *         val scrollIntoView: ScrollIntoViewProperty
 *         val items: Flow<List<T>>
 *
 *         // items.renderEach T {
 *             dataCollectionItem(item: T) {
 *                 val selected: Flow<Boolean>
 *                 val active: Flow<Boolean>
 *             }
 *         // }
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollection)
 */
fun <T, C : HTMLElement> RenderContext.dataCollection(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: DataCollection<T, C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    DataCollection<T, C>(this).run {
        initialize(this)
    }
}

/**
 * Factory function to create a [DataCollection] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * dataColection<T>() {
 *     val data: CollectionDataProperty<T>
 *     val sortBy: SimpleHandler<SortingOrder<T>?>
 *     val toggleSorting: SimpleHandler<Sorting<T>>
 *     val filterBy: SimpleHandler<((List<T>) -> List<T>)?>
 *     val selection: SelectionMode<T>
 *
 *     fun filterByText(toString: (T) -> String) : SimpleHandler<String>
 *
 *     // use multiple times
 *     dataCollectionSortButton(sort: Sorting<T>) {
 *         val direction: Flow<SortDirection>
 *     }
 *
 *     dataCollectionItems() {
 *         val scrollIntoView: ScrollIntoViewProperty
 *         val items: Flow<List<T>>
 *
 *         // items.renderEach T {
 *             dataCollectionItem(item: T) {
 *                 val selected: Flow<Boolean>
 *                 val active: Flow<Boolean>
 *             }
 *         // }
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/datacollection/#datacollection)
 */
fun <T> RenderContext.dataCollection(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: DataCollection<T, HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = dataCollection(classes, id, scope, RenderContext::div, initialize)
