package dev.fritz2.styling.theme

import DefaultTheme
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.GridParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.w3c.dom.Element

/**
 * alias to easily create predefined styles based on [BasicParams]
 */
typealias PredefinedBasicStyle = BasicParams.() -> Unit

/**
 * alias to easily create predefined styles based on [FlexParams]
 */
typealias PredefinedFlexStyle = FlexParams.() -> Unit

/**
 * alias to easily create predefined styles based on [GridParams]
 */
typealias PredefinedGridStyle = GridParams.() -> Unit

/**
 * alias to easily create predefined styles based on [BoxParams]
 */
typealias PredefinedBoxStyle = BoxParams.() -> Unit

/**
 * alias for property values
 */
typealias Property = String

/**
 * Standard interface for themes in fritz2.
 *
 * This interface is implemented by fritz2's [DefaultTheme].
 * Of course you can use your own implementations or even extend this interface and add more specifications to it that you need for your UI.
 *
 * ```
 * interface ExtendedTheme : Theme {
 *     interface Columns {
 *         val landscapeMode: Int
 *         val portraitMode: Int
 *     }
 *
 *     val columns: Columns
 * }
 * ```
 *
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
    val gaps: ScaledValue<Property>

    /**
     * definition of theme-icons
     */
    val icons: Icons
}

/**
 * [StateFlow] that holds the current selected [Theme]
 */
@ExperimentalCoroutinesApi
var currentTheme: Theme = DefaultTheme()

/**
 * get the currently selected [Theme]
 */
@ExperimentalCoroutinesApi
fun theme(): Theme = currentTheme

/**
 * get the currently selected [Theme] correctly casted
 */
@ExperimentalCoroutinesApi
inline fun <reified T : Theme> theme(): Theme = currentTheme.unsafeCast<T>()

/**
 * convenience function to create a render-context that provides a specialized theme correctly typed
 */
//TODO: add for Flow.render and each().render
@ExperimentalCoroutinesApi
inline fun <E : Element, reified T : Theme> render(crossinline content: HtmlElements.(T) -> Tag<E>): Tag<E> =
    dev.fritz2.dom.html.render {
        content(currentTheme.unsafeCast<T>())
    }

