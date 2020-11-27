package dev.fritz2.components

import dev.fritz2.binding.*
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Td
import dev.fritz2.dom.html.Th
import dev.fritz2.dom.states
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.buildLens
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.GridParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.*


class NaiveSorter<T> {
    fun sortedBy(elements: List<T>, config: List<TableComponent.TableColumn<T>>): List<T> {
        val filterRules = config.filter {
            it.sorting != TableComponent.Companion.Sorting.DISABLED
                    && it.sorting != TableComponent.Companion.Sorting.NONE
                    // TODO: Not right!
                    // TODO: && no special sorting given
                    && (it.lens != null || it.sortBy != null)
        }
        return if (filterRules.isNotEmpty()) {
            val first = filterRules.first()
            elements.sortedWith(
                filterRules
                    .drop(1)
                    .fold(
                        createInitialComparator(first),
                        { previous, column ->
                            combineWithPreviousComparator(column, previous)
                        }
                    )
            )
        } else elements
    }

    private fun createInitialComparator(column: TableComponent.TableColumn<T>): Comparator<T> {
        if (column.sortBy == null) {
            return when (column.sorting) {
                TableComponent.Companion.Sorting.ASC -> {
                    compareBy { column.lens!!.get(it) }
                }
                else -> {
                    compareByDescending { column.lens!!.get(it) }
                }
            }
        } else {
            return when (column.sorting) {
                TableComponent.Companion.Sorting.ASC -> {
                    column.sortBy
                }
                else -> {
                    column.sortBy.reversed()
                }
            }
        }
    }

    private fun combineWithPreviousComparator(
        column: TableComponent.TableColumn<T>,
        predecessor: Comparator<T>
    ): Comparator<T> {
        if (column.sortBy == null) {
            return when (column.sorting) {
                TableComponent.Companion.Sorting.ASC -> {
                    predecessor.thenBy { column.lens!!.get(it) }
                }
                else -> {
                    predecessor.thenByDescending { column.lens!!.get(it) }
                }
            }
        } else {
            return when (column.sorting) {
                TableComponent.Companion.Sorting.ASC -> {
                    predecessor.then(column.sortBy)
                }
                else -> {
                    predecessor.thenDescending(column.sortBy)
                }
            }
        }
    }
}

/**
 * TODO open questions
 *  tfoot what will we do with this section of a table?
 *
 * TODO open todos
 *
 */
class TableComponent<T> {
    companion object {
        const val prefix = "table"
        val staticCss = staticStyle(
            prefix,
            """
                display:grid;
                //min-width: 100vw;
                width: auto;
                flex: 1;
                display: grid;
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
                  padding: 0.5rem 1.4rem .5rem 0.5rem;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                  &:last-child {
                    border-right: none;
                  }
                }
                
                td {
                    border-bottom: 1px solid inherit;
                }
                
                th {
                  position: sticky;
                  top: 0;
                  background: rgb(52, 58, 64);
                  text-align: left;
                  font-weight: normal;
                  font-size: 1.1rem;
                  color: rgb(226, 232, 240);
                  position: relative;
                }
                
                tr {
                    td { background: rgba(52, 58, 64, 0.1); }
                    &:nth-child(even) {
                        td {  background: rgba(52, 58, 64, 0.2); }
                    } 
                    &.selected {
                    td { background: rgba(255, 193, 7, 0.8) }
                    }
                }
            """
        )

        val sortDirectionSelected: Style<BasicParams> = {
            color { warning }
        }

        val sortDirectionIcon: Style<BasicParams> = {
            width { "0.9rem" }
            height { "0.9rem" }
            css("cursor:pointer;")
        }

        val sorterStyle: Style<BasicParams> = {
            display { inlineGrid }
            paddings { vertical { "0.35rem" } }
            height { "fitContent" }
            position {
                absolute {
                    right { "0.5rem" }
                    top { "0" }
                }
            }
        }


        enum class SelectionMode {
            NONE,
            SINGLE,
            SINGLE_CHECKBOX,
            MULTI
        }

        enum class Sorting {
            DISABLED,
            NONE,
            ASC,
            DESC
        }

        enum class CaptionPlacement {
            TOP,
            BOTTOM
        }

    }

    val configIdProvider = TableColumn<T>::_id

    @Lenses
    data class TableColumn<T>(
        val _id: String = uniqueId(),
        val lens: Lens<T, String>? = null,
        val headerName: String = "",
        val minWidth: Property? = null,
        val maxWidth: Property? = null,
        val hidden: Boolean = false,
        val position: Int = 0,
        val sorting: Sorting = Sorting.NONE,
        val sortBy: Comparator<T>? = null,
        val styling: Style<BasicParams> = {},
        // TODO: Remove default
        val content: (
            renderContext: Td,
            cellStore: Store<String>?,
            rowStore: SubStore<List<T>, List<T>, T>?
        ) -> Unit = { renderContext, store, _ ->
            renderContext.apply {
                store?.data?.asText()
            }
        },
        val stylingHead: Style<BasicParams> = {},
        // TODO: Remove default
        val contentHead: Th.(tableColumn: TableColumn<T>) -> Unit = { config ->
            +config.headerName
        }
    )

