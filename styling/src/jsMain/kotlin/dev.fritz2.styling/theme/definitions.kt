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
    val giant: T = huge,
    open val none: T = tiny,
    val full: T = giant
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
    val light: T = normal,
    val lighter: T = light,
    val strong: T = normal,
    val stronger: T = strong,
    val none: T = lighter,
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

/**
 * Defines a value that has different expressions for different sizes.
 */
class Sizes(
    normal: Property,
    small: Property = normal,
    smaller: Property = small,
    tiny: Property = smaller,
    large: Property = normal,
    larger: Property = large,
    huge: Property = larger,
    giant: Property = huge,
    full: Property = giant
) : ScaledValue<Property>(normal, small, smaller, tiny, large, larger, huge, giant, full = full) {
    val borderBox: Property = "border-box"
    val contentBox: Property = "content-box"
    val maxContent: Property = "max-content"
    val minContent: Property = "min-content"
    val available: Property = "available"
    val unset: Property = "unset"

    fun fitContent(value: Property): Property = "fit-content($value)"
}

/**
 * Defines the scheme for zIndices in fritz2
 *
 * @property baseValue z-index for normal content ("bottom")
 * @property layer start z-index for layers
 * @property layerStep step to add for each new layer
 * @property overlayValue z-index for an overlay
 * @property toast start z-index for toasts
 * @property toastStep step to add for each new toast
 * @property modal start z-index for modals
 * @property modalStep step to add for each new modal
 */
class ZIndices(
    private val baseValue: Int, private val layer: Int, private val layerStep: Int, private val overlayValue: Int,
    private val toast: Int, private val toastStep: Int, private val modal: Int, private val modalStep: Int
) {

    companion object {
        /**
         * key to set z-index-property
         */
        const val key: Property = "z-index: "
    }

    /**
     * [Property] for base z-index
     */
    val base: Property = "$baseValue"

    /**
     * [Property] for overlay z-index
     */
    val overlay: Property = "$overlayValue"

    /**
     * creates [Property] for a specific layer z-index
     *
     * Use self defined constants for the different layers of your UI.
     *
     * @param value number of layer the z-index should be calculated for
     */
    fun layer(value: Int): Property = zIndexFrom(layer, layerStep, value)

    /**
     * creates [Property] for a specific toast z-index
     *
     * @param value number of toast the z-index should be calculated for
     */
    fun toast(value: Int): Property = zIndexFrom(toast, toastStep, value)

    /**
     * creates [Property] for a specific modals z-index
     *
     * @param value number of modal the z-index should be calculated for
     */
    fun modal(value: Int): Property = zIndexFrom(modal, modalStep, value)

    private fun zIndexFrom(level: Int, step: Int, value: Int) = "${level + step * (value - 1)}"
}

/**
 * Defines the scheme fonts in a theme
 *
 */
interface Fonts {
    val body: Property
    val heading: Property
    val mono: Property
}

/**
 * Defines the scheme colors in a theme
 *
 */
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

/**
 * Defines the scheme shadows in a theme
 *
 */
class Shadows(
    val flat: ShadowProperty,
    val raised: ShadowProperty,
    val raisedFurther: ShadowProperty = raised,
    val top: ShadowProperty = raisedFurther,
    val lowered: ShadowProperty,
    val bottom: ShadowProperty = lowered,
    val glowing: ShadowProperty,
)
