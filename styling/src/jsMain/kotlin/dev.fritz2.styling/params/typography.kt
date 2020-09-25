package dev.fritz2.styling.params

import dev.fritz2.styling.Fonts
import dev.fritz2.styling.Property
import dev.fritz2.styling.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

typealias TextTransformProperty = Property

object TextTransforms : PropertyValues {
    override val key = "text-transform: "

    const val none: TextTransformProperty = "none"
    const val capitalize: TextTransformProperty = "capitalize"
    const val uppercase: TextTransformProperty = "uppercase"
    const val lowercase: TextTransformProperty = "lowercase"
    const val initial: TextTransformProperty = "initial"
    const val inherit: TextTransformProperty = "inherit"
}

typealias FontStyleProperty = Property

object FontStyles : PropertyValues {
    override val key = "font-style: "

    const val normal: FontStyleProperty = "normal"
    const val italic: FontStyleProperty = "italic"
    const val oblique: FontStyleProperty = "oblique"
    const val initial: FontStyleProperty = "initial"
    const val inherit: FontStyleProperty = "inherit"
}

typealias  TextAlignProperty = Property

object TextAligns : PropertyValues {
    override val key = "text-align: "

    const val left: TextAlignProperty = "left"
    const val right: TextAlignProperty = "right"
    const val center: TextAlignProperty = "center"
    const val justify: TextAlignProperty = "justify"
    const val initial: TextAlignProperty = "initial"
    const val inherit: TextAlignProperty = "inherit"
}

typealias FontWeightProperty = Property

object FontWeights : PropertyValues {
    override val key = "font-weight: "

    const val normal: FontWeightProperty = "normal"
    const val bold: FontWeightProperty = "bold"
    const val medium: FontWeightProperty = "500"
    const val semiBold: FontWeightProperty = "600"
    const val bolder: FontWeightProperty = "bolder"
    const val lighter: FontWeightProperty = "lighter"
    const val initial: FontWeightProperty = "initial"
    const val inherit: FontWeightProperty = "inherit"
}

internal const val fontSizeKey = "font-size: "
internal const val letterSpacingKey = "letter-spacing: "
internal const val lineHeightKey = "line-height: "


//FIXME: make abstract class to allow inline?
@ExperimentalCoroutinesApi
interface Typo : StyleParams {

    /*
     * fontFamily
    */
    fun fontFamily(value: Fonts.() -> Property) = property(fontSizeKey, theme().fonts, value)

    fun fontFamily(
        sm: (Fonts.() -> Property)? = null,
        md: (Fonts.() -> Property)? = null,
        lg: (Fonts.() -> Property)? = null,
        xl: (Fonts.() -> Property)? = null
    ) =
        property(fontSizeKey, theme().fonts, sm, md, lg, xl)

    /*
     * font-size
     */
    fun fontSize(value: ScaledValueProperty) = property(fontSizeKey, theme().fontSizes, value)

    fun fontSize(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(fontSizeKey, theme().fontSizes, sm, md, lg, xl)

    /*
     * font-weight
     */
    fun fontWeight(value: FontWeights.() -> FontWeightProperty) = property(FontWeights, value)

    fun fontWeight(
        sm: (FontWeights.() -> FontWeightProperty)? = null,
        md: (FontWeights.() -> FontWeightProperty)? = null,
        lg: (FontWeights.() -> FontWeightProperty)? = null,
        xl: (FontWeights.() -> FontWeightProperty)? = null,
    ) =
        property(FontWeights, sm, md, lg, xl)

    /*
     * line-height
     */
    fun lineHeight(value: ScaledValueProperty) = property(lineHeightKey, theme().lineHeights, value)

    fun lineHeight(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(lineHeightKey, theme().lineHeights, sm, md, lg, xl)

    /*
     * letter-spacing
     */
    fun letterSpacing(value: ScaledValueProperty) = property(letterSpacingKey, theme().letterSpacings, value)

    fun letterSpacing(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(letterSpacingKey, theme().letterSpacings, sm, md, lg, xl)

    /*
     * text-align
     */
    fun textAlign(value: TextAligns.() -> TextAlignProperty) = property(TextAligns, value)

    fun textAlign(
        sm: (TextAligns.() -> TextAlignProperty)? = null,
        md: (TextAligns.() -> TextAlignProperty)? = null,
        lg: (TextAligns.() -> TextAlignProperty)? = null,
        xl: (TextAligns.() -> TextAlignProperty)? = null,
    ) =
        property(TextAligns, sm, md, lg, xl)

    /*
     * text-transform
     */
    fun textTransform(value: TextTransforms.() -> TextTransformProperty) = property(TextTransforms, value)

    fun textTransform(
        sm: (TextTransforms.() -> TextTransformProperty)? = null,
        md: (TextTransforms.() -> TextTransformProperty)? = null,
        lg: (TextTransforms.() -> TextTransformProperty)? = null,
        xl: (TextTransforms.() -> TextTransformProperty)? = null,
    ) =
        property(TextTransforms, sm, md, lg, xl)

    /*
     * font-style
     */
    fun fontStyle(value: FontStyles.() -> FontStyleProperty) = property(FontStyles, value)

    fun fontStyle(
        sm: (FontStyles.() -> FontStyleProperty)? = null,
        md: (FontStyles.() -> FontStyleProperty)? = null,
        lg: (FontStyles.() -> FontStyleProperty)? = null,
        xl: (FontStyles.() -> FontStyleProperty)? = null,
    ) =
        property(FontStyles, sm, md, lg, xl)
}