package dev.fritz2.components.datatable

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SubStore
import dev.fritz2.components.checkbox
import dev.fritz2.dom.html.Tr
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map


/**
 * This store manages the selected rows of the data table.
 * It does **not** manage the state of the columns (configuration meta data like sorting, order and so on); this is
 * done by [StateStore]!
 */
class RowSelectionStore<T, I>(private val rowIdProvider: (T) -> I) : RootStore<List<T>>(emptyList()) {

    /**
     * The first item must always be dropped; need to find out why!
     */
    val selectedData = data.drop(1)

    fun isDataRowSelected(item: T) = data.map { selectedRows -> selectedRows.contains(item) }

    /**
     * This handler can be used to synchronize the selection with the actual data of the table.
     * If some item gets deleted, it should also disappear from the selection.
     */
    val syncHandler = handle<List<T>> { old, allItems ->
        old.mapNotNull { oldItem -> allItems.firstOrNull { rowIdProvider(it) == rowIdProvider(oldItem) } }
    }

    val selectRows = handleAndEmit<T, List<T>> { selectedRows, new ->
        val newSelection = if (selectedRows.contains(new))
            selectedRows - new
        else {
            val temp = selectedRows.map { if (rowIdProvider(it) == rowIdProvider(new)) new else it }
            if (temp.any { rowIdProvider(it) == rowIdProvider(new) }) temp else temp + new
        }
        emit(newSelection)
        newSelection
    }

    val selectRow = handle<T?> { old, new ->
        val newSelection = if (new == null || old.firstOrNull() == new) {
            emptyList()
        } else {
            listOf(new)
        }
        newSelection
    }

    /**
     * This handler can be used to preselect one single row for the initial rendering of the table.
     * As the store is made to handle a [List] of rows as well as a single selection, we need this special
     * [Flow] for preselecting a single row. For preselecting arbitrary rows, just stick to the default [update]
     * handler!
     */
    val updateRow = handle<T?> { _, new ->
        if (new == null) emptyList() else listOf(new)
    }

    val dbClickedRow = handleAndEmit<T, T> { selectedRows, new ->
        emit(new)
        selectedRows
    }
}

/**
 * This interface defines a strategy type in order to implement different selection mechanisms.
 *
 * As we have identified already two different methods for selecting rows and provide default implementations for them,
 * it is necessary to separate the implementation from the core data table component.
 *
 * - [SelectionByClick]: selecting by clicking onto a row
 * - [SelectionByCheckBox]: selection by checkbox added with an additional column
 *
 * And of course we can use the strategy pattern, to provide the *null object* by [NoSelection] implementation, if
 * the selection is disabled for the table.
 */
interface SelectionStrategy<T, I> {
    fun manageSelectionByExtraColumn(component: DataTableComponent<T, I>)
    fun manageSelectionByRowEvents(
        component: DataTableComponent<T, I>, rowStore: SubStore<List<T>, T>,
        renderContext: Tr
    )
}

/**
 * Selection strategy for disabling the selection at all.
 *
 * Acts as the *null object* pattern.
 *
 * @see [SelectionStrategy]
 */
class NoSelection<T, I> : SelectionStrategy<T, I> {
    override fun manageSelectionByExtraColumn(component: DataTableComponent<T, I>) {
        // no extra column needed -> nothing should get selected!
    }

    override fun manageSelectionByRowEvents(
        component: DataTableComponent<T, I>,
        rowStore: SubStore<List<T>, T>,
        renderContext: Tr
    ) {
        // don't wire events -> nothing should get selected!
    }
}

/**
 * This selection strategy offers the selection via an additional checkbox per row, which is added in front of the
 * row as artificial column.
 *
 * @see [SelectionStrategy]
 */
class SelectionByCheckBox<T, I> : SelectionStrategy<T, I> {

    private val commonSettings: ColumnsContext.ColumnContext<T>.(
        component: DataTableComponent<T, I>
    ) -> Unit = { component ->
        width {
            min("60px")
            max("60px")
        }
        sorting { disabled }
        content { _, _, rowStore ->
            checkbox(
                {
                    margin { "0" }
                }, id = uniqueId()
            ) {
                checked(
                    component.selectionStore.data.map { selectedRows ->
                        selectedRows.contains(rowStore.current)
                    }
                )
                events {
                    when (component.selection.value.selectionMode) {
                        SelectionMode.Single ->
                            clicks.events.map { rowStore.current } handledBy component.selectionStore.selectRow
                        SelectionMode.Multi ->
                            clicks.events.map { rowStore.current } handledBy component.selectionStore.selectRows
                        else -> Unit
                    }
                }
            }
        }
    }

    private val headerSettings: ColumnsContext.ColumnContext<T>.(
        component: DataTableComponent<T, I>
    ) -> Unit = { component ->
        header {
            checkbox({ display { inlineBlock } }, id = uniqueId()) {
                checked(
                    component.selectionStore.data.map {
                        it.isNotEmpty() && it == component.dataStore.current
                    }
                )
                events {
                    changes.states().map { selected ->
                        if (selected) {
                            component.dataStore.current
                        } else {
                            emptyList()
                        }
                    } handledBy component.selectionStore.update
                }
            }
        }
    }

    override fun manageSelectionByExtraColumn(component: DataTableComponent<T, I>) {
        with(component) {
            prependAdditionalColumns {
                when (selection.value.selectionMode) {
                    SelectionMode.Multi -> {
                        column {
                            headerSettings(component)
                            commonSettings(component)
                        }
                    }
                    SelectionMode.Single -> {
                        column {
                            commonSettings(component)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun manageSelectionByRowEvents(
        component: DataTableComponent<T, I>,
        rowStore: SubStore<List<T>, T>,
        renderContext: Tr
    ) {
        // don't wire events -> extra column with checkbox handles everything!
    }
}

/**
 * This selection strategy enables the selection by clicking onto a row of the table.
 *
 * @see [SelectionStrategy]
 */
class SelectionByClick<T, I> : SelectionStrategy<T, I> {
    override fun manageSelectionByExtraColumn(component: DataTableComponent<T, I>) {
        // no extra column needed -> selection is handled by clicks!
    }

    override fun manageSelectionByRowEvents(
        component: DataTableComponent<T, I>,
        rowStore: SubStore<List<T>, T>,
        renderContext: Tr
    ) {
        renderContext.apply {
            when (component.selection.value.selectionMode) {
                SelectionMode.Single ->
                    clicks.events.map { rowStore.current } handledBy component.selectionStore.selectRow
                SelectionMode.Multi ->
                    clicks.events.map { rowStore.current } handledBy component.selectionStore.selectRows
                else -> Unit
            }
        }
    }
}
