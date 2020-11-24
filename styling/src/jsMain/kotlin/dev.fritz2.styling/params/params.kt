package dev.fritz2.styling.params

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * shortcut for functions that derive a value from [ScaledValue] defined at the [Theme]
 */
typealias ScaledValueProperty = ScaledValue.() -> Property

/**
 * shortcut for functions that derive a value from [WeightedValue] defined at the [Theme]
 */
typealias WeightedValueProperty = WeightedValue.() -> Property

/**
 * shortcut for functions that derive a value from [SizesProperty] defined at the [Theme]
 */
typealias SizesProperty = Sizes.() -> Property

/**
 * shortcut for functions that derive a value from [ZIndicesProperty] defined at the [Theme]
 */
typealias  ZIndicesProperty = ZIndices.() -> Property

/**
 * base-interface for all enum base properties
 */
interface PropertyValues {
    val key: String
}


/**
 * DSLMarker-annotation for style-parameter-DSL
 */
@DslMarker
annotation class StyleParamsMarker

/**
 * Basic interface for all StyleParams-classes
 *
 * @property smProperties collects the properties for small screens
 * @property mdProperties collects the properties for middle-sized screens
 * @property lgProperties collects the properties for large screens
 * @property xlProperties collects the properties for extra-large screens
 */
@StyleParamsMarker
@HtmlTagMarker
interface StyleParams {
    val smProperties: StringBuilder
    val mdProperties: StringBuilder
    val lgProperties: StringBuilder
    val xlProperties: StringBuilder
}

const val cssDelimiter = ";"

/**
 * sets a property for a given target screen size
 *
 * @param key of the property
 * @param value of the property
 * @param target [StringBuilder] representing the screen-size to add the property for
 */
fun StyleParams.property(
    key: String,
    value: Property,
    target: StringBuilder = smProperties
) {
    target.append(key, value, cssDelimiter)
}

/**
 * sets a responsive property for
 *
 * @param key of the property
 * @param sm value of the property for small screens
 * @param md value of the property for middle-sized screens
 * @param lg value of the property for large screens
 * @param xl value of the property for extra-large screens
 */
fun StyleParams.property(
    key: String,
    sm: Property? = null,
    md: Property? = null,
    lg: Property? = null,
    xl: Property? = null
) {
    if (sm != null) property(key, sm, smProperties)
    if (md != null) property(key, md, mdProperties)
    if (lg != null) property(key, lg, lgProperties)
    if (xl != null) property(key, xl, xlProperties)
}

/**
 * sets a property derived from the [Theme]
 *
 * @param key of the Property
 * @param base scale defined in a [Theme] to derive the value from
 * @param value function, how to derive the value
 * @param target [StringBuilder] representing the screen-size for which the property should be set
 */
inline fun <T> StyleParams.property(
    key: String,
    base: T,
    value: T.() -> Property,
    target: StringBuilder = smProperties
) =
    property(key, base.value(), target)

/**
 * sets a responsive property derived from the [Theme]
 *
 * @param key of the Property
 * @param base scale defined in a [Theme] to derive the value from
 * @param sm  function, how to derive the value for small screens
 * @param md  function, how to derive the value for middle-sized screens
 * @param lg  function, how to derive the value for large screens
 * @param xl  function, how to derive the value for extra-large screens
 */
fun <T> StyleParams.property(
    key: String,
    base: T,
    sm: (T.() -> Property)? = null,
    md: (T.() -> Property)? = null,
    lg: (T.() -> Property)? = null,
    xl: (T.() -> Property)? = null
) =
    property(key,
        sm?.let { it(base) },
        md?.let { it(base) },
        lg?.let { it(base) },
        xl?.let { it(base) }
    )

/**
 * sets an enum-based property for small-screens (default)
 *
 * @param base scale defined in a [Theme] to derive the value from
 * @param value function, how to derive the value
 */
inline fun <T : PropertyValues> StyleParams.property(
    base: T, value: T.() -> Property
) = property(base.key, value(base), smProperties)

