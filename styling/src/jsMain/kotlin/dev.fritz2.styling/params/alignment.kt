package dev.fritz2.styling.params

import dev.fritz2.styling.Property

/*
 * Central Alignment Contexts, inspired by https://drafts.csswg.org/css-align-3/
 */

typealias JustifyContentProperty = Property

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

typealias AlignItemsProperty = Property

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

typealias AlignContentProperty = Property

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

interface Alignment : StyleParams {
    /*
     * justifyContent
     */

    fun justifyContent(value: JustifyContentValues.() -> JustifyContentProperty) =
        property(JustifyContentValues.key, JustifyContentValues.value(), smProperties)

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

    /*
     * alignItems
     */

    fun alignItems(value: AlignItemsValues.() -> AlignItemsProperty) =
        property(AlignItemsValues.key, AlignItemsValues.value(), smProperties)

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

    /*
     * alignContent
     */

    fun alignContent(value: AlignContentValues.() -> AlignContentProperty) =
        property(AlignContentValues.key, AlignContentValues.value(), smProperties)

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

typealias SelfAlignItemProperty = Property

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

interface SelfAlignment {
    fun alignSelf(value: SelfAlignItemsValues.() -> SelfAlignItemProperty)
}

class SelfAlignmentImpl(
    styleParam: StyleParams,
    private val target: StringBuilder
) : SelfAlignment, StyleParams by styleParam {
    override fun alignSelf(value: SelfAlignItemsValues.() -> SelfAlignItemProperty) =
        property(SelfAlignItemsValues.key, SelfAlignItemsValues.value(), target)
}