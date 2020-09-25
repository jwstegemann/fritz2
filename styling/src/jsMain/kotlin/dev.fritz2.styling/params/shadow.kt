package dev.fritz2.styling.params

import dev.fritz2.styling.Property
import dev.fritz2.styling.Shadows
import dev.fritz2.styling.theme

const val textShadowKey = "text-shadow: "
const val boxShadowKey = "box-shadow: "

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