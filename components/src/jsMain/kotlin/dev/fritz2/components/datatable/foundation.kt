package dev.fritz2.components.datatable

import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Td
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.Lenses
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.Property

/**
 * Representation of the different sorting status.
 */
enum class Sorting {
    DISABLED,
    NONE,
    ASC,
    DESC;

    companion object {
        fun sorted(sorting: Sorting): Boolean = sorting == ASC || sorting == DESC
    }
}


/**
 * Representation of the selection possibilities.
 */
enum class SelectionMode {
    None,
    Single,
    Multi
}

/**
 * This class is meant for combining the data of one row with the current state specific properties like the
 * sorting strategy or whether the row is currently selected.
 */
data class StatefulItem<T>(val item: T, val selected: Boolean, val sorting: Sorting)

/**
 * Main class to define the representation of the data class ``T`` of one table column.
 * This class mainly is the result of the DSL configuration of the datatable.
 *
 * Besides the main properties like the header title string or the [Lens] to grab the current [String] representation
 * of the row data during the rendering process, one can also specify the sorting algorithm to fit the specific
 * type (like a Date for example) or the appearance of the cell styling and content or the header styling and content.
 */
@Lenses
data class Column<T>(
    val id: String, // must be unique!
    val lens: Lens<T, String>? = null,
    val title: String = "",
    val minWidth: Property? = null,
    val maxWidth: Property? = null,
    val hidden: Boolean = false,
    val position: Int = 0,
    val sorting: Sorting = Sorting.NONE,
    val sortBy: Comparator<T>? = null,
    val styling: BasicParams.(value: IndexedValue<StatefulItem<T>>) -> Unit = { _ -> },
    val content: Td.(
        value: IndexedValue<StatefulItem<T>>,
        cellStore: Store<String>?,
        rowStore: SubStore<List<T>, T>
    ) -> Unit,
    val headerStyling: BasicParams.(sorting: Sorting) -> Unit = {},
    val headerContent: Div.(column: Column<T>) -> Unit
)

/**
 * This helper function determines for the inner rendering loop of the cells (`td`), which cell really needs to
 * be re-rendered! Only changes to the column itself, that is its [String] representation or its state, requires such
 * a new rendering process.
 */
fun <T> columnIdProvider(param: Pair<Column<T>, IndexedValue<StatefulItem<T>>>) =
    param.first.lens?.get(param.second.value.item).hashCode() * 31 +
            param.second.index * 31 +
            param.second.value.selected.hashCode() * 31 +
            param.second.value.sorting.hashCode() * 31


/**
 * Wrapping class to group the id of a [Column] with the sorting strategy.
 * This is the base for the type ``T`` independent [SortingPlan] which itself is necessary for the *internal*
 * [State].
 */
data class ColumnIdSorting(
    val id: String?,
    val strategy: Sorting = Sorting.NONE
) {
    companion object {
        fun noSorting() = ColumnIdSorting(null)

        fun <T> of(column: Column<T>) = ColumnIdSorting(column.id, column.sorting)
    }
}

/**
 * This alias expresses the sorting plan, which is the *ordered* list of column ids paired with their strategies.
 * This is the base concept for the sorting held within a [State] object and used for the [SortingRenderer]
 * in order to create a new plan based upon user interaction (change the direction, change the column to be sorted)
 *
 * There are three different interfaces on that the separate sorting functionalities are implemented:
 *  1. [SortingPlanReducer]: create a plan of columns to be sorted in a specific direction based upon the former
 *                           plan and the triggering user action
 *  2. [RowSorter]: do the actual sorting work based upon the above plan (slightly transformed to typed column
 *                  instead of plain ids)
 *  3. [SortingRenderer]: render the appropriate UI for the sorting actions in the header columns
 *
 * Here is the big picture visualized:
 * ```
 *                          +-----------------------------------------------------------------+
 *                          |                                   (emits new state)             |
 *                          |                                                                 |
 *                          v                                                                 |
 *    +------------------------------------------+                                            |
 *    |                  State                   |                                            |
 *    +------------------------------------------+                                            |
 *    | val sortingPlan: SortingPlan             |                                            |
 *    +------------------------------------------+                                            |
 *    | fun columnSortingPlan: ColumnSortingPlan |                                            |
 *    +------------------------------------------+                                            |
 *                          ^                                                                 |
 *                          |                          +----------------------------------+   |
 * User action as input:    |(owns)              +---->|      SortingPlanReducer (1)      |---+
 * (Column ID+Strategy)     |                    |     +----------------------------------+
 *     |                    |                    |     | Creates a new SortingPlan based  |
 *     |     +-----------------------------+     |     | upon the former plan, the newly  |
 *     |     |         StateStore          |     |     | column ID and its sorting        |
 *     |     +-----------------------------+     |     | strategy (asc, desc, none)       |
 *     +---->| val sortingChanged: Handler |-----+     +----------------------------------+
 *           +-----------------------------+
 *     +---->| fun renderingRowsData       |-----+
 *     |     +-----------------------------+     |     +----------------------------------+
 *     |                                         +---->|           RowSorter (2)          |
 *     |                                               +----------------------------------+
 *     |                                               | Takes the current rows and sort  |
 * TableComponent                                      | them according to the column     |
 * .renderRows()                                       | based sorting plan.              |
 *                                                     | So the raw ``sortingPlan``       |
 *                                                     | field of the state object must   |
 *                                                     | be  enriched before with the     |
 *                                                     | actual ``Column`` objects by     |
 *                                                     | the ``columnSoftingPlan``        |
 *                                                     | function.                        |
 *                                                     +----------------------------------+
 *
 *
 *                                                     +----------------------------------+
 * TableComponent ------------------------------------>|        SortingRenderer (3)       |
 * .renderHeader()                                     +----------------------------------+
 *                                                     | Renders the UI elements          |
 *                                                     | (icons eg.) for configuring      |
 *                                                     | and changing the sorting state.  |
 *                                                     +----------------------------------+
 *
 * [Edit](https://textik.com/#f3fc13858b89df9b)
 * ```
 * @see ColumnSortingPlan
 * @see SortingPlanReducer
 * @see RowSorter
 * @see SortingRenderer
 *
 */
