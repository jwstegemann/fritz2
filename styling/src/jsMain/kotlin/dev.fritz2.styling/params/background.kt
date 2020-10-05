package dev.fritz2.styling.params

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.styling.theme.Property

/**
 * Alias for the specific [BackgroundRepeats] properties
 */
typealias BackgroundRepeatProperty = Property

/**
 * Predefined values for the [background-repeat](https://developer.mozilla.org/en-US/docs/Web/CSS/background-repeat)
 * property. Should be used as expression result in [BackgroundContext.repeat].
 */
object BackgroundRepeats {
    const val repeat: BackgroundRepeatProperty = "repeat"
    const val repeatX: BackgroundRepeatProperty = "repeat-x"
    const val repeatY: BackgroundRepeatProperty = "repeat-y"
    const val noRepeat: BackgroundRepeatProperty = "no-repeat"
    const val space: BackgroundRepeatProperty = "space"
    const val round: BackgroundRepeatProperty = "round"
    const val initial: BackgroundRepeatProperty = "initial"
    const val inherit: BackgroundRepeatProperty = "inherit"
}

/**
 * Alias for the specific [BackgroundBlendModes] properties
 */
typealias BackgroundBlendModeProperty = Property

/**
 * Predefined values for the [background-blend-mode](https://developer.mozilla.org/en-US/docs/Web/CSS/background-blend-mode)
 * property. Should be used as expression result in [BackgroundContext.blendMode].
 */
object BackgroundBlendModes {
    const val normal: BackgroundBlendModeProperty = "normal"
    const val multiply: BackgroundBlendModeProperty = "multiply"
    const val screen: BackgroundBlendModeProperty = "screen"
    const val overlay: BackgroundBlendModeProperty = "overlay"
    const val darken: BackgroundBlendModeProperty = "darken"
    const val lighten: BackgroundBlendModeProperty = "lighten"
    const val colorDodge: BackgroundBlendModeProperty = "color-dodge"
    const val colorBurn: BackgroundBlendModeProperty = "color-burn"
    const val hardLight: BackgroundBlendModeProperty = "hard-light "
    const val softLight: BackgroundBlendModeProperty = "soft-light"
    const val difference: BackgroundBlendModeProperty = "difference"
    const val exclusion: BackgroundBlendModeProperty = "exclusion"
    const val hue: BackgroundBlendModeProperty = "hue"
    const val saturation: BackgroundBlendModeProperty = "saturation"
    const val color: BackgroundBlendModeProperty = "color"
    const val luminosity: BackgroundBlendModeProperty = "luminosity"
}

/**
 * Alias for the specific [BackgroundPositions] properties
 */
typealias BackgroundPositionProperty = Property

/**
 * Predefined values for the [background-position](https://developer.mozilla.org/en-US/docs/Web/CSS/background-position)
 * property. Should be used as expression result in [BackgroundContext.position], [BackgroundPositionContext.horizontal]
 * or [BackgroundPositionContext.vertical].
 */
object BackgroundPositions {
    const val left: BackgroundPositionProperty = "left"
    const val center: BackgroundPositionProperty = "center"
    const val right: BackgroundPositionProperty = "right"
    const val top: BackgroundPositionProperty = "top"
    const val bottom: BackgroundPositionProperty = "bottom"
}

