package dev.fritz2.components.datatable

import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import dev.fritz2.components.ComponentProperty
import dev.fritz2.components.DynamicComponentProperty
import dev.fritz2.components.NullableDynamicComponentProperty
import dev.fritz2.components.datatable.ColumnsContext.ColumnContext
import dev.fritz2.components.datatable.OptionsContext.SortingContext
import dev.fritz2.dom.EventContext
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Td
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.Lens
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement

/**
 * This context class manages the configuration and defines the DSL for the overall declaration of the
 * data table's columns.
 *
 * The following aspects can be configured:
 * - the styling for all columns (overriding the theme's default)
 * - the individual columns by [ColumnContext]
 *
 * This is how the DSL looks like:
 * ```
 * columns({ // as first parameter you can pass a style that gets applied to *all* columns
 *     background { color { "purple" } }
 *     color { "white" }
 * }) { // opens the context for declaring the individual columns
 *     column {
 *      // and so on, Details in [ColumnContext]
 *     }
 * }
 * ```
 *
 * The styling gets some meta information injected, an [IndexedValue] of type ``IndexedValue<StatefulItem<T>>``,
 * so that is is possible to react to the row position (odd vs even for example) or to the content itself:
 * ```
 * columns({ (index, state) ->
 *     if(item % 2 == 1) { // odd row
 *         background { color { "purple" } }
 *         color { "white" }
 *     } else { // even row
 *         background { color { "pink" } }
 *         color { "black" }
 *     }
 *     if(state.selected) { // emphasize text if row is selected
 *         fontWeight { bold }
 *     }
 *     // access model type specific properties
 *     if(state.item.XYZ) {
 *         // some specific styling based upon values of the model (think of ranges for example)
 *     }
 * }) { // opens the context for declaring the individual columns
 *     column {
 *      // and so on, Details in [ColumnContext]
 *     }
 * }
 * ```
 *
 * Inside of the ``columns`` context, only arbitrary amount of ``column``s can be defined, but no other aspect!
 *
 * @see DataTableContext.columns
 */
class ColumnsContext<T> {

    /**
     * This context class manages the configuration and defines the DSL for exactly one column.
     *
     * The following aspects can be configured:
     * - the title
     * - an (optional) [Lens] to expose a type safe access to the column's data
     * - the styling
     * - the content rendering (default is the [String] representation of the data) for specific formatting or
     *   individual UIs like inputfields, selection elements, icons and stuff like that.
     * - the header by [HeaderContext]
     * - sizing aspects
     * - sorting aspects (initial sorting direction or disable sorting and special sorting for a type, think of
     *   some date types or even combined data)
     *
     * A column definition looks like this:
     * ```
     * column(title = "Id", {
     * }) {
     *      lens(idLens + Formats.intFormat)
     *      width { minmax("80px") }
     * }
     * ```
     *
     * Of course such column also gets meta information injected, so the styling for only one specific column based
     * upon the index, the selection, the sorting or some item based value is possible:
     * ```
     * column(title = "Id", styling = {  (index, state) ->
     *      if(state.sorting) {
     *          border {
     *              type { solid }
     *              width { fat }
     *              color { lime }
     *          }
     *      }
     * }) {
     *      //
     * }
     * ```
     *
     * For the customization of the content, there are also some parameters injected into the ``content`` context, that
     * provide the following features:
     * - the stateful index of the row: This can be used to adopt some styling of *inner* elements (or components!)
     *   as shown before
     * - the [String] based content representation as a store: This can be useful if only the HTML structure within
     *   a column should be other than just a ``td``, but the content representation itself is sufficient.
     * - and finally the type safe substore of the whole row: This can be used to combine different data properties
     *   of the model into one column.
     *
     * Example for using the [IndexedValue] of [StatefulItem] to mimic the odd-even styling of the rows within an
     * integrated sub component:
     * ```
     * content { rowIndex, _, rowStore ->
     *      inputField({
     *          if(rowIndex.index % 2 == 1) {
     *              background { color { gray100 } }
     *              color { gray700 }
     *          } else {
     *              background { color { gray300 } }
     *              color { gray900 }
     *          }
     *      }, value = rowStore.sub(L.Person.name)) {
     *          size { small }
     *      }
     *  }
     * ```
     *
     * Example for adopting the content of a cell based upon the pure String representation
     * ```
     * column(title = "language") {
     *      lens(L.Person.language) // we must inject a ``Lens`` in order to enable the automatic conversion!
     *      content { _, cellStore, _ ->
     *          cellStore.data.render { if(it.toLowerCase() == "kotlin") em { it } else it }
     *      }
     * }
     * ```
     *
     * Example for using the dynamic content to combine two properties of the model:
     * ```
     * column(title = "name") {
     *      content { _, _, rowStore ->
     *          rowStore.let { person ->
     *              val prename = person.sub(L.Person.prename)
     *              val surname = person.sub(L.Person.surname)
     *              prename.data.combine(name.data) { pre, sur -> "$pre $sur }.asText()
     *          }
     *      }
     * }
     * ```
     *
     * @see ColumnsContext.column
     */
    class ColumnContext<T> {

