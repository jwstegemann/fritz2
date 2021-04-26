package dev.fritz2.components.datatable

import dev.fritz2.components.icon
import dev.fritz2.dom.html.Div


/**
 * This interface defines the actual sorting action.
 *
 * @see ColumnSortingPlan
 */
interface RowSorter<T> {
    fun sortedBy(
        rows: List<T>,
        columnSortingPlan: ColumnSortingPlan<T>
    ): List<T>
}

/**
 * This class implements the sorting with at most *one* column as sorting criterion, that is at most one column
 * within the [ColumnSortingPlan].
 *
 * If no column is present in the plan, no sorting will take place!
 */
class OneColumnSorter<T> : RowSorter<T> {
    override fun sortedBy(
        rows: List<T>,
        columnSortingPlan: ColumnSortingPlan<T>
    ): List<T> =
        if (columnSortingPlan.isNotEmpty()) {
            val (config, sorting) = columnSortingPlan.first()
            if (
                sorting != Sorting.DISABLED
                && sorting != Sorting.NONE
                && (config.lens != null || config.sortBy != null)
            ) {
                rows.sortedWith(
                    createComparator(config, sorting),
                )
            } else rows
        } else {
            rows
        }

    private fun createComparator(
        column: Column<T>,
        sorting: Sorting
    ): Comparator<T> {
        if (column.sortBy == null) {
            return when (sorting) {
                Sorting.ASC -> {
                    compareBy { column.lens!!.get(it) }
                }
                else -> {
                    compareByDescending { column.lens!!.get(it) }
                }
            }
        } else {
            return when (sorting) {
                Sorting.ASC -> {
                    column.sortBy
                }
                else -> {
                    column.sortBy.reversed()
                }
            }
        }
    }
}

/**
 * This interface bundles the methods to render the appropriate UI elements for the sorting actions within the
 * header column of a table.
 *
 * The data table takes care of calling the fitting method based upon the current state of a column's sorting
 * strategy.
 *
 * @see SortingPlan
 */
interface SortingRenderer {
    fun renderSortingActive(context: Div, sorting: Sorting)
    fun renderSortingLost(context: Div)
    fun renderSortingDisabled(context: Div)
}

/**
 * This implementation of a [SortingRenderer] creates an icon based UI for choosing the sorting of a data table.
 *  - There is an up-down-arrow icon, if the column is not sorted, but *sortable*
 *  - There is an down-arrow icon, if the data is sorted descending by the current column
 *  - There is an up-arrow icon, if the data is sorted ascending by the current column
 *  - There is no icon, if sorting is disabled for this column
 *
 *  @see SortingRenderer
 */
class SingleArrowSortingRenderer : SortingRenderer {
    override fun renderSortingActive(context: Div, sorting: Sorting) {
        context.apply {
            when (sorting) {
                Sorting.NONE -> renderSortingLost((this))
                else -> icon { fromTheme { if (sorting == Sorting.ASC) arrowUp else arrowDown } }
            }
        }
    }

    override fun renderSortingLost(context: Div) {
        context.apply {
            icon { fromTheme { sort } }
        }
    }

    override fun renderSortingDisabled(context: Div) {
        // just show nothing in this case
    }
}

/**
 * This interface defines the reducing process for the sorting plan, that is the creation of a new plan based upon
 * a user input, like clicking on symbol or button ([SingleArrowSortingRenderer]).
 *
 * @see SortingPlan
 */
interface SortingPlanReducer {
    fun reduce(sortingPlan: SortingPlan, activated: ColumnIdSorting): SortingPlan
}

/**
 * This [SortingPlanReducer] implementation defines the logic to generate a plan with at most *one* column for sorting.
 * This perfectly matches with the [OneColumnSorter] implementation for doing the sorting.
 *
 * @see SortingPlan
 */
class TogglingSortingPlanReducer : SortingPlanReducer {
    override fun reduce(sortingPlan: SortingPlan, activated: ColumnIdSorting): SortingPlan =
        if (activated.strategy == Sorting.DISABLED) {
            sortingPlan
        } else {
            listOf(
                ColumnIdSorting(
                    activated.id,
                    if (sortingPlan.isNotEmpty() && sortingPlan.first().id == activated.id) {
                        when (sortingPlan.first().strategy) {
                            Sorting.ASC -> Sorting.DESC
                            Sorting.DESC -> Sorting.NONE
                            else -> Sorting.ASC
                        }
                    } else {
                        Sorting.ASC
                    }
                )
            )
        }
}
