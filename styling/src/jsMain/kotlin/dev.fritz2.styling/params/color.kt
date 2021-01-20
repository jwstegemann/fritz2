package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal const val colorKey = "color: "
internal const val opacityKey = "opacity: "

/**
 * alias for colors
 */
typealias ColorProperty = Property

/**
 * creates a [ColorProperty] from rgb-values
 */
fun rgb(r: Int, g: Int, b: Int) = "rgb($r,$g,$b)"

/**
 * creates a [ColorProperty] from rgba-values
 */
fun rgba(r: Int, g: Int, b: Int, a: Double) = "rgb($r,$g,$b,$a)"

/**
 * creates a [ColorProperty] from hsl-values
 */
fun hsl(h: Int, s: Int, l: Int) = "hsl($h,$s%,$l%)"

/**
 * creates a [ColorProperty] from hsla-values
 */
fun hsla(h: Int, s: Int, l: Int, a: Double) = "hsl($h,$s% c vn,,$l%,$a)"

/**
 * alters the brightness of a given input color in the hex format.
 * Enter a value between 1 and 2 to increase brightness, and a value between 0 and 1 to decrease brightness.
 * Increasing the brightness of a color lets them appear rather faded than shining.
 */

fun alterHexColorBrightness(color: ColorProperty, brightness: Double): ColorProperty =
        if (color.length != 7 || color[0] != '#') {
            console.log("alterHexColorBrightness: Wrong color input format (expected #rrggbb).")
            color
        } else {
            val rgb = color.asSequence()
                .drop(1)
                .chunked(2)
                .map { it.joinToString("").toInt(16) }
                .map {
                    when {
                        brightness > 1 -> {
                            it + (brightness - 1) * (255 - it)
                        }
                        brightness < 1 -> {
                            it - ((1 - brightness) * it)
                        }
                        else -> {
                            it
                        }
                    }
                }.map {
                    minOf(it.toInt(), 255)
                }.map {
                    it.toString(16).let { str -> if (str.length == 2) str else "0$str" }
                }.joinToString("")
            "#$rgb"
        }


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
     * This function sets the [color](https://developer.mozilla.org/en/docs/Web/CSS/color) property
     * for all media devices.
     *
     * Example call:
     * ```
     * color { primary } // use the predefined values from the theme (by [dev.fritz2.styling.theme.Theme.colors])
     * // color { "lime" } // we don't provide common CSS colors at the moment, you must provide them individually
     * // color { rgba(255, 0, 0, 100) }
     * ```
     *
     * @param value extension function parameter with color type return value,
     *              recommended to use predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the
     *              properties of [Colors]
     */
    fun color(value: Colors.() -> ColorProperty) = property(colorKey, Theme().colors, value)

    /**
     * This function sets the [color](https://developer.mozilla.org/en/docs/Web/CSS/color) property
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
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     * @param md extension function parameter with color type return value for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     * @param lg extension function parameter with color type return value for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     * @param xl extension function parameter with color type return value for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.colors] that offer the properties of [Colors]
     */
    fun color(
        sm: (Colors.() -> ColorProperty)? = null,
        md: (Colors.() -> ColorProperty)? = null,
        lg: (Colors.() -> ColorProperty)? = null,
        xl: (Colors.() -> ColorProperty)? = null
    ) =
        property(colorKey, Theme().colors, sm, md, lg, xl)


    /**
     * This function sets the [opacity](https://developer.mozilla.org/en/docs/Web/CSS/opacity) property
     * for all media devices.
     *
     * Example call:
     * ```
     * opacity { normal } // prefer the predefined values from the theme (by [dev.fritz2.styling.theme.Theme.opacities])
     * // opacity { "0.1" }
     * ```
     *
     * @param value provide a value of type [WeightedValueProperty] that defines the opacity,
     *              recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     */
    fun opacity(value: WeightedValueProperty) = property(opacityKey, Theme().opacities, value)

    /**
     * This function sets the [opacity](https://developer.mozilla.org/en/docs/Web/CSS/opacity) property
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
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     * @param md extension function parameter with a [WeightedValueProperty] type return value for medium sized media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     * @param lg extension function parameter with a [WeightedValueProperty] type return value for large media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     * @param xl extension function parameter with a [WeightedValueProperty] type return value for extra large media devices,
     *           recommended to use predefined values via [dev.fritz2.styling.theme.Theme.opacities]
     */
    fun opacity(
        sm: WeightedValueProperty? = null,
        md: WeightedValueProperty? = null,
        lg: WeightedValueProperty? = null,
        xl: WeightedValueProperty? = null
    ) =
        property(opacityKey, Theme().opacities, sm, md, lg, xl)
}