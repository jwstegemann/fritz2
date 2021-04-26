package dev.fritz2.styling.theme

import dev.fritz2.styling.params.*

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
class ResponsiveValue(val sm: Property, val md: Property = sm, val lg: Property = md, val xl: Property = lg)

/**
 * Defines a value that has different expressions for different scales.
 */
open class ScaledValue(
    val normal: Property,
    val small: Property = normal,
    val smaller: Property = small,
    val tiny: Property = smaller,
    val large: Property = normal,
    val larger: Property = large,
    val huge: Property = larger,
    val giant: Property = huge,
    open val none: Property = tiny,
    val full: Property = giant,
) {
    val initial: Property = "initial"
    val inherit: Property = "inherit"
    val auto: Property = "auto"
}

/**
 * Defines a value that has different expressions for different weights.
 */
class WeightedValue(
    val normal: Property,
    val light: Property = normal,
    val lighter: Property = light,
    val strong: Property = normal,
    val stronger: Property = strong,
    val none: Property = lighter,
    val full: Property = strong,
) {
    val initial: Property = "initial"
    val inherit: Property = "inherit"
}

/**
 * Defines a value that has different expressions for different thicknesses.
 */
class Thickness(
    val none: Property,
    val normal: Property,
    val thin: Property,
    val fat: Property,
    val hair: Property,
) {
    val initial: Property = "initial"
    val inherit: Property = "inherit"
}

/**
 * Defines a value that has different expressions for different sizes.
 *
 * There is a special sub-range for "bigger" sizes called ``wide``. "Bigger" really means sizes that reach dimensions up
 * to the whole screen and have to _scale_ much different (much wider steps obviously) than the basic ones.
 *
 * We believe that in most of the cases one would like to define rather *small* sizes, ranging between a fraction of
 * an ``rem`` and at most ``2rem``. This is why those are _intentionally_ the basic properties of this type and the
 * more wider scaled ones need the ``wide`` "prefix"!
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
    full: Property = giant,
    val wide: ScaledValue,
) : ScaledValue(normal, small, smaller, tiny, large, larger, huge, giant, full = full) {
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
    private val toast: Int, private val toastStep: Int, private val modal: Int, private val modalStep: Int,
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
    fun layer(value: Int): Property = zIndexFrom(layer, layerStep, value, 0)

    /**
     * creates [Property] for a specific toast z-index
     *
     * @param value number of toast the z-index should be calculated for
     */
    fun toast(value: Int): Property = zIndexFrom(toast, toastStep, value, 0)

    /**
     * creates [Property] for a specific modals z-index
     *
     * @param value number of modal the z-index should be calculated for
     */
    fun modal(value: Int): Property = zIndexFrom(modal, modalStep, value, 0)

    /**
     * creates [Property] for a specific modals z-index shifted by an offset
     *
     * @param value number of modal the z-index should be calculated for
     * @param offset number to add to the final z-index in order to place an element below (negative value!) or on top
     *               (positive value) of a regularly defined modal.
     */
    fun modal(value: Int, offset: Int): Property = zIndexFrom(modal, modalStep, value, offset)

    private fun zIndexFrom(level: Int, step: Int, value: Int, offset: Int) =
        "${level + step * (value - 1) + offset}"
}

/**
 * Defines the fonts-family in a theme
 */
interface FontFamilies {
    val normal: Property
    val mono: Property
}

/**
 * Defines three colors for a color scheme.
 * First is [main] which is the default color and [mainContrast]
 * for showing things on top of [highlight] color.
 * Besides from that there is a color [highlight] for highlighting things
 * and a color [highlightContrast] for showing things on top of [highlight] color.
 */
open class ColorScheme(
    val main: ColorProperty,
    val mainContrast: ColorProperty,
    val highlight: ColorProperty,
    val highlightContrast: ColorProperty
) {
    fun inverted() : ColorScheme = ColorScheme(this.highlight, this.highlightContrast, this.main, this.mainContrast)
}

/**
 * Defines the scheme colors in a theme
 */
interface Colors {
    val primary: ColorScheme
    val secondary: ColorScheme
    val tertiary: ColorScheme
    val success: ColorScheme
    val danger: ColorScheme
    val warning: ColorScheme
    val info: ColorScheme
    val neutral: ColorScheme

    val disabled: ColorProperty
    val focus: ColorProperty

