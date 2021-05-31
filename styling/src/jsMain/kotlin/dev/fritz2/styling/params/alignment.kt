package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property


/**
 * Alias for specific justification properties.
 */
typealias JustifyContentProperty = Property

/**
 * Predefined values for justification
 * Central Alignment Contexts inspired by [https://drafts.csswg.org/css-align-3/]
 */
object JustifyContentValues : PropertyValues {
    override val key = "justify-content: "

    const val start: JustifyContentProperty = "start"
    const val end: JustifyContentProperty = "end"
    const val flexStart: JustifyContentProperty = "flex-start"
    const val flexEnd: JustifyContentProperty = "flex-end"
    const val center: JustifyContentProperty = "center"
    const val spaceBetween: JustifyContentProperty = "space-between"
    const val spaceAround: JustifyContentProperty = "space-around"
    const val spaceEvenly: JustifyContentProperty = "space-evenly"
}

/**
 * Alias for specific item alignment properties.
 */
typealias AlignItemsProperty = Property

/**
 * Predefined values for alignment of items
 * Central Alignment Contexts inspired by [https://drafts.csswg.org/css-align-3/]
 */
object AlignItemsValues : PropertyValues {
    override val key = "align-items: "

    const val start: AlignItemsProperty = "start"
    const val end: AlignItemsProperty = "end"
    const val flexStart: AlignItemsProperty = "flex-start"
    const val flexEnd: AlignItemsProperty = "flex-end"
    const val selfStart: AlignItemsProperty = "self-start"
    const val selfEnd: AlignItemsProperty = "self-end"
    const val center: AlignItemsProperty = "center"
    const val stretch: AlignItemsProperty = "stretch"
    const val baseline: AlignItemsProperty = "baseline"
}

/**
 * Alias for specific content alignment properties.
 */
typealias AlignContentProperty = Property

/**
 * Predefined values for alignment of content
 * Central Alignment Contexts inspired by [https://drafts.csswg.org/css-align-3/]
 */
object AlignContentValues : PropertyValues {
    override val key = "align-content: "

    const val start: AlignContentProperty = "start"
    const val end: AlignContentProperty = "end"
    const val flexStart: AlignContentProperty = "flex-start"
    const val flexEnd: AlignContentProperty = "flex-end"
    const val center: AlignContentProperty = "center"
    const val spaceBetween: AlignContentProperty = "space-between"
    const val spaceAround: AlignContentProperty = "space-around"
    const val spaceEvenly: AlignContentProperty = "space-evenly"
}

/**
 * This _context_ interface offers functions to align or justify elements.
 *
 * It used in [FlexParams] and [GridParams] .
 *
 * There are overrides for all functions that enable one to define the styling for
 * the different media devices independently.
 */
interface Alignment : StyleParams {
    /**
     * This function sets the [justify-content](https://developer.mozilla.org/en/docs/Web/CSS/justify-content) property
     *
     * Example call:
     * ```
     * justifyContent { flexStart }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values from [JustifyContentValues]
     */
    fun justifyContent(value: JustifyContentValues.() -> JustifyContentProperty) =
        property(JustifyContentValues.key, JustifyContentValues.value(), smProperties)

    /**
     * This function sets the [justify-content](https://developer.mozilla.org/en/docs/Web/CSS/justify-content) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * justifyContent(
     *     sm = { flexStart },
     *     lg = { center }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values from [JustifyContentValues]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [JustifyContentValues]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [JustifyContentValues]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [JustifyContentValues]
     */
    fun justifyContent(
        sm: (JustifyContentValues.() -> JustifyContentProperty)? = null,
        md: (JustifyContentValues.() -> JustifyContentProperty)? = null,
        lg: (JustifyContentValues.() -> JustifyContentProperty)? = null,
        xl: (JustifyContentValues.() -> JustifyContentProperty)? = null
    ) {
        if (sm != null) property(JustifyContentValues.key, JustifyContentValues.sm(), smProperties)
        if (md != null) property(JustifyContentValues.key, JustifyContentValues.md(), mdProperties)
        if (lg != null) property(JustifyContentValues.key, JustifyContentValues.lg(), lgProperties)
        if (xl != null) property(JustifyContentValues.key, JustifyContentValues.xl(), xlProperties)
    }