    class TableColumnsContext<T> {

        class TableColumnContext<T> {

            // TODO: Enhance setup by setting a default comparator lens based
            // see createInitialComparator, if block and combineWithPreviousComparator if block!
            fun build(): TableColumn<T> = TableColumn(
                _id,
                lens,
                header.title,
                width?.min,
                width?.max,
                hidden,
                position,
                sorting,
                sortBy,
                styling,
                content,
                header.styling,
                header.content
            )

            private var _id: String = uniqueId()
            fun id(value: () -> String) {
                _id = value()
            }

            private var lens: Lens<T, String>? = null
            fun lens(value: () -> Lens<T, String>) {
                lens = value()
            }

            class WidthContext {
                var min: Property? = null
                fun min(value: () -> Property) {
                    min = value()
                }

                var max: Property? = null
                fun max(value: () -> Property) {
                    max = value()
                }

                fun minmax(value: () -> Property) {
                    max = value()
                    min = value()
                }
            }

            private var width: WidthContext? = WidthContext()

            fun width(expression: WidthContext.() -> Unit) {
                width = WidthContext().apply(expression)
            }

            class HeaderContext<T> {

                var title: String = ""
                fun title(value: () -> String) {
                    title = value()
                }

                var styling: Style<BasicParams> = {}
                fun styling(value: Style<BasicParams>) {
                    styling = value
                }

                var content: Th.(tableColumn: TableColumn<T>) -> Unit = { config ->
                    +config.headerName
                }

                fun content(expression: Th.(tableColumn: TableColumn<T>) -> Unit) {
                    content = expression
                }
            }

            private var header: HeaderContext<T> = HeaderContext<T>()

            fun header(expression: HeaderContext<T>.() -> Unit) {
                header = HeaderContext<T>().apply(expression)
            }

            private var hidden: Boolean = false
            fun hidden(value: () -> Boolean) {
                hidden = value()
            }

            private var position: Int = 0
            fun position(value: () -> Int) {
                position = value()
            }

            private var sorting: Sorting = Sorting.NONE
            fun sorting(value: () -> Sorting) {
                sorting = value()
            }

            private var sortBy: Comparator<T>? = null
            fun sortBy(value: () -> Comparator<T>) {
                sortBy = value()
            }

            private var styling: Style<BasicParams> = {}
            fun styling(value: Style<BasicParams>) {
                styling = value
            }

            private var content: (
                renderContext: Td,
                cellStore: Store<String>?,
                rowStore: SubStore<List<T>, List<T>, T>?
            ) -> Unit = { renderContext, store, _ ->
                renderContext.apply {
                    store?.data?.asText()
                }
            }

            fun content(
                expression: (
                    renderContext: Td,
                    cellStore: Store<String>?,
                    rowStore: SubStore<List<T>, List<T>, T>?
                ) -> Unit
            ) {
                content = expression
            }

        }

        private var initialColumns: MutableList<TableColumn<T>> = mutableListOf()

        val columns: List<TableColumn<T>>
            get() = initialColumns

        fun column(expression: TableColumnContext<T>.() -> Unit) {
            initialColumns.add(TableColumnContext<T>().apply(expression).build())
        }

        fun column(title: String = "", expression: TableColumnContext<T>.() -> Unit) {
            initialColumns.add(TableColumnContext<T>().apply {
                header { title { title } }
                expression()
            }.build())
        }

    }

    private var columns: List<TableColumn<T>> = listOf()

    fun columns(expression: TableColumnsContext<T>.() -> Unit) {
        columns = TableColumnsContext<T>().apply(expression).columns
    }

    fun buildColumns() {
        configStore.update(columns)
    }

    var configStore: RootStore<List<TableColumn<T>>> = storeOf(emptyList())

    fun configStore(value: RootStore<List<TableColumn<T>>>) {
        configStore = value
    }

    var sorter: NaiveSorter<T>? = null

    var defaultMinWidth: Property = "150px"
    var defaultMaxWidth: Property = "1fr"

    var defaultTHeadStyle: Style<BasicParams> = {}
    fun defaultTHeadStyle(value: (() -> Style<BasicParams>)) {
        defaultTHeadStyle = value()
    }

    var defaultThStyle: Style<BasicParams> = {}
    fun defaultThStyle(value: (() -> Style<BasicParams>)) {
        defaultThStyle = value()
    }