    val gray50: ColorProperty
    val gray100: ColorProperty
    val gray200: ColorProperty
    val gray300: ColorProperty
    val gray400: ColorProperty
    val gray500: ColorProperty
    val gray600: ColorProperty
    val gray700: ColorProperty
    val gray800: ColorProperty
    val gray900: ColorProperty
}

/**
 * Defines the scheme shadows in a theme
 */
class Shadows(
    val flat: ShadowProperty,
    val raised: ShadowProperty,
    val raisedFurther: ShadowProperty = raised,
    val top: ShadowProperty = raisedFurther,
    val lowered: ShadowProperty,
    val bottom: ShadowProperty = lowered,
    val outline: ShadowProperty,
    val glowing: ShadowProperty = outline,
    val danger: ShadowProperty,
    val none: ShadowProperty = "none",
)

/**
 * Defines a specific icon
 */
class IconDefinition(
    val displayName: String,
    val viewBox: String = "0 0 24 24",
    val svg: String,
)

/**
 * Definition of standard-icons
 */
interface Icons {
    val add: IconDefinition
    val archive: IconDefinition
    val arrowDown: IconDefinition
    val arrowLeftDown: IconDefinition
    val arrowLeftUp: IconDefinition
    val arrowLeft: IconDefinition
    val arrowRightDown: IconDefinition
    val arrowRightUp: IconDefinition
    val arrowRight: IconDefinition
    val arrowUp: IconDefinition
    val attachment: IconDefinition
    val ban: IconDefinition
    val barChartAlt: IconDefinition
    val barChart: IconDefinition
    val board: IconDefinition
    val book: IconDefinition
    val bookmark: IconDefinition
    val calendar: IconDefinition
    val call: IconDefinition
    val camera: IconDefinition
    val caretDown: IconDefinition
    val caretLeft: IconDefinition
    val caretRight: IconDefinition
    val caretUp: IconDefinition
    val check: IconDefinition
    val chevronDoubleDown: IconDefinition
    val chevronDoubleLeft: IconDefinition
    val chevronDoubleRight: IconDefinition
    val chevronDoubleUp: IconDefinition
    val chevronDown: IconDefinition
    val chevronLeft: IconDefinition
    val chevronRight: IconDefinition
    val chevronUp: IconDefinition
    val circleAdd: IconDefinition
    val circleArrowDown: IconDefinition
    val circleArrowLeft: IconDefinition
    val circleArrowRight: IconDefinition
    val circleArrowUp: IconDefinition
    val circleCheck: IconDefinition
    val circleError: IconDefinition
    val circleHelp: IconDefinition
    val circleInformation: IconDefinition
    val circleRemove: IconDefinition
    val circleWarning: IconDefinition
    val clipboardCheck: IconDefinition
    val clipboardList: IconDefinition
    val clipboard: IconDefinition
    val clock: IconDefinition
    val close: IconDefinition
    val cloudDownload: IconDefinition
    val cloudUpload: IconDefinition
    val cloud: IconDefinition
    val computer: IconDefinition
    val copy: IconDefinition
    val creditCard: IconDefinition
    val delete: IconDefinition
    val documentAdd: IconDefinition
    val documentCheck: IconDefinition
    val documentDownload: IconDefinition
    val documentEmpty: IconDefinition
    val documentRemove: IconDefinition
    val document: IconDefinition
    val download: IconDefinition
    val drag: IconDefinition
    val editAlt: IconDefinition
    val edit: IconDefinition
    val email: IconDefinition
    val expand: IconDefinition
    val export: IconDefinition
    val externalLink: IconDefinition
    val eyeOff: IconDefinition
    val eye: IconDefinition
    val favorite: IconDefinition
    val filterAlt: IconDefinition
    val filter: IconDefinition
    val folderAdd: IconDefinition
    val folderCheck: IconDefinition
    val folderDownload: IconDefinition
    val folderRemove: IconDefinition
    val folder: IconDefinition
    val grid: IconDefinition
    val heart: IconDefinition
    val home: IconDefinition
    val image: IconDefinition
    val inbox: IconDefinition
    val laptop: IconDefinition
    val linkAlt: IconDefinition
    val link: IconDefinition
    val list: IconDefinition
    val location: IconDefinition
    val lock: IconDefinition
    val logOut: IconDefinition
    val map: IconDefinition
    val megaphone: IconDefinition
    val menu: IconDefinition
    val messageAlt: IconDefinition
    val message: IconDefinition
    val mobile: IconDefinition
    val moon: IconDefinition
    val notificationOff: IconDefinition
    val notification: IconDefinition
    val optionsHorizontal: IconDefinition
    val optionsVertical: IconDefinition
    val pause: IconDefinition
    val percentage: IconDefinition
    val pin: IconDefinition
    val play: IconDefinition
    val refresh: IconDefinition
    val remove: IconDefinition
    val search: IconDefinition
    val select: IconDefinition
    val send: IconDefinition
    val settings: IconDefinition
    val share: IconDefinition
    val shoppingCartAdd: IconDefinition
    val shoppingCart: IconDefinition
    val sort: IconDefinition
    val speakers: IconDefinition
    val stop: IconDefinition
    val sun: IconDefinition
    val switch: IconDefinition
    val table: IconDefinition
    val tablet: IconDefinition
    val tag: IconDefinition
    val undo: IconDefinition
    val unlock: IconDefinition
    val userAdd: IconDefinition
    val userCheck: IconDefinition
    val userRemove: IconDefinition
    val user: IconDefinition
    val users: IconDefinition
    val volumeOff: IconDefinition
    val volumeUp: IconDefinition
    val warning: IconDefinition
    val zoomIn: IconDefinition
    val zoomOut: IconDefinition
    val fritz2: IconDefinition
}

