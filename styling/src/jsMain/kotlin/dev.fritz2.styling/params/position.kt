package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.ZIndices
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal const val positionKey = "position: "

/**
 * This _context class_ enables the definition of positioning properties for the four directions of _top_, _right_,
 * _bottom_ and _left_ and convenience functions for defining the _horizontal_ or _vertical_ aspects at once.
 *
 * This enables to define the position as follows:
 * ```
 * position { /* it == PositionContext.() */
 *     absolute { /* it == PositioningContext.() */
 *         top { large } // use predefined property of the theme
 *         right { "2em" } // use a custom value
 *     }
 * }
 * ```
 *
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@ExperimentalCoroutinesApi
class PositioningContext(
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This function is used to define the positional behaviour of an element by the
     * [top](https://developer.mozilla.org/en-US/docs/Web/CSS/top) property.
     *
     * example call:
     * ```
     * top { small } // use predefined properties of the theme
     * top { "2em" } // provide custom value
     * ```
     *
     * @param value positional value; recommended to use the predefined properties of the
     *              [theme][dev.fritz2.styling.theme.Theme.space]
     */
    fun top(value: ScaledValueProperty) = property("top: ", theme().space, value, target)

    /**
     * This function is used to define the positional behaviour of an element by the
     * [left](https://developer.mozilla.org/en-US/docs/Web/CSS/left) property.
     *
     * example call:
     * ```
     * left { small } // use predefined properties of the theme
     * left { "2em" } // provide custom value
     * ```
     *
     * @param value positional value; recommended to use the predefined properties of the
     *              [theme][dev.fritz2.styling.theme.Theme.space]
     */
    fun left(value: ScaledValueProperty) = property("left: ", theme().space, value, target)

    /**
     * This function is used to define the positional behaviour of an element by the
     * [bottom](https://developer.mozilla.org/en-US/docs/Web/CSS/bottom) property.
     *
     * example call:
     * ```
     * bottom { small } // use predefined properties of the theme
     * bottom { "2em" } // provide custom value
     * ```
     *
     * @param value positional value; recommended to use the predefined properties of the
     *              [theme][dev.fritz2.styling.theme.Theme.space]
     */
    fun bottom(value: ScaledValueProperty) = property("bottom: ", theme().space, value, target)

    /**
     * This function is used to define the positional behaviour of an element by the
     * [right](https://developer.mozilla.org/en-US/docs/Web/CSS/right) property.
     *
     * example call:
     * ```
     * right { small } // use predefined properties of the theme
     * right { "2em" } // provide custom value
     * ```
     *
     * @param value positional value; recommended to use the predefined properties of the
     *              [theme][dev.fritz2.styling.theme.Theme.space]
     */
    fun right(value: ScaledValueProperty) = property("right: ", theme().space, value, target)

    /**
     * This _convenience_ function is used to define the _vertical_ positional behaviour of an element by the
     * [top](https://developer.mozilla.org/en-US/docs/Web/CSS/top) and
     * [bottom](https://developer.mozilla.org/en-US/docs/Web/CSS/bottom) properties at once.
     *
     * example call:
     * ```
     * vertical { small } // use predefined properties of the theme
     * vertical { "2em" } // provide custom value
     * ```
     *
     * @param value positional value; recommended to use the predefined properties of the
     *              [theme][dev.fritz2.styling.theme.Theme.space]
     */
    fun vertical(value: ScaledValueProperty) {
        property("top: ", theme().space, value, target)
        property("bottom: ", theme().space, value, target)
    }

    /**
     * This _convenience_ function is used to define the _horizontal_ positional behaviour of an element by the
     * [right](https://developer.mozilla.org/en-US/docs/Web/CSS/right) and
     * [left](https://developer.mozilla.org/en-US/docs/Web/CSS/left) properties at once.
     *
     * example call:
     * ```
     * horizontal { small } // use predefined properties of the theme
     * horizontal { "2em" } // provide custom value
     * ```
     *
     * @param value positional value; recommended to use the predefined properties of the
     *              [theme][dev.fritz2.styling.theme.Theme.space]
     */
    fun horizontal(value: ScaledValueProperty) {
        property("left: ", theme().space, value, target)
        property("right: ", theme().space, value, target)
    }
}

/**
 * This _context class_ acts as an _intermediate context_ to set the position of an element and to specify the actual
 * positioning concerning the sides distances too.
 *
 * It offers primarily functions to define the type of the position, like [absolute] or [relative].
 * Further more those functions opens another _subcontext_ named [PositioningContext], which brings functions to
 * ultimately define the side distances.
 *
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@ExperimentalCoroutinesApi
class PositionContext(
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This property sets the ``static`` value for the [position](https://developer.mozilla.org/de/docs/Web/CSS/position)
     * property.
     *
     * Hint: As this value disables _positioning_, there is no need to open up the [PositioningContext]! That is why
     * a simple property fits here best.
     */
    val static = property(positionKey, "static", target)

    /**
     * This function sets the ``relative`` value for the
     * [position](https://developer.mozilla.org/de/docs/Web/CSS/position) property and opens up the [PositioningContext]
     * in order to enable the definition of side distances.
     *
     * @param value extension function parameter to open the [PositioningContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun relative(value: PositioningContext.() -> Unit) = positioning(value, "relative")

    /**
     * This function sets the ``absolute`` value for the
     * [position](https://developer.mozilla.org/de/docs/Web/CSS/position) property and opens up the [PositioningContext]
     * in order to enable the definition of side distances.
     *
     * @param value extension function parameter to open the [PositioningContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun absolute(value: PositioningContext.() -> Unit) = positioning(value, "absolute")

    /**
     * This function sets the ``sticky`` value for the
     * [position](https://developer.mozilla.org/de/docs/Web/CSS/position) property and opens up the [PositioningContext]
     * in order to enable the definition of side distances.
     *
     * @param value extension function parameter to open the [PositioningContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun sticky(value: PositioningContext.() -> Unit) = positioning(value, "sticky")

    /**
     * This function sets the ``fixed`` value for the
     * [position](https://developer.mozilla.org/de/docs/Web/CSS/position) property and opens up the [PositioningContext]
     * in order to enable the definition of side distances.
     *
     * @param value extension function parameter to open the [PositioningContext] as scope of the functional expression
     *              in order to use its specific styling functions.
     */
    fun fixed(value: PositioningContext.() -> Unit) = positioning(value, "fixed")

    private fun positioning(value: PositioningContext.() -> Unit, positionValue: Property) {
        val tempCss = StringBuilder()
        PositioningContext(this, tempCss).value()
        property(positionKey, "$positionValue$cssDelimiter$tempCss", target)
    }
}

/**
 * This _context_ interface offers two functions to specify the positional appearance of an element.
 *
 * - [zIndex] for arranging elements among the z-axis to bring them to the back or the front of the screen
 * - [position] for the positioning an element within its context
 *
 * There are two variants for each of those functions; one for applying to all media devices at once and another to
 * specify the functionality for each media device independently.
 */
@ExperimentalCoroutinesApi
interface Position : StyleParams {

    /**
     * This function sets the [z-index](https://developer.mozilla.org/de/docs/Web/CSS/z-index) CSS property of an element
     * for all media devices at once.
     *
     * Although you can provide a custom value for the index, it is recommended to use the predefined functions
     * (have a look at [dev.fritz2.styling.ZIndices]) and therefore _spans_ of the [theme][dev.fritz2.styling.Theme.zIndices].
     * Those are especially designed to define common groups of layers for clear purposes like one for a
     * [layer][dev.fritz2.styling.ZIndices.layer] or also [modal][dev.fritz2.styling.ZIndices.modal] dialog.
     * Those groups are designed to model an order in an ascending way.
     *
     * example call:
     * ```
     * zIndex { overlay } // use the fixed overlay layer of the theme (there is only one level of it)
     * zIndex { modal(2) } // use the z-index of the modal group of level "2"
     * zIndex { modal(nextLevel) } // inject a level-value passed by the outer component dynamically in order to bring
     *                             // the element to the front (assuming the outer context counts the consumed levels)
     * ```
     *
     * @param value extension function parameter in order to bring [dev.fritz2.styling.ZIndices] functions into scope.
     */
    fun zIndex(value: ZIndicesProperty) = property(ZIndices.key, theme().zIndices, value)

    /**
     * This function sets the [z-index](https://developer.mozilla.org/de/docs/Web/CSS/z-index) CSS property of an element
     * for each media device independently.
     *
     * For a detailed overview have a look at the variant of [zIndex] for all media devices!
     *
     * principle call example:
     * ```
     * zIndex({
     *     sm = { ... },
     *     lg = { ... }
     * })
     * ```
     *
     * @param sm extension function parameter in order to bring [dev.fritz2.styling.ZIndices] functions into scope
     *           for small media devices
     * @param md extension function parameter in order to bring [dev.fritz2.styling.ZIndices] functions into scope
     *           for medium sized media devices
     * @param lg extension function parameter in order to bring [dev.fritz2.styling.ZIndices] functions into scope
     *           for large media devices
     * @param xl extension function parameter in order to bring [dev.fritz2.styling.ZIndices] functions into scope
     *           for extra large media devices
     */
    fun zIndex(
        sm: ZIndicesProperty? = null,
        md: ZIndicesProperty? = null,
        lg: ZIndicesProperty? = null,
        xl: ZIndicesProperty? = null
    ) =
        property(ZIndices.key, theme().zIndices, sm, md, lg, xl)

    /**
     * This function sets the [position](https://developer.mozilla.org/de/docs/Web/CSS/position) CSS property of an element
     * for all media devices at once.
     *
     * the actual definition is done within a dedicated _context_ class [PositionContext].
     * This class offers functions to specify the position like [PositionContext.absolute] for example.
     *
     * example calls:
     * ```
     * position {
     *     absolute {
     *         // call functions from the [PositioningContext] for setting side distances
     *     }
     * }
     *
     * position { static }
     * ```
     *
     * @param value extension function parameter in order to bring [PositionContext] functions into scope.
     */
    fun position(value: PositionContext.() -> Unit) =
        property(positionKey, PositionContext(this, smProperties).value())

    /**
     * This function sets the [position](https://developer.mozilla.org/de/docs/Web/CSS/position) CSS property of an element
     * for each media device independently.
     *
     * the actual definition is done within a dedicated _context_ class [PositionContext].
     *
     * example call:
     * ```
     * position(
     *     sm = {
     *         absolute {
     *             // call functions from the [PositioningContext] for setting side distances
     *         }
     *     },
     *     lg = {
     *         relative {
     *              // call functions from the [PositioningContext] for setting side distances
     *         }
     *     }
     * )
     * ```
     *
     * @param sm extension function parameter in order to bring [PositionContext] functions into scope
     *           for small media devices.
     * @param md extension function parameter in order to bring [PositionContext] functions into scope
     *           for medium sized media devices.
     * @param lg extension function parameter in order to bring [PositionContext] functions into scope
     *           for large media devices.
     * @param xl extension function parameter in order to bring [PositionContext] functions into scope
     *           for extra large media devices.
     */
    fun position(
        sm: (PositionContext.() -> Unit)? = null,
        md: (PositionContext.() -> Unit)? = null,
        lg: (PositionContext.() -> Unit)? = null,
        xl: (PositionContext.() -> Unit)? = null
    ) {
        if (sm != null) property(positionKey, PositionContext(this, smProperties).sm())
        if (md != null) property(positionKey, PositionContext(this, mdProperties).md())
        if (lg != null) property(positionKey, PositionContext(this, lgProperties).lg())
        if (xl != null) property(positionKey, PositionContext(this, xlProperties).xl())
    }

}