        fun build(): Column<T> = Column(
            id.value,
            lens.value,
            title.value,
            width?.min?.value,
            width?.max?.value,
            hidden.value,
            position.value,
            sorting.value(SortingContext),
            sortBy.value,
            styling.value,
            content.value,
            header.styling,
            header.content
        )

        val id = ComponentProperty(uniqueId())
        val lens = ComponentProperty<Lens<T, String>?>(null)

        class WidthContext {
            val min = ComponentProperty<Property?>(null)
            val max = ComponentProperty<Property?>(null)

            fun minmax(value: Property) {
                max(value)
                min(value)
            }
        }

        private var width: WidthContext? = WidthContext()

        fun width(expression: WidthContext.() -> Unit) {
            width = WidthContext().apply(expression)
        }

        /**
         * This context class manages the configuration and defines the DSL for the header of one column.
         *
         * A header definition looks like this:
         * ```
         * header ({ sorting ->
         *      if(sorting) {
         *          background { color { primary.highlight } }
         *      } else {
         *          background { color { primary } }
         *      }
         * }) { column -> // *stateless* container of a column with initial configuration data!
         *      +"${column.title}-Column" // just enhance the title
         * }
         * ```
         */
        data class Header<T>(
            val styling: BasicParams.(sorting: Sorting) -> Unit = {},
            val content: Div.(column: Column<T>) -> Unit = { column ->
                +column.title
            }
        )

        var header: Header<T> = Header()

        /**
         * @see HeaderContext
         */
        fun header(styling: BasicParams.(sorting: Sorting) -> Unit = {}, content: Div.(column: Column<T>) -> Unit) {
            header = Header(styling, content)
        }

        val title = ComponentProperty("")
        val hidden = ComponentProperty(false)
        val position = ComponentProperty(0)

        object SortingContext {
            val disabled = Sorting.DISABLED
            val none = Sorting.NONE
            val asc = Sorting.ASC
            val desc = Sorting.DESC
        }

        val sorting = ComponentProperty<SortingContext.() -> Sorting> { none }

        val sortBy = ComponentProperty<Comparator<T>?>(null)
        fun sortBy(expression: (T) -> Comparable<*>) {
            sortBy(compareBy(expression))
        }

        fun sortBy(vararg expressions: (T) -> Comparable<*>) {
            sortBy(compareBy(*expressions))
        }

        val styling = ComponentProperty<BasicParams.(value: IndexedValue<StatefulItem<T>>) -> Unit> { }

        val content =
            ComponentProperty<Td.(
                value: IndexedValue<StatefulItem<T>>,
                cellStore: Store<String>?,
                rowStore: SubStore<List<T>, List<T>, T>
            ) -> Unit>
            { _, store, _ -> store?.data?.asText() }
    }

    val columns: MutableMap<String, Column<T>> = mutableMapOf()

    /**
     * @see ColumnContext
     */
    fun column(
        styling: BasicParams.(value: IndexedValue<StatefulItem<T>>) -> Unit = {},
        expression: ColumnContext<T>.() -> Unit
    ) {
        ColumnContext<T>().apply(expression).also {
            it.styling(styling)
        }.build().also {
            columns[it.id] = it
        }
    }

    /**
     * Kurzer Satz....
     *
     * @see ColumnContext
     */
    fun column(
        styling: BasicParams.(value: IndexedValue<StatefulItem<T>>) -> Unit = {},
        title: String,
        expression: ColumnContext<T>.() -> Unit
    ) {
        ColumnContext<T>().apply {
            title(title)
            expression()
        }.also {
            it.styling(styling)
        }.build().also { columns[it.id] = it }
    }

    val styling = ComponentProperty<BoxParams.(IndexedValue<StatefulItem<T>>) -> Unit> { }
}

