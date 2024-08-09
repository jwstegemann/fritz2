package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement


typealias ComboboxFilterFunction<T> = (List<T>, String) -> List<T>


class Combobox<T, E : HTMLElement>(tag: Tag<E>, id: String?) : Tag<E> by tag, OpenClose() {

    // TODO: Add structure hints
    private val componentId: String by lazy { id ?: Id.next() }


    inner class ItemsProperty : Property<Flow<List<T>>>() {
        operator fun invoke(filter: List<T>) {
            value = flowOf(filter)
        }

        operator fun invoke(filterFlow: Flow<List<T>>) {
            value = filterFlow
        }
    }

    val items: ItemsProperty = ItemsProperty()


    val value: DatabindingProperty<T> = DatabindingProperty()


    inner class FilterFunctionProperty : Property<Flow<ComboboxFilterFunction<T>>>() {
        init {
            value = flowOf { list, query ->
                list.filter { item ->
                    item.toString().contains(query, ignoreCase = true)
                }
            }
        }

        operator fun invoke(filter: ComboboxFilterFunction<T>) {
            value = flowOf(filter)
        }

        operator fun invoke(filterFlow: Flow<ComboboxFilterFunction<T>>) {
            value = filterFlow
        }
    }

    val filterBy: FilterFunctionProperty = FilterFunctionProperty()


    private var input: Tag<HTMLInputElement>? = null


    private data class State<T>(
        val items: List<T> = emptyList(),
        val filter: ComboboxFilterFunction<T> = { l, _ -> l },
        val query: String = "",
        val opened: Boolean = false
    )

    private val internalState = object : RootStore<State<T>>(State(), job) {

        val filteredItems: Flow<List<T>> = data.mapNotNull { state ->
            if (state.opened) {
                state.filter(
                    when {
                        state.query.isEmpty() -> state.items
                        else -> state.items.take(100)
                    },
                    state.query
                )
            } else null
        }

        val updateItems: Handler<List<T>> = handle { current, items ->
            current.copy(items = items)
        }

        val updateFilter: Handler<ComboboxFilterFunction<T>> = handle { current, filter ->
            current.copy(filter = filter)
        }

        val updateQuery: EmittingHandler<String, String> = handleAndEmit { current, query ->
            current.copy(query = query).also {
                emit(query)
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
    }


    init {
        openState(storeOf(false))
    }


    fun <EB : HTMLInputElement> comboboxInput(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        tag: TagFactory<Tag<EB>>,
        itemFormat: (T) -> String = { it.toString() },
        initialize: Tag<EB>.() -> Unit
    ) {
        tag(this, classes, "$componentId-input", scope) {
            value(
                merge(
                    internalState.select.map { itemFormat(it) },
                    internalState.updateQuery
                )
            )
            focusins handledBy {
                open()
                domNode.select()
            }
            inputs.values() handledBy internalState.updateQuery
            initialize()
        }.also {
            input = it
        }
    }


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
        reference = this@Combobox.input,
        ariaHasPopup = Aria.HasPopup.listbox
    ) {
        val items: Flow<List<T>> = internalState.filteredItems

        private var indexCounter = 0
        init {
            items handledBy { indexCounter = 0 }
        }

        // TODO Expose active and selected states
        inner class ComboboxItem<EJ : HTMLElement>(
            tag: Tag<EJ>,
            val query: Flow<String>
        ) : Tag<EJ> by tag

        fun <EJ : HTMLElement> comboboxItem(
            classes: String? = null,
            scope: ScopeContext.() -> Unit = {},
            tag: TagFactory<Tag<EJ>>,
            item: T,
            initialize: ComboboxItem<EJ>.() -> Unit
        ): Tag<EJ> =
            tag(this, classes, "$componentId-item-${indexCounter++}", scope) {
                with(ComboboxItem(this, internalState.data.map { it.query })) {
                    clicks.map { item } handledBy internalState.select
                    initialize()
                }
            }
    }

    fun <EI : HTMLElement> comboboxItems(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        tag: TagFactory<Tag<EI>>,
        initialize: ComboboxItems<EI>.() -> Unit
    ) {
        portal {
            ComboboxItems(this, tag, classes, scope).run {
                size = PopUpPanelSize.Min
                // TODO: Make placement configurable
                placement = PlacementValues.bottomStart
                initialize()
                render()
            }
        }
    }


    fun render() {
        if (!value.isSet) {
            warnAboutMissingDatabinding(propertyName = "value", componentName = "combobox", componentId, domNode)
        }

        items.value?.handledBy(internalState.updateItems)
        filterBy.value?.handledBy(internalState.updateFilter)

        // TODO Handle values from outside
        //value.data.map { valueFormat(it) } handledBy internalState.updateQuery
        value.handler?.invoke(this, internalState.select)

        opened handledBy internalState.setOpened
        openState.handler?.invoke(this, internalState.data.map { it.opened })
    }
}

fun <T, E : HTMLElement> RenderContext.combobox(
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
    tag: TagFactory<Tag<E>>,
    initialize: Combobox<T, E>.() -> Unit
) {
    tag(this, classes, id, scope) {
        with(Combobox<T, E>(this, id)) {
            initialize()
            render()
        }
    }
}