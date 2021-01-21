package dev.fritz2.styling.theme

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.MountTargetNotFoundException
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.render
import dev.fritz2.styling.resetCss
import kotlinx.browser.document
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.w3c.dom.HTMLElement

/**
 * alias for property values
 */
typealias Property = String

/**
 * adds important flag to resulting css
 */
val Property.important: Property
    get() = "$this !important"

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

    val select : SelectFieldStyles
}

/**
 * Creates a [RenderContext] for [Tag]s and mounts it to a constant element in the static html file
 * which id matches the [selector]. It also applies the given [Theme]
 *
 * @see render
 *
 * @param selector [query selector](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector)
 * of the element to mount to
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if target element with [selector] not found
 */
inline fun <reified T : Theme> render(
    theme: T,
    selector: String,
    override: Boolean = true,
    crossinline content: RenderContext.(T) -> Unit
) {
    Theme.use(theme)
    render(selector, override) {
        Theme.data.render {
            content(Theme().unsafeCast<T>())
        }
    }
}

/**
 * Creates a render context for [Tag]s and mounts it to an [HTMLElement]. It also applies the given [Theme].
 *
 * @see render
 *
 * @param theme [Theme] used in this [RenderContext]
 * @param targetElement [HTMLElement] to mount to, default is *document.body*
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if [targetElement] not found
 */
inline fun <reified T : Theme> render(
    theme: T,
    targetElement: HTMLElement? = document.body,
    override: Boolean = true,
    crossinline content: RenderContext.(T) -> Unit
) {
    Theme.use(theme)
    render(targetElement, override) {
        Theme.data.render {
            content(Theme().unsafeCast<T>())
        }
    }
}

//TODO: add for Flow.render and Flow.renderEach
