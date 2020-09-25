package dev.fritz2.styling

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.BoxStyleParams
import dev.fritz2.styling.params.FlexStyleParams
import dev.fritz2.styling.params.GridStyleParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.w3c.dom.Element

/**
 * alias to easily create predefined styles based on [BasicStyleParams]
 */
typealias PredefinedBasicStyle = BasicStyleParams.() -> Unit

/**
 * alias to easily create predefined styles based on [FlexStyleParams]
 */
typealias PredefinedFlexStyle = FlexStyleParams.() -> Unit

/**
 * alias to easily create predefined styles based on [GridStyleParams]
 */
typealias PredefinedGridStyle = GridStyleParams.() -> Unit

/**
 * alias to easily create predefined styles based on [BoxStyleParams]
 */
typealias PredefinedBoxStyle = BoxStyleParams.() -> Unit

/**
 * alias for property values
 */
typealias Property = String

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
    val primary: Property
    val secondary: Property
    val tertiary: Property
    val success: Property
    val danger: Property
    val warning: Property
    val info: Property
    val light: Property
    val dark: Property
    val disabled: Property
}

typealias ShadowProperty = Property

fun shadow(
    offsetHorizontal: String,
    offsetVertical: String = offsetHorizontal,
    blur: String? = null,
    spread: String? = null,
    color: String? = null,
    inset: Boolean = false
): ShadowProperty = buildString {
    append(offsetHorizontal, " ", offsetVertical)
    if (blur != null) append(" ", blur)
    if (spread != null) append(" ", spread)
    if (color != null) append(" ", color)
    if (inset) append(" inset")
}

infix fun ShadowProperty.and(other: ShadowProperty): ShadowProperty = "$this, $other"

class Shadows(
    val flat: ShadowProperty,
    val raised: ShadowProperty,
    val raisedFurther: ShadowProperty = raised,
    val top: ShadowProperty = raisedFurther,
    val lowered: ShadowProperty,
    val bottom: ShadowProperty = lowered,
    val glowing: ShadowProperty,
)

//FIXME: move to package theme
/**
 * Standard interface for themes in fritz2
 */
interface Theme {
    /**
     * break points for different screen sizes that apply when working with [ResponsiveValue]s
     */
    val breakPoints: ResponsiveValue<Property>

    /**
     * the media query used for middle sized screens
     */
    val mediaQueryMd: String

    /**
     * the media query used for large screens
     */
    val mediaQueryLg: String

    /**
     * the media query used for extra-large screens
     */
    val mediaQueryXl: String

    /**
     * definition of the space-scale
     */
    val space: ScaledValue<Property>

    /**
     * definition of the position-scale
     */
    val position: ScaledValue<Property>

    /**
     * definition of the font-size-scale
     */
    val fontSizes: ScaledValue<Property>

    /**
     * definition of the theme's colors
     */
    val colors: Colors

    /**
     * definition of the theme's fonts
     */
    val fonts: Fonts

    /**
     * definition of the scale for line-heights
     */
    val lineHeights: ScaledValue<Property>

    /**
     * definition of the scale for letter-spacings
     */
    val letterSpacings: ScaledValue<Property>

    /**
     * definition of the theme's sizes
     */
    val sizes: Sizes

    /**
     * definition of the scale for border-widths
     */
    val borderWidths: Thickness<Property>

    /**
     * definition of the scale for border-radii
     */
    val radii: ScaledValue<Property>

    /**
     * definition of the theme's shadows
     */
    val shadows: Shadows

    /**
     * definition of the theme's z-indices
     */
    val zIndices: ZIndices

    /**
     * definition of the scale for opacities
     */
    val opacities: WeightedValue<Property>

    /**
     * definition of the scale for gaps
     */
    //FIXME: rename to gaps?
    val gridGap: ScaledValue<Property>
}

//FIXME: move to example
interface ExtendedTheme : Theme {
    interface MyProp {
        val a: Property
        val b: Property
    }

    val test: MyProp

    val teaserText: PredefinedBasicStyle
}

//FIXME: move to own file
//FIXME: Inherit just from theme
/**
 * defines the default values and scales for fritz2
 */
open class DefaultTheme : ExtendedTheme {
    final override val breakPoints = ResponsiveValue("30em", "48em", "62em", "80em")

    override val mediaQueryMd: String = "@media screen and (min-width: ${breakPoints.md})"
    override val mediaQueryLg: String = "@media screen and (min-width: ${breakPoints.lg})"
    override val mediaQueryXl: String = "@media screen and (min-width: ${breakPoints.xl})"

    override val space = ScaledValue(
        none = "0",
        tiny = "0.25rem",
        smaller = "0.5rem",
        small = "0.75rem",
        normal = "1rem",
        large = "1.25rem",
        larger = "1.5rem",
        huge = "2rem",
        full = "2.5rem"
    )

