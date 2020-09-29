package dev.fritz2.styling.theme

import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.ShadowProperty

/**
 * Defines a responsive [Property] that can have different values for different screen sizes.
 * Per default the value for a certain screen size is the same as the value for the next smaller screen size.
 * You can define the concrete screen sizes that apply in the [Theme] you use.
 *
 * @param sm value for small screens like phones (and default for all the others)
 * @param md value for middle screens like tablets  (and default for all the others)
 * @param lg value for large screens (and default for all the others)
 * @param xl value for extra large screens (and default for all the others)
 */
class ResponsiveValue<T : Property>(val sm: T, val md: T = sm, val lg: T = md, val xl: T = lg)

/**
 * Defines a value that has different expressions for different scales.
 */
open class ScaledValue<T : Property>(
    val normal: T,
    val small: T = normal,
    val smaller: T = small,
    val tiny: T = smaller,
    val large: T = normal,
    val larger: T = large,
    val huge: T = larger,
    open val none: T = tiny,
    val full: T = huge
) {
    val initial: T = "initial".unsafeCast<T>()
    val inherit: T = "inherit".unsafeCast<T>()
    val auto: T = "auto".unsafeCast<T>()
}

/**
 * Defines a value that has different expressions for different weights.
 */
class WeightedValue<T : Property>(
    val normal: T,
    val lighter: T = normal,
    val light: T = lighter,
    val stronger: T = normal,
    val strong: T = stronger,
    val none: T = light,
    val full: T = strong
) {
    val initial: T = "initial".unsafeCast<T>()
    val inherit: T = "inherit".unsafeCast<T>()
}

/**
 * Defines a value that has different expressions for different thicknesses.
 */
class Thickness<T : Property>(
    val normal: T,
    val thin: T = normal,
    val fat: T = normal,
    val hair: T = thin,
) {
    val initial: T = "initial".unsafeCast<T>()
    val inherit: T = "inherit".unsafeCast<T>()
}

class Sizes(
    normal: Property,
    small: Property = normal,
    smaller: Property = small,
    tiny: Property = smaller,
    large: Property = normal,
    larger: Property = large,
    huge: Property = larger,
    full: Property = large
) : ScaledValue<Property>(normal, small, smaller, tiny, large, larger, huge, full = full) {
    val borderBox: Property = "border-box"
    val contentBox: Property = "content-box"
    val maxContent: Property = "max-content"
    val minContent: Property = "min-content"
    val available: Property = "available"
    val unset: Property = "unset"

    fun fitContent(value: Property): Property = "fit-content($value)"
}

class ZIndices(
    private val baseValue: Int, private val layer: Int, private val layerStep: Int, private val overlayValue: Int,
    private val toast: Int, private val toastStep: Int, private val modal: Int, private val modalStep: Int
) {

    companion object {
        const val key: Property = "z-index: "
    }

    val base: Property = "$baseValue"
    val overlay: Property = "$overlayValue"
    fun layer(value: Int): Property = zIndexFrom(layer, layerStep, value)
    fun toast(value: Int): Property = zIndexFrom(toast, toastStep, value)
    fun modal(value: Int): Property = zIndexFrom(modal, modalStep, value)

    private fun zIndexFrom(level: Int, step: Int, value: Int) = "${level + step * (value - 1)}"
}

interface Fonts {
    val body: Property
    val heading: Property
    val mono: Property
}

interface Colors {
    val primary: ColorProperty
    val secondary: ColorProperty
    val tertiary: ColorProperty
    val success: ColorProperty
    val danger: ColorProperty
    val warning: ColorProperty
    val info: ColorProperty
    val light: ColorProperty
    val dark: ColorProperty
    val disabled: ColorProperty
}

class Shadows(
    val flat: ShadowProperty,
    val raised: ShadowProperty,
    val raisedFurther: ShadowProperty = raised,
    val top: ShadowProperty = raisedFurther,
    val lowered: ShadowProperty,
    val bottom: ShadowProperty = lowered,
    val glowing: ShadowProperty,
)