/**
 * sets a responsive enum-based property
 *
 * @param base scale defined in a [Theme] to derive the value from
 * @param sm function, how to derive the value for small screens
 * @param md function, how to derive the value for middle-sized screens
 * @param lg function, how to derive the value for large screens
 * @param xl function, how to derive the value for extra-large screens
 */
fun <T : PropertyValues> StyleParams.property(
    base: T,
    sm: (T.() -> Property)? = null,
    md: (T.() -> Property)? = null,
    lg: (T.() -> Property)? = null,
    xl: (T.() -> Property)? = null
) =
    property(
        base.key,
        sm?.let { sm(base) },
        md?.let { md(base) },
        lg?.let { lg(base) },
        xl?.let { xl(base) },
    )


/**
 * Implemantation of all [StyleParams]-interface providing the needed [StringBuilder]s
 *
 * @property smProperties collects the properties for small screens
 * @property mdProperties collects the properties for middle-sized screens
 * @property lgProperties collects the properties for large screens
 * @property xlProperties collects the properties for extra-large screens
 */
@ExperimentalCoroutinesApi
open class StyleParamsImpl : BoxParams {
    override val smProperties = StringBuilder()
    override val mdProperties = StringBuilder()
    override val lgProperties = StringBuilder()
    override val xlProperties = StringBuilder()

    /**
     * creates a valid responsive css-rule-body from the content of the screen-size-[StringBuilder]s
     */
    fun toCss(): String {
        if (mdProperties.isNotEmpty()) smProperties.append(Theme().mediaQueryMd, "{", mdProperties, "}")
        if (lgProperties.isNotEmpty()) smProperties.append(Theme().mediaQueryLg, "{", lgProperties, "}")
        if (xlProperties.isNotEmpty()) smProperties.append(Theme().mediaQueryXl, "{", xlProperties, "}")

        return smProperties.toString()
    }

    fun cssClasses(prefix: String): StyleClass? =
        toCss().let { if (it.isNotEmpty()) style(it, prefix) else StyleClass.None }
}

/**
 * shortcut for a function defining style-parameters
 */
typealias Style<T> = T.() -> Unit

/**
 * combines to [Style]s
 */
inline operator fun <T> Style<T>.plus(crossinline other: Style<T>): Style<T> = {
    this@plus()
    other()
}

/**
 * interface combining all the basic style-parameters
 */
@ExperimentalCoroutinesApi
interface BasicParams : Space, Color, Border, Typo, Background, Position, Shadow, Layout, PseudoClasses, PseudoElements {
    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedBasicStyle] to include
     */
    operator fun PredefinedBasicStyle.invoke() = this(this@BasicParams)
}

/**
 * interface combining flex-style-parameters with the basic style-parameters
 */
@ExperimentalCoroutinesApi
interface FlexParams : BasicParams, Flexbox {
    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedFlexStyle] to include
     */
    override operator fun PredefinedBasicStyle.invoke() = this(this@FlexParams)

    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedFlexStyle] to include
     */
    operator fun PredefinedFlexStyle.invoke() = this(this@FlexParams)
}

/**
 * interface combining grid-style-parameters with the basic style-parameters
 */
@ExperimentalCoroutinesApi
interface GridParams : BasicParams, GridLayout {
    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedBasicStyle] to include
     */
    override operator fun PredefinedBasicStyle.invoke() = this(this@GridParams)

    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedGridStyle] to include
     */
    operator fun PredefinedGridStyle.invoke() = this(this@GridParams)
}

/**
 * interface combining flex-style-parameters with grid and basic style-parameters
 */
@ExperimentalCoroutinesApi
interface BoxParams : FlexParams, GridParams {
    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedBasicStyle] to include
     */
    override operator fun PredefinedBasicStyle.invoke() = this(this@BoxParams)

    /**
     * allows the usage of predefined styles in this context
     *
     * @receiver [PredefinedBoxStyle] to include
     */
    operator fun PredefinedBoxStyle.invoke() = this(this@BoxParams)
}