    var defaultTBodyStyle: Style<BasicParams> = {}
    fun defaultTBodyStyle(value: (() -> Style<BasicParams>)) {
        defaultTBodyStyle = value()
    }

    var defaultTdStyle: Style<BasicParams> = {}
    fun defaultTdStyle(value: (() -> Style<BasicParams>)) {
        defaultTdStyle = value()
    }

    // TODO defaultTrStyle

    var selectionMode: Flow<SelectionMode> = flowOf(SelectionMode.NONE)
    fun selectionMode(value: SelectionMode) {
        selectionMode = flowOf(value)
    }

    fun selectionMode(value: Flow<SelectionMode>) {
        selectionMode = value
    }

    var tableStore: RootStore<List<T>> = storeOf(emptyList())
    fun tableStore(value: RootStore<List<T>>) {
        tableStore = value
    }

    var selectedRows: Flow<List<T>> = flowOf(emptyList())
    fun selectedRows(value: Flow<List<T>>) {
        selectedRows = value
    }

    var selectedRowEvent: SimpleHandler<T>? = null
    var selectedAllRowEvents: SimpleHandler<List<T>>? = null


    var captionPlacement: CaptionPlacement = CaptionPlacement.TOP
    fun captionPlacement(value: CaptionPlacement) {
        captionPlacement = value
    }

    var caption: (RenderContext.() -> Unit)? = null
    fun caption(value: (RenderContext.() -> Unit)) {
        caption = {
            (::caption.styled() {
                if (captionPlacement == CaptionPlacement.TOP) {
                    css("grid-area:header;")
                } else {
                    css("grid-area:footer;")
                }
            }){ value() }
        }
    }

    fun caption(value: String) {
        this.caption(flowOf(value))
    }

    fun caption(value: Flow<String>) {
        caption = {
            (::caption.styled() {
                if (captionPlacement == CaptionPlacement.TOP) {
                    css("grid-area:header;")
                } else {
                    css("grid-area:footer;")
                }
            }){ value.asText() }
        }
    }

    //fun renderSorter(renderContext: Th, )

}