typealias SortingPlan = List<ColumnIdSorting>

/**
 * This alias expresses the grouping of a [Column] paired with its sorting strategy.
 * Together with the [SortingPlan] this represents the *foundation* of the sorting mechanisms:
 * Based upon the [SortingPlan] this plan is created from the given [Column]s of a datatable and is then used to
 * do the actual sorting within a [RowSorter] implementation.
 *
 * There are three different interfaces on that the separate sorting functionalities are implemented:
 *  1. [SortingPlanReducer]: create a plan of columns to be sorted in a specific direction based upon the former
 *                           plan and the triggering user action
 *  2. [RowSorter]: do the actual sorting work based upon the above plan (slightly transformed to typed column
 *                  instead of plain ids)
 *  3. [SortingRenderer]: render the appropriate UI for the sorting actions in the header columns
 *
 * Here is the big picture visualized:
 * ```
 *                          +-----------------------------------------------------------------+
 *                          |                                   (emits new state)             |
 *                          |                                                                 |
 *                          v                                                                 |
 *    +------------------------------------------+                                            |
 *    |                  State                   |                                            |
 *    +------------------------------------------+                                            |
 *    | val sortingPlan: SortingPlan             |                                            |
 *    +------------------------------------------+                                            |
 *    | fun columnSortingPlan: ColumnSortingPlan |                                            |
 *    +------------------------------------------+                                            |
 *                          ^                                                                 |
 *                          |                          +----------------------------------+   |
 * User action as input:    |(owns)              +---->|      SortingPlanReducer (1)      |---+
 * (Column ID+Strategy)     |                    |     +----------------------------------+
 *     |                    |                    |     | Creates a new SortingPlan based  |
 *     |     +-----------------------------+     |     | upon the former plan, the newly  |
 *     |     |         StateStore          |     |     | column ID and its sorting        |
 *     |     +-----------------------------+     |     | strategy (asc, desc, none)       |
 *     +---->| val sortingChanged: Handler |-----+     +----------------------------------+
 *           +-----------------------------+
 *     +---->| fun renderingRowsData       |-----+
 *     |     +-----------------------------+     |     +----------------------------------+
 *     |                                         +---->|           RowSorter (2)          |
 *     |                                               +----------------------------------+
 *     |                                               | Takes the current rows and sort  |
 * TableComponent                                      | them according to the column     |
 * .renderRows()                                       | based sorting plan.              |
 *                                                     | So the raw ``sortingPlan``       |
 *                                                     | field of the state object must   |
 *                                                     | be  enriched before with the     |
 *                                                     | actual ``Column`` objects by     |
 *                                                     | the ``columnSoftingPlan``        |
 *                                                     | function.                        |
 *                                                     +----------------------------------+
 *
 *
 *                                                     +----------------------------------+
 * TableComponent ------------------------------------>|        SortingRenderer (3)       |
 * .renderHeader()                                     +----------------------------------+
 *                                                     | Renders the UI elements          |
 *                                                     | (icons eg.) for configuring      |
 *                                                     | and changing the sorting state.  |
 *                                                     +----------------------------------+
 *
 * [Edit](https://textik.com/#f3fc13858b89df9b)
 * ```
 * @see SortingPlan
 * @see SortingPlanReducer
 * @see RowSorter
 * @see SortingRenderer
 */
typealias ColumnSortingPlan<T> = List<Pair<Column<T>, Sorting>>
