package dev.fritz2.headless.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.RootStore
import dev.fritz2.core.ScopeContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.foundation.DatabindingProperty
import dev.fritz2.headless.foundation.Property
import dev.fritz2.headless.foundation.TagFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class TableDataProperty<T> : Property<Flow<List<T>>>() {
    operator fun invoke(data: List<T>) {
        value = flowOf(data)
    }

    operator fun invoke(data: Flow<List<T>>) {
        value = data
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
    val sort = sorting.handle<Sorting<T>> { old, newSorting ->
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

    fun sortingDirection(s: Sorting<T>) = sorting.data.map {
        it?.let {
            if (it.sorting == s) it.direction else SortDirection.NONE
        } ?: SortDirection.NONE
    }

    val selection = SelectionMode<T>()

    fun render() {
    }

    inner class DataCollectionSortButton<CS : HTMLElement>(private val sorting: Sorting<T>, tag: Tag<CS>) :
        Tag<CS> by tag {
        val direction = sortingDirection(sorting)

        //FIXME: ist hier bei allen Komponenten ein Receiver, wenn handledBy gecalled wird?
        fun Tag<CS>.render() {
            clicks.map { sorting } handledBy sort
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
        val items: Flow<List<T>> = if (data.isSet) {
            data.value!!.flatMapLatest { list ->
                sorting.data.map {
                    it?.let {
                        when (it.direction) {
                            SortDirection.NONE -> list
                            SortDirection.ASC -> list.sortedWith(it.sorting.comparatorAscending)
                            SortDirection.DESC -> list.sortedWith(it.sorting.comparatorDescending)
                        }
                    } ?: list
                }
            }
        } else flowOf(emptyList())

        fun render() {

        }

        inner class DataCollectionItem<CI : HTMLElement>(private val item: T, tag: Tag<CI>) : Tag<CI> by tag {
            val selected by lazy {
                if (selection.isSet) {
                    if (selection.single.isSet) selection.single.data.map { it == item }
                    else  selection.multi.data.map { it.contains(item)}
                } else flowOf(false)
            }

            fun render() {
                if (selection.isSet) {
                    console.log("sel")
                    if (selection.single.isSet) {
                        console.log("single")
                        selection.single.handler?.let {
                            console.log("handler")
                            it(selection.single.data.flatMapLatest { current ->
                                clicks.map {
                                    console.log("clicked ${current.toString().take(10)}")
                                    if (current == item) null else item
                                }
                            })
                        }
                    } else {
                        selection.multi.handler?.let {
                            it(selection.multi.data.flatMapLatest { current ->
                                clicks.map {
                                    if (current.contains(item)) current - item else current + item
                                }
                            })
                        }
                    }
                }

            }
        }

        fun <CI : HTMLElement> RenderContext.dataCollectionItem(
            item: T,
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CI>>,
            initialize: DataCollectionItem<CI>.() -> Unit
        ) = tag(this, classes, "someID", scope) { //TODO: id
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
        render()
    }
}

fun <T> RenderContext.dataCollection(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: DataCollection<T, HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = dataCollection(classes, id, scope, RenderContext::div, initialize)


/*

collection<T> {

    data: Property<low<List<T>>>

    //active:

    selection.single(Datatbinding List/Item) // Strategie für single oder multi

    filter(Databinding Flow<(List)->List)?> // sonst wird das intern verwaltet

    sort(Databinding Flow<(List)->List)?> // sonst wird das intern verwaltet

    //wir brauchen den IdProvider (oder eben keinen)


    collectionSorter {
        val sorted: Flow<SortOrder> (no, asc, desc)
        collectionSorterToggle {

        }
    }


    collectionItems {
        val items: <Flow<List<t>>

        items.withIndex().renderEach { (i, item) ->
            collectionItem(item, tag = RenderContext::tr) {
                val active: Flow<Boolean>
                val selected: : Flow<Boolean>


            }
        }
    }

}







 */