fun <T, I> RenderContext.table(
    styling: GridParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = TableComponent.prefix,
    rowIdProvider: (T) -> I,
    build: TableComponent<T>.() -> Unit = {}
) {
    val component = TableComponent<T>().apply {
        build()
        buildColumns()
    }

    val tableBaseClass = if (baseClass != null) {
        baseClass + TableComponent.staticCss
    } else {
        TableComponent.staticCss
    }
    component.selectionMode.watch()
    component.configStore.watch()

    // TODO: Use Column DSL here!
    val sortDirectionLens =
        buildLens("sortDirection", TableComponent.TableColumn<T>::sorting) { p, v -> p.copy(sorting = v) }
    val additionalCol = component.selectionMode.map { selectionMode ->
        if (selectionMode == TableComponent.Companion.SelectionMode.MULTI) {
            listOf(
                TableComponent.TableColumn<T>(
                    minWidth = "50px",
                    maxWidth = "50px",
                    contentHead = {
                        checkbox({ display { inlineBlock } }, id = uniqueId()) {
                            text("")
                            borderColor { Theme().colors.secondary }
                            checkedBackgroundColor { Theme().colors.warning }
                            events {
                                component.selectedAllRowEvents?.let {
                                    changes.states().combine(component.tableStore.data) { selected, list ->
                                        if (selected) {
                                            list
                                        } else {
                                            emptyList()
                                        }
                                    } handledBy it
                                }
                            }
                        }
                    },
                    content = { ctx, _, rowStore ->
                        ctx.apply {
                            checkbox(
                                { display { inlineBlock } },
                                id = uniqueId()
                            ) {
                                text("")
                                checkedBackgroundColor { Theme().colors.warning }
                                if (rowStore != null) {
                                    checked {

                                        component.selectedRows.combine(rowStore.data) { selectedRows, thisRow ->
                                            selectedRows.contains(thisRow)
                                        }
                                    }
                                    events {
                                        component.selectedRowEvent?.let {
                                            clicks.events.combine(rowStore.data) { _, thisRow ->
                                                thisRow
                                            } handledBy it
                                        }

                                    }
                                }

                            }
                        }
                    }
                )
            )
        } else if (selectionMode == TableComponent.Companion.SelectionMode.SINGLE_CHECKBOX) {
            listOf(TableComponent.TableColumn<T>(
                minWidth = "50px",
                maxWidth = "50px",
                content = { ctx, _, rowStore ->
                    ctx.apply {
                        checkbox(
                            { display { inlineBlock } },
                            id = uniqueId()
                        ) {
                            text("")
                            checkedBackgroundColor { Theme().colors.warning }
                            if (rowStore != null) {
                                checked {

                                    component.selectedRows.combine(rowStore.data) { selectedRows, thisRow ->
                                        selectedRows.contains(thisRow)
                                    }
                                }
                                events {
                                    component.selectedRowEvent?.let {
                                        clicks.events.combine(rowStore.data) { _, thisRow ->
                                            thisRow
                                        } handledBy it
                                    }

                                }
                            }

                        }
                    }
                }
            ))
        } else {
            emptyList()
        }
    }


    val config = additionalCol.combine(component.configStore.data.map {
        it.filterNot { it.hidden }.sortedBy { it.position }
    }) { a, b -> a + b }

    val gridCols = config.map { configItems ->
        var minmax = ""
        var header = ""
        var footer = ""
        var main = ""


        configItems.forEach { item ->
            val min = item.minWidth ?: component.defaultMinWidth
            val max = item.maxWidth ?: component.defaultMaxWidth

            minmax += "minmax($min, $max)"
            main += "main "
            footer += "footer "
            header += "header "
        }

        """
            grid-template-columns: $minmax;                
            grid-template-rows: auto;
            grid-template-areas:
           "$header"
           "$main"
           "$footer";
           """
    }

    (::table.styled({
        styling()
    }, tableBaseClass, id, prefix) {}){
        attr("style", gridCols)
        if (component.captionPlacement == TableComponent.Companion.CaptionPlacement.TOP) {
            component.caption?.invoke(this)
        }
        /*
        component.configStore.data.render {
            console.info(it)
        }
         */
        (::thead.styled() {
            component.defaultTHeadStyle()
        }) {
            tr {
                config.renderEach(component.configIdProvider) { colConfig ->
                    val colConfigStore = component.configStore.sub(colConfig, component.configIdProvider)
                    val sortDirection = colConfigStore.sub(sortDirectionLens)
                    (::th.styled(colConfig.stylingHead) {
                        component.defaultThStyle()

                    })  {
                        colConfig.contentHead(this, colConfig)

                        //TODO bring it to renderSorter
                        if (component.sorter != null
                            && colConfig.sorting != TableComponent.Companion.Sorting.DISABLED
                        ) {
                            sortDirection.data.render { sorting ->
                                (::div.styled(TableComponent.sorterStyle) {}){
                                    icon({
                                        TableComponent.sortDirectionIcon()
                                        if (sorting == TableComponent.Companion.Sorting.ASC) {
                                            TableComponent.sortDirectionSelected()
                                        }
                                        size { normal }
                                    }) { fromTheme { caretUp } }
                                    icon({
                                        TableComponent.sortDirectionIcon()
                                        if (sorting == TableComponent.Companion.Sorting.DESC) {
                                            TableComponent.sortDirectionSelected()
                                        }
                                    }) { fromTheme { caretDown } }
                                    clicks.events.map {
                                        when (sorting) {
                                            TableComponent.Companion.Sorting.ASC -> TableComponent.Companion.Sorting.DESC
                                            TableComponent.Companion.Sorting.DESC -> TableComponent.Companion.Sorting.NONE
                                            else -> TableComponent.Companion.Sorting.ASC
                                        }
                                    } handledBy sortDirection.update
                                }
                            }
                        }
                    }
                }
            }
        }
        tbody {
            component.tableStore.data.combine(config) { tableData, config ->
                if (component.sorter == null) {
                    tableData
                } else {
                    component.sorter!!.sortedBy(tableData, config)
                }
            }.renderEach(rowIdProvider) { t ->
                val rowStore = component.tableStore.sub(t, rowIdProvider)
                val selected = component.selectedRows.combine(rowStore.data) { selectedRows, thisRow ->
                    selectedRows.contains(thisRow)
                }


                tr {
                    className(selected.combine(component.selectionMode) { selected, selectionMode ->
                        if (selected && selectionMode == TableComponent.Companion.SelectionMode.SINGLE) {
                            "selected"
                        } else {
                            ""
                        }
                    })
                    component.selectionMode.render { selectionMode ->
                        if (selectionMode == TableComponent.Companion.SelectionMode.SINGLE) {
                            component.selectedRowEvent?.let {
                                clicks.events.combine(rowStore.data) { _, thisRow ->
                                    thisRow
                                } handledBy it
                            }
                        }
                    }

                    config.renderEach { ctx ->
                        (::td.styled(ctx.styling) {}) {
                            if (ctx.lens != null) {
                                val b = rowStore.sub(ctx.lens)
                                ctx.content(this, b, rowStore)
                            } else {
                                ctx.content(this, null, rowStore)
                            }
                        }
                    }

                }
            }

            if (component.captionPlacement == TableComponent.Companion.CaptionPlacement.BOTTOM) {
                component.caption?.invoke(this)
            }

        }
    }

}