/**
 * This context class defines the event context of the data table.
 *
 * It enables the manual event handling for accessing:
 * - the selected row by a double click
 * - all selected rows (if multiple selection is allowed)
 * - just the only one selected row (if single selection mode is activated)
 *
 * The DSL can be used like this:
 * ```
 * events {
 *      dbClicks handledBy someStore.handler
 *      selectedRows handledBy someStore.handler
 *      selectedRow handledBy someStore.handler
 * }
 * ```
 */
class EventsContext<T, I>(element: RenderContext, rowSelectionStore: RowSelectionStore<T, I>) :
    EventContext<HTMLElement> by element {
    val selectedRows: Flow<List<T>> = rowSelectionStore.selectedData
    val selectedRow: Flow<T?> = rowSelectionStore.selectedData.map { it.firstOrNull() }
    val dbClicks: Flow<T> = rowSelectionStore.dbClickedRow
}

/**
 * This context class configures the selection capabilities of the data table.
 *
 * One can choose the type of the selection ([selectionMode]):
 * - [single] for only allowing at most one column to be selected
 * - [multi] for selecting an arbitrary amount of rows
 * - omitting the configuration for disabling selection at all
 *
 * Besides the pure selection type, also the method of selecting ([strategy]) can be configured:
 * - selection by clicking onto a row
 * - selection by checking a checkBox that gets rendered at the front of the table in an extra column
 * - some custom strategy (by implementing the [SelectionStrategy] interface)
 *
 * The selection type chooses the strategy automatically if omitted:
 * - single -> by click
 * - multi -> by checkBox
 *
 * Be aware that the selection configuration is predetermined by the passing of an external store into the factory
 * functions! The selection gets enabled and chosen depending of the type of the store:
 * - ``T`` -> single
 * - ``List<T>`` -> multi
 * - no store -> disabled ([SelectionMode.None])
 */
class SelectionContext<T, I> {

    object StrategyContext {
        enum class StrategySpecifier {
            Checkbox,
            Click
        }

        val checkbox = StrategySpecifier.Checkbox
        val click = StrategySpecifier.Click
    }

    class Single<T> {
        val store = ComponentProperty<Store<T?>?>(null)
        val row = NullableDynamicComponentProperty<T>(emptyFlow())
    }

    internal var single: Single<T>? = null
    fun single(value: Single<T>.() -> Unit) {
        single = Single<T>().apply(value)
    }

    class Multi<T> {
        val store = ComponentProperty<Store<List<T>>?>(null)
        val rows = DynamicComponentProperty<List<T>>(emptyFlow())
    }

    internal var multi: Multi<T>? = null
    fun multi(value: Multi<T>.() -> Unit) {
        multi = Multi<T>().apply(value)
    }

    val strategy = ComponentProperty<SelectionStrategy<T, I>?>(null)
    fun strategy(expression: StrategyContext.() -> StrategyContext.StrategySpecifier) {
        when (expression(StrategyContext)) {
            StrategyContext.StrategySpecifier.Checkbox -> strategy(SelectionByCheckBox())
            StrategyContext.StrategySpecifier.Click -> strategy(SelectionByClick())
        }
    }

    val selectionMode by lazy {
        when {
            single != null -> {
                SelectionMode.Single
            }
            multi != null -> {
                SelectionMode.Multi
            }
            else -> {
                SelectionMode.None
            }
        }
    }
}

/**
 * This context class integrates the configuration and the DSL for some general and optional properties.
 *
 * The following aspects can be configured:
 * - different customizations related to sorting functionalities ([SortingContext])
 * - the hovering effect
 * - width and heights properties, including the maximum values for the whole table
 * - width and max width for a cell
 *
 */
class OptionsContext<T> {

    class SortingContext<T> {
        val reducer = ComponentProperty<SortingPlanReducer>(TogglingSortingPlanReducer())
        val sorter = ComponentProperty<RowSorter<T>>(OneColumnSorter())
        val renderer = ComponentProperty<SortingRenderer>(SingleArrowSortingRenderer())
    }

    /**
     * @see [SortingContext]
     */
    val sorting = ComponentProperty<SortingContext<T>>(SortingContext())
    fun sorting(value: SortingContext<T>.() -> Unit) {
        sorting.value.apply { value() }
    }

    class HoveringContext<T> {
        val active = ComponentProperty(true)
        val style = ComponentProperty<BasicParams.(IndexedValue<StatefulItem<T>>) -> Unit> {}
    }

    val hovering = ComponentProperty<HoveringContext<T>>(HoveringContext())
    fun hovering(value: HoveringContext<T>.() -> Unit) {
        hovering.value.apply(value)
    }

