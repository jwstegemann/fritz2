package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.scrollintoview.HeadlessScrollOptions
import dev.fritz2.headless.foundation.utils.scrollintoview.scrollIntoView
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.math.max
import kotlin.math.min


//FIXME: use IdProvider here
class TableDataProperty<T> : Property<Pair<Flow<List<T>>, IdProvider<T,*>?>>() {
    operator fun invoke(data: List<T>, idProvider: IdProvider<T,*>? = null) {
        value = flowOf(data) to idProvider
    }

    operator fun invoke(data: Flow<List<T>>, idProvider: IdProvider<T,*>? = null) {
        value = data to idProvider
    }
}

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


enum class SortDirection {
    NONE, ASC, DESC
}

//TODO: make this nullable to allow sorting only in one direction?
data class Sorting<T>(val comparatorAscending: Comparator<T>, val comparatorDescending: Comparator<T>)
data class SortingOrder<T>(val sorting: Sorting<T>, val direction: SortDirection)

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class DataCollection<T, C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag {

    val data = TableDataProperty<T>()

    private val sorting = object : RootStore<SortingOrder<T>?>(null) {}
    val sortBy = sorting.handle<Sorting<T>> { old, newSorting ->
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
    val filterByText = filtering.handle<String> { _, text ->
        { it.filter { it.toString().lowercase().contains(text.lowercase()) } }
    }

    fun sortingDirection(s: Sorting<T>) = sorting.data.map {
        it?.let {
            if (it.sorting == s) it.direction else SortDirection.NONE
        } ?: SortDirection.NONE
    }

    val selection = SelectionMode<T>()

    inner class DataCollectionSortButton<CS : HTMLElement>(private val sorting: Sorting<T>, tag: Tag<CS>) :
        Tag<CS> by tag {
        val direction = sortingDirection(sorting)

        fun render() {
            clicks.map { sorting } handledBy sortBy
        }
    }

    fun <CS : HTMLElement> RenderContext.dataCollectionSortButton(
        sort: Sorting<T>,
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CS>>,
        initialize: DataCollectionSortButton<CS>.() -> Unit
    ) {
        //TODO: id
        tag(this, classes, "someID", scope) {
            DataCollectionSortButton(sort, this).run {
                initialize()
                render()
            }
        }
    }

    fun RenderContext.dataCollectionSortButton(
        sort: Sorting<T>,
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: DataCollectionSortButton<HTMLButtonElement>.() -> Unit
    ) = dataCollectionSortButton(sort, classes, internalScope, RenderContext::button, initialize)

    fun RenderContext.dataCollectionSortButton(
        comparatorAscending: Comparator<T>,
        comparatorDescending: Comparator<T>,
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: DataCollectionSortButton<HTMLButtonElement>.() -> Unit
    ) = dataCollectionSortButton(Sorting(comparatorAscending, comparatorDescending), classes, internalScope, initialize)

    inner class DataCollectionItems<CI : HTMLElement>(tag: Tag<CI>) : Tag<CI> by tag {
        val items = if (data.isSet) {
            data.value!!.first.flatMapLatest { list ->
                sorting.data.flatMapLatest { sortOrder ->
                    filtering.data.map { filterFunction ->
                        (filterFunction?.invoke(list) ?: list).let { list ->
                            sortOrder?.let {
                                when (it.direction) {
                                    SortDirection.NONE -> list
                                    SortDirection.ASC -> list.sortedWith(it.sorting.comparatorAscending)
                                    SortDirection.DESC -> list.sortedWith(it.sorting.comparatorDescending)
                                }
                            } ?: list
                        }
                    }
                }
            }.distinctUntilChanged()
        } else flowOf(emptyList())

        val activeIndex = storeOf(-1)

        fun selectItem(itemsToSelect: Flow<T>) {
            if (selection.isSet) {
                if (selection.single.isSet) {
                    selection.single.handler?.let {
                        it(selection.single.data.flatMapLatest { current ->
                            itemsToSelect.map { item ->
                                data.value?.second?.let { id ->
                                    if (current != null && id(current) == id(item)) null else item
                                } ?: if (current == item) null else item
                            }
                        })
                    }
                } else {
                    selection.multi.handler?.let {
                        it(selection.multi.data.flatMapLatest { current ->
                            itemsToSelect.map { item ->
                                data.value?.second?.let { id ->
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

            activeIndex.data.flatMapLatest { index ->
                items.map { it.size }.distinctUntilChanged().flatMapLatest { size ->
                    keydowns.mapNotNull { event ->
                        console.log("key")
                        when (shortcutOf(event)) {
                            Keys.ArrowUp -> max(index - 1, 0)
                            Keys.ArrowDown -> min(index + 1, size - 1)
                            Keys.Home -> 0
                            Keys.End -> size - 1
                            else -> null
                        }.also {
                            if (it != null) {
                                event.preventDefault()
                                event.stopImmediatePropagation()
                            }
                        }
                    }
                }
            }.onEach { console.log(it) } handledBy activeIndex.update

            if (selection.isSet) {
                selectItem(activeIndex.data.flatMapLatest { index ->
                    items.flatMapLatest { list ->
                        keydowns.filter {
                            setOf(Keys.Enter, Keys.Space).contains(shortcutOf(it))
                        }.mapNotNull { event ->
                            list.getOrNull(index).also {
                                if (it != null) {
                                    event.preventDefault()
                                    event.stopImmediatePropagation()
                                }
                            }
                        }
                    }
                })
            }
        }

        inner class DataCollectionItem<CI : HTMLElement>(private val item: T, tag: Tag<CI>) : Tag<CI> by tag {
            fun indexOfItem(list: List<T>, item: T) = data.value?.second?.let { id ->
                    list.indexOfFirst { id(it) == id(item) }
                } ?: list.indexOf(item)

            fun isSame(a: T?, b: T) = data.value?.second?.let { id ->
                a != null && id(a) == id(b)
            } ?: (a == b)

            val selected by lazy {
                if (selection.isSet) {
                    (if (selection.single.isSet) selection.single.data.map { isSame(it, item) }
                    else  selection.multi.data.map { list -> list.any {isSame(it, item) } }).distinctUntilChanged()
                } else flowOf(false)
            }

            val active = items.flatMapLatest { list ->
                    activeIndex.data.map {
                         indexOfItem(list,item)== it
                    }
                }.distinctUntilChanged()

            fun render() {
                attrIfNotSet("role", Aria.Role.listitem)

                // selection events
                if (selection.isSet) {
                    selectItem(clicks.map { item })
                }

                items.flatMapLatest { list ->
                    mouseenters.debounce(100).map {
                        indexOfItem(list, item)
                    }
                } handledBy activeIndex.update

                // scroll if active
                //FIXME: scroll to top-element?
                active.filter { it } handledBy {
                    scrollIntoView(domNode, HeadlessScrollOptions)
                }
            }
        }

        fun <CI : HTMLElement> RenderContext.dataCollectionItem(
            item: T,
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CI>>,
            initialize: DataCollectionItem<CI>.() -> Unit
        ) = tag(this, if (selection.isSet) classes(classes, "cursor-pointer") else classes, "someID", scope) { //TODO: id
            DataCollectionItem(item, this).run {
                initialize()
                render()
            }
        }

        fun RenderContext.dataCollectionItem(
            item: T,
            classes: String? = null,
            internalScope: (ScopeContext.() -> Unit) = {},
            initialize: DataCollectionItem<HTMLDivElement>.() -> Unit
        ) = dataCollectionItem(item, classes, internalScope, RenderContext::div, initialize)

    }

    fun <CI : HTMLElement> RenderContext.dataCollectionItems(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CI>>,
        initialize: DataCollectionItems<CI>.() -> Unit
    ) {
        //TODO: id
        tag(this, classes, "someID", scope) {
            DataCollectionItems(this).run {
                initialize()
                render()
            }
        }
    }

    fun RenderContext.dataCollectionItems(
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: DataCollectionItems<HTMLDivElement>.() -> Unit
    ) = dataCollectionItems(classes, internalScope, RenderContext::div, initialize)


}

fun <T, C : HTMLElement> RenderContext.dataCollection(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: DataCollection<T, C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    DataCollection<T, C>(this, id).run {
        initialize(this)
    }
}

fun <T> RenderContext.dataCollection(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: DataCollection<T, HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = dataCollection(classes, id, scope, RenderContext::div, initialize)