/**
 * Nested _context class_ to enable separate definitions of horizontal and vertical background positioning
 * for the [background-position](https://developer.mozilla.org/en-US/docs/Web/CSS/background-position) property.
 *
 * This _context_ is passed as receiver by [BackgroundContext.positions] function and is therefore nested within the
 * [BackgroundContext] context.
 *
 * This class exposes the two functions [horizontal] and [vertical] for defining those properties independently.
 * You can use predefined values from [BackgroundPositions] as parameters or arbitrary property values.
 * ```
 * positions { /* it == BackgroundPositionContext.() -> Unit */
 *     horizontal { center }
 *     vertical { top }
 * }
 * ```
 *
 * For passing only one value for _both_ dimensions have a look at [BackgroundContext.position].
 *
 * @param values external container for collecting the positioning information within this context
 */
class BackgroundPositionContext(private val values: MutableList<Property>) {
    fun horizontal(value: BackgroundPositions.() -> BackgroundPositionProperty) =
        values.add(BackgroundPositions.value())

    fun vertical(value: BackgroundPositions.() -> BackgroundPositionProperty) =
        values.add(BackgroundPositions.value())
}

/**
 * Alias for the specific [BackgroundSizes] properties
 */
typealias  BackgroundSizeProperty = Property

/**
 * Predefined values for the [background-size](https://developer.mozilla.org/en-US/docs/Web/CSS/background-size)
 * property. Should be used as expression result in [BackgroundContext.size], [BackgroundSizeContext.horizontal]
 * or [BackgroundSizeContext.vertical].
 */
object BackgroundSizes {
    const val auto: BackgroundSizeProperty = "auto"
    const val cover: BackgroundSizeProperty = "cover"
    const val contain: BackgroundSizeProperty = "contain"
}

/**
 * Nested _context class_ to enable separate definitions of horizontal and vertical background sizing
 * for the [background-size](https://developer.mozilla.org/en-US/docs/Web/CSS/background-size) property.
 *
 * This _context_ is passed as receiver by [BackgroundContext.sizes] function and is therefore nested within the
 * [BackgroundContext] context.
 *
 * This class exposes the two functions [horizontal] and [vertical] for defining those properties independently.
 * You can use predefined values from [BackgroundSizes] as parameters or arbitrary property values.
 * ```
 * sizes { /* it == BackgroundSizeContext.() -> Unit */
 *     horizontal { auto }
 *     vertical { "80%" }
 * }
 * ```
 *
 * For passing only one value for _both_ dimensions have a look at [BackgroundContext.position].
 *
 * @param values external container for collecting the positioning information within this context
 */
class BackgroundSizeContext(private val values: MutableList<Property>) {
    fun horizontal(value: BackgroundSizes.() -> BackgroundSizeProperty) =
        values.add(BackgroundSizes.value())

    fun vertical(value: BackgroundSizes.() -> BackgroundSizeProperty) =
        values.add(BackgroundSizes.value())
}

typealias BackgroundBoxProperty = Property

/**
 * Predefined values for the [background-origin](https://developer.mozilla.org/en-US/docs/Web/CSS/background-origin)
 * and [background-clip](https://developer.mozilla.org/en-US/docs/Web/CSS/background-clip) properties.
 * Should be used as expression result in [BackgroundContext.origin] or [BackgroundContext.clip].
 */
object BackgroundBoxValues {
    const val paddingBox: BackgroundBoxProperty = "padding-box"
    const val borderBox: BackgroundBoxProperty = "border-box"
    const val contentBox: BackgroundBoxProperty = "content-box"
}

typealias BackgroundAttachmentProperty = Property

/**
 * Predefined values for the [background-attachment](https://developer.mozilla.org/en-US/docs/Web/CSS/background-attachment)
 * property. Should be used as expression result in [BackgroundContext.attachment].
 */
object BackgroundAttachments {
    const val scroll: BackgroundAttachmentProperty = "scroll"
    const val fixed: BackgroundAttachmentProperty = "fixed"
    const val local: BackgroundAttachmentProperty = "local"
    const val inherit: BackgroundAttachmentProperty = "inherit"
}

internal const val backgroundBlendModeKey = "background-blend-mode: "
internal const val backgroundImageKey = "background-image: "
internal const val backgroundPositionKey = "background-position: "
internal const val backgroundSizeKey = "background-size: "
internal const val backgroundRepeatKey = "background-repeat: "
internal const val backgroundOriginKey = "background-origin: "
internal const val backgroundClipKey = "background-clip: "
internal const val backgroundAttachmentKey = "background-attachment: "
internal const val backgroundColorKey = "background-color: "

/**
 * This _context class_ enables the definition of _background_ related styling facilities.
 *
 * It provides topic oriented functions to set properties directly and in some cases offers functions
 * to enter sub-contexts in order to specify related property values in a readable, grouped manner (for example
 * [positions] or [sizes])
 *
 * Have a look at the following example to get an impression of the capabilities this class offers:
 * ```
 * background { /* it == BackgroundContext.() -> Unit */
 *    image { "https://via.placeholder.com/150/?text=Klein" }
 *    repeat { repeatX }
 *    positions { /* sub context */
 *        horizontal { center }
 *        vertical { center }
 *    }
 * }
 * ```
 *
 * @param styleParams basic context scope interface
 * @param target the defined output [StringBuilder] to write the generated CSS into
 */
@StyleParamsMarker
@HtmlTagMarker
class BackgroundContext(
    styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {

    /**
     * This function is used to set the [background-blend-mode](https://developer.mozilla.org/en-US/docs/Web/CSS/background-blend-mode)
     * property. Predefined values are offered by [BackgroundBlendModes].
     *
     * Example call
     * ```
     * background {
     *     blendMode { screen }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined blend mode values in [BackgroundBlendModes]
     *              into the scope of the functional expression
     */
    fun blendMode(value: BackgroundBlendModes.() -> BackgroundBlendModeProperty) =
        property(backgroundBlendModeKey, BackgroundBlendModes.value(), target)

    /**
     * This function is used to set a _URL_ for the
     * [background-image](https://developer.mozilla.org/en-US/docs/Web/CSS/background-image) property.
     *
     * Example call
     * ```
     * background {
     *     image { "https://via.placeholder.com/150/?text=Small"  }
     * }
     * ```
     *
     * If you explicitly want to _unset_ an image, use [noImage] instead!
     *
     * @param value function parameter in order to return an arbitrary [String]
     */
    fun image(value: () -> Property) =
        property(backgroundImageKey, "url(${value()})", target)

    /**
     * This function is used to set the value for the
     * [background-image](https://developer.mozilla.org/en-US/docs/Web/CSS/background-image) property to ``none``.
     *
     * If you want to set a URL, use [image] instead!
     */
    fun noImage() = property(backgroundImageKey, "none", target)

    /**
     * This function is used to set the [background-position](https://developer.mozilla.org/en-US/docs/Web/CSS/background-position)
     * property. Predefined values are offered by [BackgroundPositions].
     *
     * This functions only sets _one_ value.
     *
     * Example call
     * ```
     * background {
     *     position { right }
     * }
     * ```
     *
     * If you want to specify both dimensions independently, use the [positions] function in order to open a nested scope.
     * @see [BackgroundPositionContext]
     *
     * @param value extension function parameter to bring the predefined position values in [BackgroundPositionProperty]
     *              into the scope of the functional expression
     */
    fun position(value: BackgroundPositions.() -> BackgroundPositionProperty) =
        property(backgroundPositionKey, BackgroundPositions.value(), target)

    /**
     * This function opens a _sub scope_ in order to set the two dimensions (horizontal and vertical) of the
     * background position independently.
     *
     * @see [BackgroundPositionContext]
     *
     * If you just want to set first dimension, use [position] instead.
     *
     * @param value extension function parameter in order to bring the nested context into the scope of the functional
     *              expression.
     */
    fun positions(value: BackgroundPositionContext.() -> Unit) {
        val properties: MutableList<Property> = mutableListOf()
        BackgroundPositionContext(properties).value()
        property(backgroundPositionKey, properties.joinToString(" ").trim(), target)
    }

    /**
     * This function is used to set the [background-size](https://developer.mozilla.org/en-US/docs/Web/CSS/background-size)
     * property. Predefined values are offered by [BackgroundSizes].
     *
     * This functions only sets _one_ value.
     *
     * Example call
     * ```
     * background {
     *     size { cover }
     * }
     * ```
     *
     * If you want to specify both dimensions independently, use the [sizes] function in order to open a nested scope.
     * @see [BackgroundSizeContext]
     *
     * @param value extension function parameter to bring the predefined position values in [BackgroundSizes]
     *              into the scope of the functional expression
     */
    fun size(value: BackgroundSizes.() -> BackgroundSizeProperty) =
        property(backgroundSizeKey, BackgroundSizes.value(), target)

    /**
     * This function opens a _sub scope_ in order to set the two dimensions (horizontal and vertical) of the
     * background size independently.
     *
     * @see [BackgroundSizeContext]
     *
     * If you just want to set first dimension, use [size] instead.
     *
     * @param value extension function parameter in order to bring the nested context into the scope of the functional
     *              expression.
     */
    fun sizes(value: BackgroundSizeContext.() -> Unit) {
        val properties: MutableList<Property> = mutableListOf()
        BackgroundSizeContext(properties).value()
        property(backgroundSizeKey, properties.joinToString(" ").trim(), target)
    }

    /**
     * This function is used to set the [background-repeat](https://developer.mozilla.org/en-US/docs/Web/CSS/background-repeat)
     * property. Predefined values are offered by [BackgroundRepeats].
     *
     * Example call
     * ```
     * background {
     *     repeat { noRepeat }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined repeat values in [BackgroundBlendModes]
     *              into the scope of the functional expression
     */
    fun repeat(value: BackgroundRepeats.() -> BackgroundRepeatProperty) =
        property(backgroundRepeatKey, BackgroundRepeats.value(), target)

    /**
     * This function is used to set the [background-origin](https://developer.mozilla.org/en-US/docs/Web/CSS/background-origin)
     * property. Predefined values are offered by [BackgroundBoxValues].
     *
     * Example call
     * ```
     * background {
     *     origin { contentBox }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined origin values in [BackgroundBoxValues]
     *              into the scope of the functional expression
     */
    fun origin(value: BackgroundBoxValues.() -> BackgroundBoxProperty) =
        property(backgroundOriginKey, BackgroundBoxValues.value(), target)

    /**
     * This function is used to set the [background-clip](https://developer.mozilla.org/en-US/docs/Web/CSS/background-clip)
     * property. Predefined values are offered by [BackgroundBoxValues].
     *
     * Example call
     * ```
     * background {
     *     clip { contentBox }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined origin values in [BackgroundBoxValues]
     *              into the scope of the functional expression
     */
    fun clip(value: BackgroundBoxValues.() -> BackgroundBoxProperty) =
        property(backgroundClipKey, BackgroundBoxValues.value(), target)

    /**
     * This function is used to set the [background-attachment](https://developer.mozilla.org/en-US/docs/Web/CSS/background-attachment)
     * property. Predefined values are offered by [BackgroundAttachments].
     *
     * Example call
     * ```
     * background {
     *     attachment { scroll }
     * }
     * ```
     *
     * @param value extension function parameter to bring the predefined origin values in [BackgroundAttachments]
     *              into the scope of the functional expression
     */
    fun attachment(value: BackgroundAttachments.() -> BackgroundAttachmentProperty) =
        property(backgroundAttachmentKey, BackgroundAttachments.value(), target)

    /**
     * This function is used to set a color for the
     * [background-color](https://developer.mozilla.org/en-US/docs/Web/CSS/background-color) property.
     *
     * Example call
     * ```
     * background {
     *     color { rgba(255, 0, 0, 50)  }
     *     // color { secondary }
     *     // color { "white" } (currently we don't provide predefined CSS colors, so you have to provide a String literal)
     * }
     * ```
     *
     * @param value function parameter in order to return an arbitrary [String]
     */
    fun color(value: () -> Property) = property(backgroundColorKey, value(), target)
}

