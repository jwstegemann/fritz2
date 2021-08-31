package dev.fritz2.components.datatable

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.sub
import dev.fritz2.components.*
import dev.fritz2.components.foundations.Component
import dev.fritz2.dom.html.*
import dev.fritz2.styling.*
import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.*
import kotlin.collections.Map


/**
 * Central class for the *dynamic* aspects of the column configuration, so the actual *state* of column meta
 * data in contrast to the row data!
 *
 * Currently the order of the columns (which includes visibility!) and the [SortingPlan] are managed.
 *
 * There are two helper methods to ease the [State] handling:
 * - [orderedColumnsWithSorting]: Used for generating the needed structure for the header rendering
 * - [columnSortingPlan]: enriches the pure [SortingPlan] with the actual [Column]s, which is needed by the
 *                        [RowSorter] interface instead of the pure IDs only.
 */
data class State(
    val order: List<String>,
    val sortingPlan: SortingPlan,
) {
    fun <T> orderedColumnsWithSorting(columns: Map<String, Column<T>>):
            List<Pair<Column<T>, ColumnIdSorting>> =
        order.map { colId ->
            val sortingIndexForCurrentColumn = sortingPlan.indexOfFirst { (id, _) -> id == colId }
            if (sortingIndexForCurrentColumn != -1) {
                columns[colId]!! to sortingPlan[sortingIndexForCurrentColumn]
            } else {
                columns[colId]!! to ColumnIdSorting.noSorting()
            }
        }

    fun <T> columnSortingPlan(columns: Map<String, Column<T>>): ColumnSortingPlan<T> =
        sortingPlan.map { (colId, sorting) -> columns[colId]!! to sorting }
}

/**
 * Store for the column configuration that holds a [State] object.
 * It does **not** manage the actual data of the table; this is done by [RowSelectionStore]!
 *
 * Currently only one handler ([sortingChanged]) is offered, that calculates the new [SortingPlan] based upon the
 * selected [Column]-Id and its [Sorting] strategy. The required [SortingPlanReducer] is used to to the actual
 * calculation.
 *
 * On top there are some helper functions, that produces the needed [Flow]s of data for the different rendering
 * aspects: One for the header, one for the rows and one for the cells of a row.
 */
class StateStore<T, I>(private val sortingPlanReducer: SortingPlanReducer) : RootStore<State>(
    State(emptyList(), emptyList())
) {
    fun renderingHeaderData(component: DataTableComponent<T, I>) =
        data.map { it.orderedColumnsWithSorting(component.columns.value.columns) }

    fun renderingRowsData(component: DataTableComponent<T, I>, rowIdProvider: (T) -> I) =
        component.dataStore.data
            .combine(data) { data, state -> data to state }
            .combine(component.selectionStore.data) { (data, state), selectedItems ->
                val renderingRowsData = component.options.value.sorting.value.sorter.value.sortedBy(
                    data,
                    state.columnSortingPlan(component.columns.value.columns)
                ).withIndex().toList()

                val columnsSorting = state.orderedColumnsWithSorting(component.columns.value.columns)
                val renderingColumnsData = renderingRowsData.associate { (index, row) ->
                    rowIdProvider(row) to columnsSorting.map { (column, columnIdSorting) ->
                        column to IndexedValue(
                            index,
                            StatefulItem(row, selectedItems.contains(row), columnIdSorting.strategy)
                        )
                    }
                }

                renderingRowsData to renderingColumnsData
            }

    val sortingChanged = handle { state, activated: ColumnIdSorting ->
        state.copy(sortingPlan = sortingPlanReducer.reduce(state.sortingPlan, activated))
    }

    // TODO: Add handler for ordering / hiding or showing columns (change ``order`` property)
    //  Example for UI for changing: https://tailwindcomponents.com/component/table-ui-with-tailwindcss-and-alpinejs
}


/**
 * This class implements the core of the data table.
 *
 * It handles:
 * - the rendering of the table (see all ``render`` prefixed methods)
 * - the configuration (with sub-classes) and its DSL by delegation via [DataTableContext]
 * - the state management like the current sorting or ordering of the columns with an internal store and lots of
 *   helper components like the whole infrastructure around the [SortingPlan]
 * - the selection of row(s) with an internal store and the usage of [SelectionStrategy] implementations
 * - the styling via theme and ad hoc definitions within the table column and header declarations
 *
 * @see Component
 * @see DataTableContext
 */