    /**
     * This function sets the [align-items](https://developer.mozilla.org/en/docs/Web/CSS/align-items) property
     *
     * Example call:
     * ```
     * alignItems { flexStart }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values from [AlignItemsValues]
     */
    fun alignItems(value: AlignItemsValues.() -> AlignItemsProperty) =
        property(AlignItemsValues.key, AlignItemsValues.value(), smProperties)

    /**
     * This function sets the [align-items](https://developer.mozilla.org/en/docs/Web/CSS/align-items) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * alignItems(
     *     sm = { flexStart },
     *     lg = { center }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values from [AlignItemsValues]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [AlignItemsValues]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [AlignItemsValues]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [AlignItemsValues]
     */
    fun alignItems(
        sm: (AlignItemsValues.() -> AlignItemsProperty)? = null,
        md: (AlignItemsValues.() -> AlignItemsProperty)? = null,
        lg: (AlignItemsValues.() -> AlignItemsProperty)? = null,
        xl: (AlignItemsValues.() -> AlignItemsProperty)? = null
    ) {
        if (sm != null) property(AlignItemsValues.key, AlignItemsValues.sm(), smProperties)
        if (md != null) property(AlignItemsValues.key, AlignItemsValues.md(), mdProperties)
        if (lg != null) property(AlignItemsValues.key, AlignItemsValues.lg(), lgProperties)
        if (xl != null) property(AlignItemsValues.key, AlignItemsValues.xl(), xlProperties)
    }

    /**
     * This function sets the [align-content](https://developer.mozilla.org/en/docs/Web/CSS/align-content) property
     *
     * Example call:
     * ```
     * align-content { flexStart }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values from [AlignContentValues]
     */
    fun alignContent(value: AlignContentValues.() -> AlignContentProperty) =
        property(AlignContentValues.key, AlignContentValues.value(), smProperties)

    /**
     * This function sets the [align-content](https://developer.mozilla.org/en/docs/Web/CSS/align-content) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * align-content(
     *     sm = { flexStart },
     *     lg = { center }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values from [AlignContentValues]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [AlignContentValues]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [AlignContentValues]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [AlignContentValues]
     */
    fun alignContent(
        sm: (AlignContentValues.() -> AlignContentProperty)? = null,
        md: (AlignContentValues.() -> AlignContentProperty)? = null,
        lg: (AlignContentValues.() -> AlignContentProperty)? = null,
        xl: (AlignContentValues.() -> AlignContentProperty)? = null
    ) {
        if (sm != null) property(AlignContentValues.key, AlignContentValues.sm(), smProperties)
        if (md != null) property(AlignContentValues.key, AlignContentValues.md(), mdProperties)
        if (lg != null) property(AlignContentValues.key, AlignContentValues.lg(), lgProperties)
        if (xl != null) property(AlignContentValues.key, AlignContentValues.xl(), xlProperties)
    }
}

/**
 * Alias for specific self alignment properties.
 */
typealias SelfAlignItemProperty = Property

/**
 * Predefined values for self alignment
 * Central Alignment Contexts inspired by [https://drafts.csswg.org/css-align-3/]
 */
object SelfAlignItemsValues : PropertyValues {
    override val key = "align-self: "

    const val start: SelfAlignItemProperty = "start"
    const val end: SelfAlignItemProperty = "end"
    const val flexStart: SelfAlignItemProperty = "flex-start"
    const val flexEnd: SelfAlignItemProperty = "flex-end"
    const val center: SelfAlignItemProperty = "center"
    const val stretch: SelfAlignItemProperty = "stretch"
    const val baseline: SelfAlignItemProperty = "baseline"
}

/**
 * This _context_ interface offers functions for self-alignment.
 *
 * It used in [FlexParams] and [GridParams] .
 *
 * There are overrides for all functions that enable one to define the styling for
 * the different media devices independently.
 */
interface SelfAlignment {
    /**
     * This function sets the [self-align](https://developer.mozilla.org/en/docs/Web/CSS/align-content) property
     *
     * Example call:
     * ```
     * self-align { flexStart }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values from [AlignContentValues]
     */
    fun alignSelf(value: SelfAlignItemsValues.() -> SelfAlignItemProperty)
}

internal class SelfAlignmentImpl(
    styleParam: StyleParams,
    private val target: StringBuilder
) : SelfAlignment, StyleParams by styleParam {
    override fun alignSelf(value: SelfAlignItemsValues.() -> SelfAlignItemProperty) =
        property(SelfAlignItemsValues.key, SelfAlignItemsValues.value(), target)
}