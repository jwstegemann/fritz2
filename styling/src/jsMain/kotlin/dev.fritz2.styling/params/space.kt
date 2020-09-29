package dev.fritz2.styling.params

import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal const val marginKey = "margin: "
internal const val marginTopKey = "margin-top: "
internal const val marginRightKey = "margin-right: "
internal const val marginBottomKey = "margin-bottom: "
internal const val marginLeftKey = "margin-left: "

internal const val paddingKey = "padding: "
internal const val paddingTopKey = "padding-top: "
internal const val paddingRightKey = "padding-right: "
internal const val paddingBottomKey = "padding-bottom: "
internal const val paddingLeftKey = "padding-left: "

@ExperimentalCoroutinesApi
class SpacesContext(
    private val topKey: String,
    private val leftKey: String,
    private val bottomKey: String,
    private val rightKey: String,
    val styleParams: StyleParams,
    private val target: StringBuilder
) : StyleParams by styleParams {
    fun top(value: ScaledValueProperty) = property(topKey, theme().space, value, target)
    fun left(value: ScaledValueProperty) = property(leftKey, theme().space, value, target)
    fun bottom(value: ScaledValueProperty) = property(bottomKey, theme().space, value, target)
    fun right(value: ScaledValueProperty) = property(rightKey, theme().space, value, target)
    fun vertical(value: ScaledValueProperty) {
        property(topKey, theme().space, value, target)
        property(bottomKey, theme().space, value, target)
    }

    fun horizontal(value: ScaledValueProperty) {
        property(leftKey, theme().space, value, target)
        property(rightKey, theme().space, value, target)
    }
}

@ExperimentalCoroutinesApi
interface Space : StyleParams {
    /*
     * margin
     */
    fun margin(value: ScaledValueProperty) = property(marginKey, theme().space, value)

    fun margin(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(marginKey, theme().space, sm, md, lg, xl)

    /*
     * margins
     */
    fun margins(value: SpacesContext.() -> Unit) {
        SpacesContext(marginTopKey, marginLeftKey, marginBottomKey, marginRightKey, this, smProperties).value()
    }

    fun margins(
        sm: (SpacesContext.() -> Unit)? = null,
        md: (SpacesContext.() -> Unit)? = null,
        lg: (SpacesContext.() -> Unit)? = null,
        xl: (SpacesContext.() -> Unit)? = null
    ) {
        if (sm != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            smProperties
        ).sm()
        if (md != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            mdProperties
        ).md()
        if (lg != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            lgProperties
        ).lg()
        if (xl != null) SpacesContext(
            marginTopKey,
            marginLeftKey,
            marginBottomKey,
            marginRightKey,
            this,
            xlProperties
        ).xl()
    }

    /*
     * padding
     */
    fun padding(value: ScaledValueProperty) = property(paddingKey, theme().space, value)

    fun padding(
        sm: ScaledValueProperty? = null,
        md: ScaledValueProperty? = null,
        lg: ScaledValueProperty? = null,
        xl: ScaledValueProperty? = null
    ) =
        property(paddingKey, theme().space, sm, md, lg, xl)

    /*
     * paddings
     */
    fun paddings(value: SpacesContext.() -> Unit) {
        SpacesContext(paddingTopKey, paddingLeftKey, paddingBottomKey, paddingRightKey, this, smProperties).value()
    }

    fun paddings(
        sm: (SpacesContext.() -> Unit)? = null,
        md: (SpacesContext.() -> Unit)? = null,
        lg: (SpacesContext.() -> Unit)? = null,
        xl: (SpacesContext.() -> Unit)? = null
    ) {
        if (sm != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            smProperties
        ).sm()
        if (md != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            mdProperties
        ).md()
        if (lg != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            lgProperties
        ).lg()
        if (xl != null) SpacesContext(
            paddingTopKey,
            paddingLeftKey,
            paddingBottomKey,
            paddingRightKey,
            this,
            xlProperties
        ).xl()
    }
}
