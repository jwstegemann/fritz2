package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Alias for the specific [AutoFlowValues] properties
 */
typealias AutoFlowProperty = Property

/**
 * Predefined values for the [grid-auto-flow](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-flow)
 * property. Should be used as expression result for the [GridLayout.autoFlow] functions.
 */
object AutoFlowValues : PropertyValues {
    override val key = "grid-auto-flow: "

    const val column: AutoFlowProperty = "column"
    const val row: AutoFlowProperty = "row"
    const val dense: AutoFlowProperty = "dense"
    const val columnDense: AutoFlowProperty = "column dense"
    const val rowDense: AutoFlowProperty = "row dense"
    const val inherit: AutoFlowProperty = "inherit"
    const val initial: AutoFlowProperty = "initial"
    const val unset: AutoFlowProperty = "unset"
}

/**
 * This _context_ class enables to set the template properties for _rows_, _columns_ and their corresponding _automatic_
 * variants for dynamically added rows or columns (have a look at
 * [Auto placement](https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout/Auto-placement_in_CSS_Grid_Layout)
 * documentation) in a flexible yet comfortable way.
 *
 * This class exposes two variants of the [repeat] function in order so specify the amount of rows or columns of a grid
 * layout. One is specialized for integer based amounts of repetition of row or column appearance rules, the other
 * enables the usage of custom or predefined rules also included as properties of this context, like [autoFill] for
 * example.
 *
 * It offers also the [minmax] function in order to use this as parameter to [repeat].
 *
 * example calls:
 * ```
 * // assuming both calls within some [GridTemplateContext]
 *
 * columns { /* it == GridTemplateContext.() -> Unit */
 *     repeat(9) { "9fr" }
 *     repeat(autoFill) { "9fr" }
 * }
 *
 * autoRows { /* it == GridTemplateContext.() -> Unit */
 *     minmax("100px", auto)
 * }
 * ```
 */
@ExperimentalCoroutinesApi
class GridTemplateContext(
    styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * Predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/repeat) related CSS value
     */
    val autoFit: Property = "auto-fit"

    /**
     * Predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/repeat) related CSS value
     */
    val autoFill: Property = "auto-fill"

    /**
     * Predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/repeat) related CSS value and
     * predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/minmax) related CSS value
     */
    val minContent: Property = "min-content"

    /**
     * Predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/repeat) related CSS value and
     * predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/minmax) related CSS value
     */
    val maxContent: Property = "max-content"

    /**
     * Predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/repeat) related CSS value and
     * predefined value for the [minmax()](https://developer.mozilla.org/en/docs/Web/CSS/minmax) related CSS value
     */
    val auto: Property = "auto"


    /**
     * Function to create the CSS [repeat](https://developer.mozilla.org/en/docs/Web/CSS/repeat) function.
     *
     * This variant is specialized for integer based repetition.
     *
     * If you want to pass a predefined value like [minContent] or alike, use the other variant.
     *
     * @param count amount of cells / cell patterns to repeat
     * @param value extension function parameter in order to bring the predefined values from [GridTemplateContext]
     *              into the scope of the functional expression
     */
    fun repeat(count: Int, value: GridTemplateContext.() -> Property) =
        "repeat($count, ${value()})"

    /**
     * Function to create the CSS [repeat](https://developer.mozilla.org/en/docs/Web/CSS/repeat) function.
     *
     * This variant is specialized for property based repetition, which works best if you want to apply one of the
     * predefined values like [minContent] or similar.
     *
     * If you want to pass an integer value use the other variant.
     *
     * @param count property of cells / cell patterns to repeat; best used with the predefined properties of this class
     *              like [autoFill] or similar.
     * @param value extension function parameter in order to bring the predefined values from [GridTemplateContext]
     *              into the scope of the functional expression
     */
    fun repeat(count: Property, value: GridTemplateContext.() -> Property) =
        "repeat($count, ${value()})"

    /**
     * Function to create the CSS [minmax](https://developer.mozilla.org/en/docs/Web/CSS/minmax) function.
     *
     * @param min value or expression for the lower limit
     * @param max value or expression for the upper limit
     */
    fun minmax(min: Property, max: Property) = "minmax($min, $max)"
}


/**
 * Alias for the _named_ areas of a grid layout.
 */
typealias AreaName = String

/**
 * Extension property that generates the value for the _start_ grid line of an area
 */
val AreaName.start: String
    get() = "$this-start"

/**
 * Extension property that generates the value for the _end_ grid line of an area
 */
val AreaName.end: String
    get() = "$this-end"

/**
 * Extension property that generates the value for the _middle_ between the start and end grid lines of an area
 */
val AreaName.middle: String
    get() = "$this-middle"


/**
 * This _context_ class is responsible for defining the
 * [grid area layout](https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout/Grid_Template_Areas)
 * as comfortable and safe as possible.
 *
 * It contains only one function: [row]. This function has the purpose to create exactly _one_ row of the grid area.
 * It takes an arbitrary amount of [Property] parameters, which represent one _cell_ by its name.
 *
 * One can pass in string literals directly of course, but for good reasons consider an approach,
 * where you manage the string based cell names within a type of _container_ like a [Map], [List] or an ``object``.
 *
 * example call
 * ```
 * areas { /* it == GridAreaContext.() */
 *     row("HEADER", "HEADER", "HEADER")
 *     row("MENU", "CONTENT", "CONTENT")
 *     row("FOOTER", "FOOTER", "FOOTER")
 * }
 * ```
 *
 * @see [GridLayout.areas]
 */
@ExperimentalCoroutinesApi
class GridAreaContext(styleParams: StyleParams, val target: StringBuilder) : StyleParams by styleParams {

    /**
     * This functions creates a single definitions of a row with named cells for the grid template area.
     *
     * @param values arbitrary area names
     */
    fun row(vararg values: AreaName) = target.append("\"${values.toList().joinToString(" ")}\"\n")
}


internal const val gridTemplateColumnsKey = "grid-template-columns: "
internal const val gridTemplateRowsKey = "grid-template-rows: "
internal const val gridTemplateAutoRowsKey = "grid-auto-rows: "
internal const val gridTemplateAutoColumnsKey = "grid-auto-columns: "
internal const val gridTemplateAreasKey = "grid-template-areas: "
internal const val rowGapKey = "row-gap: "
internal const val columnGapKey = "column-gap: "

/**
 * This _context_ interface offers functions to **style** the container component for
 * [grid](https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout/Basic_Concepts_of_Grid_Layout)-layouts.
 *
 * It does **not** create a grid layout though!
 *
 * This is done by a special [dev.fritz2.styling.components.grid] fabric function that creates a component
 * with the [display](https://developer.mozilla.org/en/docs/Web/CSS/display) property already set to ``grid``.
 *
 * So it is recommended to use the provided functions within the styles parameter of [dev.fritz2.styling.components.grid].
 *
 * This interface offers all the inherited functions of [Alignment] that corresponds to the
 * [CSS alignment module](https://www.w3.org/TR/css-align-3/).
 *
 * In order to get a grid layout to work, you also have to style the _child_ elements. This is done by the [Layout.grid]
 * function, that brings the special [GridContext] for this task into the scope.
 *
 * The following example grid layout...
 * ```
 * +----------+----------+----------+
 * | Header                         |
 * +----------+----------+----------+
 * | Menu     | Content             |
 * +----------+----------+----------+
 * | Footer                         |
 * +----------+----------+----------+
 * ```
 *
 * ... can be defined with these functions:
 * ```
 * grid({ /* it == GridStyleParams (sum type of different StyleParams based interfaces including this one) */
 *     columns { repeat(3) { "1fr" } }
 *     autoRows { minmax(100) { auto } }
 *     areas {
 *         row("HEADER", "HEADER", "HEADER")
 *         row("MENU", "CONTENT", "CONTENT")
 *         row("FOOTER", "FOOTER", "FOOTER")
 *     }
 *     gap { normal }
 * }) {
 *     // child content of the grid container, different _context_, but relation to the _area names_!
 *     box({
 *         grid { area { "HEADER" } }
 *     }) {
 *         text { +"Header" }
 *     }
 *     box({
 *         grid { area { "MENU" } }
 *     }) {
 *         text { +"Menu" }
 *     }
 *     box({
 *         grid { area { "CONTENT" } }
 *     }) {
 *         text { +"Content" }
 *     }
 *     box({
 *         grid { area { "FOOTER" } }
 *     }) {
 *         text { +"Footer" }
 *     }
 * }
 * ```
 *
 * Remember that [GridLayout] ist only for defining the _template_ of the grid; to combine the child elements with
 * a template you have to use the [Layout.grid] functions!
 *
 * @see Alignment
 */
@ExperimentalCoroutinesApi
interface GridLayout : StyleParams, Alignment {

    /**
     * This function opens a context ([GridTemplateContext]) to specify the column layout of a grid layout via the
     * [grid-template-columns](https://developer.mozilla.org/en/docs/Web/CSS/grid-template-columns) CSS property
     * for all media devices.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param value extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun columns(value: GridTemplateContext.() -> Property) =
        property(gridTemplateColumnsKey, GridTemplateContext(this, smProperties).value(), smProperties)

    /**
     * This function opens a context ([GridTemplateContext]) to specify the column layout of a grid layout via the
     * [grid-template-columns](https://developer.mozilla.org/en/docs/Web/CSS/grid-template-columns) CSS property
     * for each media device independently.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param sm extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for small media devices.
     * @param md extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for medium sized media devices.
     * @param lg extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for large media devices.
     * @param xl extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for extra large media devices.
     */
    fun columns(
        sm: (GridTemplateContext.() -> Property)? = null,
        md: (GridTemplateContext.() -> Property)? = null,
        lg: (GridTemplateContext.() -> Property)? = null,
        xl: (GridTemplateContext.() -> Property)? = null
    ) {
        if (sm != null) property(gridTemplateColumnsKey, GridTemplateContext(this, smProperties).sm(), smProperties)
        if (md != null) property(gridTemplateColumnsKey, GridTemplateContext(this, mdProperties).md(), mdProperties)
        if (lg != null) property(gridTemplateColumnsKey, GridTemplateContext(this, lgProperties).lg(), lgProperties)
        if (xl != null) property(gridTemplateColumnsKey, GridTemplateContext(this, xlProperties).xl(), xlProperties)
    }

    /**
     * This function opens a context ([GridTemplateContext]) to specify the row layout of a grid layout via the
     * [grid-template-rows](https://developer.mozilla.org/en/docs/Web/CSS/grid-template-rows) CSS property
     * for all media devices.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param value extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun rows(value: GridTemplateContext.() -> Property) =
        property(gridTemplateRowsKey, GridTemplateContext(this, smProperties).value(), smProperties)

    /**
     * This function opens a context ([GridTemplateContext]) to specify the row layout of a grid layout via the
     * [grid-template-rows](https://developer.mozilla.org/en/docs/Web/CSS/grid-template-rows) CSS property
     * for each media device independently.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param sm extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for small media devices.
     * @param md extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for medium sized media devices.
     * @param lg extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for large media devices.
     * @param xl extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for extra large media devices.
     */
    fun rows(
        sm: (GridTemplateContext.() -> Property)? = null,
        md: (GridTemplateContext.() -> Property)? = null,
        lg: (GridTemplateContext.() -> Property)? = null,
        xl: (GridTemplateContext.() -> Property)? = null
    ) {
        if (sm != null) property(gridTemplateRowsKey, GridTemplateContext(this, smProperties).sm(), smProperties)
        if (md != null) property(gridTemplateRowsKey, GridTemplateContext(this, mdProperties).md(), mdProperties)
        if (lg != null) property(gridTemplateRowsKey, GridTemplateContext(this, lgProperties).lg(), lgProperties)
        if (xl != null) property(gridTemplateRowsKey, GridTemplateContext(this, xlProperties).xl(), xlProperties)
    }

    /**
     * This function opens a context ([GridTemplateContext]) to specify the auto rows layout of a grid layout via the
     * [grid-auto-rows](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-rows) CSS property
     * for all media devices.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param value extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun autoRows(value: GridTemplateContext.() -> Property) =
        property(gridTemplateAutoRowsKey, GridTemplateContext(this, smProperties).value(), smProperties)

    /**
     * This function opens a context ([GridTemplateContext]) to specify the auto rows layout of a grid layout via the
     * [grid-auto-rows](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-rows) CSS property
     * for each media device independently.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param sm extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for small media devices.
     * @param md extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for medium sized media devices.
     * @param lg extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for large media devices.
     * @param xl extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for extra large media devices.
     */
    fun autoRows(
        sm: (GridTemplateContext.() -> Property)? = null,
        md: (GridTemplateContext.() -> Property)? = null,
        lg: (GridTemplateContext.() -> Property)? = null,
        xl: (GridTemplateContext.() -> Property)? = null
    ) {
        if (sm != null) property(gridTemplateAutoRowsKey, GridTemplateContext(this, smProperties).sm(), smProperties)
        if (md != null) property(gridTemplateAutoRowsKey, GridTemplateContext(this, mdProperties).md(), mdProperties)
        if (lg != null) property(gridTemplateAutoRowsKey, GridTemplateContext(this, lgProperties).lg(), lgProperties)
        if (xl != null) property(gridTemplateAutoRowsKey, GridTemplateContext(this, xlProperties).xl(), xlProperties)
    }

    /**
     * This function opens a context ([GridTemplateContext]) to specify the auto columns layout of a grid layout via the
     * [grid-auto-columns](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-columns) CSS property
     * for all media devices.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param value extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun autoColumns(value: GridTemplateContext.() -> Property) =
        property(gridTemplateAutoColumnsKey, GridTemplateContext(this, smProperties).value(), smProperties)

    /**
     * This function opens a context ([GridTemplateContext]) to specify the auto columns layout of a grid layout via the
     * [grid-auto-columns](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-columns) CSS property
     * for each media device independently.
     *
     * The actual definition is then done within the [GridTemplateContext] using the specific topic functions of it.
     *
     * @see [GridTemplateContext]
     *
     * @param sm extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for small media devices.
     * @param md extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for medium sized media devices.
     * @param lg extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for large media devices.
     * @param xl extension function parameter to open the [GridTemplateContext] as scope of the functional expression
     *           in order to use its specific styling functions for extra large media devices.
     */
    fun autoColumns(
        sm: (GridTemplateContext.() -> Property)? = null,
        md: (GridTemplateContext.() -> Property)? = null,
        lg: (GridTemplateContext.() -> Property)? = null,
        xl: (GridTemplateContext.() -> Property)? = null
    ) {
        if (sm != null) property(gridTemplateAutoColumnsKey, GridTemplateContext(this, smProperties).sm(), smProperties)
        if (md != null) property(gridTemplateAutoColumnsKey, GridTemplateContext(this, mdProperties).md(), mdProperties)
        if (lg != null) property(gridTemplateAutoColumnsKey, GridTemplateContext(this, lgProperties).lg(), lgProperties)
        if (xl != null) property(gridTemplateAutoColumnsKey, GridTemplateContext(this, xlProperties).xl(), xlProperties)
    }

    /**
     * This function sets the [grid-auto-flow](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-flow) property
     * for all media devices.
     *
     * It is recommended to use the predefined properties of [AutoFlowValues] as value.
     *
     * Example call:
     * ```
     * autoFlow { row }
     * ```
     *
     * @see AutoFlowValues
     *
     * @param value extension function parameter to bring the predefined properties of [AutoFlowValues]
     *              into the scope of the functional expression
     */
    fun autoFlow(value: AutoFlowValues.() -> AutoFlowProperty) =
        property(AutoFlowValues.key, AutoFlowValues.value(), smProperties)

    /**
     * This function sets the [grid-auto-flow](https://developer.mozilla.org/en/docs/Web/CSS/grid-auto-flow) property
     * for each media device independently.
     *
     * It is recommended to use the predefined properties of [AutoFlowValues] as value.
     *
     * Example call:
     * ```
     * autoFlow(
     *     sm = { column },
     *     lg = { row }
     * )
     * ```
     *
     * @see AutoFlowValues
     *
     * @param sm extension function parameter to bring the predefined properties of [AutoFlowValues]
     *           into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the predefined properties of [AutoFlowValues]
     *           into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined properties of [AutoFlowValues]
     *           into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined properties of [AutoFlowValues]
     *           into the scope of the functional expression for extra large media devices
     */
    fun autoFlow(
        sm: (AutoFlowValues.() -> AutoFlowProperty)? = null,
        md: (AutoFlowValues.() -> AutoFlowProperty)? = null,
        lg: (AutoFlowValues.() -> AutoFlowProperty)? = null,
        xl: (AutoFlowValues.() -> AutoFlowProperty)? = null
    ) {
        if (sm != null) property(AutoFlowValues.key, AutoFlowValues.sm(), smProperties)
        if (md != null) property(AutoFlowValues.key, AutoFlowValues.md(), mdProperties)
        if (lg != null) property(AutoFlowValues.key, AutoFlowValues.lg(), lgProperties)
        if (xl != null) property(AutoFlowValues.key, AutoFlowValues.xl(), xlProperties)
    }

    /**
     * This function opens a context ([GridTemplateContext]) to specify the overall layout of a grid layout via the
     * [grid-template-areas](https://developer.mozilla.org/en/docs/Web/CSS/grid-template-areas) CSS property
     * for all media devices.
     *
     * The actual definition for the rows is then done within the [GridAreaContext] using the [GridAreaContext.row]
     * function. One can pass in string literals directly of course, but for good reasons consider an approach,
     * where you manage the string based cell names within a type of _container_ like a [Map], [List] or an ``object``.
     *
     * A simple but working call might look like this:
     * ```
     * areas { /* it == GridAreaContext.() */
     *     row("HEADER", "HEADER", "HEADER")
     *     row("MENU", "CONTENT", "CONTENT")
     *     row("FOOTER", "FOOTER", "FOOTER")
     * }
     *
     * // later on in the corresponding child element
     * grid { area { "CONTENT" } }
     * //             ^^^^^^^
     * //             "Bind" the element to the named area in the template!
     * ```
     *
     * The former approach has some drawbacks:
     * - tedious to type (no autocompletion)
     * - not safe for refactorings (only search + replace is possible)
     * - easy to introduce typos
     * - easy to misspell the name in the related [dev.fritz2.styling.components.box] component and therefore break the
     *   relation!
     *
     * That is why it is recommended to use some sort of _container_ for the area names!
     *
     * Here is one solid approach using an object:
     * ```
     * // provide properties for all area names
     * val grid = object {
     *     val HEADER: AreaName = "header"
     *     val SIDEBAR: AreaName = "sidebar"
     *     val CONTENT: AreaName = "content"
     *     val FOOTER: AreaName = "footer"
     * }
     *
     * // inject the object into the component and use it
     * areas { /* it == GridAreaContext.() */
     *     with(grid) { /* it == "grid-object" */
     *         row(HEADER, HEADER, HEADER) // Type safe and refactoring ready names!
     *         row(MENU, CONTENT, CONTENT)
     *         row(FOOTER, FOOTER, FOOTER)
     *     }
     * }
     *
     * // inject the object later on in the corresponding child element too
     * grid { area { grid.CONTENT } }
     * //            ^^^^^^^^^^^^
     * //            "Bind" the element to the named area in the template! No misspelling, but ready for refactoring!
     * ```
     *
     * @see [GridAreaContext]
     *
     * @param value extension function parameter to open the [GridAreaContext] as scope of the functional expression
     *              in order to use its row function.
     */
    fun areas(value: GridAreaContext.() -> Unit) {
        val target = StringBuilder()
        GridAreaContext(this, target).value()
        property(gridTemplateAreasKey, "\n$target", smProperties)
    }

    /**
     * This function opens a context ([GridTemplateContext]) to specify the overall layout of a grid layout via the
     * [grid-template-areas](https://developer.mozilla.org/en/docs/Web/CSS/grid-template-areas) CSS property
     * for each media device independently.
     *
     * For a detailed overview and usage recommendations have a look at the variant for all media devices at once!
     *
     * @see GridLayout.areas
     *
     * @param sm extension function parameter to open the [GridAreaContext] as scope of the functional expression
     *           in order to use its row function for small media devices
     * @param md extension function parameter to open the [GridAreaContext] as scope of the functional expression
     *           in order to use its row function for medium sized media devices
     * @param lg extension function parameter to open the [GridAreaContext] as scope of the functional expression
     *           in order to use its row function for large media devices
     * @param xl extension function parameter to open the [GridAreaContext] as scope of the functional expression
     *           in order to use its row function for extra large media devices
     */
    fun areas(
        sm: (GridAreaContext.() -> Unit)? = null,
        md: (GridAreaContext.() -> Unit)? = null,
        lg: (GridAreaContext.() -> Unit)? = null,
        xl: (GridAreaContext.() -> Unit)? = null
    ) {
        if (sm != null) {
            val target = StringBuilder()
            GridAreaContext(this, target).sm()
            property(gridTemplateAreasKey, "\n$target", smProperties)
        }
        if (md != null) {
            val target = StringBuilder()
            GridAreaContext(this, target).md()
            property(gridTemplateAreasKey, "\n$target", mdProperties)
        }
        if (lg != null) {
            val target = StringBuilder()
            GridAreaContext(this, target).lg()
            property(gridTemplateAreasKey, "\n$target", lgProperties)
        }
        if (xl != null) {
            val target = StringBuilder()
            GridAreaContext(this, target).xl()
            property(gridTemplateAreasKey, "\n$target", xlProperties)
        }
    }

    /**
     * This function sets the [column-gap](https://developer.mozilla.org/en/docs/Web/CSS/column-gap) property
     * for all media devices.
     *
     * It is recommended to use the predefined properties from the [theme][dev.fritz2.styling.Theme.gridGap].
     *
     * Example call:
     * ```
     * columnGap { small }
     * ```
     *
     * @param value provide a value of type [ScaledValueProperty] that defines the gap of the column,
     *              recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     */
    fun columnGap(value: ScaledValueProperty) = property(columnGapKey, theme().gaps, value)

    /**
     * This function sets the [column-gap](https://developer.mozilla.org/en/docs/Web/CSS/column-gap) property
     * for each media device independently.
     *
     * It is recommended to use the predefined properties from the [theme][dev.fritz2.styling.Theme.gridGap].
     *
     * Example call:
     * ```
     * columnGap(
     *     sm = { small },
     *     lg = { large }
     * )
     * ```
     *
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the column for small media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the column for medium sized media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the column for large media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the column for extra large media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     */
    fun columnGap(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(columnGapKey, theme().gaps, sm, md, lg, xl)

    /**
     * This function sets the [row-gap](https://developer.mozilla.org/en/docs/Web/CSS/row-gap) property
     * for all media devices.
     *
     * It is recommended to use the predefined properties from the [theme][dev.fritz2.styling.Theme.gridGap].
     *
     * Example call:
     * ```
     * rowGap { small }
     * ```
     *
     * @param value provide a value of type [ScaledValueProperty] that defines the gap of the row,
     *              recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     */
    fun rowGap(value: ScaledValueProperty) = property(rowGapKey, theme().gaps, value)

    /**
     * This function sets the [row-gap](https://developer.mozilla.org/en/docs/Web/CSS/row-gap) property
     * for each media device independently.
     *
     * It is recommended to use the predefined properties from the [theme][dev.fritz2.styling.Theme.gridGap].
     *
     * Example call:
     * ```
     * rowGap(
     *     sm = { small },
     *     lg = { large }
     * )
     * ```
     *
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row for small media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row for medium sized media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row for large media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row for extra large media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     */
    fun rowGap(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(rowGapKey, theme().gaps, sm, md, lg, xl)

    /**
     * This _convenience_ function sets the gap for the rows and columns in a grid all at once.
     * for all media devices.
     *
     * It sets the following CSS properties:
     * - [row-gap](https://developer.mozilla.org/en/docs/Web/CSS/row-gap)
     * - [column-gap](https://developer.mozilla.org/en/docs/Web/CSS/column-gap)
     *
     * It is recommended to use the predefined properties from the [theme][dev.fritz2.styling.Theme.gridGap].
     *
     * Example call:
     * ```
     * gap { small }
     * ```
     *
     * @param value provide a value of type [ScaledValueProperty] that defines the gap of the row and column,
     *              recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     */
    fun gap(value: ScaledValueProperty) {
        columnGap(value)
        rowGap(value)
    }

    /**
     * This _convenience_ function sets the gap for the rows and columns in a grid all at once.
     * for each media device independently.
     *
     * It sets the following CSS properties:
     * - [row-gap](https://developer.mozilla.org/en/docs/Web/CSS/row-gap)
     * - [column-gap](https://developer.mozilla.org/en/docs/Web/CSS/column-gap)
     *
     * It is recommended to use the predefined properties from the [theme][dev.fritz2.styling.Theme.gridGap].
     *
     * Example call:
     * ```
     * gap(
     *     sm = { small },
     *     lg = { large }
     * )
     * ```
     *
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row and column for small media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row and column  for medium sized media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row and column for large media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     * @param sm provide a value of type [ScaledValueProperty] that defines the gap of the row and column for extra large media devices,
     *           recommended to use predefined values of the [theme][dev.fritz2.styling.theme.Theme.gridGap].
     */
    fun gap(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) {
        columnGap(sm, md, lg, xl)
        rowGap(sm, md, lg, xl)
    }

    /**
     * Function to create the CSS [fit-content](https://developer.mozilla.org/en/docs/Web/CSS/fit-content) function.
     */
    // TODO: Find better scope; could be also used outside of grid,
    //  like ``width``, ``height`` and similar!
    fun fitContent(value: Property) = "fit-content($value)"

}

