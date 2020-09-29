package dev.fritz2.styling.params

import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Shadows
import dev.fritz2.styling.theme.theme

const val textShadowKey = "text-shadow: "
const val boxShadowKey = "box-shadow: "

typealias ShadowProperty = Property

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

infix fun ShadowProperty.and(other: ShadowProperty): ShadowProperty = "$this, $other"

interface Shadow : StyleParams {
    /*
     * textShadow
     */
    fun textShadow(value: Shadows.() -> Property) = property(textShadowKey, theme().shadows, value)

    fun textShadow(
        sm: (Shadows.() -> Property)? = null,
        md: (Shadows.() -> Property)? = null,
        lg: (Shadows.() -> Property)? = null,
        xl: (Shadows.() -> Property)? = null
    ) =
        property(textShadowKey, theme().shadows, sm, md, lg, xl)

    /*
     * boxShadow
     */
    fun boxShadow(value: Shadows.() -> Property) = property(boxShadowKey, theme().shadows, value)

    fun boxShadow(
        sm: (Shadows.() -> Property)? = null,
        md: (Shadows.() -> Property)? = null,
        lg: (Shadows.() -> Property)? = null,
        xl: (Shadows.() -> Property)? = null
    ) =
        property(boxShadowKey, theme().shadows, sm, md, lg, xl)
}