open class DataTableComponent<T, I>(val dataStore: RootStore<List<T>>, protected val rowIdProvider: (T) -> I) :
    Component<Unit>, DataTableContext<T, I> by DefaultContext(rowIdProvider) {
    companion object {
        const val prefix = "table"
        val staticCss = staticStyle(
            prefix,
            """
                display:grid;
                min-width:100%;
                flex: 1;
                border-collapse: collapse;
                text-align: left;
                
                thead,tbody,tr {
                    display:contents;
                }
                             
                thead {             
                  grid-area:main;  
                }
                
                tbody {             
                  grid-area:main;  
                }
                                
                th,
                td {                 
                  &:last-child {
                    border-right: none;
                  }
                }
            """
        )
    }

    private val columnStateIdProvider: (Pair<Column<T>, ColumnIdSorting>) -> String = { it.first.id + it.second }

    private val stateStore: StateStore<T, I> by lazy {
        StateStore(options.value.sorting.value.reducer.value)
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        selection.value.strategy.value?.manageSelectionByExtraColumn(this)

        stateStore.update(
            State(
                columns.value.columns.values.filter { !it.hidden }.sortedBy { it.position }.map { it.id },
                emptyList()
            )
        )

        context.apply {
            this@DataTableComponent.dataStore.data handledBy this@DataTableComponent.selectionStore.syncHandler

            // preset selection via external store or flow and forward selection to external store
            when (this@DataTableComponent.selection.value.selectionMode) {
                SelectionMode.Single -> {
                    (this@DataTableComponent.selection.value.single?.store?.value?.data
                        ?: this@DataTableComponent.selection.value.single?.row!!.values) handledBy
                            this@DataTableComponent.selectionStore.updateRow
                    this@DataTableComponent.selection.value.single!!.store.value?.let {
                        this@DataTableComponent.selectionStore.selectedData.map { it.firstOrNull() } handledBy it.update
                    }
                }
                SelectionMode.Multi -> {
                    (this@DataTableComponent.selection.value.multi?.store?.value?.data
                        ?: this@DataTableComponent.selection.value.multi?.rows!!.values) handledBy
                            this@DataTableComponent.selectionStore.update
                    this@DataTableComponent.selection.value.multi!!.store.value?.let {
                        this@DataTableComponent.selectionStore.selectedData handledBy it.update
                    }
                }
                else -> Unit
            }

            div({
                styling()
                this@DataTableComponent.options.value.width.value?.also { width { it } }
                this@DataTableComponent.options.value.height.value?.also { height { it } }
                this@DataTableComponent.options.value.maxHeight.value?.also { maxHeight { it } }
                this@DataTableComponent.options.value.maxWidth.value?.also { maxWidth { it } }
                if (this@DataTableComponent.options.value.height.value != null
                    || this@DataTableComponent.options.value.width.value != null
                ) {
                    overflow { OverflowValues.auto }
                }
                css("overscroll-behavior: contain")
                position { relative { } }
            }) {
                this@DataTableComponent.renderTable(baseClass, id, prefix, this@DataTableComponent.rowIdProvider, this)
                EventsContext(this, this@DataTableComponent.selectionStore).apply {
                    this@DataTableComponent.events.value(this)
                }
            }
        }
    }

    private fun renderTable(
        baseClass: StyleClass?,
        id: String?,
        prefix: String,
        rowIdProvider: (T) -> I,
        RenderContext: RenderContext,
    ) {
        val component = this
        val tableBaseClass = if (baseClass != null) {
            baseClass + staticCss
        } else {
            staticCss
        }

        val gridCols = component.stateStore.data
            .map { (order, _) ->
                var minmax = ""
                //var header = ""
                //var footer = ""
                //var main = ""

                order.forEach { itemId ->
                    component.columns.value.columns[itemId]?.let {
                        val min = it.minWidth ?: component.options.value.cellMinWidth.value
                        val max = it.maxWidth ?: component.options.value.cellMaxWidth.value
                        minmax += "\n" + if (min == max) {
                            max
                        } else {
                            if (!min.contains(Regex("px|%"))) {
                                "minmax(${component.options.value.cellMinWidth.value}, $max)"
                            } else {
                                "minmax($min, $max)"
                            }
                        }
                    }
                }

                """
                    grid-template-columns: $minmax;                
                    grid-template-rows: auto;
                   """
            }


        if (component.header.value.fixedHeader.value) {
            renderFixedHeaderTable(
                rowIdProvider,
                gridCols,
                tableBaseClass,
                id,
                prefix,
                RenderContext
            )
        } else {
            renderSimpleTable(
                rowIdProvider,
                gridCols,
                tableBaseClass,
                id,
                prefix,
                RenderContext
            )
        }
    }

    private fun renderFixedHeaderTable(
        rowIdProvider: (T) -> I,
        gridCols: Flow<String>,
        baseClass: StyleClass,
        id: String?,
        prefix: String,
        RenderContext: RenderContext
    ) {
        val component = this
        RenderContext.apply {
            table({
                height { component.header.value.fixedHeaderHeight.value }
                overflow { OverflowValues.hidden }
                position {
                    sticky {
                        top { "0" }
                    }
                }
                zIndex { tableHeader }
            }, baseClass, "$id-fixedHeader", "$prefix-fixedHeader") {
                attr("style", gridCols)
                this@DataTableComponent.renderHeader({}, this)
                this@DataTableComponent.renderRows({
                    css("visibility:hidden")
                }, rowIdProvider, this)
            }

            table({
                margins {
                    top { "-${component.header.value.fixedHeaderHeight.value}" }
                }
                height { "fit-content" }
            }, baseClass, id, prefix) {
                attr("style", gridCols)
                this@DataTableComponent.renderHeader({
                    css("visibility:hidden")
                }, this)
                this@DataTableComponent.renderRows({}, rowIdProvider, this)
            }
        }
    }

    private fun renderSimpleTable(
        rowIdProvider: (T) -> I,
        gridCols: Flow<String>,
        baseClass: StyleClass,
        id: String?,
        prefix: String,
        RenderContext: RenderContext
    ) {
        RenderContext.apply {
            table({}, baseClass, id, prefix) {
                attr("style", gridCols)
                this@DataTableComponent.renderHeader({}, this)
                this@DataTableComponent.renderRows({}, rowIdProvider, this)
            }
        }
    }

    private fun renderHeader(
        styling: GridParams.() -> Unit,
        renderContext: RenderContext
    ) {
        val component = this
        renderContext.apply {
            thead({
                styling()
            }) {
                tr {
                    component.stateStore.renderingHeaderData(component)
                        .renderEach(component.columnStateIdProvider) { (column, sorting) ->
                            th({
                                Sorting.sorted(sorting.strategy)
                                Theme().dataTableStyles.headerStyle(
                                    this,
                                    Sorting.sorted(sorting.strategy)
                                )
                                component.header.value.styling.value()
                                column.headerStyling(this, sorting.strategy)
                            }) {
                                flexBox({
                                    height { "100%" }
                                    position { relative { } }
                                    alignItems { center }
                                }) {
                                    // Column Header Content
                                    column.headerContent(this, column)

                                    // Sorting
                                    div({
                                        Sorting.sorted(sorting.strategy)
                                        Theme().dataTableStyles.sorterStyle(
                                            this,
                                            Sorting.sorted(sorting.strategy)
                                        )
                                    }) {
                                        when {
                                            column.id == sorting.id -> {
                                                component.options.value.sorting.value.renderer.value.renderSortingActive(
                                                    this,
                                                    sorting.strategy
                                                )
                                            }
                                            column.sorting != Sorting.DISABLED -> {
                                                component.options.value.sorting.value.renderer.value.renderSortingLost(
                                                    this
                                                )
                                            }
                                            else -> {
                                                component.options.value.sorting.value.renderer.value.renderSortingDisabled(
                                                    this
                                                )
                                            }
                                        }
                                        clicks.events.map {
                                            ColumnIdSorting.of(column)
                                        } handledBy component.stateStore.sortingChanged
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    private fun renderRows(
        styling: GridParams.() -> Unit,
        rowIdProvider: (T) -> I,
        renderContext: RenderContext
    ) {
        val indexedRowIdProvider: (IndexedValue<T>) -> I = { (_, value) -> rowIdProvider(value) }
        renderContext.apply {
            tbody({
                styling()
            }) {
                this@DataTableComponent.stateStore
                    .renderingRowsData(this@DataTableComponent, rowIdProvider).let { renderingData ->
                        renderingData.map { it.first }.renderEach(indexedRowIdProvider) { (_, rowData) ->
                            val rowStore = this@DataTableComponent.dataStore.sub(rowData, rowIdProvider)
                            tr {
                                this@DataTableComponent.selection.value.strategy.value
                                    ?.manageSelectionByRowEvents(this@DataTableComponent, rowStore, this)
                                dblclicks.events.map {
                                    rowStore.current
                                } handledBy this@DataTableComponent.selectionStore.dbClickedRow

                                renderingData.mapNotNull { it.second[rowIdProvider(rowData)] }
                                    .renderEach({ columnIdProvider(it) }) { (column, statefulIndex) ->
                                        val (index, stateful) = statefulIndex
                                        td({
                                            Theme().dataTableStyles.cellStyle(
                                                this,
                                                IndexedValue(
                                                    index,
                                                    stateful.item as Any, // cast necessary, as theme can't depend on ``T``!
                                                ),
                                                stateful.selected,
                                                Sorting.sorted(statefulIndex.value.sorting)
                                            )
                                            this@DataTableComponent.columns.value.styling.value(this, statefulIndex)
                                            column.styling(this, statefulIndex)
                                        }) {
                                            this@DataTableComponent.applySelectionStyle(
                                                this@tr,
                                                stateful.selected,
                                                index,
                                                stateful.item
                                            )
                                            column.content(
                                                this,
                                                statefulIndex,
                                                if (column.lens != null) rowStore.sub(column.lens) else null,
                                                rowStore,
                                            )
                                        }
                                    }
                            }
                        }
                    }
            }
        }
    }

    private fun applySelectionStyle(renderContext: Tr, isSelected: Boolean, index: Int, rowData: T) {
        renderContext.apply {
            if (this@DataTableComponent.options.value.hovering.value.active.value) {
                className(
                    style {
                        children("&:hover td") {
                            Theme().dataTableStyles.hoveringStyle(
                                this,
                                IndexedValue(
                                    index,
                                    rowData as Any, // cast necessary, as theme can't depend on ``T``!
                                ),
                                isSelected,
                                false
                            )
                            this@DataTableComponent.options.value.hovering.value.style.value(
                                this, IndexedValue(
                                    index,
                                    StatefulItem(
                                        rowData,
                                        isSelected,
                                        Sorting.NONE
                                    )
                                )
                            )
                        }
                    }.name
                )
            }
        }
    }
}
