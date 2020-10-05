package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Alias for the specific [DirectionValues] properties.
 */
typealias DirectionProperty = Property

/**
 * Predefined values for the [flex-direction](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-direction)
 * property. Should be used as expression result in [Flexbox.direction] functions.
 */
object DirectionValues : PropertyValues {
    override val key = "flex-direction: "

    const val row: DirectionProperty = "row"
    const val column: DirectionProperty = "column"
    const val rowReverse: DirectionProperty = "row-reverse"
    const val columnReverse: DirectionProperty = "column-reverse"
}

/**
 * Alias for the specific [WrapValues] properties.
 */
typealias WrapProperty = Property

/**
 * Predefined values for the [flex-wrap](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-wrap)
 * property. Should be used as expression result in [Flexbox.wrap] functions.
 */
object WrapValues : PropertyValues {
    override val key = "flex-wrap: "

    const val wrap: WrapProperty = "wrap"
    const val nowrap: WrapProperty = "nowrap"
    const val wrapReverse: WrapProperty = "wrap-reverse"
}

/**
 * This _context_ interface offers functions to **style**
 * [flexbox](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Flexbox)-layouts.
 *
 * It does **not** create a flexbox layout though!
 *
 * This is done by a special [dev.fritz2.components.flex] fabric function that creates a component
 * with the [display](https://developer.mozilla.org/en-US/docs/Web/CSS/display) property already set to ``flex``.
 *
 * So it is recommended to use the provided functions within the styles parameter of [dev.fritz2.styling.components.flex].
 *
 * This interface offers all the inherited functions of [Alignment] that corresponds to the
 * [CSS alignment module](https://www.w3.org/TR/css-align-3/).
 *
 * On top it offers the [direction] function in order to set the direction of the layout flow.
 * There is an overloaded variant that enables one to define the flow for each media device independently.
 *
 * There is also the [wrap] function that sets the wrapping behaviour of a flexbox layout.
 * There exist an overloaded variant for this function too, that enables to define the behaviour for all media devices
 * independently.
 *
 * You can define a layout like this for example:
 * ```
 * flex ({ /* it == FlexStyleParams (sum type of different StyleParams based interfaces including this one) */
 *     direction { row }
 *     wrap { nowrap }
 *     // inherited from [Alignment]
 *     justifyContent { center }
 * }) {
 *     // child content of the flex container
 * }
 * ```
 *
 * @see Alignment
 */
@ExperimentalCoroutinesApi
interface Flexbox : StyleParams, Alignment {

    /**
     * This function sets the [flex-direction](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-direction) property
     * for all media devices.
     *
     * Example call:
     * ```
     * direction { row }
     * ```
     *
     * @param value extension function parameter to bring the predefined properties of [DirectionValues]
     *              into the scope of the functional expression
     */
    fun direction(value: DirectionValues.() -> DirectionProperty) =
        property(DirectionValues.key, DirectionValues.value(), smProperties)

    /**
     * This function sets the [flex-direction](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-direction) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * direction(
     *     sm = { column }, // small devices apply better to vertical layouts!
     *     lg = { row }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the predefined properties of [DirectionValues]
     *           into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the predefined properties of [DirectionValues]
     *           into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined properties of [DirectionValues]
     *           into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined properties of [DirectionValues]
     *           into the scope of the functional expression for extra large media devices
     */
    fun direction(
        sm: (DirectionValues.() -> DirectionProperty)? = null,
        md: (DirectionValues.() -> DirectionProperty)? = null,
        lg: (DirectionValues.() -> DirectionProperty)? = null,
        xl: (DirectionValues.() -> DirectionProperty)? = null
    ) {
        if (sm != null) property(DirectionValues.key, DirectionValues.sm(), smProperties)
        if (md != null) property(DirectionValues.key, DirectionValues.md(), mdProperties)
        if (lg != null) property(DirectionValues.key, DirectionValues.lg(), lgProperties)
        if (xl != null) property(DirectionValues.key, DirectionValues.xl(), xlProperties)
    }

    /**
     * This function sets the [flex-wrap](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-wrap) property
     * for all media devices.
     *
     * Example call:
     * ```
     * wrap { nowrap }
     * ```
     *
     * @param value extension function parameter to bring the predefined properties of [WrapValues]
     *              into the scope of the functional expression
     */
    fun wrap(value: WrapValues.() -> WrapProperty) =
        property(WrapValues.key, WrapValues.value(), smProperties)

    /**
     * This function sets the [flex-wrap](https://developer.mozilla.org/en-US/docs/Web/CSS/flex-wrap) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * wrap(
     *     sm = { wrap }
     *     lg = { nowrap }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the predefined properties of [WrapValues]
     *           into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the predefined properties of [WrapValues]
     *           into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the predefined properties of [WrapValues]
     *           into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the predefined properties of [WrapValues]
     *           into the scope of the functional expression for extra large media devices
     */
    fun wrap(
        sm: (WrapValues.() -> WrapProperty)? = null,
        md: (WrapValues.() -> WrapProperty)? = null,
        lg: (WrapValues.() -> WrapProperty)? = null,
        xl: (WrapValues.() -> WrapProperty)? = null
    ) {
        if (sm != null) property(WrapValues.key, WrapValues.sm(), smProperties)
        if (md != null) property(WrapValues.key, WrapValues.md(), mdProperties)
        if (lg != null) property(WrapValues.key, WrapValues.lg(), lgProperties)
        if (xl != null) property(WrapValues.key, WrapValues.xl(), xlProperties)
    }

}