/**
 * general component's theme abstractions
 */
interface SeverityStyles {
    val info: Style<BasicParams>
    val success: Style<BasicParams>
    val warning: Style<BasicParams>
    val error: Style<BasicParams>
}

interface SeverityAware {
    val severity: SeverityStyles
}

interface FormSizes {
    val small: Style<BasicParams>
    val normal: Style<BasicParams>
    val large: Style<BasicParams>
}


/**
 * definition of the theme's checkbox
 */
interface CheckboxStyles : SeverityAware {
    val sizes: FormSizes
    val input: Style<BasicParams>
    val icon: Style<BasicParams>
    val label: Style<BasicParams>
    val default: Style<BasicParams>
    val checked: Style<BasicParams>
}


/**
 * definition of the theme's radioButton
 */
interface RadioStyles : SeverityAware {
    val sizes: FormSizes
    val input: Style<BasicParams>
    val label: Style<BasicParams>
    val default: Style<BasicParams>
    val selected: Style<BasicParams>
}


/**
 * definition of the theme's switch
 */
interface SwitchStyles : SeverityAware {
    val sizes: FormSizes
    val input: Style<BasicParams>
    val dot: Style<BasicParams>
    val label: Style<BasicParams>
    val default: Style<BasicParams>
    val checked: Style<BasicParams>
}


/**
 * definition of the theme's inputField
 */
interface InputFieldStyles : SeverityAware {
    val variants: InputFieldVariants
    val sizes: FormSizes
}

interface InputFieldVariants {
    val outline: Style<BasicParams>
    val filled: Style<BasicParams>
}


/**
 * definition of the theme's pushButton
 */
interface PushButtonStyles {
    val types: PushButtonTypes
    val variants: PushButtonVariants
    val sizes: FormSizes
}

interface PushButtonTypes {
    val primary: ColorScheme
    val secondary: ColorScheme
    val info: ColorScheme
    val success: ColorScheme
    val warning: ColorScheme
    val danger: ColorScheme
}

interface PushButtonVariants {
    val outline: BasicParams.(ColorScheme) -> Unit
    val solid: BasicParams.(ColorScheme) -> Unit
    val ghost: BasicParams.(ColorScheme) -> Unit
    val link: BasicParams.(ColorScheme) -> Unit
}

/**
 * definition of the theme's modal
 */
interface ModalStyles {
    val overlay: Style<BasicParams>
    val sizes: ModalSizes
    val variants: ModalVariants
}

interface ModalVariants {
    val auto: Style<BasicParams>
    val verticalFilled: Style<BasicParams>
    val centered: Style<BasicParams>
}

interface ModalSizes {
    val full: Style<BasicParams>
    val small: Style<BasicParams>
    val normal: Style<BasicParams>
    val large: Style<BasicParams>
}


/**
 * definition of the theme's popover
 */
interface PopoverStyles {
    val size: PopoverSizes
    val toggle: Style<BasicParams>
    val header: Style<BasicParams>
    val section: Style<BasicParams>
    val footer: Style<BasicParams>
    val placement: PopoverPlacements
    val arrowPlacement: PopoverArrowPlacements
    val closeButton: Style<BasicParams>
}

