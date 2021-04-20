package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.Thickness
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Alias for specific [BorderStyleValues] properties.
 */
typealias BorderStyleProperty = Property

/**
 * Predefined values for the [border-style](https://developer.mozilla.org/en/docs/Web/CSS/border-style)
 * property. Should be used as expression result in [BorderContext.style].
 */
object BorderStyleValues : PropertyValues {
    override val key = "border-style: "

    const val none: BorderStyleProperty = "none"
    const val hidden: BorderStyleProperty = "hidden"
    const val dotted: BorderStyleProperty = "dotted"
    const val dashed: BorderStyleProperty = "dashed"
    const val solid: BorderStyleProperty = "solid"
    const val double: BorderStyleProperty = "double"
    const val groove: BorderStyleProperty = "groove"
    const val ridge: BorderStyleProperty = "ridge"
    const val inset: BorderStyleProperty = "inset"
    const val outset: BorderStyleProperty = "outset"
    const val initial: BorderStyleProperty = "initial"
    const val inherit: BorderStyleProperty = "inherit"
}


internal const val borderWidthKey = "border-width: "
internal const val borderStyleKey = "border-style: "
internal const val borderColorKey = "border-color: "

internal const val borderTopWidthKey = "border-top-width: "
internal const val borderTopStyleKey = "border-top-style: "
internal const val borderTopColorKey = "border-top-color: "

internal const val borderRightWidthKey = "border-right-width: "
internal const val borderRightStyleKey = "border-right-style: "
internal const val borderRightColorKey = "border-right-color: "

internal const val borderBottomWidthKey = "border-bottom-width: "
internal const val borderBottomStyleKey = "border-bottom-style: "
internal const val borderBottomColorKey = "border-bottom-color: "

internal const val borderLeftWidthKey = "border-left-width: "
internal const val borderLeftStyleKey = "border-left-style: "
internal const val borderLeftColorKey = "border-left-color: "

internal const val borderRadiusKey = "border-radius: "
internal const val borderTopLeftRadiusKey = "border-top-left-radius: "
internal const val borderTopRightRadiusKey = "border-top-right-radius: "
internal const val borderBottomRightRadiusKey = "border-bottom-right-radius: "
internal const val borderBottomLeftRadiusKey = "border-bottom-left-radius: "


@ExperimentalCoroutinesApi
/**
 * This _context class_ enables the definition of the common border styling properties like _width_, _style_ and _color_.
 *
 * This _context_ is passed as receiver either by [Border.border] functions for setting the properties for all
 * sides at once or by [BordersContext] functions for defining each side independently. Called by the latter this context
 * acts as a _nested context_.
 *
 * The definable properties for the direct usage by [Border.border] correspond to the
 * [border-width](https://developer.mozilla.org/en/docs/Web/CSS/border-width),
 * [border-style](https://developer.mozilla.org/en/docs/Web/CSS/border-style) and
 * [border-color](https://developer.mozilla.org/en/docs/Web/CSS/border-color) CSS properties.
 *
 * This usage enables border styling like this:
 * ```
 * border { /* it == BorderContext.() -> Unit */
 *     width { thin }
 *     style { dashed }
 *     color { dark }
 * }
 * ```
 *
 * Used within the nested context those three properties correspond to the ``border-{side}-{type}`` properties,
 * where ``type`` is one of ``width``, ``style`` or ``color`` and ``side`` is one of ``top``, ``right``, ``bottom``
 * or ``left``.
 *
 * This usage enables border styling for each side like this:
 * ```
 * borders { it == /* BordersContext.() -> Unit - with plural "s"! */
 *     top {  it == /* BorderContext.() -> Unit */
 *         width { thin }
 *         style { dashed }
 *         color { dark }
 *     },
 *     bottom {
 *         // some properties
 *     },
 *     // ... define the rest with ``right`` and ``left``
 * }
 * ```
 *
 * @param widthKey the CSS-property for the _width_
 * @param styleKey the CSS-property for the _style_
 * @param colorKey the CSS-property for the _color_
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
class BorderContext(
    private val widthKey: String,
    private val styleKey: String,
    private val colorKey: String,
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This function is used to set the _width_ of a border for the
     * [border-width](https://developer.mozilla.org/en/docs/Web/CSS/border-width) property or the individual side width
     * property according to the passed [BorderContext.widthKey] value like ``border-{side}-width``.
     *
     * example call:
     * ```
     * border {
     *     width { normal }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined thickness values ([Thickness]) of the theme
     *              into the scope of the functional expression
     */
    fun width(value: Thickness.() -> Property) = property(widthKey, Theme().borderWidths, value, target)

    /**
     * This function is used to set the _style_ of a border for the
     * [border-style](https://developer.mozilla.org/en/docs/Web/CSS/border-style) property or the individual side style
     * property according to the passed [BorderContext.styleKey] value like ``border-{side}-style``.
     *
     * example call:
     * ```
     * border {
     *     style { dotted }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined border style values from [BorderStyleValues]
     *              into the scope of the functional expression
     */
    fun style(value: BorderStyleValues.() -> BorderStyleProperty) =
        property(styleKey, BorderStyleValues.value(), smProperties)

    /**
     * This function is used to set the _color_ of a border for the
     * [border-color](https://developer.mozilla.org/en/docs/Web/CSS/border-color) property or the individual side color
     * property according to the passed [BorderContext.colorKey] value like ``border-{side}-color``.
     *
     * example call:
     * ```
     * border {
     *     color { dark }
     *     // color { "lime" }
     *     // color { rgba(255, 0, 0, 100 }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined color values of the theme ([Colors])
     *              into the scope of the functional expression
     */
    fun color(value: Colors.() -> Property) = property(colorKey, Theme().colors, value, target)
}