    override val position = space

    override val gridGap = space

    override val fontSizes = ScaledValue(
        smaller = "0.75rem",
        small = "0.875rem",
        normal = "1rem",
        large = "1.125rem",
        larger = "1.5rem",
        huge = "2.25rem"
    )

    override val colors = object : Colors {
        override val primary = "#007bff"
        override val secondary = "#6c757d"
        override val tertiary = "#6c757d"
        override val success = "#28a745"
        override val danger = "#dc3545"
        override val warning = "#ffc107"
        override val info = "#17a2b8"
        override val light = "#f8f9fa"
        override val dark = "#343a40"
        override val disabled = "#6c757d"
    }

    override val fonts = object : Fonts {
        override val body =
            """-apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol" """
        override val heading =
            """-apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol" """
        override val mono = """SFMono-Regular,Menlo,Monaco,Consolas,"Liberation Mono","Courier New",monospace"""
    }

    override val lineHeights = ScaledValue(
        normal = "normal",
        tiny = "1",
        small = "1.25",
        smaller = "1.375",
        large = "1.5",
        larger = "1.625",
        huge = "2"
    )

    override val letterSpacings = ScaledValue(
        smaller = "-0.05em",
        small = "-0.025em",
        normal = "0",
        large = "0.025em",
        larger = "0.05em",
        huge = "0.1em"
    )

    override val sizes = Sizes(
        normal = "auto",
        small = "25rem",
        smaller = "15rem",
        tiny = "10rem",
        large = "50rem",
        larger = "75rem",
        huge = "100rem",
        full = "100%"
    )

    override val borderWidths = Thickness<Property>(
        normal = "2px",
        thin = "1px",
        fat = "4px",
        hair = "0.1px"
    )

    override val radii = ScaledValue(
        small = "0.125rem",
        normal = "0.25rem",
        large = "0.5rem",
        full = "9999px"
    )

    override val test = object : ExtendedTheme.MyProp {
        override val a: Property = space.normal
        override val b: Property = "b"
    }

    override val shadows = Shadows(
        flat = shadow("0", "1px", "3px", color = rgba(0, 0, 0, 0.12))
                and shadow("0", "1px", "2px", rgba(0, 0, 0, 0.24)),
        raised = shadow("0", "14px", "28px", rgba(0, 0, 0, 0.25))
                and shadow(" 0", "10px", "10px", rgba(0, 0, 0, 0.22)),
        raisedFurther = shadow("0", "14px", "28px", rgba(0, 0, 0, 0.25))
                and shadow("0", "10px", "10px", rgba(0, 0, 0, 0.22)),
        top = shadow("0", "19px", "38px", rgba(0, 0, 0, 0.30))
                and shadow("0", "15px", "12px", rgba(0, 0, 0, 0.22)),
        lowered = shadow("0", "2px", "4px", color = rgba(0, 0, 0, 0.06), inset = true),
        glowing = shadow("0", "0", "2px", color = rgba(0, 0, 255, 0.5))
    )

    override val zIndices = ZIndices(1, 100, 2, 200, 300, 2, 400, 2)

    override val opacities = WeightedValue(
        normal = "0.5"
    )

    override val teaserText: PredefinedBasicStyle = {
        fontWeight { semiBold }
        textTransform { uppercase }
        fontSize { smaller }
        letterSpacing { large }
        textShadow { glowing }
        color { info }
    }


}

//FIXME: remove
class Default2 : DefaultTheme() {
    override val fontSizes = ScaledValue<Property>(
        smaller = "1.125rem",
        small = "1.25rem",
        normal = "1.5rem",
        large = "1.875rem",
        larger = "2.25rem",
        huge = "3rem"
    )
}


/**
 * [StateFlow] that holds the current selected [Theme]
 */
@ExperimentalCoroutinesApi
val currentTheme = MutableStateFlow<Theme>(DefaultTheme())

/**
 * get the currently selected [Theme]
 */
@ExperimentalCoroutinesApi
fun theme(): Theme = currentTheme.value

/**
 * get the currently selected [Theme] correctly casted
 */
@ExperimentalCoroutinesApi
fun <T : Theme> theme(): Theme = currentTheme.value.unsafeCast<T>()

/**
 * convenience function to create a render-context that provides a specialized theme correctly typed
 */
//TODO: add for Flow.render and each().render
@ExperimentalCoroutinesApi
inline fun <E : Element, T : Theme> render(crossinline content: HtmlElements.(T) -> Tag<E>): Tag<E> =
    dev.fritz2.dom.html.render {
        content(currentTheme.value.unsafeCast<T>())
    }