interface PopoverPlacements {
    val top: Style<BasicParams>
    val right: Style<BasicParams>
    val bottom: Style<BasicParams>
    val left: Style<BasicParams>
}

interface PopoverArrowPlacements {
    val top: Style<BasicParams>
    val right: Style<BasicParams>
    val bottom: Style<BasicParams>
    val left: Style<BasicParams>
}

interface PopoverSizes {
    val auto: Style<BasicParams>
    val normal: Style<BasicParams>
}


/**
 * definition of the theme's tooltip
 */
interface TooltipStyles {
    fun write(vararg value: String): Style<BasicParams>
    fun write(vararg value: String, tooltipPlacement: TooltipPlacements.() -> Style<BasicParams>): Style<BasicParams>
    val placement: TooltipPlacements
}

interface TooltipPlacements {
    val top: Style<BasicParams>
    val right: Style<BasicParams>
    val bottom: Style<BasicParams>
    val left: Style<BasicParams>
}

/**
 * definition of the theme's textArea styles
 */
interface TextAreaStyles : SeverityAware {
    val resize: TextAreaResize
    val sizes: FormSizes
    val variants: TextAreaVariants
}

/**
 * definition of the theme's textArea resizes
 */
interface TextAreaResize {
    val both: Style<BasicParams>
    val none: Style<BasicParams>
    val vertical: Style<BasicParams>
    val horizontal: Style<BasicParams>
}

interface TextAreaVariants {
    val basic: Style<BasicParams>
}

/**
 * definition of the theme's selectField styles
 */
interface SelectFieldStyles : SeverityAware {
    val variants: SelectFieldVariants
    val sizes: FormSizes
}

/**
 * definition of the theme's selectField variants
 */
interface SelectFieldVariants {
    val outline: Style<BasicParams>
    val filled: Style<BasicParams>
}


/**
 * definition of the theme's formControl
 */
interface FormControlStyles {
    val sizes: FormSizes
    val label: Style<BasicParams>
    val helperText: Style<BasicParams>
}


/**
 * definition of the theme's alerts
 */
interface AlertStyles {
    val severities: AlertSeverities
    val variants: AlertVariants
    val sizes: FormSizes
    val stacking: AlertStacking
}

interface AlertStacking {
    val compact: Style<BasicParams>
    val separated: Style<BasicParams>
}

interface AlertSeverity {
    val colorScheme: ColorScheme
    val icon: IconDefinition
}

interface AlertSeverities {
    val info: AlertSeverity
    val success: AlertSeverity
    val warning: AlertSeverity
    val error: AlertSeverity
}

interface AlertVariants {
    val subtle: BasicParams.(ColorScheme) -> Unit
    val solid: BasicParams.(ColorScheme) -> Unit
    val leftAccent: BasicParams.(ColorScheme) -> Unit
    val topAccent: BasicParams.(ColorScheme) -> Unit
    val discreet: BasicParams.(ColorScheme) -> Unit
}


/**
 * definition of the theme's toasts
 */
interface ToastStyles {
    val placement: ToastPlacement
    val status: ToastStatus
    val closeButton : ToastButton
}

interface ToastPlacement {
    val top: Style<BasicParams>
    val topLeft: Style<BasicParams>
    val topRight: Style<BasicParams>
    val bottom: Style<BasicParams>
    val bottomLeft: Style<BasicParams>
    val bottomRight: Style<BasicParams>
}

interface ToastStatus {
    val success: Style<BasicParams>
    val error: Style<BasicParams>
    val warning: Style<BasicParams>
    val info: Style<BasicParams>
}

interface ToastButton {
    val close: Style<BasicParams>
}

/**
 * definition of the theme's appFrame
 */
interface AppFrameStyles {
    val headerHeight: Property
    val footerMinHeight: Property
    val mobileSidebarWidth: Property
    val brand: Style<FlexParams>
    val sidebar: Style<BasicParams>
    val nav: Style<BasicParams>
    val footer: Style<BasicParams>
    val header: Style<FlexParams>
    val main: Style<BasicParams>
    val tabs: Style<FlexParams>
    val navLink: Style<FlexParams>
    val activeNavLink: Style<FlexParams>
    val navSection: Style<BasicParams>
}

