package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Shadows
import dev.fritz2.styling.theme.theme

const val textShadowKey = "text-shadow: "
const val boxShadowKey = "box-shadow: "

/**
 * Alias for specific [Shadow] properties.
 */
typealias ShadowProperty = Property

/**
 * creates a valid value for shadow-properties like
 * [box-shadow](https://developer.mozilla.org/de/docs/Web/CSS/box-shadow) or
 * [text-shadow](https://developer.mozilla.org/de/docs/Web/CSS/text-shadow)
 *
 * @param offsetHorizontal horizontal offset of the shadow
 * @param offsetVertical vertical offset of the shadow
 * @param blur blur radius of the shadow
 * @param spread of the shadow
 * @param color base color of the shadow
 * @param inset defines of the shadow is inset of not
 */
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

/**
 * combines to shadow property values created by [shadow]
 */
infix fun ShadowProperty.and(other: ShadowProperty): ShadowProperty = "$this, $other"

/**
 * This _context_ interface offers functions to style the shadow related CSS properties of a component.
 *
 * It offers functions to define box-shadow and text-shadow properties.

 * There are overrides for all functions that enable one to define the styling for
 * the different media devices independently.
 */
interface Shadow : StyleParams {
    /**
     * This function sets the [text-shadow](https://developer.mozilla.org/de/docs/Web/CSS/text-shadow) property.
     *
     * Example call:
     * ```
     * text-shadow { flat }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     */
    fun textShadow(value: Shadows.() -> Property) = property(textShadowKey, theme().shadows, value)

    /**
     * This function sets the [text-shadow](https://developer.mozilla.org/de/docs/Web/CSS/text-shadow) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * text-shadow(
     *     sm = { flat }
     *     lg = { raised }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     */
    fun textShadow(
        sm: (Shadows.() -> Property)? = null,
        md: (Shadows.() -> Property)? = null,
        lg: (Shadows.() -> Property)? = null,
        xl: (Shadows.() -> Property)? = null
    ) =
        property(textShadowKey, theme().shadows, sm, md, lg, xl)

    /**
     * This function sets the [box-shadow](https://developer.mozilla.org/de/docs/Web/CSS/box-shadow) property.
     *
     * Example call:
     * ```
     * box-shadow { flat }
     * ```
     *
     * @param value extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     */
    fun boxShadow(value: Shadows.() -> Property) = property(boxShadowKey, theme().shadows, value)

    /**
     * This function sets the [box-shadow](https://developer.mozilla.org/de/docs/Web/CSS/box-shadow) property
     * for each media device independently.
     *
     * Example call:
     * ```
     * box-shadow(
     *     sm = { flat }
     *     lg = { raised }
     * )
     * ```
     *
     * @param sm extension function parameter for small media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     * @param md extension function parameter for medium sized media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     * @param lg extension function parameter for large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     * @param xl extension function parameter for extra large media devices, recommended to use
     *           predefined values via [dev.fritz2.styling.theme.Theme.shadows] that offer the properties of [Shadows]
     */
    fun boxShadow(
        sm: (Shadows.() -> Property)? = null,
        md: (Shadows.() -> Property)? = null,
        lg: (Shadows.() -> Property)? = null,
        xl: (Shadows.() -> Property)? = null
    ) =
        property(boxShadowKey, theme().shadows, sm, md, lg, xl)
}