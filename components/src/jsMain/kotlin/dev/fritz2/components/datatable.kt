package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.components.datatable.DataTableComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.GridParams

/**
 * This component factory generates a data table.
 *
 * It provides the following main functions:
 * - automatic scrolling enabled by default including a fixed header, so this line always remains visible
 * - sorting facility including easily defining specific type based sorting for each column separately (lexicographic
 *   sorting is the default, thus [String] based
 * - default styling via fritz2's [dev.fritz2.styling.theme.Theme] infrastructure
 * - variety of custom styling for header and cells, row based or column based
 * - styling based upon the index and current stateful information like the sorting or selection state.
 * - custom content for header and column cells including access to current row data
 * - different modes for selecting rows (checkbox and click based) and separation between single or multiple selection
 *
 * @see DataTableComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param rows the required store holding the data rows that should be rendered by the table
 * @param rowIdProvider an appropriate id provider for the specific row type ``T`` (typically some sort of Id property)
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form $prefix-$hash
 * @param build a lambda expression for setting up the component itself. Details in [DataTableComponent]
 */
fun <T, I> RenderContext.dataTable(
    styling: GridParams.() -> Unit = {},
    rows: RootStore<List<T>>,
    rowIdProvider: (T) -> I,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = DataTableComponent.prefix,
    build: DataTableComponent<T, I>.() -> Unit = {}
) {
    DataTableComponent(rows, rowIdProvider).apply(build).render(this, styling, baseClass, id, prefix)
}

/**
 * This component factory generates a data table.
 *
 * It provides the following main functions:
 * - automatic scrolling enabled by default including a fixed header, so this line always remains visible
 * - sorting facility including easily defining specific type based sorting for each column separately (lexicographic
 *   sorting is the default, thus [String] based
 * - default styling via fritz2's [dev.fritz2.styling.theme.Theme] infrastructure
 * - variety of custom styling for header and cells, row based or column based
 * - styling based upon the index and current stateful information like the sorting or selection state.
 * - custom content for header and column cells including access to current row data
 * - different modes for selecting rows (checkbox and click based) and separation between single or multiple selection
 *
 * @see DataTableComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param rows the required store holding the data rows that should be rendered by the table
 * @param rowIdProvider an appropriate id provider for the specific row type ``T`` (typically some sort of Id property)
 * @param selection a store that holds arbitrary rows that get selected
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form $prefix-$hash
 * @param build a lambda expression for setting up the component itself. Details in [DataTableComponent]
 */
fun <T, I> RenderContext.dataTable(
    styling: GridParams.() -> Unit = {},
    rows: RootStore<List<T>>,
    rowIdProvider: (T) -> I,
    selection: Store<List<T>>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = DataTableComponent.prefix,
    build: DataTableComponent<T, I>.() -> Unit = {}
) {
    DataTableComponent(rows, rowIdProvider).apply {
        build()
        selection { multi { store(selection) } }
    }.render(this, styling, baseClass, id, prefix)
}

/**
 * This component factory generates a data table.
 *
 * It provides the following main functions:
 * - automatic scrolling enabled by default including a fixed header, so this line always remains visible
 * - sorting facility including easily defining specific type based sorting for each column separately (lexicographic
 *   sorting is the default, thus [String] based
 * - default styling via fritz2's [dev.fritz2.styling.theme.Theme] infrastructure
 * - variety of custom styling for header and cells, row based or column based
 * - styling based upon the index and current stateful information like the sorting or selection state.
 * - custom content for header and column cells including access to current row data
 * - different modes for selecting rows (checkbox and click based) and separation between single or multiple selection
 *
 * @see DataTableComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param rows the required store holding the data rows that should be rendered by the table
 * @param rowIdProvider an appropriate id provider for the specific row type ``T`` (typically some sort of Id property)
 * @param selection a store that holds at most one row that gets selected
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form $prefix-$hash
 * @param build a lambda expression for setting up the component itself. Details in [DataTableComponent]
 */
fun <T, I> RenderContext.dataTable(
    styling: GridParams.() -> Unit = {},
    rows: RootStore<List<T>>,
    rowIdProvider: (T) -> I,
    selection: Store<T?>,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = DataTableComponent.prefix,
    build: DataTableComponent<T, I>.() -> Unit = {}
) {
    DataTableComponent(rows, rowIdProvider).apply {
        build()
        selection { single { store(selection) } }
    }.render(this, styling, baseClass, id, prefix)
}