/**
 * This _context_ interface offers functions to style the background related CSS properties of a component.
 *
 * It basically offers only two common functions to leverage the specific functions of [BackgroundContext] to define
 * the real styling; one for the default media device and one for defining the background for multiple media devices.
 */
interface Background : StyleParams {

    /**
     * This function opens the context for defining the background related properties for all media devices.
     *
     * Example call:
     * ```
     * background {
     *     // some functions of [BackgroundContext] with appropriate values for *all devices*
     * }
     * ```
     *
     * @param value extension function parameter to bring the specialized topical functions of the [BackgroundContext]
     *              into the scope of the functional expression
     */
    fun background(value: BackgroundContext.() -> Unit) = BackgroundContext(this, smProperties).value()

    /**
     * This function opens the context for defining the background related properties for each media device independently.
     *
     * Example call:
     * ```
     * background(
     *     sm = {
     *         // some functions of [BackgroundContext] with appropriate values for *small devices*
     *     },
     *     lg = {
     *         // some functions of [BackgroundContext] with appropriate values for *large devices*
     *     }
     * )
     * ```
     *
     * @param sm extension function parameter to bring the specialized topical functions of the [BackgroundContext]
     *           into the scope of the functional expression for small media devices
     * @param md extension function parameter to bring the specialized topical functions of the [BackgroundContext]
     *           into the scope of the functional expression for medium sized media devices
     * @param lg extension function parameter to bring the specialized topical functions of the [BackgroundContext]
     *           into the scope of the functional expression for large media devices
     * @param xl extension function parameter to bring the specialized topical functions of the [BackgroundContext]
     *           into the scope of the functional expression for extra large media devices
     */
    fun background(
        sm: (BackgroundContext.() -> Unit)? = null,
        md: (BackgroundContext.() -> Unit)? = null,
        lg: (BackgroundContext.() -> Unit)? = null,
        xl: (BackgroundContext.() -> Unit)? = null,
    ) {
        if (sm != null) BackgroundContext(this, smProperties).sm()
        if (md != null) BackgroundContext(this, mdProperties).md()
        if (lg != null) BackgroundContext(this, lgProperties).lg()
        if (xl != null) BackgroundContext(this, xlProperties).xl()
    }
}