    val width = ComponentProperty<Property?>("100%")
    val maxWidth = ComponentProperty<Property?>(null)
    val height = ComponentProperty<Property?>(null)
    val maxHeight = ComponentProperty<Property?>("97vh")
    val cellMinWidth = ComponentProperty<Property>("130px")
    val cellMaxWidth = ComponentProperty<Property>("1fr")
}

/**
 * This context class integrates the configuration and the DSL for the common header aspects.
 *
 * The following aspects can be configured:
 * - the general styling of the header
 * - enable or disable the pinning of the header during the scrolling
 * - adjust the height for the fixed header
 *
 */
class HeaderContext {
    val fixedHeader = ComponentProperty(true)
    val fixedHeaderHeight = ComponentProperty<Property>("37px")
    val styling = ComponentProperty<Style<BasicParams>> { }
}

/**
 * This context class integrates the configuration and the DSL for the whole data table using sub contexts with
 * their own DSLs.
 *
 * The main purpose is to integrate this directly into the [DataTableComponent] via delegation ([DefaultContext]).
 *
 * @see ColumnsContext
 * @see SelectionContext
 * @see EventContext
 * @see HeaderContext
 * @see OptionsContext
 */
interface DataTableContext<T, I> {
    // sub contexts

    /**
     * @see ColumnsContext
     */
    val columns: ComponentProperty<ColumnsContext<T>>

    /**
     * @see EventsContext
     */
    val events: ComponentProperty<EventsContext<T, I>.() -> Unit>

    /**
     * @see SelectionContext
     */
    val selection: ComponentProperty<SelectionContext<T, I>>

    /**
     * @see OptionsContext
     */
    val options: ComponentProperty<OptionsContext<T>>

    /**
     * @see HeaderContext
     */
    val header: ComponentProperty<HeaderContext>


    // special factories for sub contexts (offer additional parameters or special setup)

    /**
     * @see ColumnsContext
     */
    fun columns(
        styling: BoxParams.(IndexedValue<StatefulItem<T>>) -> Unit = {},
        expression: ColumnsContext<T>.() -> Unit
    )

    /**
     * @see SelectionContext
     */
    fun selection(value: SelectionContext<T, I>.() -> Unit)

    /**
     * @see OptionsContext
     */
    fun options(value: OptionsContext<T>.() -> Unit)

    /**
     * @see HeaderContext
     */
    fun header(styling: Style<BasicParams> = {}, expression: HeaderContext.() -> Unit)

    // other stuff
    val selectionStore: RowSelectionStore<T, I>
    fun prependAdditionalColumns(expression: ColumnsContext<T>.() -> Unit)
}

class DefaultContext<T, I>(rowIdProvider: (T) -> I) : DataTableContext<T, I> {

    override val columns = ComponentProperty(ColumnsContext<T>())

    override fun columns(
        styling: BoxParams.(IndexedValue<StatefulItem<T>>) -> Unit,
        expression: ColumnsContext<T>.() -> Unit
    ) {
        columns.value.apply(expression).also {
            it.styling(styling)
        }
    }

    override fun prependAdditionalColumns(expression: ColumnsContext<T>.() -> Unit) {
        val minPos = columns.value.columns.values.minOf { it.position }
        columns.value.columns.putAll(ColumnsContext<T>().apply(expression).columns
            .mapValues { it.value.copy(position = minPos - 1) }.entries
            .map { (a, b) -> a to b }
            .toMap()
        )
    }

    override val events = ComponentProperty<EventsContext<T, I>.() -> Unit> {}

    override val selectionStore: RowSelectionStore<T, I> = RowSelectionStore(rowIdProvider)

    override val selection = ComponentProperty(SelectionContext<T, I>())
    override fun selection(value: SelectionContext<T, I>.() -> Unit) {
        selection.value.value()
        if (selection.value.strategy.value == null) {
            when (selection.value.selectionMode) {
                SelectionMode.Single -> selection.value.strategy(SelectionByClick())
                SelectionMode.Multi -> selection.value.strategy(SelectionByCheckBox())
                else -> selection.value.strategy(NoSelection())
            }
        }
    }

    override val options = ComponentProperty(OptionsContext<T>())
    override fun options(value: OptionsContext<T>.() -> Unit) {
        options.value.apply { value() }
    }

    override val header = ComponentProperty(HeaderContext())
    override fun header(styling: Style<BasicParams>, expression: HeaderContext.() -> Unit) {
        header.value.apply {
            expression()
            styling(styling)
        }
    }
}