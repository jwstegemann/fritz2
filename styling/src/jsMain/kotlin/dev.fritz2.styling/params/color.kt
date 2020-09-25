package dev.fritz2.styling.params

import dev.fritz2.styling.Colors
import dev.fritz2.styling.Property
import dev.fritz2.styling.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal const val colorKey = "color: "
internal const val opacityKey = "opacity: "

/**
 * This _context_ interface offers functions to style the color related CSS properties of a component.
 *
 * It only offers two functions
 * - [color] for setting the color, and
 * - [opacity] for setting the opacity of a component.
 *
 * Both functions have two variants, one for setting the property for all media devices at once and another for
 * setting the properties for each media device independently.
 */
@ExperimentalCoroutinesApi
interface Color : StyleParams {

    /**
     * This function sets the [color](https://developer.mozilla.org/de/docs/Web/CSS/color) property
     * for all media devices.
     *
     * Example call:
     * ```
     * color { primary } // use the predefined values from the theme (by [dev.fritz2.styling.Theme.colors])
     * // color { "lime" } // we don't provide common CSS colors at the moment, you must provide them individually
     * // color { rgba(255, 0, 0, 100) }
     * ```
     *
     * @param value extension function parameter with color type return value,
     *              recommended to use predefined values via [dev.fritz2.styling.Theme.colors] that offer the
     *              properties of [Colors]
     */
    fun color(value: Colors.() -> Property) = property(colorKey, theme().colors, value)

    /**
     * This function sets the [color](https://developer.mozilla.org/de/docs/Web/CSS/color) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * color(
     *     sm = { primary }
     *     lg = { dark }
     * )
     * ```
     *
     * @param sm extension function parameter with color type return value for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.Theme.colors] that offer the properties of [Colors]
     * @param md extension function parameter with color type return value for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.Theme.colors] that offer the properties of [Colors]
     * @param lg extension function parameter with color type return value for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.Theme.colors] that offer the properties of [Colors]
     * @param xl extension function parameter with color type return value for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.Theme.colors] that offer the properties of [Colors]
     */
    fun color(
        sm: (Colors.() -> Property)? = null,
        md: (Colors.() -> Property)? = null,
        lg: (Colors.() -> Property)? = null,
        xl: (Colors.() -> Property)? = null
    ) =
        property(colorKey, theme().colors, sm, md, lg, xl)


    /**
     * This function sets the [opacity](https://developer.mozilla.org/de/docs/Web/CSS/opacity) property
     * for all media devices.
     *
     * Example call:
     * ```
     * opacity { normal } // prefer the predefined values from the theme (by [dev.fritz2.styling.Theme.opacities])
     * // opacity { "0.1" }
     * ```
     *
     * @param value provide a value of type [WeightedValueProperty] that defines the opacity,
     *              recommended to use predefined values via [dev.fritz2.styling.Theme.opacities]
     */
    fun opacity(value: WeightedValueProperty) = property(opacityKey, theme().opacities, value)

    /**
     * This function sets the [opacity](https://developer.mozilla.org/de/docs/Web/CSS/opacity) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * opacity(
     *     sm = { normal }
     *     lg = { "0.8" }
     * )
     * ```
     *
     * @param sm extension function parameter with a [WeightedValueProperty] type return value for small media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.Theme.opacities]
     * @param md extension function parameter with a [WeightedValueProperty] type return value for medium sized media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.Theme.opacities]
     * @param lg extension function parameter with a [WeightedValueProperty] type return value for large media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.Theme.opacities]
     * @param xl extension function parameter with a [WeightedValueProperty] type return value for extra large media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.Theme.opacities]
     */
    fun opacity(
        sm: WeightedValueProperty? = null,
        md: WeightedValueProperty? = null,
        lg: WeightedValueProperty? = null,
        xl: WeightedValueProperty? = null
    ) =
        property(opacityKey, theme().opacities, sm, md, lg, xl)

}