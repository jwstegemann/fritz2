package dev.fritz2.headless.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.ScopeContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.foundation.Property
import dev.fritz2.headless.foundation.TagFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class DataCollection<T, C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag {

    val data = TableDataProperty<T>()

    fun render() {

    }



    inner class DataCollectionItems<CI : HTMLElement>(tag: Tag<CI>) : Tag<CI> by tag {
        val items: Flow<List<T>> = if (data.isSet) data.value!! else flowOf(emptyList())

        fun render() {

        }

        inner class DataCollectionItem<CI : HTMLElement>(tag: Tag<CI>) : Tag<CI> by tag {
            fun render() {

            }
        }

        fun <CI : HTMLElement> RenderContext.dataCollectionItem(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CI>>,
            initialize: DataCollectionItem<CI>.() -> Unit
        ) {
            //TODO: id
            tag(this, classes, "someID", scope) {
                DataCollectionItem(this).run {
                    initialize()
                    render()
                }
            }
        }

        fun RenderContext.dataCollectionItem(
            classes: String? = null,
            internalScope: (ScopeContext.() -> Unit) = {},
            initialize: DataCollectionItem<HTMLDivElement>.() -> Unit
        ) = dataCollectionItem(classes, internalScope, RenderContext::div, initialize)

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