/**
 * This _context class_ acts as an _intermediate context_ to style the four sides of a border independently.
 *
 * It offers the four functions [top], [right], [bottom] and [left] to bring in the [BorderContext] scope in order
 * to set the properties for each side individually.
 *
 * There are also two convenience functions [horizontal] and [vertical] that enables the pairwise styling for the
 * related horizontal or vertical sides.
 *
 * For the final usage have a look at [BorderContext].
 *
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@ExperimentalCoroutinesApi
class BordersContext(
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This function opens a context ([BorderContext]) to specify the border styles for the _top_ side of a block
     * via the ``border-top-{property}`` CSS properties.
     *
     * The actual styling is then done within the [BorderContext] using the specific topic functions of it.
     *
     * @see [BorderContext]
     *
     * @param value extension function parameter to open the [BorderContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun top(value: BorderContext.() -> Unit) =
        BorderContext(borderTopWidthKey, borderTopStyleKey, borderTopColorKey, this, target).value()

    /**
     * This function opens a context ([BorderContext]) to specify the border styles for the _right_ side of a block
     * via the ``border-right-{property}`` CSS properties.
     *
     * The actual styling is then done within the [BorderContext] using the specific topic functions of it.
     *
     * @see [BorderContext]
     *
     * @param value extension function parameter to open the [BorderContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun right(value: BorderContext.() -> Unit) =
        BorderContext(borderRightWidthKey, borderRightStyleKey, borderRightColorKey, this, target).value()

    /**
     * This function opens a context ([BorderContext]) to specify the border styles for the _bottom_ side of a block
     * via the ``border-bottom-{property}`` CSS properties.
     *
     * The actual styling is then done within the [BorderContext] using the specific topic functions of it.
     *
     * @see [BorderContext]
     *
     * @param value extension function parameter to open the [BorderContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun bottom(value: BorderContext.() -> Unit) =
        BorderContext(borderBottomWidthKey, borderBottomStyleKey, borderBottomColorKey, this, target).value()

    /**
     * This function opens a context ([BorderContext]) to specify the border styles for the _left_ side of a block
     * via the ``border-left-{property}`` CSS properties.
     *
     * The actual styling is then done within the [BorderContext] using the specific topic functions of it.
     *
     * @see [BorderContext]
     *
     * @param value extension function parameter to open the [BorderContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun left(value: BorderContext.() -> Unit) =
        BorderContext(borderLeftWidthKey, borderLeftStyleKey, borderLeftColorKey, this, target).value()

    /**
     * This is a _convenience_ function that opens a context ([BorderContext]) to specify the border styles
     * for both vertical sides of a block at once, that is the _right_ and _left_ hand side via
     * ``border-left-{property}`` and ``border-right-{property}`` CSS properties.
     *
     * The actual styling is then done within the [BorderContext] using the specific topic functions of it.
     *
     * @see [BorderContext]
     *
     * @param value extension function parameter to open the [BorderContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun vertical(value: BorderContext.() -> Unit) {
        right(value)
        left(value)
    }

    /**
     * This is a _convenience_ function that opens a context ([BorderContext]) to specify the border styles
     * for both horizontal sides of a block at once, that is the _top_ and _bottom_ side via
     * ``border-top-{property}`` and ``border-bottom-{property}`` CSS properties.
     *
     * The actual styling is then done within the [BorderContext] using the specific topic functions of it.
     *
     * @see [BorderContext]
     *
     * @param value extension function parameter to open the [BorderContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun horizontal(value: BorderContext.() -> Unit) {
        top(value)
        bottom(value)
    }
}

/**
 * This _context class_ is used to provide a scope for styling the four radii of a border independently.
 *
 * It offers the four functions [topLeft], [topRight], [bottomRight] and [bottomLeft] to set the radius property for
 * each corner independently. There are also convenience functions to handle the radii of one _side_ at once, like
 * [right] and [left] or [top] and [bottom]. Those are most useful for pairwise usage of course.
 *
 * The theme offers predefined values as [ScaledValueProperty] via its [dev.fritz2.styling.theme.Theme.radii] property.
 *
 * This usage enables border styling for each side like this:
 * ```
 * radii { /* it == RadiiContext.() -> Property */
 *     topLeft { small } // predefined values only for _circles_!
 *     topRight { small }
 *     bottomRight { big }
 *     bottomLeft { "1em 2em" } // provide a custom property for _ellipsis_
 * }
 * // or with a pair of convenience functions:
 * radii {
 *     top { small }
 *     bottom { large }
 * }
 * ```
 *
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
//FIXME: inline funs?
@ExperimentalCoroutinesApi
class RadiiContext(
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This function enables the definition of the border radius for the _top left_ corner of a block via the
     * [border-top-left-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-top-left-radius) CSS property.
     *
     * example call:
     * ```
     * radius {
     *     topLeft { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun topLeft(value: ScaledValueProperty) = property(borderTopLeftRadiusKey, Theme().radii, value, target)

    /**
     * This function enables the definition of the border radius for the _top right_ corner of a block via the
     * [border-top-right-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-top-right-radius) CSS property.
     *
     * example call:
     * ```
     * radius {
     *     topRight { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun topRight(value: ScaledValueProperty) = property(borderTopRightRadiusKey, Theme().radii, value, target)

    /**
     * This function enables the definition of the border radius for the _bottom right_ corner of a block via the
     * [border-bottom-right-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-bottom-right-radius) CSS property.
     *
     * example call:
     * ```
     * radius {
     *     bottomRight { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun bottomRight(value: ScaledValueProperty) = property(borderBottomRightRadiusKey, Theme().radii, value, target)

    /**
     * This function enables the definition of the border radius for the _bottom left_ corner of a block via the
     * [border-bottom-left-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-bottom-left-radius) CSS property.
     *
     * example call:
     * ```
     * radius {
     *     bottomLeft { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun bottomLeft(value: ScaledValueProperty) = property(borderBottomLeftRadiusKey, Theme().radii, value, target)

    /**
     * This _convenience_ function enables the definition of the border radius for the _top left_ and _top right_
     * corners of a block at once via the
     * [border-top-left-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-top-left-radius) and
     * [border-top-right-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-top-right-radius) CSS properties.
     *
     * example call:
     * ```
     * radius {
     *     top { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun top(value: ScaledValueProperty) {
        topRight(value)
        topLeft(value)
    }

    /**
     * This _convenience_ function enables the definition of the border radius for the _bottom left_ and _bottom right_
     * corners of a block at once via the
     * [border-bottom-left-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-bottom-left-radius) and
     * [border-bottom-right-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-bottom-right-radius) CSS properties.
     *
     * example call:
     * ```
     * radius {
     *     bottom { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun bottom(value: ScaledValueProperty) {
        bottomLeft(value)
        bottomRight(value)
    }

    /**
     * This _convenience_ function enables the definition of the border radius for the _top right_ and _bottom right_
     * corners of a block at once via the
     * [border-top-right-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-top-right-radius) and
     * [border-bottom-right-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-bottom-right-radius)
     * CSS properties.
     *
     * example call:
     * ```
     * radius {
     *     right { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun right(value: ScaledValueProperty) {
        topRight(value)
        bottomRight(value)
    }

    /**
     * This _convenience_ function enables the definition of the border radius for the _top left_ and _bottom left_
     * corners of a block at once via the
     * [border-top-left-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-top-left-radius) and
     * [border-bottom-left-radius](https://developer.mozilla.org/en/docs/Web/CSS/border-bottom-left-radius)
     * CSS properties.
     *
     * example call:
     * ```
     * radius {
     *     left { small }
     * }
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun left(value: ScaledValueProperty) {
        topLeft(value)
        bottomLeft(value)
    }
}

/**
 * This _context_ interface offers functions to style the border related CSS properties of a component.
 *
 * It offers two functions to define the border shape itself ([border]) and the radius ([radius]) of the border.
 * Both functions come in two variants:
 * - one for the styling for all sides / corners at once
 * - another for the individual styling of each side / corner
 *
 * The actual styling of the borders is done within a specific _context_ that the [border] and [borders] functions bring
 * directly or via an intermediate context (have a look at [BordersContext]) into scope: [BorderContext].
 *
 * The styling for the radius is done by the [radius] function for all radii at once.
 * For styling each corner or pairs of corner you can use the [radii] function in order to bring a specific _context_
 * into the scope: [RadiiContext]
 *
 * Last but not least there exist a variant for all four previous functions that enables one to define the styling for
 * the different media devices independently.
 */
@ExperimentalCoroutinesApi
interface Border : StyleParams {

    /**
     * This function opens the context for defining the border related properties for all media devices.
     *
     * Example call:
     * ```
     * border {
     *     // some functions of [BorderContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @param value extension function parameter to bring the specialized topical functions of the [BorderContext]
     *              into the scope of the functional expression
     */
    fun border(value: BorderContext.() -> Unit) =
        BorderContext(borderWidthKey, borderStyleKey, borderColorKey, this, smProperties).value()

    /**
     * This function opens the context for defining the border related properties for each media device independently.
     *
     * Example call:
     * ```
     * border(
     *     sm = {
     *         // some functions of [BorderContext] with appropriate values for *small devices*
     *     },
     *     lg = {
     *         // some functions of [BorderContext] with appropriate values for *large devices*
     *     }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the specialized topical functions of the [BorderContext]
     *           into the scope of the functional expression
     * @param md extension function parameter to bring the specialized topical functions of the [BorderContext]
     *           into the scope of the functional expression
     * @param lg extension function parameter to bring the specialized topical functions of the [BorderContext]
     *           into the scope of the functional expression
     * @param xl extension function parameter to bring the specialized topical functions of the [BorderContext]
     *           into the scope of the functional expression
     */
    fun border(
        sm: (BorderContext.() -> Unit)? = null,
        md: (BorderContext.() -> Unit)? = null,
        lg: (BorderContext.() -> Unit)? = null,
        xl: (BorderContext.() -> Unit)? = null
    ) {
        if (sm != null) BorderContext(borderWidthKey, borderStyleKey, borderColorKey, this, smProperties).sm()
        if (md != null) BorderContext(borderWidthKey, borderStyleKey, borderColorKey, this, mdProperties).md()
        if (lg != null) BorderContext(borderWidthKey, borderStyleKey, borderColorKey, this, lgProperties).lg()
        if (xl != null) BorderContext(borderWidthKey, borderStyleKey, borderColorKey, this, xlProperties).xl()
    }

    /**
     * This function opens the _intermediate context_ for defining the border related properties independently for each
     * side for all media devices.
     *
     * Example call:
     * ```
     * borders {
     *     // some functions of [BordersContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @param value extension function parameter to bring the specialized side related functions of the [BordersContext]
     *              into the scope of the functional expression
     */
    fun borders(value: BordersContext.() -> Unit) = BordersContext(this, smProperties).value()

    /**
     * This function opens the _intermediate context_ for defining the border related properties independently for each
     * side and for each media device.
     *
     * Example call:
     * ```
     * borders(
     *     sm = {
     *         // some functions of [BordersContext] with appropriate values for *small devices*
     *     },
     *     lg = {
     *         // some functions of [BordersContext] with appropriate values for *large devices*
     *     }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the specialized side related functions of the [BordersContext]
     *           into the scope of the functional expression
     * @param md extension function parameter to bring the specialized side related functions of the [BordersContext]
     *           into the scope of the functional expression
     * @param lg extension function parameter to bring the specialized side related functions of the [BordersContext]
     *           into the scope of the functional expression
     * @param xl extension function parameter to bring the specialized side related functions of the [BordersContext]
     *           into the scope of the functional expression
     */
    fun borders(
        sm: (BordersContext.() -> Unit)? = null,
        md: (BordersContext.() -> Unit)? = null,
        lg: (BordersContext.() -> Unit)? = null,
        xl: (BordersContext.() -> Unit)? = null
    ) {
        if (sm != null) BordersContext(this, smProperties).sm()
        if (md != null) BordersContext(this, mdProperties).md()
        if (lg != null) BordersContext(this, lgProperties).lg()
        if (xl != null) BordersContext(this, xlProperties).xl()
    }

    /**
     * This function sets the radius property for all media devices.
     *
     * Example call:
     * ```
     * radius { small } // predefined values only for _circles_!
     * // radius { "1em 2em" } // use custom property for _ellipsis_!
     * ```
     *
     * @param value scale value, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun radius(value: ScaledValueProperty) =
        property(borderRadiusKey, Theme().radii, value, smProperties)

    /**
     * This function sets the radius property for each media device independently.
     *
     * Example call:
     * ```
     * radius(
     *     sm = { small }
     *     lg = { normal }
     * )
     * ```
     *
     * @param sm scale value for small media devices, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     * @param md scale value for medium sized media devices, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     * @param lg scale value for large media devices, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     * @param xl scale value for extra large media devices, recommended to use predefined values via [dev.fritz2.styling.theme.Theme.radii]
     */
    fun radius(
        sm: (ScaledValueProperty)? = null,
        md: (ScaledValueProperty)? = null,
        lg: (ScaledValueProperty)? = null,
        xl: (ScaledValueProperty)? = null
    ) {
        if (sm != null) property(borderRadiusKey, Theme().radii, sm, smProperties)
        if (md != null) property(borderRadiusKey, Theme().radii, md, mdProperties)
        if (lg != null) property(borderRadiusKey, Theme().radii, lg, lgProperties)
        if (xl != null) property(borderRadiusKey, Theme().radii, xl, xlProperties)
    }

    /**
     * This function opens the context for defining the radius related properties for each corner independently
     * for all media devices.
     *
     * Example call:
     * ```
     * radii {
     *     // some functions of [RadiiContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @param value extension function parameter to bring the specialized corner or side related functions
     *              of the [RadiiContext] into the scope of the functional expression
     */
    fun radii(value: RadiiContext.() -> Unit) = RadiiContext(this, smProperties).value()

    /**
     * This function opens the context for defining the radius related properties for each corner and for each
     * media device independently
     *
     * Example call:
     * ```
     * radii(
     *     sm = {
     *         // some functions of [RadiiContext] with appropriate values for *all devices*
     *     },
     *     lg = {
     *         // some functions of [RadiiContext] with appropriate values for *all devices*
     *     }
     * }
     * ```
     *
     * @param sm extension function parameter to bring the specialized corner or side related functions
     *           of the [RadiiContext] into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the specialized corner or side related functions
     *           of the [RadiiContext] into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the specialized corner or side related functions
     *           of the [RadiiContext] into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the specialized corner or side related functions
     *           of the [RadiiContext] into the scope of the functional expression for extra large media devices
     */
    fun radii(
        sm: (RadiiContext.() -> Unit)? = null,
        md: (RadiiContext.() -> Unit)? = null,
        lg: (RadiiContext.() -> Unit)? = null,
        xl: (RadiiContext.() -> Unit)? = null
    ) {
        if (sm != null) RadiiContext(this, smProperties).sm()
        if (md != null) RadiiContext(this, mdProperties).md()
        if (lg != null) RadiiContext(this, lgProperties).lg()
        if (xl != null) RadiiContext(this, xlProperties).xl()
    }

}