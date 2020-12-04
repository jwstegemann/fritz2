package dev.fritz2.styling.theme

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.resetCss
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

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

@ExperimentalCoroutinesApi
interface Theme {
    companion object {
        private val currentTheme = MutableStateFlow<Theme>(DefaultTheme())

        /**
         * exposes the current [Theme] as a [Flow]
         */
        val data: Flow<Theme> = currentTheme

        /**
         * gets the current active [Theme]
         */
        operator fun invoke() = currentTheme.value

        /**
         * sets the current active theme
         *
         * @param theme [Theme] to activate
         */
        fun use(theme: Theme) {
            resetCss(theme.reset)
            currentTheme.value = theme
        }

        init {
            resetCss(currentTheme.value.reset)
        }
    }

    /**
     * css to reset browser's defaults and set your own
     */
    val reset: String

    /**
     * an human readable name like ``default`` or ``dark`` for example
     */
    val name: String

    /**
     * break points for different screen sizes that apply when working with [ResponsiveValue]s
     */
    val breakPoints: ResponsiveValue

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
    val space: ScaledValue

    /**
     * definition of the position-scale
     */
    val position: ScaledValue

    /**
     * definition of the font-size-scale
     */
    val fontSizes: ScaledValue

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
    val lineHeights: ScaledValue

    /**
     * definition of the scale for letter-spacings
     */
    val letterSpacings: ScaledValue

    /**
     * definition of the theme's sizes
     */
    val sizes: Sizes

    /**
     * definition of the scale for border-widths
     */
    val borderWidths: Thickness

    /**
     * definition of the scale for border-radii
     */
    val radii: ScaledValue

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
    val opacities: WeightedValue

    /**
     * definition of the scale for gaps
     */
    val gaps: ScaledValue

    /**
     * definition of theme-icons
     */
    val icons: Icons

    val input: InputFieldStyles

    val button: PushButtonStyles

    val radio: RadioStyles

    val checkbox: CheckboxStyles

    val switch: SwitchStyles

    val modal: ModalStyles

    /**
     * definition of the theme's popover
     */
    val popover: PopoverStyles

    val tooltip: Tooltip

    val textarea: TextAreaStyles

    val select : SelectStyles
}

/**
 * convenience function to create a render-context that provides a specialized theme correctly typed
 */
//TODO: add for Flow.render and each().render
@ExperimentalCoroutinesApi
inline fun <reified T : Theme> render(crossinline content: RenderContext.(T) -> Unit): List<Tag<HTMLElement>> =
    dev.fritz2.dom.html.render {
        content(Theme().unsafeCast<T>())
    }

inline fun <E : Element, reified T : Theme> renderElement(crossinline content: RenderContext.(T) -> Tag<E>) =
    dev.fritz2.dom.html.renderElement {
        content(Theme().unsafeCast<T>())
    }

@ExperimentalCoroutinesApi
inline fun <reified T : Theme> render(
    theme: T,
    crossinline content: RenderContext.(T) -> Unit
): List<Tag<HTMLElement>> {
    Theme.use(theme)
    return render { currentTheme: T ->
        div {
            Theme.data.render {
                content(currentTheme)
            }
        }
    }
}
