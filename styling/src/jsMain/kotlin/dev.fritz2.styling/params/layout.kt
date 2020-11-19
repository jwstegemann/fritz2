package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal const val widthKey = "width: "
internal const val heightKey = "height: "
internal const val minWidthKey = "min-width: "
internal const val maxWidthKey = "max-width: "
internal const val minHeightKey = "min-height: "
internal const val maxHeightKey = "max-height: "

/**
 * Alias for specific [DisplayValues] properties.
 */
typealias DisplayProperty = Property

/**
 * Predefined values for the [display](https://developer.mozilla.org/en/docs/Web/CSS/display)
 * property. Should be used as expression result in [Layout.display].
 */
object DisplayValues : PropertyValues {
    override val key = "display: "

    const val none: DisplayProperty = "none"
    const val inline: DisplayProperty = "inline"
    const val block: DisplayProperty = "block"
    const val contents: DisplayProperty = "contents"
    const val listItem: DisplayProperty = "list-item"
    const val inlineBlock: DisplayProperty = "inline-block"
    const val inlineTable: DisplayProperty = "inline-table"
    const val table: DisplayProperty = "table"
    const val tableCell: DisplayProperty = "table-cell"
    const val tableColumn: DisplayProperty = "table-column"
    const val tableColumnGroup: DisplayProperty = "table-column-group"
    const val tableFooterGroup: DisplayProperty = "table-footer-group"
    const val tableHeaderGroup: DisplayProperty = "table-header-group"
    const val tableRow: DisplayProperty = "table-row"
    const val tableRowGroup: DisplayProperty = "table-row-group"
    const val flex: DisplayProperty = "flex"
    const val inlineFlex: DisplayProperty = "inline-flex"
    const val grid: DisplayProperty = "grid"
    const val inlineGrid: DisplayProperty = "inline-grid"
    const val ruby: DisplayProperty = "ruby"
    const val rubyBase: DisplayProperty = "ruby-base"
    const val rubyText: DisplayProperty = "ruby-text"
    const val rubyBaseContainer: DisplayProperty = "ruby-base-container"
    const val rubyTextContainer: DisplayProperty = "ruby-text-container"
    const val runIn: DisplayProperty = "run-in"
    const val inherit: DisplayProperty = "inherit"
    const val initial: DisplayProperty = "initial"
    const val unset: DisplayProperty = "unset"
}

/**
 * Alias for specific [OverflowBaseValues] properties.
 */
typealias OverflowProperty = Property

/**
 * Predefined _common_ values for the [overflow](https://developer.mozilla.org/en/docs/Web/CSS/overflow)
 * related properties.
 *
 * This class acts only as base class and should not be used directly!
 *
 * Please use one of the implementing objects instead:
 *
 * @see OverflowValues
 * @see OverflowXValues
 * @see OverflowYValues
 *
 * @param key the name of the CSS property with a colon as suffix
 */
open class OverflowBaseValues(override val key: String) : PropertyValues {

    val visible: OverflowProperty = "visible"
    val hidden: OverflowProperty = "hidden"
    val scroll: OverflowProperty = "scroll"
    val auto: OverflowProperty = "auto"
    val inherit: OverflowProperty = "inherit"
}

/**
 * Predefined values for the [overflow](https://developer.mozilla.org/en/docs/Web/CSS/overflow)
 * property. Should be used as expression result in [Layout.overflow].
 */
object OverflowValues : OverflowBaseValues("overflow: ")

/**
 * Predefined values for the [overflow-x](https://developer.mozilla.org/en/docs/Web/CSS/overflow-x)
 * property. Should be used as expression result in [Layout.overflowX].
 */
object OverflowXValues : OverflowBaseValues("overflow-x: ") {
    const val clip: OverflowProperty = "clip"
    const val initial: OverflowProperty = "initial"
    const val unset: OverflowProperty = "unset"
}

/**
 * Predefined values for the [overflow-y](https://developer.mozilla.org/en/docs/Web/CSS/overflow-y)
 * property. Should be used as expression result in [Layout.overflowY].
 */
object OverflowYValues : OverflowBaseValues("overflow-y: ") {
    const val clip: OverflowProperty = "clip"
    const val initial: OverflowProperty = "initial"
    const val unset: OverflowProperty = "unset"
}

/**
 * Alias for specific [VerticalAlignValues] properties.
 */
typealias VerticalAlignProperty = Property

/**
 * Predefined values for the [vertical-align](https://developer.mozilla.org/en/docs/Web/CSS/vertical-align)
 * property. Should be used as expression result in [Layout.verticalAlign].
 */
object VerticalAlignValues : PropertyValues {
    override val key = "vertical-align: "

    const val baseline: VerticalAlignProperty = "baseline"
    const val sub: VerticalAlignProperty = "sub"
    const val `super`: VerticalAlignProperty = "super"
    const val textTop: VerticalAlignProperty = "textTop"
    const val textBottom: VerticalAlignProperty = "textBottom"
    const val middle: VerticalAlignProperty = "middle"
    const val top: VerticalAlignProperty = "top"
    const val bottom: VerticalAlignProperty = "bottom"
    const val inherit: VerticalAlignProperty = "inherit"
    const val initial: VerticalAlignProperty = "initial"
    const val unset: VerticalAlignProperty = "unset"
}

/**
 * This _context_ interface offers functions to **style**
 * [grid layout](https://developer.mozilla.org/en/docs/Web/CSS/CSS_Grid_Layout)-cells / child elements.
 *
 * It does **not** configure the grid template / container itself. This should be done by [GridLayout] functions!
 *
 * This interface offers all the inherited functions of [SelfAlignment] that corresponds to the
 * [CSS alignment module](https://www.w3.org/TR/css-align-3/).
 *
 * This class offers methods for _binding_ a cell or a group of cells to the area template of the grid layout.
 * This is done in two general ways:
 * - provide specific names for a cell via the [area] function
 * - provide _patterns_ with _start_ and _end_ values referring to either names or counting values. This can be done
 *   by [column] or [row].
 *
 * You can define the position of a cell within the grid layout as follows:
 * ```
 * // always need a component as child element like ``box``
 *
 * // named cell approach
 * box({
 *     grid { /* it == GridContext.() */
 *         area { "CONTENT" }
 *     }
 * })
 *
 * // pattern approach (
 * box({
 *     grid { /* it == GridContext.() */
 *         row {
 *             start { "CONTENT".start } // start at the beginning of the "content" area
 *             end { "CONTENT".end } // occupy all of the "content" area
 *         }
 *         column {
 *             start { "0" } // start at the beginning of the columns
 *             end { span(2) } // occupy two columns
 *         }
 *     }
 * })
 * ```
 *
 * @param styleParams basic context scope interface
 * @param selfAlignment common alignment functions for child elements
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@ExperimentalCoroutinesApi
class GridContext(
    styleParams: StyleParams,
    selfAlignment: SelfAlignment,
    private val target: StringBuilder
) : StyleParams by styleParams, SelfAlignment by selfAlignment {

    /**
     * This function is used to set the name of an area part of the grid layout via the
     * [grid-area](https://developer.mozilla.org/en/docs/Web/CSS/grid-area) property.
     *
     * example calls:
     * ```
     * area { "FOO" }
     * // better approach using an ``object`` (or other container)
     * area { grid.FOO }
     * ```
     *
     * @see [GridLayout.areas] for a detailed explanation of the management of _names_ and string literals within this
     *                         context.
     *
     * @param value extension function parameter to get an arbitrary area name back
     */
    fun area(value: () -> Property) = property("grid-area: ", value(), target)

    /**
     * This function is used to define the position within the columns of a grid for a child element using the
     * [grid-column](https://developer.mozilla.org/en/docs/Web/CSS/grid-column) property.
     *
     * example call:
     * ```
     * column {
     *     // use ``start`` and ``end`` functions of [GridRowColumnContext]
     * }
     * ```
     *
     * @see GridRowColumnContext
     *
     * @param value extension function parameter to bring the specialized functions of the [GridRowColumnContext]
     *              into the scope of the functional expression
     */
    fun column(value: GridRowColumnContext.() -> Unit) {
        GridRowColumnContext("column", this, target).value()
    }

    /**
     * This function is used to define the position within the rows of a grid for a child element using the
     * [grid-row](https://developer.mozilla.org/en/docs/Web/CSS/grid-row) property.
     *
     * example call:
     * ```
     * row {
     *     // use ``start`` and ``end`` functions of [GridRowColumnContext]
     * }
     * ```
     *
     * @see GridRowColumnContext
     *
     * @param value extension function parameter to bring the specialized functions of the [GridRowColumnContext]
     *              into the scope of the functional expression
     */
    fun row(value: GridRowColumnContext.() -> Unit) {
        GridRowColumnContext("row", this, target).value()
    }
}


@ExperimentalCoroutinesApi
class GridRowColumnContext(
    private val keyFragment: String,
    styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This function is used to define the start position within the columns or rows of a grid for a child element
     * using the CSS properties:
     * - [grid-column-start](https://developer.mozilla.org/en/docs/Web/CSS/grid-column-start)
     * - [grid-row-start](https://developer.mozilla.org/en/docs/Web/CSS/grid-row-start)
     *
     * example calls:
     * ```
     * start { "CONTENT".start } // start at the beginning of the "content" area
     * start { "0" } // start at the top of the columns / rows
     * start { span(3) } start after three cells from the top
     * ```
     *
     * @param value function parameter to get a CSS property back
     */
    fun start(value: () -> Property) = property("grid-$keyFragment-start: ", value(), target)

    /**
     * This function is used to define the end position within the columns or rows of a grid for a child element
     * using the CSS properties:
     * - [grid-column-end](https://developer.mozilla.org/en/docs/Web/CSS/grid-column-end)
     * - [grid-row-end](https://developer.mozilla.org/en/docs/Web/CSS/grid-row-end)
     *
     * example calls:
     * ```
     * end { "CONTENT".start } // end at the beginning of the "content" area
     * end { "CONTENT".end } // end at the end of the "content" area
     * end { "5" } // end at the fifth column or row from the bottom
     * end { span(3) } end before three cells from the bottom
     * ```
     *
     * @param value function parameter to get a CSS property back
     */
    fun end(value: () -> Property) = property("grid-$keyFragment-end: ", value(), target)

    /**
     * This function creates a ``span`` expression for integer values.
     *
     * @param value count of rows / columns
     */
    fun span(value: Int) = "span $value"

    /**
     * This function creates a ``span`` expression for generic properties.
     *
     * @param value arbitrary expression, often named areas
     */
    fun span(value: String) = "span $value"
}

/**
 * Alias for specific [FlexBasisValues] properties.
 */
typealias FlexBasisProperty = Property

/**
 * Predefined values for the [flex-basis](https://developer.mozilla.org/en/docs/Web/CSS/flex-basis)
 * property. Should be used as expression result in [FlexItemContext.basis].
 */
object FlexBasisValues : PropertyValues {
    override val key = "flex-basis: "

    const val auto: FlexBasisProperty = "auto"
    const val fill: FlexBasisProperty = "fill"
    const val maxContent: FlexBasisProperty = "max-content"
    const val minContent: FlexBasisProperty = "min-content"
    const val fitContent: FlexBasisProperty = "fit-content"
    const val content: FlexBasisProperty = "content"
    const val inherit: FlexBasisProperty = "inherit"
    const val initial: FlexBasisProperty = "initial"
    const val unset: FlexBasisProperty = "unset"
}

/**
 * This _context_ interface offers functions to **style** elements of a flex layout.
 *
 * It does **not** configure the flex template / container itself. This should be done by [Flexbox] functions!
 *
 * This interface offers all the inherited functions of [SelfAlignment] that corresponds to the
 * [CSS alignment module](https://www.w3.org/TR/css-align-3/).
 *
 * This class offers functions to set the [order] of appearance, the [growth][grow], the [shrinking][shrink] and the
 * initial [size][basis].
 *
 * This enables the definition of a layout item like this:
 * ```
 * flex {
 *     order { 2 } // element will appear after those with _lower_ values, independently of the position within the
 *                 // DOM-Node!
 *     grow { 1 } // factor of growth
 *     shrink { 1 } // factor of shrinking
 *     basis { "10em" } // width of the element
 * }
 * ```
 *
 * @param styleParams basic context scope interface
 * @param selfAlignment common alignment functions for child elements
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@ExperimentalCoroutinesApi
class FlexItemContext(
    styleParams: StyleParams,
    selfAlignment: SelfAlignment,
    private val target: StringBuilder
) : StyleParams by styleParams, SelfAlignment by selfAlignment {

    /**
     * This function sets the [order](https://developer.mozilla.org/en/docs/Web/CSS/order) CSS property.
     *
     * example call:
     * ```
     * order { 2 }
     * ```
     *
     * @param value function parameter to get an order value back
     */
    fun order(value: () -> Property) = property("order: ", value(), target)

    /**
     * This function sets the [flex-grow](https://developer.mozilla.org/en/docs/Web/CSS/flex-grow) CSS property.
     *
     * example call:
     * ```
     * grow { 2 }
     * ```
     *
     * @param value function parameter to get a growth factor back
     */
    fun grow(value: () -> Property) = property("flex-grow: ", value(), target)

    /**
     * This function sets the [flex-shrink](https://developer.mozilla.org/en/docs/Web/CSS/flex-shrink) CSS property.
     *
     * example call:
     * ```
     * shrink { 2 }
     * ```
     *
     * @param value function parameter to get a shrink factor back
     */
    fun shrink(value: () -> Property) = property("flex-shrink: ", value(), target)

    /**
     * This function sets the [flex-basis](https://developer.mozilla.org/en/docs/Web/CSS/flex-basis) CSS property.
     *
     * example call:
     * ```
     * basis { content } // sizing based upon the content of the element
     * ```
     *
     * @param value extension function parameter to bring the predefined values of the [FlexBasisValues] class into
     *              the scope of the expression
     */
    fun basis(value: FlexBasisValues.() -> FlexBasisProperty) =
        property(FlexBasisValues.key, FlexBasisValues.value(), target)
}


/**
 * This _context_ interface offers different functions to define or affect the fundamental layout of the whole page,
 * a container within the page or an element itself.
 *
 * There are always two variants of the same function; one for applying to all media devices at once and another to
 * specify the functionality for each media device independently.
 */
@ExperimentalCoroutinesApi
interface Layout : StyleParams {

    /**
     * This function passes raw CSS code into the underlying model without modification by fritz2 for all media devices
     * at once.
     *
     * This could be useful for corner cases where our abstractions fail, to use new CSS features
     * we have not yet implemented or simply for toying around with CSS code from external sources.
     *
     * @param value raw CSS code as string
     */
    fun css(value: Property) = smProperties.append(value.let { if (it.endsWith(";")) it else "$it;" })

    /**
     * This function passes raw CSS code into the underlying model without modification by fritz2 for each media device
     * independently.
     *
     * This could be useful for corner cases where our abstractions fail, to use new CSS features
     * we have not yet implemented or simply for toying around with CSS code from external sources.
     *
     * @param sm raw CSS code as string for small media devices
     * @param md raw CSS code as string for medium sized media devices
     * @param lg raw CSS code as string for large media devices
     * @param xl raw CSS code as string for extra large media devices
     */
    fun css(
        sm: Property? = null,
        md: Property? = null,
        lg: Property? = null,
        xl: Property? = null
    ) {
        if (sm != null) smProperties.append(sm)
        if (md != null) mdProperties.append(sm)
        if (lg != null) lgProperties.append(sm)
        if (xl != null) xlProperties.append(sm)
    }


    /**
     * This _convenience_ function sets the [width] and [height] of a component at once for all media devices.
     *
     * example calls:
     * ```
     * size { small } // use a predefined symbol
     * size { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun size(value: SizesProperty) {
        width(value)
        height(value)
    }

    /**
     * This _convenience_ function sets the [width] and [height] of a component at once for all media devices.
     *
     * example calls:
     * ```
     * size { small } // use a predefined symbol
     * size { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun size(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) {
        width(sm, md, lg, xl)
        height(sm, md, lg, xl)
    }

    /**
     * This function sets the [width](https://developer.mozilla.org/en/docs/Web/CSS/width) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * width { small } // use a predefined symbol
     * width { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun width(value: SizesProperty) = property(widthKey, theme().sizes, value)

    /**
     * This function sets the [width](https://developer.mozilla.org/en/docs/Web/CSS/width) property of a component
     * for each media device independently.
     *
     * example calls:
     * ```
     * width { small } // use a predefined symbol
     * width { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun width(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) =
        property(widthKey, theme().sizes, sm, md, lg, xl)

    /**
     * This function sets the [height](https://developer.mozilla.org/en/docs/Web/CSS/height) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * height { small } // use a predefined symbol
     * height { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun height(value: SizesProperty) = property(heightKey, theme().sizes, value)

    /**
     * This function sets the [height](https://developer.mozilla.org/en/docs/Web/CSS/height) property of a component
     * for each media device independently.
     *
     * example calls:
     * ```
     * height { small } // use a predefined symbol
     * height { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun height(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) =
        property(heightKey, theme().sizes, sm, md, lg, xl)

    /**
     * This function sets the [min-width](https://developer.mozilla.org/en/docs/Web/CSS/min-width) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * minWidth { small } // use a predefined symbol
     * minWidth { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun minWidth(value: SizesProperty) = property(minWidthKey, theme().sizes, value)

    /**
     * This function sets the [min-width](https://developer.mozilla.org/en/docs/Web/CSS/min-width) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * minWidth { small } // use a predefined symbol
     * minWidth { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun minWidth(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) =
        property(minWidthKey, theme().sizes, sm, md, lg, xl)

    /**
     * This function sets the [max-width](https://developer.mozilla.org/en/docs/Web/CSS/max-width) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * maxWidth { small } // use a predefined symbol
     * maxWidth { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun maxWidth(value: SizesProperty) = property(maxWidthKey, theme().sizes, value)

    /**
     * This function sets the [max-width](https://developer.mozilla.org/en/docs/Web/CSS/max-width) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * maxWidth { small } // use a predefined symbol
     * maxWidth { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun maxWidth(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) =
        property(maxWidthKey, theme().sizes, sm, md, lg, xl)

    /**
     * This function sets the [min-height](https://developer.mozilla.org/en/docs/Web/CSS/min-height) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * minHeight { small } // use a predefined symbol
     * minHeight { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun minHeight(value: SizesProperty) = property(minHeightKey, theme().sizes, value)

    /**
     * This function sets the [min-height](https://developer.mozilla.org/en/docs/Web/CSS/min-height) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * minHeight { small } // use a predefined symbol
     * minHeight { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun minHeight(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) =
        property(minHeightKey, theme().sizes, sm, md, lg, xl)

    /**
     * This function sets the [max-height](https://developer.mozilla.org/en/docs/Web/CSS/max-height) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * maxHeight { small } // use a predefined symbol
     * maxHeight { "2em" } // provide a custom value
     * ```
     *
     * @param value custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] like [dev.fritz2.styling.theme.Sizes.normal]
     *             or alike.
     */
    fun maxHeight(value: SizesProperty) = property(maxHeightKey, theme().sizes, value)

    /**
     * This function sets the [max-height](https://developer.mozilla.org/en/docs/Web/CSS/max-height) property of a component
     * for all media devices.
     *
     * example calls:
     * ```
     * maxHeight { small } // use a predefined symbol
     * maxHeight { "2em" } // provide a custom value
     * ```
     *
     * @param sm custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for small media devices
     * @param md custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for medium sized media devices
     * @param lg custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for large media devices
     * @param xl custom property or predefined symbol by [dev.fritz2.styling.theme.Sizes] for extra large media devices
     */
    fun maxHeight(
        sm: SizesProperty? = null,
        md: SizesProperty? = null,
        lg: SizesProperty? = null,
        xl: SizesProperty? = null
    ) =
        property(maxHeightKey, theme().sizes, sm, md, lg, xl)

    /**
     * This function sets the [display](https://developer.mozilla.org/en/docs/Web/CSS/display) property of a component
     * for all media devices.
     *
     * If you want to create a flex- or grid-layout prefer to use the specialized ``dev.fritz2.styling.components.flexBox``
     * and ``dev.fritz2.styling.components.gridBox`` factory functions in order to create a box, that has this property
     * already set.
     *
     * example calls:
     * ```
     * display { inline } // use predefined symbols
     * display { block }
     * // Try to avoid
     * display { flex }
     * ```
     *
     * @param value extension function parameter to bring the predefined values of [DisplayValues] into the scope of
     *              the functional expression
     */
    fun display(value: DisplayValues.() -> DisplayProperty) = property(DisplayValues, value)

    /**
     * This function sets the [display](https://developer.mozilla.org/en/docs/Web/CSS/display) property of a component
     * for each media device independently.
     *
     * If you want to create a flex- or grid-layout prefer to use the specialized ``dev.fritz2.components.flexBox``
     * and ``dev.fritz2.components.gridBox`` factory functions in order to create a box, that has this property
     * already set.
     *
     * example calls:
     * ```
     * display { inline } // use predefined symbols
     * display { block }
     * // Try to avoid
     * display { flex }
     * ```
     *
     * @param sm extension function parameter to bring the predefined values of [DisplayValues] into the scope of
     *           the functional expression for small media devices
     * @param md extension function parameter to bring the predefined values of [DisplayValues] into the scope of
     *           the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined values of [DisplayValues] into the scope of
     *           the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined values of [DisplayValues] into the scope of
     *           the functional expression for extra large media devices
     */
    fun display(
        sm: (DisplayValues.() -> DisplayProperty)? = null,
        md: (DisplayValues.() -> DisplayProperty)? = null,
        lg: (DisplayValues.() -> DisplayProperty)? = null,
        xl: (DisplayValues.() -> DisplayProperty)? = null
    ) =
        property(DisplayValues, sm, md, lg, xl)

    /**
     * This function sets the [vertical-align](https://developer.mozilla.org/en/docs/Web/CSS/vertical-align) property
     * of a component for all media devices.
     *
     * example calls:
     * ```
     * verticalAlign { top } // use a predefined value
     * verticalAlign { "1em" } // provide a custom value
     * ```
     *
     * @param value extension function parameter to bring the predefined values of [VerticalAlignValues] into the scope of
     *              the functional expression
     */
    fun verticalAlign(value: VerticalAlignValues.() -> VerticalAlignProperty) =
        property(VerticalAlignValues, value)

    /**
     * This function sets the [vertical-align](https://developer.mozilla.org/en/docs/Web/CSS/vertical-align) property
     * of a component for each media device independently.
     *
     * example calls:
     * ```
     * verticalAlign { top } // use a predefined value
     * verticalAlign { "1em" } // provide a custom value
     * ```
     *
     * @param sm extension function parameter to bring the predefined values of [VerticalAlignValues] into the scope of
     *           the functional expression for small media devices
     * @param md extension function parameter to bring the predefined values of [VerticalAlignValues] into the scope of
     *           the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined values of [VerticalAlignValues] into the scope of
     *           the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined values of [VerticalAlignValues] into the scope of
     *           the functional expression for extra large media devices
     */
    fun verticalAlign(
        sm: (VerticalAlignValues.() -> VerticalAlignProperty)? = null,
        md: (VerticalAlignValues.() -> VerticalAlignProperty)? = null,
        lg: (VerticalAlignValues.() -> VerticalAlignProperty)? = null,
        xl: (VerticalAlignValues.() -> VerticalAlignProperty)? = null
    ) =
        property(VerticalAlignValues, sm, md, lg, xl)

    /**
     * This function sets the [overflow](https://developer.mozilla.org/en/docs/Web/CSS/overflow) property
     * of a component for all media devices.
     *
     * example calls:
     * ```
     * overflow { scroll } // use a predefined value
     * ```
     *
     * @param value extension function parameter to bring the predefined values of [OverflowValues] into the scope of
     *              the functional expression
     */
    fun overflow(value: OverflowValues.() -> OverflowProperty) = property(OverflowValues, value)

    /**
     * This function sets the [overflow](https://developer.mozilla.org/en/docs/Web/CSS/overflow) property
     * of a component for each media device independently.
     *
     * example calls:
     * ```
     * overflow { scroll } // use a predefined value
     * ```
     *
     * @param sm extension function parameter to bring the predefined values of [OverflowValues] into the scope of
     *           the functional expression for small media devices
     * @param md extension function parameter to bring the predefined values of [OverflowValues] into the scope of
     *           the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined values of [OverflowValues] into the scope of
     *           the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined values of [OverflowValues] into the scope of
     *           the functional expression for extra large media devices
     */
    fun overflow(
        sm: (OverflowValues.() -> OverflowProperty)? = null,
        md: (OverflowValues.() -> OverflowProperty)? = null,
        lg: (OverflowValues.() -> OverflowProperty)? = null,
        xl: (OverflowValues.() -> OverflowProperty)? = null
    ) =
        property(OverflowValues, sm, md, lg, xl)

    /**
     * This function sets the [overflow-x](https://developer.mozilla.org/en/docs/Web/CSS/overflow-x) property
     * of a component for all media devices.
     *
     * example calls:
     * ```
     * overflowX { scroll } // use a predefined value
     * ```
     *
     * @param value extension function parameter to bring the predefined values of [OverflowXValues] into the scope of
     *              the functional expression
     */
    fun overflowX(value: OverflowXValues.() -> OverflowProperty) = property(OverflowXValues, value)

    /**
     * This function sets the [overflow-x](https://developer.mozilla.org/en/docs/Web/CSS/overflow-x) property
     * of a component for all media devices.
     *
     * example calls:
     * ```
     * overflowX { scroll } // use a predefined value
     * ```
     *
     * @param sm extension function parameter to bring the predefined values of [OverflowXValues] into the scope of
     *           the functional expression for small media devices
     * @param md extension function parameter to bring the predefined values of [OverflowXValues] into the scope of
     *           the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined values of [OverflowXValues] into the scope of
     *           the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined values of [OverflowXValues] into the scope of
     *           the functional expression for extra large media devices
     */
    fun overflowX(
        sm: (OverflowXValues.() -> OverflowProperty)? = null,
        md: (OverflowXValues.() -> OverflowProperty)? = null,
        lg: (OverflowXValues.() -> OverflowProperty)? = null,
        xl: (OverflowXValues.() -> OverflowProperty)? = null
    ) =
        property(OverflowXValues, sm, md, lg, xl)

    /**
     * This function sets the [overflow-y](https://developer.mozilla.org/en/docs/Web/CSS/overflow-y) property
     * of a component for all media devices.
     *
     * example calls:
     * ```
     * overflowY { scroll } // use a predefined value
     * ```
     *
     * @param value extension function parameter to bring the predefined values of [OverflowYValues] into the scope of
     *              the functional expression
     */
    fun overflowY(value: OverflowYValues.() -> OverflowProperty) = property(OverflowYValues, value)

    /**
     * This function sets the [overflow-y](https://developer.mozilla.org/en/docs/Web/CSS/overflow-y) property
     * of a component for all media devices.
     *
     * example calls:
     * ```
     * overflowY { scroll } // use a predefined value
     * ```
     *
     * @param sm extension function parameter to bring the predefined values of [OverflowYValues] into the scope of
     *           the functional expression for small media devices
     * @param md extension function parameter to bring the predefined values of [OverflowYValues] into the scope of
     *           the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined values of [OverflowYValues] into the scope of
     *           the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined values of [OverflowYValues] into the scope of
     *           the functional expression for extra large media devices
     */
    fun overflowY(
        sm: (OverflowYValues.() -> OverflowProperty)? = null,
        md: (OverflowYValues.() -> OverflowProperty)? = null,
        lg: (OverflowYValues.() -> OverflowProperty)? = null,
        xl: (OverflowYValues.() -> OverflowProperty)? = null
    ) =
        property(OverflowYValues, sm, md, lg, xl)

    /**
     * This function opens the context for defining the grid layout related properties for a grid layout child component
     * for all media devices.
     *
     * This function does **not** create a layout itself; for the latter have a look at [GridLayout] functions!
     *
     * Example call:
     * ```
     * grid {
     *     // some functions of [GridContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @see GridContext
     *
     * @param value extension function parameter to bring the specialized topical functions of the [GridContext]
     *              into the scope of the functional expression
     */
    fun grid(value: GridContext.() -> Unit) =
        GridContext(this, SelfAlignmentImpl(this, smProperties), smProperties).value()

    /**
     * This function opens the context for defining the grid layout related properties for a grid layout child component
     * for each media device independently.
     *
     * This function does **not** create a layout itself; for the latter have a look at [GridLayout] functions!
     *
     * Example call:
     * ```
     * grid {
     *     // some functions of [GridContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @see GridContext
     *
     * @param sm extension function parameter to bring the specialized topical functions of the [GridContext]
     *           into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the specialized topical functions of the [GridContext]
     *           into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the specialized topical functions of the [GridContext]
     *           into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the specialized topical functions of the [GridContext]
     *           into the scope of the functional expression for extra large media devices
     */
    fun grid(
        sm: (GridContext.() -> Unit)? = null,
        md: (GridContext.() -> Unit)? = null,
        lg: (GridContext.() -> Unit)? = null,
        xl: (GridContext.() -> Unit)? = null
    ) {
        if (sm != null) GridContext(this, SelfAlignmentImpl(this, smProperties), smProperties).sm()
        if (md != null) GridContext(this, SelfAlignmentImpl(this, mdProperties), mdProperties).md()
        if (lg != null) GridContext(this, SelfAlignmentImpl(this, lgProperties), lgProperties).lg()
        if (xl != null) GridContext(this, SelfAlignmentImpl(this, xlProperties), xlProperties).xl()
    }

    /**
     * This function opens the context for defining the flex layout related properties for a flex layout child component
     * for all media devices.
     *
     * This function does **not** create a layout itself; for the latter have a look at [Flexbox] functions!
     *
     * Example call:
     * ```
     * flex {
     *     // some functions of [FlexItemContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @see FlexItemContext
     *
     * @param value extension function parameter to bring the specialized topical functions of the [FlexItemContext]
     *              into the scope of the functional expression
     */
    fun flex(value: FlexItemContext.() -> Unit) =
        FlexItemContext(this, SelfAlignmentImpl(this, smProperties), smProperties).value()

    /**
     * This function opens the context for defining the flex layout related properties for a flex layout child component
     * for all media devices.
     *
     * This function does **not** create a layout itself; for the latter have a look at [Flexbox] functions!
     *
     * Example call:
     * ```
     * flex {
     *     // some functions of [FlexItemContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @see FlexItemContext
     *
     * @param sm extension function parameter to bring the specialized topical functions of the [FlexItemContext]
     *           into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the specialized topical functions of the [FlexItemContext]
     *           into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the specialized topical functions of the [FlexItemContext]
     *           into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the specialized topical functions of the [FlexItemContext]
     *           into the scope of the functional expression for extra large media devices
     */
    fun flex(
        sm: (FlexItemContext.() -> Unit)? = null,
        md: (FlexItemContext.() -> Unit)? = null,
        lg: (FlexItemContext.() -> Unit)? = null,
        xl: (FlexItemContext.() -> Unit)? = null
    ) {
        if (sm != null) FlexItemContext(this, SelfAlignmentImpl(this, smProperties), smProperties).sm()
        if (md != null) FlexItemContext(this, SelfAlignmentImpl(this, mdProperties), mdProperties).md()
        if (lg != null) FlexItemContext(this, SelfAlignmentImpl(this, lgProperties), lgProperties).lg()
        if (xl != null) FlexItemContext(this, SelfAlignmentImpl(this, xlProperties), xlProperties).xl()
    }
}